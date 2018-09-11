package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.util.MultiClassKey;
import java.util.HashMap;
import java.util.Map;

public class TranscoderRegistry {
   private static final MultiClassKey GET_KEY = new MultiClassKey();
   private final Map factories = new HashMap();

   public ResourceTranscoder get(Class param1, Class param2) {
      // $FF: Couldn't be decompiled
   }

   public void register(Class var1, Class var2, ResourceTranscoder var3) {
      this.factories.put(new MultiClassKey(var1, var2), var3);
   }
}
