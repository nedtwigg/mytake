/*
 * MyTake.org website and tooling.
 * Copyright (C) 2020 MyTake.org, Inc.
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
package org.mytake.factset.search;

import com.diffplug.common.collect.ImmutableList;
import com.diffplug.common.collect.ImmutableMap;
import com.jsoniter.spi.TypeLiteral;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java2ts.FT;
import java2ts.FT.FactLink;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.mytake.factset.JsonMisc;
import org.mytake.factset.video.VideoFactContentJava;

public class GenerateIndex {
	public static ImmutableList<String> REPOS = ImmutableList.of("us-founding-documents", "us-presidential-debates");

	public static void main(String[] args) throws IOException {
		ImmutableMap<String, String> repoToSha = ImmutableMap.of(
				"us-presidential-debates",
				"c8833250cf42ad80565a923e0074b3c8597f915c");
		Path luceneTemp = Files.createTempDirectory("mytake-lucene");
		generate(luceneTemp, repoToSha);
	}

	public static void generate(Path luceneIndex, Map<String, String> repoToSha) throws IOException {
		OkHttpClient client = new OkHttpClient.Builder()
				.cache(new Cache(
						new File("build/fact-cache"),
						10L * 1024L * 1024L // 100 MiB
				))
				.build();
		try (LuceneWriter writer = new LuceneWriter(luceneIndex)) {
			for (Map.Entry<String, String> factset : repoToSha.entrySet()) {
				String repo = factset.getKey();
				String sha = factset.getValue();
				List<FactLink> factLinks;
				try (Response res = get(client, "https://raw.githubusercontent.com/mytakedotorg/" + repo + "/" + sha + "/sausage/index.json")) {
					factLinks = JsonMisc.fromJson(res.body().bytes(), FACTLINKS);
				}
				int id = REPOS.indexOf(repo);
				for (FactLink factLink : factLinks) {
					try (Response res = get(client, "https://mytake.org/api/fact/" + id + "/" + factLink.hash)) {
						System.out.println("for " + factLink.fact.title);
						FT.VideoFactContentEncoded encoded = JsonMisc.fromJson(res.body().bytes(), FT.VideoFactContentEncoded.class);
						writer.writeVideo(factLink.hash, VideoFactContentJava.decode(encoded));
					}
				}
			}
		}
	}

	private static okhttp3.Response get(OkHttpClient client, String url) throws IOException {
		return client.newCall(new okhttp3.Request.Builder().url(url).build()).execute();
	}

	private static TypeLiteral<List<FactLink>> FACTLINKS = new TypeLiteral<List<FactLink>>() {};
}