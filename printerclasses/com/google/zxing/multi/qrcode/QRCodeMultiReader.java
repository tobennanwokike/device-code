package com.google.zxing.multi.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.detector.MultiDetector;
import com.google.zxing.qrcode.QRCodeReader;
import java.util.Hashtable;
import java.util.Vector;

public final class QRCodeMultiReader extends QRCodeReader implements MultipleBarcodeReader {
   private static final Result[] EMPTY_RESULT_ARRAY = new Result[0];

   public Result[] decodeMultiple(BinaryBitmap var1) throws NotFoundException {
      return this.decodeMultiple(var1, (Hashtable)null);
   }

   public Result[] decodeMultiple(BinaryBitmap var1, Hashtable var2) throws NotFoundException {
      byte var4 = 0;
      Vector var5 = new Vector();
      DetectorResult[] var9 = (new MultiDetector(var1.getBlackMatrix())).detectMulti(var2);

      int var3;
      for(var3 = 0; var3 < var9.length; ++var3) {
         try {
            DecoderResult var11 = this.getDecoder().decode(var9[var3].getBits());
            ResultPoint[] var6 = var9[var3].getPoints();
            Result var7 = new Result(var11.getText(), var11.getRawBytes(), var6, BarcodeFormat.QR_CODE);
            if(var11.getByteSegments() != null) {
               var7.putMetadata(ResultMetadataType.BYTE_SEGMENTS, var11.getByteSegments());
            }

            if(var11.getECLevel() != null) {
               var7.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, var11.getECLevel().toString());
            }

            var5.addElement(var7);
         } catch (ReaderException var8) {
            ;
         }
      }

      Result[] var10;
      if(var5.isEmpty()) {
         var10 = EMPTY_RESULT_ARRAY;
      } else {
         var10 = new Result[var5.size()];

         for(var3 = var4; var3 < var5.size(); ++var3) {
            var10[var3] = (Result)var5.elementAt(var3);
         }
      }

      return var10;
   }
}
