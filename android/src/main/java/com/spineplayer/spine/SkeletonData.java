package com.spineplayer.spine;

public class SkeletonData implements AutoCloseable 
{
    private long pointer;
    private boolean isDisposed;

    public SkeletonData(long pointer) {
        this.pointer = pointer;
    }

    public SkeletonData() {
        pointer = create();
    }

    private native long create();
    
    private native void destroy(long pointer);

    private native float getWidth(long pointer);
    public float getWidth() {
        if (pointer == 0) return Float.MIN_VALUE;
        return getWidth(pointer);
    }

    private native float getHeight(long pointer);
    public float getHeight() {
        if (pointer == 0) return Float.MIN_VALUE;
        return getHeight(pointer);
    }

    private native void setWidth(long pointer, float width);
    public void setWidth(float width) {
        if (pointer == 0) return;
        setWidth(pointer, width);
    }

    private native void setHeight(long pointer, float height);
    public void setHeight(float height) {
        if (pointer == 0) return;
        setHeight(pointer, height);
    }

    private native float getFps(long pointer);
    public float getFps() {
        if (pointer == 0) return Float.MIN_VALUE;
        return getFps(pointer);
    }

    private native long findAnimation(long pointer, String animationName);
    public Animation findAnimation(String animationName) {
        if (pointer == 0) return null;
        long animationPointer = findAnimation(pointer, animationName);
        if (animationPointer == 0) {
            return null;
        }

        return new Animation(animationPointer);
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