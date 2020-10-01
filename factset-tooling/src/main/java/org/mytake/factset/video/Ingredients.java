/*
 * MyTake.org website and tooling.
 * Copyright (C) 2018-2020 MyTake.org, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with Eclipse SWT (or a modified version of that library), containing parts
 * covered by the terms of the Eclipse Public License, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 * {Corresponding Source for a non-source form of such a combination shall include the
 * source code for the parts of Eclipse SWT used as well as that of the covered work.}
 *
 * You can contact us at team@mytake.org
 */
package org.mytake.factset.video;


import com.diffplug.common.base.Preconditions;
import com.diffplug.common.collect.ImmutableSet;
import com.diffplug.common.collect.SetMultimap;
import com.diffplug.common.collect.TreeMultimap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java2ts.FT;
import org.mytake.factset.JsonMisc;
import org.mytake.factset.video.VttTranscript.Mode;

/**
 * A folder of transcript data. `all_people.ini` is a list of every
 * person in every transcript, and `all_roles.ini` is a list of every role.
 * 
 * When loading a video, the first file loaded is `name.json`.  The people
 * and roles in that json file are checked against `all_people.ini` and
 * `all_roles.ini`. Next, `name.said` is loaded, which checks that every
 * person named in the transcript is listed in the json file.
 */
public class Ingredients {
	private final File root;
	private final Set<String> people;
	private final Set<String> roles;

	public Ingredients(File root) throws IOException {
		this.root = Objects.requireNonNull(root);
		this.people = SetStoredAsIni.parse(new File(root, "all_people.ini"));
		this.roles = SetStoredAsIni.parse(new File(root, "all_roles.ini"));
	}

	public static final ImmutableSet<String> VIDEO_EXTENSIONS = ImmutableSet.of("json", "said", "vtt");

	private SetMultimap<String, String> possibleTitles() {
		SetMultimap<String, String> map = TreeMultimap.create();
		for (File file : root.listFiles()) {
			String fileName = file.getName();
			int dot = fileName.lastIndexOf('.');
			if (dot != -1) {
				String name = fileName.substring(0, dot);
				String ext = fileName.substring(dot + 1);
				if (VIDEO_EXTENSIONS.contains(ext)) {
					map.put(name, ext);
				}
			}
		}
		return map;
	}

	/** Has all the files needed for syncing. */
	public List<String> transcripts() {
		SetMultimap<String, String> map = possibleTitles();
		List<String> result = new ArrayList<>(map.size());
		for (Map.Entry<String, Collection<String>> entry : map.asMap().entrySet()) {
			if (entry.getValue().containsAll(VIDEO_EXTENSIONS)) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	/** Has some of the files needed for syncing, but others are missing. */
	public List<String> incompleteTranscripts() {
		SetMultimap<String, String> map = possibleTitles();
		List<String> result = new ArrayList<>(map.size());
		for (Map.Entry<String, Collection<String>> entry : map.asMap().entrySet()) {
			if (!entry.getValue().containsAll(VIDEO_EXTENSIONS)) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	/** Returns every transcript that has a meta file. */
	public List<String> transcriptsWithMeta() {
		SetMultimap<String, String> map = possibleTitles();
		List<String> result = new ArrayList<>(map.size());
		for (Map.Entry<String, Collection<String>> entry : map.asMap().entrySet()) {
			if (entry.getValue().contains("json")) {
				result.add(entry.getKey());
			}
		}
		return result;
	}

	/** Loads the given transcript. */
	public TranscriptMatch loadTranscript(String name) throws IOException {
		// load and validate the json speakers
		FT.VideoFactMeta meta = loadMetaNoValidation(name);
		for (FT.Speaker speaker : meta.speakers) {
			Preconditions.checkArgument(people.contains(speaker.fullName), "Unknown person: %s", speaker.fullName);
			Preconditions.checkArgument(roles.contains(speaker.role), "Unknown role: %s", speaker.role);
		}
		// load the transcripts
		SaidTranscript said = SaidTranscript.parse(meta, fileSaid(name));
		VttTranscript vtt = VttTranscript.parse(fileVtt(name), Mode.STRICT);
		return new TranscriptMatch(meta, said, vtt);
	}

	/** Loads transcript metadata without any validation. */
	public FT.VideoFactMeta loadMetaNoValidation(String name) throws IOException {
		return JsonMisc.fromJson(fileMeta(name), FT.VideoFactMeta.class);
	}

	File fileMeta(String name) {
		return new File(root, name + ".json");
	}

	File fileSaid(String name) {
		return new File(root, name + ".said");
	}

	public File fileVtt(String name) {
		return new File(root, name + ".vtt");
	}

	/** Given a json/said/vtt/anything, this returns the name appropriate for the methods fileXXX() */
	public String name(Path source) {
		String name = root.toPath().relativize(source).toString();
		int lastDot = name.lastIndexOf('.');
		return lastDot == -1 ? name : name.substring(0, lastDot);
	}
}