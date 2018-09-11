package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class TextParsedResult extends ParsedResult {
   private final String language;
   private final String text;

   public TextParsedResult(String var1, String var2) {
      super(ParsedResultType.TEXT);
      this.text = var1;
      this.language = var2;
   }

   public String getDisplayResult() {
      return this.text;
   }

   public String getLanguage() {
      return this.language;
   }

   public String getText() {
      return this.text;
   }
}
