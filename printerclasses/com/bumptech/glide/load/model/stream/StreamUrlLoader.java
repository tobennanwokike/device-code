package com.bumptech.glide.load.model.stream;

import android.content.Context;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.UrlLoader;
import java.io.InputStream;

public class StreamUrlLoader extends UrlLoader {
   public StreamUrlLoader(ModelLoader var1) {
      super(var1);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new StreamUrlLoader(var2.buildModelLoader(GlideUrl.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
