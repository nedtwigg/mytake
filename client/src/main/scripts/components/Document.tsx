import * as React from "react";
import * as ReactDOM from "react-dom";
import { FoundationNode } from "../utils/functions";
import { Foundation } from "../java2ts/Foundation";
import DocumentTextNodeList from "./DocumentTextNodeList";
import CaptionTextNodeList, {
  CaptionTextNodeListEventHandlers
} from "./CaptionTextNodeList";
import { TimeRange } from "./Video";

interface CaptionData {
  captionTimer: number;
  videoFact: Foundation.VideoFactContentFast;
}

export interface DocumentEventHandlers {
  onMouseUp: () => any;
  onScroll?: (viewRange: [number, number]) => any;
}

interface DocumentProps {
  eventHandlers: DocumentEventHandlers;
  nodes: FoundationNode[];
  className?: string;
  captionData?: CaptionData;
  view?: TimeRange;
}

interface DocumentState {}

class Document extends React.Component<DocumentProps, DocumentState> {
  constructor(props: DocumentProps) {
    super(props);
  }
  getDocumentNodes = () => {
    return [...this.props.nodes];
  };
  render() {
    let classes = "document document--static";
    let documentClass = this.props.className
      ? this.props.className
      : "document__row";

    let childComponent;
    if (
      this.props.captionData &&
      this.props.eventHandlers.onScroll &&
      this.props.view
    ) {
      const eventHandlers: CaptionTextNodeListEventHandlers = {
        onMouseUp: this.props.eventHandlers.onMouseUp,
        onScroll: this.props.eventHandlers.onScroll
      };
      childComponent = (
        <CaptionTextNodeList
          captionTimer={this.props.captionData.captionTimer}
          className="document__text document__text--caption"
          documentNodes={this.props.nodes}
          eventHandlers={eventHandlers}
          videoFact={this.props.captionData.videoFact}
          view={this.props.view}
        />
      );
    } else {
      childComponent = (
        <DocumentTextNodeList
          className="document__text"
          onMouseUp={this.props.eventHandlers.onMouseUp}
          documentNodes={this.props.nodes}
        />
      );
    }

    return (
      <div className={classes}>
        <div className={documentClass}>
          <div className={"document__row-inner"}>
            {childComponent}
            {this.props.children ? this.props.children : null}
          </div>
        </div>
      </div>
    );
  }
}

export default Document;
