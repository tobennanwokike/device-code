package com.google.zxing.qrcode.decoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Version;

final class DataBlock {
   private final byte[] codewords;
   private final int numDataCodewords;

   private DataBlock(int var1, byte[] var2) {
      this.numDataCodewords = var1;
      this.codewords = var2;
   }

   static DataBlock[] getDataBlocks(byte[] var0, Version var1, ErrorCorrectionLevel var2) {
      if(var0.length != var1.getTotalCodewords()) {
         throw new IllegalArgumentException();
      } else {
         Version.ECBlocks var13 = var1.getECBlocksForLevel(var2);
         Version.ECB[] var11 = var13.getECBlocks();
         int var3 = 0;

         int var4;
         for(var4 = 0; var3 < var11.length; ++var3) {
            var4 += var11[var3].getCount();
         }

         DataBlock[] var10 = new DataBlock[var4];
         var3 = 0;

         int var5;
         int var6;
         for(var5 = 0; var3 < var11.length; ++var3) {
            Version.ECB var12 = var11[var3];

            for(var4 = 0; var4 < var12.getCount(); ++var5) {
               var6 = var12.getDataCodewords();
               var10[var5] = new DataBlock(var6, new byte[var13.getECCodewordsPerBlock() + var6]);
               ++var4;
            }
         }

         var4 = var10[0].codewords.length;

         for(var3 = var10.length - 1; var3 >= 0 && var10[var3].codewords.length != var4; --var3) {
            ;
         }

         int var8 = var3 + 1;
         int var7 = var4 - var13.getECCodewordsPerBlock();
         var4 = 0;

         for(var3 = 0; var4 < var7; ++var4) {
            for(var6 = 0; var6 < var5; ++var3) {
               var10[var6].codewords[var4] = var0[var3];
               ++var6;
            }
         }

         var6 = var8;

         for(var4 = var3; var6 < var5; ++var4) {
            var10[var6].codewords[var7] = var0[var4];
            ++var6;
         }

         int var9 = var10[0].codewords.length;

         for(var3 = var7; var3 < var9; ++var3) {
            for(var6 = 0; var6 < var5; ++var4) {
               if(var6 < var8) {
                  var7 = var3;
               } else {
                  var7 = var3 + 1;
               }

               var10[var6].codewords[var7] = var0[var4];
               ++var6;
            }
         }

         return var10;
      }
   }

   byte[] getCodewords() {
      return this.codewords;
   }

   int getNumDataCodewords() {
      return this.numDataCodewords;
   }
}
