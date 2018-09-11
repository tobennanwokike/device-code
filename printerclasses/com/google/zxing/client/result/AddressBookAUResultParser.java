package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ResultParser;
import java.util.Vector;

final class AddressBookAUResultParser extends ResultParser {
   private static String[] matchMultipleValuePrefix(String var0, int var1, String var2, boolean var3) {
      int var4 = 1;

      Vector var5;
      Vector var6;
      for(var5 = null; var4 <= var1; var5 = var6) {
         String var7 = matchSinglePrefixedField(var0 + var4 + ':', var2, '\r', var3);
         if(var7 == null) {
            break;
         }

         var6 = var5;
         if(var5 == null) {
            var6 = new Vector(var1);
         }

         var6.addElement(var7);
         ++var4;
      }

      String[] var8;
      if(var5 == null) {
         var8 = null;
      } else {
         var8 = toStringArray(var5);
      }

      return var8;
   }

   public static AddressBookParsedResult parse(Result var0) {
      String var7 = var0.getText();
      AddressBookParsedResult var8;
      if(var7 != null && var7.indexOf("MEMORY") >= 0 && var7.indexOf("\r\n") >= 0) {
         String var3 = matchSinglePrefixedField("NAME1:", var7, '\r', true);
         String var4 = matchSinglePrefixedField("NAME2:", var7, '\r', true);
         String[] var2 = matchMultipleValuePrefix("TEL", 3, var7, true);
         String[] var1 = matchMultipleValuePrefix("MAIL", 3, var7, true);
         String var5 = matchSinglePrefixedField("MEMORY:", var7, '\r', false);
         String var6 = matchSinglePrefixedField("ADD:", var7, '\r', true);
         String[] var9;
         if(var6 == null) {
            var9 = null;
         } else {
            var9 = new String[]{var6};
         }

         var8 = new AddressBookParsedResult(maybeWrap(var3), var4, var2, var1, var5, var9, (String)null, (String)null, (String)null, (String)null);
      } else {
         var8 = null;
      }

      return var8;
   }
}
