package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.OutputStream;

public class NullResourceEncoder implements ResourceEncoder {
   private static final NullResourceEncoder NULL_ENCODER = new NullResourceEncoder();

   public static NullResourceEncoder get() {
      return NULL_ENCODER;
   }

   public boolean encode(Resource var1, OutputStream var2) {
      return false;
   }

   public String getId() {
      return "";
   }
}
