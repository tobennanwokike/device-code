package com.google.zxing.common;

public final class BitSource {
   private int bitOffset;
   private int byteOffset;
   private final byte[] bytes;

   public BitSource(byte[] var1) {
      this.bytes = var1;
   }

   public int available() {
      return (this.bytes.length - this.byteOffset) * 8 - this.bitOffset;
   }

   public int readBits(int var1) {
      if(var1 >= 1 && var1 <= 32) {
         int var2;
         int var3;
         if(this.bitOffset > 0) {
            var3 = 8 - this.bitOffset;
            if(var1 < var3) {
               var2 = var1;
            } else {
               var2 = var3;
            }

            int var4 = var3 - var2;
            byte var6 = this.bytes[this.byteOffset];
            this.bitOffset += var2;
            if(this.bitOffset == 8) {
               this.bitOffset = 0;
               ++this.byteOffset;
            }

            var4 = (255 >> 8 - var2 << var4 & var6) >> var4;
            var3 = var1 - var2;
            var1 = var4;
         } else {
            byte var5 = 0;
            var3 = var1;
            var1 = var5;
         }

         var2 = var1;
         if(var3 > 0) {
            while(var3 >= 8) {
               var1 = var1 << 8 | this.bytes[this.byteOffset] & 255;
               ++this.byteOffset;
               var3 -= 8;
            }

            var2 = var1;
            if(var3 > 0) {
               var2 = 8 - var3;
               var2 = var1 << var3 | (255 >> var2 << var2 & this.bytes[this.byteOffset]) >> var2;
               this.bitOffset += var3;
            }
         }

         return var2;
      } else {
         throw new IllegalArgumentException();
      }
   }
}
