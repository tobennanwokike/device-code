package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AI01decoder;

abstract class AI01weightDecoder extends AI01decoder {
   AI01weightDecoder(BitArray var1) {
      super(var1);
   }

   protected abstract void addWeightCode(StringBuffer var1, int var2);

   protected abstract int checkWeight(int var1);

   protected void encodeCompressedWeight(StringBuffer var1, int var2, int var3) {
      var2 = this.generalDecoder.extractNumericValueFromBitArray(var2, var3);
      this.addWeightCode(var1, var2);
      int var4 = this.checkWeight(var2);
      var2 = 100000;

      for(var3 = 0; var3 < 5; ++var3) {
         if(var4 / var2 == 0) {
            var1.append('0');
         }

         var2 /= 10;
      }

      var1.append(var4);
   }
}
