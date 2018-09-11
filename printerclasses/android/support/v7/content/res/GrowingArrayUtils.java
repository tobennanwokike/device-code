package android.support.v7.content.res;

import java.lang.reflect.Array;

final class GrowingArrayUtils {
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   static {
      boolean var0;
      if(!GrowingArrayUtils.class.desiredAssertionStatus()) {
         var0 = true;
      } else {
         var0 = false;
      }

      $assertionsDisabled = var0;
   }

   public static int[] append(int[] var0, int var1, int var2) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         int[] var3 = var0;
         if(var1 + 1 > var0.length) {
            var3 = new int[growSize(var1)];
            System.arraycopy(var0, 0, var3, 0, var1);
         }

         var3[var1] = var2;
         return var3;
      }
   }

   public static long[] append(long[] var0, int var1, long var2) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         long[] var4 = var0;
         if(var1 + 1 > var0.length) {
            var4 = new long[growSize(var1)];
            System.arraycopy(var0, 0, var4, 0, var1);
         }

         var4[var1] = var2;
         return var4;
      }
   }

   public static Object[] append(Object[] var0, int var1, Object var2) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         Object[] var3 = var0;
         if(var1 + 1 > var0.length) {
            var3 = (Object[])((Object[])Array.newInstance(var0.getClass().getComponentType(), growSize(var1)));
            System.arraycopy(var0, 0, var3, 0, var1);
         }

         var3[var1] = var2;
         return var3;
      }
   }

   public static boolean[] append(boolean[] var0, int var1, boolean var2) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         boolean[] var3 = var0;
         if(var1 + 1 > var0.length) {
            var3 = new boolean[growSize(var1)];
            System.arraycopy(var0, 0, var3, 0, var1);
         }

         var3[var1] = var2;
         return var3;
      }
   }

   public static int growSize(int var0) {
      if(var0 <= 4) {
         var0 = 8;
      } else {
         var0 *= 2;
      }

      return var0;
   }

   public static int[] insert(int[] var0, int var1, int var2, int var3) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         if(var1 + 1 <= var0.length) {
            System.arraycopy(var0, var2, var0, var2 + 1, var1 - var2);
            var0[var2] = var3;
         } else {
            int[] var4 = new int[growSize(var1)];
            System.arraycopy(var0, 0, var4, 0, var2);
            var4[var2] = var3;
            System.arraycopy(var0, var2, var4, var2 + 1, var0.length - var2);
            var0 = var4;
         }

         return var0;
      }
   }

   public static long[] insert(long[] var0, int var1, int var2, long var3) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         if(var1 + 1 <= var0.length) {
            System.arraycopy(var0, var2, var0, var2 + 1, var1 - var2);
            var0[var2] = var3;
         } else {
            long[] var5 = new long[growSize(var1)];
            System.arraycopy(var0, 0, var5, 0, var2);
            var5[var2] = var3;
            System.arraycopy(var0, var2, var5, var2 + 1, var0.length - var2);
            var0 = var5;
         }

         return var0;
      }
   }

   public static Object[] insert(Object[] var0, int var1, int var2, Object var3) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         if(var1 + 1 <= var0.length) {
            System.arraycopy(var0, var2, var0, var2 + 1, var1 - var2);
            var0[var2] = var3;
         } else {
            Object[] var4 = (Object[])((Object[])Array.newInstance(var0.getClass().getComponentType(), growSize(var1)));
            System.arraycopy(var0, 0, var4, 0, var2);
            var4[var2] = var3;
            System.arraycopy(var0, var2, var4, var2 + 1, var0.length - var2);
            var0 = var4;
         }

         return var0;
      }
   }

   public static boolean[] insert(boolean[] var0, int var1, int var2, boolean var3) {
      if(!$assertionsDisabled && var1 > var0.length) {
         throw new AssertionError();
      } else {
         if(var1 + 1 <= var0.length) {
            System.arraycopy(var0, var2, var0, var2 + 1, var1 - var2);
            var0[var2] = var3;
         } else {
            boolean[] var4 = new boolean[growSize(var1)];
            System.arraycopy(var0, 0, var4, 0, var2);
            var4[var2] = var3;
            System.arraycopy(var0, var2, var4, var2 + 1, var0.length - var2);
            var0 = var4;
         }

         return var0;
      }
   }
}
