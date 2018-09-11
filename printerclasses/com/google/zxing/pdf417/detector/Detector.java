package com.google.zxing.pdf417.detector;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import java.util.Hashtable;

public final class Detector {
   private static final int MAX_AVG_VARIANCE = 107;
   private static final int MAX_INDIVIDUAL_VARIANCE = 204;
   private static final int SKEW_THRESHOLD = 2;
   private static final int[] START_PATTERN = new int[]{8, 1, 1, 1, 1, 1, 1, 3};
   private static final int[] START_PATTERN_REVERSE = new int[]{3, 1, 1, 1, 1, 1, 1, 8};
   private static final int[] STOP_PATTERN = new int[]{7, 1, 1, 3, 1, 1, 1, 2, 1};
   private static final int[] STOP_PATTERN_REVERSE = new int[]{1, 2, 1, 1, 1, 3, 1, 1, 7};
   private final BinaryBitmap image;

   public Detector(BinaryBitmap var1) {
      this.image = var1;
   }

   private static int computeDimension(ResultPoint var0, ResultPoint var1, ResultPoint var2, ResultPoint var3, float var4) {
      return ((round(ResultPoint.distance(var0, var1) / var4) + round(ResultPoint.distance(var2, var3) / var4) >> 1) + 8) / 17 * 17;
   }

   private static float computeModuleWidth(ResultPoint[] var0) {
      return ((ResultPoint.distance(var0[0], var0[4]) + ResultPoint.distance(var0[1], var0[5])) / 34.0F + (ResultPoint.distance(var0[6], var0[2]) + ResultPoint.distance(var0[7], var0[3])) / 36.0F) / 2.0F;
   }

   private static void correctCodeWordVertices(ResultPoint[] var0, boolean var1) {
      float var3 = var0[4].getY() - var0[6].getY();
      float var2 = var3;
      if(var1) {
         var2 = -var3;
      }

      float var4;
      float var5;
      if(var2 > 2.0F) {
         var3 = var0[4].getX();
         var4 = var0[0].getX();
         var5 = var0[6].getX();
         var2 = var0[0].getX();
         var2 = (var3 - var4) * (var0[6].getY() - var0[0].getY()) / (var5 - var2);
         var0[4] = new ResultPoint(var0[4].getX(), var2 + var0[4].getY());
      } else if(-var2 > 2.0F) {
         var3 = var0[2].getX();
         var2 = var0[6].getX();
         var5 = var0[2].getX();
         var4 = var0[4].getX();
         var2 = (var3 - var2) * (var0[2].getY() - var0[4].getY()) / (var5 - var4);
         var0[6] = new ResultPoint(var0[6].getX(), var0[6].getY() - var2);
      }

      var3 = var0[7].getY() - var0[5].getY();
      var2 = var3;
      if(var1) {
         var2 = -var3;
      }

      if(var2 > 2.0F) {
         var5 = var0[5].getX();
         var4 = var0[1].getX();
         var3 = var0[7].getX();
         var2 = var0[1].getX();
         var2 = (var5 - var4) * (var0[7].getY() - var0[1].getY()) / (var3 - var2);
         var0[5] = new ResultPoint(var0[5].getX(), var2 + var0[5].getY());
      } else if(-var2 > 2.0F) {
         var4 = var0[3].getX();
         var3 = var0[7].getX();
         var5 = var0[3].getX();
         var2 = var0[5].getX();
         var2 = (var4 - var3) * (var0[3].getY() - var0[5].getY()) / (var5 - var2);
         var0[7] = new ResultPoint(var0[7].getX(), var0[7].getY() - var2);
      }

   }

   private static int[] findGuardPattern(BitMatrix var0, int var1, int var2, int var3, boolean var4, int[] var5) {
      int var10 = var5.length;
      int[] var11 = new int[var10];
      int var7 = 0;
      int var8 = var1;
      int var6 = var1;

      int[] var12;
      while(true) {
         if(var8 >= var1 + var3) {
            var12 = null;
            break;
         }

         int var9;
         if(var0.get(var8, var2) ^ var4) {
            ++var11[var7];
            var9 = var7;
            var7 = var6;
         } else {
            if(var7 != var10 - 1) {
               var9 = var7 + 1;
               var7 = var6;
               var6 = var9;
            } else {
               if(patternMatchVariance(var11, var5, 204) < 107) {
                  var12 = new int[]{var6, var8};
                  break;
               }

               var9 = var6 + var11[0] + var11[1];

               for(var6 = 2; var6 < var10; ++var6) {
                  var11[var6 - 2] = var11[var6];
               }

               var11[var10 - 2] = 0;
               var11[var10 - 1] = 0;
               var6 = var7 - 1;
               var7 = var9;
            }

            var11[var6] = 1;
            if(!var4) {
               var4 = true;
               var9 = var6;
            } else {
               var4 = false;
               var9 = var6;
            }
         }

         ++var8;
         var6 = var7;
         var7 = var9;
      }

      return var12;
   }

   private static ResultPoint[] findVertices(BitMatrix var0) {
      boolean var3 = false;
      int var5 = var0.getHeight();
      int var4 = var0.getWidth();
      ResultPoint[] var6 = new ResultPoint[8];
      int var1 = 0;

      boolean var2;
      int[] var7;
      while(true) {
         if(var1 >= var5) {
            var2 = false;
            break;
         }

         var7 = findGuardPattern(var0, 0, var1, var4, false, START_PATTERN);
         if(var7 != null) {
            var6[0] = new ResultPoint((float)var7[0], (float)var1);
            var6[4] = new ResultPoint((float)var7[1], (float)var1);
            var2 = true;
            break;
         }

         ++var1;
      }

      boolean var9 = var2;
      if(var2) {
         var1 = var5 - 1;

         while(true) {
            if(var1 <= 0) {
               var9 = false;
               break;
            }

            var7 = findGuardPattern(var0, 0, var1, var4, false, START_PATTERN);
            if(var7 != null) {
               var6[1] = new ResultPoint((float)var7[0], (float)var1);
               var6[5] = new ResultPoint((float)var7[1], (float)var1);
               var9 = true;
               break;
            }

            --var1;
         }
      }

      var2 = var9;
      if(var9) {
         var1 = 0;

         while(true) {
            if(var1 >= var5) {
               var2 = false;
               break;
            }

            var7 = findGuardPattern(var0, 0, var1, var4, false, STOP_PATTERN);
            if(var7 != null) {
               var6[2] = new ResultPoint((float)var7[1], (float)var1);
               var6[6] = new ResultPoint((float)var7[0], (float)var1);
               var2 = true;
               break;
            }

            ++var1;
         }
      }

      if(var2) {
         int var10 = var5 - 1;

         while(true) {
            var9 = var3;
            if(var10 <= 0) {
               break;
            }

            var7 = findGuardPattern(var0, 0, var10, var4, false, STOP_PATTERN);
            if(var7 != null) {
               var6[3] = new ResultPoint((float)var7[1], (float)var10);
               var6[7] = new ResultPoint((float)var7[0], (float)var10);
               var9 = true;
               break;
            }

            --var10;
         }
      } else {
         var9 = var2;
      }

      ResultPoint[] var8;
      if(var9) {
         var8 = var6;
      } else {
         var8 = null;
      }

      return var8;
   }

   private static ResultPoint[] findVertices180(BitMatrix var0) {
      boolean var3 = true;
      int var4 = var0.getHeight();
      int var5 = var0.getWidth() >> 1;
      ResultPoint[] var6 = new ResultPoint[8];
      int var1 = var4 - 1;

      boolean var2;
      int[] var7;
      while(true) {
         if(var1 <= 0) {
            var2 = false;
            break;
         }

         var7 = findGuardPattern(var0, var5, var1, var5, true, START_PATTERN_REVERSE);
         if(var7 != null) {
            var6[0] = new ResultPoint((float)var7[1], (float)var1);
            var6[4] = new ResultPoint((float)var7[0], (float)var1);
            var2 = true;
            break;
         }

         --var1;
      }

      boolean var9 = var2;
      if(var2) {
         var1 = 0;

         while(true) {
            if(var1 >= var4) {
               var9 = false;
               break;
            }

            var7 = findGuardPattern(var0, var5, var1, var5, true, START_PATTERN_REVERSE);
            if(var7 != null) {
               var6[1] = new ResultPoint((float)var7[1], (float)var1);
               var6[5] = new ResultPoint((float)var7[0], (float)var1);
               var9 = true;
               break;
            }

            ++var1;
         }
      }

      var2 = var9;
      if(var9) {
         var1 = var4 - 1;

         while(true) {
            if(var1 <= 0) {
               var2 = false;
               break;
            }

            var7 = findGuardPattern(var0, 0, var1, var5, false, STOP_PATTERN_REVERSE);
            if(var7 != null) {
               var6[2] = new ResultPoint((float)var7[0], (float)var1);
               var6[6] = new ResultPoint((float)var7[1], (float)var1);
               var2 = true;
               break;
            }

            --var1;
         }
      }

      if(var2) {
         var1 = 0;

         while(true) {
            if(var1 >= var4) {
               var9 = false;
               break;
            }

            var7 = findGuardPattern(var0, 0, var1, var5, false, STOP_PATTERN_REVERSE);
            if(var7 != null) {
               var6[3] = new ResultPoint((float)var7[0], (float)var1);
               var6[7] = new ResultPoint((float)var7[1], (float)var1);
               var9 = var3;
               break;
            }

            ++var1;
         }
      } else {
         var9 = var2;
      }

      ResultPoint[] var8;
      if(var9) {
         var8 = var6;
      } else {
         var8 = null;
      }

      return var8;
   }

   private static int patternMatchVariance(int[] var0, int[] var1, int var2) {
      int var8 = Integer.MAX_VALUE;
      int var9 = var0.length;
      int var5 = 0;
      int var4 = 0;

      int var3;
      for(var3 = 0; var5 < var9; ++var5) {
         var3 += var0[var5];
         var4 += var1[var5];
      }

      int var7;
      if(var3 < var4) {
         var7 = var8;
      } else {
         int var10 = (var3 << 8) / var4;
         var5 = 0;

         for(var4 = 0; var5 < var9; ++var5) {
            var7 = var0[var5] << 8;
            int var6 = var1[var5] * var10;
            if(var7 > var6) {
               var6 = var7 - var6;
            } else {
               var6 -= var7;
            }

            var7 = var8;
            if(var6 > var2 * var10 >> 8) {
               return var7;
            }

            var4 += var6;
         }

         var7 = var4 / var3;
      }

      return var7;
   }

   private static int round(float var0) {
      return (int)(0.5F + var0);
   }

   private static BitMatrix sampleGrid(BitMatrix var0, ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5) throws NotFoundException {
      return GridSampler.getInstance().sampleGrid(var0, var5, 0.0F, 0.0F, (float)var5, 0.0F, (float)var5, (float)var5, 0.0F, (float)var5, var1.getX(), var1.getY(), var3.getX(), var3.getY(), var4.getX(), var4.getY(), var2.getX(), var2.getY());
   }

   public DetectorResult detect() throws NotFoundException {
      return this.detect((Hashtable)null);
   }

   public DetectorResult detect(Hashtable var1) throws NotFoundException {
      BitMatrix var5 = this.image.getBlackMatrix();
      ResultPoint[] var6 = findVertices(var5);
      if(var6 == null) {
         ResultPoint[] var4 = findVertices180(var5);
         var6 = var4;
         if(var4 != null) {
            correctCodeWordVertices(var4, true);
            var6 = var4;
         }
      } else {
         correctCodeWordVertices(var6, false);
      }

      if(var6 == null) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         float var2 = computeModuleWidth(var6);
         if(var2 < 1.0F) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            int var3 = computeDimension(var6[4], var6[6], var6[5], var6[7], var2);
            if(var3 < 1) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               return new DetectorResult(sampleGrid(var5, var6[4], var6[5], var6[6], var6[7], var3), new ResultPoint[]{var6[4], var6[5], var6[6], var6[7]});
            }
         }
      }
   }
}
