package com.spineplayer.spine;

import androidx.annotation.NonNull;

public class Event implements AutoCloseable
{
    private long pointer;
    private boolean isDisposed;

    public Event(long pointer) {
        this.pointer = pointer;
    }

    private native long getData(long pointer);
    @NonNull
    public EventData getData() {
        long eventDataPointer = getData(pointer);
        return new EventData(eventDataPointer);
    }

    private native float getTime(long pointer);
    public float getTime() {
        return getTime(pointer);
    }

    private native int getIntValue(long pointer);
    public int getIntValue() {
        return getIntValue(pointer);
    }

    private native float getFloatValue(long pointer);
    public float getFloatValue() {
        return getFloatValue(pointer);
    }

    private native String getStringValue(long pointer);
    public String getStringValue() {
        return getStringValue(pointer);
    }

    private native float getVolume(long pointer);
    public float getVolume() {
        return getVolume(pointer);
    }

    private native float getBalance(long pointer);
    public float getBalance() {
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