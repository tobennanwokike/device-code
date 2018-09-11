package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import java.util.Hashtable;
import java.util.Vector;

final class SMSMMSResultParser extends ResultParser {
   private static void addNumberVia(Vector var0, Vector var1, String var2) {
      Object var4 = null;
      int var3 = var2.indexOf(59);
      if(var3 < 0) {
         var0.addElement(var2);
         var1.addElement((Object)null);
      } else {
         var0.addElement(var2.substring(0, var3));
         var2 = var2.substring(var3 + 1);
         String var5 = (String)var4;
         if(var2.startsWith("via=")) {
            var5 = var2.substring(4);
         }

         var1.addElement(var5);
      }

   }

   public static SMSParsedResult parse(Result var0) {
      String var3 = null;
      String var4 = null;
      String var5 = var0.getText();
      SMSParsedResult var7;
      if(var5 == null) {
         var7 = var4;
      } else {
         if(!var5.startsWith("sms:") && !var5.startsWith("SMS:") && !var5.startsWith("mms:")) {
            var7 = var4;
            if(!var5.startsWith("MMS:")) {
               return var7;
            }
         }

         Hashtable var8 = parseNameValuePairs(var5);
         boolean var1 = false;
         String var9;
         if(var8 != null && !var8.isEmpty()) {
            var3 = (String)var8.get("subject");
            var9 = (String)var8.get("body");
            var1 = true;
         } else {
            var4 = null;
            var9 = var3;
            var3 = var4;
         }

         int var2 = var5.indexOf(63, 4);
         if(var2 >= 0 && var1) {
            var4 = var5.substring(4, var2);
         } else {
            var4 = var5.substring(4);
         }

         Vector var6 = new Vector(1);
         Vector var11 = new Vector(1);
         int var10 = -1;

         while(true) {
            var2 = var4.indexOf(44, var10 + 1);
            if(var2 <= var10) {
               addNumberVia(var6, var11, var4.substring(var10 + 1));
               var7 = new SMSParsedResult(toStringArray(var6), toStringArray(var11), var3, var9);
               break;
            }

            addNumberVia(var6, var11, var4.substring(var10 + 1, var2));
            var10 = var2;
         }
      }

      return var7;
   }
}
