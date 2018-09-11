package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.EmailDoCoMoResultParser;
import com.google.zxing.client.result.ResultParser;
import java.util.Hashtable;

final class EmailAddressResultParser extends ResultParser {
   public static EmailAddressParsedResult parse(Result var0) {
      String var3 = null;
      String var2 = null;
      String var5 = var0.getText();
      EmailAddressParsedResult var6;
      if(var5 == null) {
         var6 = var2;
      } else if(!var5.startsWith("mailto:") && !var5.startsWith("MAILTO:")) {
         var6 = var2;
         if(EmailDoCoMoResultParser.isBasicallyValidEmailAddress(var5)) {
            var6 = new EmailAddressParsedResult(var5, (String)null, (String)null, "mailto:" + var5);
         }
      } else {
         var2 = var5.substring(7);
         int var1 = var2.indexOf(63);
         String var7 = var2;
         if(var1 >= 0) {
            var7 = var2.substring(0, var1);
         }

         Hashtable var8 = parseNameValuePairs(var5);
         if(var8 != null) {
            if(var7.length() == 0) {
               var7 = (String)var8.get("to");
            }

            String var4 = (String)var8.get("subject");
            var3 = (String)var8.get("body");
            var2 = var7;
            var7 = var4;
         } else {
            var2 = var7;
            var7 = null;
         }

         var6 = new EmailAddressParsedResult(var2, var7, var3, var5);
      }

      return var6;
   }
}
