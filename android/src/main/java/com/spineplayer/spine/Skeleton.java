package com.spineplayer.spine;

import android.graphics.Rect;

public class Skeleton implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;
    private final SkeletonData skeletonData;

    public Skeleton(SkeletonData skeletonData) {
        this.skeletonData = skeletonData;
        pointer = create(skeletonData.getPointer());
    }

    private native long create(long skeletonDataPointer);
    
    private native void destroy(long pointer);
    
    private native void setX(long pointer, float x);
    public void setX(float x) {
        setX(pointer, x);
    }
    
    private native void setY(long pointer, float y);
    public void setY(float y) {
        setY(pointer, y);
    }

    private native void setScaleX(long pointer, float x);
    public void setScaleX(float x) {
        setScaleX(pointer, x);
    }

    private native void setScaleY(long pointer, float y);
    public void setScaleY(float y) {
        setScaleY(pointer, y);
    }

    private native float[] calculateAllAnimationsBounds(long pointer, long animationStatePointer, int physics);
    public Rect calculateAllAnimationsBounds(AnimationState animationState, Physics physics) {
        float[] bounds = calculateAllAnimationsBounds(pointer, animationState.getPointer(), physics.ordinal());
        return new Rect(0, 0, Math.round(bounds[0]), Math.round(bounds[1]));
    }

    private native void setToSetupPose(long pointer);
    public void setToSetupPose() {
        setToSetupPose(pointer);
    }

    private native void updateWorldTransform(long pointer, int physics);
    public void updateWorldTransform(Physics physics) {
        updateWorldTransform(pointer, physics.ordinal());
    }

    public SkeletonData getData() {
        return skeletonData;
    }

    public long getPointer() {
        return pointer;
    }

    private synchronized void dispose(boolean disposing) {
        if (isDisposed) {
            return;
        }

        if (pointer != 0) {
            skeletonData.close();
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