package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.AttributeStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruPoolStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LruBitmapPool implements BitmapPool {
   private static final Config DEFAULT_CONFIG;
   private static final String TAG = "LruBitmapPool";
   private final Set allowedConfigs;
   private int currentSize;
   private int evictions;
   private int hits;
   private final int initialMaxSize;
   private int maxSize;
   private int misses;
   private int puts;
   private final LruPoolStrategy strategy;
   private final LruBitmapPool.BitmapTracker tracker;

   static {
      DEFAULT_CONFIG = Config.ARGB_8888;
   }

   public LruBitmapPool(int var1) {
      this(var1, getDefaultStrategy(), getDefaultAllowedConfigs());
   }

   LruBitmapPool(int var1, LruPoolStrategy var2, Set var3) {
      this.initialMaxSize = var1;
      this.maxSize = var1;
      this.strategy = var2;
      this.allowedConfigs = var3;
      this.tracker = new LruBitmapPool.NullBitmapTracker();
   }

   public LruBitmapPool(int var1, Set var2) {
      this(var1, getDefaultStrategy(), var2);
   }

   private void dump() {
      if(Log.isLoggable("LruBitmapPool", 2)) {
         this.dumpUnchecked();
      }

   }

   private void dumpUnchecked() {
      Log.v("LruBitmapPool", "Hits=" + this.hits + ", misses=" + this.misses + ", puts=" + this.puts + ", evictions=" + this.evictions + ", currentSize=" + this.currentSize + ", maxSize=" + this.maxSize + "\nStrategy=" + this.strategy);
   }

   private void evict() {
      this.trimToSize(this.maxSize);
   }

   private static Set getDefaultAllowedConfigs() {
      HashSet var0 = new HashSet();
      var0.addAll(Arrays.asList(Config.values()));
      if(VERSION.SDK_INT >= 19) {
         var0.add((Object)null);
      }

      return Collections.unmodifiableSet(var0);
   }

   private static LruPoolStrategy getDefaultStrategy() {
      Object var0;
      if(VERSION.SDK_INT >= 19) {
         var0 = new SizeConfigStrategy();
      } else {
         var0 = new AttributeStrategy();
      }

      return (LruPoolStrategy)var0;
   }

   private void trimToSize(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void clearMemory() {
      if(Log.isLoggable("LruBitmapPool", 3)) {
         Log.d("LruBitmapPool", "clearMemory");
      }

      this.trimToSize(0);
   }

   public Bitmap get(int param1, int param2, Config param3) {
      // $FF: Couldn't be decompiled
   }

   @TargetApi(12)
   public Bitmap getDirty(int param1, int param2, Config param3) {
      // $FF: Couldn't be decompiled
   }

   public int getMaxSize() {
      return this.maxSize;
   }

   public boolean put(Bitmap param1) {
      // $FF: Couldn't be decompiled
   }

   public void setSizeMultiplier(float var1) {
      synchronized(this){}

      try {
         this.maxSize = Math.round((float)this.initialMaxSize * var1);
         this.evict();
      } finally {
         ;
      }

   }

   @SuppressLint({"InlinedApi"})
   public void trimMemory(int var1) {
      if(Log.isLoggable("LruBitmapPool", 3)) {
         Log.d("LruBitmapPool", "trimMemory, level=" + var1);
      }

      if(var1 >= 60) {
         this.clearMemory();
      } else if(var1 >= 40) {
         this.trimToSize(this.maxSize / 2);
      }

   }

   private interface BitmapTracker {
      void add(Bitmap var1);

      void remove(Bitmap var1);
   }

   private static class NullBitmapTracker implements LruBitmapPool.BitmapTracker {
      private NullBitmapTracker() {
      }

      // $FF: synthetic method
      NullBitmapTracker(Object var1) {
         this();
      }

      public void add(Bitmap var1) {
      }

      public void remove(Bitmap var1) {
      }
   }

   private static class ThrowingBitmapTracker implements LruBitmapPool.BitmapTracker {
      private final Set bitmaps = Collections.synchronizedSet(new HashSet());

      public void add(Bitmap var1) {
         if(this.bitmaps.contains(var1)) {
            throw new IllegalStateException("Can\'t add already added bitmap: " + var1 + " [" + var1.getWidth() + "x" + var1.getHeight() + "]");
         } else {
            this.bitmaps.add(var1);
         }
      }

      public void remove(Bitmap var1) {
         if(!this.bitmaps.contains(var1)) {
            throw new IllegalStateException("Cannot remove bitmap not in tracker");
         } else {
            this.bitmaps.remove(var1);
         }
      }
   }
}
