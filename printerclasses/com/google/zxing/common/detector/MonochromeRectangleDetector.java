package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class MonochromeRectangleDetector {
   private static final int MAX_MODULES = 32;
   private final BitMatrix image;

   public MonochromeRectangleDetector(BitMatrix var1) {
      this.image = var1;
   }

   private int[] blackWhiteRange(int var1, int var2, int var3, int var4, boolean var5) {
      int var7 = var3 + var4 >> 1;
      int var6 = var7;

      int var8;
      while(var6 >= var3) {
         label88: {
            if(var5) {
               if(!this.image.get(var6, var1)) {
                  break label88;
               }
            } else if(!this.image.get(var1, var6)) {
               break label88;
            }

            --var6;
            continue;
         }

         var8 = var6;

         int var9;
         while(true) {
            var9 = var8 - 1;
            if(var9 < var3) {
               break;
            }

            if(var5) {
               var8 = var9;
               if(this.image.get(var9, var1)) {
                  break;
               }
            } else {
               var8 = var9;
               if(this.image.get(var1, var9)) {
                  break;
               }
            }
         }

         if(var9 < var3 || var6 - var9 > var2) {
            break;
         }

         var6 = var9;
      }

      var8 = var6 + 1;
      var3 = var7;

      while(var3 < var4) {
         label98: {
            if(var5) {
               if(this.image.get(var3, var1)) {
                  break label98;
               }
            } else if(this.image.get(var1, var3)) {
               break label98;
            }

            var6 = var3;

            while(true) {
               var7 = var6 + 1;
               if(var7 >= var4) {
                  break;
               }

               if(var5) {
                  var6 = var7;
                  if(this.image.get(var7, var1)) {
                     break;
                  }
               } else {
                  var6 = var7;
                  if(this.image.get(var1, var7)) {
                     break;
                  }
               }
            }

            if(var7 >= var4 || var7 - var3 > var2) {
               break;
            }

            var3 = var7;
            continue;
         }

         ++var3;
      }

      var1 = var3 - 1;
      int[] var10;
      if(var1 > var8) {
         var10 = new int[]{var8, var1};
      } else {
         var10 = null;
      }

      return var10;
   }

   private ResultPoint findCornerFromCenter(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) throws NotFoundException {
      int var13 = var1;
      int var12 = var5;

      int[] var14;
      for(int[] var15 = null; var12 < var8 && var12 >= var7 && var13 < var4 && var13 >= var3; var15 = var14) {
         if(var2 == 0) {
            var14 = this.blackWhiteRange(var12, var9, var3, var4, true);
         } else {
            var14 = this.blackWhiteRange(var13, var9, var7, var8, false);
         }

         if(var14 == null) {
            if(var15 == null) {
               throw NotFoundException.getNotFoundInstance();
            }

            float var10;
            ResultPoint var16;
            if(var2 == 0) {
               var2 = var12 - var6;
               if(var15[0] < var1) {
                  if(var15[1] > var1) {
                     if(var6 > 0) {
                        var10 = (float)var15[0];
                     } else {
                        var10 = (float)var15[1];
                     }

                     var16 = new ResultPoint(var10, (float)var2);
                  } else {
                     var16 = new ResultPoint((float)var15[0], (float)var2);
                  }
               } else {
                  var16 = new ResultPoint((float)var15[1], (float)var2);
               }
            } else {
               var1 = var13 - var2;
               if(var15[0] < var5) {
                  if(var15[1] > var5) {
                     float var11 = (float)var1;
                     if(var2 < 0) {
                        var10 = (float)var15[0];
                     } else {
                        var10 = (float)var15[1];
                     }

                     var16 = new ResultPoint(var11, var10);
                  } else {
                     var16 = new ResultPoint((float)var1, (float)var15[0]);
                  }
               } else {
                  var16 = new ResultPoint((float)var1, (float)var15[1]);
               }
            }

            return var16;
         }

         var13 += var2;
         var12 += var6;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public ResultPoint[] detect() throws NotFoundException {
      int var6 = this.image.getHeight();
      int var8 = this.image.getWidth();
      int var3 = var6 >> 1;
      int var2 = var8 >> 1;
      int var4 = Math.max(1, var6 / 256);
      int var7 = Math.max(1, var8 / 256);
      int var1 = (int)this.findCornerFromCenter(var2, 0, 0, var8, var3, -var4, 0, var6, var2 >> 1).getY() - 1;
      ResultPoint var9 = this.findCornerFromCenter(var2, -var7, 0, var8, var3, 0, var1, var6, var3 >> 1);
      int var5 = (int)var9.getX() - 1;
      ResultPoint var10 = this.findCornerFromCenter(var2, var7, var5, var8, var3, 0, var1, var6, var3 >> 1);
      var7 = (int)var10.getX() + 1;
      ResultPoint var11 = this.findCornerFromCenter(var2, 0, var5, var7, var3, var4, var1, var6, var2 >> 1);
      var6 = (int)var11.getY();
      return new ResultPoint[]{this.findCornerFromCenter(var2, 0, var5, var7, var3, -var4, var1, var6 + 1, var2 >> 2), var9, var10, var11};
   }
}
