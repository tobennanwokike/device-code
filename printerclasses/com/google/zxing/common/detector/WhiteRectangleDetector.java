package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class WhiteRectangleDetector {
   private static final int CORR = 1;
   private static final int INIT_SIZE = 40;
   private final int height;
   private final BitMatrix image;
   private final int width;

   public WhiteRectangleDetector(BitMatrix var1) {
      this.image = var1;
      this.height = var1.getHeight();
      this.width = var1.getWidth();
   }

   private ResultPoint[] centerEdges(ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4) {
      float var7 = var1.getX();
      float var12 = var1.getY();
      float var9 = var2.getX();
      float var8 = var2.getY();
      float var10 = var3.getX();
      float var5 = var3.getY();
      float var6 = var4.getX();
      float var11 = var4.getY();
      ResultPoint[] var13;
      if(var7 < (float)(this.width / 2)) {
         var13 = new ResultPoint[]{new ResultPoint(var6 - 1.0F, var11 + 1.0F), new ResultPoint(var9 + 1.0F, var8 + 1.0F), new ResultPoint(var10 - 1.0F, var5 - 1.0F), new ResultPoint(var7 + 1.0F, var12 - 1.0F)};
      } else {
         var13 = new ResultPoint[]{new ResultPoint(var6 + 1.0F, var11 + 1.0F), new ResultPoint(var9 + 1.0F, var8 - 1.0F), new ResultPoint(var10 - 1.0F, var5 + 1.0F), new ResultPoint(var7 - 1.0F, var12 - 1.0F)};
      }

      return var13;
   }

   private boolean containsBlackPoint(int var1, int var2, int var3, boolean var4) {
      boolean var6 = true;
      int var5 = var1;
      if(var4) {
         while(var1 <= var2) {
            if(this.image.get(var1, var3)) {
               var4 = var6;
               return var4;
            }

            ++var1;
         }
      } else {
         while(var5 <= var2) {
            if(this.image.get(var3, var5)) {
               var4 = var6;
               return var4;
            }

            ++var5;
         }
      }

      var4 = false;
      return var4;
   }

   private static int distanceL2(float var0, float var1, float var2, float var3) {
      var0 -= var2;
      var1 -= var3;
      return round((float)Math.sqrt((double)(var0 * var0 + var1 * var1)));
   }

   private ResultPoint getBlackPointOnSegment(float var1, float var2, float var3, float var4) {
      int var6 = distanceL2(var1, var2, var3, var4);
      var3 = (var3 - var1) / (float)var6;
      var4 = (var4 - var2) / (float)var6;
      int var5 = 0;

      ResultPoint var9;
      while(true) {
         if(var5 >= var6) {
            var9 = null;
            break;
         }

         int var7 = round((float)var5 * var3 + var1);
         int var8 = round((float)var5 * var4 + var2);
         if(this.image.get(var7, var8)) {
            var9 = new ResultPoint((float)var7, (float)var8);
            break;
         }

         ++var5;
      }

      return var9;
   }

   private static int round(float var0) {
      return (int)(0.5F + var0);
   }

   public ResultPoint[] detect() throws NotFoundException {
      boolean var13 = false;
      byte var12 = 1;
      int var3 = this.width - 40 >> 1;
      int var8 = this.width + 40 >> 1;
      int var1 = this.height - 40 >> 1;
      int var6 = this.height + 40 >> 1;
      boolean var10 = false;
      boolean var11 = true;

      int var2;
      boolean var5;
      int var20;
      int var21;
      while(true) {
         if(!var11) {
            var20 = var8;
            var2 = var3;
            var3 = var1;
            var5 = var13;
            var1 = var6;
            break;
         }

         boolean var14 = true;
         boolean var4 = false;
         var2 = var8;

         boolean var15;
         while(var14 && var2 < this.width) {
            var15 = this.containsBlackPoint(var1, var6, var2, false);
            var14 = var15;
            if(var15) {
               ++var2;
               var4 = true;
               var14 = var15;
            }
         }

         if(var2 >= this.width) {
            var5 = true;
            var20 = var2;
            var2 = var3;
            var3 = var1;
            var1 = var6;
            break;
         }

         var14 = true;
         var5 = var4;
         var20 = var6;

         while(var14 && var20 < this.height) {
            var15 = this.containsBlackPoint(var3, var2, var20, true);
            var14 = var15;
            if(var15) {
               ++var20;
               var5 = true;
               var14 = var15;
            }
         }

         if(var20 >= this.height) {
            var5 = true;
            var6 = var2;
            var2 = var3;
            var3 = var1;
            var1 = var20;
            var20 = var6;
            break;
         }

         var14 = true;
         boolean var22 = var5;
         var21 = var3;

         while(var14 && var21 >= 0) {
            var15 = this.containsBlackPoint(var1, var20, var21, false);
            var14 = var15;
            if(var15) {
               --var21;
               var22 = true;
               var14 = var15;
            }
         }

         if(var21 < 0) {
            boolean var23 = true;
            var6 = var2;
            var2 = var21;
            var3 = var1;
            var5 = var23;
            var1 = var20;
            var20 = var6;
            break;
         }

         var14 = true;
         boolean var9 = var22;
         int var7 = var1;

         while(var14 && var7 >= 0) {
            var15 = this.containsBlackPoint(var21, var2, var7, true);
            var14 = var15;
            if(var15) {
               --var7;
               var9 = true;
               var14 = var15;
            }
         }

         if(var7 < 0) {
            boolean var24 = true;
            var6 = var2;
            var1 = var20;
            var2 = var21;
            var3 = var7;
            var5 = var24;
            var20 = var6;
            break;
         }

         var6 = var20;
         var1 = var7;
         var8 = var2;
         var3 = var21;
         var11 = var9;
         if(var9) {
            var10 = true;
            var6 = var20;
            var1 = var7;
            var8 = var2;
            var3 = var21;
            var11 = var9;
         }
      }

      if(!var5 && var10) {
         var6 = var20 - var2;
         var21 = 1;
         ResultPoint var16 = null;

         ResultPoint var17;
         while(true) {
            if(var21 >= var6) {
               var17 = var16;
               break;
            }

            var16 = this.getBlackPointOnSegment((float)var2, (float)(var1 - var21), (float)(var2 + var21), (float)var1);
            if(var16 != null) {
               var17 = var16;
               break;
            }

            ++var21;
         }

         if(var17 == null) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            var21 = 1;
            var16 = null;

            ResultPoint var18;
            while(true) {
               if(var21 >= var6) {
                  var18 = var16;
                  break;
               }

               var16 = this.getBlackPointOnSegment((float)var2, (float)(var3 + var21), (float)(var2 + var21), (float)var3);
               if(var16 != null) {
                  var18 = var16;
                  break;
               }

               ++var21;
            }

            if(var18 == null) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               var2 = 1;
               var16 = null;

               ResultPoint var19;
               while(true) {
                  if(var2 >= var6) {
                     var19 = var16;
                     break;
                  }

                  var16 = this.getBlackPointOnSegment((float)var20, (float)(var3 + var2), (float)(var20 - var2), (float)var3);
                  if(var16 != null) {
                     var19 = var16;
                     break;
                  }

                  ++var2;
               }

               if(var19 == null) {
                  throw NotFoundException.getNotFoundInstance();
               } else {
                  var16 = null;

                  for(var2 = var12; var2 < var6; ++var2) {
                     var16 = this.getBlackPointOnSegment((float)var20, (float)(var1 - var2), (float)(var20 - var2), (float)var1);
                     if(var16 != null) {
                        break;
                     }
                  }

                  if(var16 == null) {
                     throw NotFoundException.getNotFoundInstance();
                  } else {
                     return this.centerEdges(var16, var17, var19, var18);
                  }
               }
            }
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }
}
