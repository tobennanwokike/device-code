package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Reader;
import com.google.zxing.oned.UPCEANWriter;
import java.util.Hashtable;

public final class Code128Writer extends UPCEANWriter {
   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      if(var2 != BarcodeFormat.CODE_128) {
         throw new IllegalArgumentException("Can only encode CODE_128, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public byte[] encode(String var1) {
      byte var5 = 0;
      int var6 = var1.length();
      if(var6 > 80) {
         throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + var6);
      } else {
         int var2 = 35;

         int var3;
         int var4;
         for(var3 = 0; var3 < var6; ++var3) {
            int[] var7 = Code128Reader.CODE_PATTERNS[var1.charAt(var3) - 32];

            for(var4 = 0; var4 < var7.length; ++var4) {
               var2 += var7[var4];
            }
         }

         byte[] var8 = new byte[var2];
         var3 = appendPattern(var8, 0, Code128Reader.CODE_PATTERNS[104], 1);
         var4 = 104;

         for(var2 = var5; var2 < var6; ++var2) {
            var4 += (var1.charAt(var2) - 32) * (var2 + 1);
            var3 += appendPattern(var8, var3, Code128Reader.CODE_PATTERNS[var1.charAt(var2) - 32], 1);
         }

         var2 = appendPattern(var8, var3, Code128Reader.CODE_PATTERNS[var4 % 103], 1) + var3;
         appendPattern(var8, var2, Code128Reader.CODE_PATTERNS[106], 1);
         return var8;
      }
   }
}
