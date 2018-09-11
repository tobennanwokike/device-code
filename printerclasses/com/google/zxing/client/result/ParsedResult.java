package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResultType;

public abstract class ParsedResult {
   private final ParsedResultType type;

   protected ParsedResult(ParsedResultType var1) {
      this.type = var1;
   }

   public static void maybeAppend(String var0, StringBuffer var1) {
      if(var0 != null && var0.length() > 0) {
         if(var1.length() > 0) {
            var1.append('\n');
         }

         var1.append(var0);
      }

   }

   public static void maybeAppend(String[] var0, StringBuffer var1) {
      if(var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if(var0[var2] != null && var0[var2].length() > 0) {
               if(var1.length() > 0) {
                  var1.append('\n');
               }

               var1.append(var0[var2]);
            }
         }
      }

   }

   public abstract String getDisplayResult();

   public ParsedResultType getType() {
      return this.type;
   }

   public String toString() {
      return this.getDisplayResult();
   }
}
