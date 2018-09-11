package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.load.engine.bitmap_recycle.BaseKeyPool;
import com.bumptech.glide.load.engine.bitmap_recycle.GroupedLinkedMap;
import com.bumptech.glide.load.engine.bitmap_recycle.LruPoolStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

@TargetApi(19)
public class SizeConfigStrategy implements LruPoolStrategy {
   private static final Config[] ALPHA_8_IN_CONFIGS;
   private static final Config[] ARGB_4444_IN_CONFIGS;
   private static final Config[] ARGB_8888_IN_CONFIGS;
   private static final int MAX_SIZE_MULTIPLE = 8;
   private static final Config[] RGB_565_IN_CONFIGS;
   private final GroupedLinkedMap groupedMap = new GroupedLinkedMap();
   private final SizeConfigStrategy.KeyPool keyPool = new SizeConfigStrategy.KeyPool();
   private final Map sortedSizes = new HashMap();

   static {
      ARGB_8888_IN_CONFIGS = new Config[]{Config.ARGB_8888, null};
      RGB_565_IN_CONFIGS = new Config[]{Config.RGB_565};
      ARGB_4444_IN_CONFIGS = new Config[]{Config.ARGB_4444};
      ALPHA_8_IN_CONFIGS = new Config[]{Config.ALPHA_8};
   }

   private void decrementBitmapOfSize(Integer var1, Config var2) {
      NavigableMap var3 = this.getSizesForConfig(var2);
      Integer var4 = (Integer)var3.get(var1);
      if(var4.intValue() == 1) {
         var3.remove(var1);
      } else {
         var3.put(var1, Integer.valueOf(var4.intValue() - 1));
      }

   }

   private SizeConfigStrategy.Key findBestKey(SizeConfigStrategy.Key var1, int var2, Config var3) {
      SizeConfigStrategy.Key var7 = var1;
      Config[] var10 = getInConfigs(var3);
      int var5 = var10.length;
      int var4 = 0;

      SizeConfigStrategy.Key var6;
      while(true) {
         var6 = var7;
         if(var4 >= var5) {
            break;
         }

         Config var8 = var10[var4];
         Integer var9 = (Integer)this.getSizesForConfig(var8).ceilingKey(Integer.valueOf(var2));
         if(var9 != null && var9.intValue() <= var2 * 8) {
            if(var9.intValue() == var2) {
               if(var8 == null) {
                  var6 = var7;
                  if(var3 == null) {
                     break;
                  }
               } else {
                  var6 = var7;
                  if(var8.equals(var3)) {
                     break;
                  }
               }
            }

            this.keyPool.offer(var1);
            var6 = this.keyPool.get(var9.intValue(), var8);
            break;
         }

         ++var4;
      }

      return var6;
   }

   private static String getBitmapString(int var0, Config var1) {
      return "[" + var0 + "](" + var1 + ")";
   }

   private static Config[] getInConfigs(Config var0) {
      Config[] var2;
      switch(null.$SwitchMap$android$graphics$Bitmap$Config[var0.ordinal()]) {
      case 1:
         var2 = ARGB_8888_IN_CONFIGS;
         break;
      case 2:
         var2 = RGB_565_IN_CONFIGS;
         break;
      case 3:
         var2 = ARGB_4444_IN_CONFIGS;
         break;
      case 4:
         var2 = ALPHA_8_IN_CONFIGS;
         break;
      default:
         Config[] var1 = new Config[]{var0};
         var2 = var1;
      }

      return var2;
   }

   private NavigableMap getSizesForConfig(Config var1) {
      NavigableMap var3 = (NavigableMap)this.sortedSizes.get(var1);
      Object var2 = var3;
      if(var3 == null) {
         var2 = new TreeMap();
         this.sortedSizes.put(var1, var2);
      }

      return (NavigableMap)var2;
   }

   public Bitmap get(int var1, int var2, Config var3) {
      int var4 = Util.getBitmapByteSize(var1, var2, var3);
      SizeConfigStrategy.Key var6 = this.findBestKey(this.keyPool.get(var4, var3), var4, var3);
      Bitmap var5 = (Bitmap)this.groupedMap.get(var6);
      if(var5 != null) {
         this.decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(var5)), var5.getConfig());
         if(var5.getConfig() != null) {
            var3 = var5.getConfig();
         } else {
            var3 = Config.ARGB_8888;
         }

         var5.reconfigure(var1, var2, var3);
      }

      return var5;
   }

   public int getSize(Bitmap var1) {
      return Util.getBitmapByteSize(var1);
   }

   public String logBitmap(int var1, int var2, Config var3) {
      return getBitmapString(Util.getBitmapByteSize(var1, var2, var3), var3);
   }

   public String logBitmap(Bitmap var1) {
      return getBitmapString(Util.getBitmapByteSize(var1), var1.getConfig());
   }

   public void put(Bitmap var1) {
      int var2 = Util.getBitmapByteSize(var1);
      SizeConfigStrategy.Key var4 = this.keyPool.get(var2, var1.getConfig());
      this.groupedMap.put(var4, var1);
      NavigableMap var5 = this.getSizesForConfig(var1.getConfig());
      Integer var6 = (Integer)var5.get(Integer.valueOf(var4.size));
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
         this.decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(var1)), var1.getConfig());
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder()).append("SizeConfigStrategy{groupedMap=").append(this.groupedMap).append(", sortedSizes=(");
      Iterator var2 = this.sortedSizes.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.append(var3.getKey()).append('[').append(var3.getValue()).append("], ");
      }

      if(!this.sortedSizes.isEmpty()) {
         var1.replace(var1.length() - 2, var1.length(), "");
      }

      return var1.append(")}").toString();
   }

   static final class Key implements Poolable {
      private Config config;
      private final SizeConfigStrategy.KeyPool pool;
      private int size;

      public Key(SizeConfigStrategy.KeyPool var1) {
         this.pool = var1;
      }

      Key(SizeConfigStrategy.KeyPool var1, int var2, Config var3) {
         this(var1);
         this.init(var2, var3);
      }

      public boolean equals(Object var1) {
         boolean var3 = false;
         boolean var2 = var3;
         if(var1 instanceof SizeConfigStrategy.Key) {
            SizeConfigStrategy.Key var4 = (SizeConfigStrategy.Key)var1;
            var2 = var3;
            if(this.size == var4.size) {
               if(this.config == null) {
                  var2 = var3;
                  if(var4.config != null) {
                     return var2;
                  }
               } else {
                  var2 = var3;
                  if(!this.config.equals(var4.config)) {
                     return var2;
                  }
               }

               var2 = true;
            }
         }

         return var2;
      }

      public int hashCode() {
         int var2 = this.size;
         int var1;
         if(this.config != null) {
            var1 = this.config.hashCode();
         } else {
            var1 = 0;
         }

         return var2 * 31 + var1;
      }

      public void init(int var1, Config var2) {
         this.size = var1;
         this.config = var2;
      }

      public void offer() {
         this.pool.offer(this);
      }

      public String toString() {
         return SizeConfigStrategy.getBitmapString(this.size, this.config);
      }
   }

   static class KeyPool extends BaseKeyPool {
      protected SizeConfigStrategy.Key create() {
         return new SizeConfigStrategy.Key(this);
      }

      public SizeConfigStrategy.Key get(int var1, Config var2) {
         SizeConfigStrategy.Key var3 = (SizeConfigStrategy.Key)this.get();
         var3.init(var1, var2);
         return var3;
      }
   }
}
