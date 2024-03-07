package com.spineplayer;

import android.view.View;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.spineplayer.spine.AnimationNamePair;
import com.spineplayer.spine.Physics;

import java.util.Map;

public class SpinePlayerManagerImpl {
    public static final String REACT_CLASS = "SpinePlayerView";

    public static SpinePlayerView createViewInstance(ThemedReactContext context) {
        return new SpinePlayerView(context);
    }
    
    public static void setWidth(Integer value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setWidth(value);
        viewManager.commitChanges();
    }

    public static void setHeight(Integer value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setHeight(value);
        viewManager.commitChanges();
    }

    public static void setImageUri(String value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setImageUri(value);
        viewManager.commitChanges();
    }

    public static void setAtlasData(String value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setAtlasData(value);
        viewManager.commitChanges();
    }

    public static void setSkeletonData(String value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setSkeletonData(value);
        viewManager.commitChanges();
    }

    public static void setAnimationNames(String[] value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setAnimationNames(value);
        viewManager.commitChanges();
    }

    public static void setLoopAnimation(boolean value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setLoopAnimation(value);
        viewManager.commitChanges();
    }

    public static void setDefaultMix(float value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setDefaultMix(value);
        viewManager.commitChanges();
    }

    public static void setMixes(Map<AnimationNamePair, Float> value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setMixes(value);
        viewManager.commitChanges();
    }

    public static void setX(float value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setX(value);
        viewManager.commitChanges();
    }

    public static void setY(float value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setY(value);
        viewManager.commitChanges();
    }

    public static void setScaleX(float value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setScaleX(value);
        viewManager.commitChanges();
    }

    public static void setScaleY(float value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setScaleY(value);
        viewManager.commitChanges();
    }

    public static void setPlaybackSpeed(float value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setPlaybackSpeed(value);
        viewManager.commitChanges();
    }

    public static void setAutoPlay(boolean value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setAutoPlay(value);
        viewManager.commitChanges();
    }

    public static void setPhysics(Physics value, SpinePlayerViewPropertyManager viewManager) {
        viewManager.setPhysics(value);
        viewManager.commitChanges();
    }
}
