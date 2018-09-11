package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AI01decoder;

final class AI01392xDecoder extends AI01decoder {
   private static final int headerSize = 8;
   private static final int lastDigitSize = 2;

   AI01392xDecoder(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException {
      if(this.information.size < 48) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuffer var2 = new StringBuffer();
         this.encodeCompressedGtin(var2, 8);
         int var1 = this.generalDecoder.extractNumericValueFromBitArray(48, 2);
         var2.append("(392");
         var2.append(var1);
         var2.append(')');
         var2.append(this.generalDecoder.decodeGeneralPurposeField(50, (String)null).getNewString());
         return var2.toString();
      }
   }
}
