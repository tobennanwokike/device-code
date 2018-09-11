package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.load.engine.bitmap_recycle.BaseKeyPool;
import com.bumptech.glide.load.engine.bitmap_recycle.GroupedLinkedMap;
import com.bumptech.glide.load.engine.bitmap_recycle.LruPoolStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import com.bumptech.glide.load.engine.bitmap_recycle.PrettyPrintTreeMap;
import com.bumptech.glide.util.Util;
import java.util.TreeMap;

@TargetApi(19)
class SizeStrategy implements LruPoolStrategy {
   private static final int MAX_SIZE_MULTIPLE = 8;
   private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
   private final SizeStrategy.KeyPool keyPool = new SizeStrategy.KeyPool();
   private final TreeMap sortedSizes = new PrettyPrintTreeMap();

   private void decrementBitmapOfSize(Integer var1) {
      Integer var2 = (Integer)this.sortedSizes.get(var1);
      if(var2.intValue() == 1) {
         this.sortedSizes.remove(var1);
      } else {
         this.sortedSizes.put(var1, Integer.valueOf(var2.intValue() - 1));
      }

   }

   private static String getBitmapString(int var0) {
      return "[" + var0 + "]";
   }

   private static String getBitmapString(Bitmap var0) {
      return getBitmapString(Util.getBitmapByteSize(var0));
   }

   public Bitmap get(int var1, int var2, Config var3) {
      int var4 = Util.getBitmapByteSize(var1, var2, var3);
      SizeStrategy.Key var6 = this.keyPool.get(var4);
      Integer var7 = (Integer)this.sortedSizes.ceilingKey(Integer.valueOf(var4));
      SizeStrategy.Key var5 = var6;
      if(var7 != null) {
         var5 = var6;
         if(var7.intValue() != var4) {
            var5 = var6;
            if(var7.intValue() <= var4 * 8) {
               this.keyPool.offer(var6);
               var5 = this.keyPool.get(var7.intValue());
            }
         }
      }

      Bitmap var8 = (Bitmap)this.groupedMap.get(var5);
      if(var8 != null) {
         var8.reconfigure(var1, var2, var3);
         this.decrementBitmapOfSize(var7);
      }

      return var8;
   }

   public int getSize(Bitmap var1) {
      return Util.getBitmapByteSize(var1);
   }

   public String logBitmap(int var1, int var2, Config var3) {
      return getBitmapString(Util.getBitmapByteSize(var1, var2, var3));
   }

   public String logBitmap(Bitmap var1) {
      return getBitmapString(var1);
   }

   public void put(Bitmap var1) {
      int var2 = Util.getBitmapByteSize(var1);
      SizeStrategy.Key var4 = this.keyPool.get(var2);
      this.groupedMap.put(var4, var1);
      Integer var6 = (Integer)this.sortedSizes.get(Integer.valueOf(var4.size));
      TreeMap var5 = this.sortedSizes;
      int var3 = var4.size;
      if(var6 == null) {
         var2 = 1;
      } else {
         var2 = var6.intValue() + 1;
      }

      var5.put(Integer.valueOf(var3), Integer.valueOf(var2));
   }

   public Bitmap removeLast() {
      Bitmap var1 = (Bitmap)this.groupedMap.removeLast();
      if(var1 != null) {
         this.decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(var1)));
      }

      return var1;
   }

   public String toString() {
      return "SizeStrategy:\n  " + this.groupedMap + "\n" + "  SortedSizes" + this.sortedSizes;
   }

   static final class Key implements Poolable {
      private final SizeStrategy.KeyPool pool;
      private int size;

      Key(SizeStrategy.KeyPool var1) {
         this.pool = var1;
      }

      public boolean equals(Object var1) {
         boolean var3 = false;
         boolean var2 = var3;
         if(var1 instanceof SizeStrategy.Key) {
            SizeStrategy.Key var4 = (SizeStrategy.Key)var1;
            var2 = var3;
            if(this.size == var4.size) {
               var2 = true;
            }
         }

         return var2;
      }

      public int hashCode() {
         return this.size;
      }

      public void init(int var1) {
         this.size = var1;
      }

      public void offer() {
         this.pool.offer(this);
      }

      public String toString() {
         return SizeStrategy.getBitmapString(this.size);
      }
   }

   static class KeyPool extends BaseKeyPool {
      protected SizeStrategy.Key create() {
         return new SizeStrategy.Key(this);
      }

      public SizeStrategy.Key get(int var1) {
         SizeStrategy.Key var2 = (SizeStrategy.Key)this.get();
         var2.init(var1);
         return var2;
      }
   }
}
