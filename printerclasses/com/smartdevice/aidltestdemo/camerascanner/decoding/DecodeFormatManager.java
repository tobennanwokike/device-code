package com.smartdevice.aidltestdemo.camerascanner.decoding;

import android.content.Intent;
import android.net.Uri;
import com.google.zxing.BarcodeFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

final class DecodeFormatManager {
   private static final Pattern COMMA_PATTERN = Pattern.compile(",");
   static final Vector DATA_MATRIX_FORMATS;
   static final Vector ONE_D_FORMATS;
   static final Vector PRODUCT_FORMATS = new Vector(5);
   static final Vector QR_CODE_FORMATS;

   static {
      PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
      PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
      PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
      PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
      PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
      ONE_D_FORMATS = new Vector(PRODUCT_FORMATS.size() + 4);
      ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
      ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
      ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
      ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
      ONE_D_FORMATS.add(BarcodeFormat.ITF);
      QR_CODE_FORMATS = new Vector(1);
      QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
      DATA_MATRIX_FORMATS = new Vector(1);
      DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);
   }

   static Vector parseDecodeFormats(Intent var0) {
      List var1 = null;
      String var2 = var0.getStringExtra("SCAN_FORMATS");
      if(var2 != null) {
         var1 = Arrays.asList(COMMA_PATTERN.split(var2));
      }

      return parseDecodeFormats(var1, var0.getStringExtra("SCAN_MODE"));
   }

   static Vector parseDecodeFormats(Uri var0) {
      List var2 = var0.getQueryParameters("SCAN_FORMATS");
      List var1 = var2;
      if(var2 != null) {
         var1 = var2;
         if(var2.size() == 1) {
            var1 = var2;
            if(var2.get(0) != null) {
               var1 = Arrays.asList(COMMA_PATTERN.split((CharSequence)var2.get(0)));
            }
         }
      }

      return parseDecodeFormats(var1, var0.getQueryParameter("SCAN_MODE"));
   }

   private static Vector parseDecodeFormats(Iterable param0, String param1) {
      // $FF: Couldn't be decompiled
   }
}
