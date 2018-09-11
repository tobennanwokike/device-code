package com.google.zxing.oned.rss;

import com.google.zxing.NotFoundException;
import com.google.zxing.oned.OneDReader;

public abstract class AbstractRSSReader extends OneDReader {
   private static final int MAX_AVG_VARIANCE = 51;
   private static final float MAX_FINDER_PATTERN_RATIO = 0.89285713F;
   private static final int MAX_INDIVIDUAL_VARIANCE = 102;
   private static final float MIN_FINDER_PATTERN_RATIO = 0.7916667F;
   protected final int[] dataCharacterCounters = new int[8];
   protected final int[] decodeFinderCounters = new int[4];
   protected final int[] evenCounts;
   protected final float[] evenRoundingErrors = new float[4];
   protected final int[] oddCounts;
   protected final float[] oddRoundingErrors = new float[4];

   protected AbstractRSSReader() {
      this.oddCounts = new int[this.dataCharacterCounters.length / 2];
      this.evenCounts = new int[this.dataCharacterCounters.length / 2];
   }

   protected static int count(int[] var0) {
      int var1 = 0;

      int var2;
      for(var2 = 0; var1 < var0.length; ++var1) {
         var2 += var0[var1];
      }

      return var2;
   }

   protected static void decrement(int[] var0, float[] var1) {
      int var5 = 0;
      float var3 = var1[0];

      float var2;
      for(int var4 = 1; var4 < var0.length; var3 = var2) {
         var2 = var3;
         if(var1[var4] < var3) {
            var2 = var1[var4];
            var5 = var4;
         }

         ++var4;
      }

      --var0[var5];
   }

   protected static void increment(int[] var0, float[] var1) {
      int var5 = 0;
      float var2 = var1[0];

      float var3;
      for(int var4 = 1; var4 < var0.length; var2 = var3) {
         var3 = var2;
         if(var1[var4] > var2) {
            var3 = var1[var4];
            var5 = var4;
         }

         ++var4;
      }

      ++var0[var5];
   }

   protected static boolean isFinderPattern(int[] var0) {
      boolean var9 = false;
      int var3 = var0[0] + var0[1];
      int var2 = var0[2];
      int var4 = var0[3];
      float var1 = (float)var3 / (float)(var2 + var3 + var4);
      boolean var8 = var9;
      if(var1 >= 0.7916667F) {
         var8 = var9;
         if(var1 <= 0.89285713F) {
            var2 = Integer.MAX_VALUE;
            int var5 = Integer.MIN_VALUE;

            int var7;
            for(var4 = 0; var4 < var0.length; var2 = var7) {
               int var6 = var0[var4];
               var3 = var5;
               if(var6 > var5) {
                  var3 = var6;
               }

               var7 = var2;
               if(var6 < var2) {
                  var7 = var6;
               }

               ++var4;
               var5 = var3;
            }

            var8 = var9;
            if(var5 < var2 * 10) {
               var8 = true;
            }
         }
      }

      return var8;
   }

   protected static int parseFinderValue(int[] var0, int[][] var1) throws NotFoundException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if(patternMatchVariance(var0, var1[var2], 102) < 51) {
            return var2;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }
}
