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
import * as React from "react";
import * as renderer from "react-test-renderer";
import { VideoResultsList } from "./VideoResultsList";
import { SearchWithData, searchImpl } from "./database/SearchDatabaseApi";
import { FoundationDataTestBuilder } from "./../../utils/foundationData/FoundationDataTestBuilder";

// this data is generated by server/src/test/java/controllers/LucenePlayground.java
import socialSecuritySearchResults from "./testData/socialSecuritySearchResults.json";

test("VideoResultsList social security", () => {
  const result = searchImpl(
    new SearchWithData(
      "social security",
      socialSecuritySearchResults.facts,
      FoundationDataTestBuilder.loadAllFromDisk()
    )
  );

  const tree = renderer
    .create(
      new VideoResultsList({
        searchResult: result,
      }).render()
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test("VideoResultsList no results", () => {
  const result = searchImpl(
    new SearchWithData(
      "gibberish",
      [],
      FoundationDataTestBuilder.loadAllFromDisk()
    )
  );
  const tree = renderer
    .create(
      new VideoResultsList({
        searchResult: result,
      }).render()
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

// 415-714-5328