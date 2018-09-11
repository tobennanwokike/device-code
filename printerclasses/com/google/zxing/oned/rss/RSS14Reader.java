package com.google.zxing.oned.rss;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.AbstractRSSReader;
import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import com.google.zxing.oned.rss.Pair;
import com.google.zxing.oned.rss.RSSUtils;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public final class RSS14Reader extends AbstractRSSReader {
   private static final int[][] FINDER_PATTERNS;
   private static final int[] INSIDE_GSUM = new int[]{0, 336, 1036, 1516};
   private static final int[] INSIDE_ODD_TOTAL_SUBSET = new int[]{4, 20, 48, 81};
   private static final int[] INSIDE_ODD_WIDEST = new int[]{2, 4, 6, 8};
   private static final int[] OUTSIDE_EVEN_TOTAL_SUBSET = new int[]{1, 10, 34, 70, 126};
   private static final int[] OUTSIDE_GSUM = new int[]{0, 161, 961, 2015, 2715};
   private static final int[] OUTSIDE_ODD_WIDEST = new int[]{8, 6, 4, 3, 1};
   private final Vector possibleLeftPairs = new Vector();
   private final Vector possibleRightPairs = new Vector();

   static {
      int[] var0 = new int[]{3, 5, 5, 1};
      int[] var1 = new int[]{3, 3, 7, 1};
      int[] var2 = new int[]{2, 5, 6, 1};
      int[] var3 = new int[]{2, 3, 8, 1};
      FINDER_PATTERNS = new int[][]{{3, 8, 2, 1}, var0, var1, {3, 1, 9, 1}, {2, 7, 4, 1}, var2, var3, {1, 5, 7, 1}, {1, 3, 9, 1}};
   }

   private static void addOrTally(Vector var0, Pair var1) {
      if(var1 != null) {
         Enumeration var4 = var0.elements();

         boolean var2;
         while(true) {
            if(!var4.hasMoreElements()) {
               var2 = false;
               break;
            }

            Pair var3 = (Pair)var4.nextElement();
            if(var3.getValue() == var1.getValue()) {
               var3.incrementCount();
               var2 = true;
               break;
            }
         }

         if(!var2) {
            var0.addElement(var1);
         }
      }

   }

   private void adjustOddEvenCounts(boolean var1, int var2) throws NotFoundException {
      boolean var5 = false;
      boolean var10 = true;
      int var12 = count(this.oddCounts);
      int var11 = count(this.evenCounts);
      int var13 = var12 + var11 - var2;
      byte var14;
      if(var1) {
         var14 = 1;
      } else {
         var14 = 0;
      }

      boolean var8;
      if((var12 & 1) == var14) {
         var8 = true;
      } else {
         var8 = false;
      }

      boolean var9;
      if((var11 & 1) == 1) {
         var9 = true;
      } else {
         var9 = false;
      }

      boolean var3;
      boolean var4;
      boolean var6;
      boolean var15;
      label125: {
         boolean var7;
         if(var1) {
            if(var12 > 12) {
               var4 = true;
               var3 = false;
            } else if(var12 < 4) {
               var4 = false;
               var3 = true;
            } else {
               var4 = false;
               var3 = false;
            }

            if(var11 > 12) {
               var15 = false;
               var5 = true;
               break label125;
            }

            var7 = var4;
            var6 = var3;
            if(var11 < 4) {
               var15 = true;
               break label125;
            }
         } else {
            if(var12 > 11) {
               var4 = true;
               var3 = false;
            } else if(var12 < 5) {
               var4 = false;
               var3 = true;
            } else {
               var4 = false;
               var3 = false;
            }

            if(var11 > 10) {
               var15 = false;
               var5 = true;
               break label125;
            }

            var7 = var4;
            var6 = var3;
            if(var11 < 4) {
               var15 = true;
               break label125;
            }
         }

         var15 = false;
         var4 = var7;
         var3 = var6;
      }

      if(var13 == 1) {
         if(var8) {
            if(var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            var4 = var15;
            var15 = true;
         } else {
            if(!var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            var5 = true;
            var6 = var15;
            var15 = var4;
            var4 = var6;
         }
      } else if(var13 == -1) {
         if(var8) {
            if(var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            var3 = var4;
            var6 = true;
            var4 = var15;
            var15 = var3;
            var3 = var6;
         } else {
            if(!var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            var15 = var4;
            var4 = var10;
         }
      } else {
         if(var13 != 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         if(var8) {
            if(!var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            if(var12 < var11) {
               var5 = true;
               var3 = var4;
               var6 = true;
               var4 = var15;
               var15 = var3;
               var3 = var6;
            } else {
               var15 = true;
               var4 = var10;
            }
         } else {
            if(var9) {
               throw NotFoundException.getNotFoundInstance();
            }

            var6 = var4;
            var4 = var15;
            var15 = var6;
         }
      }

      if(var3) {
         if(var15) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.oddCounts, this.oddRoundingErrors);
      }

      if(var15) {
         decrement(this.oddCounts, this.oddRoundingErrors);
      }

      if(var4) {
         if(var5) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.evenCounts, this.oddRoundingErrors);
      }

      if(var5) {
         decrement(this.evenCounts, this.evenRoundingErrors);
      }

   }

   private static boolean checkChecksum(Pair var0, Pair var1) {
      int var3 = var0.getFinderPattern().getValue();
      int var2 = var1.getFinderPattern().getValue();
      if((var3 != 0 || var2 != 8) && var3 == 8 && var2 == 0) {
         ;
      }

      int var5 = var0.getChecksumPortion();
      int var4 = var1.getChecksumPortion();
      var3 = var0.getFinderPattern().getValue() * 9 + var1.getFinderPattern().getValue();
      var2 = var3;
      if(var3 > 72) {
         var2 = var3 - 1;
      }

      var3 = var2;
      if(var2 > 8) {
         var3 = var2 - 1;
      }

      boolean var6;
      if((var5 + var4 * 16) % 79 == var3) {
         var6 = true;
      } else {
         var6 = false;
      }

      return var6;
   }

   private static Result constructResult(Pair var0, Pair var1) {
      String var6 = String.valueOf(4537077L * (long)var0.getValue() + (long)var1.getValue());
      StringBuffer var8 = new StringBuffer(14);

      int var2;
      for(var2 = 13 - var6.length(); var2 > 0; --var2) {
         var8.append('0');
      }

      var8.append(var6);
      var2 = 0;

      int var3;
      for(var3 = 0; var2 < 13; ++var2) {
         int var5 = var8.charAt(var2) - 48;
         int var4 = var5;
         if((var2 & 1) == 0) {
            var4 = var5 * 3;
         }

         var3 += var4;
      }

      var3 = 10 - var3 % 10;
      var2 = var3;
      if(var3 == 10) {
         var2 = 0;
      }

      var8.append(var2);
      ResultPoint[] var12 = var0.getFinderPattern().getResultPoints();
      ResultPoint[] var7 = var1.getFinderPattern().getResultPoints();
      String var10 = String.valueOf(var8.toString());
      ResultPoint var11 = var12[0];
      ResultPoint var15 = var12[1];
      ResultPoint var13 = var7[0];
      ResultPoint var9 = var7[1];
      BarcodeFormat var14 = BarcodeFormat.RSS14;
      return new Result(var10, (byte[])null, new ResultPoint[]{var11, var15, var13, var9}, var14);
   }

   private DataCharacter decodeDataCharacter(BitArray var1, FinderPattern var2, boolean var3) throws NotFoundException {
      int[] var11 = this.dataCharacterCounters;
      var11[0] = 0;
      var11[1] = 0;
      var11[2] = 0;
      var11[3] = 0;
      var11[4] = 0;
      var11[5] = 0;
      var11[6] = 0;
      var11[7] = 0;
      int var6;
      int var7;
      int var8;
      if(var3) {
         recordPatternInReverse(var1, var2.getStartEnd()[0], var11);
      } else {
         recordPattern(var1, var2.getStartEnd()[1] + 1, var11);
         var7 = 0;

         for(var6 = var11.length - 1; var7 < var6; --var6) {
            var8 = var11[var7];
            var11[var7] = var11[var6];
            var11[var6] = var8;
            ++var7;
         }
      }

      byte var17;
      if(var3) {
         var17 = 16;
      } else {
         var17 = 15;
      }

      float var4 = (float)count(var11) / (float)var17;
      int[] var13 = this.oddCounts;
      int[] var12 = this.evenCounts;
      float[] var14 = this.oddRoundingErrors;
      float[] var16 = this.evenRoundingErrors;

      int var9;
      for(var8 = 0; var8 < var11.length; ++var8) {
         float var5 = (float)var11[var8] / var4;
         var9 = (int)(0.5F + var5);
         if(var9 < 1) {
            var6 = 1;
         } else {
            var6 = var9;
            if(var9 > 8) {
               var6 = 8;
            }
         }

         var9 = var8 >> 1;
         if((var8 & 1) == 0) {
            var13[var9] = var6;
            var14[var9] = var5 - (float)var6;
         } else {
            var12[var9] = var6;
            var16[var9] = var5 - (float)var6;
         }
      }

      this.adjustOddEvenCounts(var3, var17);
      var8 = var13.length - 1;
      var7 = 0;

      int var10;
      for(var6 = 0; var8 >= 0; var6 += var9) {
         var10 = var13[var8];
         var9 = var13[var8];
         --var8;
         var7 = var7 * 9 + var10;
      }

      var9 = 0;
      var8 = 0;

      for(var10 = var12.length - 1; var10 >= 0; --var10) {
         var9 = var9 * 9 + var12[var10];
         var8 += var12[var10];
      }

      var7 += var9 * 3;
      DataCharacter var15;
      if(var3) {
         if((var6 & 1) != 0 || var6 > 12 || var6 < 4) {
            throw NotFoundException.getNotFoundInstance();
         }

         var6 = (12 - var6) / 2;
         var9 = OUTSIDE_ODD_WIDEST[var6];
         var8 = RSSUtils.getRSSvalue(var13, var9, false);
         var9 = RSSUtils.getRSSvalue(var12, 9 - var9, true);
         var15 = new DataCharacter(var8 * OUTSIDE_EVEN_TOTAL_SUBSET[var6] + var9 + OUTSIDE_GSUM[var6], var7);
      } else {
         if((var8 & 1) != 0 || var8 > 10 || var8 < 4) {
            throw NotFoundException.getNotFoundInstance();
         }

         var8 = (10 - var8) / 2;
         var6 = INSIDE_ODD_WIDEST[var8];
         var15 = new DataCharacter(RSSUtils.getRSSvalue(var13, var6, true) + RSSUtils.getRSSvalue(var12, 9 - var6, false) * INSIDE_ODD_TOTAL_SUBSET[var8] + INSIDE_GSUM[var8], var7);
      }

      return var15;
   }

   private Pair decodePair(BitArray param1, boolean param2, int param3, Hashtable param4) {
      // $FF: Couldn't be decompiled
   }

   private int[] findFinderPattern(BitArray var1, int var2, boolean var3) throws NotFoundException {
      int[] var9 = this.decodeFinderCounters;
      var9[0] = 0;
      var9[1] = 0;
      var9[2] = 0;
      var9[3] = 0;
      int var7 = var1.getSize();

      boolean var8;
      for(var8 = false; var2 < var7; ++var2) {
         if(!var1.get(var2)) {
            var8 = true;
         } else {
            var8 = false;
         }

         if(var3 == var8) {
            break;
         }
      }

      int var5 = var2;
      var3 = var8;

      int var6;
      for(int var4 = 0; var5 < var7; var4 = var6) {
         if(var1.get(var5) ^ var3) {
            ++var9[var4];
            var6 = var4;
            var4 = var2;
         } else {
            if(var4 == 3) {
               if(isFinderPattern(var9)) {
                  return new int[]{var2, var5};
               }

               var6 = var2 + var9[0] + var9[1];
               var9[0] = var9[2];
               var9[1] = var9[3];
               var9[2] = 0;
               var9[3] = 0;
               var2 = var4 - 1;
               var4 = var6;
            } else {
               var6 = var4 + 1;
               var4 = var2;
               var2 = var6;
            }

            var9[var2] = 1;
            if(!var3) {
               var3 = true;
               var6 = var2;
            } else {
               var3 = false;
               var6 = var2;
            }
         }

         ++var5;
         var2 = var4;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private FinderPattern parseFoundFinderPattern(BitArray var1, int var2, boolean var3, int[] var4) throws NotFoundException {
      boolean var9 = var1.get(var4[0]);

      int var5;
      for(var5 = var4[0] - 1; var5 >= 0 && var1.get(var5) ^ var9; --var5) {
         ;
      }

      int var7 = var5 + 1;
      int var6 = var4[0];
      int[] var10 = this.decodeFinderCounters;

      for(var5 = var10.length - 1; var5 > 0; --var5) {
         var10[var5] = var10[var5 - 1];
      }

      var10[0] = var6 - var7;
      int var8 = parseFinderValue(var10, FINDER_PATTERNS);
      var6 = var4[1];
      if(var3) {
         var5 = var1.getSize() - 1 - var7;
         var6 = var1.getSize() - 1 - var6;
      } else {
         var5 = var7;
      }

      return new FinderPattern(var8, new int[]{var7, var4[1]}, var5, var6, var2);
   }

   public Result decodeRow(int var1, BitArray var2, Hashtable var3) throws NotFoundException {
      Pair var7 = this.decodePair(var2, false, var1, var3);
      addOrTally(this.possibleLeftPairs, var7);
      var2.reverse();
      Pair var9 = this.decodePair(var2, true, var1, var3);
      addOrTally(this.possibleRightPairs, var9);
      var2.reverse();
      int var6 = this.possibleLeftPairs.size();
      int var5 = this.possibleRightPairs.size();

      for(var1 = 0; var1 < var6; ++var1) {
         Pair var8 = (Pair)this.possibleLeftPairs.elementAt(var1);
         if(var8.getCount() > 1) {
            for(int var4 = 0; var4 < var5; ++var4) {
               var9 = (Pair)this.possibleRightPairs.elementAt(var4);
               if(var9.getCount() > 1 && checkChecksum(var8, var9)) {
                  return constructResult(var8, var9);
               }
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public void reset() {
      this.possibleLeftPairs.setSize(0);
      this.possibleRightPairs.setSize(0);
   }
}
