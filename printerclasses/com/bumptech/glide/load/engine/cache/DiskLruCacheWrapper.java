package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheWriteLocker;
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper implements DiskCache {
   private static final int APP_VERSION = 1;
   private static final String TAG = "DiskLruCacheWrapper";
   private static final int VALUE_COUNT = 1;
   private static DiskLruCacheWrapper wrapper = null;
   private final File directory;
   private DiskLruCache diskLruCache;
   private final int maxSize;
   private final SafeKeyGenerator safeKeyGenerator;
   private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

   protected DiskLruCacheWrapper(File var1, int var2) {
      this.directory = var1;
      this.maxSize = var2;
      this.safeKeyGenerator = new SafeKeyGenerator();
   }

   public static DiskCache get(File var0, int var1) {
      synchronized(DiskLruCacheWrapper.class){}

      DiskLruCacheWrapper var5;
      try {
         if(wrapper == null) {
            DiskLruCacheWrapper var2 = new DiskLruCacheWrapper(var0, var1);
            wrapper = var2;
         }

         var5 = wrapper;
      } finally {
         ;
      }

      return var5;
   }

   private DiskLruCache getDiskCache() throws IOException {
      synchronized(this){}

      DiskLruCache var1;
      try {
         if(this.diskLruCache == null) {
            this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, (long)this.maxSize);
         }

         var1 = this.diskLruCache;
      } finally {
         ;
      }

      return var1;
   }

   private void resetDiskCache() {
      synchronized(this){}

      try {
         this.diskLruCache = null;
      } finally {
         ;
      }

   }

   public void clear() {
      synchronized(this){}

      try {
         this.getDiskCache().delete();
         this.resetDiskCache();
      } catch (IOException var4) {
         if(Log.isLoggable("DiskLruCacheWrapper", 5)) {
            Log.w("DiskLruCacheWrapper", "Unable to clear disk cache", var4);
         }
      } finally {
         ;
      }

   }

   public void delete(Key var1) {
      String var3 = this.safeKeyGenerator.getSafeKey(var1);

      try {
         this.getDiskCache().remove(var3);
      } catch (IOException var2) {
         if(Log.isLoggable("DiskLruCacheWrapper", 5)) {
            Log.w("DiskLruCacheWrapper", "Unable to delete from disk cache", var2);
         }
      }

   }

   public File get(Key param1) {
      // $FF: Couldn't be decompiled
   }

   public void put(Key param1, DiskCache.Writer param2) {
      // $FF: Couldn't be decompiled
   }
}
