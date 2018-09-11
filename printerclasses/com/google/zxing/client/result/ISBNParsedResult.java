package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class ISBNParsedResult extends ParsedResult {
   private final String isbn;

   ISBNParsedResult(String var1) {
      super(ParsedResultType.ISBN);
      this.isbn = var1;
   }

   public String getDisplayResult() {
      return this.isbn;
   }

   public String getISBN() {
      return this.isbn;
   }
}
