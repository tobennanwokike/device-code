package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.UPCEANReader;

public final class EAN13Reader extends UPCEANReader {
   static final int[] FIRST_DIGIT_ENCODINGS = new int[]{0, 11, 13, 14, 19, 25, 28, 21, 22, 26};
   private final int[] decodeMiddleCounters = new int[4];

   private static void determineFirstDigit(StringBuffer var0, int var1) throws NotFoundException {
      for(int var2 = 0; var2 < 10; ++var2) {
         if(var1 == FIRST_DIGIT_ENCODINGS[var2]) {
            var0.insert(0, (char)(var2 + 48));
            return;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   protected int decodeMiddle(BitArray var1, int[] var2, StringBuffer var3) throws NotFoundException {
      int[] var10 = this.decodeMiddleCounters;
      var10[0] = 0;
      var10[1] = 0;
      var10[2] = 0;
      var10[3] = 0;
      int var8 = var1.getSize();
      int var5 = var2[1];
      int var6 = 0;

      int var4;
      for(var4 = 0; var6 < 6 && var5 < var8; ++var6) {
         int var9 = decodeDigit(var1, var10, var5, L_AND_G_PATTERNS);
         var3.append((char)(var9 % 10 + 48));

         for(int var7 = 0; var7 < var10.length; ++var7) {
            var5 += var10[var7];
         }

         if(var9 >= 10) {
            var4 |= 1 << 5 - var6;
         }
      }

      determineFirstDigit(var3, var4);
      var4 = findGuardPattern(var1, var5, true, MIDDLE_PATTERN)[1];

      for(var5 = 0; var5 < 6 && var4 < var8; ++var5) {
         var3.append((char)(decodeDigit(var1, var10, var4, L_PATTERNS) + 48));

         for(var6 = 0; var6 < var10.length; ++var6) {
            var4 += var10[var6];
         }
      }

      return var4;
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.EAN_13;
   }
}
