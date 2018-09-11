package com.google.zxing.common.reedsolomon;

import com.google.zxing.common.reedsolomon.GF256Poly;

public final class GF256 {
   public static final GF256 DATA_MATRIX_FIELD = new GF256(301);
   public static final GF256 QR_CODE_FIELD = new GF256(285);
   private final int[] expTable = new int[256];
   private final int[] logTable = new int[256];
   private final GF256Poly one;
   private final GF256Poly zero;

   private GF256(int var1) {
      int var3 = 0;

      for(int var2 = 1; var3 < 256; ++var3) {
         this.expTable[var3] = var2;
         int var4 = var2 << 1;
         var2 = var4;
         if(var4 >= 256) {
            var2 = var4 ^ var1;
         }
      }

      for(var1 = 0; var1 < 255; this.logTable[this.expTable[var1]] = var1++) {
         ;
      }

      this.zero = new GF256Poly(this, new int[]{0});
      this.one = new GF256Poly(this, new int[]{1});
   }

   static int addOrSubtract(int var0, int var1) {
      return var0 ^ var1;
   }

   GF256Poly buildMonomial(int var1, int var2) {
      if(var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         GF256Poly var3;
         if(var2 == 0) {
            var3 = this.zero;
         } else {
            int[] var4 = new int[var1 + 1];
            var4[0] = var2;
            var3 = new GF256Poly(this, var4);
         }

         return var3;
      }
   }

   int exp(int var1) {
      return this.expTable[var1];
   }

   GF256Poly getOne() {
      return this.one;
   }

   GF256Poly getZero() {
      return this.zero;
   }

   int inverse(int var1) {
      if(var1 == 0) {
         throw new ArithmeticException();
      } else {
         return this.expTable[255 - this.logTable[var1]];
      }
   }

   int log(int var1) {
      if(var1 == 0) {
         throw new IllegalArgumentException();
      } else {
         return this.logTable[var1];
      }
   }

   int multiply(int var1, int var2) {
      if(var1 != 0 && var2 != 0) {
         var1 = this.logTable[var1] + this.logTable[var2];
         var1 = this.expTable[(var1 >>> 8) + (var1 & 255)];
      } else {
         var1 = 0;
      }

      return var1;
   }
}
