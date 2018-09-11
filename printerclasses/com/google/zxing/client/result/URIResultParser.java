package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.URIParsedResult;

final class URIResultParser extends ResultParser {
   static boolean isBasicallyValidURI(String var0) {
      boolean var5 = false;
      boolean var4 = var5;
      if(var0 != null) {
         var4 = var5;
         if(var0.indexOf(32) < 0) {
            if(var0.indexOf(10) >= 0) {
               var4 = var5;
            } else {
               int var1 = var0.indexOf(46);
               var4 = var5;
               if(var1 < var0.length() - 2) {
                  int var2 = var0.indexOf(58);
                  if(var1 < 0) {
                     var4 = var5;
                     if(var2 < 0) {
                        return var4;
                     }
                  }

                  if(var2 >= 0) {
                     char var3;
                     if(var1 >= 0 && var1 <= var2) {
                        var4 = var5;
                        if(var2 >= var0.length() - 2) {
                           return var4;
                        }

                        for(var1 = var2 + 1; var1 < var2 + 3; ++var1) {
                           var3 = var0.charAt(var1);
                           var4 = var5;
                           if(var3 < 48) {
                              return var4;
                           }

                           var4 = var5;
                           if(var3 > 57) {
                              return var4;
                           }
                        }
                     } else {
                        for(var1 = 0; var1 < var2; ++var1) {
                           var3 = var0.charAt(var1);
                           if(var3 < 97 || var3 > 122) {
                              var4 = var5;
                              if(var3 < 65) {
                                 return var4;
                              }

                              var4 = var5;
                              if(var3 > 90) {
                                 return var4;
                              }
                           }
                        }
                     }
                  }

                  var4 = true;
               }
            }
         }
      }

      return var4;
   }

   public static URIParsedResult parse(Result var0) {
      String var1 = var0.getText();
      String var2 = var1;
      if(var1 != null) {
         var2 = var1;
         if(var1.startsWith("URL:")) {
            var2 = var1.substring(4);
         }
      }

      URIParsedResult var3;
      if(!isBasicallyValidURI(var2)) {
         var3 = null;
      } else {
         var3 = new URIParsedResult(var2, (String)null);
      }

      return var3;
   }
}
