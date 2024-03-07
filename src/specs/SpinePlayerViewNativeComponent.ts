import type { HostComponent, ViewProps } from "react-native";
import { DirectEventHandler, Float, Int32 } from "react-native/Libraries/Types/CodegenTypes";
import codegenNativeComponent from "react-native/Libraries/Utilities/codegenNativeComponent";
import codegenNativeCommands from "react-native/Libraries/Utilities/codegenNativeCommands";

export interface NativeProps extends ViewProps {
  imageUri: string;
  atlasData: string;
  skeletonData: string;
  animationNames: Array<string>;
  loopAnimation?: boolean;
  x?: Float;
  y?: Float;
  defaultMix: Float;
  mixes: Array<{ from: string; to: string; value: Float }>;
  // Scale X/Y hidden as is automatically adjusted in SpineDrawable.java
  // scaleX?: Float;
  // scaleY?: Float;
  playbackSpeed: Float;
  autoPlay: boolean;
  physics?: Int32;
  onSpineEvent?: DirectEventHandler<
    Readonly<{
      data: Readonly<{
        name: string;
        intValue: Int32;
        floatValue: Float;
        stringValue: string;
        audioPath: string;
        volume: Float;
        balance: Float;
      }>;
      type: Int32;
      time: Float;
      intValue: Int32;
      floatValue: Float;
      stringValue: string;
      volume: Float;
      balance: Float;
    }>
  >;
}

type SpinePlayerViewNativeComponentType = HostComponent<NativeProps>;

interface NativeCommands {
  play: (viewRef: React.ElementRef<SpinePlayerViewNativeComponentType>) => void;
  stop: (viewRef: React.ElementRef<SpinePlayerViewNativeComponentType>) => void;
  reset: (viewRef: React.ElementRef<SpinePlayerViewNativeComponentType>) => void;
}

export const Commands: NativeCommands = codegenNativeCommands<NativeCommands>({
  supportedCommands: ["play", "stop", "reset"],
});

export default codegenNativeComponent<NativeProps>("SpinePlayerView"); //as HostComponent<NativeProps>;
