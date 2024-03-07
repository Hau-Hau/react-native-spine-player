package com.spineplayer.utils;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class VolleyRequestQueue {
    private final ImageLoader imageLoader;
    private final RequestQueue requestQueue;

    public VolleyRequestQueue(Context context) {
        requestQueue = newRequestQueue(context.getApplicationContext());

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public <T> void add(Request<T> req) {
        requestQueue.add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
