package com.google.zxing.common;

import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import java.lang.reflect.Array;

public final class HybridBinarizer extends GlobalHistogramBinarizer {
   private static final int MINIMUM_DIMENSION = 40;
   private BitMatrix matrix = null;

   public HybridBinarizer(LuminanceSource var1) {
      super(var1);
   }

   private void binarizeEntireImage() throws NotFoundException {
      if(this.matrix == null) {
         LuminanceSource var7 = this.getLuminanceSource();
         if(var7.getWidth() >= 40 && var7.getHeight() >= 40) {
            byte[] var6 = var7.getMatrix();
            int var4 = var7.getWidth();
            int var5 = var7.getHeight();
            int var2 = var4 >> 3;
            int var1 = var2;
            if((var4 & 7) != 0) {
               var1 = var2 + 1;
            }

            int var3 = var5 >> 3;
            var2 = var3;
            if((var5 & 7) != 0) {
               var2 = var3 + 1;
            }

            int[][] var8 = calculateBlackPoints(var6, var1, var2, var4, var5);
            this.matrix = new BitMatrix(var4, var5);
            calculateThresholdForBlock(var6, var1, var2, var4, var5, var8, this.matrix);
         } else {
            this.matrix = super.getBlackMatrix();
         }
      }

   }

   private static int[][] calculateBlackPoints(byte[] var0, int var1, int var2, int var3, int var4) {
      int[][] var16 = (int[][])Array.newInstance(Integer.TYPE, new int[]{var2, var1});

      for(int var7 = 0; var7 < var2; ++var7) {
         int var5 = var7 << 3;
         int var8 = var5;
         if(var5 + 8 >= var4) {
            var8 = var4 - 8;
         }

         for(int var9 = 0; var9 < var1; ++var9) {
            var5 = var9 << 3;
            int var10 = var5;
            if(var5 + 8 >= var3) {
               var10 = var3 - 8;
            }

            var5 = 0;
            int var13 = 255;
            int var14 = 0;

            int var6;
            for(int var11 = 0; var11 < 8; var5 = var6) {
               int var12 = 0;
               var6 = var5;

               int var15;
               for(var5 = var14; var12 < 8; var6 = var15) {
                  var14 = var0[(var8 + var11) * var3 + var10 + var12] & 255;
                  var15 = var6 + var14;
                  var6 = var13;
                  if(var14 < var13) {
                     var6 = var14;
                  }

                  if(var14 > var5) {
                     var5 = var14;
                  }

                  ++var12;
                  var13 = var6;
               }

               ++var11;
               var14 = var5;
            }

            if(var14 - var13 > 24) {
               var5 >>= 6;
            } else if(var14 == 0) {
               var5 = 1;
            } else {
               var5 = var13 >> 1;
            }

            var16[var7][var9] = var5;
         }
      }

      return var16;
   }

   private static void calculateThresholdForBlock(byte[] var0, int var1, int var2, int var3, int var4, int[][] var5, BitMatrix var6) {
      for(int var7 = 0; var7 < var2; ++var7) {
         int var8 = var7 << 3;
         int var10 = var8;
         if(var8 + 8 >= var4) {
            var10 = var4 - 8;
         }

         for(var8 = 0; var8 < var1; ++var8) {
            int var9 = var8 << 3;
            int var11 = var9;
            if(var9 + 8 >= var3) {
               var11 = var3 - 8;
            }

            if(var8 > 1) {
               var9 = var8;
            } else {
               var9 = 2;
            }

            int var12;
            if(var9 < var1 - 2) {
               var12 = var9;
            } else {
               var12 = var1 - 3;
            }

            if(var7 > 1) {
               var9 = var7;
            } else {
               var9 = 2;
            }

            if(var9 >= var2 - 2) {
               var9 = var2 - 3;
            }

            int var14 = 0;

            for(int var13 = -2; var13 <= 2; ++var13) {
               int[] var15 = var5[var9 + var13];
               var14 = var14 + var15[var12 - 2] + var15[var12 - 1] + var15[var12] + var15[var12 + 1] + var15[var12 + 2];
            }

            threshold8x8Block(var0, var11, var10, var14 / 25, var3, var6);
         }
      }

   }

   private static void threshold8x8Block(byte[] var0, int var1, int var2, int var3, int var4, BitMatrix var5) {
      for(int var6 = 0; var6 < 8; ++var6) {
         for(int var7 = 0; var7 < 8; ++var7) {
            if((var0[(var2 + var6) * var4 + var1 + var7] & 255) < var3) {
               var5.set(var1 + var7, var2 + var6);
            }
         }
      }

   }

   public Binarizer createBinarizer(LuminanceSource var1) {
      return new HybridBinarizer(var1);
   }

   public BitMatrix getBlackMatrix() throws NotFoundException {
      this.binarizeEntireImage();
      return this.matrix;
   }
}
