package com.google.zxing.qrcode.decoder;

import com.google.zxing.common.BitMatrix;

abstract class DataMask {
   private static final DataMask[] DATA_MASKS = new DataMask[]{new DataMask.DataMask000(null), new DataMask.DataMask001(null), new DataMask.DataMask010(null), new DataMask.DataMask011(null), new DataMask.DataMask100(null), new DataMask.DataMask101(null), new DataMask.DataMask110(null), new DataMask.DataMask111(null)};

   private DataMask() {
   }

   DataMask(Object var1) {
      this();
   }

   static DataMask forReference(int var0) {
      if(var0 >= 0 && var0 <= 7) {
         return DATA_MASKS[var0];
      } else {
         throw new IllegalArgumentException();
      }
   }

   abstract boolean isMasked(int var1, int var2);

   final void unmaskBitMatrix(BitMatrix var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         for(int var4 = 0; var4 < var2; ++var4) {
            if(this.isMasked(var3, var4)) {
               var1.flip(var4, var3);
            }
         }
      }

   }

   private static class DataMask000 extends DataMask {
      private DataMask000() {
         super(null);
      }

      DataMask000(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         boolean var3;
         if((var1 + var2 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static class DataMask001 extends DataMask {
      private DataMask001() {
         super(null);
      }

      DataMask001(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         boolean var3;
         if((var1 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static class DataMask010 extends DataMask {
      private DataMask010() {
         super(null);
      }

      DataMask010(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         boolean var3;
         if(var2 % 3 == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static class DataMask011 extends DataMask {
      private DataMask011() {
         super(null);
      }

      DataMask011(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         boolean var3;
         if((var1 + var2) % 3 == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static class DataMask100 extends DataMask {
      private DataMask100() {
         super(null);
      }

      DataMask100(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         boolean var3;
         if(((var1 >>> 1) + var2 / 3 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static class DataMask101 extends DataMask {
      private DataMask101() {
         super(null);
      }

      DataMask101(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         var1 *= var2;
         boolean var3;
         if(var1 % 3 + (var1 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static class DataMask110 extends DataMask {
      private DataMask110() {
         super(null);
      }

      DataMask110(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         var1 *= var2;
         boolean var3;
         if((var1 % 3 + (var1 & 1) & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   private static class DataMask111 extends DataMask {
      private DataMask111() {
         super(null);
      }

      DataMask111(Object var1) {
         this();
      }

      boolean isMasked(int var1, int var2) {
         boolean var3;
         if(((var1 + var2 & 1) + var1 * var2 % 3 & 1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
