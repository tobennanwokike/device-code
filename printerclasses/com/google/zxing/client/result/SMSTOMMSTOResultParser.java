package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSParsedResult;

final class SMSTOMMSTOResultParser extends ResultParser {
   public static SMSParsedResult parse(Result var0) {
      String var2 = null;
      String var3 = var0.getText();
      SMSParsedResult var4;
      if(var3 == null) {
         var4 = var2;
      } else {
         if(!var3.startsWith("smsto:") && !var3.startsWith("SMSTO:") && !var3.startsWith("mmsto:")) {
            var4 = var2;
            if(!var3.startsWith("MMSTO:")) {
               return var4;
            }
         }

         String var5 = var3.substring(6);
         int var1 = var5.indexOf(58);
         if(var1 >= 0) {
            var2 = var5.substring(var1 + 1);
            var5 = var5.substring(0, var1);
         } else {
            var2 = null;
         }

         var4 = new SMSParsedResult(var5, (String)null, (String)null, var2);
      }

      return var4;
   }
}
