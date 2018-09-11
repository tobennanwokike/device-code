package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class GifBitmapWrapperDrawableTranscoder implements ResourceTranscoder {
   private final ResourceTranscoder bitmapDrawableResourceTranscoder;

   public GifBitmapWrapperDrawableTranscoder(ResourceTranscoder var1) {
      this.bitmapDrawableResourceTranscoder = var1;
   }

   public String getId() {
      return "GifBitmapWrapperDrawableTranscoder.com.bumptech.glide.load.resource.transcode";
   }

   public Resource transcode(Resource var1) {
      GifBitmapWrapper var2 = (GifBitmapWrapper)var1.get();
      var1 = var2.getBitmapResource();
      if(var1 != null) {
         var1 = this.bitmapDrawableResourceTranscoder.transcode(var1);
      } else {
         var1 = var2.getGifResource();
      }

      return var1;
   }
}
