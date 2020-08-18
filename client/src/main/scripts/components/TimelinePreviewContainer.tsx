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
import React, { useEffect, useState } from "react";
import { FoundationNode } from "../common/CaptionNodes";
import { FoundationFetcher, isDocument, isVideo } from "../common/foundation";
import { TextCut, VideoCut } from "../common/social/social";
import { FT } from "../java2ts/FT";
import { SetFactHandlers } from "./TimelinePreview";
import TimelinePreviewLoadingView from "./TimelinePreviewLoadingView";

export type TimelineSocial = VideoCut | TextCut;

interface TimelinePreviewContainerProps {
  selectedFact: TimelineSocial;
  setFactHandlers?: SetFactHandlers;
}

interface TimelinePreviewContainerState {
  loading: boolean;
  videoFact?: FT.VideoFactContent;
  nodes?: FoundationNode[];
}

const TimelinePreviewContainer: React.FC<TimelinePreviewContainerProps> = (
  props
) => {
  const [state, setState] = useState<TimelinePreviewContainerState>({
    loading: true,
  });

  useEffect(() => {
    const getFact = async (factHash: string) => {
      const builder = new FoundationFetcher();
      builder.add(factHash);
      const foundationData = await builder.build();
      const factContent = foundationData.getFactContent(factHash);

      let nodes: FoundationNode[] = [];
      if (isDocument(factContent)) {
        for (let documentComponent of factContent.components) {
          nodes.push({
            component: documentComponent.component,
            innerHTML: [documentComponent.innerHTML],
            offset: documentComponent.offset,
          });
        }
        setState({
          loading: false,
          nodes: nodes,
        });
      } else if (isVideo(factContent)) {
        setState({
          loading: false,
          videoFact: factContent,
        });
      } else {
        throw "Unknown kind of Fact";
      }
    };

    setState((prevState) => ({
      ...prevState,
      loading: true,
    }));

    getFact(props.selectedFact.fact);
  }, [props.selectedFact.fact]);

  return state.loading ? (
    <TimelinePreviewLoadingView />
  ) : (
    <p>todo</p>
    // <TimelinePreview
    //   factLink={props.factLink}
    //   videoFact={state.videoFact}
    //   nodes={state.nodes}
    //   setFactHandlers={props.setFactHandlers}
    //   ranges={props.ranges}
    //   offset={props.offset}
    // />
  );
};

export default TimelinePreviewContainer;
