package com.google.zxing.oned.rss;

public class DataCharacter {
   private final int checksumPortion;
   private final int value;

   public DataCharacter(int var1, int var2) {
      this.value = var1;
      this.checksumPortion = var2;
   }

   public int getChecksumPortion() {
      return this.checksumPortion;
   }

   public int getValue() {
      return this.value;
   }
}
