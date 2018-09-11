package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import java.io.File;

public final class ExternalCacheDiskCacheFactory extends DiskLruCacheFactory {
   public ExternalCacheDiskCacheFactory(Context var1) {
      this(var1, "image_manager_disk_cache", 262144000);
   }

   public ExternalCacheDiskCacheFactory(Context var1, int var2) {
      this(var1, "image_manager_disk_cache", var2);
   }

   public ExternalCacheDiskCacheFactory(final Context var1, final String var2, int var3) {
      super(new DiskLruCacheFactory.CacheDirectoryGetter() {
         public File getCacheDirectory() {
            File var2x = var1.getExternalCacheDir();
            File var1x;
            if(var2x == null) {
               var1x = null;
            } else {
               var1x = var2x;
               if(var2 != null) {
                  var1x = new File(var2x, var2);
               }
            }

            return var1x;
         }
      }, var3);
   }
}
