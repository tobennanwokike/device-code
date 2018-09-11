package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.pdf417.decoder.BitMatrixParser;
import com.google.zxing.pdf417.decoder.DecodedBitStreamParser;

public final class Decoder {
   private static final int MAX_EC_CODEWORDS = 512;
   private static final int MAX_ERRORS = 3;

   private static int correctErrors(int[] var0, int[] var1, int var2) throws FormatException {
      if((var1 == null || var1.length <= var2 / 2 + 3) && var2 >= 0 && var2 <= 512) {
         if(var1 != null && var1.length > 3) {
            throw FormatException.getFormatInstance();
         } else {
            return 0;
         }
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   private static void verifyCodewordCount(int[] var0, int var1) throws FormatException {
      if(var0.length < 4) {
         throw FormatException.getFormatInstance();
      } else {
         int var2 = var0[0];
         if(var2 > var0.length) {
            throw FormatException.getFormatInstance();
         } else {
            if(var2 == 0) {
               if(var1 >= var0.length) {
                  throw FormatException.getFormatInstance();
               }

               var0[0] = var0.length - var1;
            }

         }
      }
   }

   public DecoderResult decode(BitMatrix var1) throws FormatException {
      BitMatrixParser var4 = new BitMatrixParser(var1);
      int[] var3 = var4.readCodewords();
      if(var3 != null && var3.length != 0) {
         int var2 = 1 << var4.getECLevel() + 1;
         correctErrors(var3, var4.getErasures(), var2);
         verifyCodewordCount(var3, var2);
         return DecodedBitStreamParser.decode(var3);
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   public DecoderResult decode(boolean[][] var1) throws FormatException {
      int var4 = var1.length;
      BitMatrix var5 = new BitMatrix(var4);

      for(int var2 = 0; var2 < var4; ++var2) {
         for(int var3 = 0; var3 < var4; ++var3) {
            if(var1[var3][var2]) {
               var5.set(var3, var2);
            }
         }
      }

      return this.decode(var5);
   }
}
