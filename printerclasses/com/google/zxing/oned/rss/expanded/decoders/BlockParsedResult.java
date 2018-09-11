package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.oned.rss.expanded.decoders.DecodedInformation;

final class BlockParsedResult {
   private final DecodedInformation decodedInformation;
   private final boolean finished;

   BlockParsedResult() {
      this.finished = true;
      this.decodedInformation = null;
   }

   BlockParsedResult(DecodedInformation var1, boolean var2) {
      this.finished = var2;
      this.decodedInformation = var1;
   }

   BlockParsedResult(boolean var1) {
      this.finished = var1;
      this.decodedInformation = null;
   }

   DecodedInformation getDecodedInformation() {
      return this.decodedInformation;
   }

   boolean isFinished() {
      return this.finished;
   }
}
