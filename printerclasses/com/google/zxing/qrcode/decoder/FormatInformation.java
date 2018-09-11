package com.google.zxing.qrcode.decoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

final class FormatInformation {
   private static final int[] BITS_SET_IN_HALF_BYTE = new int[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4};
   private static final int[][] FORMAT_INFO_DECODE_LOOKUP = new int[][]{{21522, 0}, {20773, 1}, {24188, 2}, {23371, 3}, {17913, 4}, {16590, 5}, {20375, 6}, {19104, 7}, {30660, 8}, {29427, 9}, {32170, 10}, {30877, 11}, {26159, 12}, {25368, 13}, {27713, 14}, {26998, 15}, {5769, 16}, {5054, 17}, {7399, 18}, {6608, 19}, {1890, 20}, {597, 21}, {3340, 22}, {2107, 23}, {13663, 24}, {12392, 25}, {16177, 26}, {14854, 27}, {9396, 28}, {8579, 29}, {11994, 30}, {11245, 31}};
   private static final int FORMAT_INFO_MASK_QR = 21522;
   private final byte dataMask;
   private final ErrorCorrectionLevel errorCorrectionLevel;

   private FormatInformation(int var1) {
      this.errorCorrectionLevel = ErrorCorrectionLevel.forBits(var1 >> 3 & 3);
      this.dataMask = (byte)(var1 & 7);
   }

   static FormatInformation decodeFormatInformation(int var0, int var1) {
      FormatInformation var2 = doDecodeFormatInformation(var0, var1);
      if(var2 == null) {
         var2 = doDecodeFormatInformation(var0 ^ 21522, var1 ^ 21522);
      }

      return var2;
   }

   private static FormatInformation doDecodeFormatInformation(int var0, int var1) {
      int var2 = 0;
      int var3 = Integer.MAX_VALUE;
      int var4 = 0;

      FormatInformation var7;
      while(true) {
         if(var4 < FORMAT_INFO_DECODE_LOOKUP.length) {
            int[] var8 = FORMAT_INFO_DECODE_LOOKUP[var4];
            int var6 = var8[0];
            if(var6 != var0 && var6 != var1) {
               int var5 = numBitsDiffering(var0, var6);
               if(var5 < var3) {
                  var2 = var8[1];
                  var3 = var5;
               }

               if(var0 != var1) {
                  var5 = numBitsDiffering(var1, var6);
                  if(var5 < var3) {
                     var2 = var8[1];
                     var3 = var5;
                  }
               }

               ++var4;
               continue;
            }

            var7 = new FormatInformation(var8[1]);
            break;
         }

         if(var3 <= 3) {
            var7 = new FormatInformation(var2);
         } else {
            var7 = null;
         }
         break;
      }

      return var7;
   }

   static int numBitsDiffering(int var0, int var1) {
      int var2 = var0 ^ var1;
      int var5 = BITS_SET_IN_HALF_BYTE[var2 & 15];
      int var3 = BITS_SET_IN_HALF_BYTE[var2 >>> 4 & 15];
      int var4 = BITS_SET_IN_HALF_BYTE[var2 >>> 8 & 15];
      var0 = BITS_SET_IN_HALF_BYTE[var2 >>> 12 & 15];
      int var6 = BITS_SET_IN_HALF_BYTE[var2 >>> 16 & 15];
      int var7 = BITS_SET_IN_HALF_BYTE[var2 >>> 20 & 15];
      var1 = BITS_SET_IN_HALF_BYTE[var2 >>> 24 & 15];
      return BITS_SET_IN_HALF_BYTE[var2 >>> 28 & 15] + var5 + var3 + var4 + var0 + var6 + var7 + var1;
   }

   public boolean equals(Object var1) {
      boolean var3 = false;
      boolean var2;
      if(!(var1 instanceof FormatInformation)) {
         var2 = var3;
      } else {
         FormatInformation var4 = (FormatInformation)var1;
         var2 = var3;
         if(this.errorCorrectionLevel == var4.errorCorrectionLevel) {
            var2 = var3;
            if(this.dataMask == var4.dataMask) {
               var2 = true;
            }
         }
      }

      return var2;
   }

   byte getDataMask() {
      return this.dataMask;
   }

   ErrorCorrectionLevel getErrorCorrectionLevel() {
      return this.errorCorrectionLevel;
   }

   public int hashCode() {
      return this.errorCorrectionLevel.ordinal() << 3 | this.dataMask;
   }
}
