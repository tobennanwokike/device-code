package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.UPCEANReader;
import java.util.Hashtable;

final class UPCEANExtensionSupport {
   private static final int[] CHECK_DIGIT_ENCODINGS = new int[]{24, 20, 18, 17, 12, 6, 3, 10, 9, 5};
   private static final int[] EXTENSION_START_PATTERN = new int[]{1, 1, 2};
   private final int[] decodeMiddleCounters = new int[4];
   private final StringBuffer decodeRowStringBuffer = new StringBuffer();

   private static int determineCheckDigit(int var0) throws NotFoundException {
      for(int var1 = 0; var1 < 10; ++var1) {
         if(var0 == CHECK_DIGIT_ENCODINGS[var1]) {
            return var1;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int extensionChecksum(String var0) {
      int var3 = var0.length();
      int var2 = 0;

      int var1;
      for(var1 = var3 - 2; var1 >= 0; var1 -= 2) {
         var2 += var0.charAt(var1) - 48;
      }

      var1 = var2 * 3;

      for(var2 = var3 - 1; var2 >= 0; var2 -= 2) {
         var1 += var0.charAt(var2) - 48;
      }

      return var1 * 3 % 10;
   }

   private static Integer parseExtension2String(String var0) {
      return Integer.valueOf(var0);
   }

   private static String parseExtension5String(String var0) {
      String var2 = null;
      switch(var0.charAt(0)) {
      case '0':
         var2 = "拢";
         break;
      case '5':
         var2 = "$";
         break;
      case '9':
         if("99991".equals(var0)) {
            var0 = "0.00";
            return var0;
         }

         if("99990".equals(var0)) {
            var0 = "Used";
            return var0;
         }
         break;
      default:
         var2 = "";
      }

      int var1 = Integer.parseInt(var0.substring(1));
      var0 = var2 + var1 / 100 + '.' + var1 % 100;
      return var0;
   }

   private static Hashtable parseExtensionString(String var0) {
      Object var3 = null;
      Hashtable var2 = (Hashtable)var3;
      ResultMetadataType var1;
      Object var4;
      switch(var0.length()) {
      case 2:
         var1 = ResultMetadataType.ISSUE_NUMBER;
         var4 = parseExtension2String(var0);
         break;
      case 3:
      case 4:
         return var2;
      case 5:
         var1 = ResultMetadataType.SUGGESTED_PRICE;
         var4 = parseExtension5String(var0);
         break;
      default:
         var2 = (Hashtable)var3;
         return var2;
      }

      var2 = (Hashtable)var3;
      if(var4 != null) {
         var2 = new Hashtable(1);
         var2.put(var1, var4);
      }

      return var2;
   }

   int decodeMiddle(BitArray var1, int[] var2, StringBuffer var3) throws NotFoundException {
      int[] var10 = this.decodeMiddleCounters;
      var10[0] = 0;
      var10[1] = 0;
      var10[2] = 0;
      var10[3] = 0;
      int var8 = var1.getSize();
      int var4 = var2[1];
      int var7 = 0;

      int var5;
      int var6;
      for(var5 = 0; var7 < 5 && var4 < var8; var4 = var6) {
         int var9 = UPCEANReader.decodeDigit(var1, var10, var4, UPCEANReader.L_AND_G_PATTERNS);
         var3.append((char)(var9 % 10 + 48));

         for(var6 = 0; var6 < var10.length; ++var6) {
            var4 += var10[var6];
         }

         if(var9 >= 10) {
            var5 |= 1 << 4 - var7;
         }

         var6 = var4;
         if(var7 != 4) {
            var6 = var4;

            while(true) {
               var4 = var6;
               if(var6 >= var8) {
                  break;
               }

               var4 = var6;
               if(var1.get(var6)) {
                  break;
               }

               ++var6;
            }

            while(true) {
               var6 = var4;
               if(var4 >= var8) {
                  break;
               }

               var6 = var4;
               if(!var1.get(var4)) {
                  break;
               }

               ++var4;
            }
         }

         ++var7;
      }

      if(var3.length() != 5) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         var5 = determineCheckDigit(var5);
         if(extensionChecksum(var3.toString()) != var5) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            return var4;
         }
      }
   }

   Result decodeRow(int var1, BitArray var2, int var3) throws NotFoundException {
      int[] var6 = UPCEANReader.findGuardPattern(var2, var3, false, EXTENSION_START_PATTERN);
      StringBuffer var5 = this.decodeRowStringBuffer;
      var5.setLength(0);
      var3 = this.decodeMiddle(var2, var6, var5);
      String var10 = var5.toString();
      Hashtable var9 = parseExtensionString(var10);
      int var4 = var6[0];
      ResultPoint var7 = new ResultPoint((float)(var6[1] + var4) / 2.0F, (float)var1);
      ResultPoint var12 = new ResultPoint((float)var3, (float)var1);
      BarcodeFormat var8 = BarcodeFormat.UPC_EAN_EXTENSION;
      Result var11 = new Result(var10, (byte[])null, new ResultPoint[]{var7, var12}, var8);
      if(var9 != null) {
         var11.putAllMetadata(var9);
      }

      return var11;
   }
}
