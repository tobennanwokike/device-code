package com.google.zxing.datamatrix.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GF256;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import com.google.zxing.datamatrix.decoder.BitMatrixParser;
import com.google.zxing.datamatrix.decoder.DataBlock;
import com.google.zxing.datamatrix.decoder.DecodedBitStreamParser;
import com.google.zxing.datamatrix.decoder.Version;

public final class Decoder {
   private final ReedSolomonDecoder rsDecoder;

   public Decoder() {
      this.rsDecoder = new ReedSolomonDecoder(GF256.DATA_MATRIX_FIELD);
   }

   private void correctErrors(byte[] var1, int var2) throws ChecksumException {
      byte var4 = 0;
      int var5 = var1.length;
      int[] var6 = new int[var5];

      int var3;
      for(var3 = 0; var3 < var5; ++var3) {
         var6[var3] = var1[var3] & 255;
      }

      var3 = var1.length;

      try {
         this.rsDecoder.decode(var6, var3 - var2);
      } catch (ReedSolomonException var7) {
         throw ChecksumException.getChecksumInstance();
      }

      for(var3 = var4; var3 < var2; ++var3) {
         var1[var3] = (byte)var6[var3];
      }

   }

   public DecoderResult decode(BitMatrix var1) throws FormatException, ChecksumException {
      BitMatrixParser var6 = new BitMatrixParser(var1);
      Version var9 = var6.readVersion(var1);
      DataBlock[] var8 = DataBlock.getDataBlocks(var6.readCodewords(), var9);
      int var3 = 0;

      int var2;
      for(var2 = 0; var3 < var8.length; ++var3) {
         var2 += var8[var3].getNumDataCodewords();
      }

      byte[] var10 = new byte[var2];
      var2 = 0;

      for(var3 = 0; var2 < var8.length; ++var2) {
         DataBlock var11 = var8[var2];
         byte[] var7 = var11.getCodewords();
         int var5 = var11.getNumDataCodewords();
         this.correctErrors(var7, var5);

         for(int var4 = 0; var4 < var5; ++var3) {
            var10[var3] = var7[var4];
            ++var4;
         }
      }

      return DecodedBitStreamParser.decode(var10);
   }

   public DecoderResult decode(boolean[][] var1) throws FormatException, ChecksumException {
      int var4 = var1.length;
      BitMatrix var5 = new BitMatrix(var4);

      for(int var2 = 0; var2 < var4; ++var2) {
         for(int var3 = 0; var3 < var4; ++var3) {
            if(var1[var2][var3]) {
               var5.set(var3, var2);
            }
         }
      }

      return this.decode(var5);
   }
}
