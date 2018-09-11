package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.OneDReader;
import java.util.Hashtable;

public final class Code128Reader extends OneDReader {
   private static final int CODE_CODE_A = 101;
   private static final int CODE_CODE_B = 100;
   private static final int CODE_CODE_C = 99;
   private static final int CODE_FNC_1 = 102;
   private static final int CODE_FNC_2 = 97;
   private static final int CODE_FNC_3 = 96;
   private static final int CODE_FNC_4_A = 101;
   private static final int CODE_FNC_4_B = 100;
   static final int[][] CODE_PATTERNS;
   private static final int CODE_SHIFT = 98;
   private static final int CODE_START_A = 103;
   private static final int CODE_START_B = 104;
   private static final int CODE_START_C = 105;
   private static final int CODE_STOP = 106;
   private static final int MAX_AVG_VARIANCE = 64;
   private static final int MAX_INDIVIDUAL_VARIANCE = 179;

   static {
      int[] var0 = new int[]{2, 2, 2, 1, 2, 2};
      int[] var1 = new int[]{1, 2, 1, 3, 2, 2};
      int[] var2 = new int[]{1, 2, 2, 2, 1, 3};
      int[] var3 = new int[]{1, 3, 2, 2, 1, 2};
      int[] var4 = new int[]{2, 2, 1, 2, 1, 3};
      int[] var5 = new int[]{2, 3, 1, 2, 1, 2};
      int[] var6 = new int[]{1, 1, 2, 2, 3, 2};
      int[] var7 = new int[]{1, 2, 2, 1, 3, 2};
      int[] var8 = new int[]{1, 2, 2, 2, 3, 1};
      int[] var9 = new int[]{1, 2, 3, 1, 2, 2};
      int[] var10 = new int[]{2, 2, 3, 2, 1, 1};
      int[] var11 = new int[]{2, 2, 1, 2, 3, 1};
      int[] var12 = new int[]{2, 1, 3, 2, 1, 2};
      int[] var13 = new int[]{2, 2, 3, 1, 1, 2};
      int[] var14 = new int[]{3, 1, 1, 2, 2, 2};
      int[] var15 = new int[]{3, 2, 1, 2, 2, 1};
      int[] var16 = new int[]{3, 2, 2, 2, 1, 1};
      int[] var17 = new int[]{2, 1, 2, 1, 2, 3};
      int[] var18 = new int[]{2, 1, 2, 3, 2, 1};
      int[] var19 = new int[]{2, 3, 2, 1, 2, 1};
      int[] var20 = new int[]{1, 1, 1, 3, 2, 3};
      int[] var21 = new int[]{1, 3, 1, 1, 2, 3};
      int[] var22 = new int[]{1, 3, 2, 3, 1, 1};
      int[] var23 = new int[]{2, 1, 1, 3, 1, 3};
      int[] var24 = new int[]{1, 1, 2, 1, 3, 3};
      int[] var25 = new int[]{1, 1, 2, 3, 3, 1};
      int[] var26 = new int[]{1, 3, 2, 1, 3, 1};
      int[] var27 = new int[]{1, 1, 3, 1, 2, 3};
      int[] var28 = new int[]{1, 3, 3, 1, 2, 1};
      int[] var29 = new int[]{3, 1, 3, 1, 2, 1};
      int[] var30 = new int[]{2, 1, 3, 1, 1, 3};
      int[] var31 = new int[]{2, 1, 3, 1, 3, 1};
      int[] var32 = new int[]{3, 1, 1, 1, 2, 3};
      int[] var33 = new int[]{3, 1, 1, 3, 2, 1};
      int[] var34 = new int[]{3, 3, 1, 1, 2, 1};
      int[] var35 = new int[]{3, 1, 2, 1, 1, 3};
      int[] var36 = new int[]{3, 1, 2, 3, 1, 1};
      int[] var37 = new int[]{3, 3, 2, 1, 1, 1};
      int[] var38 = new int[]{3, 1, 4, 1, 1, 1};
      int[] var39 = new int[]{1, 1, 1, 4, 2, 2};
      int[] var40 = new int[]{1, 4, 1, 1, 2, 2};
      int[] var41 = new int[]{1, 4, 1, 2, 2, 1};
      int[] var42 = new int[]{1, 1, 2, 2, 1, 4};
      int[] var43 = new int[]{1, 1, 2, 4, 1, 2};
      int[] var44 = new int[]{1, 2, 2, 1, 1, 4};
      int[] var45 = new int[]{1, 2, 2, 4, 1, 1};
      int[] var46 = new int[]{1, 4, 2, 1, 1, 2};
      int[] var47 = new int[]{4, 1, 3, 1, 1, 1};
      int[] var48 = new int[]{2, 4, 1, 1, 1, 2};
      int[] var49 = new int[]{1, 3, 4, 1, 1, 1};
      int[] var50 = new int[]{1, 1, 1, 2, 4, 2};
      int[] var51 = new int[]{1, 2, 1, 2, 4, 1};
      int[] var52 = new int[]{1, 2, 4, 2, 1, 1};
      int[] var53 = new int[]{4, 2, 1, 1, 1, 2};
      int[] var54 = new int[]{2, 1, 4, 1, 2, 1};
      int[] var55 = new int[]{1, 1, 4, 1, 1, 3};
      int[] var56 = new int[]{1, 1, 4, 3, 1, 1};
      int[] var57 = new int[]{4, 1, 1, 1, 1, 3};
      int[] var58 = new int[]{1, 1, 3, 1, 4, 1};
      int[] var59 = new int[]{1, 1, 4, 1, 3, 1};
      int[] var60 = new int[]{3, 1, 1, 1, 4, 1};
      int[] var61 = new int[]{4, 1, 1, 1, 3, 1};
      int[] var62 = new int[]{2, 1, 1, 4, 1, 2};
      int[] var63 = new int[]{2, 1, 1, 2, 3, 2};
      CODE_PATTERNS = new int[][]{{2, 1, 2, 2, 2, 2}, var0, {2, 2, 2, 2, 2, 1}, {1, 2, 1, 2, 2, 3}, var1, {1, 3, 1, 2, 2, 2}, var2, {1, 2, 2, 3, 1, 2}, var3, var4, {2, 2, 1, 3, 1, 2}, var5, var6, var7, var8, {1, 1, 3, 2, 2, 2}, var9, {1, 2, 3, 2, 2, 1}, var10, {2, 2, 1, 1, 3, 2}, var11, var12, var13, {3, 1, 2, 1, 3, 1}, var14, {3, 2, 1, 1, 2, 2}, var15, {3, 1, 2, 2, 1, 2}, {3, 2, 2, 1, 1, 2}, var16, var17, var18, var19, var20, var21, {1, 3, 1, 3, 2, 1}, {1, 1, 2, 3, 1, 3}, {1, 3, 2, 1, 1, 3}, var22, var23, {2, 3, 1, 1, 1, 3}, {2, 3, 1, 3, 1, 1}, var24, var25, var26, var27, {1, 1, 3, 3, 2, 1}, var28, var29, {2, 1, 1, 3, 3, 1}, {2, 3, 1, 1, 3, 1}, var30, {2, 1, 3, 3, 1, 1}, var31, var32, var33, var34, var35, var36, var37, var38, {2, 2, 1, 4, 1, 1}, {4, 3, 1, 1, 1, 1}, {1, 1, 1, 2, 2, 4}, var39, {1, 2, 1, 1, 2, 4}, {1, 2, 1, 4, 2, 1}, var40, var41, var42, var43, var44, var45, var46, {1, 4, 2, 2, 1, 1}, {2, 4, 1, 2, 1, 1}, {2, 2, 1, 1, 1, 4}, var47, var48, var49, var50, {1, 2, 1, 1, 4, 2}, var51, {1, 1, 4, 2, 1, 2}, {1, 2, 4, 1, 1, 2}, var52, {4, 1, 1, 2, 1, 2}, var53, {4, 2, 1, 2, 1, 1}, {2, 1, 2, 1, 4, 1}, var54, {4, 1, 2, 1, 2, 1}, {1, 1, 1, 1, 4, 3}, {1, 1, 1, 3, 4, 1}, {1, 3, 1, 1, 4, 1}, var55, var56, var57, {4, 1, 1, 3, 1, 1}, var58, var59, var60, var61, var62, {2, 1, 1, 2, 1, 4}, var63, {2, 3, 3, 1, 1, 1, 2}};
   }

   private static int decodeCode(BitArray var0, int[] var1, int var2) throws NotFoundException {
      recordPattern(var0, var2, var1);
      int var4 = 64;
      int var5 = -1;

      int var3;
      for(var2 = 0; var2 < CODE_PATTERNS.length; var4 = var3) {
         int var6 = patternMatchVariance(var1, CODE_PATTERNS[var2], 179);
         var3 = var4;
         if(var6 < var4) {
            var5 = var2;
            var3 = var6;
         }

         ++var2;
      }

      if(var5 >= 0) {
         return var5;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static int[] findStartPattern(BitArray var0) throws NotFoundException {
      int var9 = var0.getSize();

      int var1;
      for(var1 = 0; var1 < var9 && !var0.get(var1); ++var1) {
         ;
      }

      int[] var11 = new int[6];
      int var10 = var11.length;
      int var6 = var1;
      boolean var4 = false;
      int var3 = var1;
      int var2 = 0;

      for(boolean var12 = var4; var6 < var9; ++var6) {
         if(var0.get(var6) ^ var12) {
            ++var11[var2];
         } else {
            if(var2 == var10 - 1) {
               int var5 = 64;
               int var7 = -1;

               int var13;
               for(var13 = 103; var13 <= 105; ++var13) {
                  int var8 = patternMatchVariance(var11, CODE_PATTERNS[var13], 179);
                  if(var8 < var5) {
                     var7 = var13;
                     var5 = var8;
                  }
               }

               if(var7 >= 0 && var0.isRange(Math.max(0, var3 - (var6 - var3) / 2), var3, false)) {
                  return new int[]{var3, var6, var7};
               }

               var13 = var11[0] + var11[1] + var3;

               for(var3 = 2; var3 < var10; ++var3) {
                  var11[var3 - 2] = var11[var3];
               }

               var11[var10 - 2] = 0;
               var11[var10 - 1] = 0;
               --var2;
               var3 = var13;
            } else {
               ++var2;
            }

            var11[var2] = 1;
            if(!var12) {
               var12 = true;
            } else {
               var12 = false;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException, FormatException, ChecksumException {
      int[] var23 = findStartPattern(var2);
      int var9 = var23[2];
      byte var6;
      switch(var9) {
      case 103:
         var6 = 101;
         break;
      case 104:
         var6 = 100;
         break;
      case 105:
         var6 = 99;
         break;
      default:
         throw FormatException.getFormatInstance();
      }

      StringBuffer var20 = new StringBuffer(20);
      int var13 = var23[0];
      int var11 = var23[1];
      int[] var21 = new int[6];
      boolean var8 = true;
      boolean var15 = false;
      boolean var14 = false;
      int var10 = 0;
      int var16 = 0;
      int var12 = 0;
      byte var7 = var6;
      boolean var25 = var14;

      while(!var25) {
         boolean var19 = false;
         int var18 = decodeCode(var2, var21, var11);
         if(var18 != 106) {
            var8 = true;
         }

         int var17 = var10;
         var16 = var9;
         if(var18 != 106) {
            var17 = var10 + 1;
            var16 = var9 + var17 * var18;
         }

         var9 = 0;

         for(var13 = var11; var9 < var21.length; ++var9) {
            var13 += var21[var9];
         }

         switch(var18) {
         case 103:
         case 104:
         case 105:
            throw FormatException.getFormatInstance();
         default:
            boolean var26;
            boolean var27;
            boolean var28;
            byte var29;
            label116:
            switch(var7) {
            case 99:
               if(var18 < 100) {
                  if(var18 < 10) {
                     var20.append('0');
                  }

                  var20.append(var18);
                  var27 = var25;
                  var28 = false;
                  var6 = var7;
                  var26 = var8;
                  var8 = var28;
               } else {
                  if(var18 != 106) {
                     var8 = false;
                  }

                  byte var33;
                  switch(var18) {
                  case 100:
                     var33 = 100;
                     var27 = var25;
                     var28 = false;
                     var26 = var8;
                     var6 = var33;
                     var8 = var28;
                     break label116;
                  case 101:
                     var33 = 101;
                     var27 = var25;
                     var28 = false;
                     var26 = var8;
                     var6 = var33;
                     var8 = var28;
                     break label116;
                  case 102:
                     var27 = var25;
                     var28 = false;
                     var6 = var7;
                     var26 = var8;
                     var8 = var28;
                     break label116;
                  case 103:
                  case 104:
                  case 105:
                  default:
                     var27 = var25;
                     var28 = false;
                     var6 = var7;
                     var26 = var8;
                     var8 = var28;
                     break label116;
                  case 106:
                     var6 = var7;
                     var27 = true;
                     var28 = false;
                     var26 = var8;
                     var8 = var28;
                  }
               }
               break;
            case 100:
               if(var18 < 96) {
                  var20.append((char)(var18 + 32));
                  var27 = var25;
                  var28 = false;
                  var6 = var7;
                  var26 = var8;
                  var8 = var28;
               } else {
                  if(var18 != 106) {
                     var27 = false;
                  } else {
                     var27 = var8;
                  }

                  var28 = var19;
                  var14 = var25;
                  var29 = var7;
                  switch(var18) {
                  case 96:
                  case 97:
                  case 100:
                  case 102:
                  case 103:
                  case 104:
                  case 105:
                     break;
                  case 98:
                     var28 = true;
                     var29 = 99;
                     var14 = var25;
                     break;
                  case 99:
                     var29 = 99;
                     var28 = var19;
                     var14 = var25;
                     break;
                  case 101:
                     var29 = 101;
                     var28 = var19;
                     var14 = var25;
                     break;
                  case 106:
                     var14 = true;
                     var28 = var19;
                     var29 = var7;
                     break;
                  default:
                     var29 = var7;
                     var14 = var25;
                     var28 = var19;
                  }

                  var6 = var29;
                  var8 = var28;
                  var26 = var27;
                  var27 = var14;
               }
               break;
            case 101:
               if(var18 < 64) {
                  var20.append((char)(var18 + 32));
                  var27 = var25;
                  var28 = false;
                  var6 = var7;
                  var26 = var8;
                  var8 = var28;
               } else if(var18 < 96) {
                  var20.append((char)(var18 - 64));
                  var27 = var25;
                  var28 = false;
                  var6 = var7;
                  var26 = var8;
                  var8 = var28;
               } else {
                  var27 = var8;
                  if(var18 != 106) {
                     var27 = false;
                  }

                  switch(var18) {
                  case 96:
                  case 97:
                  case 101:
                  case 102:
                     var14 = false;
                     var28 = var25;
                     var29 = var7;
                     var25 = var14;
                     var26 = var28;
                     break;
                  case 98:
                     var28 = true;
                     var29 = 100;
                     var26 = var25;
                     var25 = var28;
                     break;
                  case 99:
                     var26 = var25;
                     var29 = 99;
                     var25 = false;
                     break;
                  case 100:
                     var26 = var25;
                     var29 = 100;
                     var25 = false;
                     break;
                  case 103:
                  case 104:
                  case 105:
                  default:
                     var28 = false;
                     var29 = var7;
                     var26 = var25;
                     var25 = var28;
                     break;
                  case 106:
                     var29 = var7;
                     var25 = false;
                     var26 = true;
                  }

                  var28 = var26;
                  var26 = var27;
                  var8 = var25;
                  var6 = var29;
                  var27 = var28;
               }
               break;
            default:
               var14 = false;
               var26 = var8;
               var27 = var25;
               var8 = var14;
               var6 = var7;
            }

            byte var32 = var6;
            if(var15) {
               switch(var6) {
               case 99:
                  var32 = 100;
                  break;
               case 100:
                  var32 = 101;
                  break;
               case 101:
                  var32 = 99;
                  break;
               default:
                  var32 = var6;
               }
            }

            var15 = var8;
            var25 = var27;
            var7 = var32;
            int var36 = var12;
            var12 = var18;
            var18 = var13;
            var10 = var17;
            var9 = var16;
            var16 = var36;
            var13 = var11;
            var11 = var18;
            var8 = var26;
         }
      }

      int var31;
      for(var31 = var2.getSize(); var11 < var31 && var2.get(var11); ++var11) {
         ;
      }

      if(!var2.isRange(var11, Math.min(var31, (var11 - var13) / 2 + var11), false)) {
         throw NotFoundException.getNotFoundInstance();
      } else if((var9 - var10 * var16) % 103 != var16) {
         throw ChecksumException.getChecksumInstance();
      } else {
         var31 = var20.length();
         if(var31 > 0 && var8) {
            if(var7 == 99) {
               var20.delete(var31 - 2, var31);
            } else {
               var20.delete(var31 - 1, var31);
            }
         }

         String var22 = var20.toString();
         if(var22.length() == 0) {
            throw FormatException.getFormatInstance();
         } else {
            float var4 = (float)(var23[1] + var23[0]) / 2.0F;
            float var5 = (float)(var11 + var13) / 2.0F;
            ResultPoint var34 = new ResultPoint(var4, (float)var1);
            ResultPoint var35 = new ResultPoint(var5, (float)var1);
            BarcodeFormat var24 = BarcodeFormat.CODE_128;
            return new Result(var22, (byte[])null, new ResultPoint[]{var34, var35}, var24);
         }
      }
   }
}
