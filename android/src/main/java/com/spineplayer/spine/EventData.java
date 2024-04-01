package com.spineplayer.spine;

public class EventData implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;

    public EventData(long pointer) {
        this.pointer = pointer;
    }

    private native String getName(long pointer);
    public String getName() {
        if (pointer == 0) return "";
        return getName(pointer);
    }

    private native int getIntValue(long pointer);
    public int getIntValue() {
        if (pointer == 0) return Integer.MIN_VALUE;
        return getIntValue(pointer);
    }

    private native float getFloatValue(long pointer);
    public float getFloatValue() {
        if (pointer == 0) return Float.MIN_VALUE;
        return getFloatValue(pointer);
    }

    private native String getStringValue(long pointer);
    public String getStringValue() {
        if (pointer == 0) return "";
        return getStringValue(pointer);
    }

    private native String getAudioPath(long pointer);
    public String getAudioPath() {
        if (pointer == 0) return "";
        return getAudioPath(pointer);
    }

    private native float getVolume(long pointer);
    public float getVolume() {
        if (pointer == 0) return Float.MIN_VALUE;
        return getVolume(pointer);
    }

    private native float getBalance(long pointer);
    public float getBalance() {
        if (pointer == 0) return Float.MIN_VALUE;
        return getBalance(pointer);
    }

    public long getPointer() {
        return pointer;
    }

    private synchronized void dispose(boolean disposing) {
        if (isDisposed) {
            return;
        }

        if (pointer != 0) {
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
