package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class URIParsedResult extends ParsedResult {
   private final String title;
   private final String uri;

   public URIParsedResult(String var1, String var2) {
      super(ParsedResultType.URI);
      this.uri = massageURI(var1);
      this.title = var2;
   }

   private boolean containsUser() {
      int var1 = this.uri.indexOf(58);
      int var2 = this.uri.length();
      ++var1;

      while(var1 < var2 && this.uri.charAt(var1) == 47) {
         ++var1;
      }

      int var3 = this.uri.indexOf(47, var1);
      if(var3 >= 0) {
         var2 = var3;
      }

      var3 = this.uri.indexOf(64, var1);
      boolean var4;
      if(var3 >= var1 && var3 < var2) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   private static boolean isColonFollowedByPortNumber(String var0, int var1) {
      boolean var4 = false;
      int var2 = var0.indexOf(47, var1 + 1);
      if(var2 < 0) {
         var2 = var0.length();
      }

      boolean var3;
      if(var2 <= var1 + 1) {
         var3 = var4;
      } else {
         ++var1;

         while(true) {
            if(var1 >= var2) {
               var3 = true;
               break;
            }

            var3 = var4;
            if(var0.charAt(var1) < 48) {
               break;
            }

            var3 = var4;
            if(var0.charAt(var1) > 57) {
               break;
            }

            ++var1;
         }
      }

      return var3;
   }

   private static String massageURI(String var0) {
      int var1 = var0.indexOf(58);
      if(var1 < 0) {
         var0 = "http://" + var0;
      } else if(isColonFollowedByPortNumber(var0, var1)) {
         var0 = "http://" + var0;
      } else {
         var0 = var0.substring(0, var1).toLowerCase() + var0.substring(var1);
      }

      return var0;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(30);
      maybeAppend(this.title, var1);
      maybeAppend(this.uri, var1);
      return var1.toString();
   }

   public String getTitle() {
      return this.title;
   }

   public String getURI() {
      return this.uri;
   }

   public boolean isPossiblyMaliciousURI() {
      return this.containsUser();
   }
}
