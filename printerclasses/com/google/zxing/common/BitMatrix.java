package com.google.zxing.common;

import com.google.zxing.common.BitArray;

public final class BitMatrix {
   public final int[] bits;
   public final int height;
   public final int rowSize;
   public final int width;

   public BitMatrix(int var1) {
      this(var1, var1);
   }

   public BitMatrix(int var1, int var2) {
      if(var1 >= 1 && var2 >= 1) {
         this.width = var1;
         this.height = var2;
         this.rowSize = var1 + 31 >> 5;
         this.bits = new int[this.rowSize * var2];
      } else {
         throw new IllegalArgumentException("Both dimensions must be greater than 0");
      }
   }

   public void clear() {
      int var2 = this.bits.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         this.bits[var1] = 0;
      }

   }

   public boolean equals(Object var1) {
      boolean var4 = false;
      boolean var3;
      if(!(var1 instanceof BitMatrix)) {
         var3 = var4;
      } else {
         BitMatrix var5 = (BitMatrix)var1;
         var3 = var4;
         if(this.width == var5.width) {
            var3 = var4;
            if(this.height == var5.height) {
               var3 = var4;
               if(this.rowSize == var5.rowSize) {
                  var3 = var4;
                  if(this.bits.length == var5.bits.length) {
                     int var2 = 0;

                     while(true) {
                        if(var2 >= this.bits.length) {
                           var3 = true;
                           break;
                        }

                        var3 = var4;
                        if(this.bits[var2] != var5.bits[var2]) {
                           break;
                        }

                        ++var2;
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   public void flip(int var1, int var2) {
      var2 = this.rowSize * var2 + (var1 >> 5);
      int[] var3 = this.bits;
      var3[var2] ^= 1 << (var1 & 31);
   }

   public boolean get(int var1, int var2) {
      int var3 = this.rowSize;
      boolean var4;
      if((this.bits[var3 * var2 + (var1 >> 5)] >>> (var1 & 31) & 1) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public int getHeight() {
      return this.height;
   }

   public BitArray getRow(int var1, BitArray var2) {
      BitArray var5;
      label20: {
         if(var2 != null) {
            var5 = var2;
            if(var2.getSize() >= this.width) {
               break label20;
            }
         }

         var5 = new BitArray(this.width);
      }

      int var4 = this.rowSize;

      for(int var3 = 0; var3 < this.rowSize; ++var3) {
         var5.setBulk(var3 << 5, this.bits[var1 * var4 + var3]);
      }

      return var5;
   }

   public int[] getTopLeftOnBit() {
      int var1;
      for(var1 = 0; var1 < this.bits.length && this.bits[var1] == 0; ++var1) {
         ;
      }

      int[] var6;
      if(var1 == this.bits.length) {
         var6 = null;
      } else {
         int var3 = var1 / this.rowSize;
         int var5 = this.rowSize;
         int var4 = this.bits[var1];

         int var2;
         for(var2 = 0; var4 << 31 - var2 == 0; ++var2) {
            ;
         }

         var6 = new int[]{(var1 % var5 << 5) + var2, var3};
      }

      return var6;
   }

   public int getWidth() {
      return this.width;
   }

   public int hashCode() {
      int var1 = this.width;
      int var3 = this.width;
      int var2 = this.height;
      var1 = this.rowSize + ((var1 * 31 + var3) * 31 + var2) * 31;

      for(var2 = 0; var2 < this.bits.length; ++var2) {
         var1 = var1 * 31 + this.bits[var2];
      }

      return var1;
   }

   public void set(int var1, int var2) {
      var2 = this.rowSize * var2 + (var1 >> 5);
      int[] var3 = this.bits;
      var3[var2] |= 1 << (var1 & 31);
   }

   public void setRegion(int var1, int var2, int var3, int var4) {
      if(var2 >= 0 && var1 >= 0) {
         if(var4 >= 1 && var3 >= 1) {
            int var5 = var1 + var3;
            var4 += var2;
            if(var4 <= this.height && var5 <= this.width) {
               while(var2 < var4) {
                  int var6 = this.rowSize;

                  for(var3 = var1; var3 < var5; ++var3) {
                     int[] var8 = this.bits;
                     int var7 = (var3 >> 5) + var2 * var6;
                     var8[var7] |= 1 << (var3 & 31);
                  }

                  ++var2;
               }

            } else {
               throw new IllegalArgumentException("The region must fit inside the matrix");
            }
         } else {
            throw new IllegalArgumentException("Height and width must be at least 1");
         }
      } else {
         throw new IllegalArgumentException("Left and top must be nonnegative");
      }
   }

   public String toString() {
      StringBuffer var4 = new StringBuffer(this.height * (this.width + 1));

      for(int var1 = 0; var1 < this.height; ++var1) {
         for(int var2 = 0; var2 < this.width; ++var2) {
            String var3;
            if(this.get(var2, var1)) {
               var3 = "X ";
            } else {
               var3 = "  ";
            }

            var4.append(var3);
         }

         var4.append('\n');
      }

      return var4.toString();
   }
}
