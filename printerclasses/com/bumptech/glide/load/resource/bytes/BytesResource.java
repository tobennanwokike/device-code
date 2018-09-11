package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.engine.Resource;

public class BytesResource implements Resource {
   private final byte[] bytes;

   public BytesResource(byte[] var1) {
      if(var1 == null) {
         throw new NullPointerException("Bytes must not be null");
      } else {
         this.bytes = var1;
      }
   }

   public byte[] get() {
      return this.bytes;
   }

   public int getSize() {
      return this.bytes.length;
   }

   public void recycle() {
   }
}
