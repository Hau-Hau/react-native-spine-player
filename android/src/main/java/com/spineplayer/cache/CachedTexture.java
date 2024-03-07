package com.spineplayer.cache;

import android.graphics.Bitmap;

public final class CachedTexture {
    private final Bitmap bitmap;
    private int referencesCount = 0;

    CachedTexture(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public synchronized Bitmap get() {
        return bitmap;
    }

    public synchronized void acquire() {
        referencesCount = referencesCount + 1;
    }

    public synchronized int release() {
        if (this.referencesCount == 0) {
            return 0;
        }

        this.referencesCount = this.referencesCount - 1;
        return this.referencesCount;
    }
}
