package com.spineplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.spineplayer.spine.AnimationNamePair;
import com.spineplayer.spine.Physics;
import com.spineplayer.utils.VolleyRequestQueue;
import com.spineplayer.cache.TextureCache;

import java.util.Map;

public class SpinePlayerView extends ImageView {
    private final VolleyRequestQueue requestQueue;
    private final SpineDrawable drawable = new SpineDrawable(this);

    public SpinePlayerView(Context context) {
        super(context);
        requestQueue = new VolleyRequestQueue(context);
        this.setImageDrawable(drawable);
    }

    private int getResourceDrawableId(@Nullable String name) {
        if (name == null || name.isEmpty()) {
            return 0;
        }

        name = name.toLowerCase().replace("-", "_");
        return getContext().getResources().getIdentifier(
                name,
                "drawable",
                getContext().getPackageName());
    }

    private void loadBitmapByLocalResource(String uri) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), getResourceDrawableId(uri));
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        TextureCache.add(fileName, bitmap);
        drawable.setImageUri(uri);
    }

    private void loadBitmapByExternalURL(String uri) {
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        fileName = fileName.split("\\?")[0];

        if (TextureCache.contains(fileName)) {
            drawable.setImageUri(uri);
            TextureCache.acquire(fileName);
            return;
        }

        String finalFileName = fileName;
        ImageRequest request = new ImageRequest(uri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                TextureCache.add(finalFileName, response);
                TextureCache.acquire(finalFileName);
                drawable.setImageUri(uri);
            }
        }, Integer.MAX_VALUE, Integer.MAX_VALUE, ScaleType.CENTER, null, null);
        requestQueue.add(request);
    }

    public void setImageUri(String uri) {
        if (uri.startsWith("http")) {
            loadBitmapByExternalURL(uri);
        } else {
            loadBitmapByLocalResource(uri);
        }
    }

    public void start() {
        drawable.start();
    }

    public void stop() {
        drawable.stop();
    }

    public void reset() {
        drawable.reset();
    }

    public void setWidth(int value) {
        drawable.setCanvasWidth(value);
    }

    public void setHeight(int value) {
        drawable.setCanvasHeight(value);
    }

    public void setAtlasData(String value) {
        drawable.setAtlasData(value);
    }

    public void setSkeletonData(String value) {
        drawable.setSkeletonDataFile(value);
    }

    public void setAnimationNames(@NonNull String[] values) {
        drawable.setAnimationNames(values);
    }

    public void setLoopAnimation(boolean value) {
        drawable.setLoopAnimation(value);
    }

    public void setDefaultMix(float defaultMix) {
        drawable.setDefaultMix(defaultMix);
    }

    public void setMixes(Map<AnimationNamePair, Float> mixes) {
        drawable.setMixes(mixes);
    }

    public void setX(float value) {
        drawable.setX(value);
    }

    public void setY(float value) {
        drawable.setY(value);
    }

    public void setPlaybackSpeed(float value) {
        drawable.setPlaybackSpeed(value);
    }

    public void setAutoPlay(boolean value) {
        drawable.setAutoPlay(value);
    }

    public void setPhysics(Physics physics) {
         drawable.setPhysics(physics);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        drawable.close();
    }
}

