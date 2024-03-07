package com.spineplayer.spine;

public class AtlasAttachmentLoader implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;

    public AtlasAttachmentLoader(Atlas atlas) {
        pointer = create(atlas.getPointer());
    }

    private native long create(long atlasPointer);

    private native void destroy(long pointer);

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