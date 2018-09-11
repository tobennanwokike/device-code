package com.google.zxing.common;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.PerspectiveTransform;

public final class DefaultGridSampler extends GridSampler {
   public BitMatrix sampleGrid(BitMatrix var1, int var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18) throws NotFoundException {
      return this.sampleGrid(var1, var2, PerspectiveTransform.quadrilateralToQuadrilateral(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18));
   }

   public BitMatrix sampleGrid(BitMatrix var1, int var2, PerspectiveTransform var3) throws NotFoundException {
      BitMatrix var9 = new BitMatrix(var2);
      float[] var8 = new float[var2 << 1];

      for(int var5 = 0; var5 < var2; ++var5) {
         int var7 = var8.length;
         float var4 = (float)var5;

         int var6;
         for(var6 = 0; var6 < var7; var6 += 2) {
            var8[var6] = (float)(var6 >> 1) + 0.5F;
            var8[var6 + 1] = var4 + 0.5F;
         }

         var3.transformPoints(var8);
         checkAndNudgePoints(var1, var8);

         for(var6 = 0; var6 < var7; var6 += 2) {
            try {
               if(var1.get((int)var8[var6], (int)var8[var6 + 1])) {
                  var9.set(var6 >> 1, var5);
               }
            } catch (ArrayIndexOutOfBoundsException var10) {
               throw NotFoundException.getNotFoundInstance();
            }
         }
      }

      return var9;
   }
}
