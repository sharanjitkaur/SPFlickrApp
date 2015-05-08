package com.sharanjit.spflickrapp;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MyCache {
    static LruCache<String, Bitmap> imgcache = null;

    static void setCache(){
        imgcache = new LruCache<String, Bitmap>((4 * 1024 * 1024));
    }
}
