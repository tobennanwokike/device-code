package com.bumptech.glide.util;

import android.annotation.TargetApi;
import android.os.SystemClock;
import android.os.Build.VERSION;

public final class LogTime {
   private static final double MILLIS_MULTIPLIER;

   static {
      double var0 = 1.0D;
      if(17 <= VERSION.SDK_INT) {
         var0 = 1.0D / Math.pow(10.0D, 6.0D);
      }

      MILLIS_MULTIPLIER = var0;
   }

   public static double getElapsedMillis(long var0) {
      return (double)(getLogTime() - var0) * MILLIS_MULTIPLIER;
   }

   @TargetApi(17)
   public static long getLogTime() {
      long var0;
      if(17 <= VERSION.SDK_INT) {
         var0 = SystemClock.elapsedRealtimeNanos();
      } else {
         var0 = System.currentTimeMillis();
      }

      return var0;
   }
}
