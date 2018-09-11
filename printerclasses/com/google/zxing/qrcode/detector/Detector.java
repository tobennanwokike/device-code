package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.PerspectiveTransform;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.detector.AlignmentPattern;
import com.google.zxing.qrcode.detector.AlignmentPatternFinder;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternFinder;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.util.Hashtable;

public class Detector {
   private final BitMatrix image;
   private ResultPointCallback resultPointCallback;

   public Detector(BitMatrix var1) {
      this.image = var1;
   }

   private float calculateModuleSizeOneWay(ResultPoint var1, ResultPoint var2) {
      float var3 = this.sizeOfBlackWhiteBlackRunBothWays((int)var1.getX(), (int)var1.getY(), (int)var2.getX(), (int)var2.getY());
      float var4 = this.sizeOfBlackWhiteBlackRunBothWays((int)var2.getX(), (int)var2.getY(), (int)var1.getX(), (int)var1.getY());
      if(Float.isNaN(var3)) {
         var3 = var4 / 7.0F;
      } else if(Float.isNaN(var4)) {
         var3 /= 7.0F;
      } else {
         var3 = (var3 + var4) / 14.0F;
      }

      return var3;
   }

   protected static int computeDimension(ResultPoint var0, ResultPoint var1, ResultPoint var2, float var3) throws NotFoundException {
      int var5 = (round(ResultPoint.distance(var0, var1) / var3) + round(ResultPoint.distance(var0, var2) / var3) >> 1) + 7;
      int var4 = var5;
      switch(var5 & 3) {
      case 0:
         var4 = var5 + 1;
      case 1:
         break;
      case 2:
         var4 = var5 - 1;
         break;
      case 3:
         throw NotFoundException.getNotFoundInstance();
      default:
         var4 = var5;
      }

      return var4;
   }

   private static int round(float var0) {
      return (int)(0.5F + var0);
   }

   private static BitMatrix sampleGrid(BitMatrix var0, PerspectiveTransform var1, int var2) throws NotFoundException {
      return GridSampler.getInstance().sampleGrid(var0, var2, var1);
   }

   private float sizeOfBlackWhiteBlackRun(int var1, int var2, int var3, int var4) {
      boolean var8;
      if(Math.abs(var4 - var2) > Math.abs(var3 - var1)) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var6;
      int var9;
      int var10;
      if(var8) {
         var10 = var4;
         var9 = var3;
         var6 = var2;
         var4 = var1;
      } else {
         var9 = var4;
         var4 = var2;
         var6 = var1;
         var10 = var3;
      }

      int var15 = Math.abs(var10 - var6);
      int var16 = Math.abs(var9 - var4);
      var1 = -var15;
      byte var11;
      if(var4 < var9) {
         var11 = 1;
      } else {
         var11 = -1;
      }

      byte var12;
      if(var6 < var10) {
         var12 = 1;
      } else {
         var12 = -1;
      }

      var3 = 0;
      var2 = var6;
      int var7 = var1 >> 1;

      float var5;
      for(var1 = var4; var2 != var10; var2 += var12) {
         int var13;
         if(var8) {
            var13 = var1;
         } else {
            var13 = var2;
         }

         int var14;
         if(var8) {
            var14 = var2;
         } else {
            var14 = var1;
         }

         if(var3 == 1) {
            if(this.image.get(var13, var14)) {
               ++var3;
            }
         } else if(!this.image.get(var13, var14)) {
            ++var3;
         }

         if(var3 == 3) {
            var2 -= var6;
            var3 = var1 - var4;
            if(var12 < 0) {
               var1 = var2 + 1;
            } else {
               var1 = var2;
            }

            var5 = (float)Math.sqrt((double)(var1 * var1 + var3 * var3));
            return var5;
         }

         var7 += var16;
         if(var7 > 0) {
            if(var1 == var9) {
               break;
            }

            var1 += var11;
            var7 -= var15;
         }
      }

      var1 = var10 - var6;
      var2 = var9 - var4;
      var5 = (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
      return var5;
   }

   private float sizeOfBlackWhiteBlackRunBothWays(int var1, int var2, int var3, int var4) {
      byte var8 = 0;
      float var6 = this.sizeOfBlackWhiteBlackRun(var1, var2, var3, var4);
      var3 = var1 - (var3 - var1);
      float var5;
      if(var3 < 0) {
         var5 = (float)var1 / (float)(var1 - var3);
         var3 = 0;
      } else if(var3 > this.image.getWidth()) {
         var5 = (float)(this.image.getWidth() - var1) / (float)(var3 - var1);
         var3 = this.image.getWidth();
      } else {
         var5 = 1.0F;
      }

      var4 = (int)((float)var2 - var5 * (float)(var4 - var2));
      if(var4 < 0) {
         var5 = (float)var2 / (float)(var2 - var4);
         var4 = var8;
      } else if(var4 > this.image.getHeight()) {
         var5 = (float)(this.image.getHeight() - var2) / (float)(var4 - var2);
         var4 = this.image.getHeight();
      } else {
         var5 = 1.0F;
      }

      float var7 = (float)var1;
      return this.sizeOfBlackWhiteBlackRun(var1, var2, (int)(var5 * (float)(var3 - var1) + var7), var4) + var6;
   }

   protected float calculateModuleSize(ResultPoint var1, ResultPoint var2, ResultPoint var3) {
      return (this.calculateModuleSizeOneWay(var1, var2) + this.calculateModuleSizeOneWay(var1, var3)) / 2.0F;
   }

   public PerspectiveTransform createTransform(ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5) {
      float var6 = (float)var5 - 3.5F;
      float var7;
      float var8;
      float var9;
      float var10;
      if(var4 != null) {
         var8 = var4.getX();
         var10 = var4.getY();
         var7 = var6 - 3.0F;
         var9 = var7;
      } else {
         var8 = var2.getX() - var1.getX() + var3.getX();
         var10 = var2.getY() - var1.getY() + var3.getY();
         var7 = var6;
         var9 = var6;
      }

      return PerspectiveTransform.quadrilateralToQuadrilateral(3.5F, 3.5F, var6, 3.5F, var9, var7, 3.5F, var6, var1.getX(), var1.getY(), var2.getX(), var2.getY(), var8, var10, var3.getX(), var3.getY());
   }

   public DetectorResult detect() throws NotFoundException, FormatException {
      return this.detect((Hashtable)null);
   }

   public DetectorResult detect(Hashtable var1) throws NotFoundException, FormatException {
      ResultPointCallback var2;
      if(var1 == null) {
         var2 = null;
      } else {
         var2 = (ResultPointCallback)var1.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
      }

      this.resultPointCallback = var2;
      return this.processFinderPatternInfo((new FinderPatternFinder(this.image, this.resultPointCallback)).find(var1));
   }

   protected AlignmentPattern findAlignmentInRegion(float var1, int var2, int var3, float var4) throws NotFoundException {
      int var6 = (int)(var4 * var1);
      int var5 = Math.max(0, var2 - var6);
      var2 = Math.min(this.image.getWidth() - 1, var2 + var6);
      if((float)(var2 - var5) < var1 * 3.0F) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int var7 = Math.max(0, var3 - var6);
         var3 = Math.min(this.image.getHeight() - 1, var6 + var3);
         if((float)(var3 - var7) < var1 * 3.0F) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            return (new AlignmentPatternFinder(this.image, var5, var7, var2 - var5, var3 - var7, var1, this.resultPointCallback)).find();
         }
      }
   }

   protected BitMatrix getImage() {
      return this.image;
   }

   protected ResultPointCallback getResultPointCallback() {
      return this.resultPointCallback;
   }

   protected DetectorResult processFinderPatternInfo(FinderPatternInfo var1) throws NotFoundException, FormatException {
      FinderPattern var16 = var1.getTopLeft();
      FinderPattern var17 = var1.getTopRight();
      FinderPattern var18 = var1.getBottomLeft();
      float var2 = this.calculateModuleSize(var16, var17, var18);
      if(var2 < 1.0F) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int var12 = computeDimension(var16, var17, var18, var2);
         Version var19 = Version.getProvisionalVersionForDimension(var12);
         int var11 = var19.getDimensionForVersion();
         PerspectiveTransform var15 = null;
         AlignmentPattern var21 = var15;
         if(var19.getAlignmentPatternCenters().length > 0) {
            float var8 = var17.getX();
            float var7 = var16.getX();
            float var9 = var18.getX();
            float var6 = var17.getY();
            float var3 = var16.getY();
            float var4 = var18.getY();
            float var5 = 1.0F - 3.0F / (float)(var11 - 7);
            float var10 = var16.getX();
            int var14 = (int)((var8 - var7 + var9 - var16.getX()) * var5 + var10);
            int var13 = (int)(var16.getY() + var5 * (var6 - var3 + var4 - var16.getY()));
            var11 = 4;

            while(true) {
               var21 = var15;
               if(var11 > 16) {
                  break;
               }

               var3 = (float)var11;

               try {
                  var21 = this.findAlignmentInRegion(var2, var14, var13, var3);
                  break;
               } catch (NotFoundException var20) {
                  var11 <<= 1;
               }
            }
         }

         var15 = this.createTransform(var16, var17, var18, var21, var12);
         BitMatrix var24 = sampleGrid(this.image, var15, var12);
         ResultPoint[] var22;
         if(var21 == null) {
            var22 = new ResultPoint[]{var18, var16, var17};
         } else {
            ResultPoint[] var23 = new ResultPoint[]{var18, var16, var17, var21};
            var22 = var23;
         }

         return new DetectorResult(var24, var22);
      }
   }
}
