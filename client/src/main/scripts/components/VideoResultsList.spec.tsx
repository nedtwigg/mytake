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
import VideoResultPreview from "./VideoResultPreview";
import { kennedyNixon } from "../utils/testUtils";
import { VideoResultsList, VideoResultsListProps } from "./VideoResultsList";
import { exception } from "console";

test("VideoResultsList social security", () => {
  const resultsList: VideoResultsListProps = {
    searchTerm: "social security",
    results: {
      facts: [
        { turn: 102, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 106, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 116, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 26, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 101, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 98, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 13, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 81, hash: "ct8Gai5QMo1gqK6c5h3SBTZIpMnG_7jMcKCgpMucSWo=" },
        { turn: 54, hash: "9Ei2jkqRyt5vw9_0BnQ4vXeXgpm8ODgreLgFS7mEInM=" },
        { turn: 52, hash: "9Ei2jkqRyt5vw9_0BnQ4vXeXgpm8ODgreLgFS7mEInM=" },
        { turn: 31, hash: "9Ei2jkqRyt5vw9_0BnQ4vXeXgpm8ODgreLgFS7mEInM=" },
        { turn: 108, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 59, hash: "QBMMV1cOwoY18eVsm5JE8INSvKwGkHzr3WfWAach_Aw=" },
        { turn: 79, hash: "ct8Gai5QMo1gqK6c5h3SBTZIpMnG_7jMcKCgpMucSWo=" },
        { turn: 104, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 78, hash: "ct8Gai5QMo1gqK6c5h3SBTZIpMnG_7jMcKCgpMucSWo=" },
        { turn: 60, hash: "QBMMV1cOwoY18eVsm5JE8INSvKwGkHzr3WfWAach_Aw=" },
        { turn: 50, hash: "9Ei2jkqRyt5vw9_0BnQ4vXeXgpm8ODgreLgFS7mEInM=" },
        { turn: 99, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 21, hash: "ybl8jZ3k0SWZZGC_OtRYLl0zGRuNlm42qaEu7wxFvfo=" },
        { turn: 112, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 38, hash: "SbhZ_6P0LahGQXyGaIQHFXB9YkoigsHApb-SDCNZk-4=" },
        { turn: 61, hash: "QBMMV1cOwoY18eVsm5JE8INSvKwGkHzr3WfWAach_Aw=" },
        { turn: 11, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 80, hash: "ct8Gai5QMo1gqK6c5h3SBTZIpMnG_7jMcKCgpMucSWo=" },
        { turn: 56, hash: "SbhZ_6P0LahGQXyGaIQHFXB9YkoigsHApb-SDCNZk-4=" },
        { turn: 24, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 57, hash: "QBMMV1cOwoY18eVsm5JE8INSvKwGkHzr3WfWAach_Aw=" },
        { turn: 88, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 56, hash: "QBMMV1cOwoY18eVsm5JE8INSvKwGkHzr3WfWAach_Aw=" },
        { turn: 82, hash: "ct8Gai5QMo1gqK6c5h3SBTZIpMnG_7jMcKCgpMucSWo=" },
        { turn: 50, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 63, hash: "QBMMV1cOwoY18eVsm5JE8INSvKwGkHzr3WfWAach_Aw=" },
        { turn: 9, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 23, hash: "ybl8jZ3k0SWZZGC_OtRYLl0zGRuNlm42qaEu7wxFvfo=" },
        { turn: 39, hash: "SbhZ_6P0LahGQXyGaIQHFXB9YkoigsHApb-SDCNZk-4=" },
        { turn: 49, hash: "SbhZ_6P0LahGQXyGaIQHFXB9YkoigsHApb-SDCNZk-4=" },
        { turn: 70, hash: "SbhZ_6P0LahGQXyGaIQHFXB9YkoigsHApb-SDCNZk-4=" },
        { turn: 110, hash: "1fPLAadIV8L4-PjT6yF63KyEjfieDpZbGfGNiQN3Q7c=" },
        { turn: 78, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 7, hash: "ybl8jZ3k0SWZZGC_OtRYLl0zGRuNlm42qaEu7wxFvfo=" },
        { turn: 110, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 72, hash: "SbhZ_6P0LahGQXyGaIQHFXB9YkoigsHApb-SDCNZk-4=" },
        { turn: 19, hash: "ybl8jZ3k0SWZZGC_OtRYLl0zGRuNlm42qaEu7wxFvfo=" },
        { turn: 9, hash: "Lz55WCSZ9K3GVqgrtTV09n_zdlymUPVajCnkPhaqOuY=" },
        { turn: 34, hash: "orUj1nSeeyotMbBe4RH-beskEDldBQD7reYiKbhhx5A=" },
        { turn: 133, hash: "ybl8jZ3k0SWZZGC_OtRYLl0zGRuNlm42qaEu7wxFvfo=" },
        { turn: 51, hash: "9Ei2jkqRyt5vw9_0BnQ4vXeXgpm8ODgreLgFS7mEInM=" },
        { turn: 20, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 56, hash: "9Ei2jkqRyt5vw9_0BnQ4vXeXgpm8ODgreLgFS7mEInM=" },
        { turn: 132, hash: "vrhLapmIbWECYassLC2Umf7Z16fusYgWWGhTP7KgIYU=" },
        { turn: 1, hash: "SbhZ_6P0LahGQXyGaIQHFXB9YkoigsHApb-SDCNZk-4=" },
      ],
    },
  };
  const tree = renderer
    .create(new VideoResultsList(resultsList).render())
    .toJSON();
  expect(tree).toMatchSnapshot();
});
