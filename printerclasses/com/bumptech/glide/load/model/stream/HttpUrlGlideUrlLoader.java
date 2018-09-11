package com.bumptech.glide.load.model.stream;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

public class HttpUrlGlideUrlLoader implements ModelLoader {
   private final ModelCache modelCache;

   public HttpUrlGlideUrlLoader() {
      this((ModelCache)null);
   }

   public HttpUrlGlideUrlLoader(ModelCache var1) {
      this.modelCache = var1;
   }

   public DataFetcher getResourceFetcher(GlideUrl var1, int var2, int var3) {
      GlideUrl var4 = var1;
      if(this.modelCache != null) {
         GlideUrl var5 = (GlideUrl)this.modelCache.get(var1, 0, 0);
         var4 = var5;
         if(var5 == null) {
            this.modelCache.put(var1, 0, 0, var1);
            var4 = var1;
         }
      }

      return new HttpUrlFetcher(var4);
   }

   public static class Factory implements ModelLoaderFactory {
      private final ModelCache modelCache = new ModelCache(500);

      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new HttpUrlGlideUrlLoader(this.modelCache);
      }

      public void teardown() {
      }
   }
}
