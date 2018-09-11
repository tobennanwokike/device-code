package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.AbstractDoCoMoResultParser;
import com.google.zxing.client.result.AddressBookParsedResult;

final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {
   public static AddressBookParsedResult parse(Result var0) {
      String var1 = null;
      String var2 = var0.getText();
      AddressBookParsedResult var9 = var1;
      if(var2 != null) {
         if(!var2.startsWith("MECARD:")) {
            var9 = var1;
         } else {
            String[] var3 = matchDoCoMoPrefixedField("N:", var2, true);
            var9 = var1;
            if(var3 != null) {
               String var4 = parseName(var3[0]);
               String var7 = matchSingleDoCoMoPrefixedField("SOUND:", var2, true);
               var3 = matchDoCoMoPrefixedField("TEL:", var2, true);
               String[] var6 = matchDoCoMoPrefixedField("EMAIL:", var2, true);
               String var5 = matchSingleDoCoMoPrefixedField("NOTE:", var2, false);
               String[] var8 = matchDoCoMoPrefixedField("ADR:", var2, true);
               var1 = matchSingleDoCoMoPrefixedField("BDAY:", var2, true);
               String var10 = var1;
               if(var1 != null) {
                  var10 = var1;
                  if(!isStringOfDigits(var1, 8)) {
                     var10 = null;
                  }
               }

               var1 = matchSingleDoCoMoPrefixedField("URL:", var2, true);
               var2 = matchSingleDoCoMoPrefixedField("ORG:", var2, true);
               var9 = new AddressBookParsedResult(maybeWrap(var4), var7, var3, var6, var5, var8, var2, var10, (String)null, var1);
            }
         }
      }

      return var9;
   }

   private static String parseName(String var0) {
      int var1 = var0.indexOf(44);
      String var2 = var0;
      if(var1 >= 0) {
         var2 = var0.substring(var1 + 1) + ' ' + var0.substring(0, var1);
      }

      return var2;
   }
}
