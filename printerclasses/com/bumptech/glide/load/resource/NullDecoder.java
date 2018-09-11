package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;

public class NullDecoder implements ResourceDecoder {
   private static final NullDecoder NULL_DECODER = new NullDecoder();

   public static NullDecoder get() {
      return NULL_DECODER;
   }

   public Resource decode(Object var1, int var2, int var3) {
      return null;
   }

   public String getId() {
      return "";
   }
}
