package com.google.zxing.oned.rss.expanded.decoders;

abstract class DecodedObject {
   protected final int newPosition;

   DecodedObject(int var1) {
      this.newPosition = var1;
   }

   int getNewPosition() {
      return this.newPosition;
   }
}
