package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.UPCEANReader;

public final class UPCEReader extends UPCEANReader {
   private static final int[] MIDDLE_END_PATTERN = new int[]{1, 1, 1, 1, 1, 1};
   private static final int[][] NUMSYS_AND_CHECK_DIGIT_PATTERNS;
   private final int[] decodeMiddleCounters = new int[4];

   static {
      int[] var0 = new int[]{7, 11, 13, 14, 19, 25, 28, 21, 22, 26};
      NUMSYS_AND_CHECK_DIGIT_PATTERNS = new int[][]{{56, 52, 50, 49, 44, 38, 35, 42, 41, 37}, var0};
   }

   public static String convertUPCEtoUPCA(String var0) {
      char[] var2 = new char[6];
      var0.getChars(1, 7, var2, 0);
      StringBuffer var3 = new StringBuffer(12);
      var3.append(var0.charAt(0));
      char var1 = var2[5];
      switch(var1) {
      case '0':
      case '1':
      case '2':
         var3.append(var2, 0, 2);
         var3.append(var1);
         var3.append("0000");
         var3.append(var2, 2, 3);
         break;
      case '3':
         var3.append(var2, 0, 3);
         var3.append("00000");
         var3.append(var2, 3, 2);
         break;
      case '4':
         var3.append(var2, 0, 4);
         var3.append("00000");
         var3.append(var2[4]);
         break;
      default:
         var3.append(var2, 0, 5);
         var3.append("0000");
         var3.append(var1);
      }

      var3.append(var0.charAt(7));
      return var3.toString();
   }

   private static void determineNumSysAndCheckDigit(StringBuffer var0, int var1) throws NotFoundException {
      for(int var2 = 0; var2 <= 1; ++var2) {
         for(int var3 = 0; var3 < 10; ++var3) {
            if(var1 == NUMSYS_AND_CHECK_DIGIT_PATTERNS[var2][var3]) {
               var0.insert(0, (char)(var2 + 48));
               var0.append((char)(var3 + 48));
               return;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   protected boolean checkChecksum(String var1) throws FormatException, ChecksumException {
      return super.checkChecksum(convertUPCEtoUPCA(var1));
   }

   protected int[] decodeEnd(BitArray var1, int var2) throws NotFoundException {
      return findGuardPattern(var1, var2, true, MIDDLE_END_PATTERN);
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

      determineNumSysAndCheckDigit(var3, var4);
      return var5;
   }

   BarcodeFormat getBarcodeFormat() {
      return BarcodeFormat.UPC_E;
   }
}
