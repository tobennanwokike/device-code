package com.google.zxing.common.reedsolomon;

import com.google.zxing.common.reedsolomon.GF256;
import com.google.zxing.common.reedsolomon.GF256Poly;
import com.google.zxing.common.reedsolomon.ReedSolomonException;

public final class ReedSolomonDecoder {
   private final GF256 field;

   public ReedSolomonDecoder(GF256 var1) {
      this.field = var1;
   }

   private int[] findErrorLocations(GF256Poly var1) throws ReedSolomonException {
      int var2 = 1;
      int var5 = var1.getDegree();
      int[] var6;
      int[] var7;
      if(var5 == 1) {
         var6 = new int[]{var1.getCoefficient(1)};
         var7 = var6;
      } else {
         var6 = new int[var5];

         int var3;
         int var4;
         for(var3 = 0; var2 < 256 && var3 < var5; var3 = var4) {
            var4 = var3;
            if(var1.evaluateAt(var2) == 0) {
               var6[var3] = this.field.inverse(var2);
               var4 = var3 + 1;
            }

            ++var2;
         }

         if(var3 != var5) {
            throw new ReedSolomonException("Error locator degree does not match number of roots");
         }

         var7 = var6;
      }

      return var7;
   }

   private int[] findErrorMagnitudes(GF256Poly var1, int[] var2, boolean var3) {
      int var8 = var2.length;
      int[] var10 = new int[var8];

      for(int var5 = 0; var5 < var8; ++var5) {
         int var9 = this.field.inverse(var2[var5]);
         int var4 = 1;

         for(int var6 = 0; var6 < var8; ++var6) {
            if(var5 != var6) {
               int var7 = this.field.multiply(var2[var6], var9);
               if((var7 & 1) == 0) {
                  var7 |= 1;
               } else {
                  var7 &= -2;
               }

               var4 = this.field.multiply(var4, var7);
            }
         }

         var10[var5] = this.field.multiply(var1.evaluateAt(var9), this.field.inverse(var4));
         if(var3) {
            var10[var5] = this.field.multiply(var10[var5], var9);
         }
      }

      return var10;
   }

   private GF256Poly[] runEuclideanAlgorithm(GF256Poly var1, GF256Poly var2, int var3) throws ReedSolomonException {
      GF256Poly var7;
      if(var1.getDegree() >= var2.getDegree()) {
         var7 = var1;
         var1 = var2;
         var2 = var7;
      }

      GF256Poly var10 = this.field.getOne();
      GF256Poly var11 = this.field.getZero();
      GF256Poly var9 = this.field.getZero();
      GF256Poly var8 = this.field.getOne();
      var7 = var2;

      for(var2 = var11; var1.getDegree() >= var3 / 2; var7 = var11) {
         if(var1.isZero()) {
            throw new ReedSolomonException("r_{i-1} was zero");
         }

         var11 = this.field.getZero();
         int var4 = var1.getCoefficient(var1.getDegree());

         int var5;
         for(int var6 = this.field.inverse(var4); var7.getDegree() >= var1.getDegree() && !var7.isZero(); var7 = var7.addOrSubtract(var1.multiplyByMonomial(var5, var4))) {
            var5 = var7.getDegree() - var1.getDegree();
            var4 = this.field.multiply(var7.getCoefficient(var7.getDegree()), var6);
            var11 = var11.addOrSubtract(this.field.buildMonomial(var5, var4));
         }

         GF256Poly var12 = var11.multiply(var2).addOrSubtract(var10);
         var9 = var11.multiply(var8).addOrSubtract(var9);
         var11 = var1;
         var1 = var7;
         var10 = var2;
         var2 = var12;
         var7 = var9;
         var9 = var8;
         var8 = var7;
      }

      var3 = var8.getCoefficient(0);
      if(var3 == 0) {
         throw new ReedSolomonException("sigmaTilde(0) was zero");
      } else {
         var3 = this.field.inverse(var3);
         return new GF256Poly[]{var8.multiply(var3), var1.multiply(var3)};
      }
   }

   public void decode(int[] var1, int var2) throws ReedSolomonException {
      byte var6 = 0;
      GF256Poly var10 = new GF256Poly(this.field, var1);
      int[] var8 = new int[var2];
      boolean var7 = this.field.equals(GF256.DATA_MATRIX_FIELD);
      int var3 = 0;

      boolean var4;
      for(var4 = true; var3 < var2; ++var3) {
         GF256 var9 = this.field;
         int var5;
         if(var7) {
            var5 = var3 + 1;
         } else {
            var5 = var3;
         }

         var5 = var10.evaluateAt(var9.exp(var5));
         var8[var8.length - 1 - var3] = var5;
         if(var5 != 0) {
            var4 = false;
         }
      }

      if(!var4) {
         GF256Poly var11 = new GF256Poly(this.field, var8);
         GF256Poly[] var12 = this.runEuclideanAlgorithm(this.field.buildMonomial(var2, 1), var11, var2);
         GF256Poly var13 = var12[0];
         var11 = var12[1];
         int[] var14 = this.findErrorLocations(var13);
         var8 = this.findErrorMagnitudes(var11, var14, var7);

         for(var2 = var6; var2 < var14.length; ++var2) {
            var3 = var1.length - 1 - this.field.log(var14[var2]);
            if(var3 < 0) {
               throw new ReedSolomonException("Bad error location");
            }

            var1[var3] = GF256.addOrSubtract(var1[var3], var8[var2]);
         }
      }

   }
}
