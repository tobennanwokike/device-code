package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache;

public class MemoryCacheAdapter implements MemoryCache {
   private MemoryCache.ResourceRemovedListener listener;

   public void clearMemory() {
   }

   public int getCurrentSize() {
      return 0;
   }

   public int getMaxSize() {
      return 0;
   }

   public Resource put(Key var1, Resource var2) {
      this.listener.onResourceRemoved(var2);
      return null;
   }

   public Resource remove(Key var1) {
      return null;
   }

   public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener var1) {
      this.listener = var1;
   }

   public void setSizeMultiplier(float var1) {
   }

   public void trimMemory(int var1) {
   }
}
