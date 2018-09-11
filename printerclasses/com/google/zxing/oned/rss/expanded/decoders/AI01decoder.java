package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;

abstract class AI01decoder extends AbstractExpandedDecoder {
   protected static final int gtinSize = 40;

   AI01decoder(BitArray var1) {
      super(var1);
   }

   private static void appendCheckDigit(StringBuffer var0, int var1) {
      int var3 = 0;

      int var2;
      for(var2 = 0; var3 < 13; ++var3) {
         int var5 = var0.charAt(var3 + var1) - 48;
         int var4 = var5;
         if((var3 & 1) == 0) {
            var4 = var5 * 3;
         }

         var2 += var4;
      }

      var2 = 10 - var2 % 10;
      var1 = var2;
      if(var2 == 10) {
         var1 = 0;
      }

      var0.append(var1);
   }

   protected void encodeCompressedGtin(StringBuffer var1, int var2) {
      var1.append("(01)");
      int var3 = var1.length();
      var1.append('9');
      this.encodeCompressedGtinWithoutAI(var1, var2, var3);
   }

   protected void encodeCompressedGtinWithoutAI(StringBuffer var1, int var2, int var3) {
      for(int var4 = 0; var4 < 4; ++var4) {
         int var5 = this.generalDecoder.extractNumericValueFromBitArray(var4 * 10 + var2, 10);
         if(var5 / 100 == 0) {
            var1.append('0');
         }

         if(var5 / 10 == 0) {
            var1.append('0');
         }

         var1.append(var5);
      }

      appendCheckDigit(var1, var3);
   }
}
