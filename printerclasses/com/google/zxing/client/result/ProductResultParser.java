package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.oned.UPCEReader;

final class ProductResultParser extends ResultParser {
   public static ProductParsedResult parse(Result var0) {
      Object var5 = null;
      BarcodeFormat var6 = var0.getBarcodeFormat();
      ProductParsedResult var7;
      if(!BarcodeFormat.UPC_A.equals(var6) && !BarcodeFormat.UPC_E.equals(var6) && !BarcodeFormat.EAN_8.equals(var6) && !BarcodeFormat.EAN_13.equals(var6)) {
         var7 = (ProductParsedResult)var5;
      } else {
         String var4 = var0.getText();
         var7 = (ProductParsedResult)var5;
         if(var4 != null) {
            int var2 = var4.length();
            int var1 = 0;

            while(true) {
               if(var1 >= var2) {
                  String var8;
                  if(BarcodeFormat.UPC_E.equals(var6)) {
                     var8 = UPCEReader.convertUPCEtoUPCA(var4);
                  } else {
                     var8 = var4;
                  }

                  var7 = new ProductParsedResult(var4, var8);
                  break;
               }

               char var3 = var4.charAt(var1);
               var7 = (ProductParsedResult)var5;
               if(var3 < 48) {
                  break;
               }

               var7 = (ProductParsedResult)var5;
               if(var3 > 57) {
                  break;
               }

               ++var1;
            }
         }
      }

      return var7;
   }
}
