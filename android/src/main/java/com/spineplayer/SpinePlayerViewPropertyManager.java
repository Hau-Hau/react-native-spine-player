package com.spineplayer;

import android.view.View;

import com.spineplayer.spine.AnimationNamePair;
import com.spineplayer.spine.Physics;

import java.lang.ref.WeakReference;
import java.util.Map;

public class SpinePlayerViewPropertyManager {
    private final WeakReference<SpinePlayerView> viewWeakReference;
    private Integer height = null;
    private Integer width = null;
    private String imageUri = null;
    private String atlasData = null;
    private String skeletonData = null;
    private String[] animationNames = null;
    private Boolean loopAnimation = null;
    private Float defaultMix = null;
    private Map<AnimationNamePair, Float> mixes = null;
    private Float x = null;
    private Float y = null;
    private Float scaleX = null;
    private Float scaleY = null;
    private Float playbackSpeed = null;
    private Boolean autoPlay = null;
    private Physics physics = null;

    public SpinePlayerViewPropertyManager(SpinePlayerView view) {
        this.viewWeakReference = new WeakReference<>(view);
    }

    public void commitChanges() {
        SpinePlayerView view = viewWeakReference.get();
        if (view == null) {
            return;
        }

        if (height != null) {
            view.setHeight(height);
            height = null;
        }

        if (width != null) {
            view.setWidth(width);
            width = null;
        }

        if (imageUri != null) {
            view.setImageUri(imageUri);
            imageUri = null;
        }

        if (atlasData != null) {
            view.setAtlasData(atlasData);
            atlasData = null;
        }

        if (skeletonData != null) {
            view.setSkeletonData(skeletonData);
            skeletonData = null;
        }

        if (animationNames != null) {
            view.setAnimationNames(animationNames);
            animationNames = null;
        }

        if (loopAnimation != null) {
            view.setLoopAnimation(loopAnimation);
            loopAnimation = null;
        }

        if (defaultMix != null) {
            view.setDefaultMix(defaultMix);
            defaultMix = null;
        }

        if (mixes != null) {
            view.setMixes(mixes);
            mixes = null;
        }

        if (x != null) {
            view.setX(x);
            x = null;
        }

        if (y != null) {
            view.setY(y);
            y = null;
        }

        if (scaleX != null) {
//            view.setScaleX(scaleX);
            scaleX = null;
        }

        if (scaleY != null) {
//            view.setScaleY(scaleY);
            scaleY = null;
        }

        if (playbackSpeed != null) {
            view.setPlaybackSpeed(playbackSpeed);
            playbackSpeed = null;
        }

        if (autoPlay != null) {
            view.setAutoPlay(autoPlay);
            autoPlay = null;
        }

        if (physics != null) {
            view.setPhysics(physics);
            physics = null;
        }
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setAtlasData(String atlasData) {
        this.atlasData = atlasData;
    }

    public void setSkeletonData(String skeletonData) {
        this.skeletonData = skeletonData;
    }

    public void setAnimationNames(String[] animationNames) {
        this.animationNames = animationNames;
    }

    public void setLoopAnimation(boolean loopAnimation) {
        this.loopAnimation = loopAnimation;
    }

    public void setDefaultMix(float defaultMix) {
        this.defaultMix = defaultMix;
    }

    public void setMixes(Map<AnimationNamePair, Float> mixes) {
        this.mixes = mixes;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setPlaybackSpeed(float playbackSpeed) {
        this.playbackSpeed = playbackSpeed;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void setPhysics(Physics physics) {
        this.physics = physics;
    }
}