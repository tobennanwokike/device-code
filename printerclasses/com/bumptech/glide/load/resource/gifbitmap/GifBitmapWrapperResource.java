package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;

public class GifBitmapWrapperResource implements Resource {
   private final GifBitmapWrapper data;

   public GifBitmapWrapperResource(GifBitmapWrapper var1) {
      if(var1 == null) {
         throw new NullPointerException("Data must not be null");
      } else {
         this.data = var1;
      }
   }

   public GifBitmapWrapper get() {
      return this.data;
   }

   public int getSize() {
      return this.data.getSize();
   }

   public void recycle() {
      Resource var1 = this.data.getBitmapResource();
      if(var1 != null) {
         var1.recycle();
      }

      var1 = this.data.getGifResource();
      if(var1 != null) {
         var1.recycle();
      }

   }
}
