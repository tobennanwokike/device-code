package com.google.zxing.client.result.optional;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class NDEFSmartPosterParsedResult extends ParsedResult {
   public static final int ACTION_DO = 0;
   public static final int ACTION_OPEN = 2;
   public static final int ACTION_SAVE = 1;
   public static final int ACTION_UNSPECIFIED = -1;
   private final int action;
   private final String title;
   private final String uri;

   NDEFSmartPosterParsedResult(int var1, String var2, String var3) {
      super(ParsedResultType.NDEF_SMART_POSTER);
      this.action = var1;
      this.uri = var2;
      this.title = var3;
   }

   public int getAction() {
      return this.action;
   }

   public String getDisplayResult() {
      String var1;
      if(this.title == null) {
         var1 = this.uri;
      } else {
         var1 = this.title + '\n' + this.uri;
      }

      return var1;
   }

   public String getTitle() {
      return this.title;
   }

   public String getURI() {
      return this.uri;
   }
}
