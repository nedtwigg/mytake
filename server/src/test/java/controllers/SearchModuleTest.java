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
package controllers;

import com.jsoniter.JsonIterator;
import common.CustomAssets;
import common.JoobyDevRule;
import common.Snapshot;
import io.restassured.RestAssured;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import json.JsoniterModule;
import org.jooby.Jooby;
import org.junit.ClassRule;
import org.junit.Test;

public class SearchModuleTest {
	@ClassRule
	public static JoobyDevRule app = new JoobyDevRule(new Jooby() {
		{
			CustomAssets.initTemplates(this);
			use(new JsoniterModule());
			use(new SearchModule());
		}
	});

	@Test
	public void html() {
		Snapshot.match("html", RestAssured.get("/search?q=cuba"));
	}

	@Test
	public void api() throws IOException {
		Path hashFile = Paths.get("../client/src/main/scripts/components/search/search-index.json");
		String hash = JsonIterator.deserialize(Files.readAllBytes(hashFile)).toString("hash");
		Snapshot.match("api", RestAssured.get("/api/static/search?q=cuba&h=e37375809df9dd1259b247ea7ee094b60dfd88cc"));
		RestAssured.get("/api/static/search?q=cuba&h=hashdoesntmatch").then()
				.header("Cache-Control", "no-cache");
		RestAssured.get("/api/static/search?q=cuba&h=" + hash).then()
				.header("Cache-Control", "public, max-age=31536000, immutable");
	}
}
