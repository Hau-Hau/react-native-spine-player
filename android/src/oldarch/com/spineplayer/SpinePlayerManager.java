package com.spineplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.spineplayer.spine.AnimationNamePair;
import com.spineplayer.spine.Physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class SpinePlayerManager extends SimpleViewManager<SpinePlayerView> {
    private final WeakHashMap<SpinePlayerView, SpinePlayerViewPropertyManager> propManagersMap = new WeakHashMap<>();

    private SpinePlayerViewPropertyManager getOrCreatePropertyManager(SpinePlayerView view) {
        SpinePlayerViewPropertyManager result = propManagersMap.get(view);
        if (result == null) {
            result = new SpinePlayerViewPropertyManager(view);
            propManagersMap.put(view, result);
        }

        return result;
    }

    @NonNull
    @Override
    public String getName() {
        return SpinePlayerManagerImpl.REACT_CLASS;
    }

    @NonNull
    @Override
    protected SpinePlayerView createViewInstance(@NonNull ThemedReactContext context) {
        return SpinePlayerManagerImpl.createViewInstance(context);
    }

    @Override
    public void receiveCommand(@NonNull SpinePlayerView root, String commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case "play":
                root.start();
                break;
            case "stop":
                root.stop();
                break;
            case "reset":
                root.reset();
                break;
            default:
                throw new UnsupportedOperationException("Unknown command: '" + commandId + "'.");
        }
    }

    /** @noinspection unused*/
    @ReactPropGroup(names = { "width", "height" }, customType = "Style")
    public void setStyle(SpinePlayerView view, int index, Integer value) {
        if (index == 0) {
             SpinePlayerManagerImpl.setWidth(value, getOrCreatePropertyManager(view));
        }

        if (index == 1) {
            SpinePlayerManagerImpl.setHeight(value, getOrCreatePropertyManager(view));
        }
    }

    /** @noinspection unused*/
    @ReactProp(name = "imageUri")
    public void setImageUri(SpinePlayerView view, String uri) {
        SpinePlayerManagerImpl.setImageUri(uri, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "atlasData")
    public void setAtlasData(SpinePlayerView view, @Nullable String value) {
        SpinePlayerManagerImpl.setAtlasData(value, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "skeletonData")
    public void setSkeletonData(SpinePlayerView view, @Nullable String value) {
        SpinePlayerManagerImpl.setSkeletonData(value, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "animationNames")
    public void setAnimationNames(SpinePlayerView view, @Nullable ReadableArray value) {
        if (value == null) {
            return;
        }

        List<String> animations = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            animations.add(value.getString(i));
        }

        SpinePlayerManagerImpl.setAnimationNames(animations.toArray(new String[0]), getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "loopAnimation")
    public void setLoopAnimation(SpinePlayerView view, boolean value) {
        SpinePlayerManagerImpl.setLoopAnimation(value, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "defaultMix")
    public void setDefaultMix(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setDefaultMix(value, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "mixes")
    public void setMixes(SpinePlayerView view, @Nullable ReadableArray value) {
        if (value == null) {
            return;
        }

        Map<AnimationNamePair, Float> mixes = new HashMap<>();
        for (int i = 0; i < value.size(); i++) {
                ReadableMap map = value.getMap(i);
                if (!map.hasKey("from") || !map.hasKey("to") || !map.hasKey("value")) {
                   continue;
                }

                String from = map.getString("from");
                String to = map.getString("to");
                Float mixValue = (float)map.getDouble("value");
                if (from == null || to == null) {
                    continue;
                }

                mixes.put(new AnimationNamePair(from, to), mixValue);
        }

        SpinePlayerManagerImpl.setMixes(mixes, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "x")
    public void setX(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setX(value, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "y")
    public void setY(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setY(value, getOrCreatePropertyManager(view));
    }



// Scale X/Y hidden as is set automatically un SpineDrawable
//    /**
//     * @noinspection unused, deprecation, NullableProblems
//     */
//    @ReactProp(name = "scaleX")
//    public void setScaleX(SpinePlayerView view, float value) {
//        SpinePlayerManagerImpl.setScaleX(value, getOrCreatePropertyManager(view));
//    }

//    /**
//     * @noinspection unused, deprecation, NullableProblems
//     */
//    @ReactProp(name = "scaleY")
//    public void setScaleY(SpinePlayerView view, float value) {
//        SpinePlayerManagerImpl.setScaleY(value, getOrCreatePropertyManager(view));
//    }

    /** @noinspection unused*/
    @ReactProp(name = "playbackSpeed")
    public void setPlaybackSpeed(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setPlaybackSpeed(value, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "autoPlay")
    public void setAutoPlay(SpinePlayerView view, boolean value) {
        SpinePlayerManagerImpl.setAutoPlay(value, getOrCreatePropertyManager(view));
    }

    /** @noinspection unused*/
    @ReactProp(name = "physics")
    public void setAutoPlay(SpinePlayerView view, int value) {
        Physics physics = Physics.getValue(value);
        if (physics == null) {
            return;
        }

        SpinePlayerManagerImpl.setPhysics(physics, getOrCreatePropertyManager(view));
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> export = super.getExportedCustomDirectEventTypeConstants();
        if (export == null) {
            export = MapBuilder.newHashMap();
        }

        export.put(SpineEvent.EVENT_NAME, MapBuilder.of("registrationName", "onSpineEvent"));
        return export;
    }

    @Override
    protected void onAfterUpdateTransaction(@NonNull SpinePlayerView view) {
        super.onAfterUpdateTransaction(view);
        getOrCreatePropertyManager(view).commitChanges();
    }
}