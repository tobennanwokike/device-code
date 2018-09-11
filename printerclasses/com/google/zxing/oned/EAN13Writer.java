package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Reader;
import com.google.zxing.oned.UPCEANReader;
import com.google.zxing.oned.UPCEANWriter;
import java.util.Hashtable;

public final class EAN13Writer extends UPCEANWriter {
   private static final int codeWidth = 95;

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      if(var2 != BarcodeFormat.EAN_13) {
         throw new IllegalArgumentException("Can only encode EAN_13, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public byte[] encode(String var1) {
      if(var1.length() != 13) {
         throw new IllegalArgumentException("Requested contents should be 13 digits long, but got " + var1.length());
      } else {
         int var2 = Integer.parseInt(var1.substring(0, 1));
         int var6 = EAN13Reader.FIRST_DIGIT_ENCODINGS[var2];
         byte[] var7 = new byte[95];
         var2 = appendPattern(var7, 0, UPCEANReader.START_END_PATTERN, 1);
         int var3 = 1;

         int var4;
         for(var2 += 0; var3 <= 6; ++var3) {
            int var5 = Integer.parseInt(var1.substring(var3, var3 + 1));
            var4 = var5;
            if((var6 >> 6 - var3 & 1) == 1) {
               var4 = var5 + 10;
            }

            var2 += appendPattern(var7, var2, UPCEANReader.L_AND_G_PATTERNS[var4], 0);
         }

         var2 += appendPattern(var7, var2, UPCEANReader.MIDDLE_PATTERN, 0);

         for(var3 = 7; var3 <= 12; ++var3) {
            var4 = Integer.parseInt(var1.substring(var3, var3 + 1));
            var2 += appendPattern(var7, var2, UPCEANReader.L_PATTERNS[var4], 1);
         }

         appendPattern(var7, var2, UPCEANReader.START_END_PATTERN, 1);
         return var7;
      }
   }
}
