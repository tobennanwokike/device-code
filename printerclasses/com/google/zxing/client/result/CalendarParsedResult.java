package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class CalendarParsedResult extends ParsedResult {
   private final String attendee;
   private final String description;
   private final String end;
   private final String location;
   private final String start;
   private final String summary;

   public CalendarParsedResult(String var1, String var2, String var3, String var4, String var5, String var6) {
      super(ParsedResultType.CALENDAR);
      if(var2 == null) {
         throw new IllegalArgumentException();
      } else {
         validateDate(var2);
         if(var3 == null) {
            var3 = var2;
         } else {
            validateDate(var3);
         }

         this.summary = var1;
         this.start = var2;
         this.end = var3;
         this.location = var4;
         this.attendee = var5;
         this.description = var6;
      }
   }

   private static void validateDate(String var0) {
      if(var0 != null) {
         int var2 = var0.length();
         if(var2 != 8 && var2 != 15 && var2 != 16) {
            throw new IllegalArgumentException();
         }

         int var1;
         for(var1 = 0; var1 < 8; ++var1) {
            if(!Character.isDigit(var0.charAt(var1))) {
               throw new IllegalArgumentException();
            }
         }

         if(var2 > 8) {
            if(var0.charAt(8) != 84) {
               throw new IllegalArgumentException();
            }

            for(var1 = 9; var1 < 15; ++var1) {
               if(!Character.isDigit(var0.charAt(var1))) {
                  throw new IllegalArgumentException();
               }
            }

            if(var2 == 16 && var0.charAt(15) != 90) {
               throw new IllegalArgumentException();
            }
         }
      }

   }

   public String getAttendee() {
      return this.attendee;
   }

   public String getDescription() {
      return this.description;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(100);
      maybeAppend(this.summary, var1);
      maybeAppend(this.start, var1);
      maybeAppend(this.end, var1);
      maybeAppend(this.location, var1);
      maybeAppend(this.attendee, var1);
      maybeAppend(this.description, var1);
      return var1.toString();
   }

   public String getEnd() {
      return this.end;
   }

   public String getLocation() {
      return this.location;
   }

   public String getStart() {
      return this.start;
   }

   public String getSummary() {
      return this.summary;
   }
}
