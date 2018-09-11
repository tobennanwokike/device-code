package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class UnitTranscoder implements ResourceTranscoder {
   private static final UnitTranscoder UNIT_TRANSCODER = new UnitTranscoder();

   public static ResourceTranscoder get() {
      return UNIT_TRANSCODER;
   }

   public String getId() {
      return "";
   }

   public Resource transcode(Resource var1) {
      return var1;
   }
}
