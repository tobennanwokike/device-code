package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResource;

public class GifBitmapWrapperTransformation implements Transformation {
   private final Transformation bitmapTransformation;
   private final Transformation gifDataTransformation;

   GifBitmapWrapperTransformation(Transformation var1, Transformation var2) {
      this.bitmapTransformation = var1;
      this.gifDataTransformation = var2;
   }

   public GifBitmapWrapperTransformation(BitmapPool var1, Transformation var2) {
      this((Transformation)var2, new GifDrawableTransformation(var2, var1));
   }

   public String getId() {
      return this.bitmapTransformation.getId();
   }

   public Resource transform(Resource var1, int var2, int var3) {
      Resource var5 = ((GifBitmapWrapper)var1.get()).getBitmapResource();
      Resource var6 = ((GifBitmapWrapper)var1.get()).getGifResource();
      Object var4;
      if(var5 != null && this.bitmapTransformation != null) {
         var6 = this.bitmapTransformation.transform(var5, var2, var3);
         var4 = var1;
         if(!var5.equals(var6)) {
            var4 = new GifBitmapWrapperResource(new GifBitmapWrapper(var6, ((GifBitmapWrapper)var1.get()).getGifResource()));
         }
      } else {
         var4 = var1;
         if(var6 != null) {
            var4 = var1;
            if(this.gifDataTransformation != null) {
               var5 = this.gifDataTransformation.transform(var6, var2, var3);
               var4 = var1;
               if(!var6.equals(var5)) {
                  var4 = new GifBitmapWrapperResource(new GifBitmapWrapper(((GifBitmapWrapper)var1.get()).getBitmapResource(), var5));
               }
            }
         }
      }

      return (Resource)var4;
   }
}
