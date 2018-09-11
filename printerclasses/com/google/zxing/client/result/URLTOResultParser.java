package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.URIParsedResult;

final class URLTOResultParser {
   public static URIParsedResult parse(Result var0) {
      Object var3 = null;
      Object var2 = null;
      String var4 = var0.getText();
      URIParsedResult var5 = (URIParsedResult)var2;
      if(var4 != null) {
         if(!var4.startsWith("urlto:") && !var4.startsWith("URLTO:")) {
            var5 = (URIParsedResult)var2;
         } else {
            int var1 = var4.indexOf(58, 6);
            var5 = (URIParsedResult)var2;
            if(var1 >= 0) {
               String var6;
               if(var1 <= 6) {
                  var6 = (String)var3;
               } else {
                  var6 = var4.substring(6, var1);
               }

               var5 = new URIParsedResult(var4.substring(var1 + 1), var6);
            }
         }
      }

      return var5;
   }
}
