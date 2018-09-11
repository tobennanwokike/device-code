package com.google.zxing.common.reedsolomon;

import com.google.zxing.common.reedsolomon.GF256;
import com.google.zxing.common.reedsolomon.GF256Poly;
import java.util.Vector;

public final class ReedSolomonEncoder {
   private final Vector cachedGenerators;
   private final GF256 field;

   public ReedSolomonEncoder(GF256 var1) {
      if(!GF256.QR_CODE_FIELD.equals(var1)) {
         throw new IllegalArgumentException("Only QR Code is supported at this time");
      } else {
         this.field = var1;
         this.cachedGenerators = new Vector();
         this.cachedGenerators.addElement(new GF256Poly(var1, new int[]{1}));
      }
   }

   private GF256Poly buildGenerator(int var1) {
      if(var1 >= this.cachedGenerators.size()) {
         GF256Poly var3 = (GF256Poly)this.cachedGenerators.elementAt(this.cachedGenerators.size() - 1);

         for(int var2 = this.cachedGenerators.size(); var2 <= var1; ++var2) {
            var3 = var3.multiply(new GF256Poly(this.field, new int[]{1, this.field.exp(var2 - 1)}));
            this.cachedGenerators.addElement(var3);
         }
      }

      return (GF256Poly)this.cachedGenerators.elementAt(var1);
   }

   public void encode(int[] var1, int var2) {
      if(var2 == 0) {
         throw new IllegalArgumentException("No error correction bytes");
      } else {
         int var3 = var1.length - var2;
         if(var3 <= 0) {
            throw new IllegalArgumentException("No data bytes provided");
         } else {
            GF256Poly var6 = this.buildGenerator(var2);
            int[] var5 = new int[var3];
            System.arraycopy(var1, 0, var5, 0, var3);
            var5 = (new GF256Poly(this.field, var5)).multiplyByMonomial(var2, 1).divide(var6)[1].getCoefficients();
            int var4 = var2 - var5.length;

            for(var2 = 0; var2 < var4; ++var2) {
               var1[var3 + var2] = 0;
            }

            System.arraycopy(var5, 0, var1, var3 + var4, var5.length);
         }
      }
   }
}
