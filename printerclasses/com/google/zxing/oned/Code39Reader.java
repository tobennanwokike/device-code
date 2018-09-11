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

public final class Code39Reader extends OneDReader {
   private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".toCharArray();
   static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";
   private static final int ASTERISK_ENCODING;
   static final int[] CHARACTER_ENCODINGS = new int[]{52, 289, 97, 352, 49, 304, 112, 37, 292, 100, 265, 73, 328, 25, 280, 88, 13, 268, 76, 28, 259, 67, 322, 19, 274, 82, 7, 262, 70, 22, 385, 193, 448, 145, 400, 208, 133, 388, 196, 148, 168, 162, 138, 42};
   private final boolean extendedMode;
   private final boolean usingCheckDigit;

   static {
      ASTERISK_ENCODING = CHARACTER_ENCODINGS[39];
   }

   public Code39Reader() {
      this.usingCheckDigit = false;
      this.extendedMode = false;
   }

   public Code39Reader(boolean var1) {
      this.usingCheckDigit = var1;
      this.extendedMode = false;
   }

   public Code39Reader(boolean var1, boolean var2) {
      this.usingCheckDigit = var1;
      this.extendedMode = var2;
   }

   private static String decodeExtended(StringBuffer var0) throws FormatException {
      int var3 = var0.length();
      StringBuffer var5 = new StringBuffer(var3);

      for(int var2 = 0; var2 < var3; ++var2) {
         char var1 = var0.charAt(var2);
         if(var1 != 43 && var1 != 36 && var1 != 37 && var1 != 47) {
            var5.append(var1);
         } else {
            char var4 = var0.charAt(var2 + 1);
            switch(var1) {
            case '$':
               if(var4 < 65 || var4 > 90) {
                  throw FormatException.getFormatInstance();
               }

               var1 = (char)(var4 - 64);
               break;
            case '%':
               if(var4 >= 65 && var4 <= 69) {
                  var1 = (char)(var4 - 38);
               } else {
                  if(var4 < 70 || var4 > 87) {
                     throw FormatException.getFormatInstance();
                  }

                  var1 = (char)(var4 - 11);
               }
               break;
            case '+':
               if(var4 >= 65 && var4 <= 90) {
                  var1 = (char)(var4 + 32);
                  break;
               }

               throw FormatException.getFormatInstance();
            case '/':
               if(var4 >= 65 && var4 <= 79) {
                  var1 = (char)(var4 - 32);
               } else {
                  if(var4 != 90) {
                     throw FormatException.getFormatInstance();
                  }

                  var1 = 58;
               }
               break;
            default:
               var1 = 0;
            }

            var5.append(var1);
            ++var2;
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

      int[] var9 = new int[9];
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
               if(toNarrowWidePattern(var9) == ASTERISK_ENCODING && var0.isRange(Math.max(0, var1 - (var3 - var1) / 2), var1, false)) {
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

   private static int toNarrowWidePattern(int[] var0) {
      int var9 = var0.length;
      int var2 = 0;

      while(true) {
         int var1 = Integer.MAX_VALUE;

         int var3;
         int var4;
         int var5;
         for(var3 = 0; var3 < var9; var1 = var4) {
            var5 = var0[var3];
            var4 = var1;
            if(var5 < var1) {
               var4 = var1;
               if(var5 > var2) {
                  var4 = var5;
               }
            }

            ++var3;
         }

         var5 = 0;
         var2 = 0;
         var4 = 0;

         int var6;
         int var7;
         for(var3 = 0; var5 < var9; var3 = var6) {
            int var10 = var0[var5];
            int var8 = var2;
            var7 = var4;
            var6 = var3;
            if(var0[var5] > var1) {
               var8 = var2 | 1 << var9 - 1 - var5;
               var6 = var3 + 1;
               var7 = var4 + var10;
            }

            ++var5;
            var2 = var8;
            var4 = var7;
         }

         if(var3 == 3) {
            var5 = 0;
            var6 = var3;

            while(true) {
               var3 = var2;
               if(var5 >= var9) {
                  return var3;
               }

               var3 = var2;
               if(var6 <= 0) {
                  return var3;
               }

               var7 = var0[var5];
               var3 = var6;
               if(var0[var5] > var1) {
                  var3 = var6 - 1;
                  if(var7 << 1 >= var4) {
                     var3 = -1;
                     return var3;
                  }
               }

               ++var5;
               var6 = var3;
            }
         }

         if(var3 <= 3) {
            var3 = -1;
            return var3;
         }

         var2 = var1;
      }
   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException, ChecksumException, FormatException {
      int[] var15 = findAsteriskPattern(var2);
      int var7 = var15[1];

      int var11;
      for(var11 = var2.getSize(); var7 < var11 && !var2.get(var7); ++var7) {
         ;
      }

      StringBuffer var12 = new StringBuffer(20);
      int[] var13 = new int[9];

      while(true) {
         recordPattern(var2, var7, var13);
         int var8 = toNarrowWidePattern(var13);
         if(var8 < 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         char var4 = patternToChar(var8);
         var12.append(var4);
         int var9 = 0;

         for(var8 = var7; var9 < var13.length; ++var9) {
            var8 += var13[var9];
         }

         while(var8 < var11 && !var2.get(var8)) {
            ++var8;
         }

         if(var4 == 42) {
            var12.deleteCharAt(var12.length() - 1);
            int var10 = 0;

            for(var9 = 0; var10 < var13.length; ++var10) {
               var9 += var13[var10];
            }

            if(var8 != var11 && (var8 - var7 - var9) / 2 < var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            if(this.usingCheckDigit) {
               var11 = var12.length() - 1;
               var10 = 0;

               for(var9 = 0; var10 < var11; ++var10) {
                  var9 += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(var12.charAt(var10));
               }

               if(var12.charAt(var11) != ALPHABET[var9 % 43]) {
                  throw ChecksumException.getChecksumInstance();
               }

               var12.deleteCharAt(var11);
            }

            if(var12.length() == 0) {
               throw NotFoundException.getNotFoundInstance();
            }

            String var14;
            if(this.extendedMode) {
               var14 = decodeExtended(var12);
            } else {
               var14 = var12.toString();
            }

            float var6 = (float)(var15[1] + var15[0]) / 2.0F;
            float var5 = (float)(var7 + var8) / 2.0F;
            ResultPoint var16 = new ResultPoint(var6, (float)var1);
            ResultPoint var18 = new ResultPoint(var5, (float)var1);
            BarcodeFormat var17 = BarcodeFormat.CODE_39;
            return new Result(var14, (byte[])null, new ResultPoint[]{var16, var18}, var17);
         }

         var7 = var8;
      }
   }
}
