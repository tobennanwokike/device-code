package com.google.zxing.common;

import com.google.zxing.common.Comparator;
import java.util.Vector;

public final class Collections {
   public static void insertionSort(Vector var0, Comparator var1) {
      int var4 = var0.size();

      for(int var2 = 1; var2 < var4; ++var2) {
         Object var5 = var0.elementAt(var2);

         int var3;
         for(var3 = var2 - 1; var3 >= 0; --var3) {
            Object var6 = var0.elementAt(var3);
            if(var1.compare(var6, var5) <= 0) {
               break;
            }

            var0.setElementAt(var6, var3 + 1);
         }

         var0.setElementAt(var5, var3 + 1);
      }

   }
}
