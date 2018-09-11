package com.google.zxing.common;

public final class BitArray {
   public int[] bits;
   public int size;

   public BitArray() {
      this.size = 0;
      this.bits = new int[1];
   }

   public BitArray(int var1) {
      this.size = var1;
      this.bits = makeArray(var1);
   }

   private void ensureCapacity(int var1) {
      if(var1 > this.bits.length << 5) {
         int[] var2 = makeArray(var1);
         System.arraycopy(this.bits, 0, var2, 0, this.bits.length);
         this.bits = var2;
      }

   }

   private static int[] makeArray(int var0) {
      return new int[var0 + 31 >> 5];
   }

   public void appendBit(boolean var1) {
      this.ensureCapacity(this.size + 1);
      if(var1) {
         int[] var3 = this.bits;
         int var2 = this.size >> 5;
         var3[var2] |= 1 << (this.size & 31);
      }

      ++this.size;
   }

   public void appendBitArray(BitArray var1) {
      int var3 = var1.getSize();
      this.ensureCapacity(this.size + var3);

      for(int var2 = 0; var2 < var3; ++var2) {
         this.appendBit(var1.get(var2));
      }

   }

   public void appendBits(int var1, int var2) {
      if(var2 >= 0 && var2 <= 32) {
         this.ensureCapacity(this.size + var2);

         while(var2 > 0) {
            boolean var3;
            if((var1 >> var2 - 1 & 1) == 1) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.appendBit(var3);
            --var2;
         }

      } else {
         throw new IllegalArgumentException("Num bits must be between 0 and 32");
      }
   }

   public void clear() {
      int var2 = this.bits.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         this.bits[var1] = 0;
      }

   }

   public void flip(int var1) {
      int[] var3 = this.bits;
      int var2 = var1 >> 5;
      var3[var2] ^= 1 << (var1 & 31);
   }

   public boolean get(int var1) {
      boolean var2 = true;
      if((this.bits[var1 >> 5] & 1 << (var1 & 31)) == 0) {
         var2 = false;
      }

      return var2;
   }

   public int[] getBitArray() {
      return this.bits;
   }

   public int getSize() {
      return this.size;
   }

   public int getSizeInBytes() {
      return this.size + 7 >> 3;
   }

   public boolean isRange(int var1, int var2, boolean var3) {
      if(var2 < var1) {
         throw new IllegalArgumentException();
      } else {
         if(var2 == var1) {
            var3 = true;
         } else {
            int var9 = var2 - 1;
            int var7 = var1 >> 5;
            int var10 = var9 >> 5;

            for(int var5 = var7; var5 <= var10; ++var5) {
               if(var5 > var7) {
                  var2 = 0;
               } else {
                  var2 = var1 & 31;
               }

               int var6;
               if(var5 < var10) {
                  var6 = 31;
               } else {
                  var6 = var9 & 31;
               }

               int var4;
               if(var2 == 0 && var6 == 31) {
                  var2 = -1;
               } else {
                  var4 = 0;

                  while(true) {
                     int var8 = var2;
                     var2 = var4;
                     if(var8 > var6) {
                        break;
                     }

                     var2 = var8 + 1;
                     var4 |= 1 << var8;
                  }
               }

               var6 = this.bits[var5];
               if(var3) {
                  var4 = var2;
               } else {
                  var4 = 0;
               }

               if((var6 & var2) != var4) {
                  var3 = false;
                  return var3;
               }
            }

            var3 = true;
         }

         return var3;
      }
   }

   public void reverse() {
      int[] var4 = new int[this.bits.length];
      int var2 = this.size;

      for(int var1 = 0; var1 < var2; ++var1) {
         if(this.get(var2 - var1 - 1)) {
            int var3 = var1 >> 5;
            var4[var3] |= 1 << (var1 & 31);
         }
      }

      this.bits = var4;
   }

   public void set(int var1) {
      int[] var3 = this.bits;
      int var2 = var1 >> 5;
      var3[var2] |= 1 << (var1 & 31);
   }

   public void setBulk(int var1, int var2) {
      this.bits[var1 >> 5] = var2;
   }

   public void toBytes(int var1, byte[] var2, int var3, int var4) {
      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = 0;

         int var7;
         int var8;
         for(var7 = 0; var6 < 8; var7 = var8) {
            var8 = var7;
            if(this.get(var1)) {
               var8 = var7 | 1 << 7 - var6;
            }

            ++var1;
            ++var6;
         }

         var2[var3 + var5] = (byte)var7;
      }

   }

   public String toString() {
      StringBuffer var3 = new StringBuffer(this.size);

      for(int var2 = 0; var2 < this.size; ++var2) {
         if((var2 & 7) == 0) {
            var3.append(' ');
         }

         char var1;
         if(this.get(var2)) {
            var1 = 88;
         } else {
            var1 = 46;
         }

         var3.append(var1);
      }

      return var3.toString();
   }

   public void xor(BitArray var1) {
      if(this.bits.length != var1.bits.length) {
         throw new IllegalArgumentException("Sizes don\'t match");
      } else {
         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            int[] var3 = this.bits;
            var3[var2] ^= var1.bits[var2];
         }

      }
   }
}
