package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.DataMask;
import com.google.zxing.qrcode.decoder.FormatInformation;
import com.google.zxing.qrcode.decoder.Version;

final class BitMatrixParser {
   private final BitMatrix bitMatrix;
   private FormatInformation parsedFormatInfo;
   private Version parsedVersion;

   BitMatrixParser(BitMatrix var1) throws FormatException {
      int var2 = var1.getHeight();
      if(var2 >= 21 && (var2 & 3) == 1) {
         this.bitMatrix = var1;
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   private int copyBit(int var1, int var2, int var3) {
      if(this.bitMatrix.get(var1, var2)) {
         var1 = var3 << 1 | 1;
      } else {
         var1 = var3 << 1;
      }

      return var1;
   }

   byte[] readCodewords() throws FormatException {
      FormatInformation var14 = this.readFormatInformation();
      Version var13 = this.readVersion();
      DataMask var16 = DataMask.forReference(var14.getDataMask());
      int var12 = this.bitMatrix.getHeight();
      var16.unmaskBitMatrix(this.bitMatrix, var12);
      BitMatrix var15 = var13.buildFunctionPattern();
      byte[] var17 = new byte[var13.getTotalCodewords()];
      int var1 = var12 - 1;
      int var2 = 0;
      int var3 = 0;
      int var9 = 0;

      for(boolean var5 = true; var1 > 0; var5 ^= true) {
         int var6 = var1;
         if(var1 == 6) {
            var6 = var1 - 1;
         }

         int var4;
         for(var1 = 0; var1 < var12; var3 = var4) {
            int var7;
            if(var5) {
               var7 = var12 - 1 - var1;
            } else {
               var7 = var1;
            }

            int var8 = 0;
            var4 = var3;

            int var10;
            int var11;
            for(var11 = var2; var8 < 2; var9 = var10) {
               var3 = var11;
               var2 = var4;
               var10 = var9;
               if(!var15.get(var6 - var8, var7)) {
                  ++var11;
                  var2 = var4 << 1;
                  var4 = var2;
                  if(this.bitMatrix.get(var6 - var8, var7)) {
                     var4 = var2 | 1;
                  }

                  var3 = var11;
                  var2 = var4;
                  var10 = var9;
                  if(var11 == 8) {
                     var17[var9] = (byte)var4;
                     var2 = 0;
                     var10 = var9 + 1;
                     var3 = 0;
                  }
               }

               ++var8;
               var11 = var3;
               var4 = var2;
            }

            ++var1;
            var2 = var11;
         }

         var1 = var6 - 2;
      }

      if(var9 != var13.getTotalCodewords()) {
         throw FormatException.getFormatInstance();
      } else {
         return var17;
      }
   }

   FormatInformation readFormatInformation() throws FormatException {
      byte var3 = 0;
      FormatInformation var6;
      if(this.parsedFormatInfo != null) {
         var6 = this.parsedFormatInfo;
      } else {
         int var1 = 0;

         int var2;
         for(var2 = 0; var1 < 6; ++var1) {
            var2 = this.copyBit(var1, 8, var2);
         }

         var2 = this.copyBit(8, 7, this.copyBit(8, 8, this.copyBit(7, 8, var2)));

         for(var1 = 5; var1 >= 0; --var1) {
            var2 = this.copyBit(8, var1, var2);
         }

         int var5 = this.bitMatrix.getHeight();
         int var4 = var5 - 1;
         var1 = var3;

         int var7;
         for(var7 = var4; var7 >= var5 - 8; --var7) {
            var1 = this.copyBit(var7, 8, var1);
         }

         var4 = var5 - 7;
         var7 = var1;

         for(var1 = var4; var1 < var5; ++var1) {
            var7 = this.copyBit(8, var1, var7);
         }

         this.parsedFormatInfo = FormatInformation.decodeFormatInformation(var2, var7);
         if(this.parsedFormatInfo == null) {
            throw FormatException.getFormatInstance();
         }

         var6 = this.parsedFormatInfo;
      }

      return var6;
   }

   Version readVersion() throws FormatException {
      byte var4 = 0;
      Version var7;
      if(this.parsedVersion != null) {
         var7 = this.parsedVersion;
      } else {
         int var5 = this.bitMatrix.getHeight();
         int var1 = var5 - 17 >> 2;
         if(var1 <= 6) {
            var7 = Version.getVersionForNumber(var1);
         } else {
            int var6 = var5 - 11;
            var1 = 5;
            int var2 = 0;

            while(true) {
               int var3;
               if(var1 < 0) {
                  this.parsedVersion = Version.decodeVersionInformation(var2);
                  if(this.parsedVersion != null && this.parsedVersion.getDimensionForVersion() == var5) {
                     var7 = this.parsedVersion;
                     break;
                  }

                  var1 = 5;

                  for(var2 = var4; var1 >= 0; --var1) {
                     for(var3 = var5 - 9; var3 >= var6; --var3) {
                        var2 = this.copyBit(var1, var3, var2);
                     }
                  }

                  this.parsedVersion = Version.decodeVersionInformation(var2);
                  if(this.parsedVersion != null && this.parsedVersion.getDimensionForVersion() == var5) {
                     var7 = this.parsedVersion;
                     break;
                  }

                  throw FormatException.getFormatInstance();
               }

               for(var3 = var5 - 9; var3 >= var6; --var3) {
                  var2 = this.copyBit(var3, var1, var2);
               }

               --var1;
            }
         }
      }

      return var7;
   }
}
