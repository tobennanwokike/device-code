package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;

public abstract class BitmapTransformation implements Transformation {
   private BitmapPool bitmapPool;

   public BitmapTransformation(Context var1) {
      this(Glide.get(var1).getBitmapPool());
   }

   public BitmapTransformation(BitmapPool var1) {
      this.bitmapPool = var1;
   }

   protected abstract Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4);

   public final Resource transform(Resource var1, int var2, int var3) {
      if(!Util.isValidDimensions(var2, var3)) {
         throw new IllegalArgumentException("Cannot apply transformation on width: " + var2 + " or height: " + var3 + " less than or equal to zero and not Target.SIZE_ORIGINAL");
      } else {
         Bitmap var5 = (Bitmap)((Resource)var1).get();
         if(var2 == Integer.MIN_VALUE) {
            var2 = var5.getWidth();
         }

         if(var3 == Integer.MIN_VALUE) {
            var3 = var5.getHeight();
         }

         Bitmap var4 = this.transform(this.bitmapPool, var5, var2, var3);
         if(!var5.equals(var4)) {
            var1 = BitmapResource.obtain(var4, this.bitmapPool);
         }

         return (Resource)var1;
      }
   }
}
