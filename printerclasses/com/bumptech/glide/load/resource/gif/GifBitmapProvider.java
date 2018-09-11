package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

class GifBitmapProvider implements GifDecoder.BitmapProvider {
   private final BitmapPool bitmapPool;

   public GifBitmapProvider(BitmapPool var1) {
      this.bitmapPool = var1;
   }

   public Bitmap obtain(int var1, int var2, Config var3) {
      return this.bitmapPool.getDirty(var1, var2, var3);
   }

   public void release(Bitmap var1) {
      if(!this.bitmapPool.put(var1)) {
         var1.recycle();
      }

   }
}
