package com.google.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import java.util.Hashtable;

public final class QRCodeWriter implements Writer {
   private static final int QUIET_ZONE_SIZE = 4;

   private static BitMatrix renderResult(QRCode var0, int var1, int var2) {
      ByteMatrix var9 = var0.getMatrix();
      int var6 = var9.getWidth();
      int var7 = var9.getHeight();
      int var5 = var6 + 8;
      int var4 = var7 + 8;
      var1 = Math.max(var1, var5);
      int var3 = Math.max(var2, var4);
      int var8 = Math.min(var1 / var5, var3 / var4);
      var5 = (var1 - var6 * var8) / 2;
      var2 = (var3 - var7 * var8) / 2;
      BitMatrix var10 = new BitMatrix(var1, var3);

      for(var1 = 0; var1 < var7; ++var1) {
         var4 = var5;

         for(var3 = 0; var3 < var6; var4 += var8) {
            if(var9.get(var3, var1) == 1) {
               var10.setRegion(var4, var2, var8, var8);
            }

            ++var3;
         }

         var2 += var8;
      }

      return var10;
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Hashtable)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      if(var1 != null && var1.length() != 0) {
         if(var2 != BarcodeFormat.QR_CODE) {
            throw new IllegalArgumentException("Can only encode QR_CODE, but got " + var2);
         } else if(var3 >= 0 && var4 >= 0) {
            ErrorCorrectionLevel var7 = ErrorCorrectionLevel.L;
            if(var5 != null) {
               ErrorCorrectionLevel var6 = (ErrorCorrectionLevel)var5.get(EncodeHintType.ERROR_CORRECTION);
               if(var6 != null) {
                  var7 = var6;
               }
            }

            QRCode var8 = new QRCode();
            Encoder.encode(var1, var7, var5, var8);
            return renderResult(var8, var3, var4);
         } else {
            throw new IllegalArgumentException("Requested dimensions are too small: " + var3 + 'x' + var4);
         }
      } else {
         throw new IllegalArgumentException("Found empty contents");
      }
   }
}
