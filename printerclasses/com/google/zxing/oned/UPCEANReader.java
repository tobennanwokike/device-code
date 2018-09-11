package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.EANManufacturerOrgSupport;
import com.google.zxing.oned.OneDReader;
import com.google.zxing.oned.UPCEANExtensionSupport;
import java.util.Hashtable;

public abstract class UPCEANReader extends OneDReader {
   static final int[][] L_AND_G_PATTERNS = new int[20][];
   static final int[][] L_PATTERNS = new int[][]{{3, 2, 1, 1}, {2, 2, 2, 1}, {2, 1, 2, 2}, {1, 4, 1, 1}, {1, 1, 3, 2}, {1, 2, 3, 1}, {1, 1, 1, 4}, {1, 3, 1, 2}, {1, 2, 1, 3}, {3, 1, 1, 2}};
   private static final int MAX_AVG_VARIANCE = 107;
   private static final int MAX_INDIVIDUAL_VARIANCE = 179;
   static final int[] MIDDLE_PATTERN = new int[]{1, 1, 1, 1, 1};
   static final int[] START_END_PATTERN = new int[]{1, 1, 1};
   private final StringBuffer decodeRowStringBuffer = new StringBuffer(20);
   private final EANManufacturerOrgSupport eanManSupport = new EANManufacturerOrgSupport();
   private final UPCEANExtensionSupport extensionReader = new UPCEANExtensionSupport();

   static {
      int var0;
      for(var0 = 0; var0 < 10; ++var0) {
         L_AND_G_PATTERNS[var0] = L_PATTERNS[var0];
      }

      for(var0 = 10; var0 < 20; ++var0) {
         int[] var3 = L_PATTERNS[var0 - 10];
         int[] var2 = new int[var3.length];

         for(int var1 = 0; var1 < var3.length; ++var1) {
            var2[var1] = var3[var3.length - var1 - 1];
         }

         L_AND_G_PATTERNS[var0] = var2;
      }

   }

   private static boolean checkStandardUPCEANChecksum(String var0) throws FormatException {
      boolean var5 = false;
      int var3 = var0.length();
      if(var3 != 0) {
         int var2 = var3 - 2;
         int var1 = 0;

         while(true) {
            if(var2 < 0) {
               var2 = var1 * 3;

               for(var1 = var3 - 1; var1 >= 0; var1 -= 2) {
                  var3 = var0.charAt(var1) - 48;
                  if(var3 < 0 || var3 > 9) {
                     throw FormatException.getFormatInstance();
                  }

                  var2 += var3;
               }

               if(var2 % 10 == 0) {
                  var5 = true;
               }
               break;
            }

            int var4 = var0.charAt(var2) - 48;
            if(var4 < 0 || var4 > 9) {
               throw FormatException.getFormatInstance();
            }

            var1 += var4;
            var2 -= 2;
         }
      }

      return var5;
   }

   static int decodeDigit(BitArray var0, int[] var1, int var2, int[][] var3) throws NotFoundException {
      recordPattern(var0, var2, var1);
      int var4 = 107;
      int var5 = -1;
      int var7 = var3.length;

      for(var2 = 0; var2 < var7; ++var2) {
         int var6 = patternMatchVariance(var1, var3[var2], 179);
         if(var6 < var4) {
            var5 = var2;
            var4 = var6;
         }
      }

      if(var5 >= 0) {
         return var5;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   static int[] findGuardPattern(BitArray var0, int var1, boolean var2, int[] var3) throws NotFoundException {
      int var7 = var3.length;
      int[] var10 = new int[var7];
      int var8 = var0.getSize();

      boolean var9;
      for(var9 = false; var1 < var8; ++var1) {
         if(!var0.get(var1)) {
            var9 = true;
         } else {
            var9 = false;
         }

         if(var2 == var9) {
            break;
         }
      }

      int var5 = var1;
      int var4 = 0;

      int var6;
      for(var2 = var9; var5 < var8; var1 = var6) {
         if(var0.get(var5) ^ var2) {
            ++var10[var4];
            var6 = var1;
         } else {
            if(var4 == var7 - 1) {
               if(patternMatchVariance(var10, var3, 179) < 107) {
                  return new int[]{var1, var5};
               }

               var6 = var1 + var10[0] + var10[1];

               for(var1 = 2; var1 < var7; ++var1) {
                  var10[var1 - 2] = var10[var1];
               }

               var10[var7 - 2] = 0;
               var10[var7 - 1] = 0;
               var1 = var4 - 1;
               var4 = var6;
            } else {
               var6 = var4 + 1;
               var4 = var1;
               var1 = var6;
            }

            var10[var1] = 1;
            if(!var2) {
               var2 = true;
               var6 = var4;
               var4 = var1;
            } else {
               var2 = false;
               var6 = var4;
               var4 = var1;
            }
         }

         ++var5;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   static int[] findStartGuardPattern(BitArray var0) throws NotFoundException {
      int var1 = 0;
      int[] var6 = null;
      boolean var5 = false;

      while(!var5) {
         int[] var7 = findGuardPattern(var0, var1, false, START_END_PATTERN);
         int var4 = var7[0];
         int var2 = var7[1];
         int var3 = var4 - (var2 - var4);
         var1 = var2;
         var6 = var7;
         if(var3 >= 0) {
            var5 = var0.isRange(var3, var4, false);
            var1 = var2;
            var6 = var7;
         }
      }

      return var6;
   }

   boolean checkChecksum(String var1) throws ChecksumException, FormatException {
      return checkStandardUPCEANChecksum(var1);
   }

   int[] decodeEnd(BitArray var1, int var2) throws NotFoundException {
      return findGuardPattern(var1, var2, false, START_END_PATTERN);
   }

   protected abstract int decodeMiddle(BitArray var1, int[] var2, StringBuffer var3) throws NotFoundException;

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException, ChecksumException, FormatException {
      return this.decodeRow(var1, var2, findStartGuardPattern(var2), var3);
   }

   public Result decodeRow(int var1, BitArray var2, int[] var3, Hashtable var4) throws NotFoundException, ChecksumException, FormatException {
      ResultPointCallback var15;
      if(var4 == null) {
         var15 = null;
      } else {
         var15 = (ResultPointCallback)var4.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
      }

      if(var15 != null) {
         var15.foundPossibleResultPoint(new ResultPoint((float)(var3[0] + var3[1]) / 2.0F, (float)var1));
      }

      StringBuffer var10 = this.decodeRowStringBuffer;
      var10.setLength(0);
      int var7 = this.decodeMiddle(var2, var3, var10);
      if(var15 != null) {
         var15.foundPossibleResultPoint(new ResultPoint((float)var7, (float)var1));
      }

      int[] var9 = this.decodeEnd(var2, var7);
      if(var15 != null) {
         var15.foundPossibleResultPoint(new ResultPoint((float)(var9[0] + var9[1]) / 2.0F, (float)var1));
      }

      var7 = var9[1];
      int var8 = var7 - var9[0] + var7;
      if(var8 < var2.getSize() && var2.isRange(var7, var8, false)) {
         String var16 = var10.toString();
         if(!this.checkChecksum(var16)) {
            throw ChecksumException.getChecksumInstance();
         } else {
            float var5 = (float)(var3[1] + var3[0]) / 2.0F;
            float var6 = (float)(var9[1] + var9[0]) / 2.0F;
            BarcodeFormat var17 = this.getBarcodeFormat();
            Result var14 = new Result(var16, (byte[])null, new ResultPoint[]{new ResultPoint(var5, (float)var1), new ResultPoint(var6, (float)var1)}, var17);

            try {
               Result var12 = this.extensionReader.decodeRow(var1, var2, var9[1]);
               var14.putAllMetadata(var12.getResultMetadata());
               var14.addResultPoints(var12.getResultPoints());
            } catch (ReaderException var11) {
               ;
            }

            if(BarcodeFormat.EAN_13.equals(var17) || BarcodeFormat.UPC_A.equals(var17)) {
               String var13 = this.eanManSupport.lookupCountryIdentifier(var16);
               if(var13 != null) {
                  var14.putMetadata(ResultMetadataType.POSSIBLE_COUNTRY, var13);
               }
            }

            return var14;
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   abstract BarcodeFormat getBarcodeFormat();
}
