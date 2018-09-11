package com.google.zxing.client.result.optional;

import com.google.zxing.client.result.optional.AbstractNDEFResultParser;

final class NDEFRecord {
   public static final String ACTION_WELL_KNOWN_TYPE = "act";
   public static final String SMART_POSTER_WELL_KNOWN_TYPE = "Sp";
   private static final int SUPPORTED_HEADER = 17;
   private static final int SUPPORTED_HEADER_MASK = 63;
   public static final String TEXT_WELL_KNOWN_TYPE = "T";
   public static final String URI_WELL_KNOWN_TYPE = "U";
   private final int header;
   private final byte[] payload;
   private final int totalRecordLength;
   private final String type;

   private NDEFRecord(int var1, String var2, byte[] var3, int var4) {
      this.header = var1;
      this.type = var2;
      this.payload = var3;
      this.totalRecordLength = var4;
   }

   static NDEFRecord readRecord(byte[] var0, int var1) {
      int var4 = var0[var1] & 255;
      NDEFRecord var7;
      if(((var4 ^ 17) & 63) != 0) {
         var7 = null;
      } else {
         int var2 = var0[var1 + 1] & 255;
         int var3 = var0[var1 + 2] & 255;
         String var5 = AbstractNDEFResultParser.bytesToString(var0, var1 + 3, var2, "US-ASCII");
         byte[] var6 = new byte[var3];
         System.arraycopy(var0, var1 + 3 + var2, var6, 0, var3);
         var7 = new NDEFRecord(var4, var5, var6, var2 + 3 + var3);
      }

      return var7;
   }

   byte[] getPayload() {
      return this.payload;
   }

   int getTotalRecordLength() {
      return this.totalRecordLength;
   }

   String getType() {
      return this.type;
   }

   boolean isMessageBegin() {
      boolean var1;
      if((this.header & 128) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean isMessageEnd() {
      boolean var1;
      if((this.header & 64) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
