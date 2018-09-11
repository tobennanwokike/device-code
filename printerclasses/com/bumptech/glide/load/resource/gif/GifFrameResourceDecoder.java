package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

class GifFrameResourceDecoder implements ResourceDecoder {
   private final BitmapPool bitmapPool;

   public GifFrameResourceDecoder(BitmapPool var1) {
      this.bitmapPool = var1;
   }

   public Resource decode(GifDecoder var1, int var2, int var3) {
      return BitmapResource.obtain(var1.getNextFrame(), this.bitmapPool);
   }

   public String getId() {
      return "GifFrameResourceDecoder.com.bumptech.glide.load.resource.gif";
   }
}
