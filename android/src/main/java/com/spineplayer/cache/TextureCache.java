package com.spineplayer.cache;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class TextureCache {
    private static final Map<String, CachedTexture> cache = new ConcurrentHashMap<>();

    public static synchronized void add(@NonNull String key, @NonNull Bitmap value) {
        if (Objects.isNull(key)) {
            throw new IllegalArgumentException("Parameter 'key' cannot be null");
        }

        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Parameter 'value' cannot be null");
        }

        if (cache.containsKey(key)) {
            return;
        }

        cache.put(key, new CachedTexture(value));
    }

    @NonNull
    public static synchronized Bitmap get(@NonNull String key) {
        if (Objects.isNull(key)) {
            throw new IllegalArgumentException("Parameter 'key' cannot be null");
        }

        return Objects.requireNonNull(cache.get(key)).get();
    }

    public static synchronized void acquire(@NonNull String key) {
        if (Objects.isNull(key)) {
            throw new IllegalArgumentException("Parameter 'key' cannot be null");
        }

        Objects.requireNonNull(cache.get(key)).acquire();
    }

    public synchronized static void release(@NonNull String key) {
        if (Objects.isNull(key)) {
            throw new IllegalArgumentException("Parameter 'key' cannot be null");
        }

        if (!cache.containsKey(key)) {
            return;
        }

        CachedTexture cachedTexture = Objects.requireNonNull(cache.get(key));
        if (cachedTexture.release() == 0) {
            cache.remove(key);
        }
    }

    public static synchronized boolean contains(String key) {
        if (Objects.isNull(key)) {
            throw new IllegalArgumentException("key");
        }

        if (!cache.containsKey(key)) {
            return false;
        }

        CachedTexture cachedTexture = cache.get(key);
        if (cachedTexture == null) {
            cache.remove(key);
            return false;
        }

        return cache.containsKey(key);
    }
}
