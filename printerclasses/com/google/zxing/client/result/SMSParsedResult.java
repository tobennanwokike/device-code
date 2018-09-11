package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class SMSParsedResult extends ParsedResult {
   private final String body;
   private final String[] numbers;
   private final String subject;
   private final String[] vias;

   public SMSParsedResult(String var1, String var2, String var3, String var4) {
      super(ParsedResultType.SMS);
      this.numbers = new String[]{var1};
      this.vias = new String[]{var2};
      this.subject = var3;
      this.body = var4;
   }

   public SMSParsedResult(String[] var1, String[] var2, String var3, String var4) {
      super(ParsedResultType.SMS);
      this.numbers = var1;
      this.vias = var2;
      this.subject = var3;
      this.body = var4;
   }

   public String getBody() {
      return this.body;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(100);
      maybeAppend(this.numbers, var1);
      maybeAppend(this.subject, var1);
      maybeAppend(this.body, var1);
      return var1.toString();
   }

   public String[] getNumbers() {
      return this.numbers;
   }

   public String getSMSURI() {
      boolean var3 = true;
      StringBuffer var4 = new StringBuffer();
      var4.append("sms:");
      int var1 = 0;

      boolean var2;
      for(var2 = true; var1 < this.numbers.length; ++var1) {
         if(var2) {
            var2 = false;
         } else {
            var4.append(',');
         }

         var4.append(this.numbers[var1]);
         if(this.vias[var1] != null) {
            var4.append(";via=");
            var4.append(this.vias[var1]);
         }
      }

      boolean var5;
      if(this.body != null) {
         var5 = true;
      } else {
         var5 = false;
      }

      if(this.subject != null) {
         var2 = var3;
      } else {
         var2 = false;
      }

      if(var5 || var2) {
         var4.append('?');
         if(var5) {
            var4.append("body=");
            var4.append(this.body);
         }

         if(var2) {
            if(var5) {
               var4.append('&');
            }

            var4.append("subject=");
            var4.append(this.subject);
         }
      }

      return var4.toString();
   }

   public String getSubject() {
      return this.subject;
   }

   public String[] getVias() {
      return this.vias;
   }
}
