package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.UPCEANWriter;
import java.util.Hashtable;

public final class Code39Writer extends UPCEANWriter {
   private static void toIntArray(int var0, int[] var1) {
      for(int var2 = 0; var2 < 9; ++var2) {
         byte var3;
         if((1 << var2 & var0) == 0) {
            var3 = 1;
         } else {
            var3 = 2;
         }

         var1[var2] = var3;
      }

   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      if(var2 != BarcodeFormat.CODE_39) {
         throw new IllegalArgumentException("Can only encode CODE_39, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public byte[] encode(String var1) {
      int var5 = var1.length();
      if(var5 > 80) {
         throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + var5);
      } else {
         int[] var6 = new int[9];
         int var2 = var5 + 25;

         int var3;
         int var4;
         for(var3 = 0; var3 < var5; ++var3) {
            var4 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(var1.charAt(var3));
            toIntArray(Code39Reader.CHARACTER_ENCODINGS[var4], var6);

            for(var4 = 0; var4 < var6.length; ++var4) {
               var2 += var6[var4];
            }
         }

         byte[] var7 = new byte[var2];
         toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], var6);
         var2 = appendPattern(var7, 0, var6, 1);
         int[] var8 = new int[]{1};
         var3 = appendPattern(var7, var2, var8, 0) + var2;

         for(var2 = var5 - 1; var2 >= 0; --var2) {
            var4 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(var1.charAt(var2));
            toIntArray(Code39Reader.CHARACTER_ENCODINGS[var4], var6);
            var3 += appendPattern(var7, var3, var6, 1);
            var3 += appendPattern(var7, var3, var8, 0);
         }

         toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], var6);
         appendPattern(var7, var3, var6, 1);
         return var7;
      }
   }
}
