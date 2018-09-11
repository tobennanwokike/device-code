package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.AbstractDoCoMoResultParser;
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.client.result.URIResultParser;

final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser {
   public static URIParsedResult parse(Result var0) {
      Object var1 = null;
      String var3 = var0.getText();
      URIParsedResult var4 = (URIParsedResult)var1;
      if(var3 != null) {
         if(!var3.startsWith("MEBKM:")) {
            var4 = (URIParsedResult)var1;
         } else {
            String var2 = matchSingleDoCoMoPrefixedField("TITLE:", var3, true);
            String[] var5 = matchDoCoMoPrefixedField("URL:", var3, true);
            var4 = (URIParsedResult)var1;
            if(var5 != null) {
               var3 = var5[0];
               var4 = (URIParsedResult)var1;
               if(URIResultParser.isBasicallyValidURI(var3)) {
                  var4 = new URIParsedResult(var3, var2);
               }
            }
         }
      }

      return var4;
   }
}
