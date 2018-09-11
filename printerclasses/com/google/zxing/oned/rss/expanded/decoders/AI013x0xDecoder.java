package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AI01weightDecoder;

abstract class AI013x0xDecoder extends AI01weightDecoder {
   private static final int headerSize = 5;
   private static final int weightSize = 15;

   AI013x0xDecoder(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException {
      if(this.information.size != 60) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         StringBuffer var1 = new StringBuffer();
         this.encodeCompressedGtin(var1, 5);
         this.encodeCompressedWeight(var1, 45, 15);
         return var1.toString();
      }
   }
}
