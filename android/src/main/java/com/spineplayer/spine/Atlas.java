package com.spineplayer.spine;

public class Atlas implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;

    public Atlas(String atlasData) {
        pointer = create(atlasData);
    }

    private native long create(String atlasData);

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