package com.google.zxing.common;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DefaultGridSampler;
import com.google.zxing.common.PerspectiveTransform;

public abstract class GridSampler {
   private static GridSampler gridSampler = new DefaultGridSampler();

   protected static void checkAndNudgePoints(BitMatrix var0, float[] var1) throws NotFoundException {
      int var5 = var0.getWidth();
      int var4 = var0.getHeight();
      int var3 = 0;
      boolean var2 = true;

      while(true) {
         int var6;
         int var7;
         if(var3 < var1.length && var2) {
            var7 = (int)var1[var3];
            var6 = (int)var1[var3 + 1];
            if(var7 >= -1 && var7 <= var5 && var6 >= -1 && var6 <= var4) {
               if(var7 == -1) {
                  var1[var3] = 0.0F;
                  var2 = true;
               } else if(var7 == var5) {
                  var1[var3] = (float)(var5 - 1);
                  var2 = true;
               } else {
                  var2 = false;
               }

               if(var6 == -1) {
                  var1[var3 + 1] = 0.0F;
                  var2 = true;
               } else if(var6 == var4) {
                  var1[var3 + 1] = (float)(var4 - 1);
                  var2 = true;
               }

               var3 += 2;
               continue;
            }

            throw NotFoundException.getNotFoundInstance();
         }

         var3 = var1.length - 2;
         var2 = true;

         while(true) {
            if(var3 >= 0 && var2) {
               var7 = (int)var1[var3];
               var6 = (int)var1[var3 + 1];
               if(var7 >= -1 && var7 <= var5 && var6 >= -1 && var6 <= var4) {
                  if(var7 == -1) {
                     var1[var3] = 0.0F;
                     var2 = true;
                  } else if(var7 == var5) {
                     var1[var3] = (float)(var5 - 1);
                     var2 = true;
                  } else {
                     var2 = false;
                  }

                  if(var6 == -1) {
                     var1[var3 + 1] = 0.0F;
                     var2 = true;
                  } else if(var6 == var4) {
                     var1[var3 + 1] = (float)(var4 - 1);
                     var2 = true;
                  }

                  var3 -= 2;
                  continue;
               }

               throw NotFoundException.getNotFoundInstance();
            }

            return;
         }
      }
   }

   public static GridSampler getInstance() {
      return gridSampler;
   }

   public static void setGridSampler(GridSampler var0) {
      if(var0 == null) {
         throw new IllegalArgumentException();
      } else {
         gridSampler = var0;
      }
   }

   public abstract BitMatrix sampleGrid(BitMatrix var1, int var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18) throws NotFoundException;

   public BitMatrix sampleGrid(BitMatrix var1, int var2, PerspectiveTransform var3) throws NotFoundException {
      throw new IllegalStateException();
   }
}
