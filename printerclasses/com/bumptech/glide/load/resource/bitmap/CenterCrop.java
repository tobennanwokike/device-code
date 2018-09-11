package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

public class CenterCrop extends BitmapTransformation {
   public CenterCrop(Context var1) {
      super(var1);
   }

   public CenterCrop(BitmapPool var1) {
      super(var1);
   }

   public String getId() {
      return "CenterCrop.com.bumptech.glide.load.resource.bitmap";
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      Config var5;
      if(var2.getConfig() != null) {
         var5 = var2.getConfig();
      } else {
         var5 = Config.ARGB_8888;
      }

      Bitmap var6 = var1.get(var3, var4, var5);
      var2 = TransformationUtils.centerCrop(var6, var2, var3, var4);
      if(var6 != null && var6 != var2 && !var1.put(var6)) {
         var6.recycle();
      }

      return var2;
   }
}
