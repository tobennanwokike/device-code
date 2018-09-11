package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.OneDReader;
import java.util.Hashtable;

public final class CodaBarReader extends OneDReader {
   private static final char[] ALPHABET = "0123456789-$:/.+ABCDTN".toCharArray();
   private static final String ALPHABET_STRING = "0123456789-$:/.+ABCDTN";
   private static final int[] CHARACTER_ENCODINGS = new int[]{3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 37, 81, 84, 21, 26, 41, 11, 14, 26, 41};
   private static final char[] STARTEND_ENCODING = new char[]{'E', '*', 'A', 'B', 'C', 'D', 'T', 'N'};
   private static final int minCharacterLength = 6;

   private static boolean arrayContains(char[] var0, char var1) {
      boolean var4 = false;
      boolean var3 = var4;
      if(var0 != null) {
         int var2 = 0;

         while(true) {
            var3 = var4;
            if(var2 >= var0.length) {
               break;
            }

            if(var0[var2] == var1) {
               var3 = true;
               break;
            }

            ++var2;
         }
      }

      return var3;
   }

   private static int[] findAsteriskPattern(BitArray var0) throws NotFoundException {
      int var6 = var0.getSize();

      int var1;
      for(var1 = 0; var1 < var6 && !var0.get(var1); ++var1) {
         ;
      }

      int[] var9 = new int[7];
      int var7 = var9.length;
      int var4 = var1;
      boolean var2 = false;

      int var5;
      for(int var3 = 0; var4 < var6; var4 = var5) {
         boolean var11;
         int var12;
         if(var0.get(var4) ^ var2) {
            ++var9[var3];
            var5 = var3;
            var3 = var1;
            var11 = var2;
            var12 = var5;
         } else {
            if(var3 != var7 - 1) {
               ++var3;
            } else {
               try {
                  if(arrayContains(STARTEND_ENCODING, toNarrowWidePattern(var9)) && var0.isRange(Math.max(0, var1 - (var4 - var1) / 2), var1, false)) {
                     return new int[]{var1, var4};
                  }
               } catch (IllegalArgumentException var10) {
                  ;
               }

               var5 = var1 + var9[0] + var9[1];

               for(var1 = 2; var1 < var7; ++var1) {
                  var9[var1 - 2] = var9[var1];
               }

               var9[var7 - 2] = 0;
               var9[var7 - 1] = 0;
               --var3;
               var1 = var5;
            }

            var9[var3] = 1;
            var5 = var3;
            var3 = var1;
            var11 = var2 ^ true;
            var12 = var5;
         }

         var5 = var4 + 1;
         var2 = var11;
         var1 = var3;
         var3 = var12;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static char toNarrowWidePattern(int[] var0) {
      int var9 = var0.length;
      int var3 = Integer.MAX_VALUE;
      int var5 = 0;
      int var2 = 0;

      while(true) {
         int var4 = var2;
         int var6;
         if(var5 >= var9) {
            char var1;
            do {
               var5 = 0;
               var2 = 0;

               int var7;
               for(var6 = 0; var5 < var9; var6 = var7) {
                  int var8 = var2;
                  var7 = var6;
                  if(var0[var5] > var4) {
                     var8 = var2 | 1 << var9 - 1 - var5;
                     var7 = var6 + 1;
                  }

                  ++var5;
                  var2 = var8;
               }

               if(var6 == 2 || var6 == 3) {
                  for(var5 = 0; var5 < CHARACTER_ENCODINGS.length; ++var5) {
                     if(CHARACTER_ENCODINGS[var5] == var2) {
                        var1 = ALPHABET[var5];
                        return var1;
                     }
                  }
               }

               var2 = var4 - 1;
               var4 = var2;
            } while(var2 > var3);

            var1 = 33;
            return var1;
         }

         var4 = var3;
         if(var0[var5] < var3) {
            var4 = var0[var5];
         }

         var6 = var2;
         if(var0[var5] > var2) {
            var6 = var0[var5];
         }

         ++var5;
         var3 = var4;
         var2 = var6;
      }
   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException {
      int[] var13 = findAsteriskPattern(var2);
      var13[1] = 0;
      int var7 = var13[1];

      int var11;
      for(var11 = var2.getSize(); var7 < var11 && !var2.get(var7); ++var7) {
         ;
      }

      StringBuffer var12 = new StringBuffer();

      while(true) {
         int[] var15 = new int[]{0, 0, 0, 0, 0, 0, 0};
         recordPattern(var2, var7, var15);
         char var4 = toNarrowWidePattern(var15);
         if(var4 == 33) {
            throw NotFoundException.getNotFoundInstance();
         }

         var12.append(var4);
         int var9 = 0;

         int var8;
         for(var8 = var7; var9 < var15.length; ++var9) {
            var8 += var15[var9];
         }

         while(var8 < var11 && !var2.get(var8)) {
            ++var8;
         }

         if(var8 >= var11) {
            var9 = 0;

            int var10;
            for(var10 = 0; var10 < var15.length; ++var10) {
               var9 += var15[var10];
            }

            if(var8 != var11 && (var8 - var7 - var9) / 2 < var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            if(var12.length() < 2) {
               throw NotFoundException.getNotFoundInstance();
            }

            var4 = var12.charAt(0);
            if(!arrayContains(STARTEND_ENCODING, var4)) {
               throw NotFoundException.getNotFoundInstance();
            }

            for(var9 = 1; var9 < var12.length(); var9 = var10 + 1) {
               var10 = var9;
               if(var12.charAt(var9) == var4) {
                  var10 = var9;
                  if(var9 + 1 != var12.length()) {
                     var12.delete(var9 + 1, var12.length() - 1);
                     var10 = var12.length();
                  }
               }
            }

            if(var12.length() > 6) {
               var12.deleteCharAt(var12.length() - 1);
               var12.deleteCharAt(0);
               float var5 = (float)(var13[1] + var13[0]) / 2.0F;
               float var6 = (float)(var7 + var8) / 2.0F;
               String var18 = var12.toString();
               ResultPoint var17 = new ResultPoint(var5, (float)var1);
               ResultPoint var16 = new ResultPoint(var6, (float)var1);
               BarcodeFormat var14 = BarcodeFormat.CODABAR;
               return new Result(var18, (byte[])null, new ResultPoint[]{var17, var16}, var14);
            }

            throw NotFoundException.getNotFoundInstance();
         }

         var7 = var8;
      }
   }
}
