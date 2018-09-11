package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import java.io.InputStream;

public abstract class BaseGlideUrlLoader implements StreamModelLoader {
   private final ModelLoader concreteLoader;
   private final ModelCache modelCache;

   public BaseGlideUrlLoader(Context var1) {
      this((Context)var1, (ModelCache)null);
   }

   public BaseGlideUrlLoader(Context var1, ModelCache var2) {
      this(Glide.buildModelLoader(GlideUrl.class, InputStream.class, var1), var2);
   }

   public BaseGlideUrlLoader(ModelLoader var1) {
      this((ModelLoader)var1, (ModelCache)null);
   }

   public BaseGlideUrlLoader(ModelLoader var1, ModelCache var2) {
      this.concreteLoader = var1;
      this.modelCache = var2;
   }

   protected Headers getHeaders(Object var1, int var2, int var3) {
      return Headers.DEFAULT;
   }

   public DataFetcher getResourceFetcher(Object var1, int var2, int var3) {
      GlideUrl var4 = null;
      if(this.modelCache != null) {
         var4 = (GlideUrl)this.modelCache.get(var1, var2, var3);
      }

      GlideUrl var5 = var4;
      DataFetcher var7;
      if(var4 == null) {
         String var6 = this.getUrl(var1, var2, var3);
         if(TextUtils.isEmpty(var6)) {
            var7 = null;
            return var7;
         }

         var4 = new GlideUrl(var6, this.getHeaders(var1, var2, var3));
         var5 = var4;
         if(this.modelCache != null) {
            this.modelCache.put(var1, var2, var3, var4);
            var5 = var4;
         }
      }

      var7 = this.concreteLoader.getResourceFetcher(var5, var2, var3);
      return var7;
   }

   protected abstract String getUrl(Object var1, int var2, int var3);
}
