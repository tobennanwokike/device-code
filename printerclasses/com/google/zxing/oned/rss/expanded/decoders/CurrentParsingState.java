package com.google.zxing.oned.rss.expanded.decoders;

final class CurrentParsingState {
   private static final int ALPHA = 2;
   private static final int ISO_IEC_646 = 4;
   private static final int NUMERIC = 1;
   private int encoding = 1;
   int position = 0;

   boolean isAlpha() {
      boolean var1;
      if(this.encoding == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean isIsoIec646() {
      boolean var1;
      if(this.encoding == 4) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean isNumeric() {
      boolean var1 = true;
      if(this.encoding != 1) {
         var1 = false;
      }

      return var1;
   }

   void setAlpha() {
      this.encoding = 2;
   }

   void setIsoIec646() {
      this.encoding = 4;
   }

   void setNumeric() {
      this.encoding = 1;
   }
}
