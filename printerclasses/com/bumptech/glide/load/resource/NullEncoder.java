package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.Encoder;
import java.io.OutputStream;

public class NullEncoder implements Encoder {
   private static final NullEncoder NULL_ENCODER = new NullEncoder();

   public static Encoder get() {
      return NULL_ENCODER;
   }

   public boolean encode(Object var1, OutputStream var2) {
      return false;
   }

   public String getId() {
      return "";
   }
}
