package com.google.zxing.qrcode.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.detector.AlignmentPattern;
import java.util.Vector;

final class AlignmentPatternFinder {
   private final int[] crossCheckStateCount;
   private final int height;
   private final BitMatrix image;
   private final float moduleSize;
   private final Vector possibleCenters;
   private final ResultPointCallback resultPointCallback;
   private final int startX;
   private final int startY;
   private final int width;

   AlignmentPatternFinder(BitMatrix var1, int var2, int var3, int var4, int var5, float var6, ResultPointCallback var7) {
      this.image = var1;
      this.possibleCenters = new Vector(5);
      this.startX = var2;
      this.startY = var3;
      this.width = var4;
      this.height = var5;
      this.moduleSize = var6;
      this.crossCheckStateCount = new int[3];
      this.resultPointCallback = var7;
   }

   private static float centerFromEnd(int[] var0, int var1) {
      return (float)(var1 - var0[2]) - (float)var0[1] / 2.0F;
   }

   private float crossCheckVertical(int var1, int var2, int var3, int var4) {
      float var6 = Float.NaN;
      BitMatrix var10 = this.image;
      int var8 = var10.getHeight();
      int[] var9 = this.crossCheckStateCount;
      var9[0] = 0;
      var9[1] = 0;
      var9[2] = 0;

      int var7;
      for(var7 = var1; var7 >= 0 && var10.get(var2, var7) && var9[1] <= var3; --var7) {
         ++var9[1];
      }

      float var5 = var6;
      if(var7 >= 0) {
         if(var9[1] > var3) {
            var5 = var6;
         } else {
            while(var7 >= 0 && !var10.get(var2, var7) && var9[0] <= var3) {
               ++var9[0];
               --var7;
            }

            var5 = var6;
            if(var9[0] <= var3) {
               ++var1;

               while(var1 < var8 && var10.get(var2, var1) && var9[1] <= var3) {
                  ++var9[1];
                  ++var1;
               }

               var5 = var6;
               if(var1 != var8) {
                  var5 = var6;
                  if(var9[1] <= var3) {
                     while(var1 < var8 && !var10.get(var2, var1) && var9[2] <= var3) {
                        ++var9[2];
                        ++var1;
                     }

                     var5 = var6;
                     if(var9[2] <= var3) {
                        var5 = var6;
                        if(Math.abs(var9[0] + var9[1] + var9[2] - var4) * 5 < var4 * 2) {
                           var5 = var6;
                           if(this.foundPatternCross(var9)) {
                              var5 = centerFromEnd(var9, var1);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var5;
   }

   private boolean foundPatternCross(int[] var1) {
      boolean var5 = false;
      float var2 = this.moduleSize;
      float var3 = var2 / 2.0F;
      int var4 = 0;

      while(true) {
         if(var4 >= 3) {
            var5 = true;
            break;
         }

         if(Math.abs(var2 - (float)var1[var4]) >= var3) {
            break;
         }

         ++var4;
      }

      return var5;
   }

   private AlignmentPattern handlePossibleCenter(int[] var1, int var2, int var3) {
      int var7 = var1[0];
      int var9 = var1[1];
      int var8 = var1[2];
      float var6 = centerFromEnd(var1, var3);
      float var5 = this.crossCheckVertical(var2, (int)var6, var1[1] * 2, var7 + var9 + var8);
      AlignmentPattern var10;
      if(!Float.isNaN(var5)) {
         float var4 = (float)(var1[0] + var1[1] + var1[2]) / 3.0F;
         var3 = this.possibleCenters.size();

         for(var2 = 0; var2 < var3; ++var2) {
            if(((AlignmentPattern)this.possibleCenters.elementAt(var2)).aboutEquals(var4, var5, var6)) {
               var10 = new AlignmentPattern(var6, var5, var4);
               return var10;
            }
         }

         var10 = new AlignmentPattern(var6, var5, var4);
         this.possibleCenters.addElement(var10);
         if(this.resultPointCallback != null) {
            this.resultPointCallback.foundPossibleResultPoint(var10);
         }
      }

      var10 = null;
      return var10;
   }

   AlignmentPattern find() throws NotFoundException {
      int var5 = this.startX;
      int var6 = this.height;
      int var7 = var5 + this.width;
      int var8 = this.startY;
      int[] var12 = new int[3];
      int var3 = 0;

      AlignmentPattern var10;
      while(true) {
         if(var3 >= var6) {
            if(this.possibleCenters.isEmpty()) {
               throw NotFoundException.getNotFoundInstance();
            }

            var10 = (AlignmentPattern)this.possibleCenters.elementAt(0);
            break;
         }

         int var1;
         if((var3 & 1) == 0) {
            var1 = var3 + 1 >> 1;
         } else {
            var1 = -(var3 + 1 >> 1);
         }

         int var9 = var8 + (var6 >> 1) + var1;
         var12[0] = 0;
         var12[1] = 0;
         var12[2] = 0;

         for(var1 = var5; var1 < var7 && !this.image.get(var1, var9); ++var1) {
            ;
         }

         byte var2 = 0;
         int var4 = var1;

         for(var1 = var2; var4 < var7; ++var4) {
            if(this.image.get(var4, var9)) {
               if(var1 == 1) {
                  ++var12[var1];
               } else if(var1 == 2) {
                  if(this.foundPatternCross(var12)) {
                     var10 = this.handlePossibleCenter(var12, var9, var4);
                     if(var10 != null) {
                        return var10;
                     }
                  }

                  var12[0] = var12[2];
                  var12[1] = 1;
                  var12[2] = 0;
                  var1 = 1;
               } else {
                  ++var1;
                  ++var12[var1];
               }
            } else {
               int var13 = var1;
               if(var1 == 1) {
                  var13 = var1 + 1;
               }

               ++var12[var13];
               var1 = var13;
            }
         }

         if(this.foundPatternCross(var12)) {
            AlignmentPattern var11 = this.handlePossibleCenter(var12, var9, var7);
            var10 = var11;
            if(var11 != null) {
               break;
            }
         }

         ++var3;
      }

      return var10;
   }
}
