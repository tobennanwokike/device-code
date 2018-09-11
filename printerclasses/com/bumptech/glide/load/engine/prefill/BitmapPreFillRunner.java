package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.PreFillQueue;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

final class BitmapPreFillRunner implements Runnable {
   static final int BACKOFF_RATIO = 4;
   private static final BitmapPreFillRunner.Clock DEFAULT_CLOCK = new BitmapPreFillRunner.Clock();
   static final long INITIAL_BACKOFF_MS = 40L;
   static final long MAX_BACKOFF_MS;
   static final long MAX_DURATION_MS = 32L;
   private static final String TAG = "PreFillRunner";
   private final BitmapPool bitmapPool;
   private final BitmapPreFillRunner.Clock clock;
   private long currentDelay;
   private final Handler handler;
   private boolean isCancelled;
   private final MemoryCache memoryCache;
   private final Set seenTypes;
   private final PreFillQueue toPrefill;

   static {
      MAX_BACKOFF_MS = TimeUnit.SECONDS.toMillis(1L);
   }

   public BitmapPreFillRunner(BitmapPool var1, MemoryCache var2, PreFillQueue var3) {
      this(var1, var2, var3, DEFAULT_CLOCK, new Handler(Looper.getMainLooper()));
   }

   BitmapPreFillRunner(BitmapPool var1, MemoryCache var2, PreFillQueue var3, BitmapPreFillRunner.Clock var4, Handler var5) {
      this.seenTypes = new HashSet();
      this.currentDelay = 40L;
      this.bitmapPool = var1;
      this.memoryCache = var2;
      this.toPrefill = var3;
      this.clock = var4;
      this.handler = var5;
   }

   private void addToBitmapPool(PreFillType var1, Bitmap var2) {
      if(this.seenTypes.add(var1)) {
         Bitmap var3 = this.bitmapPool.get(var1.getWidth(), var1.getHeight(), var1.getConfig());
         if(var3 != null) {
            this.bitmapPool.put(var3);
         }
      }

      this.bitmapPool.put(var2);
   }

   private boolean allocate() {
      long var2 = this.clock.now();

      while(!this.toPrefill.isEmpty() && !this.isGcDetected(var2)) {
         PreFillType var4 = this.toPrefill.remove();
         Bitmap var5 = Bitmap.createBitmap(var4.getWidth(), var4.getHeight(), var4.getConfig());
         if(this.getFreeMemoryCacheBytes() >= Util.getBitmapByteSize(var5)) {
            this.memoryCache.put(new BitmapPreFillRunner.UniqueKey(), BitmapResource.obtain(var5, this.bitmapPool));
         } else {
            this.addToBitmapPool(var4, var5);
         }

         if(Log.isLoggable("PreFillRunner", 3)) {
            Log.d("PreFillRunner", "allocated [" + var4.getWidth() + "x" + var4.getHeight() + "] " + var4.getConfig() + " size: " + Util.getBitmapByteSize(var5));
         }
      }

      boolean var1;
      if(!this.isCancelled && !this.toPrefill.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private int getFreeMemoryCacheBytes() {
      return this.memoryCache.getMaxSize() - this.memoryCache.getCurrentSize();
   }

   private long getNextDelay() {
      long var1 = this.currentDelay;
      this.currentDelay = Math.min(this.currentDelay * 4L, MAX_BACKOFF_MS);
      return var1;
   }

   private boolean isGcDetected(long var1) {
      boolean var3;
      if(this.clock.now() - var1 >= 32L) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void cancel() {
      this.isCancelled = true;
   }

   public void run() {
      if(this.allocate()) {
         this.handler.postDelayed(this, this.getNextDelay());
      }

   }

   static class Clock {
      public long now() {
         return SystemClock.currentThreadTimeMillis();
      }
   }

   private static class UniqueKey implements Key {
      private UniqueKey() {
      }

      // $FF: synthetic method
      UniqueKey(Object var1) {
         this();
      }

      public void updateDiskCacheKey(MessageDigest var1) throws UnsupportedEncodingException {
      }
   }
}
