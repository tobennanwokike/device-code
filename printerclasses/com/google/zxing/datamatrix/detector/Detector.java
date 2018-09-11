package com.google.zxing.datamatrix.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.Collections;
import com.google.zxing.common.Comparator;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.WhiteRectangleDetector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public final class Detector {
   private static final Integer[] INTEGERS = new Integer[]{new Integer(0), new Integer(1), new Integer(2), new Integer(3), new Integer(4)};
   private final BitMatrix image;
   private final WhiteRectangleDetector rectangleDetector;

   public Detector(BitMatrix var1) {
      this.image = var1;
      this.rectangleDetector = new WhiteRectangleDetector(var1);
   }

   private ResultPoint correctTopRight(ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5) {
      float var7 = (float)distance(var1, var2) / (float)var5;
      int var9 = distance(var3, var4);
      float var6 = (var4.getX() - var3.getX()) / (float)var9;
      float var8 = (var4.getY() - var3.getY()) / (float)var9;
      ResultPoint var10 = new ResultPoint(var6 * var7 + var4.getX(), var7 * var8 + var4.getY());
      var6 = (float)distance(var1, var2) / (float)var5;
      var5 = distance(var2, var4);
      var8 = (var4.getX() - var2.getX()) / (float)var5;
      var7 = (var4.getY() - var2.getY()) / (float)var5;
      var4 = new ResultPoint(var8 * var6 + var4.getX(), var6 * var7 + var4.getY());
      if(!this.isValid(var10)) {
         if(this.isValid(var4)) {
            var1 = var4;
         } else {
            var1 = null;
         }
      } else if(!this.isValid(var4)) {
         var1 = var10;
      } else {
         var1 = var4;
         if(Math.abs(this.transitionsBetween(var3, var10).getTransitions() - this.transitionsBetween(var2, var10).getTransitions()) <= Math.abs(this.transitionsBetween(var3, var4).getTransitions() - this.transitionsBetween(var2, var4).getTransitions())) {
            var1 = var10;
         }
      }

      return var1;
   }

   private static int distance(ResultPoint var0, ResultPoint var1) {
      return round((float)Math.sqrt((double)((var0.getX() - var1.getX()) * (var0.getX() - var1.getX()) + (var0.getY() - var1.getY()) * (var0.getY() - var1.getY()))));
   }

   private static void increment(Hashtable var0, ResultPoint var1) {
      Integer var2 = (Integer)var0.get(var1);
      if(var2 == null) {
         var2 = INTEGERS[1];
      } else {
         var2 = INTEGERS[var2.intValue() + 1];
      }

      var0.put(var1, var2);
   }

   private boolean isValid(ResultPoint var1) {
      boolean var2;
      if(var1.getX() >= 0.0F && var1.getX() < (float)this.image.width && var1.getY() > 0.0F && var1.getY() < (float)this.image.height) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static int round(float var0) {
      return (int)(0.5F + var0);
   }

   private static BitMatrix sampleGrid(BitMatrix var0, ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4, int var5) throws NotFoundException {
      return GridSampler.getInstance().sampleGrid(var0, var5, 0.5F, 0.5F, (float)var5 - 0.5F, 0.5F, (float)var5 - 0.5F, (float)var5 - 0.5F, 0.5F, (float)var5 - 0.5F, var1.getX(), var1.getY(), var4.getX(), var4.getY(), var3.getX(), var3.getY(), var2.getX(), var2.getY());
   }

   private Detector.ResultPointsAndTransitions transitionsBetween(ResultPoint var1, ResultPoint var2) {
      int var4 = (int)var1.getX();
      int var3 = (int)var1.getY();
      int var9 = (int)var2.getX();
      int var8 = (int)var2.getY();
      boolean var7;
      if(Math.abs(var8 - var3) > Math.abs(var9 - var4)) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var5;
      int var6;
      if(!var7) {
         var5 = var9;
         var6 = var4;
         var9 = var8;
         var8 = var5;
         var4 = var3;
         var3 = var6;
      }

      int var15 = Math.abs(var8 - var3);
      int var14 = Math.abs(var9 - var4);
      int var12 = -var15;
      byte var10;
      if(var4 < var9) {
         var10 = 1;
      } else {
         var10 = -1;
      }

      byte var11;
      if(var3 < var8) {
         var11 = 1;
      } else {
         var11 = -1;
      }

      byte var13 = 0;
      BitMatrix var19 = this.image;
      if(var7) {
         var5 = var4;
      } else {
         var5 = var3;
      }

      if(var7) {
         var6 = var3;
      } else {
         var6 = var4;
      }

      boolean var16 = var19.get(var5, var6);
      var12 >>= 1;

      int var20;
      for(var5 = var13; var3 != var8; var12 = var20) {
         var19 = this.image;
         if(var7) {
            var6 = var4;
         } else {
            var6 = var3;
         }

         if(var7) {
            var20 = var3;
         } else {
            var20 = var4;
         }

         boolean var18 = var19.get(var6, var20);
         var6 = var5;
         boolean var17 = var16;
         if(var18 != var16) {
            var6 = var5 + 1;
            var17 = var18;
         }

         var20 = var12 + var14;
         var5 = var20;
         var12 = var4;
         if(var20 > 0) {
            if(var4 == var9) {
               var5 = var6;
               break;
            }

            var12 = var4 + var10;
            var5 = var20 - var15;
         }

         var3 += var11;
         var20 = var5;
         var4 = var12;
         var5 = var6;
         var16 = var17;
      }

      return new Detector.ResultPointsAndTransitions(var1, var2, var5, null);
   }

   public DetectorResult detect() throws NotFoundException {
      ResultPoint[] var3 = this.rectangleDetector.detect();
      ResultPoint var8 = var3[0];
      ResultPoint var9 = var3[1];
      ResultPoint var10 = var3[2];
      ResultPoint var11 = var3[3];
      Vector var4 = new Vector(4);
      var4.addElement(this.transitionsBetween(var8, var9));
      var4.addElement(this.transitionsBetween(var8, var10));
      var4.addElement(this.transitionsBetween(var9, var11));
      var4.addElement(this.transitionsBetween(var10, var11));
      Collections.insertionSort(var4, new Detector.ResultPointsAndTransitionsComparator(null));
      Detector.ResultPointsAndTransitions var14 = (Detector.ResultPointsAndTransitions)var4.elementAt(0);
      Detector.ResultPointsAndTransitions var16 = (Detector.ResultPointsAndTransitions)var4.elementAt(1);
      Hashtable var12 = new Hashtable();
      increment(var12, var14.getFrom());
      increment(var12, var14.getTo());
      increment(var12, var16.getFrom());
      increment(var12, var16.getTo());
      ResultPoint var17 = null;
      ResultPoint var5 = null;
      ResultPoint var15 = null;

      ResultPoint var6;
      ResultPoint var7;
      for(Enumeration var13 = var12.keys(); var13.hasMoreElements(); var5 = var6) {
         var6 = (ResultPoint)var13.nextElement();
         if(((Integer)var12.get(var6)).intValue() == 2) {
            var5 = var17;
            var17 = var6;
         } else if(var17 == null) {
            var17 = var5;
            var5 = var6;
         } else {
            var7 = var17;
            var15 = var6;
            var17 = var5;
            var5 = var7;
         }

         var6 = var17;
         var17 = var5;
      }

      if(var17 != null && var5 != null && var15 != null) {
         ResultPoint[] var18 = new ResultPoint[]{var17, var5, var15};
         ResultPoint.orderBestPatterns(var18);
         var6 = var18[0];
         var7 = var18[1];
         ResultPoint var19 = var18[2];
         if(!var12.containsKey(var8)) {
            var15 = var8;
         } else if(!var12.containsKey(var9)) {
            var15 = var9;
         } else if(!var12.containsKey(var10)) {
            var15 = var10;
         } else {
            var15 = var11;
         }

         int var2 = Math.min(this.transitionsBetween(var19, var15).getTransitions(), this.transitionsBetween(var6, var15).getTransitions());
         int var1 = var2;
         if((var2 & 1) == 1) {
            var1 = var2 + 1;
         }

         var5 = this.correctTopRight(var7, var6, var19, var15, var1 + 2);
         var17 = var5;
         if(var5 == null) {
            var17 = var15;
         }

         var2 = Math.max(this.transitionsBetween(var19, var17).getTransitions(), this.transitionsBetween(var6, var17).getTransitions()) + 1;
         var1 = var2;
         if((var2 & 1) == 1) {
            var1 = var2 + 1;
         }

         return new DetectorResult(sampleGrid(this.image, var19, var7, var6, var17, var1), new ResultPoint[]{var19, var7, var6, var17});
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static class ResultPointsAndTransitions {
      private final ResultPoint from;
      private final ResultPoint to;
      private final int transitions;

      private ResultPointsAndTransitions(ResultPoint var1, ResultPoint var2, int var3) {
         this.from = var1;
         this.to = var2;
         this.transitions = var3;
      }

      ResultPointsAndTransitions(ResultPoint var1, ResultPoint var2, int var3, Object var4) {
         this(var1, var2, var3);
      }

      public ResultPoint getFrom() {
         return this.from;
      }

      public ResultPoint getTo() {
         return this.to;
      }

      public int getTransitions() {
         return this.transitions;
      }

      public String toString() {
         return this.from + "/" + this.to + '/' + this.transitions;
      }
   }

   private static class ResultPointsAndTransitionsComparator implements Comparator {
      private ResultPointsAndTransitionsComparator() {
      }

      ResultPointsAndTransitionsComparator(Object var1) {
         this();
      }

      public int compare(Object var1, Object var2) {
         return ((Detector.ResultPointsAndTransitions)var1).getTransitions() - ((Detector.ResultPointsAndTransitions)var2).getTransitions();
      }
   }
}
