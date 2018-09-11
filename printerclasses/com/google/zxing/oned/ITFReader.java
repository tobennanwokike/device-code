package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.OneDReader;
import java.util.Hashtable;

public final class ITFReader extends OneDReader {
   private static final int[] DEFAULT_ALLOWED_LENGTHS = new int[]{6, 10, 12, 14, 44};
   private static final int[] END_PATTERN_REVERSED = new int[]{1, 1, 3};
   private static final int MAX_AVG_VARIANCE = 107;
   private static final int MAX_INDIVIDUAL_VARIANCE = 204;
   private static final int N = 1;
   static final int[][] PATTERNS = new int[][]{{1, 1, 3, 3, 1}, {3, 1, 1, 1, 3}, {1, 3, 1, 1, 3}, {3, 3, 1, 1, 1}, {1, 1, 3, 1, 3}, {3, 1, 3, 1, 1}, {1, 3, 3, 1, 1}, {1, 1, 1, 3, 3}, {3, 1, 1, 3, 1}, {1, 3, 1, 3, 1}};
   private static final int[] START_PATTERN = new int[]{1, 1, 1, 1};
   private static final int W = 3;
   private int narrowLineWidth = -1;

   private static int decodeDigit(int[] var0) throws NotFoundException {
      int var2 = 107;
      int var3 = -1;
      int var5 = PATTERNS.length;

      for(int var1 = 0; var1 < var5; ++var1) {
         int var4 = patternMatchVariance(var0, PATTERNS[var1], 204);
         if(var4 < var2) {
            var3 = var1;
            var2 = var4;
         }
      }

      if(var3 >= 0) {
         return var3;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static void decodeMiddle(BitArray var0, int var1, int var2, StringBuffer var3) throws NotFoundException {
      int[] var8 = new int[10];
      int[] var6 = new int[5];
      int[] var7 = new int[5];

      while(var1 < var2) {
         recordPattern(var0, var1, var8);

         int var4;
         for(var4 = 0; var4 < 5; ++var4) {
            int var5 = var4 << 1;
            var6[var4] = var8[var5];
            var7[var4] = var8[var5 + 1];
         }

         var3.append((char)(decodeDigit(var6) + 48));
         var3.append((char)(decodeDigit(var7) + 48));

         for(var4 = 0; var4 < var8.length; ++var4) {
            var1 += var8[var4];
         }
      }

   }

   private static int[] findGuardPattern(BitArray var0, int var1, int[] var2) throws NotFoundException {
      int var8 = var2.length;
      int[] var10 = new int[var8];
      int var9 = var0.getSize();
      int var6 = 0;
      boolean var5 = false;
      int var4 = var1;

      boolean var11;
      for(var1 = var1; var4 < var9; var5 = var11) {
         int var12;
         if(var0.get(var4) ^ var5) {
            ++var10[var6];
            var11 = var5;
            var12 = var1;
         } else {
            int var3;
            if(var6 == var8 - 1) {
               if(patternMatchVariance(var10, var2, 204) < 107) {
                  return new int[]{var1, var4};
               }

               var3 = var1 + var10[0] + var10[1];

               for(var1 = 2; var1 < var8; ++var1) {
                  var10[var1 - 2] = var10[var1];
               }

               var10[var8 - 2] = 0;
               var10[var8 - 1] = 0;
               var1 = var6 - 1;
            } else {
               ++var6;
               var3 = var1;
               var1 = var6;
            }

            var10[var1] = 1;
            boolean var7;
            if(!var5) {
               var7 = true;
               var12 = var3;
               var6 = var1;
               var11 = var7;
            } else {
               var7 = false;
               var12 = var3;
               var6 = var1;
               var11 = var7;
            }
         }

         ++var4;
         var1 = var12;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int skipWhiteSpace(BitArray var0) throws NotFoundException {
      int var2 = var0.getSize();

      int var1;
      for(var1 = 0; var1 < var2 && !var0.get(var1); ++var1) {
         ;
      }

      if(var1 == var2) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return var1;
      }
   }

   private void validateQuietZone(BitArray var1, int var2) throws NotFoundException {
      int var3 = this.narrowLineWidth * 10;
      int var4 = var2 - 1;
      var2 = var3;

      for(var3 = var4; var2 > 0 && var3 >= 0 && !var1.get(var3); --var3) {
         --var2;
      }

      if(var2 != 0) {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   int[] decodeEnd(BitArray param1) throws NotFoundException {
      // $FF: Couldn't be decompiled
   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws FormatException, NotFoundException {
      int[] var8 = this.decodeStart(var2);
      int[] var7 = this.decodeEnd(var2);
      StringBuffer var6 = new StringBuffer(20);
      decodeMiddle(var2, var8[1], var7[0], var6);
      String var14 = var6.toString();
      int[] var9;
      if(var3 != null) {
         var9 = (int[])((int[])var3.get(DecodeHintType.ALLOWED_LENGTHS));
      } else {
         var9 = null;
      }

      int[] var10 = var9;
      if(var9 == null) {
         var10 = DEFAULT_ALLOWED_LENGTHS;
      }

      int var5 = var14.length();
      int var4 = 0;

      boolean var13;
      while(true) {
         if(var4 >= var10.length) {
            var13 = false;
            break;
         }

         if(var5 == var10[var4]) {
            var13 = true;
            break;
         }

         ++var4;
      }

      if(!var13) {
         throw FormatException.getFormatInstance();
      } else {
         ResultPoint var11 = new ResultPoint((float)var8[1], (float)var1);
         ResultPoint var15 = new ResultPoint((float)var7[0], (float)var1);
         BarcodeFormat var12 = BarcodeFormat.ITF;
         return new Result(var14, (byte[])null, new ResultPoint[]{var11, var15}, var12);
      }
   }

   int[] decodeStart(BitArray var1) throws NotFoundException {
      int[] var2 = findGuardPattern(var1, skipWhiteSpace(var1), START_PATTERN);
      this.narrowLineWidth = var2[1] - var2[0] >> 2;
      this.validateQuietZone(var1, var2[0]);
      return var2;
   }
}
