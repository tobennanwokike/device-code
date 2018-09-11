package com.bumptech.glide.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruCache {
   private final LinkedHashMap cache = new LinkedHashMap(100, 0.75F, true);
   private int currentSize = 0;
   private final int initialMaxSize;
   private int maxSize;

   public LruCache(int var1) {
      this.initialMaxSize = var1;
      this.maxSize = var1;
   }

   private void evict() {
      this.trimToSize(this.maxSize);
   }

   public void clearMemory() {
      this.trimToSize(0);
   }

   public boolean contains(Object var1) {
      return this.cache.containsKey(var1);
   }

   public Object get(Object var1) {
      return this.cache.get(var1);
   }

   public int getCurrentSize() {
      return this.currentSize;
   }

   public int getMaxSize() {
      return this.maxSize;
   }

   protected int getSize(Object var1) {
      return 1;
   }

   protected void onItemEvicted(Object var1, Object var2) {
   }

   public Object put(Object var1, Object var2) {
      if(this.getSize(var2) >= this.maxSize) {
         this.onItemEvicted(var1, var2);
         var1 = null;
      } else {
         var1 = this.cache.put(var1, var2);
         if(var2 != null) {
            this.currentSize += this.getSize(var2);
         }

         if(var1 != null) {
            this.currentSize -= this.getSize(var1);
         }

         this.evict();
      }

      return var1;
   }

   public Object remove(Object var1) {
      var1 = this.cache.remove(var1);
      if(var1 != null) {
         this.currentSize -= this.getSize(var1);
      }

      return var1;
   }

   public void setSizeMultiplier(float var1) {
      if(var1 < 0.0F) {
         throw new IllegalArgumentException("Multiplier must be >= 0");
      } else {
         this.maxSize = Math.round((float)this.initialMaxSize * var1);
         this.evict();
      }
   }

   protected void trimToSize(int var1) {
      while(this.currentSize > var1) {
         Entry var3 = (Entry)this.cache.entrySet().iterator().next();
         Object var2 = var3.getValue();
         this.currentSize -= this.getSize(var2);
         Object var4 = var3.getKey();
         this.cache.remove(var4);
         this.onItemEvicted(var4, var2);
      }

   }
}
