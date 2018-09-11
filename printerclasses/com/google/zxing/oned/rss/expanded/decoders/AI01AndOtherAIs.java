package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AI01decoder;

final class AI01AndOtherAIs extends AI01decoder {
   private static final int HEADER_SIZE = 4;

   AI01AndOtherAIs(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException {
      StringBuffer var2 = new StringBuffer();
      var2.append("(01)");
      int var1 = var2.length();
      var2.append(this.generalDecoder.extractNumericValueFromBitArray(4, 4));
      this.encodeCompressedGtinWithoutAI(var2, 8, var1);
      return this.generalDecoder.decodeAllCodes(var2, 48);
   }
}
