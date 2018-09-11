package com.google.zxing;

import java.util.Hashtable;

public final class ResultMetadataType {
   public static final ResultMetadataType BYTE_SEGMENTS = new ResultMetadataType("BYTE_SEGMENTS");
   public static final ResultMetadataType ERROR_CORRECTION_LEVEL = new ResultMetadataType("ERROR_CORRECTION_LEVEL");
   public static final ResultMetadataType ISSUE_NUMBER = new ResultMetadataType("ISSUE_NUMBER");
   public static final ResultMetadataType ORIENTATION = new ResultMetadataType("ORIENTATION");
   public static final ResultMetadataType OTHER = new ResultMetadataType("OTHER");
   public static final ResultMetadataType POSSIBLE_COUNTRY = new ResultMetadataType("POSSIBLE_COUNTRY");
   public static final ResultMetadataType SUGGESTED_PRICE = new ResultMetadataType("SUGGESTED_PRICE");
   private static final Hashtable VALUES = new Hashtable();
   private final String name;

   private ResultMetadataType(String var1) {
      this.name = var1;
      VALUES.put(var1, this);
   }

   public static ResultMetadataType valueOf(String var0) {
      if(var0 != null && var0.length() != 0) {
         ResultMetadataType var1 = (ResultMetadataType)VALUES.get(var0);
         if(var1 == null) {
            throw new IllegalArgumentException();
         } else {
            return var1;
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return this.name;
   }
}
