package com.google.zxing.datamatrix;

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
import com.google.zxing.datamatrix.decoder.Decoder;
import com.google.zxing.datamatrix.detector.Detector;
import java.util.Hashtable;

public final class DataMatrixReader implements Reader {
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
   private final Decoder decoder = new Decoder();

   private static BitMatrix extractPureBits(BitMatrix var0) throws NotFoundException {
      int var4 = var0.getHeight();
      int var5 = var0.getWidth();
      int var2 = Math.min(var4, var5);
      int[] var9 = var0.getTopLeftOnBit();
      if(var9 == null) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int var1 = var9[0];

         int var6;
         for(var6 = var9[1]; var1 < var2 && var6 < var2 && var0.get(var1, var6); ++var1) {
            ;
         }

         if(var1 == var2) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            int var3 = var1 - var9[0];

            for(var2 = var5 - 1; var2 >= 0 && !var0.get(var2, var6); --var2) {
               ;
            }

            if(var2 < 0) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               ++var2;
               if((var2 - var1) % var3 != 0) {
                  throw NotFoundException.getNotFoundInstance();
               } else {
                  int var7 = (var2 - var1) / var3 + 2;
                  int var8 = var1 - (var3 >> 1);
                  var6 = var6 + var3 - (var3 >> 1);
                  if((var7 - 1) * var3 + var8 < var5 && (var7 - 1) * var3 + var6 < var4) {
                     BitMatrix var10 = new BitMatrix(var7);

                     for(var1 = 0; var1 < var7; ++var1) {
                        for(var2 = 0; var2 < var7; ++var2) {
                           if(var0.get(var2 * var3 + var8, var6 + var1 * var3)) {
                              var10.set(var2, var1);
                           }
                        }
                     }

                     return var10;
                  } else {
                     throw NotFoundException.getNotFoundInstance();
                  }
               }
            }
         }
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException {
      return this.decode(var1, (Hashtable)null);
   }

   public Result decode(BinaryBitmap var1, Hashtable var2) throws NotFoundException, ChecksumException, FormatException {
      DecoderResult var3;
      ResultPoint[] var6;
      if(var2 != null && var2.containsKey(DecodeHintType.PURE_BARCODE)) {
         BitMatrix var4 = extractPureBits(var1.getBlackMatrix());
         var3 = this.decoder.decode(var4);
         var6 = NO_POINTS;
      } else {
         DetectorResult var5 = (new Detector(var1.getBlackMatrix())).detect();
         var3 = this.decoder.decode(var5.getBits());
         var6 = var5.getPoints();
      }

      Result var7 = new Result(var3.getText(), var3.getRawBytes(), var6, BarcodeFormat.DATA_MATRIX);
      if(var3.getByteSegments() != null) {
         var7.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var3.getByteSegments());
      }

      if(var3.getECLevel() != null) {
         var7.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var3.getECLevel().toString());
      }

      return var7;
   }

   public void reset() {
   }
}
