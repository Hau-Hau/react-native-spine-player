package com.spineplayer.spine;

import android.graphics.Bitmap;

public class SkeletonRenderer implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;

    public SkeletonRenderer() {
        //noinspection MoveFieldAssignmentToInitializer
        pointer = create();
    }

    private native long create();

    private native void destroy(long pointer);

    private native void draw(long skeletonRendererPointer, Bitmap bitmap, long skeletonPointer);
    public void draw(Bitmap bitmap, Skeleton skeleton) {
        if (skeleton.getPointer() == 0) {
            return;
        }

        draw(pointer, bitmap, skeleton.getPointer());
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