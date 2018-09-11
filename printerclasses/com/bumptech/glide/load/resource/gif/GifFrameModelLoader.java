package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.Priority;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;

class GifFrameModelLoader implements ModelLoader {
   public DataFetcher getResourceFetcher(GifDecoder var1, int var2, int var3) {
      return new GifFrameModelLoader.GifFrameDataFetcher(var1);
   }

   private static class GifFrameDataFetcher implements DataFetcher {
      private final GifDecoder decoder;

      public GifFrameDataFetcher(GifDecoder var1) {
         this.decoder = var1;
      }

      public void cancel() {
      }

      public void cleanup() {
      }

      public String getId() {
         return String.valueOf(this.decoder.getCurrentFrameIndex());
      }

      public GifDecoder loadData(Priority var1) {
         return this.decoder;
      }
   }
}
