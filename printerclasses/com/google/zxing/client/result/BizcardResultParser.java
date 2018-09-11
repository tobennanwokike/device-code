package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.AbstractDoCoMoResultParser;
import com.google.zxing.client.result.AddressBookParsedResult;
import java.util.Vector;

final class BizcardResultParser extends AbstractDoCoMoResultParser {
   private static String buildName(String var0, String var1) {
      if(var0 == null) {
         var0 = var1;
      } else if(var1 != null) {
         var0 = var0 + ' ' + var1;
      }

      return var0;
   }

   private static String[] buildPhoneNumbers(String var0, String var1, String var2) {
      Vector var5 = new Vector(3);
      if(var0 != null) {
         var5.addElement(var0);
      }

      if(var1 != null) {
         var5.addElement(var1);
      }

      if(var2 != null) {
         var5.addElement(var2);
      }

      int var4 = var5.size();
      String[] var6;
      if(var4 == 0) {
         var6 = null;
      } else {
         var6 = new String[var4];

         for(int var3 = 0; var3 < var4; ++var3) {
            var6[var3] = (String)var5.elementAt(var3);
         }
      }

      return var6;
   }

   public static AddressBookParsedResult parse(Result var0) {
      String var7 = var0.getText();
      AddressBookParsedResult var8;
      if(var7 != null && var7.startsWith("BIZCARD:")) {
         String var4 = buildName(matchSingleDoCoMoPrefixedField("N:", var7, true), matchSingleDoCoMoPrefixedField("X:", var7, true));
         String var6 = matchSingleDoCoMoPrefixedField("T:", var7, true);
         String var1 = matchSingleDoCoMoPrefixedField("C:", var7, true);
         String[] var3 = matchDoCoMoPrefixedField("A:", var7, true);
         String var9 = matchSingleDoCoMoPrefixedField("B:", var7, true);
         String var5 = matchSingleDoCoMoPrefixedField("M:", var7, true);
         String var2 = matchSingleDoCoMoPrefixedField("F:", var7, true);
         var7 = matchSingleDoCoMoPrefixedField("E:", var7, true);
         var8 = new AddressBookParsedResult(maybeWrap(var4), (String)null, buildPhoneNumbers(var9, var5, var2), maybeWrap(var7), (String)null, var3, var1, (String)null, var6, (String)null);
      } else {
         var8 = null;
      }

      return var8;
   }
}
