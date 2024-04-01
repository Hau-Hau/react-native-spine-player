package com.spineplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.os.Handler;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.spineplayer.cache.TextureCache;
import com.spineplayer.spine.Animation;
import com.spineplayer.spine.AnimationNamePair;
import com.spineplayer.spine.AnimationState;
import com.spineplayer.spine.AnimationStateData;
import com.spineplayer.spine.Atlas;
import com.spineplayer.spine.AtlasAttachmentLoader;
import com.spineplayer.spine.Physics;
import com.spineplayer.spine.Skeleton;
import com.spineplayer.spine.SkeletonData;
import com.spineplayer.spine.SkeletonJson;
import com.spineplayer.spine.SkeletonRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpineDrawable extends Drawable implements Animatable, AutoCloseable {
    private final boolean useChoreographer = false;
    // isRendering should be a Semaphore
    private final AtomicBoolean isRendering = new AtomicBoolean(false);
    private final SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
    private boolean isDisposed;
    private boolean isInitialized = false;
    private boolean isRunning = false;
    private Bitmap bufferBitmap;
    private long previousTime;
    private boolean retryCreateSkeleton = false;
    private Atlas atlas;
    private Skeleton skeleton;
    private AnimationState animationState;
    private int frameTimeMs = 0;
    private int canvasWidth = 0;
    private int canvasHeight = 0;
    private int boundsWidth = 0;
    private int boundsHeight = 0;
    private String imageUri = null;
    private String atlasData = null;
    private String skeletonDataFile = null;
    private String[] animationNames = new String[0];
    private boolean loopAnimation = false;
    private float defaultMix = 0;
    private Map<AnimationNamePair, Float> mixes = new HashMap<>();
    private Float x = null;
    private Float y = null;
    private float playbackSpeed = 1.0f;
    private boolean autoPlay = false;
    private Physics physics = Physics.Physics_None;
    private View spineEventListenerTarget;

    public SpineDrawable(View parent) {
        spineEventListenerTarget = parent;

        if (useChoreographer) {
            playWithChoreographer();
        } else {
            playWithHandler();
        }
    }

    private void createSkeleton() {
        if (atlasData == null
                || atlasData.trim().isEmpty()
                || skeletonDataFile == null
                || skeletonDataFile.trim().isEmpty()
                || imageUri == null
                || imageUri.trim().isEmpty()
                || retryCreateSkeleton) {
            return;
        }

        try {
            // TODO Semaphore should be better than recursive retry
            if (!isRendering.compareAndSet(false, true)) {
                retryCreateSkeleton = true;
                return;
            }

            if (animationState != null) {
                animationState.close();
            }

            if (skeleton != null) {
                skeleton.close();
            }

            if (atlas != null) {
                atlas.close();
            }

            atlas = new Atlas(atlasData);
            try (AtlasAttachmentLoader attachmentLoader = new AtlasAttachmentLoader(atlas)) {
                try (SkeletonJson json = new SkeletonJson(attachmentLoader)) {
                    SkeletonData skeletonData = json.readSkeletonData(skeletonDataFile);
                    skeleton = new Skeleton(Objects.requireNonNull(skeletonData));
                    skeleton.setToSetupPose();
                    skeleton.updateWorldTransform(physics);

                    if (x != null) {
                        skeleton.setX(x);
                    }

                    if (y != null) {
                        skeleton.setY(y);
                    }

                    AnimationStateData animationStateData = new AnimationStateData(skeleton.getData());
                    animationState = new AnimationState(animationStateData);
                    stop();

                    Rect bounds = skeleton.calculateAllAnimationsBounds(animationState, physics);
                    boundsWidth = bounds.width();
                    boundsHeight = bounds.height();

                    initializeBufferBitmap();

                    // Try to restore animation mixes and state
                    setMixes(mixes);
                    setAnimationNames(animationNames);

                    // Try to set listener
                    setSpineEventListenerTarget(spineEventListenerTarget);

                    if (!isInitialized) {
                        if (autoPlay) {
                            start();
                        } else {
                            doFrame(System.nanoTime());
                        }
                        isInitialized = true;
                    }
                }
            }
        } finally {
            isRendering.set(false);
        }

        if (retryCreateSkeleton) {
            retryCreateSkeleton = false;
            createSkeleton();
        }
    }

    private void initializeBufferBitmap() {
        if (canvasWidth == 0 || canvasHeight == 0 || boundsWidth == 0 || boundsHeight == 0) {
            return;
        }

        final float aspectRatio = boundsWidth > boundsHeight ? (float) boundsWidth / boundsHeight : (float) boundsHeight / boundsWidth;
        final int widthReduced = Math.round(canvasWidth * aspectRatio);
        final int heightReduced = Math.round(canvasHeight * aspectRatio);
        final float averageScale = (((float) widthReduced / boundsWidth) + ((float) heightReduced / boundsHeight)) / 2;
        skeleton.setScaleX(averageScale);
        skeleton.setScaleY(averageScale);
        bufferBitmap = Bitmap.createBitmap(widthReduced, heightReduced, Bitmap.Config.ARGB_8888);
    }

    public void setCanvasWidth(final int canvasWidth) {
        this.canvasWidth = canvasWidth;
        initializeBufferBitmap();
    }

    public void setCanvasHeight(final int canvasHeight) {
        this.canvasHeight = canvasHeight;
        initializeBufferBitmap();
    }

    public void setImageUri(final String value) {
        if (this.imageUri != null && !this.imageUri.equals(value)) {
            String fileName = imageUri.substring(imageUri.lastIndexOf("/") + 1);
            fileName = fileName.split("\\?")[0];
            TextureCache.release(fileName);
        }

        this.imageUri = value;
        createSkeleton();
    }

    public void setAtlasData(final String value) {
        this.atlasData = value;
        createSkeleton();
    }

    public void setSkeletonDataFile(final String value) {
        this.skeletonDataFile = value;
        createSkeleton();
    }

    public void setAnimationNames(@NonNull final String[] animationNames) {
        this.animationNames = animationNames;
        if (skeleton == null) {
            return;
        }

        float animationDuration = 0;
        for (String animationName : animationNames) {
            Animation animation = skeleton.getData().findAnimation(animationName);
            if (animation == null) {
                Log.e("RNSpinePlayer", "Animation '" + animationName + "' has been not found.");
                return;
            }
            animationDuration = Math.max(animationDuration, animation.getDuration());
        }

        for (int i = 0; i < animationNames.length; i++) {
            animationState.setAnimation(i, animationNames[i], loopAnimation);
        }

        frameTimeMs = Math.round((1000 / skeleton.getData().getFps()));
    }

    public void setLoopAnimation(final boolean loopAnimation) {
        this.loopAnimation = loopAnimation;
        setAnimationNames(animationNames);
    }

    public void setX(final float value) {
        x = value;
        if (skeleton == null) {
            return;
        }

        skeleton.setX(value);
    }

    public void setY(final float value) {
        y = value;
        if (skeleton == null) {
            return;
        }

        skeleton.setY(value);
    }

    public void setDefaultMix(final float value) {
        defaultMix = value < 0 ? 0 : value;
        if (animationState == null) {
            return;
        }

        AnimationStateData animationStateData = animationState.getData();
        if (animationStateData == null) {
            return;
        }

        animationStateData.setDefaultMix(defaultMix);
    }

    public void setMixes(final Map<AnimationNamePair, Float> mixes) {
        this.mixes = mixes;
        if (animationState == null) {
            return;
        }

        final AnimationStateData animationStateData = animationState.getData();
        if (animationStateData == null) {
            return;
        }

        animationStateData.clear();
        animationStateData.setDefaultMix(defaultMix);
        for (Map.Entry<AnimationNamePair, Float> entry : mixes.entrySet()) {
            AnimationNamePair pair = entry.getKey();
            if (pair == null) {
                continue;
            }
            Float mix = entry.getValue();
            mix = mix == null || entry.getValue() < 0 ? defaultMix : mix;
            animationStateData.setMix(pair.getA1(), pair.getA2(), mix);
        }
    }

    public void setPlaybackSpeed(final float value) {
        playbackSpeed = value;
    }

    public void setAutoPlay(final boolean value) {
        if (isInitialized && !autoPlay && value) {
            start();
        }

        autoPlay = value;
    }

    public void setPhysics(Physics value) {
        if (value == null) {
            value = Physics.Physics_None;
        }

        if (physics == value) {
            return;
        }

        physics = value;
        createSkeleton();
    }

    public void setSpineEventListenerTarget(@Nullable View view) {
        spineEventListenerTarget = view;

        if (animationState == null) {
            return;
        }

        if (view == null) {
            animationState.destroyListener();
        } else {
            animationState.setListener(view);
        }
    }
    private void doFrame(final float deltaTime) {
        if (animationState == null || skeleton == null || deltaTime == 0) {
            return;
        }

        animationState.update(deltaTime * playbackSpeed);
        animationState.apply(skeleton);
        skeleton.updateWorldTransform(physics);

        skeletonRenderer.draw(bufferBitmap, skeleton);
    }

    @Override
    public void start() {
        isRunning = true;
        if (animationState == null) {
            return;
        }

        // TODO Controlling play/pause through setTimeScale without pausing loop is not best solution but works for now
        animationState.setTimeScale(1);
    }

    private void playWithHandler() {
        final Handler handler = new Handler();
        final Runnable frameCallback = new Runnable() {
            @Override
            public void run() {
                final long currentTime = System.nanoTime();
                if (isDisposed) {
                    return;
                }

                handler.postDelayed(this, frameTimeMs);

                final float deltaTime = previousTime < 0 ? 0 : (currentTime - previousTime) / 1e9f;
                previousTime = currentTime;

                if (deltaTime != 0) {
                    invalidateSelf();
                }

                if (isRendering.compareAndSet(false, true)) {
                    try {
                        doFrame(deltaTime);
                    }
                    finally {
                        isRendering.set(false);
                    }
                }
            }
        };
        handler.post(frameCallback);
    }

    private void playWithChoreographer() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(final long currentTime) {
                if (isDisposed) {
                    return;
                }

                Choreographer.getInstance().postFrameCallbackDelayed(this, frameTimeMs);

                final float deltaTime = previousTime < 0 ? 0 : (currentTime - previousTime) / 1e9f;
                previousTime = currentTime;

                if (deltaTime != 0) {
                    invalidateSelf();
                }

                if (isRendering.compareAndSet(false, true)) {
                    try {
                        SpineDrawable.this.doFrame(deltaTime);
                    }
                    finally {
                        isRendering.set(false);
                    }
                }
            }
        });
    }

    public void reset() {
        if (skeleton == null || animationState == null) {
            return;
        }

        animationState.clearTracks();
        animationState.setEmptyAnimation(0, 0);
        skeleton.setToSetupPose();
        setMixes(mixes);
        setAnimationNames(animationNames);
    }

    @Override
    public void stop() {
        isRunning = false;
        if (animationState == null) {
            return;
        }

        // TODO Controlling play/pause through setTimeScale without pausing loop is not best solution but works for now
        animationState.setTimeScale(0);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (skeleton == null || animationNames.length == 0 || bufferBitmap == null) {
            return;
        }

        if (isRendering.compareAndSet(false, true)) {
            try {
                canvas.drawBitmap(bufferBitmap, 0, 0, null);
            }
            finally {
                isRendering.set(false);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // Implement if needed
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // Implement if needed
    }

    @Override
    public int getOpacity() {
        // Implement if needed
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return canvasWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return canvasHeight;
    }

    /**
     * @noinspection unused
     */
    private synchronized void dispose(boolean disposing) {
        if (isDisposed) {
            return;
        }

        stop();
        animationState.close();
        skeleton.close();
        atlas.close();
        skeletonRenderer.close();

        String fileName = imageUri.substring(imageUri.lastIndexOf("/") + 1);
        fileName = fileName.split("\\?")[0];
        TextureCache.release(fileName);

        isDisposed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        if (!isDisposed) {
            dispose(false);
        }

        super.finalize();
    }

    @Override
    public void close() {
        dispose(true);
    }
}