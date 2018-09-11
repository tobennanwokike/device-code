package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.oned.rss.expanded.decoders.DecodedObject;

final class DecodedNumeric extends DecodedObject {
   static final int FNC1 = 10;
   private final int firstDigit;
   private final int secondDigit;

   DecodedNumeric(int var1, int var2, int var3) {
      super(var1);
      this.firstDigit = var2;
      this.secondDigit = var3;
      if(this.firstDigit >= 0 && this.firstDigit <= 10) {
         if(this.secondDigit < 0 || this.secondDigit > 10) {
            throw new IllegalArgumentException("Invalid secondDigit: " + var3);
         }
      } else {
         throw new IllegalArgumentException("Invalid firstDigit: " + var2);
      }
   }

   int getFirstDigit() {
      return this.firstDigit;
   }

   int getSecondDigit() {
      return this.secondDigit;
   }

   int getValue() {
      return this.firstDigit * 10 + this.secondDigit;
   }

   boolean isAnyFNC1() {
      boolean var1;
      if(this.firstDigit != 10 && this.secondDigit != 10) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   boolean isFirstDigitFNC1() {
      boolean var1;
      if(this.firstDigit == 10) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean isSecondDigitFNC1() {
      boolean var1;
      if(this.secondDigit == 10) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
