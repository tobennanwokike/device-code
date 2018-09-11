package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import java.io.InputStream;

public class StreamFileLoader extends FileLoader implements StreamModelLoader {
   public StreamFileLoader(Context var1) {
      this(Glide.buildStreamModelLoader(Uri.class, var1));
   }

   public StreamFileLoader(ModelLoader var1) {
      super(var1);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new StreamFileLoader(var2.buildModelLoader(Uri.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
