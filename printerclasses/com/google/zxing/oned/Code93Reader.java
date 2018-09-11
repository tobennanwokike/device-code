package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.OneDReader;
import java.util.Hashtable;

public final class Code93Reader extends OneDReader {
   private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".toCharArray();
   private static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*";
   private static final int ASTERISK_ENCODING;
   private static final int[] CHARACTER_ENCODINGS = new int[]{276, 328, 324, 322, 296, 292, 290, 336, 274, 266, 424, 420, 418, 404, 402, 394, 360, 356, 354, 308, 282, 344, 332, 326, 300, 278, 436, 434, 428, 422, 406, 410, 364, 358, 310, 314, 302, 468, 466, 458, 366, 374, 430, 294, 474, 470, 306, 350};

   static {
      ASTERISK_ENCODING = CHARACTER_ENCODINGS[47];
   }

   private static void checkChecksums(StringBuffer var0) throws ChecksumException {
      int var1 = var0.length();
      checkOneChecksum(var0, var1 - 2, 20);
      checkOneChecksum(var0, var1 - 1, 15);
   }

   private static void checkOneChecksum(StringBuffer var0, int var1, int var2) throws ChecksumException {
      int var3 = 1;
      int var5 = var1 - 1;
      int var4 = 0;

      while(true) {
         int var6 = var3;
         if(var5 < 0) {
            if(var0.charAt(var1) != ALPHABET[var4 % 47]) {
               throw ChecksumException.getChecksumInstance();
            }

            return;
         }

         int var8 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%abcd*".indexOf(var0.charAt(var5));
         int var7 = var3 + 1;
         var3 = var7;
         if(var7 > var2) {
            var3 = 1;
         }

         --var5;
         var4 += var8 * var6;
      }
   }

   private static String decodeExtended(StringBuffer var0) throws FormatException {
      int var3 = var0.length();
      StringBuffer var5 = new StringBuffer(var3);

      for(int var2 = 0; var2 < var3; ++var2) {
         char var1 = var0.charAt(var2);
         if(var1 >= 97 && var1 <= 100) {
            char var4 = var0.charAt(var2 + 1);
            switch(var1) {
            case 'a':
               if(var4 < 65 || var4 > 90) {
                  throw FormatException.getFormatInstance();
               }

               var1 = (char)(var4 - 64);
               break;
            case 'b':
               if(var4 >= 65 && var4 <= 69) {
                  var1 = (char)(var4 - 38);
               } else {
                  if(var4 < 70 || var4 > 87) {
                     throw FormatException.getFormatInstance();
                  }

                  var1 = (char)(var4 - 11);
               }
               break;
            case 'c':
               if(var4 >= 65 && var4 <= 79) {
                  var1 = (char)(var4 - 32);
               } else {
                  if(var4 != 90) {
                     throw FormatException.getFormatInstance();
                  }

                  var1 = 58;
               }
               break;
            case 'd':
               if(var4 >= 65 && var4 <= 90) {
                  var1 = (char)(var4 + 32);
                  break;
               }

               throw FormatException.getFormatInstance();
            default:
               var1 = 0;
            }

            var5.append(var1);
            ++var2;
         } else {
            var5.append(var1);
         }
      }

      return var5.toString();
   }

   private static int[] findAsteriskPattern(BitArray var0) throws NotFoundException {
      int var7 = var0.getSize();

      int var1;
      for(var1 = 0; var1 < var7 && !var0.get(var1); ++var1) {
         ;
      }

      int[] var9 = new int[6];
      int var8 = var9.length;
      int var3 = var1;
      boolean var4 = false;

      boolean var10;
      for(int var5 = 0; var3 < var7; var4 = var10) {
         int var11;
         if(var0.get(var3) ^ var4) {
            ++var9[var5];
            var10 = var4;
            var11 = var1;
         } else {
            int var2;
            if(var5 == var8 - 1) {
               if(toPattern(var9) == ASTERISK_ENCODING) {
                  return new int[]{var1, var3};
               }

               var2 = var1 + var9[0] + var9[1];

               for(var1 = 2; var1 < var8; ++var1) {
                  var9[var1 - 2] = var9[var1];
               }

               var9[var8 - 2] = 0;
               var9[var8 - 1] = 0;
               var1 = var5 - 1;
            } else {
               ++var5;
               var2 = var1;
               var1 = var5;
            }

            var9[var1] = 1;
            boolean var6;
            if(!var4) {
               var6 = true;
               var11 = var2;
               var5 = var1;
               var10 = var6;
            } else {
               var6 = false;
               var11 = var2;
               var5 = var1;
               var10 = var6;
            }
         }

         ++var3;
         var1 = var11;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static char patternToChar(int var0) throws NotFoundException {
      for(int var1 = 0; var1 < CHARACTER_ENCODINGS.length; ++var1) {
         if(CHARACTER_ENCODINGS[var1] == var0) {
            return ALPHABET[var1];
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int toPattern(int[] var0) {
      int var7 = var0.length;
      int var1 = 0;

      int var2;
      int var3;
      for(var3 = 0; var1 < var7; var3 += var2) {
         var2 = var0[var1];
         ++var1;
      }

      int var4 = 0;
      var1 = 0;

      while(true) {
         var2 = var1;
         if(var4 >= var7) {
            break;
         }

         var2 = (var0[var4] << 8) * 9 / var3;
         int var5 = var2 >> 8;
         if((var2 & 255) > 127) {
            ++var5;
         }

         if(var5 < 1 || var5 > 4) {
            var2 = -1;
            break;
         }

         if((var4 & 1) == 0) {
            int var6 = 0;
            var2 = var1;

            while(true) {
               var1 = var2;
               if(var6 >= var5) {
                  break;
               }

               ++var6;
               var2 = var2 << 1 | 1;
            }
         } else {
            var1 <<= var5;
         }

         ++var4;
      }

      return var2;
   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException, ChecksumException, FormatException {
      int[] var14 = findAsteriskPattern(var2);
      int var7 = var14[1];

      int var10;
      for(var10 = var2.getSize(); var7 < var10 && !var2.get(var7); ++var7) {
         ;
      }

      StringBuffer var12 = new StringBuffer(20);
      int[] var11 = new int[6];

      while(true) {
         recordPattern(var2, var7, var11);
         int var8 = toPattern(var11);
         if(var8 < 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         char var4 = patternToChar(var8);
         var12.append(var4);
         int var9 = 0;

         for(var8 = var7; var9 < var11.length; ++var9) {
            var8 += var11[var9];
         }

         while(var8 < var10 && !var2.get(var8)) {
            ++var8;
         }

         if(var4 == 42) {
            var12.deleteCharAt(var12.length() - 1);
            if(var8 != var10 && var2.get(var8)) {
               if(var12.length() < 2) {
                  throw NotFoundException.getNotFoundInstance();
               }

               checkChecksums(var12);
               var12.setLength(var12.length() - 2);
               String var13 = decodeExtended(var12);
               var9 = var14[1];
               float var6 = (float)(var14[0] + var9) / 2.0F;
               float var5 = (float)(var7 + var8) / 2.0F;
               ResultPoint var17 = new ResultPoint(var6, (float)var1);
               ResultPoint var16 = new ResultPoint(var5, (float)var1);
               BarcodeFormat var15 = BarcodeFormat.CODE_93;
               return new Result(var13, (byte[])null, new ResultPoint[]{var17, var16}, var15);
            }

            throw NotFoundException.getNotFoundInstance();
         }

         var7 = var8;
      }
   }
}
