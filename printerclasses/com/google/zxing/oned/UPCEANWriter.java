package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.UPCEANReader;
import java.util.Hashtable;

public abstract class UPCEANWriter implements Writer {
   protected static int appendPattern(byte[] var0, int var1, int[] var2, int var3) {
      if(var3 != 0 && var3 != 1) {
         throw new IllegalArgumentException("startColor must be either 0 or 1, but got: " + var3);
      } else {
         byte var4 = (byte)var3;
         var3 = 0;
         byte var6 = 0;
         int var5 = var1;

         for(var1 = var6; var1 < var2.length; ++var1) {
            for(int var7 = 0; var7 < var2[var1]; ++var3) {
               var0[var5] = var4;
               ++var5;
               ++var7;
            }

            var4 = (byte)(var4 ^ 1);
         }

         return var3;
      }
   }

   private static BitMatrix renderResult(byte[] var0, int var1, int var2) {
      int var3 = var0.length;
      int var5 = (UPCEANReader.START_END_PATTERN.length << 1) + var3;
      int var6 = Math.max(var1, var5);
      int var4 = Math.max(1, var2);
      var5 = var6 / var5;
      var1 = (var6 - var3 * var5) / 2;
      BitMatrix var7 = new BitMatrix(var6, var4);

      for(var2 = 0; var2 < var3; var1 += var5) {
         if(var0[var2] == 1) {
            var7.setRegion(var1, 0, var5, var4);
         }

         ++var2;
      }

      return var7;
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Hashtable)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      if(var1 != null && var1.length() != 0) {
         if(var3 >= 0 && var4 >= 0) {
            return renderResult(this.encode(var1), var3, var4);
         } else {
            throw new IllegalArgumentException("Requested dimensions are too small: " + var3 + 'x' + var4);
         }
      } else {
         throw new IllegalArgumentException("Found empty contents");
      }
   }

   public abstract byte[] encode(String var1);
}
