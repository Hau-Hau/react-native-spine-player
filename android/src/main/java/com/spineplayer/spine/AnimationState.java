package com.spineplayer.spine;

import android.view.View;

public class AnimationState implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;
    private AnimationStateData animationStateData;

    public AnimationState(AnimationStateData animationStateData) {
        this.animationStateData = animationStateData;
        pointer = create(animationStateData.getPointer());
    }

    public AnimationState(long pointer) {
        this.pointer = pointer;
    }
    
    private native long create(long animationStateData);
    
    private native void destroy(long pointer);

    private native long getData(long pointer);
    public AnimationStateData getData() {
        if (animationStateData == null) {
            long animationStateDataPointer = getData(pointer);
            if (animationStateDataPointer == 0) {
                return null;
            }

            animationStateData = new AnimationStateData(animationStateDataPointer);
        }

        return animationStateData;
    }

    private native void setAnimation(long pointer, int trackIndex, String animationName, boolean loop);
    public void setAnimation(int trackIndex, String animationName, boolean loop) {
        if (pointer == 0) return;
        setAnimation(pointer, trackIndex, animationName, loop);
    }

    private native void setEmptyAnimation(long pointer, int trackIndex, float mixDuration);
    public void setEmptyAnimation(int trackIndex, float mixDuration) {
        if (pointer == 0) return;
        setEmptyAnimation(pointer, trackIndex, mixDuration);
    }

    private native void setTimeScale(long pointer, float inValue);
    public void setTimeScale(float inValue) {
        if (pointer == 0) return;
        setTimeScale(pointer, inValue);
    }

    private native void update(long pointer, float delta);
    public void update(float delta) {
        if (pointer == 0) return;
        update(pointer, delta);
    }

    private native void apply(long pointer, long skeletonPointer);
    public void apply(Skeleton skeleton) {
        if (pointer == 0) return;
        apply(pointer, skeleton.getPointer());
    }

    private native void clearTracks(long pointer);
    public void clearTracks() {
        if (pointer == 0) return;
        clearTracks(pointer);
    }

    private native void setListener(long pointer, View view);
    public void setListener(View view) {
        if (pointer == 0) return;
        setListener(pointer, view);
    }

    private native void destroyListener(long pointer);
    public void destroyListener() {
        if (pointer == 0) return;
        destroyListener(pointer);
    }

    public long getPointer() {
        return pointer;
    }

    private synchronized void dispose(boolean disposing) {
        if (isDisposed) {
            return;
        }

        if (pointer != 0) {
            animationStateData.close();
            destroyListener(pointer);
            destroy(pointer);
            pointer = 0;
        }

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