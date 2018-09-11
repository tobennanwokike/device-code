package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.io.PrintWriter;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public final class TimeUtils {
   public static final int HUNDRED_DAY_FIELD_LEN = 19;
   private static final int SECONDS_PER_DAY = 86400;
   private static final int SECONDS_PER_HOUR = 3600;
   private static final int SECONDS_PER_MINUTE = 60;
   private static char[] sFormatStr = new char[24];
   private static final Object sFormatSync = new Object();

   private static int accumField(int var0, int var1, boolean var2, int var3) {
      if(var0 <= 99 && (!var2 || var3 < 3)) {
         if(var0 > 9 || var2 && var3 >= 2) {
            var0 = var1 + 2;
         } else if(!var2 && var0 <= 0) {
            var0 = 0;
         } else {
            var0 = var1 + 1;
         }
      } else {
         var0 = var1 + 3;
      }

      return var0;
   }

   public static void formatDuration(long var0, long var2, PrintWriter var4) {
      if(var0 == 0L) {
         var4.print("--");
      } else {
         formatDuration(var0 - var2, var4, 0);
      }

   }

   public static void formatDuration(long var0, PrintWriter var2) {
      formatDuration(var0, var2, 0);
   }

   public static void formatDuration(long param0, PrintWriter param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static void formatDuration(long param0, StringBuilder param2) {
      // $FF: Couldn't be decompiled
   }

   private static int formatDurationLocked(long var0, int var2) {
      if(sFormatStr.length < var2) {
         sFormatStr = new char[var2];
      }

      char[] var14 = sFormatStr;
      if(var0 != 0L) {
         byte var3;
         if(var0 > 0L) {
            var3 = 43;
         } else {
            var3 = 45;
            var0 = -var0;
         }

         int var12 = (int)(var0 % 1000L);
         int var4 = (int)Math.floor((double)(var0 / 1000L));
         int var6 = 0;
         int var7 = 0;
         int var8 = 0;
         int var5 = var4;
         if(var4 > 86400) {
            var6 = var4 / 86400;
            var5 = var4 - 86400 * var6;
         }

         var4 = var5;
         if(var5 > 3600) {
            var7 = var5 / 3600;
            var4 = var5 - var7 * 3600;
         }

         var5 = var4;
         if(var4 > 60) {
            var8 = var4 / 60;
            var5 = var4 - var8 * 60;
         }

         int var10 = 0;
         byte var11 = 0;
         int var9;
         boolean var13;
         byte var17;
         if(var2 != 0) {
            var4 = accumField(var6, 1, false, 0);
            if(var4 > 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            var4 += accumField(var7, 1, var13, 2);
            if(var4 > 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            var4 += accumField(var8, 1, var13, 2);
            if(var4 > 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            var9 = var4 + accumField(var5, 1, var13, 2);
            if(var9 > 0) {
               var17 = 3;
            } else {
               var17 = 0;
            }

            var9 += accumField(var12, 2, true, var17) + 1;
            var4 = var11;

            while(true) {
               var10 = var4;
               if(var9 >= var2) {
                  break;
               }

               var14[var4] = 32;
               ++var4;
               ++var9;
            }
         }

         var14[var10] = (char)var3;
         var9 = var10 + 1;
         boolean var15;
         if(var2 != 0) {
            var15 = true;
         } else {
            var15 = false;
         }

         var6 = printField(var14, var6, 'd', var9, false, 0);
         if(var6 != var9) {
            var13 = true;
         } else {
            var13 = false;
         }

         if(var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var6 = printField(var14, var7, 'h', var6, var13, var17);
         if(var6 != var9) {
            var13 = true;
         } else {
            var13 = false;
         }

         if(var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var6 = printField(var14, var8, 'm', var6, var13, var17);
         if(var6 != var9) {
            var13 = true;
         } else {
            var13 = false;
         }

         if(var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var4 = printField(var14, var5, 's', var6, var13, var17);
         byte var16;
         if(var15 && var4 != var9) {
            var16 = 3;
         } else {
            var16 = 0;
         }

         var2 = printField(var14, var12, 'm', var4, true, var16);
         var14[var2] = 115;
         ++var2;
      } else {
         while(var2 - 1 < 0) {
            var14[0] = 32;
         }

         var14[0] = 48;
         var2 = 1;
      }

      return var2;
   }

   private static int printField(char[] var0, int var1, char var2, int var3, boolean var4, int var5) {
      int var6;
      if(!var4) {
         var6 = var3;
         if(var1 <= 0) {
            return var6;
         }
      }

      int var7;
      label40: {
         if(!var4 || var5 < 3) {
            var6 = var1;
            var7 = var3;
            if(var1 <= 99) {
               break label40;
            }
         }

         var6 = var1 / 100;
         var0[var3] = (char)(var6 + 48);
         var7 = var3 + 1;
         var6 = var1 - var6 * 100;
      }

      label41: {
         if((!var4 || var5 < 2) && var6 <= 9) {
            var5 = var6;
            var1 = var7;
            if(var3 == var7) {
               break label41;
            }
         }

         var3 = var6 / 10;
         var0[var7] = (char)(var3 + 48);
         var1 = var7 + 1;
         var5 = var6 - var3 * 10;
      }

      var0[var1] = (char)(var5 + 48);
      ++var1;
      var0[var1] = var2;
      var6 = var1 + 1;
      return var6;
   }
}
