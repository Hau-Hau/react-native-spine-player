package com.spineplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.spineplayer.spine.AnimationNamePair;

import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.facebook.react.viewmanagers.SpinePlayerViewManagerDelegate;
import com.facebook.react.viewmanagers.SpinePlayerViewManagerInterface;
import com.spineplayer.spine.Physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/** @noinspection deprecation*/
@ReactModule(name = SpinePlayerManagerImpl.REACT_CLASS)
public class SpinePlayerManager
        extends SimpleViewManager<SpinePlayerView>
        implements SpinePlayerViewManagerInterface<SpinePlayerView> {
    private final WeakHashMap<SpinePlayerView, SpinePlayerViewPropertyManager> propManagersMap = new WeakHashMap<>();
    private final ViewManagerDelegate<SpinePlayerView> delegate;

    public SpinePlayerManager() {
        delegate = new SpinePlayerViewManagerDelegate<>(this);
    }

    private SpinePlayerViewPropertyManager getOrCreatePropertyManager(SpinePlayerView view) {
        SpinePlayerViewPropertyManager result = propManagersMap.get(view);
        if (result == null) {
            result = new SpinePlayerViewPropertyManager(view);
            propManagersMap.put(view, result);
        }

        return result;
    }

    @Override
    protected ViewManagerDelegate<SpinePlayerView> getDelegate() {
        return delegate;
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

    @Override
    @ReactProp(name = "imageUri")
    public void setImageUri(SpinePlayerView view, String uri) {
        SpinePlayerManagerImpl.setImageUri(uri, getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "atlasData")
    public void setAtlasData(SpinePlayerView view, @Nullable String value) {
        SpinePlayerManagerImpl.setAtlasData(value, getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "skeletonData")
    public void setSkeletonData(SpinePlayerView view, @Nullable String value) {
        SpinePlayerManagerImpl.setSkeletonData(value, getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "animationNames")
    public void setAnimationNames(SpinePlayerView view, @Nullable ReadableArray value) {
        if (value == null) {
            return;
        }

        List<String> animations = new ArrayList<>();
        for (int i = 0; i < (value != null ? value.size() : 0); i++) {
            animations.add(value.getString(i));

        }
        SpinePlayerManagerImpl.setAnimationNames(animations.toArray(new String[0]), getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "loopAnimation")
    public void setLoopAnimation(SpinePlayerView view, boolean value) {
        SpinePlayerManagerImpl.setLoopAnimation(value, getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "defaultMix")
    public void setDefaultMix(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setDefaultMix(value, getOrCreatePropertyManager(view));
    }

    @Override
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

    @Override
    @ReactProp(name = "x")
    public void setX(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setX(value, getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "y")
    public void setY(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setY(value, getOrCreatePropertyManager(view));
    }

// Scale X/Y hidden as is set automatically un SpineDrawable
//    /**
//     * @noinspection deprecation
//     */
//    @Override
//    @ReactProp(name = "scaleX")
//    public void setScaleX(SpinePlayerView view, float value) {
//        SpinePlayerManagerImpl.setScaleX(value, getOrCreatePropertyManager(view));
//    }

//    /**
//     * @noinspection deprecation
//     */
//    @Override
//    @ReactProp(name = "scaleY")
//    public void setScaleY(SpinePlayerView view, float value) {
//        SpinePlayerManagerImpl.setScaleY(value, getOrCreatePropertyManager(view));
//    }

    @Override
    @ReactProp(name = "playbackSpeed")
    public void setPlaybackSpeed(SpinePlayerView view, float value) {
        SpinePlayerManagerImpl.setPlaybackSpeed(value, getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "autoPlay")
    public void setAutoPlay(SpinePlayerView view, boolean value) {
        SpinePlayerManagerImpl.setAutoPlay(value, getOrCreatePropertyManager(view));
    }

    @Override
    @ReactProp(name = "physics")
    public void setPhysics(SpinePlayerView view, int value) {
        Physics physics = Physics.getValue(value);
        if (physics == null) {
            return;
        }

        SpinePlayerManagerImpl.setPhysics(physics, getOrCreatePropertyManager(view));

    }

    @Override
    public void play(SpinePlayerView view) {
        view.start();
    }

    @Override
    public void stop(SpinePlayerView view) {
        view.stop();
    }

    @Override
    public void reset(SpinePlayerView view) {
        view.reset();
    }

    // https://stackoverflow.com/questions/72981626/how-to-do-event-handling-for-android-and-ios-in-react-natives-new-architecture
    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> export = super.getExportedCustomDirectEventTypeConstants();
        if (export == null) {
            export = MapBuilder.newHashMap();
        }

        export.put(SpinePlayerEvent.EVENT_NAME, MapBuilder.of("registrationName", "onSpineEvent"));
        return export;
    }

    @Override
    protected void onAfterUpdateTransaction(@NonNull SpinePlayerView view) {
        super.onAfterUpdateTransaction(view);
        getOrCreatePropertyManager(view).commitChanges();
    }
}