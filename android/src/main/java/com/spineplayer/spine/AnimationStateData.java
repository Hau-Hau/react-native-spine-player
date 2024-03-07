package com.spineplayer.spine;

public class AnimationStateData implements AutoCloseable 
{
    private long pointer;
    private boolean isDisposed;

    public AnimationStateData(SkeletonData skeletonData) {
        pointer = create(skeletonData.getPointer());
    }

    public AnimationStateData(long pointer) {
        this.pointer = pointer;
    }
    
    private native long create(long skeletonDataPointer);
    
    private native void destroy(long pointer);

    private native void clear(long pointer);
    public void clear() {
        clear(pointer);
    }

    private native void setDefaultMix(long pointer, float inValue);
    public void setDefaultMix(float inValue) {
        setDefaultMix(pointer, inValue);
    }

    private native void setMix(long pointer, String fromAnimationName, String toAnimationName, float inValue);
    public void setMix(String fromAnimationName, String toAnimationName, float inValue) {
        setMix(pointer, fromAnimationName, toAnimationName, inValue);
    }

    public long getPointer() {
        return pointer;
    }

    private synchronized void dispose(boolean disposing) {
        if (isDisposed) {
            return;
        }

        if (pointer != 0) {
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