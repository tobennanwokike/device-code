package com.google.zxing.multi.qrcode.detector;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.Collections;
import com.google.zxing.common.Comparator;
import com.google.zxing.qrcode.detector.FinderPattern;
import com.google.zxing.qrcode.detector.FinderPatternFinder;
import com.google.zxing.qrcode.detector.FinderPatternInfo;
import java.util.Hashtable;
import java.util.Vector;

final class MultiFinderPatternFinder extends FinderPatternFinder {
   private static final float DIFF_MODSIZE_CUTOFF = 0.5F;
   private static final float DIFF_MODSIZE_CUTOFF_PERCENT = 0.05F;
   private static final FinderPatternInfo[] EMPTY_RESULT_ARRAY = new FinderPatternInfo[0];
   private static final float MAX_MODULE_COUNT_PER_EDGE = 180.0F;
   private static final float MIN_MODULE_COUNT_PER_EDGE = 9.0F;

   MultiFinderPatternFinder(BitMatrix var1) {
      super(var1);
   }

   MultiFinderPatternFinder(BitMatrix var1, ResultPointCallback var2) {
      super(var1, var2);
   }

   private FinderPattern[][] selectBestPatterns() throws NotFoundException {
      Vector var12 = this.getPossibleCenters();
      int var8 = var12.size();
      if(var8 < 3) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         FinderPattern[][] var9;
         if(var8 == 3) {
            var9 = new FinderPattern[][]{{(FinderPattern)var12.elementAt(0), (FinderPattern)var12.elementAt(1), (FinderPattern)var12.elementAt(2)}};
         } else {
            Collections.insertionSort(var12, new MultiFinderPatternFinder.ModuleSizeComparator(null));
            Vector var10 = new Vector();

            int var5;
            for(var5 = 0; var5 < var8 - 2; ++var5) {
               FinderPattern var15 = (FinderPattern)var12.elementAt(var5);
               if(var15 != null) {
                  for(int var6 = var5 + 1; var6 < var8 - 1; ++var6) {
                     FinderPattern var11 = (FinderPattern)var12.elementAt(var6);
                     if(var11 != null) {
                        float var1 = (var15.getEstimatedModuleSize() - var11.getEstimatedModuleSize()) / Math.min(var15.getEstimatedModuleSize(), var11.getEstimatedModuleSize());
                        if(Math.abs(var15.getEstimatedModuleSize() - var11.getEstimatedModuleSize()) > 0.5F && var1 >= 0.05F) {
                           break;
                        }

                        for(int var7 = var6 + 1; var7 < var8; ++var7) {
                           FinderPattern var14 = (FinderPattern)var12.elementAt(var7);
                           if(var14 != null) {
                              var1 = (var11.getEstimatedModuleSize() - var14.getEstimatedModuleSize()) / Math.min(var11.getEstimatedModuleSize(), var14.getEstimatedModuleSize());
                              if(Math.abs(var11.getEstimatedModuleSize() - var14.getEstimatedModuleSize()) > 0.5F && var1 >= 0.05F) {
                                 break;
                              }

                              FinderPattern[] var13 = new FinderPattern[]{var15, var11, var14};
                              ResultPoint.orderBestPatterns(var13);
                              FinderPatternInfo var16 = new FinderPatternInfo(var13);
                              float var4 = ResultPoint.distance(var16.getTopLeft(), var16.getBottomLeft());
                              var1 = ResultPoint.distance(var16.getTopRight(), var16.getBottomLeft());
                              float var2 = ResultPoint.distance(var16.getTopLeft(), var16.getTopRight());
                              float var3 = (var4 + var2) / var15.getEstimatedModuleSize() / 2.0F;
                              if(var3 <= 180.0F && var3 >= 9.0F && Math.abs((var4 - var2) / Math.min(var4, var2)) < 0.1F) {
                                 var2 = (float)Math.sqrt((double)(var2 * var2 + var4 * var4));
                                 if(Math.abs((var1 - var2) / Math.min(var1, var2)) < 0.1F) {
                                    var10.addElement(var13);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            if(var10.isEmpty()) {
               throw NotFoundException.getNotFoundInstance();
            }

            var9 = new FinderPattern[var10.size()][];

            for(var5 = 0; var5 < var10.size(); ++var5) {
               var9[var5] = (FinderPattern[])((FinderPattern[])var10.elementAt(var5));
            }
         }

         return var9;
      }
   }

   public FinderPatternInfo[] findMulti(Hashtable var1) throws NotFoundException {
      byte var7 = 0;
      boolean var2;
      if(var1 != null && var1.containsKey(DecodeHintType.TRY_HARDER)) {
         var2 = true;
      } else {
         var2 = false;
      }

      BitMatrix var12 = this.getImage();
      int var8 = var12.getHeight();
      int var9 = var12.getWidth();
      int var5 = (int)((float)var8 / 228.0F * 3.0F);
      if(var5 < 3 || var2) {
         var5 = 3;
      }

      int[] var10 = new int[5];

      int var15;
      for(int var6 = var5 - 1; var6 < var8; var6 += var5) {
         var10[0] = 0;
         var10[1] = 0;
         var10[2] = 0;
         var10[3] = 0;
         var10[4] = 0;
         var15 = 0;

         for(int var3 = 0; var15 < var9; ++var15) {
            if(var12.get(var15, var6)) {
               int var4 = var3;
               if((var3 & 1) == 1) {
                  var4 = var3 + 1;
               }

               ++var10[var4];
               var3 = var4;
            } else if((var3 & 1) != 0) {
               ++var10[var3];
            } else if(var3 == 4) {
               if(!foundPatternCross(var10)) {
                  var10[0] = var10[2];
                  var10[1] = var10[3];
                  var10[2] = var10[4];
                  var10[3] = 1;
                  var10[4] = 0;
                  var3 = 3;
               } else {
                  if(!this.handlePossibleCenter(var10, var6, var15)) {
                     do {
                        var3 = var15 + 1;
                        if(var3 >= var9) {
                           break;
                        }

                        var15 = var3;
                     } while(!var12.get(var3, var6));

                     var15 = var3 - 1;
                  }

                  var10[0] = 0;
                  var10[1] = 0;
                  var10[2] = 0;
                  var10[3] = 0;
                  var10[4] = 0;
                  var3 = 0;
               }
            } else {
               ++var3;
               ++var10[var3];
            }
         }

         if(foundPatternCross(var10)) {
            this.handlePossibleCenter(var10, var6, var9);
         }
      }

      FinderPattern[][] var13 = this.selectBestPatterns();
      Vector var16 = new Vector();

      for(var15 = 0; var15 < var13.length; ++var15) {
         FinderPattern[] var11 = var13[var15];
         ResultPoint.orderBestPatterns(var11);
         var16.addElement(new FinderPatternInfo(var11));
      }

      FinderPatternInfo[] var14;
      if(var16.isEmpty()) {
         var14 = EMPTY_RESULT_ARRAY;
      } else {
         var14 = new FinderPatternInfo[var16.size()];

         for(var15 = var7; var15 < var16.size(); ++var15) {
            var14[var15] = (FinderPatternInfo)var16.elementAt(var15);
         }
      }

      return var14;
   }

   private static class ModuleSizeComparator implements Comparator {
      private ModuleSizeComparator() {
      }

      ModuleSizeComparator(Object var1) {
         this();
      }

      public int compare(Object var1, Object var2) {
         float var3 = ((FinderPattern)var2).getEstimatedModuleSize() - ((FinderPattern)var1).getEstimatedModuleSize();
         byte var4;
         if((double)var3 < 0.0D) {
            var4 = -1;
         } else if((double)var3 > 0.0D) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         return var4;
      }
   }
}
