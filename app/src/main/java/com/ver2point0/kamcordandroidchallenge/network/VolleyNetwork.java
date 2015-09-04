package com.ver2point0.kamcordandroidchallenge.network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ver2point0.kamcordandroidchallenge.context.MyApplication;

public class VolleyNetwork {

    private static VolleyNetwork sInstance;
    private final ImageLoader mImageLoader;
    private final RequestQueue mRequestQueue;

    private VolleyNetwork() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            // an image cache
            // returns memory in bytes
            private final LruCache<String, Bitmap> imageCache =
                    new LruCache<String, Bitmap>((int) Runtime.getRuntime().maxMemory()
                            / 1024 / 8);

            @Override
            public Bitmap getBitmap(String url) {
                return imageCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                imageCache.put(url, bitmap);
            }
        });
    }

    public static VolleyNetwork getInstance() {
        if (sInstance == null) {
            sInstance = new VolleyNetwork();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
