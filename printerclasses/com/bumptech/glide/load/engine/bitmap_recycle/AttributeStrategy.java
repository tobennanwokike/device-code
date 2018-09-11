package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.load.engine.bitmap_recycle.BaseKeyPool;
import com.bumptech.glide.load.engine.bitmap_recycle.GroupedLinkedMap;
import com.bumptech.glide.load.engine.bitmap_recycle.LruPoolStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import com.bumptech.glide.util.Util;

class AttributeStrategy implements LruPoolStrategy {
   private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
   private final AttributeStrategy.KeyPool keyPool = new AttributeStrategy.KeyPool();

   private static String getBitmapString(int var0, int var1, Config var2) {
      return "[" + var0 + "x" + var1 + "], " + var2;
   }

   private static String getBitmapString(Bitmap var0) {
      return getBitmapString(var0.getWidth(), var0.getHeight(), var0.getConfig());
   }

   public Bitmap get(int var1, int var2, Config var3) {
      AttributeStrategy.Key var4 = this.keyPool.get(var1, var2, var3);
      return (Bitmap)this.groupedMap.get(var4);
   }

   public int getSize(Bitmap var1) {
      return Util.getBitmapByteSize(var1);
   }

   public String logBitmap(int var1, int var2, Config var3) {
      return getBitmapString(var1, var2, var3);
   }

   public String logBitmap(Bitmap var1) {
      return getBitmapString(var1);
   }

   public void put(Bitmap var1) {
      AttributeStrategy.Key var2 = this.keyPool.get(var1.getWidth(), var1.getHeight(), var1.getConfig());
      this.groupedMap.put(var2, var1);
   }

   public Bitmap removeLast() {
      return (Bitmap)this.groupedMap.removeLast();
   }

   public String toString() {
      return "AttributeStrategy:\n  " + this.groupedMap;
   }

   static class Key implements Poolable {
      private Config config;
      private int height;
      private final AttributeStrategy.KeyPool pool;
      private int width;

      public Key(AttributeStrategy.KeyPool var1) {
         this.pool = var1;
      }

      public boolean equals(Object var1) {
         boolean var3 = false;
         boolean var2 = var3;
         if(var1 instanceof AttributeStrategy.Key) {
            AttributeStrategy.Key var4 = (AttributeStrategy.Key)var1;
            var2 = var3;
            if(this.width == var4.width) {
               var2 = var3;
               if(this.height == var4.height) {
                  var2 = var3;
                  if(this.config == var4.config) {
                     var2 = true;
                  }
               }
            }
         }

         return var2;
      }

      public int hashCode() {
         int var2 = this.width;
         int var3 = this.height;
         int var1;
         if(this.config != null) {
            var1 = this.config.hashCode();
         } else {
            var1 = 0;
         }

         return (var2 * 31 + var3) * 31 + var1;
      }

      public void init(int var1, int var2, Config var3) {
         this.width = var1;
         this.height = var2;
         this.config = var3;
      }

      public void offer() {
         this.pool.offer(this);
      }

      public String toString() {
         return AttributeStrategy.getBitmapString(this.width, this.height, this.config);
      }
   }

   static class KeyPool extends BaseKeyPool {
      protected AttributeStrategy.Key create() {
         return new AttributeStrategy.Key(this);
      }

      public AttributeStrategy.Key get(int var1, int var2, Config var3) {
         AttributeStrategy.Key var4 = (AttributeStrategy.Key)this.get();
         var4.init(var1, var2, var3);
         return var4;
      }
   }
}
