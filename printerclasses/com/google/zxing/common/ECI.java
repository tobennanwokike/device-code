package com.google.zxing.common;

import com.google.zxing.common.CharacterSetECI;

public abstract class ECI {
   private final int value;

   ECI(int var1) {
      this.value = var1;
   }

   public static ECI getECIByValue(int var0) {
      if(var0 >= 0 && var0 <= 999999) {
         CharacterSetECI var1;
         if(var0 < 900) {
            var1 = CharacterSetECI.getCharacterSetECIByValue(var0);
         } else {
            var1 = null;
         }

         return var1;
      } else {
         throw new IllegalArgumentException("Bad ECI value: " + var0);
      }
   }

   public int getValue() {
      return this.value;
   }
}
