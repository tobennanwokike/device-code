package com.google.zxing.datamatrix.decoder;

import com.google.zxing.datamatrix.decoder.Version;

final class DataBlock {
   private final byte[] codewords;
   private final int numDataCodewords;

   private DataBlock(int var1, byte[] var2) {
      this.numDataCodewords = var1;
      this.codewords = var2;
   }

   static DataBlock[] getDataBlocks(byte[] var0, Version var1) {
      Version.ECBlocks var11 = var1.getECBlocks();
      Version.ECB[] var12 = var11.getECBlocks();
      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < var12.length; ++var2) {
         var3 += var12[var2].getCount();
      }

      DataBlock[] var9 = new DataBlock[var3];
      var3 = 0;

      int var4;
      int var5;
      for(var2 = 0; var3 < var12.length; ++var3) {
         Version.ECB var10 = var12[var3];

         for(var4 = 0; var4 < var10.getCount(); ++var2) {
            var5 = var10.getDataCodewords();
            var9[var2] = new DataBlock(var5, new byte[var11.getECCodewords() + var5]);
            ++var4;
         }
      }

      int var7 = var9[0].codewords.length - var11.getECCodewords();
      var4 = 0;

      for(var3 = 0; var4 < var7 - 1; ++var4) {
         for(var5 = 0; var5 < var2; ++var3) {
            var9[var5].codewords[var4] = var0[var3];
            ++var5;
         }
      }

      boolean var13;
      if(var1.getVersionNumber() == 24) {
         var13 = true;
      } else {
         var13 = false;
      }

      if(var13) {
         var4 = 8;
      } else {
         var4 = var2;
      }

      int var6;
      for(var6 = 0; var6 < var4; ++var3) {
         var9[var6].codewords[var7 - 1] = var0[var3];
         ++var6;
      }

      int var8 = var9[0].codewords.length;
      var4 = var3;

      for(var3 = var7; var3 < var8; ++var3) {
         for(var6 = 0; var6 < var2; ++var4) {
            if(var13 && var6 > 7) {
               var7 = var3 - 1;
            } else {
               var7 = var3;
            }

            var9[var6].codewords[var7] = var0[var4];
            ++var6;
         }
      }

      if(var4 != var0.length) {
         throw new IllegalArgumentException();
      } else {
         return var9;
      }
   }

   byte[] getCodewords() {
      return this.codewords;
   }

   int getNumDataCodewords() {
      return this.numDataCodewords;
   }
}
