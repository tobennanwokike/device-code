package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;

final class AnyAIDecoder extends AbstractExpandedDecoder {
   private static final int HEADER_SIZE = 5;

   AnyAIDecoder(BitArray var1) {
      super(var1);
   }

   public String parseInformation() throws NotFoundException {
      StringBuffer var1 = new StringBuffer();
      return this.generalDecoder.decodeAllCodes(var1, 5);
   }
}
