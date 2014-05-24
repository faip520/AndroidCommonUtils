package com.A1w0n.androidcommonutils.asyncimageutils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Memory cache manages items in it and makes sure its size is never greater than given.
 */
public class MemoryCache {

    private static final String TAG = "FaipMemoryCache";
    
    // Get a concurrent LinkedHashMap instance. Last argument true for LRU ordering
    private Map<String, Bitmap> cache=Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
    // Current allocated size
    private long size=0;
    // Max memory in bytes
    private long limit=1000000;

    public MemoryCache() {
        // Use 10% of available heap size by default.
        setLimit(Runtime.getRuntime().maxMemory() / 10);
    }
    
    /**
     * Set memory limit.
     */
    public void setLimit(long new_limit) {
        limit = new_limit;
    }

    /**
     * Get bitmap from cache.
     */
    public Bitmap get(String id) {
        try{
            if(!cache.containsKey(id))
                return null;
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            return cache.get(id);
        }catch(NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Put bitmap into cache.
     */
    public void put(String id, Bitmap bitmap) {
        try{
            if(cache.containsKey(id))
                size -= getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th) {
            th.printStackTrace();
        }
    }
    
    private void checkSize() {
        Log.i(TAG, "cache size=" + size + " length=" + cache.size());
        if(size > limit) {
        	// Least recently accessed item will be the first one iterated 
            Iterator<Entry<String, Bitmap>> iter=cache.entrySet().iterator(); 
            while(iter.hasNext()){
                Entry<String, Bitmap> entry=iter.next();
                size-=getSizeInBytes(entry.getValue());
                iter.remove();
                if(size <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache. New size = " + cache.size());
        }
    }

    public void clear() {
        try {
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            cache.clear();
            size=0;
        } catch(NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}