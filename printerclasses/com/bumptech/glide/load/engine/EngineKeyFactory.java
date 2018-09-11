package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.EngineKey;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

class EngineKeyFactory {
   public EngineKey buildKey(String var1, Key var2, int var3, int var4, ResourceDecoder var5, ResourceDecoder var6, Transformation var7, ResourceEncoder var8, ResourceTranscoder var9, Encoder var10) {
      return new EngineKey(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }
}
