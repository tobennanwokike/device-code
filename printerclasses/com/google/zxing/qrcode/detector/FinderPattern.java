package com.google.zxing.qrcode.detector;

import com.google.zxing.ResultPoint;

public final class FinderPattern extends ResultPoint {
   private int count;
   private final float estimatedModuleSize;

   FinderPattern(float var1, float var2, float var3) {
      super(var1, var2);
      this.estimatedModuleSize = var3;
      this.count = 1;
   }

   boolean aboutEquals(float var1, float var2, float var3) {
      boolean var5 = false;
      boolean var4 = var5;
      if(Math.abs(var2 - this.getY()) <= var1) {
         var4 = var5;
         if(Math.abs(var3 - this.getX()) <= var1) {
            var1 = Math.abs(var1 - this.estimatedModuleSize);
            if(var1 > 1.0F) {
               var4 = var5;
               if(var1 / this.estimatedModuleSize > 1.0F) {
                  return var4;
               }
            }

            var4 = true;
         }
      }

      return var4;
   }

   int getCount() {
      return this.count;
   }

   public float getEstimatedModuleSize() {
      return this.estimatedModuleSize;
   }

   void incrementCount() {
      ++this.count;
   }
}
