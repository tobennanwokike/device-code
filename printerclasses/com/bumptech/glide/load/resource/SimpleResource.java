package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.engine.Resource;

public class SimpleResource implements Resource {
   protected final Object data;

   public SimpleResource(Object var1) {
      if(var1 == null) {
         throw new NullPointerException("Data must not be null");
      } else {
         this.data = var1;
      }
   }

   public final Object get() {
      return this.data;
   }

   public final int getSize() {
      return 1;
   }

   public void recycle() {
   }
}
