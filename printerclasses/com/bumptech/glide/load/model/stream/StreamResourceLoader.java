package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import java.io.InputStream;

public class StreamResourceLoader extends ResourceLoader implements StreamModelLoader {
   public StreamResourceLoader(Context var1) {
      this(var1, Glide.buildStreamModelLoader(Uri.class, var1));
   }

   public StreamResourceLoader(Context var1, ModelLoader var2) {
      super(var1, var2);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new StreamResourceLoader(var1, var2.buildModelLoader(Uri.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
