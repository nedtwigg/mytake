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
 * You can contact us at team@mytake.org
 */
package org.mytake.lucene;

import com.diffplug.common.collect.ImmutableSet;
import com.diffplug.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java2ts.Search.FactResultList;
import java2ts.Search.VideoResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.QueryBuilder;

public class Lucene implements AutoCloseable {
	public static final String INDEX_ARCHIVE = "foundation-index.zip";

	/** Extracts the index from an archive, then instantiates Lucene on top of that. */
	public static Lucene openFromArchive() throws IOException {
		Path luceneIndexDir = Files.createTempDirectory("lucene-index");
		try (InputStream input = Resources.asByteSource(Lucene.class.getResource("/" + INDEX_ARCHIVE)).openBufferedStream()) {
			ZipMisc.unzip(input, luceneIndexDir);
		}
		return new Lucene(luceneIndexDir) {
			@Override
			public void close() throws IOException {
				super.close();
				ZipMisc.deleteDir(luceneIndexDir);
			}
		};
	}

	private final MyTakeDotOrgAnalyzer analyzer;
	private final Directory directory;
	private final DirectoryReader reader;
	private final IndexSearcher searcher;

	public Lucene(Path path) throws IOException {
		analyzer = new MyTakeDotOrgAnalyzer();
		directory = new MMapDirectory(path);
		reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
	}

	@Override
	public void close() throws IOException {
		reader.close();
		directory.close();
		analyzer.close();
	}

	static class NextRequest {
		TreeSet<String> clauses = new TreeSet<>();

		NextRequest(String q) {
			String[] clausesRaw = q.split(",", -1);
			for (String clause : clausesRaw) {
				String trimmed = clause.trim().toLowerCase(Locale.ROOT);
				if (trimmed.isEmpty()) {
					continue;
				}
				if (trimmed.charAt(0) == '-') {
					// these are filtered out on the client
				} else {
					clauses.add(trimmed);
				}
			}
		}
	}

	public FactResultList searchDebate(String query) throws IOException {
		NextRequest next = new NextRequest(query);
		return searchDebate(next);
	}

	FactResultList searchDebate(NextRequest request) throws IOException {
		BooleanQuery.Builder finalQuery = new BooleanQuery.Builder();
		{
			QueryBuilder phraseParser = new QueryBuilder(analyzer);
			for (String clause : request.clauses) {
				finalQuery.add(phraseParser.createPhraseQuery(CONTENT, clause), Occur.SHOULD);
			}
		}

		List<Document> queryResults = runQuery(finalQuery.build());
		FactResultList resultList = new FactResultList();
		resultList.facts = new ArrayList<>(queryResults.size());
		for (int i = 0; i < queryResults.size(); ++i) {
			Document document = queryResults.get(i);
			VideoResult result = new VideoResult();
			result.hash = document.get(HASH);
			result.turn = Integer.parseInt(document.get(TURN));
			resultList.facts.add(result);
		}
		return resultList;
	}

	private static final int MAX_RESULTS = 100;

	private List<Document> runQuery(Query query) throws IOException {
		TopDocs topDocs = searcher.search(query, MAX_RESULTS);
		List<Document> docs = new ArrayList<>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			docs.add(searcher.doc(scoreDoc.doc, TO_FETCH));
		}
		return docs;
	}

	public static final String HASH = "hash";
	public static final String TURN = "turn";
	public static final String DATE = "date";
	public static final String SPEAKER = "speaker";
	public static final String CONTENT = "content";

	private static final ImmutableSet<String> TO_FETCH = ImmutableSet.of(HASH, TURN);

	public static class MyTakeDotOrgAnalyzer extends Analyzer {
		private static final int MAX_TOKEN_LENGTH = 127;
		private static final CharArraySet STOPWORDS;

		static {
			boolean ignoreCase = true;
			STOPWORDS = new CharArraySet(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET, ignoreCase);
			STOPWORDS.add("uh");
			STOPWORDS.add("eh");
		}

		@Override
		protected TokenStreamComponents createComponents(final String fieldName) {
			StandardTokenizer src = new StandardTokenizer();
			src.setMaxTokenLength(MAX_TOKEN_LENGTH);
			TokenStream tok = src;
			tok = new LowerCaseFilter(tok);
			tok = new StopFilter(tok, STOPWORDS);
			return new TokenStreamComponents(reader -> {
				// So that if maxTokenLength was changed, the change takes
				// effect next time tokenStream is called:
				src.setMaxTokenLength(MAX_TOKEN_LENGTH);
				src.setReader(reader);
			}, tok);
		}

		@Override
		protected TokenStream normalize(String fieldName, TokenStream in) {
			return new LowerCaseFilter(in);
		}
	}
}
