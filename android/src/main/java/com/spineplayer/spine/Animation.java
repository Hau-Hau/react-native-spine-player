package com.spineplayer.spine;

// No need to make this class AutoCloseable and to release pointer
// as this class is managed internally by SkeletonData
public class Animation {
    private final long pointer;
    private boolean isDisposed;

    public Animation(long pointer) {
        this.pointer = pointer;
    }

    private native float getDuration(long pointer);
    public float getDuration() {
        return getDuration(pointer);
    }
}
