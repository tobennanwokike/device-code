package com.google.zxing.qrcode.decoder;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GF256;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import com.google.zxing.qrcode.decoder.BitMatrixParser;
import com.google.zxing.qrcode.decoder.DataBlock;
import com.google.zxing.qrcode.decoder.DecodedBitStreamParser;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Version;
import java.util.Hashtable;

public final class Decoder {
   private final ReedSolomonDecoder rsDecoder;

   public Decoder() {
      this.rsDecoder = new ReedSolomonDecoder(GF256.QR_CODE_FIELD);
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

   public DecoderResult decode(BitMatrix var1) throws ChecksumException, FormatException, NotFoundException {
      return this.decode((BitMatrix)var1, (Hashtable)null);
   }

   public DecoderResult decode(BitMatrix var1, Hashtable var2) throws FormatException, ChecksumException {
      BitMatrixParser var8 = new BitMatrixParser(var1);
      Version var7 = var8.readVersion();
      ErrorCorrectionLevel var12 = var8.readFormatInformation().getErrorCorrectionLevel();
      DataBlock[] var13 = DataBlock.getDataBlocks(var8.readCodewords(), var7, var12);
      int var3 = 0;

      int var4;
      for(var4 = 0; var3 < var13.length; ++var3) {
         var4 += var13[var3].getNumDataCodewords();
      }

      byte[] var10 = new byte[var4];
      var3 = 0;

      for(var4 = 0; var3 < var13.length; ++var3) {
         DataBlock var11 = var13[var3];
         byte[] var9 = var11.getCodewords();
         int var6 = var11.getNumDataCodewords();
         this.correctErrors(var9, var6);

         for(int var5 = 0; var5 < var6; ++var4) {
            var10[var4] = var9[var5];
            ++var5;
         }
      }

      return DecodedBitStreamParser.decode(var10, var7, var12, var2);
   }

   public DecoderResult decode(boolean[][] var1) throws ChecksumException, FormatException, NotFoundException {
      return this.decode((boolean[][])var1, (Hashtable)null);
   }

   public DecoderResult decode(boolean[][] var1, Hashtable var2) throws ChecksumException, FormatException, NotFoundException {
      int var5 = var1.length;
      BitMatrix var6 = new BitMatrix(var5);

      for(int var3 = 0; var3 < var5; ++var3) {
         for(int var4 = 0; var4 < var5; ++var4) {
            if(var1[var3][var4]) {
               var6.set(var4, var3);
            }
         }
      }

      return this.decode(var6, var2);
   }
}
