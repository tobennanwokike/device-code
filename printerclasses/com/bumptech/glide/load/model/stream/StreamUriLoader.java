package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.UriLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import java.io.InputStream;

public class StreamUriLoader extends UriLoader implements StreamModelLoader {
   public StreamUriLoader(Context var1) {
      this(var1, Glide.buildStreamModelLoader(GlideUrl.class, var1));
   }

   public StreamUriLoader(Context var1, ModelLoader var2) {
      super(var1, var2);
   }

   protected DataFetcher getAssetPathFetcher(Context var1, String var2) {
      return new StreamAssetPathFetcher(var1.getApplicationContext().getAssets(), var2);
   }

   protected DataFetcher getLocalUriFetcher(Context var1, Uri var2) {
      return new StreamLocalUriFetcher(var1, var2);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new StreamUriLoader(var1, var2.buildModelLoader(GlideUrl.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
