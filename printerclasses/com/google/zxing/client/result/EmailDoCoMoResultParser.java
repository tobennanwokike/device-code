package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.AbstractDoCoMoResultParser;
import com.google.zxing.client.result.EmailAddressParsedResult;

final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {
   private static final char[] ATEXT_SYMBOLS = new char[]{'@', '.', '!', '#', '$', '%', '&', '\'', '*', '+', '-', '/', '=', '?', '^', '_', '`', '{', '|', '}', '~'};

   private static boolean isAtextSymbol(char var0) {
      boolean var3 = false;
      int var1 = 0;

      boolean var2;
      while(true) {
         var2 = var3;
         if(var1 >= ATEXT_SYMBOLS.length) {
            break;
         }

         if(var0 == ATEXT_SYMBOLS[var1]) {
            var2 = true;
            break;
         }

         ++var1;
      }

      return var2;
   }

   static boolean isBasicallyValidEmailAddress(String var0) {
      boolean var5 = false;
      boolean var4;
      if(var0 == null) {
         var4 = var5;
      } else {
         int var2 = 0;
         boolean var3 = false;

         while(true) {
            if(var2 >= var0.length()) {
               var4 = var3;
               break;
            }

            char var1 = var0.charAt(var2);
            if((var1 < 97 || var1 > 122) && (var1 < 65 || var1 > 90) && (var1 < 48 || var1 > 57)) {
               var4 = var5;
               if(!isAtextSymbol(var1)) {
                  break;
               }
            }

            var4 = var3;
            if(var1 == 64) {
               var4 = var5;
               if(var3) {
                  break;
               }

               var4 = true;
            }

            ++var2;
            var3 = var4;
         }
      }

      return var4;
   }

   public static EmailAddressParsedResult parse(Result var0) {
      Object var1 = null;
      String var2 = var0.getText();
      EmailAddressParsedResult var4 = (EmailAddressParsedResult)var1;
      if(var2 != null) {
         if(!var2.startsWith("MATMSG:")) {
            var4 = (EmailAddressParsedResult)var1;
         } else {
            String[] var3 = matchDoCoMoPrefixedField("TO:", var2, true);
            var4 = (EmailAddressParsedResult)var1;
            if(var3 != null) {
               String var5 = var3[0];
               var4 = (EmailAddressParsedResult)var1;
               if(isBasicallyValidEmailAddress(var5)) {
                  var4 = new EmailAddressParsedResult(var5, matchSingleDoCoMoPrefixedField("SUB:", var2, false), matchSingleDoCoMoPrefixedField("BODY:", var2, false), "mailto:" + var5);
               }
            }
         }
      }

      return var4;
   }
}
