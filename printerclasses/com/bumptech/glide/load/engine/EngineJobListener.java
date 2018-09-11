package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.EngineJob;
import com.bumptech.glide.load.engine.EngineResource;

interface EngineJobListener {
   void onEngineJobCancelled(EngineJob var1, Key var2);

   void onEngineJobComplete(Key var1, EngineResource var2);
}
