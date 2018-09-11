package com.google.zxing.client.result.optional;

import com.google.zxing.Result;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.optional.AbstractNDEFResultParser;
import com.google.zxing.client.result.optional.NDEFRecord;

final class NDEFTextResultParser extends AbstractNDEFResultParser {
   static String[] decodeTextPayload(byte[] var0) {
      byte var2 = var0[0];
      boolean var1;
      if((var2 & 128) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      int var5 = var2 & 31;
      String var4 = bytesToString(var0, 1, var5, "US-ASCII");
      String var3;
      if(var1) {
         var3 = "UTF-16";
      } else {
         var3 = "UTF8";
      }

      return new String[]{var4, bytesToString(var0, var5 + 1, var0.length - var5 - 1, var3)};
   }

   public static TextParsedResult parse(Result var0) {
      Object var1 = null;
      byte[] var3 = var0.getRawBytes();
      TextParsedResult var4;
      if(var3 == null) {
         var4 = (TextParsedResult)var1;
      } else {
         NDEFRecord var2 = NDEFRecord.readRecord(var3, 0);
         var4 = (TextParsedResult)var1;
         if(var2 != null) {
            var4 = (TextParsedResult)var1;
            if(var2.isMessageBegin()) {
               var4 = (TextParsedResult)var1;
               if(var2.isMessageEnd()) {
                  var4 = (TextParsedResult)var1;
                  if(var2.getType().equals("T")) {
                     String[] var5 = decodeTextPayload(var2.getPayload());
                     var4 = new TextParsedResult(var5[0], var5[1]);
                  }
               }
            }
         }
      }

      return var4;
   }
}
