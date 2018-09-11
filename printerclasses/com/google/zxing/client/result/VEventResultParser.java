package com.google.zxing.client.result;

import com.google.zxing.Result;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.VCardResultParser;

final class VEventResultParser extends ResultParser {
   public static CalendarParsedResult parse(Result var0) {
      String var7 = var0.getText();
      CalendarParsedResult var8;
      if(var7 == null) {
         var8 = null;
      } else if(var7.indexOf("BEGIN:VEVENT") < 0) {
         var8 = null;
      } else {
         String var1 = VCardResultParser.matchSingleVCardPrefixedField("SUMMARY", var7, true);
         String var3 = VCardResultParser.matchSingleVCardPrefixedField("DTSTART", var7, true);
         String var4 = VCardResultParser.matchSingleVCardPrefixedField("DTEND", var7, true);
         String var2 = VCardResultParser.matchSingleVCardPrefixedField("LOCATION", var7, true);
         String var5 = VCardResultParser.matchSingleVCardPrefixedField("DESCRIPTION", var7, true);

         try {
            var8 = new CalendarParsedResult(var1, var3, var4, var2, (String)null, var5);
         } catch (IllegalArgumentException var6) {
            var8 = null;
         }
      }

      return var8;
   }
}
