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
import { INFO_HEADER_TABS_ENUM } from "./infoHeader";
import Tabs from "./Tabs";

test("Tabs - visible", () => {
  const props = {
    activeTab: INFO_HEADER_TABS_ENUM.WHAT_IS_THIS,
    onClose: jest.fn(),
    onTabClick: jest.fn(),
    isVisible: true,
  };
  const tree = renderer.create(<Tabs {...props} />).toJSON();
  expect(tree).toMatchSnapshot();
});

test("Tabs - hidden", () => {
  const props = {
    activeTab: INFO_HEADER_TABS_ENUM.WHAT_IS_THIS,
    onClose: jest.fn(),
    onTabClick: jest.fn(),
    isVisible: false,
  };
  const tree = renderer.create(<Tabs {...props} />).toJSON();
  expect(tree).toMatchSnapshot();
});
