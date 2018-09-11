package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.UPCEANReader;

public final class EAN8Reader extends UPCEANReader {
   private final int[] decodeMiddleCounters = new int[4];

   protected int decodeMiddle(BitArray var1, int[] var2, StringBuffer var3) throws NotFoundException {
      int[] var8 = this.decodeMiddleCounters;
      var8[0] = 0;
      var8[1] = 0;
      var8[2] = 0;
      var8[3] = 0;
      int var7 = var1.getSize();
      int var4 = var2[1];

      int var5;
      int var6;
      for(var5 = 0; var5 < 4 && var4 < var7; ++var5) {
         var3.append((char)(decodeDigit(var1, var8, var4, L_PATTERNS) + 48));

         for(var6 = 0; var6 < var8.length; ++var6) {
            var4 += var8[var6];
         }
      }

      var4 = findGuardPattern(var1, var4, true, MIDDLE_PATTERN)[1];

      for(var5 = 0; var5 < 4 && var4 < var7; ++var5) {
         var3.append((char)(decodeDigit(var1, var8, var4, L_PATTERNS) + 48));

         for(var6 = 0; var6 < var8.length; ++var6) {
            var4 += var8[var6];
         }
      }

      return var4;
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.EAN_8;
   }
}
