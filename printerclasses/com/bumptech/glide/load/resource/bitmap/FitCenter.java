package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

public class FitCenter extends BitmapTransformation {
   public FitCenter(Context var1) {
      super(var1);
   }

   public FitCenter(BitmapPool var1) {
      super(var1);
   }

   public String getId() {
      return "FitCenter.com.bumptech.glide.load.resource.bitmap";
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      return TransformationUtils.fitCenter(var2, var1, var3, var4);
   }
}
