package com.google.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Hashtable;

public final class MultiFormatWriter implements Writer {
   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Hashtable)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Hashtable var5) throws WriterException {
      Object var6;
      if(var2 == BarcodeFormat.EAN_8) {
         var6 = new EAN8Writer();
      } else if(var2 == BarcodeFormat.EAN_13) {
         var6 = new EAN13Writer();
      } else if(var2 == BarcodeFormat.QR_CODE) {
         var6 = new QRCodeWriter();
      } else if(var2 == BarcodeFormat.CODE_39) {
         var6 = new Code39Writer();
      } else if(var2 == BarcodeFormat.CODE_128) {
         var6 = new Code128Writer();
      } else {
         if(var2 != BarcodeFormat.ITF) {
            throw new IllegalArgumentException("No encoder available for format " + var2);
         }

         var6 = new ITFWriter();
      }

      return ((Writer)var6).encode(var1, var2, var3, var4, var5);
   }
}
