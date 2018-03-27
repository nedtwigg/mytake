/*
 * MyTake.org
 *
 *  Copyright 2017 by its authors.
 *  Some rights reserved. See LICENSE, https://github.com/mytakedotorg/mytakedotorg/graphs/contributors
 */
package org.mytake.foundation.transcript;

import com.diffplug.common.base.Preconditions;
import com.diffplug.common.base.StringPrinter;
import com.diffplug.common.base.Unhandled;
import com.diffplug.common.io.CharSink;
import com.diffplug.common.io.CharSource;
import com.diffplug.common.io.Files;
import com.google.auto.value.AutoValue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.eclipse.jgit.diff.Edit;

@AutoValue
public abstract class VttTranscript {
	public enum Mode {
		PERMISSIVE, STRICT;
	}

	public abstract String header();

	public abstract List<Line> lines();

	public List<Word.Vtt> words() {
		List<Word.Vtt> words = new ArrayList<>();
		for (Line line : lines()) {
			line.addWords(words);
		}
		return words;
	}

	@AutoValue
	public static abstract class Line {
		public abstract LineHeader header();

		public abstract List<VttToken> tokens();

		void addWords(List<Word.Vtt> words) {
			double time = header().start();
			for (VttToken token : tokens()) {
				if (token.isTime()) {
					time = token.assertTime().timestamp;
				} else if (token.isWord()) {
					String rawWord = token.assertWord().word.trim();
					if (!rawWord.isEmpty()) {
						Word.Vtt word = new Word.Vtt(rawWord, time);
						if (!word.lowercase.isEmpty()) {
							words.add(word);
						}
					}
				}
			}
		}

		List<Word.Vtt> wordsNoTimestamp() {
			List<Word.Vtt> words = new ArrayList<>(tokens().size() / 2 + 2);
			for (VttToken token : tokens()) {
				if (token.isWord()) {
					words.add(new Word.Vtt(token.assertWord().word, Double.NaN));
				}
			}
			return words;
		}

		public static Line create(LineHeader template, List<Word.Vtt> words, double nextStart) {
			Iterator<Word.Vtt> wordIter = words.iterator();
			Word.Vtt word = wordIter.next();

			double start = word.time;
			LineHeader header = new AutoValue_VttTranscript_LineHeader(start, nextStart, template.formatting());
			List<VttToken> tokens = new ArrayList<>(words.size() * 2);

			tokens.add(new VttToken.Word(word.lowercase));
			while (wordIter.hasNext()) {
				word = wordIter.next();
				tokens.add(new VttToken.Time(word.time));
				tokens.add(new VttToken.Word(" " + word.lowercase));
			}
			tokens.add(new VttToken.UnexpectedClose());
			return new AutoValue_VttTranscript_Line(header, tokens);
		}
	}

	@AutoValue
	public static abstract class LineHeader {
		public abstract double start();

		public abstract double end();

		public abstract String formatting();

		private static final String ARROW = " --> ";

		public static LineHeader parse(String input) {
			int arrowIdx = input.indexOf(ARROW);
			double start = str2ts(input.substring(0, arrowIdx));
			int nextStart = arrowIdx + ARROW.length();
			int spaceIdx = input.indexOf(' ', nextStart);
			double end = str2ts(input.substring(nextStart, spaceIdx));
			String formatting = input.substring(spaceIdx + 1);
			return new AutoValue_VttTranscript_LineHeader(start, end, formatting);
		}

		public String asString() {
			return ts2str(start()) + ARROW + ts2str(end()) + " " + formatting();
		}
	}

	public static VttTranscript parse(File file, Mode mode) throws IOException {
		return parse(Files.asByteSource(file).asCharSource(StandardCharsets.UTF_8), mode);
	}

	public static VttTranscript parse(CharSource charSource, Mode mode) throws IOException {
		int lineCount = 1;
		try (BufferedReader reader = charSource.openBufferedStream()) {
			StringBuilder headerStr = new StringBuilder();
			String line;
			while (!(line = reader.readLine()).isEmpty()) {
				headerStr.append(line);
				headerStr.append("\n");
				++lineCount;
			}

			List<Line> lines = new ArrayList<>();
			while (true) {
				String headerLine = reader.readLine();
				String tokenLine = reader.readLine();
				if (tokenLine == null) {
					break;
				}

				++lineCount;
				LineHeader header = LineHeader.parse(headerLine);
				++lineCount;
				List<VttToken> tokens;
				if (mode == Mode.STRICT) {
					tokens = VttToken.parseLine(tokenLine);
				} else if (mode == Mode.PERMISSIVE) {
					tokens = parseLineWithTimestamp(tokenLine);
					while (tokens == null) {
						++lineCount;
						line = reader.readLine();
						if (line == null) {
							break;
						}
						tokens = parseLineWithTimestamp(line);
					}
				} else {
					throw Unhandled.enumException(mode);
				}
				if (tokens != null) {
					lines.add(new AutoValue_VttTranscript_Line(header, tokens));
				}

				++lineCount;
				String emptyLine = reader.readLine();
				if (emptyLine == null) {
					break;
				} else {
					Preconditions.checkArgument(emptyLine.isEmpty());
				}
			}
			return new AutoValue_VttTranscript(headerStr.toString(), lines);
		} catch (Exception e) {
			throw new RuntimeException("On line " + lineCount, e);
		}
	}

	@Nullable
	private static List<VttToken> parseLineWithTimestamp(String line) {
		try {
			List<VttToken> tokens = VttToken.parseLine(line);
			for (VttToken token : tokens) {
				if (token.isTime()) {
					return tokens;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public String asString() {
		return StringPrinter.buildString(printer -> {
			printer.println(header());
			Iterator<Line> lineIter = lines().iterator();
			while (lineIter.hasNext()) {
				Line line = lineIter.next();
				printer.println(line.header().asString());
				printer.println(VttToken.lineAsString(line.tokens()));
				if (lineIter.hasNext()) {
					printer.println("");
				}
			}
		});
	}

	public VttTranscript save(List<Word.Vtt> newVtt, CharSink charSink) throws IOException {
		List<Word.Vtt> remainder = newVtt;
		List<Line> newLines = new ArrayList<>();
		for (VttTranscript.Line line : lines()) {
			List<Word.Vtt> words = line.wordsNoTimestamp();

			// allow 4 - 1 = 3 insertions per line at most.  otherwise, it might match the
			// end of this line's words with the end of the entire remainder
			int maxEligibleCutpoint = Math.min(words.size() + 4, remainder.size());
			List<Word.Vtt> eligibleRemainder = remainder.subList(0, maxEligibleCutpoint);
			List<Edit> edits = TranscriptMatch.edits(words, eligibleRemainder);

			List<Word.Vtt> newWords;
			if (edits.isEmpty()) {
				newWords = eligibleRemainder;
				remainder = remainder.subList(maxEligibleCutpoint, remainder.size());
			} else {
				// cut at the beginning of the last edit
				int cutpoint = edits.get(edits.size() - 1).getBeginB();
				newWords = remainder.subList(0, cutpoint);
				remainder = remainder.subList(cutpoint, remainder.size());
			}
			if (newWords.isEmpty()) {
				continue;
			}
			if (!remainder.isEmpty()) {
				newLines.add(Line.create(line.header(), newWords, remainder.get(0).time));
			} else {
				newLines.add(Line.create(line.header(), newWords, newWords.get(newWords.size() - 1).time + LAST_WORD_DURATION));
				break;
			}
			if (remainder.isEmpty()) {
				break;
			}
		}
		if (!remainder.isEmpty()) {
			newLines.add(Line.create(lines().get(lines().size() - 1).header(), remainder, remainder.get(remainder.size() - 1).time + LAST_WORD_DURATION));
		}
		VttTranscript newTranscript = new AutoValue_VttTranscript(header(), newLines);
		charSink.write(newTranscript.asString());
		return newTranscript;
	}

	public static final double LAST_WORD_DURATION = 2.0;

	static String ts2str(double ts) {
		int totalMinutes = (int) Math.floor(ts / 60.0);
		int ms = (int) Math.round((ts - totalMinutes * 60) * 1000.0);
		int MS = ms % 1000;
		int SS = ms / 1000;
		int MM = totalMinutes % 60;
		int HH = totalMinutes / 60;
		return String.format("%02d:%02d:%02d.%03d", HH, MM, SS, MS);
	}

	static double str2ts(String str) {
		String[] pieces = str.split(":");
		// parse data string in form HH:MM:SS.SSS
		int HH = Integer.parseInt(pieces[0]);
		int MM = Integer.parseInt(pieces[1]);
		double SS = Double.parseDouble(pieces[2]);
		// convert HHMMSS to seconds
		return HH * 60 * 60 + MM * 60 + SS;
	}
}
