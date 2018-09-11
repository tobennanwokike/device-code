package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.WifiParsedResult;

final class WifiResultParser extends ResultParser {
   public static WifiParsedResult parse(Result var0) {
      String var3 = var0.getText();
      WifiParsedResult var4;
      if(var3 != null && var3.startsWith("WIFI:")) {
         String var2 = matchSinglePrefixedField("S:", var3, ';', false);
         String var1 = matchSinglePrefixedField("P:", var3, ';', false);
         var4 = new WifiParsedResult(matchSinglePrefixedField("T:", var3, ';', false), var2, var1);
      } else {
         var4 = null;
      }

      return var4;
   }
}
