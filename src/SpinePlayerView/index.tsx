import React, { useEffect, useImperativeHandle, useState } from "react";
import { ImageRequireSource, NativeSyntheticEvent } from "react-native";
import SpinePlayerViewNative, { Commands } from "../specs/SpinePlayerViewNativeComponent";
import { isValidJson, resolveDefaultSource } from "./utils";

export enum SpinePlayerPhysics {
  /** Physics are not updated or applied. */
  Physics_None,

  /** Physics are reset to the current pose. */
  Physics_Reset,

  /** Physics are updated and the pose from physics is applied. */
  Physics_Update,

  /** Physics are not updated but the pose from physics is applied. */
  Physics_Pose,
}

export enum SpinePlayerEventType {
  EventType_Start,
  EventType_Interrupt,
  EventType_End,
  EventType_Complete,
  EventType_Dispose,
  EventType_Event,
}

export interface SpinePlayerViewProps {
  image: ImageRequireSource;
  atlasData: string;
  skeletonData: string | object;
  animationNames: Array<string>;
  loopAnimation?: boolean;
  defaultMix?: number;
  mixes?: Array<{ from: string; to: string; value: number }>;
  x?: number;
  y?: number;
  // Scale X/Y hidden as is automatically adjusted in SpineDrawable.java
  // scaleX?: number;
  // scaleY?: number;
  playbackSpeed?: number;
  autoPlay?: boolean;
  physics?: SpinePlayerPhysics;
  onSpineEvent?: (
    e: NativeSyntheticEvent<
      Readonly<{
        data: Readonly<{
          name: string;
          intValue: number;
          floatValue: number;
          stringValue: string;
          audioPath: string;
          volume: number;
          balance: number;
        }>;
        type: SpinePlayerEventType;
        time: number;
        intValue: number;
        floatValue: number;
        stringValue: string;
        volume: number;
        balance: number;
      }>
    >,
  ) => void;
}

export type SpinePlayerEventCommands = {
  play: () => void;
  stop: () => void;
  reset: () => void;
};

export const SpinePlayerView = React.forwardRef(
  (props: SpinePlayerViewProps, ref: React.ForwardedRef<SpinePlayerEventCommands>): JSX.Element => {
    let spinePlayerViewNativeRef: React.ElementRef<typeof SpinePlayerViewNative> | undefined;

    const {
      atlasData,
      skeletonData,
      image,
      animationNames,
      loopAnimation,
      defaultMix,
      mixes,
      x,
      y,
      playbackSpeed,
      autoPlay,
      physics,
      onSpineEvent,
      ...rest
    } = props;
    const [skeletonJson, setSkeletonJson] = useState<string>("");

    useEffect(() => loadSkeleton(), [skeletonData]);

    const loadSkeleton = () => {
      switch (typeof skeletonData) {
        case "string": {
          if (isValidJson(skeletonData)) {
            setSkeletonJson(skeletonData);
          } else {
            throw Error(`UnrecognizedFormat: The data provided in skeletonJson is not recognized as a valid format`);
          }
          break;
        }
        case "object": {
          setSkeletonJson(JSON.stringify(skeletonData));
          break;
        }
        default: {
          throw Error(`UnsupportedType: The type '${typeof skeletonData}' is not supported by skeletonJson`);
        }
      }
    };

    useImperativeHandle(ref, () => ({
      play: () => spinePlayerViewNativeRef && Commands.play(spinePlayerViewNativeRef),
      stop: () => spinePlayerViewNativeRef && Commands.stop(spinePlayerViewNativeRef),
      reset: () => spinePlayerViewNativeRef && Commands.reset(spinePlayerViewNativeRef),
    }));

    const captureRef = (nativeRef: React.ElementRef<typeof SpinePlayerViewNative>) => {
      if (nativeRef === null) {
        return;
      }

      spinePlayerViewNativeRef = nativeRef;
      if (autoPlay === true) {
        Commands.play(nativeRef);
      }
    };

    return (
      <SpinePlayerViewNative
        ref={captureRef}
        atlasData={atlasData}
        skeletonData={skeletonJson}
        imageUri={resolveDefaultSource(image)}
        animationNames={animationNames}
        loopAnimation={loopAnimation === true}
        defaultMix={defaultMix || 0}
        mixes={mixes ?? []}
        x={x}
        y={y}
        // Scale X/Y hidden as is automatically adjusted in SpineDrawable.java
        // scaleX={scaleX}
        // scaleY={scaleY}
        playbackSpeed={playbackSpeed == null ? 1.0 : playbackSpeed}
        autoPlay={autoPlay === true}
        physics={physics}
        onSpineEvent={onSpineEvent}
        {...rest}
      />
    );
  },
);
SpinePlayerView.displayName = "SpinePlayerView";
