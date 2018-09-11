package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.decoder.Version;

final class BitMatrixParser {
   private final BitMatrix mappingBitMatrix;
   private final BitMatrix readMappingMatrix;
   private final Version version;

   BitMatrixParser(BitMatrix var1) throws FormatException {
      int var2 = var1.getHeight();
      if(var2 >= 10 && var2 <= 144 && (var2 & 1) == 0) {
         this.version = this.readVersion(var1);
         this.mappingBitMatrix = this.extractDataRegion(var1);
         this.readMappingMatrix = new BitMatrix(this.mappingBitMatrix.getHeight());
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   BitMatrix extractDataRegion(BitMatrix var1) {
      int var3 = this.version.getSymbolSizeRows();
      int var2 = this.version.getSymbolSizeColumns();
      if(var1.getHeight() != var3) {
         throw new IllegalArgumentException("Dimension of bitMarix must match the version size");
      } else {
         int var6 = this.version.getDataRegionSizeRows();
         int var7 = this.version.getDataRegionSizeColumns();
         int var8 = var3 / var6;
         int var9 = var2 / var7;
         BitMatrix var10 = new BitMatrix(var8 * var6);

         for(var2 = 0; var2 < var8; ++var2) {
            for(var3 = 0; var3 < var9; ++var3) {
               for(int var4 = 0; var4 < var6; ++var4) {
                  for(int var5 = 0; var5 < var7; ++var5) {
                     if(var1.get((var7 + 2) * var3 + 1 + var5, (var6 + 2) * var2 + 1 + var4)) {
                        var10.set(var3 * var7 + var5, var2 * var6 + var4);
                     }
                  }
               }
            }
         }

         return var10;
      }
   }

   byte[] readCodewords() throws FormatException {
      boolean var7 = false;
      byte[] var10 = new byte[this.version.getTotalCodewords()];
      int var9 = this.mappingBitMatrix.getHeight();
      boolean var5 = false;
      boolean var4 = false;
      boolean var6 = false;
      int var1 = 0;
      int var2 = 4;
      int var3 = 0;

      do {
         if(var2 == var9 && var1 == 0 && !var6) {
            var10[var3] = (byte)this.readCorner1(var9, var9);
            ++var3;
            var2 -= 2;
            var1 += 2;
            var6 = true;
         } else if(var2 == var9 - 2 && var1 == 0 && (var9 & 3) != 0 && !var4) {
            var10[var3] = (byte)this.readCorner2(var9, var9);
            ++var3;
            var2 -= 2;
            var1 += 2;
            var4 = true;
         } else if(var2 == var9 + 4 && var1 == 2 && (var9 & 7) == 0 && !var5) {
            var10[var3] = (byte)this.readCorner3(var9, var9);
            ++var3;
            var2 -= 2;
            var1 += 2;
            var5 = true;
         } else if(var2 == var9 - 2 && var1 == 0 && (var9 & 7) == 4 && !var7) {
            var10[var3] = (byte)this.readCorner4(var9, var9);
            ++var3;
            var2 -= 2;
            var1 += 2;
            var7 = true;
         } else {
            int var8 = var1;
            var1 = var3;
            var3 = var2;
            var2 = var8;

            do {
               if(var3 < var9 && var2 >= 0 && !this.readMappingMatrix.get(var2, var3)) {
                  var8 = var1 + 1;
                  var10[var1] = (byte)this.readUtah(var3, var2, var9, var9);
                  var1 = var8;
               }

               var3 -= 2;
               var2 += 2;
            } while(var3 >= 0 && var2 < var9);

            var8 = var3 + 1;
            var2 += 3;

            while(true) {
               if(var8 >= 0 && var2 < var9 && !this.readMappingMatrix.get(var2, var8)) {
                  var3 = var1 + 1;
                  var10[var1] = (byte)this.readUtah(var8, var2, var9, var9);
               } else {
                  var3 = var1;
               }

               var8 += 2;
               var1 = var2 - 2;
               if(var8 >= var9 || var1 < 0) {
                  var2 = var8 + 3;
                  ++var1;
                  break;
               }

               var2 = var1;
               var1 = var3;
            }
         }
      } while(var2 < var9 || var1 < var9);

      if(var3 != this.version.getTotalCodewords()) {
         throw FormatException.getFormatInstance();
      } else {
         return var10;
      }
   }

   int readCorner1(int var1, int var2) {
      byte var3;
      if(this.readModule(var1 - 1, 0, var1, var2)) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if(this.readModule(var1 - 1, 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(var1 - 1, 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(2, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(3, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   int readCorner2(int var1, int var2) {
      byte var3;
      if(this.readModule(var1 - 3, 0, var1, var2)) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if(this.readModule(var1 - 2, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(var1 - 1, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 4, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 3, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   int readCorner3(int var1, int var2) {
      byte var3;
      if(this.readModule(var1 - 1, 0, var1, var2)) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if(this.readModule(var1 - 1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 3, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(1, var2 - 3, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(1, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   int readCorner4(int var1, int var2) {
      byte var3;
      if(this.readModule(var1 - 3, 0, var1, var2)) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      int var4 = var3 << 1;
      int var5 = var4;
      if(this.readModule(var1 - 2, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(var1 - 1, 0, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 2, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(0, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(1, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(2, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      var4 = var5 << 1;
      var5 = var4;
      if(this.readModule(3, var2 - 1, var1, var2)) {
         var5 = var4 | 1;
      }

      return var5;
   }

   boolean readModule(int var1, int var2, int var3, int var4) {
      if(var1 < 0) {
         var1 += var3;
         var2 += 4 - (var3 + 4 & 7);
      }

      int var5 = var2;
      var3 = var1;
      if(var2 < 0) {
         var5 = var2 + var4;
         var3 = var1 + (4 - (var4 + 4 & 7));
      }

      this.readMappingMatrix.set(var5, var3);
      return this.mappingBitMatrix.get(var5, var3);
   }

   int readUtah(int var1, int var2, int var3, int var4) {
      byte var5 = 0;
      if(this.readModule(var1 - 2, var2 - 2, var3, var4)) {
         var5 = 1;
      }

      int var6 = var5 << 1;
      int var7 = var6;
      if(this.readModule(var1 - 2, var2 - 1, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if(this.readModule(var1 - 1, var2 - 2, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if(this.readModule(var1 - 1, var2 - 1, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if(this.readModule(var1 - 1, var2, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if(this.readModule(var1, var2 - 2, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if(this.readModule(var1, var2 - 1, var3, var4)) {
         var7 = var6 | 1;
      }

      var6 = var7 << 1;
      var7 = var6;
      if(this.readModule(var1, var2, var3, var4)) {
         var7 = var6 | 1;
      }

      return var7;
   }

   Version readVersion(BitMatrix var1) throws FormatException {
      Version var3;
      if(this.version != null) {
         var3 = this.version;
      } else {
         int var2 = var1.getHeight();
         var3 = Version.getVersionForDimensions(var2, var2);
      }

      return var3;
   }
}
