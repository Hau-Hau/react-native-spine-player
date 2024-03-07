package com.spineplayer.spine.bindings;

import android.graphics.Bitmap;

import com.spineplayer.cache.TextureCache;

public final class TextureCacheBindings {
    public static Bitmap get(String key) {
        return TextureCache.get(key);
    }
}