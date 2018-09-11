package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.oned.rss.expanded.decoders.DecodedObject;

final class DecodedChar extends DecodedObject {
   static final char FNC1 = '$';
   private final char value;

   DecodedChar(int var1, char var2) {
      super(var1);
      this.value = var2;
   }

   char getValue() {
      return this.value;
   }

   boolean isFNC1() {
      boolean var1;
      if(this.value == 36) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
