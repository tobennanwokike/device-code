package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.prefill.BitmapPreFillRunner;
import com.bumptech.glide.load.engine.prefill.PreFillQueue;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.util.Util;
import java.util.HashMap;

public final class BitmapPreFiller {
   private final BitmapPool bitmapPool;
   private BitmapPreFillRunner current;
   private final DecodeFormat defaultFormat;
   private final Handler handler = new Handler(Looper.getMainLooper());
   private final MemoryCache memoryCache;

   public BitmapPreFiller(MemoryCache var1, BitmapPool var2, DecodeFormat var3) {
      this.memoryCache = var1;
      this.bitmapPool = var2;
      this.defaultFormat = var3;
   }

   private static int getSizeInBytes(PreFillType var0) {
      return Util.getBitmapByteSize(var0.getWidth(), var0.getHeight(), var0.getConfig());
   }

   PreFillQueue generateAllocationOrder(PreFillType[] var1) {
      int var6 = this.memoryCache.getMaxSize();
      int var8 = this.memoryCache.getCurrentSize();
      int var5 = this.bitmapPool.getMaxSize();
      int var4 = 0;
      int var7 = var1.length;

      int var3;
      for(var3 = 0; var3 < var7; ++var3) {
         var4 += var1[var3].getWeight();
      }

      float var2 = (float)(var6 - var8 + var5) / (float)var4;
      HashMap var10 = new HashMap();
      var4 = var1.length;

      for(var3 = 0; var3 < var4; ++var3) {
         PreFillType var9 = var1[var3];
         var10.put(var9, Integer.valueOf(Math.round((float)var9.getWeight() * var2) / getSizeInBytes(var9)));
      }

      return new PreFillQueue(var10);
   }

   public void preFill(PreFillType.Builder... var1) {
      if(this.current != null) {
         this.current.cancel();
      }

      PreFillType[] var4 = new PreFillType[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         PreFillType.Builder var5 = var1[var2];
         if(var5.getConfig() == null) {
            Config var3;
            if(this.defaultFormat != DecodeFormat.ALWAYS_ARGB_8888 && this.defaultFormat != DecodeFormat.PREFER_ARGB_8888) {
               var3 = Config.RGB_565;
            } else {
               var3 = Config.ARGB_8888;
            }

            var5.setConfig(var3);
         }

         var4[var2] = var5.build();
      }

      PreFillQueue var6 = this.generateAllocationOrder(var4);
      this.current = new BitmapPreFillRunner(this.bitmapPool, this.memoryCache, var6);
      this.handler.post(this.current);
   }
}
