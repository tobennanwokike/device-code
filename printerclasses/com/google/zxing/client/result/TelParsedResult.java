package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class TelParsedResult extends ParsedResult {
   private final String number;
   private final String telURI;
   private final String title;

   public TelParsedResult(String var1, String var2, String var3) {
      super(ParsedResultType.TEL);
      this.number = var1;
      this.telURI = var2;
      this.title = var3;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(20);
      maybeAppend(this.number, var1);
      maybeAppend(this.title, var1);
      return var1.toString();
   }

   public String getNumber() {
      return this.number;
   }

   public String getTelURI() {
      return this.telURI;
   }

   public String getTitle() {
      return this.title;
   }
}
