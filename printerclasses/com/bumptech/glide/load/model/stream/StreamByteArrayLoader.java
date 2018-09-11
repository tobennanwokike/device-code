package com.bumptech.glide.load.model.stream;

import android.content.Context;
import com.bumptech.glide.load.data.ByteArrayFetcher;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

public class StreamByteArrayLoader implements StreamModelLoader {
   private final String id;

   public StreamByteArrayLoader() {
      this("");
   }

   @Deprecated
   public StreamByteArrayLoader(String var1) {
      this.id = var1;
   }

   public DataFetcher getResourceFetcher(byte[] var1, int var2, int var3) {
      return new ByteArrayFetcher(var1, this.id);
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(Context var1, GenericLoaderFactory var2) {
         return new StreamByteArrayLoader();
      }

      public void teardown() {
      }
   }
}
