package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.ITFReader;
import com.google.zxing.oned.UPCEANWriter;
import java.util.Hashtable;

public final class ITFWriter extends UPCEANWriter {
   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      if(var2 != BarcodeFormat.ITF) {
         throw new IllegalArgumentException("Can only encode ITF, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public byte[] encode(String var1) {
      int var5 = var1.length();
      if(var5 > 80) {
         throw new IllegalArgumentException("Requested contents should be less than 80 digits long, but got " + var5);
      } else {
         byte[] var8 = new byte[var5 * 9 + 9];
         int var2 = appendPattern(var8, 0, new int[]{1, 1, 1, 1}, 1);

         for(int var3 = 0; var3 < var5; var3 += 2) {
            int var7 = Character.digit(var1.charAt(var3), 10);
            int var6 = Character.digit(var1.charAt(var3 + 1), 10);
            int[] var9 = new int[18];

            for(int var4 = 0; var4 < 5; ++var4) {
               var9[var4 << 1] = ITFReader.PATTERNS[var7][var4];
               var9[(var4 << 1) + 1] = ITFReader.PATTERNS[var6][var4];
            }

            var2 += appendPattern(var8, var2, var9, 1);
         }

         appendPattern(var8, var2, new int[]{3, 1, 1}, 1);
         return var8;
      }
   }
}
