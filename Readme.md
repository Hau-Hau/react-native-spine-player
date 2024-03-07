React-Native-Spine-Player
=========================

This repository contains a library that integrates Spine2D with React Native, using Skia for rendering.

It's important to know that this isn't aiming to be the ultimate Spine2D solution for React Native. Instead, it's a demo showing how these technologies can work together. So, don't expect that this implementation will cover every possible use case.

Still, it does work. It can handle multiple animations at a decent speed of 30 FPS. Just remember, performance might vary based on things like how big your assets are and the size of the canvas used for the animation.

Please be advised that this project is experimental in nature. Due to lack of time, I'm not able to provide support for any bugs or usage issues that may arise. However, feel free to utilize the code as you see fit for your own purposes.

Integration Steps
-------------------------
1. Copy the 'react-native-spine-player' directory into your project's directory.
2. Navigate to the 'react-native-spine-player' directory and execute `npm run init`.
3. Within the 'react-native-spine-player' directory, run `npm run build`.
4. In your 'package.json', under 'dependencies', include `"react-native-spine-player": "file:react-native-spine-player"`.
5. Use component
```typescript
import { useRef, useState } from "react";
import { readAsStringAsync } from "expo-file-system";
import { SpinePlayerView, SpinePlayerEventCommands } from "react-native-spine-player";

const FooComponent = () => {
  const spinePlayerViewRef = useRef<SpinePlayerEventCommands>(null);
  const [atlas, setAtlas] = useState<string | null>(null);
  resolveAsync(require("/assets/spine/raptor.atlas")).then((asset) => {
    readAsStringAsync(asset.localUri!).then((text) => {
      setAtlas(text ?? null);
    });
  });

    return (
        <SpinePlayerView
            ref={spinePlayerViewRef}
            
            // width and height have to be provided!
            style={{ width: 200, height: 200, backgroundColor: "green" }}
            x={100}
            y={200}
            image={require("/assets/spine/raptor.png")}
            atlasData={atlas ?? ""}
            skeletonData={JSON.stringify(require("/assets/spine/raptor.json"))}

            // raptor animations: "walk", "jump", "roar", "gun-holster", "gun-grab"
            animationNames={["jump"]}
            defaultMix={0.3}
            mixes={[{ from: "walk", to: "jump", value: 0.7 }]}
            loopAnimation
            autoPlay
            playbackSpeed={1}
            onSpineEvent={(e) => console.log(JSON.stringify(e.nativeEvent))}
          />
    )
};
```

Functionalities Currently Implemented
-------------------------
- Support for Android
- Compatibility with Spine 4.2
- Loading capabilities based on .json and .atlas files
- Simultaneous playback of multiple animations
- Support for animation blends (mixes)
- Control over playback speed
- Autoplay feature
- Seamless looping functionality
- Event handling
- Ref based commands for Play, Stop, and Reset operations
- Spine 4.2 Physics (not extensively tested)
