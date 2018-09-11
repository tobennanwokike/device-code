package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class EmailAddressParsedResult extends ParsedResult {
   private final String body;
   private final String emailAddress;
   private final String mailtoURI;
   private final String subject;

   EmailAddressParsedResult(String var1, String var2, String var3, String var4) {
      super(ParsedResultType.EMAIL_ADDRESS);
      this.emailAddress = var1;
      this.subject = var2;
      this.body = var3;
      this.mailtoURI = var4;
   }

   public String getBody() {
      return this.body;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(30);
      maybeAppend(this.emailAddress, var1);
      maybeAppend(this.subject, var1);
      maybeAppend(this.body, var1);
      return var1.toString();
   }

   public String getEmailAddress() {
      return this.emailAddress;
   }

   public String getMailtoURI() {
      return this.mailtoURI;
   }

   public String getSubject() {
      return this.subject;
   }
}
