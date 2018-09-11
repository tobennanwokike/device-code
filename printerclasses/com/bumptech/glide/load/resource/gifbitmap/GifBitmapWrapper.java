package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.engine.Resource;

public class GifBitmapWrapper {
   private final Resource bitmapResource;
   private final Resource gifResource;

   public GifBitmapWrapper(Resource var1, Resource var2) {
      if(var1 != null && var2 != null) {
         throw new IllegalArgumentException("Can only contain either a bitmap resource or a gif resource, not both");
      } else if(var1 == null && var2 == null) {
         throw new IllegalArgumentException("Must contain either a bitmap resource or a gif resource");
      } else {
         this.bitmapResource = var1;
         this.gifResource = var2;
      }
   }

   public Resource getBitmapResource() {
      return this.bitmapResource;
   }

   public Resource getGifResource() {
      return this.gifResource;
   }

   public int getSize() {
      int var1;
      if(this.bitmapResource != null) {
         var1 = this.bitmapResource.getSize();
      } else {
         var1 = this.gifResource.getSize();
      }

      return var1;
   }
}
