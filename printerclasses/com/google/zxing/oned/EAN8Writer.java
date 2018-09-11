package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.UPCEANReader;
import com.google.zxing.oned.UPCEANWriter;
import java.util.Hashtable;

public final class EAN8Writer extends UPCEANWriter {
   private static final int codeWidth = 67;

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      if(var2 != BarcodeFormat.EAN_8) {
         throw new IllegalArgumentException("Can only encode EAN_8, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public byte[] encode(String var1) {
      if(var1.length() != 8) {
         throw new IllegalArgumentException("Requested contents should be 8 digits long, but got " + var1.length());
      } else {
         byte[] var5 = new byte[67];
         int var3 = appendPattern(var5, 0, UPCEANReader.START_END_PATTERN, 1) + 0;

         int var2;
         int var4;
         for(var2 = 0; var2 <= 3; ++var2) {
            var4 = Integer.parseInt(var1.substring(var2, var2 + 1));
            var3 += appendPattern(var5, var3, UPCEANReader.L_PATTERNS[var4], 0);
         }

         var2 = var3 + appendPattern(var5, var3, UPCEANReader.MIDDLE_PATTERN, 0);

         for(var3 = 4; var3 <= 7; ++var3) {
            var4 = Integer.parseInt(var1.substring(var3, var3 + 1));
            var2 += appendPattern(var5, var2, UPCEANReader.L_PATTERNS[var4], 1);
         }

         appendPattern(var5, var2, UPCEANReader.START_END_PATTERN, 1);
         return var5;
      }
   }
}
