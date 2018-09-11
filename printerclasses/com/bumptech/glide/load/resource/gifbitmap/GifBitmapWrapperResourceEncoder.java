package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import java.io.OutputStream;

public class GifBitmapWrapperResourceEncoder implements ResourceEncoder {
   private final ResourceEncoder bitmapEncoder;
   private final ResourceEncoder gifEncoder;
   private String id;

   public GifBitmapWrapperResourceEncoder(ResourceEncoder var1, ResourceEncoder var2) {
      this.bitmapEncoder = var1;
      this.gifEncoder = var2;
   }

   public boolean encode(Resource var1, OutputStream var2) {
      GifBitmapWrapper var5 = (GifBitmapWrapper)var1.get();
      Resource var4 = var5.getBitmapResource();
      boolean var3;
      if(var4 != null) {
         var3 = this.bitmapEncoder.encode(var4, var2);
      } else {
         var3 = this.gifEncoder.encode(var5.getGifResource(), var2);
      }

      return var3;
   }

   public String getId() {
      if(this.id == null) {
         this.id = this.bitmapEncoder.getId() + this.gifEncoder.getId();
      }

      return this.id;
   }
}
