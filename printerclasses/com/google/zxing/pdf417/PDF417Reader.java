package com.google.zxing.pdf417;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.pdf417.decoder.Decoder;
import com.google.zxing.pdf417.detector.Detector;
import com.google.zxing.qrcode.QRCodeReader;
import java.util.Hashtable;

public final class PDF417Reader implements Reader {
   private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
   private final Decoder decoder = new Decoder();

   public Result decode(BinaryBitmap var1) throws NotFoundException, FormatException {
      return this.decode(var1, (Hashtable)null);
   }

   public Result decode(BinaryBitmap var1, Hashtable var2) throws NotFoundException, FormatException {
      DecoderResult var3;
      ResultPoint[] var6;
      if(var2 != null && var2.containsKey(DecodeHintType.PURE_BARCODE)) {
         BitMatrix var4 = QRCodeReader.extractPureBits(var1.getBlackMatrix());
         var3 = this.decoder.decode(var4);
         var6 = NO_POINTS;
      } else {
         DetectorResult var5 = (new Detector(var1)).detect();
         var3 = this.decoder.decode(var5.getBits());
         var6 = var5.getPoints();
      }

      return new Result(var3.getText(), var3.getRawBytes(), var6, BarcodeFormat.PDF417);
   }

   public void reset() {
   }
}
