package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.TelParsedResult;

final class TelResultParser extends ResultParser {
   public static TelParsedResult parse(Result var0) {
      String var2 = var0.getText();
      TelParsedResult var4;
      if(var2 == null || !var2.startsWith("tel:") && !var2.startsWith("TEL:")) {
         var4 = null;
      } else {
         String var3;
         if(var2.startsWith("TEL:")) {
            var3 = "tel:" + var2.substring(4);
         } else {
            var3 = var2;
         }

         int var1 = var2.indexOf(63, 4);
         if(var1 < 0) {
            var2 = var2.substring(4);
         } else {
            var2 = var2.substring(4, var1);
         }

         var4 = new TelParsedResult(var2, var3, (String)null);
      }

      return var4;
   }
}
