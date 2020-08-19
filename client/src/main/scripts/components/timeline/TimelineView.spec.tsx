/*
 * MyTake.org website and tooling.
 * Copyright (C) 2017-2020 MyTake.org, Inc.
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
import { documentFactLink, videoFactLink } from "../../utils/testUtils";
import TimelineView from "./TimelineView";

jest.mock("./TimelinePreviewContainer", () => ({
  __esModule: true,
  default: "TimelinePreviewContainer",
}));

jest.mock("./Timeline", () => ({
  __esModule: true,
  default: "Timeline",
}));

jest.mock("./TimelineRadioButtons", () => ({
  __esModule: true,
  default: "TimelineRadioButtons",
}));

test("Successfully loaded a Document in view", () => {
  const tree = renderer
    .create(
      <TimelineView factLinks={[documentFactLink]} path={"/foundation"} />
    )
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test("Successfully loaded a Video in view", () => {
  const tree = renderer
    .create(<TimelineView factLinks={[videoFactLink]} path={"/foundation"} />)
    .toJSON();
  expect(tree).toMatchSnapshot();
});
