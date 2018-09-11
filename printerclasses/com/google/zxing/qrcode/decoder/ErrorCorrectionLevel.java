package com.google.zxing.qrcode.decoder;

public final class ErrorCorrectionLevel {
   private static final ErrorCorrectionLevel[] FOR_BITS;
   public static final ErrorCorrectionLevel H = new ErrorCorrectionLevel(3, 2, "H");
   public static final ErrorCorrectionLevel L = new ErrorCorrectionLevel(0, 1, "L");
   public static final ErrorCorrectionLevel M = new ErrorCorrectionLevel(1, 0, "M");
   public static final ErrorCorrectionLevel Q = new ErrorCorrectionLevel(2, 3, "Q");
   private final int bits;
   private final String name;
   private final int ordinal;

   static {
      FOR_BITS = new ErrorCorrectionLevel[]{M, L, H, Q};
   }

   private ErrorCorrectionLevel(int var1, int var2, String var3) {
      this.ordinal = var1;
      this.bits = var2;
      this.name = var3;
   }

   public static ErrorCorrectionLevel forBits(int var0) {
      if(var0 >= 0 && var0 < FOR_BITS.length) {
         return FOR_BITS[var0];
      } else {
         throw new IllegalArgumentException();
      }
   }

   public int getBits() {
      return this.bits;
   }

   public String getName() {
      return this.name;
   }

   public int ordinal() {
      return this.ordinal;
   }

   public String toString() {
      return this.name;
   }
}
