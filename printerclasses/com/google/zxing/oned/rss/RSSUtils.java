package com.google.zxing.oned.rss;

public final class RSSUtils {
   static int combins(int var0, int var1) {
      byte var5 = 1;
      int var2;
      int var3;
      if(var0 - var1 > var1) {
         var3 = var0 - var1;
         var2 = var1;
      } else {
         var3 = var1;
         var2 = var0 - var1;
      }

      var1 = 1;
      int var4 = var0;

      int var7;
      for(var0 = var5; var4 > var3; var0 = var7) {
         int var6 = var1 * var4;
         var7 = var0;
         var1 = var6;
         if(var0 <= var2) {
            var1 = var6 / var0;
            var7 = var0 + 1;
         }

         --var4;
      }

      while(var0 <= var2) {
         var1 /= var0;
         ++var0;
      }

      return var1;
   }

   static int[] elements(int[] var0, int var1, int var2) {
      int[] var7 = new int[var0.length + 2];
      int var6 = var2 << 1;
      var7[0] = 1;
      var2 = 10;
      int var4 = 1;

      int var3;
      int var5;
      for(var3 = 1; var3 < var6 - 2; var4 = var5) {
         var7[var3] = var0[var3 - 1] - var7[var3 - 1];
         var7[var3 + 1] = var0[var3] - var7[var3];
         var5 = var4 + var7[var3] + var7[var3 + 1];
         var4 = var2;
         if(var7[var3] < var2) {
            var4 = var7[var3];
         }

         var3 += 2;
         var2 = var4;
      }

      var7[var6 - 1] = var1 - var4;
      if(var7[var6 - 1] < var2) {
         var1 = var7[var6 - 1];
      } else {
         var1 = var2;
      }

      if(var1 > 1) {
         for(var2 = 0; var2 < var6; var2 += 2) {
            var7[var2] += var1 - 1;
            var3 = var2 + 1;
            var7[var3] -= var1 - 1;
         }
      }

      return var7;
   }

   public static int getRSSvalue(int[] var0, int var1, boolean var2) {
      int var11 = var0.length;
      int var4 = 0;

      int var3;
      int var5;
      for(var3 = 0; var4 < var11; var3 += var5) {
         var5 = var0[var4];
         ++var4;
      }

      int var8 = 0;
      byte var12 = 0;
      var4 = 0;
      int var7 = var3;

      int var9;
      for(var3 = var12; var8 < var11 - 1; var7 -= var9) {
         var4 |= 1 << var8;

         for(var9 = 1; var9 < var0[var8]; var4 &= ~(1 << var8)) {
            int var6 = combins(var7 - var9 - 1, var11 - var8 - 2);
            var5 = var6;
            if(var2) {
               var5 = var6;
               if(var4 == 0) {
                  var5 = var6;
                  if(var7 - var9 - (var11 - var8 - 1) >= var11 - var8 - 1) {
                     var5 = var6 - combins(var7 - var9 - (var11 - var8), var11 - var8 - 2);
                  }
               }
            }

            if(var11 - var8 - 1 <= 1) {
               var6 = var5;
               if(var7 - var9 > var1) {
                  var6 = var5 - 1;
               }
            } else {
               int var10 = var7 - var9 - (var11 - var8 - 2);

               for(var6 = 0; var10 > var1; --var10) {
                  var6 += combins(var7 - var9 - var10 - 1, var11 - var8 - 3);
               }

               var6 = var5 - (var11 - 1 - var8) * var6;
            }

            var3 += var6;
            ++var9;
         }

         ++var8;
      }

      return var3;
   }

   static int[] getRSSwidths(int var0, int var1, int var2, int var3, boolean var4) {
      int[] var11 = new int[var2];
      byte var5 = 0;
      int var6 = 0;
      int var7 = var1;

      int var12;
      for(var1 = var5; var6 < var2 - 1; var0 = var12) {
         var1 |= 1 << var6;
         int var8 = 1;
         int var9 = var0;
         var0 = var1;

         while(true) {
            var12 = combins(var7 - var8 - 1, var2 - var6 - 2);
            var1 = var12;
            if(var4) {
               var1 = var12;
               if(var0 == 0) {
                  var1 = var12;
                  if(var7 - var8 - (var2 - var6 - 1) >= var2 - var6 - 1) {
                     var1 = var12 - combins(var7 - var8 - (var2 - var6), var2 - var6 - 2);
                  }
               }
            }

            if(var2 - var6 - 1 <= 1) {
               var12 = var1;
               if(var7 - var8 > var3) {
                  var12 = var1 - 1;
               }
            } else {
               var12 = 0;

               for(int var10 = var7 - var8 - (var2 - var6 - 2); var10 > var3; --var10) {
                  var12 += combins(var7 - var8 - var10 - 1, var2 - var6 - 3);
               }

               var12 = var1 - (var2 - 1 - var6) * var12;
            }

            var9 -= var12;
            if(var9 < 0) {
               var12 += var9;
               var7 -= var8;
               var11[var6] = var8;
               ++var6;
               var1 = var0;
               break;
            }

            ++var8;
            var0 &= ~(1 << var6);
         }
      }

      var11[var6] = var7;
      return var11;
   }
}
