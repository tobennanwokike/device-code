package com.google.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.detector.Detector;
import java.util.Hashtable;

public class QRCodeReader implements Reader {
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
   private final Decoder decoder = new Decoder();

   public static BitMatrix extractPureBits(BitMatrix var0) throws NotFoundException {
      byte var4 = 1;
      int var5 = var0.getHeight();
      int var6 = var0.getWidth();
      int var3 = Math.min(var5, var6);
      int[] var9 = var0.getTopLeftOnBit();
      if(var9 == null) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int var2 = var9[0];

         int var1;
         for(var1 = var9[1]; var2 < var3 && var1 < var3 && var0.get(var2, var1); ++var1) {
            ++var2;
         }

         if(var2 != var3 && var1 != var3) {
            int var7 = var2 - var9[0];
            if(var7 == 0) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               for(var3 = var6 - 1; var3 > var2 && !var0.get(var3, var1); --var3) {
                  ;
               }

               if(var3 <= var2) {
                  throw NotFoundException.getNotFoundInstance();
               } else {
                  ++var3;
                  if((var3 - var2) % var7 != 0) {
                     throw NotFoundException.getNotFoundInstance();
                  } else {
                     int var8 = (var3 - var2) / var7 + 1;
                     if(var7 == 1) {
                        var3 = var4;
                     } else {
                        var3 = var7 >> 1;
                     }

                     int var10 = var2 - var3;
                     var3 = var1 - var3;
                     if((var8 - 1) * var7 + var10 < var6 && (var8 - 1) * var7 + var3 < var5) {
                        BitMatrix var11 = new BitMatrix(var8);

                        for(var1 = 0; var1 < var8; ++var1) {
                           for(var2 = 0; var2 < var8; ++var2) {
                              if(var0.get(var2 * var7 + var10, var3 + var1 * var7)) {
                                 var11.set(var2, var1);
                              }
                           }
                        }

                        return var11;
                     } else {
                        throw NotFoundException.getNotFoundInstance();
                     }
                  }
               }
            }
         } else {
            throw NotFoundException.getNotFoundInstance();
         }
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException {
      return this.decode(var1, (Hashtable)null);
   }

   public Result decode(BinaryBitmap var1, Hashtable var2) throws NotFoundException, ChecksumException, FormatException {
      DecoderResult var4;
      ResultPoint[] var6;
      if(var2 != null && var2.containsKey(DecodeHintType.PURE_BARCODE)) {
         BitMatrix var5 = extractPureBits(var1.getBlackMatrix());
         var4 = this.decoder.decode(var5, var2);
         var6 = NO_POINTS;
      } else {
         DetectorResult var3 = (new Detector(var1.getBlackMatrix())).detect(var2);
         var4 = this.decoder.decode(var3.getBits(), var2);
         var6 = var3.getPoints();
      }

      Result var7 = new Result(var4.getText(), var4.getRawBytes(), var6, BarcodeFormat.QR_CODE);
      if(var4.getByteSegments() != null) {
         var7.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var4.getByteSegments());
      }

      if(var4.getECLevel() != null) {
         var7.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var4.getECLevel().toString());
      }

      return var7;
   }

   protected Decoder getDecoder() {
      return this.decoder;
   }

   public void reset() {
   }
}
