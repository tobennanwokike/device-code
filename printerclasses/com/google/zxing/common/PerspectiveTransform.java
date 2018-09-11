package com.google.zxing.common;

public final class PerspectiveTransform {
   private final float a11;
   private final float a12;
   private final float a13;
   private final float a21;
   private final float a22;
   private final float a23;
   private final float a31;
   private final float a32;
   private final float a33;

   private PerspectiveTransform(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.a11 = var1;
      this.a12 = var4;
      this.a13 = var7;
      this.a21 = var2;
      this.a22 = var5;
      this.a23 = var8;
      this.a31 = var3;
      this.a32 = var6;
      this.a33 = var9;
   }

   public static PerspectiveTransform quadrilateralToQuadrilateral(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15) {
      PerspectiveTransform var16 = quadrilateralToSquare(var0, var1, var2, var3, var4, var5, var6, var7);
      return squareToQuadrilateral(var8, var9, var10, var11, var12, var13, var14, var15).times(var16);
   }

   public static PerspectiveTransform quadrilateralToSquare(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      return squareToQuadrilateral(var0, var1, var2, var3, var4, var5, var6, var7).buildAdjoint();
   }

   public static PerspectiveTransform squareToQuadrilateral(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var10 = var7 - var5;
      float var9 = var1 - var3 + var5 - var7;
      PerspectiveTransform var13;
      if(var10 == 0.0F && var9 == 0.0F) {
         var13 = new PerspectiveTransform(var2 - var0, var4 - var2, var0, var3 - var1, var5 - var3, var1, 0.0F, 0.0F, 1.0F);
      } else {
         float var8 = var2 - var4;
         float var11 = var6 - var4;
         var4 = var0 - var2 + var4 - var6;
         var5 = var3 - var5;
         float var12 = var8 * var10 - var11 * var5;
         var10 = (var10 * var4 - var11 * var9) / var12;
         var4 = (var8 * var9 - var4 * var5) / var12;
         var13 = new PerspectiveTransform(var2 - var0 + var10 * var2, var6 - var0 + var4 * var6, var0, var10 * var3 + (var3 - var1), var4 * var7 + (var7 - var1), var1, var10, var4, 1.0F);
      }

      return var13;
   }

   PerspectiveTransform buildAdjoint() {
      return new PerspectiveTransform(this.a22 * this.a33 - this.a23 * this.a32, this.a23 * this.a31 - this.a21 * this.a33, this.a21 * this.a32 - this.a22 * this.a31, this.a13 * this.a32 - this.a12 * this.a33, this.a11 * this.a33 - this.a13 * this.a31, this.a12 * this.a31 - this.a11 * this.a32, this.a12 * this.a23 - this.a13 * this.a22, this.a13 * this.a21 - this.a11 * this.a23, this.a11 * this.a22 - this.a12 * this.a21);
   }

   PerspectiveTransform times(PerspectiveTransform var1) {
      return new PerspectiveTransform(this.a11 * var1.a11 + this.a21 * var1.a12 + this.a31 * var1.a13, this.a11 * var1.a21 + this.a21 * var1.a22 + this.a31 * var1.a23, this.a11 * var1.a31 + this.a21 * var1.a32 + this.a31 * var1.a33, this.a12 * var1.a11 + this.a22 * var1.a12 + this.a32 * var1.a13, this.a12 * var1.a21 + this.a22 * var1.a22 + this.a32 * var1.a23, this.a12 * var1.a31 + this.a22 * var1.a32 + this.a32 * var1.a33, this.a13 * var1.a11 + this.a23 * var1.a12 + this.a33 * var1.a13, this.a13 * var1.a21 + this.a23 * var1.a22 + this.a33 * var1.a23, this.a13 * var1.a31 + this.a23 * var1.a32 + this.a33 * var1.a33);
   }

   public void transformPoints(float[] var1) {
      int var15 = var1.length;
      float var7 = this.a11;
      float var4 = this.a12;
      float var2 = this.a13;
      float var3 = this.a21;
      float var10 = this.a22;
      float var8 = this.a23;
      float var6 = this.a31;
      float var13 = this.a32;
      float var5 = this.a33;

      for(int var14 = 0; var14 < var15; var14 += 2) {
         float var9 = var1[var14];
         float var11 = var1[var14 + 1];
         float var12 = var2 * var9 + var8 * var11 + var5;
         var1[var14] = (var7 * var9 + var3 * var11 + var6) / var12;
         var1[var14 + 1] = (var9 * var4 + var11 * var10 + var13) / var12;
      }

   }

   public void transformPoints(float[] var1, float[] var2) {
      int var7 = var1.length;

      for(int var6 = 0; var6 < var7; ++var6) {
         float var4 = var1[var6];
         float var3 = var2[var6];
         float var5 = this.a13 * var4 + this.a23 * var3 + this.a33;
         var1[var6] = (this.a11 * var4 + this.a21 * var3 + this.a31) / var5;
         var2[var6] = (var4 * this.a12 + var3 * this.a22 + this.a32) / var5;
      }

   }
}
