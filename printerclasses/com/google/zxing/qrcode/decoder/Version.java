package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.FormatInformation;

public final class Version {
   private static final Version[] VERSIONS = buildVersions();
   private static final int[] VERSION_DECODE_INFO = new int[]{31892, '薼', '骙', 'ꓓ', '믶', '읢', '\ud847', '\ue60d', '廊', 68472, 70749, 76311, 79154, 84390, 87683, 92361, 96236, 102084, 102881, 110507, 110734, 117786, 119615, 126325, 127568, 133589, 136944, 141498, 145311, 150283, 152622, 158308, 161089, 167017};
   private final int[] alignmentPatternCenters;
   private final Version.ECBlocks[] ecBlocks;
   private final int totalCodewords;
   private final int versionNumber;

   private Version(int var1, int[] var2, Version.ECBlocks var3, Version.ECBlocks var4, Version.ECBlocks var5, Version.ECBlocks var6) {
      byte var8 = 0;
      super();
      this.versionNumber = var1;
      this.alignmentPatternCenters = var2;
      this.ecBlocks = new Version.ECBlocks[]{var3, var4, var5, var6};
      int var9 = var3.getECCodewordsPerBlock();
      Version.ECB[] var11 = var3.getECBlocks();
      int var7 = 0;

      for(var1 = var8; var1 < var11.length; ++var1) {
         Version.ECB var10 = var11[var1];
         int var12 = var10.getCount();
         var7 += (var10.getDataCodewords() + var9) * var12;
      }

      this.totalCodewords = var7;
   }

   private static Version[] buildVersions() {
      Version.ECBlocks var3 = new Version.ECBlocks(7, new Version.ECB(1, 19));
      Version.ECBlocks var0 = new Version.ECBlocks(10, new Version.ECB(1, 16));
      Version.ECBlocks var2 = new Version.ECBlocks(13, new Version.ECB(1, 13));
      Version.ECBlocks var1 = new Version.ECBlocks(17, new Version.ECB(1, 9));
      Version var43 = new Version(1, new int[0], var3, var0, var2, var1);
      var3 = new Version.ECBlocks(10, new Version.ECB(1, 34));
      var1 = new Version.ECBlocks(16, new Version.ECB(1, 28));
      var2 = new Version.ECBlocks(22, new Version.ECB(1, 22));
      Version.ECBlocks var4 = new Version.ECBlocks(28, new Version.ECB(1, 16));
      Version var44 = new Version(2, new int[]{6, 18}, var3, var1, var2, var4);
      var4 = new Version.ECBlocks(15, new Version.ECB(1, 55));
      Version.ECBlocks var5 = new Version.ECBlocks(26, new Version.ECB(1, 44));
      var3 = new Version.ECBlocks(18, new Version.ECB(2, 17));
      var2 = new Version.ECBlocks(22, new Version.ECB(2, 13));
      Version var45 = new Version(3, new int[]{6, 22}, var4, var5, var3, var2);
      var5 = new Version.ECBlocks(20, new Version.ECB(1, 80));
      var3 = new Version.ECBlocks(18, new Version.ECB(2, 32));
      Version.ECBlocks var6 = new Version.ECBlocks(26, new Version.ECB(2, 24));
      var4 = new Version.ECBlocks(16, new Version.ECB(4, 9));
      Version var46 = new Version(4, new int[]{6, 26}, var5, var3, var6, var4);
      var5 = new Version.ECBlocks(26, new Version.ECB(1, 108));
      var4 = new Version.ECBlocks(24, new Version.ECB(2, 43));
      Version.ECBlocks var7 = new Version.ECBlocks(18, new Version.ECB(2, 15), new Version.ECB(2, 16));
      var6 = new Version.ECBlocks(22, new Version.ECB(2, 11), new Version.ECB(2, 12));
      Version var47 = new Version(5, new int[]{6, 30}, var5, var4, var7, var6);
      var5 = new Version.ECBlocks(18, new Version.ECB(2, 68));
      var7 = new Version.ECBlocks(16, new Version.ECB(4, 27));
      Version.ECBlocks var8 = new Version.ECBlocks(24, new Version.ECB(4, 19));
      var6 = new Version.ECBlocks(28, new Version.ECB(4, 15));
      Version var48 = new Version(6, new int[]{6, 34}, var5, var7, var8, var6);
      Version.ECBlocks var9 = new Version.ECBlocks(20, new Version.ECB(2, 78));
      var8 = new Version.ECBlocks(18, new Version.ECB(4, 31));
      var6 = new Version.ECBlocks(18, new Version.ECB(2, 14), new Version.ECB(4, 15));
      var7 = new Version.ECBlocks(26, new Version.ECB(4, 13), new Version.ECB(1, 14));
      Version var49 = new Version(7, new int[]{6, 22, 38}, var9, var8, var6, var7);
      Version.ECBlocks var10 = new Version.ECBlocks(24, new Version.ECB(2, 97));
      var7 = new Version.ECBlocks(22, new Version.ECB(2, 38), new Version.ECB(2, 39));
      var9 = new Version.ECBlocks(22, new Version.ECB(4, 18), new Version.ECB(2, 19));
      var8 = new Version.ECBlocks(26, new Version.ECB(4, 14), new Version.ECB(2, 15));
      Version var50 = new Version(8, new int[]{6, 24, 42}, var10, var7, var9, var8);
      var8 = new Version.ECBlocks(30, new Version.ECB(2, 116));
      var10 = new Version.ECBlocks(22, new Version.ECB(3, 36), new Version.ECB(2, 37));
      Version.ECBlocks var11 = new Version.ECBlocks(20, new Version.ECB(4, 16), new Version.ECB(4, 17));
      var9 = new Version.ECBlocks(24, new Version.ECB(4, 12), new Version.ECB(4, 13));
      Version var51 = new Version(9, new int[]{6, 26, 46}, var8, var10, var11, var9);
      var9 = new Version.ECBlocks(18, new Version.ECB(2, 68), new Version.ECB(2, 69));
      var10 = new Version.ECBlocks(26, new Version.ECB(4, 43), new Version.ECB(1, 44));
      var11 = new Version.ECBlocks(24, new Version.ECB(6, 19), new Version.ECB(2, 20));
      Version.ECBlocks var12 = new Version.ECBlocks(28, new Version.ECB(6, 15), new Version.ECB(2, 16));
      Version var52 = new Version(10, new int[]{6, 28, 50}, var9, var10, var11, var12);
      Version.ECBlocks var13 = new Version.ECBlocks(20, new Version.ECB(4, 81));
      var12 = new Version.ECBlocks(30, new Version.ECB(1, 50), new Version.ECB(4, 51));
      var10 = new Version.ECBlocks(28, new Version.ECB(4, 22), new Version.ECB(4, 23));
      var11 = new Version.ECBlocks(24, new Version.ECB(3, 12), new Version.ECB(8, 13));
      Version var53 = new Version(11, new int[]{6, 30, 54}, var13, var12, var10, var11);
      var11 = new Version.ECBlocks(24, new Version.ECB(2, 92), new Version.ECB(2, 93));
      var12 = new Version.ECBlocks(22, new Version.ECB(6, 36), new Version.ECB(2, 37));
      Version.ECBlocks var14 = new Version.ECBlocks(26, new Version.ECB(4, 20), new Version.ECB(6, 21));
      var13 = new Version.ECBlocks(28, new Version.ECB(7, 14), new Version.ECB(4, 15));
      Version var54 = new Version(12, new int[]{6, 32, 58}, var11, var12, var14, var13);
      var14 = new Version.ECBlocks(26, new Version.ECB(4, 107));
      Version.ECBlocks var15 = new Version.ECBlocks(22, new Version.ECB(8, 37), new Version.ECB(1, 38));
      var13 = new Version.ECBlocks(24, new Version.ECB(8, 20), new Version.ECB(4, 21));
      var12 = new Version.ECBlocks(22, new Version.ECB(12, 11), new Version.ECB(4, 12));
      Version var55 = new Version(13, new int[]{6, 34, 62}, var14, var15, var13, var12);
      Version.ECBlocks var16 = new Version.ECBlocks(30, new Version.ECB(3, 115), new Version.ECB(1, 116));
      var15 = new Version.ECBlocks(24, new Version.ECB(4, 40), new Version.ECB(5, 41));
      var13 = new Version.ECBlocks(20, new Version.ECB(11, 16), new Version.ECB(5, 17));
      var14 = new Version.ECBlocks(24, new Version.ECB(11, 12), new Version.ECB(5, 13));
      Version var56 = new Version(14, new int[]{6, 26, 46, 66}, var16, var15, var13, var14);
      var16 = new Version.ECBlocks(22, new Version.ECB(5, 87), new Version.ECB(1, 88));
      var14 = new Version.ECBlocks(24, new Version.ECB(5, 41), new Version.ECB(5, 42));
      Version.ECBlocks var17 = new Version.ECBlocks(30, new Version.ECB(5, 24), new Version.ECB(7, 25));
      var15 = new Version.ECBlocks(24, new Version.ECB(11, 12), new Version.ECB(7, 13));
      Version var57 = new Version(15, new int[]{6, 26, 48, 70}, var16, var14, var17, var15);
      Version.ECBlocks var18 = new Version.ECBlocks(24, new Version.ECB(5, 98), new Version.ECB(1, 99));
      var16 = new Version.ECBlocks(28, new Version.ECB(7, 45), new Version.ECB(3, 46));
      var15 = new Version.ECBlocks(24, new Version.ECB(15, 19), new Version.ECB(2, 20));
      var17 = new Version.ECBlocks(30, new Version.ECB(3, 15), new Version.ECB(13, 16));
      Version var58 = new Version(16, new int[]{6, 26, 50, 74}, var18, var16, var15, var17);
      var16 = new Version.ECBlocks(28, new Version.ECB(1, 107), new Version.ECB(5, 108));
      Version.ECBlocks var19 = new Version.ECBlocks(28, new Version.ECB(10, 46), new Version.ECB(1, 47));
      var17 = new Version.ECBlocks(28, new Version.ECB(1, 22), new Version.ECB(15, 23));
      var18 = new Version.ECBlocks(28, new Version.ECB(2, 14), new Version.ECB(17, 15));
      Version var59 = new Version(17, new int[]{6, 30, 54, 78}, var16, var19, var17, var18);
      var18 = new Version.ECBlocks(30, new Version.ECB(5, 120), new Version.ECB(1, 121));
      Version.ECBlocks var20 = new Version.ECBlocks(26, new Version.ECB(9, 43), new Version.ECB(4, 44));
      var17 = new Version.ECBlocks(28, new Version.ECB(17, 22), new Version.ECB(1, 23));
      var19 = new Version.ECBlocks(28, new Version.ECB(2, 14), new Version.ECB(19, 15));
      Version var60 = new Version(18, new int[]{6, 30, 56, 82}, var18, var20, var17, var19);
      var19 = new Version.ECBlocks(28, new Version.ECB(3, 113), new Version.ECB(4, 114));
      var18 = new Version.ECBlocks(26, new Version.ECB(3, 44), new Version.ECB(11, 45));
      Version.ECBlocks var21 = new Version.ECBlocks(26, new Version.ECB(17, 21), new Version.ECB(4, 22));
      var20 = new Version.ECBlocks(26, new Version.ECB(9, 13), new Version.ECB(16, 14));
      Version var61 = new Version(19, new int[]{6, 30, 58, 86}, var19, var18, var21, var20);
      var21 = new Version.ECBlocks(28, new Version.ECB(3, 107), new Version.ECB(5, 108));
      Version.ECBlocks var22 = new Version.ECBlocks(26, new Version.ECB(3, 41), new Version.ECB(13, 42));
      var20 = new Version.ECBlocks(30, new Version.ECB(15, 24), new Version.ECB(5, 25));
      var19 = new Version.ECBlocks(28, new Version.ECB(15, 15), new Version.ECB(10, 16));
      Version var62 = new Version(20, new int[]{6, 34, 62, 90}, var21, var22, var20, var19);
      var21 = new Version.ECBlocks(28, new Version.ECB(4, 116), new Version.ECB(4, 117));
      var20 = new Version.ECBlocks(26, new Version.ECB(17, 42));
      var22 = new Version.ECBlocks(28, new Version.ECB(17, 22), new Version.ECB(6, 23));
      Version.ECBlocks var23 = new Version.ECBlocks(30, new Version.ECB(19, 16), new Version.ECB(6, 17));
      Version var63 = new Version(21, new int[]{6, 28, 50, 72, 94}, var21, var20, var22, var23);
      var22 = new Version.ECBlocks(28, new Version.ECB(2, 111), new Version.ECB(7, 112));
      Version.ECBlocks var24 = new Version.ECBlocks(28, new Version.ECB(17, 46));
      var21 = new Version.ECBlocks(30, new Version.ECB(7, 24), new Version.ECB(16, 25));
      var23 = new Version.ECBlocks(24, new Version.ECB(34, 13));
      Version var64 = new Version(22, new int[]{6, 26, 50, 74, 98}, var22, var24, var21, var23);
      var22 = new Version.ECBlocks(30, new Version.ECB(4, 121), new Version.ECB(5, 122));
      var23 = new Version.ECBlocks(28, new Version.ECB(4, 47), new Version.ECB(14, 48));
      Version.ECBlocks var25 = new Version.ECBlocks(30, new Version.ECB(11, 24), new Version.ECB(14, 25));
      var24 = new Version.ECBlocks(30, new Version.ECB(16, 15), new Version.ECB(14, 16));
      Version var65 = new Version(23, new int[]{6, 30, 54, 78, 102}, var22, var23, var25, var24);
      var24 = new Version.ECBlocks(30, new Version.ECB(6, 117), new Version.ECB(4, 118));
      Version.ECBlocks var26 = new Version.ECBlocks(28, new Version.ECB(6, 45), new Version.ECB(14, 46));
      var25 = new Version.ECBlocks(30, new Version.ECB(11, 24), new Version.ECB(16, 25));
      var23 = new Version.ECBlocks(30, new Version.ECB(30, 16), new Version.ECB(2, 17));
      Version var66 = new Version(24, new int[]{6, 28, 54, 80, 106}, var24, var26, var25, var23);
      var26 = new Version.ECBlocks(26, new Version.ECB(8, 106), new Version.ECB(4, 107));
      var24 = new Version.ECBlocks(28, new Version.ECB(8, 47), new Version.ECB(13, 48));
      Version.ECBlocks var27 = new Version.ECBlocks(30, new Version.ECB(7, 24), new Version.ECB(22, 25));
      var25 = new Version.ECBlocks(30, new Version.ECB(22, 15), new Version.ECB(13, 16));
      Version var67 = new Version(25, new int[]{6, 32, 58, 84, 110}, var26, var24, var27, var25);
      var27 = new Version.ECBlocks(28, new Version.ECB(10, 114), new Version.ECB(2, 115));
      var25 = new Version.ECBlocks(28, new Version.ECB(19, 46), new Version.ECB(4, 47));
      var26 = new Version.ECBlocks(28, new Version.ECB(28, 22), new Version.ECB(6, 23));
      Version.ECBlocks var28 = new Version.ECBlocks(30, new Version.ECB(33, 16), new Version.ECB(4, 17));
      Version var68 = new Version(26, new int[]{6, 30, 58, 86, 114}, var27, var25, var26, var28);
      var27 = new Version.ECBlocks(30, new Version.ECB(8, 122), new Version.ECB(4, 123));
      var28 = new Version.ECBlocks(28, new Version.ECB(22, 45), new Version.ECB(3, 46));
      var26 = new Version.ECBlocks(30, new Version.ECB(8, 23), new Version.ECB(26, 24));
      Version.ECBlocks var29 = new Version.ECBlocks(30, new Version.ECB(12, 15), new Version.ECB(28, 16));
      Version var69 = new Version(27, new int[]{6, 34, 62, 90, 118}, var27, var28, var26, var29);
      Version.ECBlocks var30 = new Version.ECBlocks(30, new Version.ECB(3, 117), new Version.ECB(10, 118));
      var28 = new Version.ECBlocks(28, new Version.ECB(3, 45), new Version.ECB(23, 46));
      var29 = new Version.ECBlocks(30, new Version.ECB(4, 24), new Version.ECB(31, 25));
      var27 = new Version.ECBlocks(30, new Version.ECB(11, 15), new Version.ECB(31, 16));
      Version var70 = new Version(28, new int[]{6, 26, 50, 74, 98, 122}, var30, var28, var29, var27);
      var30 = new Version.ECBlocks(30, new Version.ECB(7, 116), new Version.ECB(7, 117));
      var29 = new Version.ECBlocks(28, new Version.ECB(21, 45), new Version.ECB(7, 46));
      Version.ECBlocks var31 = new Version.ECBlocks(30, new Version.ECB(1, 23), new Version.ECB(37, 24));
      var28 = new Version.ECBlocks(30, new Version.ECB(19, 15), new Version.ECB(26, 16));
      Version var71 = new Version(29, new int[]{6, 30, 54, 78, 102, 126}, var30, var29, var31, var28);
      var30 = new Version.ECBlocks(30, new Version.ECB(5, 115), new Version.ECB(10, 116));
      var29 = new Version.ECBlocks(28, new Version.ECB(19, 47), new Version.ECB(10, 48));
      var31 = new Version.ECBlocks(30, new Version.ECB(15, 24), new Version.ECB(25, 25));
      Version.ECBlocks var32 = new Version.ECBlocks(30, new Version.ECB(23, 15), new Version.ECB(25, 16));
      Version var72 = new Version(30, new int[]{6, 26, 52, 78, 104, 130}, var30, var29, var31, var32);
      var31 = new Version.ECBlocks(30, new Version.ECB(13, 115), new Version.ECB(3, 116));
      var30 = new Version.ECBlocks(28, new Version.ECB(2, 46), new Version.ECB(29, 47));
      var32 = new Version.ECBlocks(30, new Version.ECB(42, 24), new Version.ECB(1, 25));
      Version.ECBlocks var33 = new Version.ECBlocks(30, new Version.ECB(23, 15), new Version.ECB(28, 16));
      Version var73 = new Version(31, new int[]{6, 30, 56, 82, 108, 134}, var31, var30, var32, var33);
      var32 = new Version.ECBlocks(30, new Version.ECB(17, 115));
      var31 = new Version.ECBlocks(28, new Version.ECB(10, 46), new Version.ECB(23, 47));
      Version.ECBlocks var34 = new Version.ECBlocks(30, new Version.ECB(10, 24), new Version.ECB(35, 25));
      var33 = new Version.ECBlocks(30, new Version.ECB(19, 15), new Version.ECB(35, 16));
      Version var74 = new Version(32, new int[]{6, 34, 60, 86, 112, 138}, var32, var31, var34, var33);
      Version.ECBlocks var35 = new Version.ECBlocks(30, new Version.ECB(17, 115), new Version.ECB(1, 116));
      var34 = new Version.ECBlocks(28, new Version.ECB(14, 46), new Version.ECB(21, 47));
      var32 = new Version.ECBlocks(30, new Version.ECB(29, 24), new Version.ECB(19, 25));
      var33 = new Version.ECBlocks(30, new Version.ECB(11, 15), new Version.ECB(46, 16));
      Version var75 = new Version(33, new int[]{6, 30, 58, 86, 114, 142}, var35, var34, var32, var33);
      var33 = new Version.ECBlocks(30, new Version.ECB(13, 115), new Version.ECB(6, 116));
      var34 = new Version.ECBlocks(28, new Version.ECB(14, 46), new Version.ECB(23, 47));
      Version.ECBlocks var36 = new Version.ECBlocks(30, new Version.ECB(44, 24), new Version.ECB(7, 25));
      var35 = new Version.ECBlocks(30, new Version.ECB(59, 16), new Version.ECB(1, 17));
      Version var76 = new Version(34, new int[]{6, 34, 62, 90, 118, 146}, var33, var34, var36, var35);
      var34 = new Version.ECBlocks(30, new Version.ECB(12, 121), new Version.ECB(7, 122));
      Version.ECBlocks var37 = new Version.ECBlocks(28, new Version.ECB(12, 47), new Version.ECB(26, 48));
      var35 = new Version.ECBlocks(30, new Version.ECB(39, 24), new Version.ECB(14, 25));
      var36 = new Version.ECBlocks(30, new Version.ECB(22, 15), new Version.ECB(41, 16));
      Version var77 = new Version(35, new int[]{6, 30, 54, 78, 102, 126, 150}, var34, var37, var35, var36);
      Version.ECBlocks var38 = new Version.ECBlocks(30, new Version.ECB(6, 121), new Version.ECB(14, 122));
      var35 = new Version.ECBlocks(28, new Version.ECB(6, 47), new Version.ECB(34, 48));
      var36 = new Version.ECBlocks(30, new Version.ECB(46, 24), new Version.ECB(10, 25));
      var37 = new Version.ECBlocks(30, new Version.ECB(2, 15), new Version.ECB(64, 16));
      Version var78 = new Version(36, new int[]{6, 24, 50, 76, 102, 128, 154}, var38, var35, var36, var37);
      Version.ECBlocks var39 = new Version.ECBlocks(30, new Version.ECB(17, 122), new Version.ECB(4, 123));
      var38 = new Version.ECBlocks(28, new Version.ECB(29, 46), new Version.ECB(14, 47));
      var36 = new Version.ECBlocks(30, new Version.ECB(49, 24), new Version.ECB(10, 25));
      var37 = new Version.ECBlocks(30, new Version.ECB(24, 15), new Version.ECB(46, 16));
      Version var79 = new Version(37, new int[]{6, 28, 54, 80, 106, 132, 158}, var39, var38, var36, var37);
      var37 = new Version.ECBlocks(30, new Version.ECB(4, 122), new Version.ECB(18, 123));
      var39 = new Version.ECBlocks(28, new Version.ECB(13, 46), new Version.ECB(32, 47));
      var38 = new Version.ECBlocks(30, new Version.ECB(48, 24), new Version.ECB(14, 25));
      Version.ECBlocks var40 = new Version.ECBlocks(30, new Version.ECB(42, 15), new Version.ECB(32, 16));
      Version var80 = new Version(38, new int[]{6, 32, 58, 84, 110, 136, 162}, var37, var39, var38, var40);
      var38 = new Version.ECBlocks(30, new Version.ECB(20, 117), new Version.ECB(4, 118));
      Version.ECBlocks var41 = new Version.ECBlocks(28, new Version.ECB(40, 47), new Version.ECB(7, 48));
      var40 = new Version.ECBlocks(30, new Version.ECB(43, 24), new Version.ECB(22, 25));
      var39 = new Version.ECBlocks(30, new Version.ECB(10, 15), new Version.ECB(67, 16));
      Version var81 = new Version(39, new int[]{6, 26, 54, 82, 110, 138, 166}, var38, var41, var40, var39);
      var38 = new Version.ECBlocks(30, new Version.ECB(19, 118), new Version.ECB(6, 119));
      Version.ECBlocks var42 = new Version.ECBlocks(28, new Version.ECB(18, 47), new Version.ECB(31, 48));
      var41 = new Version.ECBlocks(30, new Version.ECB(34, 24), new Version.ECB(34, 25));
      var39 = new Version.ECBlocks(30, new Version.ECB(20, 15), new Version.ECB(61, 16));
      return new Version[]{var43, var44, var45, var46, var47, var48, var49, var50, var51, var52, var53, var54, var55, var56, var57, var58, var59, var60, var61, var62, var63, var64, var65, var66, var67, var68, var69, var70, var71, var72, var73, var74, var75, var76, var77, var78, var79, var80, var81, new Version(40, new int[]{6, 30, 58, 86, 114, 142, 170}, var38, var42, var41, var39)};
   }

   static Version decodeVersionInformation(int var0) {
      int var2 = 0;
      int var1 = Integer.MAX_VALUE;
      int var4 = 0;

      Version var6;
      while(true) {
         if(var2 >= VERSION_DECODE_INFO.length) {
            if(var1 <= 3) {
               var6 = getVersionForNumber(var4);
            } else {
               var6 = null;
            }
            break;
         }

         int var3 = VERSION_DECODE_INFO[var2];
         if(var3 == var0) {
            var6 = getVersionForNumber(var2 + 7);
            break;
         }

         int var5 = FormatInformation.numBitsDiffering(var0, var3);
         var3 = var1;
         if(var5 < var1) {
            var4 = var2 + 7;
            var3 = var5;
         }

         ++var2;
         var1 = var3;
      }

      return var6;
   }

   public static Version getProvisionalVersionForDimension(int var0) throws FormatException {
      if(var0 % 4 != 1) {
         throw FormatException.getFormatInstance();
      } else {
         try {
            Version var1 = getVersionForNumber(var0 - 17 >> 2);
            return var1;
         } catch (IllegalArgumentException var2) {
            throw FormatException.getFormatInstance();
         }
      }
   }

   public static Version getVersionForNumber(int var0) {
      if(var0 >= 1 && var0 <= 40) {
         return VERSIONS[var0 - 1];
      } else {
         throw new IllegalArgumentException();
      }
   }

   BitMatrix buildFunctionPattern() {
      int var3 = this.getDimensionForVersion();
      BitMatrix var6 = new BitMatrix(var3);
      var6.setRegion(0, 0, 9, 9);
      var6.setRegion(var3 - 8, 0, 8, 9);
      var6.setRegion(0, var3 - 8, 9, 8);
      int var4 = this.alignmentPatternCenters.length;

      for(int var1 = 0; var1 < var4; ++var1) {
         int var5 = this.alignmentPatternCenters[var1];

         for(int var2 = 0; var2 < var4; ++var2) {
            if((var1 != 0 || var2 != 0 && var2 != var4 - 1) && (var1 != var4 - 1 || var2 != 0)) {
               var6.setRegion(this.alignmentPatternCenters[var2] - 2, var5 - 2, 5, 5);
            }
         }
      }

      var6.setRegion(6, 9, 1, var3 - 17);
      var6.setRegion(9, 6, var3 - 17, 1);
      if(this.versionNumber > 6) {
         var6.setRegion(var3 - 11, 0, 3, 6);
         var6.setRegion(0, var3 - 11, 6, 3);
      }

      return var6;
   }

   public int[] getAlignmentPatternCenters() {
      return this.alignmentPatternCenters;
   }

   public int getDimensionForVersion() {
      return this.versionNumber * 4 + 17;
   }

   public Version.ECBlocks getECBlocksForLevel(ErrorCorrectionLevel var1) {
      return this.ecBlocks[var1.ordinal()];
   }

   public int getTotalCodewords() {
      return this.totalCodewords;
   }

   public int getVersionNumber() {
      return this.versionNumber;
   }

   public String toString() {
      return String.valueOf(this.versionNumber);
   }

   public static final class ECB {
      private final int count;
      private final int dataCodewords;

      ECB(int var1, int var2) {
         this.count = var1;
         this.dataCodewords = var2;
      }

      public int getCount() {
         return this.count;
      }

      public int getDataCodewords() {
         return this.dataCodewords;
      }
   }

   public static final class ECBlocks {
      private final Version.ECB[] ecBlocks;
      private final int ecCodewordsPerBlock;

      ECBlocks(int var1, Version.ECB var2) {
         this.ecCodewordsPerBlock = var1;
         this.ecBlocks = new Version.ECB[]{var2};
      }

      ECBlocks(int var1, Version.ECB var2, Version.ECB var3) {
         this.ecCodewordsPerBlock = var1;
         this.ecBlocks = new Version.ECB[]{var2, var3};
      }

      public Version.ECB[] getECBlocks() {
         return this.ecBlocks;
      }

      public int getECCodewordsPerBlock() {
         return this.ecCodewordsPerBlock;
      }

      public int getNumBlocks() {
         int var1 = 0;

         int var2;
         for(var2 = 0; var1 < this.ecBlocks.length; ++var1) {
            var2 += this.ecBlocks[var1].getCount();
         }

         return var2;
      }

      public int getTotalECCodewords() {
         return this.ecCodewordsPerBlock * this.getNumBlocks();
      }
   }
}
