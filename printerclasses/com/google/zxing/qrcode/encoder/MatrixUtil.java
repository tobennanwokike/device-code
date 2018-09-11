package com.google.zxing.qrcode.encoder;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.MaskUtil;
import com.google.zxing.qrcode.encoder.QRCode;

public final class MatrixUtil {
   private static final int[][] HORIZONTAL_SEPARATION_PATTERN;
   private static final int[][] POSITION_ADJUSTMENT_PATTERN;
   private static final int[][] POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE;
   private static final int[][] POSITION_DETECTION_PATTERN;
   private static final int[][] TYPE_INFO_COORDINATES;
   private static final int TYPE_INFO_MASK_PATTERN = 21522;
   private static final int TYPE_INFO_POLY = 1335;
   private static final int VERSION_INFO_POLY = 7973;
   private static final int[][] VERTICAL_SEPARATION_PATTERN;

   static {
      int[] var0 = new int[]{1, 0, 1, 1, 1, 0, 1};
      POSITION_DETECTION_PATTERN = new int[][]{{1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 0, 0, 1}, var0, {1, 0, 1, 1, 1, 0, 1}, {1, 0, 1, 1, 1, 0, 1}, {1, 0, 0, 0, 0, 0, 1}, {1, 1, 1, 1, 1, 1, 1}};
      HORIZONTAL_SEPARATION_PATTERN = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0}};
      var0 = new int[]{0};
      int[] var1 = new int[]{0};
      int[] var2 = new int[]{0};
      VERTICAL_SEPARATION_PATTERN = new int[][]{{0}, {0}, {0}, var0, {0}, var1, var2};
      POSITION_ADJUSTMENT_PATTERN = new int[][]{{1, 1, 1, 1, 1}, {1, 0, 0, 0, 1}, {1, 0, 1, 0, 1}, {1, 0, 0, 0, 1}, {1, 1, 1, 1, 1}};
      var0 = new int[]{6, 30, -1, -1, -1, -1, -1};
      var1 = new int[]{6, 26, 46, -1, -1, -1, -1};
      var2 = new int[]{6, 26, 48, 70, -1, -1, -1};
      int[] var3 = new int[]{6, 30, 56, 82, -1, -1, -1};
      int[] var4 = new int[]{6, 30, 58, 86, -1, -1, -1};
      int[] var5 = new int[]{6, 26, 50, 74, 98, -1, -1};
      int[] var6 = new int[]{6, 28, 54, 80, 106, -1, -1};
      int[] var7 = new int[]{6, 30, 58, 86, 114, 142, -1};
      int[] var8 = new int[]{6, 24, 50, 76, 102, 128, 154};
      int[] var9 = new int[]{6, 26, 54, 82, 110, 138, 166};
      POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE = new int[][]{{-1, -1, -1, -1, -1, -1, -1}, {6, 18, -1, -1, -1, -1, -1}, {6, 22, -1, -1, -1, -1, -1}, {6, 26, -1, -1, -1, -1, -1}, var0, {6, 34, -1, -1, -1, -1, -1}, {6, 22, 38, -1, -1, -1, -1}, {6, 24, 42, -1, -1, -1, -1}, var1, {6, 28, 50, -1, -1, -1, -1}, {6, 30, 54, -1, -1, -1, -1}, {6, 32, 58, -1, -1, -1, -1}, {6, 34, 62, -1, -1, -1, -1}, {6, 26, 46, 66, -1, -1, -1}, var2, {6, 26, 50, 74, -1, -1, -1}, {6, 30, 54, 78, -1, -1, -1}, var3, var4, {6, 34, 62, 90, -1, -1, -1}, {6, 28, 50, 72, 94, -1, -1}, var5, {6, 30, 54, 78, 102, -1, -1}, var6, {6, 32, 58, 84, 110, -1, -1}, {6, 30, 58, 86, 114, -1, -1}, {6, 34, 62, 90, 118, -1, -1}, {6, 26, 50, 74, 98, 122, -1}, {6, 30, 54, 78, 102, 126, -1}, {6, 26, 52, 78, 104, 130, -1}, {6, 30, 56, 82, 108, 134, -1}, {6, 34, 60, 86, 112, 138, -1}, var7, {6, 34, 62, 90, 118, 146, -1}, {6, 30, 54, 78, 102, 126, 150}, var8, {6, 28, 54, 80, 106, 132, 158}, {6, 32, 58, 84, 110, 136, 162}, var9, {6, 30, 58, 86, 114, 142, 170}};
      var0 = new int[]{8, 0};
      var1 = new int[]{8, 2};
      var2 = new int[]{2, 8};
      TYPE_INFO_COORDINATES = new int[][]{var0, {8, 1}, var1, {8, 3}, {8, 4}, {8, 5}, {8, 7}, {8, 8}, {7, 8}, {5, 8}, {4, 8}, {3, 8}, var2, {1, 8}, {0, 8}};
   }

   public static void buildMatrix(BitArray var0, ErrorCorrectionLevel var1, int var2, int var3, ByteMatrix var4) throws WriterException {
      clearMatrix(var4);
      embedBasicPatterns(var2, var4);
      embedTypeInfo(var1, var3, var4);
      maybeEmbedVersionInfo(var2, var4);
      embedDataBits(var0, var3, var4);
   }

   public static int calculateBCHCode(int var0, int var1) {
      int var2 = findMSBSet(var1);

      for(var0 <<= var2 - 1; findMSBSet(var0) >= var2; var0 ^= var1 << findMSBSet(var0) - var2) {
         ;
      }

      return var0;
   }

   public static void clearMatrix(ByteMatrix var0) {
      var0.clear((byte)-1);
   }

   public static void embedBasicPatterns(int var0, ByteMatrix var1) throws WriterException {
      embedPositionDetectionPatternsAndSeparators(var1);
      embedDarkDotAtLeftBottomCorner(var1);
      maybeEmbedPositionAdjustmentPatterns(var0, var1);
      embedTimingPatterns(var1);
   }

   private static void embedDarkDotAtLeftBottomCorner(ByteMatrix var0) throws WriterException {
      if(var0.get(8, var0.getHeight() - 8) == 0) {
         throw new WriterException();
      } else {
         var0.set(8, var0.getHeight() - 8, (int)1);
      }
   }

   public static void embedDataBits(BitArray var0, int var1, ByteMatrix var2) throws WriterException {
      int var5 = var2.getWidth() - 1;
      int var4 = var2.getHeight() - 1;
      int var6 = -1;

      int var3;
      for(var3 = 0; var5 > 0; var4 += var6) {
         if(var5 == 6) {
            --var5;
         }

         while(var4 >= 0 && var4 < var2.getHeight()) {
            for(int var7 = 0; var7 < 2; ++var7) {
               int var8 = var5 - var7;
               if(isEmpty(var2.get(var8, var4))) {
                  boolean var10;
                  if(var3 < var0.getSize()) {
                     var10 = var0.get(var3);
                     ++var3;
                  } else {
                     var10 = false;
                  }

                  boolean var9 = var10;
                  if(var1 != -1) {
                     var9 = var10;
                     if(MaskUtil.getDataMaskBit(var1, var8, var4)) {
                        if(!var10) {
                           var9 = true;
                        } else {
                           var9 = false;
                        }
                     }
                  }

                  var2.set(var8, var4, var9);
               }
            }

            var4 += var6;
         }

         var6 = -var6;
         var5 -= 2;
      }

      if(var3 != var0.getSize()) {
         throw new WriterException("Not all bits consumed: " + var3 + '/' + var0.getSize());
      }
   }

   private static void embedHorizontalSeparationPattern(int var0, int var1, ByteMatrix var2) throws WriterException {
      if(HORIZONTAL_SEPARATION_PATTERN[0].length == 8 && HORIZONTAL_SEPARATION_PATTERN.length == 1) {
         for(int var3 = 0; var3 < 8; ++var3) {
            if(!isEmpty(var2.get(var0 + var3, var1))) {
               throw new WriterException();
            }

            var2.set(var0 + var3, var1, HORIZONTAL_SEPARATION_PATTERN[0][var3]);
         }

      } else {
         throw new WriterException("Bad horizontal separation pattern");
      }
   }

   private static void embedPositionAdjustmentPattern(int var0, int var1, ByteMatrix var2) throws WriterException {
      if(POSITION_ADJUSTMENT_PATTERN[0].length == 5 && POSITION_ADJUSTMENT_PATTERN.length == 5) {
         for(int var3 = 0; var3 < 5; ++var3) {
            for(int var4 = 0; var4 < 5; ++var4) {
               if(!isEmpty(var2.get(var0 + var4, var1 + var3))) {
                  throw new WriterException();
               }

               var2.set(var0 + var4, var1 + var3, POSITION_ADJUSTMENT_PATTERN[var3][var4]);
            }
         }

      } else {
         throw new WriterException("Bad position adjustment");
      }
   }

   private static void embedPositionDetectionPattern(int var0, int var1, ByteMatrix var2) throws WriterException {
      if(POSITION_DETECTION_PATTERN[0].length == 7 && POSITION_DETECTION_PATTERN.length == 7) {
         for(int var3 = 0; var3 < 7; ++var3) {
            for(int var4 = 0; var4 < 7; ++var4) {
               if(!isEmpty(var2.get(var0 + var4, var1 + var3))) {
                  throw new WriterException();
               }

               var2.set(var0 + var4, var1 + var3, POSITION_DETECTION_PATTERN[var3][var4]);
            }
         }

      } else {
         throw new WriterException("Bad position detection pattern");
      }
   }

   private static void embedPositionDetectionPatternsAndSeparators(ByteMatrix var0) throws WriterException {
      int var1 = POSITION_DETECTION_PATTERN[0].length;
      embedPositionDetectionPattern(0, 0, var0);
      embedPositionDetectionPattern(var0.getWidth() - var1, 0, var0);
      embedPositionDetectionPattern(0, var0.getWidth() - var1, var0);
      var1 = HORIZONTAL_SEPARATION_PATTERN[0].length;
      embedHorizontalSeparationPattern(0, var1 - 1, var0);
      embedHorizontalSeparationPattern(var0.getWidth() - var1, var1 - 1, var0);
      embedHorizontalSeparationPattern(0, var0.getWidth() - var1, var0);
      var1 = VERTICAL_SEPARATION_PATTERN.length;
      embedVerticalSeparationPattern(var1, 0, var0);
      embedVerticalSeparationPattern(var0.getHeight() - var1 - 1, 0, var0);
      embedVerticalSeparationPattern(var1, var0.getHeight() - var1, var0);
   }

   private static void embedTimingPatterns(ByteMatrix var0) throws WriterException {
      for(int var1 = 8; var1 < var0.getWidth() - 8; ++var1) {
         int var2 = (var1 + 1) % 2;
         if(!isValidValue(var0.get(var1, 6))) {
            throw new WriterException();
         }

         if(isEmpty(var0.get(var1, 6))) {
            var0.set(var1, 6, (int)var2);
         }

         if(!isValidValue(var0.get(6, var1))) {
            throw new WriterException();
         }

         if(isEmpty(var0.get(6, var1))) {
            var0.set(6, var1, (int)var2);
         }
      }

   }

   public static void embedTypeInfo(ErrorCorrectionLevel var0, int var1, ByteMatrix var2) throws WriterException {
      BitArray var4 = new BitArray();
      makeTypeInfoBits(var0, var1, var4);

      for(var1 = 0; var1 < var4.getSize(); ++var1) {
         boolean var3 = var4.get(var4.getSize() - 1 - var1);
         var2.set(TYPE_INFO_COORDINATES[var1][0], TYPE_INFO_COORDINATES[var1][1], var3);
         if(var1 < 8) {
            var2.set(var2.getWidth() - var1 - 1, 8, var3);
         } else {
            var2.set(8, var2.getHeight() - 7 + (var1 - 8), var3);
         }
      }

   }

   private static void embedVerticalSeparationPattern(int var0, int var1, ByteMatrix var2) throws WriterException {
      if(VERTICAL_SEPARATION_PATTERN[0].length == 1 && VERTICAL_SEPARATION_PATTERN.length == 7) {
         for(int var3 = 0; var3 < 7; ++var3) {
            if(!isEmpty(var2.get(var0, var1 + var3))) {
               throw new WriterException();
            }

            var2.set(var0, var1 + var3, VERTICAL_SEPARATION_PATTERN[var3][0]);
         }

      } else {
         throw new WriterException("Bad vertical separation pattern");
      }
   }

   public static int findMSBSet(int var0) {
      byte var2 = 0;
      int var1 = var0;

      for(var0 = var2; var1 != 0; ++var0) {
         var1 >>>= 1;
      }

      return var0;
   }

   private static boolean isEmpty(int var0) {
      boolean var1;
      if(var0 == -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static boolean isValidValue(int var0) {
      boolean var2 = true;
      boolean var1 = var2;
      if(var0 != -1) {
         var1 = var2;
         if(var0 != 0) {
            if(var0 == 1) {
               var1 = var2;
            } else {
               var1 = false;
            }
         }
      }

      return var1;
   }

   public static void makeTypeInfoBits(ErrorCorrectionLevel var0, int var1, BitArray var2) throws WriterException {
      if(!QRCode.isValidMaskPattern(var1)) {
         throw new WriterException("Invalid mask pattern");
      } else {
         var1 |= var0.getBits() << 3;
         var2.appendBits(var1, 5);
         var2.appendBits(calculateBCHCode(var1, 1335), 10);
         BitArray var3 = new BitArray();
         var3.appendBits(21522, 15);
         var2.xor(var3);
         if(var2.getSize() != 15) {
            throw new WriterException("should not happen but we got: " + var2.getSize());
         }
      }
   }

   public static void makeVersionInfoBits(int var0, BitArray var1) throws WriterException {
      var1.appendBits(var0, 6);
      var1.appendBits(calculateBCHCode(var0, 7973), 12);
      if(var1.getSize() != 18) {
         throw new WriterException("should not happen but we got: " + var1.getSize());
      }
   }

   private static void maybeEmbedPositionAdjustmentPatterns(int var0, ByteMatrix var1) throws WriterException {
      if(var0 >= 2) {
         --var0;
         int[] var6 = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[var0];
         int var3 = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[var0].length;

         for(var0 = 0; var0 < var3; ++var0) {
            for(int var2 = 0; var2 < var3; ++var2) {
               int var5 = var6[var0];
               int var4 = var6[var2];
               if(var4 != -1 && var5 != -1 && isEmpty(var1.get(var4, var5))) {
                  embedPositionAdjustmentPattern(var4 - 2, var5 - 2, var1);
               }
            }
         }
      }

   }

   public static void maybeEmbedVersionInfo(int var0, ByteMatrix var1) throws WriterException {
      if(var0 >= 7) {
         BitArray var5 = new BitArray();
         makeVersionInfoBits(var0, var5);
         var0 = 17;

         for(int var2 = 0; var2 < 6; ++var2) {
            for(int var3 = 0; var3 < 3; ++var3) {
               boolean var4 = var5.get(var0);
               --var0;
               var1.set(var2, var1.getHeight() - 11 + var3, var4);
               var1.set(var1.getHeight() - 11 + var3, var2, var4);
            }
         }
      }

   }
}
