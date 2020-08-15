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

export function socialHeader(arg: string): React.ReactElement {
  return <SocialHeader embed={arg} />;
}

export type SocialHeaderProps = {
  embed: string;
};

export const SocialHeader: React.FC<SocialHeaderProps> = (props) => {
  return (
    <Twitter
      title={props.embed}
      desc={props.embed}
      image={props.embed}
      imageAlt={props.embed}
    />
  );
};

// https://developer.twitter.com/en/docs/twitter-for-websites/cards/overview/summary-card-with-large-image
interface TwitterProps {
  /** Needs to start with @ */
  creator?: string;
  title: string;
  /** A description that concisely summarizes the content as appropriate for presentation within a Tweet. You should not re-use the title as the description or use this field to describe the general services provided by the website. Platform specific behaviors:
        iOS, Android: Not displayed
        Web: Truncated to three lines in timeline and expanded Tweet */
  desc: string;
  image: string;
  /** Max 420 characters. */
  imageAlt: string;
}

const Twitter: React.FC<TwitterProps> = (props) => {
  return (
    <header>
      <meta name="twitter:card" content="summary_large_image" />
      <meta name="twitter:site" content="@mytakedotorg" />
      {props.creator && <meta name="twitter:creator" content={props.creator} />}
      <meta name="twitter:title" content={props.title} />
      <meta name="twitter:description" content={props.desc} />
      <meta name="twitter:image" content={props.image} />
      <meta name="twitter:image:alt" content={props.imageAlt} />
    </header>
  );
};