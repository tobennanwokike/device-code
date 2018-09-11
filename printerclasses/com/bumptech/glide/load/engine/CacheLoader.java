package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.io.File;
import java.io.IOException;

class CacheLoader {
   private static final String TAG = "CacheLoader";
   private final DiskCache diskCache;

   public CacheLoader(DiskCache var1) {
      this.diskCache = var1;
   }

   public Resource load(Key var1, ResourceDecoder var2, int var3, int var4) {
      File var6 = this.diskCache.get(var1);
      Resource var5;
      if(var6 == null) {
         var5 = null;
      } else {
         var5 = null;

         Resource var8;
         try {
            var8 = var2.decode(var6, var3, var4);
         } catch (IOException var7) {
            var8 = var5;
            if(Log.isLoggable("CacheLoader", 3)) {
               Log.d("CacheLoader", "Exception decoding image from cache", var7);
               var8 = var5;
            }
         }

         var5 = var8;
         if(var8 == null) {
            if(Log.isLoggable("CacheLoader", 3)) {
               Log.d("CacheLoader", "Failed to decode image from cache or not present in cache");
            }

            this.diskCache.delete(var1);
            var5 = var8;
         }
      }

      return var5;
   }
}
