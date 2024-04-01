package com.spineplayer.spine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Event implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;

    public Event(long pointer) {
        this.pointer = pointer;
    }

    private native long getData(long pointer);
    @Nullable
    public EventData getData() {
        if (pointer == 0) return null;
        long eventDataPointer = getData(pointer);
        return new EventData(eventDataPointer);
    }

    private native float getTime(long pointer);
    public float getTime() {
        if (pointer == 0) return Float.MIN_VALUE;
        return getTime(pointer);
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
    @Nullable
    public String getStringValue() {
        if (pointer == 0) return null;
        return getStringValue(pointer);
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