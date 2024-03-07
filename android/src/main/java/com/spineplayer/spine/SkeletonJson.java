package com.spineplayer.spine;

public class SkeletonJson implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;

    public SkeletonJson(AtlasAttachmentLoader atlasAttachmentLoader) {
        pointer = create(atlasAttachmentLoader.getPointer());
    }

    private native long create(long atlasAttachmentLoaderPointer);

    private native void destroy(long pointer);

    private native long readSkeletonData(long pointer, String json);
    public SkeletonData readSkeletonData(String json) {
        long skeletonDataPointer = readSkeletonData(pointer, json);
        return new SkeletonData(skeletonDataPointer);
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