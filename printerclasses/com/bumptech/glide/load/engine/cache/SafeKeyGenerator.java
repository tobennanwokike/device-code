package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;

class SafeKeyGenerator {
   private final LruCache loadIdToSafeHash = new LruCache(1000);

   public String getSafeKey(Key param1) {
      // $FF: Couldn't be decompiled
   }
}
