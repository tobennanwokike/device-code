package com.google.zxing.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.Collections;
import com.google.zxing.common.Comparator;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.util.Hashtable;
import java.util.Vector;

public class FinderPatternFinder {
   private static final int CENTER_QUORUM = 2;
   private static final int INTEGER_MATH_SHIFT = 8;
   protected static final int MAX_MODULES = 57;
   protected static final int MIN_SKIP = 3;
   private final int[] crossCheckStateCount;
   private boolean hasSkipped;
   private final BitMatrix image;
   private final Vector possibleCenters;
   private final ResultPointCallback resultPointCallback;

   public FinderPatternFinder(BitMatrix var1) {
      this(var1, (ResultPointCallback)null);
   }

   public FinderPatternFinder(BitMatrix var1, ResultPointCallback var2) {
      this.image = var1;
      this.possibleCenters = new Vector();
      this.crossCheckStateCount = new int[5];
      this.resultPointCallback = var2;
   }

   private static float centerFromEnd(int[] var0, int var1) {
      return (float)(var1 - var0[4] - var0[3]) - (float)var0[2] / 2.0F;
   }

   private float crossCheckHorizontal(int var1, int var2, int var3, int var4) {
      float var6 = Float.NaN;
      BitMatrix var11 = this.image;
      int var9 = var11.getWidth();
      int[] var10 = this.getCrossCheckStateCount();

      int var8;
      for(var8 = var1; var8 >= 0 && var11.get(var8, var2); --var8) {
         ++var10[2];
      }

      int var7 = var8;
      float var5;
      if(var8 < 0) {
         var5 = var6;
      } else {
         while(var7 >= 0 && !var11.get(var7, var2) && var10[1] <= var3) {
            ++var10[1];
            --var7;
         }

         var5 = var6;
         if(var7 >= 0) {
            var5 = var6;
            if(var10[1] <= var3) {
               while(var7 >= 0 && var11.get(var7, var2) && var10[0] <= var3) {
                  ++var10[0];
                  --var7;
               }

               var5 = var6;
               if(var10[0] <= var3) {
                  ++var1;

                  while(var1 < var9 && var11.get(var1, var2)) {
                     ++var10[2];
                     ++var1;
                  }

                  var5 = var6;
                  if(var1 != var9) {
                     while(var1 < var9 && !var11.get(var1, var2) && var10[3] < var3) {
                        ++var10[3];
                        ++var1;
                     }

                     var5 = var6;
                     if(var1 != var9) {
                        var5 = var6;
                        if(var10[3] < var3) {
                           while(var1 < var9 && var11.get(var1, var2) && var10[4] < var3) {
                              ++var10[4];
                              ++var1;
                           }

                           var5 = var6;
                           if(var10[4] < var3) {
                              var5 = var6;
                              if(Math.abs(var10[0] + var10[1] + var10[2] + var10[3] + var10[4] - var4) * 5 < var4) {
                                 var5 = var6;
                                 if(foundPatternCross(var10)) {
                                    var5 = centerFromEnd(var10, var1);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var5;
   }

   private float crossCheckVertical(int var1, int var2, int var3, int var4) {
      float var6 = Float.NaN;
      BitMatrix var11 = this.image;
      int var9 = var11.getHeight();
      int[] var10 = this.getCrossCheckStateCount();

      int var8;
      for(var8 = var1; var8 >= 0 && var11.get(var2, var8); --var8) {
         ++var10[2];
      }

      int var7 = var8;
      float var5;
      if(var8 < 0) {
         var5 = var6;
      } else {
         while(var7 >= 0 && !var11.get(var2, var7) && var10[1] <= var3) {
            ++var10[1];
            --var7;
         }

         var5 = var6;
         if(var7 >= 0) {
            var5 = var6;
            if(var10[1] <= var3) {
               while(var7 >= 0 && var11.get(var2, var7) && var10[0] <= var3) {
                  ++var10[0];
                  --var7;
               }

               var5 = var6;
               if(var10[0] <= var3) {
                  ++var1;

                  while(var1 < var9 && var11.get(var2, var1)) {
                     ++var10[2];
                     ++var1;
                  }

                  var5 = var6;
                  if(var1 != var9) {
                     while(var1 < var9 && !var11.get(var2, var1) && var10[3] < var3) {
                        ++var10[3];
                        ++var1;
                     }

                     var5 = var6;
                     if(var1 != var9) {
                        var5 = var6;
                        if(var10[3] < var3) {
                           while(var1 < var9 && var11.get(var2, var1) && var10[4] < var3) {
                              ++var10[4];
                              ++var1;
                           }

                           var5 = var6;
                           if(var10[4] < var3) {
                              var5 = var6;
                              if(Math.abs(var10[0] + var10[1] + var10[2] + var10[3] + var10[4] - var4) * 5 < var4 * 2) {
                                 var5 = var6;
                                 if(foundPatternCross(var10)) {
                                    var5 = centerFromEnd(var10, var1);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var5;
   }

   private int findRowSkip() {
      int var2 = this.possibleCenters.size();
      int var1;
      if(var2 <= 1) {
         var1 = 0;
      } else {
         FinderPattern var3 = null;
         var1 = 0;

         while(true) {
            if(var1 >= var2) {
               var1 = 0;
               break;
            }

            FinderPattern var4 = (FinderPattern)this.possibleCenters.elementAt(var1);
            if(var4.getCount() >= 2) {
               if(var3 != null) {
                  this.hasSkipped = true;
                  var1 = (int)(Math.abs(var3.getX() - var4.getX()) - Math.abs(var3.getY() - var4.getY())) / 2;
                  break;
               }

               var3 = var4;
            }

            ++var1;
         }
      }

      return var1;
   }

   protected static boolean foundPatternCross(int[] var0) {
      boolean var5 = true;
      boolean var4 = false;
      int var1 = 0;
      int var2 = 0;

      while(true) {
         if(var1 >= 5) {
            if(var2 >= 7) {
               var2 = (var2 << 8) / 7;
               var1 = var2 / 2;
               if(Math.abs(var2 - (var0[0] << 8)) < var1 && Math.abs(var2 - (var0[1] << 8)) < var1 && Math.abs(var2 * 3 - (var0[2] << 8)) < var1 * 3 && Math.abs(var2 - (var0[3] << 8)) < var1 && Math.abs(var2 - (var0[4] << 8)) < var1) {
                  var4 = var5;
               } else {
                  var4 = false;
               }
            }
            break;
         }

         int var3 = var0[var1];
         if(var3 == 0) {
            break;
         }

         var2 += var3;
         ++var1;
      }

      return var4;
   }

   private int[] getCrossCheckStateCount() {
      this.crossCheckStateCount[0] = 0;
      this.crossCheckStateCount[1] = 0;
      this.crossCheckStateCount[2] = 0;
      this.crossCheckStateCount[3] = 0;
      this.crossCheckStateCount[4] = 0;
      return this.crossCheckStateCount;
   }

   private boolean haveMultiplyConfirmedCenters() {
      float var2 = 0.0F;
      boolean var7 = false;
      int var6 = this.possibleCenters.size();
      int var5 = 0;
      float var1 = 0.0F;

      int var4;
      for(var4 = 0; var5 < var6; ++var5) {
         FinderPattern var8 = (FinderPattern)this.possibleCenters.elementAt(var5);
         if(var8.getCount() >= 2) {
            var1 += var8.getEstimatedModuleSize();
            ++var4;
         }
      }

      if(var4 >= 3) {
         float var3 = var1 / (float)var6;

         for(var4 = 0; var4 < var6; ++var4) {
            var2 += Math.abs(((FinderPattern)this.possibleCenters.elementAt(var4)).getEstimatedModuleSize() - var3);
         }

         if(var2 <= 0.05F * var1) {
            var7 = true;
         } else {
            var7 = false;
         }
      }

      return var7;
   }

   private FinderPattern[] selectBestPatterns() throws NotFoundException {
      float var3 = 0.0F;
      int var6 = this.possibleCenters.size();
      if(var6 < 3) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         float var1;
         int var5;
         if(var6 > 3) {
            var5 = 0;
            var1 = 0.0F;

            float var2;
            for(var2 = 0.0F; var5 < var6; ++var5) {
               float var4 = ((FinderPattern)this.possibleCenters.elementAt(var5)).getEstimatedModuleSize();
               var2 += var4;
               var1 += var4 * var4;
            }

            var2 /= (float)var6;
            var1 = (float)Math.sqrt((double)(var1 / (float)var6 - var2 * var2));
            Collections.insertionSort(this.possibleCenters, new FinderPatternFinder.FurthestFromAverageComparator(var2));
            var1 = Math.max(0.2F * var2, var1);

            for(var5 = 0; var5 < this.possibleCenters.size() && this.possibleCenters.size() > 3; var5 = var6 + 1) {
               var6 = var5;
               if(Math.abs(((FinderPattern)this.possibleCenters.elementAt(var5)).getEstimatedModuleSize() - var2) > var1) {
                  this.possibleCenters.removeElementAt(var5);
                  var6 = var5 - 1;
               }
            }
         }

         if(this.possibleCenters.size() > 3) {
            var5 = 0;

            for(var1 = var3; var5 < this.possibleCenters.size(); ++var5) {
               var1 += ((FinderPattern)this.possibleCenters.elementAt(var5)).getEstimatedModuleSize();
            }

            var1 /= (float)this.possibleCenters.size();
            Collections.insertionSort(this.possibleCenters, new FinderPatternFinder.CenterComparator(var1));
            this.possibleCenters.setSize(3);
         }

         return new FinderPattern[]{(FinderPattern)this.possibleCenters.elementAt(0), (FinderPattern)this.possibleCenters.elementAt(1), (FinderPattern)this.possibleCenters.elementAt(2)};
      }
   }

   FinderPatternInfo find(Hashtable var1) throws NotFoundException {
      boolean var2;
      if(var1 != null && var1.containsKey(DecodeHintType.TRY_HARDER)) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var8 = this.image.getHeight();
      int var7 = this.image.getWidth();
      int var3 = var8 * 3 / 228;
      int var13;
      if(var3 >= 3 && !var2) {
         var13 = var3;
      } else {
         var13 = 3;
      }

      int[] var11 = new int[5];
      int var4 = var13 - 1;

      boolean var10;
      for(boolean var9 = false; var4 < var8 && !var9; var9 = var10) {
         var11[0] = 0;
         var11[1] = 0;
         var11[2] = 0;
         var11[3] = 0;
         var11[4] = 0;
         var3 = 0;
         byte var6 = 0;
         int var5 = var13;

         for(var13 = var6; var3 < var7; ++var3) {
            if(this.image.get(var3, var4)) {
               int var14 = var13;
               if((var13 & 1) == 1) {
                  var14 = var13 + 1;
               }

               ++var11[var14];
               var13 = var14;
            } else if((var13 & 1) == 0) {
               if(var13 == 4) {
                  if(foundPatternCross(var11)) {
                     if(this.handlePossibleCenter(var11, var4, var3)) {
                        if(this.hasSkipped) {
                           var9 = this.haveMultiplyConfirmedCenters();
                        } else {
                           var13 = this.findRowSkip();
                           if(var13 > var11[2]) {
                              var3 = var4 + (var13 - var11[2] - 2);
                              var13 = var7 - 1;
                           } else {
                              var13 = var3;
                              var3 = var4;
                           }

                           var4 = var3;
                           var3 = var13;
                        }

                        var11[0] = 0;
                        var11[1] = 0;
                        var11[2] = 0;
                        var11[3] = 0;
                        var11[4] = 0;
                        var5 = 2;
                        var13 = 0;
                     } else {
                        var11[0] = var11[2];
                        var11[1] = var11[3];
                        var11[2] = var11[4];
                        var11[3] = 1;
                        var11[4] = 0;
                        var13 = 3;
                     }
                  } else {
                     var11[0] = var11[2];
                     var11[1] = var11[3];
                     var11[2] = var11[4];
                     var11[3] = 1;
                     var11[4] = 0;
                     var13 = 3;
                  }
               } else {
                  ++var13;
                  ++var11[var13];
               }
            } else {
               ++var11[var13];
            }
         }

         var10 = var9;
         var13 = var5;
         if(foundPatternCross(var11)) {
            var10 = var9;
            var13 = var5;
            if(this.handlePossibleCenter(var11, var4, var7)) {
               var3 = var11[0];
               var10 = var9;
               var13 = var3;
               if(this.hasSkipped) {
                  var10 = this.haveMultiplyConfirmedCenters();
                  var13 = var3;
               }
            }
         }

         var4 += var13;
      }

      FinderPattern[] var12 = this.selectBestPatterns();
      ResultPoint.orderBestPatterns(var12);
      return new FinderPatternInfo(var12);
   }

   protected BitMatrix getImage() {
      return this.image;
   }

   protected Vector getPossibleCenters() {
      return this.possibleCenters;
   }

   protected boolean handlePossibleCenter(int[] var1, int var2, int var3) {
      boolean var7 = false;
      int var8 = var1[0] + var1[1] + var1[2] + var1[3] + var1[4];
      float var5 = centerFromEnd(var1, var3);
      float var4 = this.crossCheckVertical(var2, (int)var5, var1[2], var8);
      boolean var9;
      if(!Float.isNaN(var4)) {
         var5 = this.crossCheckHorizontal((int)var5, (int)var4, var1[2], var8);
         if(!Float.isNaN(var5)) {
            float var6 = (float)var8 / 7.0F;
            var8 = this.possibleCenters.size();
            var3 = 0;

            FinderPattern var10;
            boolean var11;
            while(true) {
               var11 = var7;
               if(var3 >= var8) {
                  break;
               }

               var10 = (FinderPattern)this.possibleCenters.elementAt(var3);
               if(var10.aboutEquals(var6, var4, var5)) {
                  var10.incrementCount();
                  var11 = true;
                  break;
               }

               ++var3;
            }

            if(!var11) {
               var10 = new FinderPattern(var5, var4, var6);
               this.possibleCenters.addElement(var10);
               if(this.resultPointCallback != null) {
                  this.resultPointCallback.foundPossibleResultPoint(var10);
               }
            }

            var9 = true;
            return var9;
         }
      }

      var9 = false;
      return var9;
   }

   private static class CenterComparator implements Comparator {
      private final float average;

      public CenterComparator(float var1) {
         this.average = var1;
      }

      public int compare(Object var1, Object var2) {
         int var5;
         if(((FinderPattern)var2).getCount() != ((FinderPattern)var1).getCount()) {
            var5 = ((FinderPattern)var2).getCount() - ((FinderPattern)var1).getCount();
         } else {
            float var3 = Math.abs(((FinderPattern)var2).getEstimatedModuleSize() - this.average);
            float var4 = Math.abs(((FinderPattern)var1).getEstimatedModuleSize() - this.average);
            if(var3 < var4) {
               var5 = 1;
            } else if(var3 == var4) {
               var5 = 0;
            } else {
               var5 = -1;
            }
         }

         return var5;
      }
   }

   private static class FurthestFromAverageComparator implements Comparator {
      private final float average;

      public FurthestFromAverageComparator(float var1) {
         this.average = var1;
      }

      public int compare(Object var1, Object var2) {
         float var4 = Math.abs(((FinderPattern)var2).getEstimatedModuleSize() - this.average);
         float var3 = Math.abs(((FinderPattern)var1).getEstimatedModuleSize() - this.average);
         byte var5;
         if(var4 < var3) {
            var5 = -1;
         } else if(var4 == var3) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         return var5;
      }
   }
}
