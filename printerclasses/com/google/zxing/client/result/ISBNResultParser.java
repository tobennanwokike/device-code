package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ResultParser;

public class ISBNResultParser extends ResultParser {
   public static ISBNParsedResult parse(Result var0) {
      Object var1 = null;
      BarcodeFormat var2 = var0.getBarcodeFormat();
      ISBNParsedResult var3;
      if(!BarcodeFormat.EAN_13.equals(var2)) {
         var3 = (ISBNParsedResult)var1;
      } else {
         String var4 = var0.getText();
         var3 = (ISBNParsedResult)var1;
         if(var4 != null) {
            var3 = (ISBNParsedResult)var1;
            if(var4.length() == 13) {
               if(!var4.startsWith("978")) {
                  var3 = (ISBNParsedResult)var1;
                  if(!var4.startsWith("979")) {
                     return var3;
                  }
               }

               var3 = new ISBNParsedResult(var4);
            }
         }
      }

      return var3;
   }
}
