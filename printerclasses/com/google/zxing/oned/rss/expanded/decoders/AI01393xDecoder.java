package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AI01decoder;

final class AI01393xDecoder extends AI01decoder {
   private static final int firstThreeDigitsSize = 10;
   private static final int headerSize = 8;
   private static final int lastDigitSize = 2;

   AI01393xDecoder(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException {
      if(this.information.size < 48) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuffer var2 = new StringBuffer();
         this.encodeCompressedGtin(var2, 8);
         int var1 = this.generalDecoder.extractNumericValueFromBitArray(48, 2);
         var2.append("(393");
         var2.append(var1);
         var2.append(')');
         var1 = this.generalDecoder.extractNumericValueFromBitArray(50, 10);
         if(var1 / 100 == 0) {
            var2.append('0');
         }

         if(var1 / 10 == 0) {
            var2.append('0');
         }

         var2.append(var1);
         var2.append(this.generalDecoder.decodeGeneralPurposeField(60, (String)null).getNewString());
         return var2.toString();
      }
   }
}
