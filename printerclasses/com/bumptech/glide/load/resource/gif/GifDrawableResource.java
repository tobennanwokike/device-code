package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.Util;

public class GifDrawableResource extends DrawableResource {
   public GifDrawableResource(GifDrawable var1) {
      super(var1);
   }

   public int getSize() {
      int var1 = ((GifDrawable)this.drawable).getData().length;
      return Util.getBitmapByteSize(((GifDrawable)this.drawable).getFirstFrame()) + var1;
   }

   public void recycle() {
      ((GifDrawable)this.drawable).stop();
      ((GifDrawable)this.drawable).recycle();
   }
}
