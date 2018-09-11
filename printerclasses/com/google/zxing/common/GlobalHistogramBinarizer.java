package com.google.zxing.common;

import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

public class GlobalHistogramBinarizer extends Binarizer {
   private static final int LUMINANCE_BITS = 5;
   private static final int LUMINANCE_BUCKETS = 32;
   private static final int LUMINANCE_SHIFT = 3;
   private int[] buckets = null;
   private byte[] luminances = null;

   public GlobalHistogramBinarizer(LuminanceSource var1) {
      super(var1);
   }

   private static int estimateBlackPoint(int[] var0) throws NotFoundException {
      byte var7 = 0;
      int var8 = var0.length;
      int var1 = 0;
      int var6 = 0;
      int var2 = 0;

      int var3;
      int var4;
      int var5;
      for(var5 = 0; var1 < var8; var5 = var4) {
         var3 = var6;
         if(var0[var1] > var6) {
            var3 = var0[var1];
            var2 = var1;
         }

         var4 = var5;
         if(var0[var1] > var5) {
            var4 = var0[var1];
         }

         ++var1;
         var6 = var3;
      }

      var4 = 0;
      var1 = 0;

      for(var3 = var7; var3 < var8; var1 = var6) {
         var6 = var3 - var2;
         var6 *= var0[var3] * var6;
         if(var6 > var4) {
            var4 = var3;
            var1 = var6;
         } else {
            var6 = var1;
            var1 = var4;
            var4 = var6;
         }

         ++var3;
         var6 = var4;
         var4 = var1;
      }

      if(var2 > var1) {
         var6 = var1;
         var4 = var2;
      } else {
         var4 = var1;
         var6 = var2;
      }

      if(var4 - var6 <= var8 >> 4) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         var2 = var4 - 1;
         var3 = -1;

         int var9;
         for(var1 = var4 - 1; var1 > var6; var2 = var9) {
            var9 = var1 - var6;
            var9 = var9 * var9 * (var4 - var1) * (var5 - var0[var1]);
            if(var9 > var3) {
               var3 = var1;
               var2 = var9;
            } else {
               var9 = var3;
               var3 = var2;
               var2 = var9;
            }

            --var1;
            var9 = var3;
            var3 = var2;
         }

         return var2 << 3;
      }
   }

   private void initArrays(int var1) {
      if(this.luminances == null || this.luminances.length < var1) {
         this.luminances = new byte[var1];
      }

      if(this.buckets == null) {
         this.buckets = new int[32];
      } else {
         for(var1 = 0; var1 < 32; ++var1) {
            this.buckets[var1] = 0;
         }
      }

   }

   public Binarizer createBinarizer(LuminanceSource var1) {
      return new GlobalHistogramBinarizer(var1);
   }

   public BitMatrix getBlackMatrix() throws NotFoundException {
      LuminanceSource var10 = this.getLuminanceSource();
      int var4 = var10.getWidth();
      int var3 = var10.getHeight();
      BitMatrix var7 = new BitMatrix(var4, var3);
      this.initArrays(var4);
      int[] var9 = this.buckets;

      int var1;
      int var2;
      int var5;
      byte[] var8;
      for(var1 = 1; var1 < 5; ++var1) {
         var8 = var10.getRow(var3 * var1 / 5, this.luminances);
         var5 = (var4 << 2) / 5;

         for(var2 = var4 / 5; var2 < var5; ++var2) {
            int var6 = (var8[var2] & 255) >> 3;
            ++var9[var6];
         }
      }

      var5 = estimateBlackPoint(var9);
      var8 = var10.getMatrix();

      for(var1 = 0; var1 < var3; ++var1) {
         for(var2 = 0; var2 < var4; ++var2) {
            if((var8[var1 * var4 + var2] & 255) < var5) {
               var7.set(var2, var1);
            }
         }
      }

      return var7;
   }

   public BitArray getBlackRow(int var1, BitArray var2) throws NotFoundException {
      int var4 = 1;
      LuminanceSource var8 = this.getLuminanceSource();
      int var6 = var8.getWidth();
      if(var2 != null && var2.getSize() >= var6) {
         var2.clear();
      } else {
         var2 = new BitArray(var6);
      }

      this.initArrays(var6);
      byte[] var11 = var8.getRow(var1, this.luminances);
      int[] var9 = this.buckets;

      int var3;
      for(var1 = 0; var1 < var6; ++var1) {
         var3 = (var11[var1] & 255) >> 3;
         ++var9[var3];
      }

      int var7 = estimateBlackPoint(var9);
      byte var10 = var11[0];
      var1 = var11[1] & 255;

      int var5;
      for(var3 = var10 & 255; var4 < var6 - 1; var1 = var5) {
         var5 = var11[var4 + 1] & 255;
         if((var1 << 2) - var3 - var5 >> 1 < var7) {
            var2.set(var4);
         }

         ++var4;
         var3 = var1;
      }

      return var2;
   }
}
