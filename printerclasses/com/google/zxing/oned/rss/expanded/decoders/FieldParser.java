package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;

final class FieldParser {
   private static final Object[][] FOUR_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
   private static final Object[][] TWO_DIGIT_DATA_LENGTH;
   private static final Object VARIABLE_LENGTH = new Object();

   static {
      Object[] var9 = new Object[]{"00", new Integer(18)};
      Object[] var10 = new Object[]{"01", new Integer(14)};
      Object[] var11 = new Object[]{"02", new Integer(14)};
      Object[] var12 = new Object[]{"10", VARIABLE_LENGTH, new Integer(20)};
      Object[] var13 = new Object[]{"11", new Integer(6)};
      Object[] var14 = new Object[]{"12", new Integer(6)};
      Integer var7 = new Integer(6);
      Object[] var15 = new Object[]{"15", new Integer(6)};
      Object[] var16 = new Object[]{"17", new Integer(6)};
      Object[] var17 = new Object[]{"20", new Integer(2)};
      Object[] var18 = new Object[]{"21", VARIABLE_LENGTH, new Integer(20)};
      Object var3 = VARIABLE_LENGTH;
      Integer var4 = new Integer(29);
      Object var0 = VARIABLE_LENGTH;
      Integer var1 = new Integer(8);
      Object var2 = VARIABLE_LENGTH;
      Integer var8 = new Integer(8);
      Object[] var19 = new Object[]{"90", VARIABLE_LENGTH, new Integer(30)};
      Object[] var20 = new Object[]{"91", VARIABLE_LENGTH, new Integer(30)};
      Object[] var21 = new Object[]{"92", VARIABLE_LENGTH, new Integer(30)};
      Object[] var22 = new Object[]{"93", VARIABLE_LENGTH, new Integer(30)};
      Object var5 = VARIABLE_LENGTH;
      Integer var6 = new Integer(30);
      Object[] var23 = new Object[]{"95", VARIABLE_LENGTH, new Integer(30)};
      Object[] var24 = new Object[]{"96", VARIABLE_LENGTH, new Integer(30)};
      Object[] var25 = new Object[]{"97", VARIABLE_LENGTH, new Integer(30)};
      Object[] var26 = new Object[]{"98", VARIABLE_LENGTH, new Integer(30)};
      Object[] var27 = new Object[]{"99", VARIABLE_LENGTH, new Integer(30)};
      TWO_DIGIT_DATA_LENGTH = new Object[][]{var9, var10, var11, var12, var13, var14, {"13", var7}, var15, var16, var17, var18, {"22", var3, var4}, {"30", var0, var1}, {"37", var2, var8}, var19, var20, var21, var22, {"94", var5, var6}, var23, var24, var25, var26, var27};
      var21 = new Object[]{"240", VARIABLE_LENGTH, new Integer(30)};
      var22 = new Object[]{"241", VARIABLE_LENGTH, new Integer(30)};
      Object var68 = VARIABLE_LENGTH;
      var4 = new Integer(6);
      var23 = new Object[]{"250", VARIABLE_LENGTH, new Integer(30)};
      Object var84 = VARIABLE_LENGTH;
      var1 = new Integer(30);
      var24 = new Object[]{"253", VARIABLE_LENGTH, new Integer(17)};
      Object var86 = VARIABLE_LENGTH;
      Integer var72 = new Integer(20);
      var3 = VARIABLE_LENGTH;
      Integer var70 = new Integer(30);
      var25 = new Object[]{"401", VARIABLE_LENGTH, new Integer(30)};
      Integer var60 = new Integer(17);
      var5 = VARIABLE_LENGTH;
      var7 = new Integer(30);
      var6 = new Integer(13);
      Integer var79 = new Integer(13);
      Integer var69 = new Integer(13);
      Integer var77 = new Integer(13);
      var26 = new Object[]{"414", new Integer(13)};
      Object var75 = VARIABLE_LENGTH;
      Integer var74 = new Integer(20);
      var2 = VARIABLE_LENGTH;
      Integer var82 = new Integer(15);
      var27 = new Object[]{"422", new Integer(3)};
      Object[] var28 = new Object[]{"423", VARIABLE_LENGTH, new Integer(15)};
      Integer var71 = new Integer(3);
      Integer var80 = new Integer(3);
      Object[] var29 = new Object[]{"426", new Integer(3)};
      THREE_DIGIT_DATA_LENGTH = new Object[][]{var21, var22, {"242", var68, var4}, var23, {"251", var84, var1}, var24, {"254", var86, var72}, {"400", var3, var70}, var25, {"402", var60}, {"403", var5, var7}, {"410", var6}, {"411", var79}, {"412", var69}, {"413", var77}, var26, {"420", var75, var74}, {"421", var2, var82}, var27, var28, {"424", var71}, {"425", var80}, var29};
      Object[] var30 = new Object[]{"310", new Integer(6)};
      Object[] var31 = new Object[]{"311", new Integer(6)};
      Object[] var32 = new Object[]{"312", new Integer(6)};
      Object[] var33 = new Object[]{"313", new Integer(6)};
      Integer var85 = new Integer(6);
      Object[] var34 = new Object[]{"315", new Integer(6)};
      Object[] var35 = new Object[]{"316", new Integer(6)};
      var74 = new Integer(6);
      Object[] var36 = new Object[]{"321", new Integer(6)};
      var72 = new Integer(6);
      Object[] var37 = new Object[]{"323", new Integer(6)};
      var69 = new Integer(6);
      Object[] var38 = new Object[]{"325", new Integer(6)};
      Object[] var39 = new Object[]{"326", new Integer(6)};
      Integer var89 = new Integer(6);
      Integer var95 = new Integer(6);
      var8 = new Integer(6);
      Object[] var40 = new Object[]{"330", new Integer(6)};
      Integer var76 = new Integer(6);
      Object[] var41 = new Object[]{"332", new Integer(6)};
      Integer var94 = new Integer(6);
      Object[] var42 = new Object[]{"334", new Integer(6)};
      Object[] var43 = new Object[]{"335", new Integer(6)};
      Integer var90 = new Integer(6);
      Object[] var44 = new Object[]{"340", new Integer(6)};
      var60 = new Integer(6);
      var1 = new Integer(6);
      Integer var65 = new Integer(6);
      Object[] var45 = new Object[]{"344", new Integer(6)};
      Integer var88 = new Integer(6);
      Integer var91 = new Integer(6);
      Object[] var46 = new Object[]{"347", new Integer(6)};
      Object[] var47 = new Object[]{"348", new Integer(6)};
      var7 = new Integer(6);
      Integer var92 = new Integer(6);
      var80 = new Integer(6);
      Object[] var48 = new Object[]{"352", new Integer(6)};
      var70 = new Integer(6);
      Integer var93 = new Integer(6);
      Object[] var49 = new Object[]{"355", new Integer(6)};
      Integer var87 = new Integer(6);
      Object[] var50 = new Object[]{"357", new Integer(6)};
      Integer var63 = new Integer(6);
      Object[] var51 = new Object[]{"361", new Integer(6)};
      Object[] var52 = new Object[]{"362", new Integer(6)};
      var79 = new Integer(6);
      Object[] var53 = new Object[]{"364", new Integer(6)};
      Object[] var54 = new Object[]{"365", new Integer(6)};
      Object[] var55 = new Object[]{"366", new Integer(6)};
      Object[] var56 = new Object[]{"367", new Integer(6)};
      Integer var96 = new Integer(6);
      Object[] var57 = new Object[]{"369", new Integer(6)};
      Object[] var58 = new Object[]{"390", VARIABLE_LENGTH, new Integer(15)};
      Object var64 = VARIABLE_LENGTH;
      Integer var62 = new Integer(18);
      Object var78 = VARIABLE_LENGTH;
      var6 = new Integer(15);
      Object var83 = VARIABLE_LENGTH;
      var71 = new Integer(18);
      Object[] var59 = new Object[]{"703", VARIABLE_LENGTH, new Integer(30)};
      THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH = new Object[][]{var30, var31, var32, var33, {"314", var85}, var34, var35, {"320", var74}, var36, {"322", var72}, var37, {"324", var69}, var38, var39, {"327", var89}, {"328", var95}, {"329", var8}, var40, {"331", var76}, var41, {"333", var94}, var42, var43, {"336", var90}, var44, {"341", var60}, {"342", var1}, {"343", var65}, var45, {"345", var88}, {"346", var91}, var46, var47, {"349", var7}, {"350", var92}, {"351", var80}, var48, {"353", var70}, {"354", var93}, var49, {"356", var87}, var50, {"360", var63}, var51, var52, {"363", var79}, var53, var54, var55, var56, {"368", var96}, var57, var58, {"391", var64, var62}, {"392", var78, var6}, {"393", var83, var71}, var59};
      var79 = new Integer(13);
      var23 = new Object[]{"7002", VARIABLE_LENGTH, new Integer(30)};
      var71 = new Integer(10);
      var76 = new Integer(14);
      Object var61 = VARIABLE_LENGTH;
      var70 = new Integer(20);
      Object var67 = VARIABLE_LENGTH;
      var63 = new Integer(30);
      Object var81 = VARIABLE_LENGTH;
      var74 = new Integer(30);
      var88 = new Integer(6);
      var65 = new Integer(18);
      var86 = VARIABLE_LENGTH;
      var62 = new Integer(30);
      var0 = VARIABLE_LENGTH;
      var89 = new Integer(12);
      var4 = new Integer(18);
      Object var66 = VARIABLE_LENGTH;
      var77 = new Integer(25);
      var8 = new Integer(6);
      var69 = new Integer(10);
      var82 = new Integer(2);
      Object var73 = VARIABLE_LENGTH;
      var85 = new Integer(30);
      FOUR_DIGIT_DATA_LENGTH = new Object[][]{{"7001", var79}, var23, {"7003", var71}, {"8001", var76}, {"8002", var61, var70}, {"8003", var67, var63}, {"8004", var81, var74}, {"8005", var88}, {"8006", var65}, {"8007", var86, var62}, {"8008", var0, var89}, {"8018", var4}, {"8020", var66, var77}, {"8100", var8}, {"8101", var69}, {"8102", var82}, {"8110", var73, var85}};
   }

   static String parseFieldsInGeneralPurpose(String var0) throws NotFoundException {
      if(var0.length() == 0) {
         var0 = "";
      } else {
         if(var0.length() < 2) {
            throw NotFoundException.getNotFoundInstance();
         }

         String var2 = var0.substring(0, 2);
         int var1 = 0;

         while(true) {
            if(var1 >= TWO_DIGIT_DATA_LENGTH.length) {
               if(var0.length() < 3) {
                  throw NotFoundException.getNotFoundInstance();
               }

               var2 = var0.substring(0, 3);

               for(var1 = 0; var1 < THREE_DIGIT_DATA_LENGTH.length; ++var1) {
                  if(THREE_DIGIT_DATA_LENGTH[var1][0].equals(var2)) {
                     if(THREE_DIGIT_DATA_LENGTH[var1][1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(3, ((Integer)THREE_DIGIT_DATA_LENGTH[var1][2]).intValue(), var0);
                     } else {
                        var0 = processFixedAI(3, ((Integer)THREE_DIGIT_DATA_LENGTH[var1][1]).intValue(), var0);
                     }

                     return var0;
                  }
               }

               for(var1 = 0; var1 < THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.length; ++var1) {
                  if(THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[var1][0].equals(var2)) {
                     if(THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[var1][1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(4, ((Integer)THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[var1][2]).intValue(), var0);
                     } else {
                        var0 = processFixedAI(4, ((Integer)THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[var1][1]).intValue(), var0);
                     }

                     return var0;
                  }
               }

               if(var0.length() < 4) {
                  throw NotFoundException.getNotFoundInstance();
               }

               var2 = var0.substring(0, 4);

               for(var1 = 0; var1 < FOUR_DIGIT_DATA_LENGTH.length; ++var1) {
                  if(FOUR_DIGIT_DATA_LENGTH[var1][0].equals(var2)) {
                     if(FOUR_DIGIT_DATA_LENGTH[var1][1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(4, ((Integer)FOUR_DIGIT_DATA_LENGTH[var1][2]).intValue(), var0);
                     } else {
                        var0 = processFixedAI(4, ((Integer)FOUR_DIGIT_DATA_LENGTH[var1][1]).intValue(), var0);
                     }

                     return var0;
                  }
               }

               throw NotFoundException.getNotFoundInstance();
            }

            if(TWO_DIGIT_DATA_LENGTH[var1][0].equals(var2)) {
               if(TWO_DIGIT_DATA_LENGTH[var1][1] == VARIABLE_LENGTH) {
                  var0 = processVariableAI(2, ((Integer)TWO_DIGIT_DATA_LENGTH[var1][2]).intValue(), var0);
               } else {
                  var0 = processFixedAI(2, ((Integer)TWO_DIGIT_DATA_LENGTH[var1][1]).intValue(), var0);
               }
               break;
            }

            ++var1;
         }
      }

      return var0;
   }

   private static String processFixedAI(int var0, int var1, String var2) throws NotFoundException {
      if(var2.length() < var0) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         String var4 = var2.substring(0, var0);
         if(var2.length() < var0 + var1) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            String var3 = var2.substring(var0, var0 + var1);
            var2 = var2.substring(var0 + var1);
            return '(' + var4 + ')' + var3 + parseFieldsInGeneralPurpose(var2);
         }
      }
   }

   private static String processVariableAI(int var0, int var1, String var2) throws NotFoundException {
      String var3 = var2.substring(0, var0);
      if(var2.length() < var0 + var1) {
         var1 = var2.length();
      } else {
         var1 += var0;
      }

      String var4 = var2.substring(var0, var1);
      var2 = var2.substring(var1);
      return '(' + var3 + ')' + var4 + parseFieldsInGeneralPurpose(var2);
   }
}
