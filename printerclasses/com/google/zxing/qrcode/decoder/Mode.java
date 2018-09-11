package com.google.zxing.qrcode.decoder;

import com.google.zxing.qrcode.decoder.Version;

public final class Mode {
   public static final Mode ALPHANUMERIC = new Mode(new int[]{9, 11, 13}, 2, "ALPHANUMERIC");
   public static final Mode BYTE = new Mode(new int[]{8, 16, 16}, 4, "BYTE");
   public static final Mode ECI = new Mode((int[])null, 7, "ECI");
   public static final Mode FNC1_FIRST_POSITION = new Mode((int[])null, 5, "FNC1_FIRST_POSITION");
   public static final Mode FNC1_SECOND_POSITION = new Mode((int[])null, 9, "FNC1_SECOND_POSITION");
   public static final Mode KANJI = new Mode(new int[]{8, 10, 12}, 8, "KANJI");
   public static final Mode NUMERIC = new Mode(new int[]{10, 12, 14}, 1, "NUMERIC");
   public static final Mode STRUCTURED_APPEND = new Mode(new int[]{0, 0, 0}, 3, "STRUCTURED_APPEND");
   public static final Mode TERMINATOR = new Mode(new int[]{0, 0, 0}, 0, "TERMINATOR");
   private final int bits;
   private final int[] characterCountBitsForVersions;
   private final String name;

   private Mode(int[] var1, int var2, String var3) {
      this.characterCountBitsForVersions = var1;
      this.bits = var2;
      this.name = var3;
   }

   public static Mode forBits(int var0) {
      Mode var1;
      switch(var0) {
      case 0:
         var1 = TERMINATOR;
         break;
      case 1:
         var1 = NUMERIC;
         break;
      case 2:
         var1 = ALPHANUMERIC;
         break;
      case 3:
         var1 = STRUCTURED_APPEND;
         break;
      case 4:
         var1 = BYTE;
         break;
      case 5:
         var1 = FNC1_FIRST_POSITION;
         break;
      case 6:
      default:
         throw new IllegalArgumentException();
      case 7:
         var1 = ECI;
         break;
      case 8:
         var1 = KANJI;
         break;
      case 9:
         var1 = FNC1_SECOND_POSITION;
      }

      return var1;
   }

   public int getBits() {
      return this.bits;
   }

   public int getCharacterCountBits(Version var1) {
      if(this.characterCountBitsForVersions == null) {
         throw new IllegalArgumentException("Character count doesn\'t apply to this mode");
      } else {
         int var2 = var1.getVersionNumber();
         byte var3;
         if(var2 <= 9) {
            var3 = 0;
         } else if(var2 <= 26) {
            var3 = 1;
         } else {
            var3 = 2;
         }

         return this.characterCountBitsForVersions[var3];
      }
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return this.name;
   }
}
