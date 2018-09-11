package com.google.zxing.oned.rss.expanded;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.AbstractRSSReader;
import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import com.google.zxing.oned.rss.RSSUtils;
import com.google.zxing.oned.rss.expanded.BitArrayBuilder;
import com.google.zxing.oned.rss.expanded.ExpandedPair;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;
import java.util.Hashtable;
import java.util.Vector;

public final class RSSExpandedReader extends AbstractRSSReader {
   private static final int[] EVEN_TOTAL_SUBSET = new int[]{4, 20, 52, 104, 204};
   private static final int[][] FINDER_PATTERNS;
   private static final int[][] FINDER_PATTERN_SEQUENCES;
   private static final int FINDER_PAT_A = 0;
   private static final int FINDER_PAT_B = 1;
   private static final int FINDER_PAT_C = 2;
   private static final int FINDER_PAT_D = 3;
   private static final int FINDER_PAT_E = 4;
   private static final int FINDER_PAT_F = 5;
   private static final int[] GSUM = new int[]{0, 348, 1388, 2948, 3988};
   private static final int LONGEST_SEQUENCE_SIZE;
   private static final int MAX_PAIRS = 11;
   private static final int[] SYMBOL_WIDEST = new int[]{7, 5, 4, 3, 1};
   private static final int[][] WEIGHTS;
   private final int[] currentSequence;
   private final Vector pairs = new Vector(11);
   private final int[] startEnd = new int[2];

   static {
      int[] var0 = new int[]{3, 6, 4, 1};
      int[] var1 = new int[]{3, 2, 8, 1};
      int[] var2 = new int[]{2, 6, 5, 1};
      int[] var3 = new int[]{2, 2, 9, 1};
      FINDER_PATTERNS = new int[][]{{1, 8, 4, 1}, var0, {3, 4, 6, 1}, var1, var2, var3};
      var0 = new int[]{193, 157, 49, 147, 19, 57, 171, 91};
      var1 = new int[]{62, 186, 136, 197, 169, 85, 44, 132};
      var2 = new int[]{113, 128, 173, 97, 80, 29, 87, 50};
      var3 = new int[]{134, 191, 151, 31, 93, 68, 204, 190};
      int[] var4 = new int[]{148, 22, 66, 198, 172, 94, 71, 2};
      int[] var5 = new int[]{120, 149, 25, 75, 14, 42, 126, 167};
      int[] var6 = new int[]{103, 98, 83, 38, 114, 131, 182, 124};
      int[] var7 = new int[]{45, 135, 194, 160, 58, 174, 100, 89};
      WEIGHTS = new int[][]{{1, 3, 9, 27, 81, 32, 96, 77}, {20, 60, 180, 118, 143, 7, 21, 63}, {189, 145, 13, 39, 117, 140, 209, 205}, var0, var1, {185, 133, 188, 142, 4, 12, 36, 108}, var2, {150, 28, 84, 41, 123, 158, 52, 156}, {46, 138, 203, 187, 139, 206, 196, 166}, {76, 17, 51, 153, 37, 111, 122, 155}, {43, 129, 176, 106, 107, 110, 119, 146}, {16, 48, 144, 10, 30, 90, 59, 177}, {109, 116, 137, 200, 178, 112, 125, 164}, {70, 210, 208, 202, 184, 130, 179, 115}, var3, var4, {6, 18, 54, 162, 64, 192, 154, 40}, var5, {79, 26, 78, 23, 69, 207, 199, 175}, var6, {161, 61, 183, 127, 170, 88, 53, 159}, {55, 165, 73, 8, 24, 72, 5, 15}, var7};
      FINDER_PATTERN_SEQUENCES = new int[][]{{0, 0}, {0, 1, 1}, {0, 2, 1, 3}, {0, 4, 1, 3, 2}, {0, 4, 1, 3, 3, 5}, {0, 4, 1, 3, 4, 5, 5}, {0, 0, 1, 1, 2, 2, 3, 3}, {0, 0, 1, 1, 2, 2, 3, 4, 4}, {0, 0, 1, 1, 2, 2, 3, 4, 5, 5}, {0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5}};
      LONGEST_SEQUENCE_SIZE = FINDER_PATTERN_SEQUENCES[FINDER_PATTERN_SEQUENCES.length - 1].length;
   }

   public RSSExpandedReader() {
      this.currentSequence = new int[LONGEST_SEQUENCE_SIZE];
   }

   private void adjustOddEvenCounts(int var1) throws NotFoundException {
      boolean var4 = false;
      boolean var7 = true;
      int var9 = count(this.oddCounts);
      int var8 = count(this.evenCounts);
      int var10 = var9 + var8 - var1;
      boolean var5;
      if((var9 & 1) == 1) {
         var5 = true;
      } else {
         var5 = false;
      }

      boolean var6;
      if((var8 & 1) == 0) {
         var6 = true;
      } else {
         var6 = false;
      }

      boolean var2;
      boolean var11;
      if(var9 > 13) {
         var2 = true;
         var11 = false;
      } else if(var9 < 4) {
         var2 = false;
         var11 = true;
      } else {
         var2 = false;
         var11 = false;
      }

      boolean var3;
      if(var8 > 13) {
         var3 = false;
         var4 = true;
      } else if(var8 < 4) {
         var3 = true;
      } else {
         var3 = false;
      }

      if(var10 == 1) {
         if(var5) {
            if(var6) {
               throw NotFoundException.getNotFoundInstance();
            }

            var5 = true;
            var2 = var11;
            var11 = var5;
         } else {
            if(!var6) {
               throw NotFoundException.getNotFoundInstance();
            }

            var4 = true;
            var5 = var11;
            var11 = var2;
            var2 = var5;
         }
      } else if(var10 == -1) {
         if(var5) {
            if(var6) {
               throw NotFoundException.getNotFoundInstance();
            }

            var11 = var2;
            var2 = true;
         } else {
            if(!var6) {
               throw NotFoundException.getNotFoundInstance();
            }

            var5 = var2;
            var2 = var11;
            var3 = var7;
            var11 = var5;
         }
      } else {
         if(var10 != 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         if(var5) {
            if(!var6) {
               throw NotFoundException.getNotFoundInstance();
            }

            if(var9 < var8) {
               var4 = true;
               var11 = var2;
               var2 = true;
            } else {
               var5 = true;
               var2 = var11;
               var3 = var7;
               var11 = var5;
            }
         } else {
            if(var6) {
               throw NotFoundException.getNotFoundInstance();
            }

            var5 = var11;
            var11 = var2;
            var2 = var5;
         }
      }

      if(var2) {
         if(var11) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.oddCounts, this.oddRoundingErrors);
      }

      if(var11) {
         decrement(this.oddCounts, this.oddRoundingErrors);
      }

      if(var3) {
         if(var4) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.evenCounts, this.oddRoundingErrors);
      }

      if(var4) {
         decrement(this.evenCounts, this.evenRoundingErrors);
      }

   }

   private boolean checkChecksum() {
      boolean var6 = true;
      ExpandedPair var8 = (ExpandedPair)this.pairs.elementAt(0);
      DataCharacter var7 = var8.getLeftChar();
      int var1 = var8.getRightChar().getChecksumPortion();
      int var2 = 2;

      for(int var3 = 1; var3 < this.pairs.size(); ++var3) {
         var8 = (ExpandedPair)this.pairs.elementAt(var3);
         int var4 = var1 + var8.getLeftChar().getChecksumPortion();
         int var5 = var2 + 1;
         var2 = var5;
         var1 = var4;
         if(var8.getRightChar() != null) {
            var1 = var4 + var8.getRightChar().getChecksumPortion();
            var2 = var5 + 1;
         }
      }

      if(var1 % 211 + (var2 - 4) * 211 != var7.getValue()) {
         var6 = false;
      }

      return var6;
   }

   private boolean checkPairSequence(Vector var1, FinderPattern var2) throws NotFoundException {
      boolean var6 = false;
      int var5 = var1.size() + 1;
      if(var5 > this.currentSequence.length) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         int var3;
         for(var3 = 0; var3 < var1.size(); ++var3) {
            this.currentSequence[var3] = ((ExpandedPair)var1.elementAt(var3)).getFinderPattern().getValue();
         }

         this.currentSequence[var5 - 1] = var2.getValue();

         for(var3 = 0; var3 < FINDER_PATTERN_SEQUENCES.length; ++var3) {
            int[] var7 = FINDER_PATTERN_SEQUENCES[var3];
            if(var7.length >= var5) {
               int var4 = 0;

               boolean var8;
               while(true) {
                  if(var4 >= var5) {
                     var8 = true;
                     break;
                  }

                  if(this.currentSequence[var4] != var7[var4]) {
                     var8 = false;
                     break;
                  }

                  ++var4;
               }

               if(var8) {
                  if(var5 == var7.length) {
                     var6 = true;
                  }

                  return var6;
               }
            }
         }

         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static Result constructResult(Vector var0) throws NotFoundException {
      String var1 = AbstractExpandedDecoder.createDecoder(BitArrayBuilder.buildBitArray(var0)).parseInformation();
      ResultPoint[] var2 = ((ExpandedPair)var0.elementAt(0)).getFinderPattern().getResultPoints();
      ResultPoint[] var4 = ((ExpandedPair)var0.lastElement()).getFinderPattern().getResultPoints();
      ResultPoint var6 = var2[0];
      ResultPoint var7 = var2[1];
      ResultPoint var3 = var4[0];
      ResultPoint var5 = var4[1];
      BarcodeFormat var8 = BarcodeFormat.RSS_EXPANDED;
      return new Result(var1, (byte[])null, new ResultPoint[]{var6, var7, var3, var5}, var8);
   }

   private void findNextPair(BitArray var1, Vector var2, int var3) throws NotFoundException {
      int[] var11 = this.decodeFinderCounters;
      var11[0] = 0;
      var11[1] = 0;
      var11[2] = 0;
      var11[3] = 0;
      int var10 = var1.getSize();
      if(var3 < 0) {
         if(var2.isEmpty()) {
            var3 = 0;
         } else {
            var3 = ((ExpandedPair)var2.lastElement()).getFinderPattern().getStartEnd()[1];
         }
      }

      boolean var5;
      if(var2.size() % 2 != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      boolean var4;
      for(var4 = false; var3 < var10; ++var3) {
         if(!var1.get(var3)) {
            var4 = true;
         } else {
            var4 = false;
         }

         if(!var4) {
            break;
         }
      }

      int var6 = var3;
      int var8 = 0;

      for(boolean var7 = var4; var6 < var10; var7 = var4) {
         int var13;
         if(var1.get(var6) ^ var7) {
            ++var11[var8];
            var4 = var7;
            var13 = var3;
         } else {
            int var12;
            if(var8 == 3) {
               if(var5) {
                  reverseCounters(var11);
               }

               if(isFinderPattern(var11)) {
                  this.startEnd[0] = var3;
                  this.startEnd[1] = var6;
                  return;
               }

               if(var5) {
                  reverseCounters(var11);
               }

               var12 = var3 + var11[0] + var11[1];
               var11[0] = var11[2];
               var11[1] = var11[3];
               var11[2] = 0;
               var11[3] = 0;
               var3 = var8 - 1;
            } else {
               ++var8;
               var12 = var3;
               var3 = var8;
            }

            var11[var3] = 1;
            boolean var9;
            if(!var7) {
               var9 = true;
               var13 = var12;
               var8 = var3;
               var4 = var9;
            } else {
               var9 = false;
               var13 = var12;
               var8 = var3;
               var4 = var9;
            }
         }

         ++var6;
         var3 = var13;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int getNextSecondBar(BitArray var0, int var1) {
      boolean var2;
      for(var2 = var0.get(var1); var1 < var0.size && var0.get(var1) == var2; ++var1) {
         ;
      }

      if(!var2) {
         var2 = true;
      } else {
         var2 = false;
      }

      while(var1 < var0.size && var0.get(var1) == var2) {
         ++var1;
      }

      return var1;
   }

   private static boolean isNotA1left(FinderPattern var0, boolean var1, boolean var2) {
      if(var0.getValue() == 0 && var1 && var2) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private FinderPattern parseFoundFinderPattern(BitArray var1, int var2, boolean var3) {
      int var4;
      int var5;
      int var6;
      int var7;
      if(var3) {
         for(var4 = this.startEnd[0] - 1; var4 >= 0 && !var1.get(var4); --var4) {
            ;
         }

         ++var4;
         var6 = this.startEnd[0] - var4;
         var5 = this.startEnd[1];
      } else {
         var7 = this.startEnd[0];

         for(var4 = this.startEnd[1] + 1; var1.get(var4) && var4 < var1.size; ++var4) {
            ;
         }

         var6 = this.startEnd[1];
         var5 = var4;
         var6 = var4 - var6;
         var4 = var7;
      }

      int[] var9 = this.decodeFinderCounters;

      for(var7 = var9.length - 1; var7 > 0; --var7) {
         var9[var7] = var9[var7 - 1];
      }

      var9[0] = var6;

      FinderPattern var10;
      try {
         var6 = parseFinderValue(var9, FINDER_PATTERNS);
      } catch (NotFoundException var8) {
         var10 = null;
         return var10;
      }

      var10 = new FinderPattern(var6, new int[]{var4, var5}, var4, var5, var2);
      return var10;
   }

   private static void reverseCounters(int[] var0) {
      int var2 = var0.length;

      for(int var1 = 0; var1 < var2 / 2; ++var1) {
         int var3 = var0[var1];
         var0[var1] = var0[var2 - var1 - 1];
         var0[var2 - var1 - 1] = var3;
      }

   }

   DataCharacter decodeDataCharacter(BitArray var1, FinderPattern var2, boolean var3, boolean var4) throws NotFoundException {
      int[] var14 = this.dataCharacterCounters;
      var14[0] = 0;
      var14[1] = 0;
      var14[2] = 0;
      var14[3] = 0;
      var14[4] = 0;
      var14[5] = 0;
      var14[6] = 0;
      var14[7] = 0;
      int var7;
      int var8;
      int var9;
      if(var4) {
         recordPatternInReverse(var1, var2.getStartEnd()[0], var14);
      } else {
         recordPattern(var1, var2.getStartEnd()[1] + 1, var14);
         var7 = 0;

         for(var8 = var14.length - 1; var7 < var8; --var8) {
            var9 = var14[var7];
            var14[var7] = var14[var8];
            var14[var8] = var9;
            ++var7;
         }
      }

      float var5 = (float)count(var14) / (float)17;
      int[] var15 = this.oddCounts;
      int[] var17 = this.evenCounts;
      float[] var16 = this.oddRoundingErrors;
      float[] var18 = this.evenRoundingErrors;

      for(var8 = 0; var8 < var14.length; ++var8) {
         float var6 = 1.0F * (float)var14[var8] / var5;
         var9 = (int)(0.5F + var6);
         if(var9 < 1) {
            var7 = 1;
         } else {
            var7 = var9;
            if(var9 > 8) {
               var7 = 8;
            }
         }

         var9 = var8 >> 1;
         if((var8 & 1) == 0) {
            var15[var9] = var7;
            var16[var9] = var6 - (float)var7;
         } else {
            var17[var9] = var7;
            var18[var9] = var6 - (float)var7;
         }
      }

      this.adjustOddEvenCounts(17);
      var9 = var2.getValue();
      byte var19;
      if(var3) {
         var19 = 0;
      } else {
         var19 = 2;
      }

      byte var20;
      if(var4) {
         var20 = 0;
      } else {
         var20 = 1;
      }

      int var13 = var20 + var9 * 4 + var19 - 1;
      var7 = var15.length;
      var8 = 0;
      var9 = var7 - 1;

      int var10;
      for(var7 = 0; var9 >= 0; var7 = var10) {
         var10 = var7;
         if(isNotA1left(var2, var3, var4)) {
            var10 = var7 + WEIGHTS[var13][var9 * 2] * var15[var9];
         }

         var7 = var15[var9];
         --var9;
         var8 += var7;
      }

      int var11 = var17.length;
      var9 = 0;
      var10 = 0;
      --var11;

      int var12;
      while(var11 >= 0) {
         var12 = var9;
         if(isNotA1left(var2, var3, var4)) {
            var12 = var9 + WEIGHTS[var13][var11 * 2 + 1] * var17[var11];
         }

         var10 += var17[var11];
         --var11;
         var9 = var12;
      }

      if((var8 & 1) == 0 && var8 <= 13 && var8 >= 4) {
         var10 = (13 - var8) / 2;
         var11 = SYMBOL_WIDEST[var10];
         var8 = RSSUtils.getRSSvalue(var15, var11, true);
         var11 = RSSUtils.getRSSvalue(var17, 9 - var11, false);
         var12 = EVEN_TOTAL_SUBSET[var10];
         return new DataCharacter(GSUM[var10] + var8 * var12 + var11, var7 + var9);
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException {
      this.reset();
      this.decodeRow2pairs(var1, var2);
      return constructResult(this.pairs);
   }

   Vector decodeRow2pairs(int var1, BitArray var2) throws NotFoundException {
      while(true) {
         ExpandedPair var3 = this.retrieveNextPair(var2, this.pairs, var1);
         this.pairs.addElement(var3);
         if(var3.mayBeLast()) {
            if(this.checkChecksum()) {
               return this.pairs;
            }

            if(var3.mustBeLast()) {
               throw NotFoundException.getNotFoundInstance();
            }
         }
      }
   }

   public void reset() {
      this.pairs.setSize(0);
   }

   ExpandedPair retrieveNextPair(BitArray var1, Vector var2, int var3) throws NotFoundException {
      boolean var7;
      if(var2.size() % 2 == 0) {
         var7 = true;
      } else {
         var7 = false;
      }

      int var6 = -1;
      boolean var4 = true;

      boolean var5;
      FinderPattern var9;
      do {
         this.findNextPair(var1, var2, var6);
         var9 = this.parseFoundFinderPattern(var1, var3, var7);
         if(var9 == null) {
            var6 = getNextSecondBar(var1, this.startEnd[0]);
            var5 = var4;
         } else {
            var5 = false;
         }

         var4 = var5;
      } while(var5);

      boolean var8 = this.checkPairSequence(var2, var9);
      DataCharacter var12 = this.decodeDataCharacter(var1, var9, var7, true);

      DataCharacter var11;
      try {
         var11 = this.decodeDataCharacter(var1, var9, var7, false);
      } catch (NotFoundException var10) {
         if(!var8) {
            throw var10;
         }

         var11 = null;
      }

      return new ExpandedPair(var12, var11, var9, var8);
   }
}
