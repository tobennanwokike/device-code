package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableResource;

public class GifDrawableTransformation implements Transformation {
   private final BitmapPool bitmapPool;
   private final Transformation wrapped;

   public GifDrawableTransformation(Transformation var1, BitmapPool var2) {
      this.wrapped = var1;
      this.bitmapPool = var2;
   }

   public String getId() {
      return this.wrapped.getId();
   }

   public Resource transform(Resource var1, int var2, int var3) {
      GifDrawable var5 = (GifDrawable)((Resource)var1).get();
      Bitmap var4 = ((GifDrawable)((Resource)var1).get()).getFirstFrame();
      BitmapResource var6 = new BitmapResource(var4, this.bitmapPool);
      Bitmap var7 = (Bitmap)this.wrapped.transform(var6, var2, var3).get();
      if(!var7.equals(var4)) {
         var1 = new GifDrawableResource(new GifDrawable(var5, var7, this.wrapped));
      }

      return (Resource)var1;
   }
}
