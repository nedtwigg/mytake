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
import React from "react";
import renderer from "react-test-renderer";
import { Foundation } from "../../common/foundation";
import { VideoResultProps } from "../shared/VideoResult";
import { SearchMode, _searchImpl, _SearchWithData } from "./search";
// this data is generated by server/src/test/java/controllers/LucenePlayground.java
import socialSecuritySearchResults from "./testData/socialSecuritySearchResults.json";
import wallSearchResults from "./testData/wallSearchResults.json";
import VideoResultsList, {
  VideoResultsListEventHandlers,
} from "./VideoResultsList";

jest.mock("../shared/VideoResult", () => ({
  __esModule: true,
  default: (props: VideoResultProps) => {
    const { videoTurn, videoFact } = props;
    return (
      <div>
        VideoResult: {videoFact.youtubeId} {videoTurn.turn} {videoTurn.cut}
      </div>
    );
  },
}));

jest.mock("../auth/LoginModal", () => ({
  __esModule: true,
  default: "LoginModal",
}));

const dateToDivMap: Map<string, HTMLDivElement> = new Map();
const eventHandlers: VideoResultsListEventHandlers = {
  onAddBookmark: jest.fn(),
  onRemoveBookmark: jest.fn(),
  onPlayClick: jest.fn(),
};

test("VideoResultsList social security", async () => {
  const result = _searchImpl(
    new _SearchWithData(
      "social security",
      socialSecuritySearchResults.facts,
      await Foundation.fetchAll(
        socialSecuritySearchResults.facts.map((fact) => fact.hash)
      ),
      SearchMode.BeforeAndAfter
    )
  );

  const tree = renderer
    .create(
      <VideoResultsList
        bookmarks={[]}
        dateToDivMap={dateToDivMap}
        eventHandlers={eventHandlers}
        searchResult={result}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test("VideoResultsList no results", async () => {
  const result = _searchImpl(
    new _SearchWithData(
      "gibberish",
      [],
      await Foundation.fetchAll([]),
      SearchMode.BeforeAndAfter
    )
  );
  const tree = renderer
    .create(
      <VideoResultsList
        bookmarks={[]}
        dateToDivMap={dateToDivMap}
        eventHandlers={eventHandlers}
        searchResult={result}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test("VideoResultsList wall, -wall street", async () => {
  const result = _searchImpl(
    new _SearchWithData(
      "wall, -wall street",
      wallSearchResults.facts,
      await Foundation.fetchAll(
        wallSearchResults.facts.map((fact) => fact.hash)
      ),
      SearchMode.BeforeAndAfter
    )
  );

  const tree = renderer
    .create(
      <VideoResultsList
        bookmarks={[]}
        dateToDivMap={dateToDivMap}
        eventHandlers={eventHandlers}
        searchResult={result}
      />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});
