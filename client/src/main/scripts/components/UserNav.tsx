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
import * as React from "react";
import { ChevronDown } from "react-feather";
import { getUserCookieString } from "../browser";
import { LoginCookie } from "../java2ts/LoginCookie";
import { Routes } from "../java2ts/Routes";
import DropDown from "./DropDown";

interface UserNavProps {}

const UserNav: React.FC<UserNavProps> = () => {
  const cookieString = getUserCookieString(); // Not pure
  let cookie = cookieString
    ? (JSON.parse(JSON.parse(cookieString)) as LoginCookie)
    : null;
  let navLinks: { name: string; href: string }[] = [];
  if (cookie) {
    navLinks = [
      { name: "New Draft", href: Routes.DRAFTS_NEW },
      { name: "Drafts", href: Routes.DRAFTS },
      { name: "Published", href: `/${cookie.username}` },
      {
        name: "Stars",
        href: `/${cookie.username}?${Routes.PROFILE_TAB}=${Routes.PROFILE_TAB_STARS}`,
      },
      {
        name: "Profile",
        href: `/${cookie.username}?${Routes.PROFILE_TAB}=${Routes.PROFILE_TAB_EDIT}`,
      },
      { name: "Logout", href: Routes.LOGOUT },
    ];
  }

  return (
    <DropDown
      classModifier="usernav"
      disabled={!cookie}
      dropdownPosition="BL"
      toggleText={
        cookie ? (
          <>
            <ChevronDown />
            {cookie.username}
          </>
        ) : (
          <a href={Routes.LOGIN}>Login</a>
        )
      }
    >
      <ul className="usernav__list">
        {navLinks.map(({ name, href }) => {
          return (
            <li className="usernav__list-item">
              <a className="usernav__link" href={href}>
                {name}
              </a>
            </li>
          );
        })}
      </ul>
    </DropDown>
  );
};

export default UserNav;
