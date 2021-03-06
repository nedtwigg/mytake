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
import React, { useState } from "react";
import { VideoCut } from "../../common/social/social";
import { FT } from "../../java2ts/FT";
import { Bookmark } from "../bookmarks/bookmarks";
import SearchBar from "../SearchBar";
import VideoLite from "../VideoLite";
import NGramViewer from "./NGramViewer";
import { SearchMode, SearchResult } from "./search";
import SearchRadioButtons from "./SearchRadioButtons";
import VideoResultsList from "./VideoResultsList";

export interface SearchContainerEventHandlers {
  onModeChange(mode: SearchMode): void;
  onAddBookmark(bookmark: Bookmark): void;
  onRemoveBookmark(bookmark: Bookmark): void;
}

interface SearchContainerProps {
  bookmarks: Bookmark[];
  mode: SearchMode;
  searchResult: SearchResult;
  eventHandlers: SearchContainerEventHandlers;
}

interface VideoPlayerState {
  isVideoPlaying: boolean;
  videoProps?: {
    videoId: string;
    clipRange: [number, number];
  };
}

const dateToDivMap: Map<string, HTMLDivElement> = new Map();

const SearchContainer: React.FC<SearchContainerProps> = ({
  bookmarks,
  mode,
  eventHandlers,
  searchResult,
}) => {
  const { factHits, searchQuery } = searchResult;
  const [videoPlayerState, setVideoPlayerState] = useState<VideoPlayerState>({
    isVideoPlaying: false,
  });

  const handleBarClick = (year: string) => {
    for (const [date, div] of dateToDivMap) {
      if (date.substring(0, 4) === year) {
        const y =
          div.getBoundingClientRect().top -
          getStickyHeaderHeight() +
          window.pageYOffset;
        scrollTo(y, () => {
          div.classList.toggle("results__preview--fade");
          setTimeout(() => {
            div.classList.toggle("results__preview--fade");
          }, 500);
        });
        break;
      }
    }
  };
  const handlePlayClick = (
    videoFact: FT.VideoFactContent,
    social: VideoCut
  ) => {
    setVideoPlayerState({
      isVideoPlaying: true,
      videoProps: {
        videoId: videoFact.youtubeId,
        clipRange: social.cut,
      },
    });
  };
  const handleClipEnd = () => {
    setVideoPlayerState({
      isVideoPlaying: false,
    });
  };

  const searchResultCount = searchResult.factHits
    .flatMap((factHit) => factHit.searchHits)
    .reduce((total, hits) => {
      return total + hits.highlightOffsets.length;
    }, 0);
  return (
    <>
      <div className="search__sticky">
        {videoPlayerState.videoProps && videoPlayerState.isVideoPlaying && (
          <div className="search__video">
            <VideoLite
              {...videoPlayerState.videoProps}
              onClipEnd={handleClipEnd}
            />
          </div>
        )}
        <SearchBar initialSearchQuery={searchQuery} classModifier="mobile" />
        <h1 className="results__heading">{searchResultCount} Results Found</h1>
        {factHits.length === 0 ? (
          <p className="turn__results">
            Search returned no results for <strong>{searchQuery}</strong>
          </p>
        ) : (
          <NGramViewer
            searchResult={searchResult}
            onBarClick={handleBarClick}
          />
        )}
      </div>
      <div className="results">
        {searchResult.factHits.length > 0 && (
          <SearchRadioButtons
            onChange={eventHandlers.onModeChange}
            selectedOption={mode}
          />
        )}
        <VideoResultsList
          bookmarks={bookmarks}
          dateToDivMap={dateToDivMap}
          eventHandlers={{
            onAddBookmark: eventHandlers.onAddBookmark,
            onRemoveBookmark: eventHandlers.onRemoveBookmark,
            onPlayClick: handlePlayClick,
          }}
          searchResult={searchResult}
        />
      </div>
    </>
  );
};

// scrollTo with completion callback https://stackoverflow.com/a/55686711
function scrollTo(offset: number, callback: () => void) {
  const fixedOffset = offset.toFixed(),
    onScroll = function () {
      if (window.pageYOffset.toFixed() === fixedOffset) {
        window.removeEventListener("scroll", onScroll);
        callback();
      }
    };

  window.addEventListener("scroll", onScroll);
  onScroll();
  window.scrollTo({
    top: offset,
    behavior: "smooth",
  });
}

function getStickyHeaderHeight(): number {
  const NGRAM_CONTAINER_HEIGHT = 218;
  const NGRAM_MARGIN_BOTTOM = 8;
  if (window.innerWidth > 768) {
    const HEADER_HEIGHT = 40;
    const HEADING_HEIGHT = 44;
    return (
      HEADER_HEIGHT +
      HEADING_HEIGHT +
      NGRAM_CONTAINER_HEIGHT +
      NGRAM_MARGIN_BOTTOM
    );
  } else {
    const MOBILE_HEADING_HEIGHT = 31;
    const SEARCHBAR_HEIGHT = 88;
    return (
      SEARCHBAR_HEIGHT +
      MOBILE_HEADING_HEIGHT +
      NGRAM_CONTAINER_HEIGHT +
      NGRAM_MARGIN_BOTTOM
    );
  }
}
export default SearchContainer;
