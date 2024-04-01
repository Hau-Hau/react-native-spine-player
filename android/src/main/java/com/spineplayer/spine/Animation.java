package com.spineplayer.spine;

// No need to make this class AutoCloseable and to release pointer
// as this class is managed internally by SkeletonData
public class Animation {
    private final long pointer;

    public Animation(long pointer) {
        this.pointer = pointer;
    }

    private native float getDuration(long pointer);
    public float getDuration() {
        if (pointer == 0) return Integer.MIN_VALUE;
        return getDuration(pointer);
    }
}
