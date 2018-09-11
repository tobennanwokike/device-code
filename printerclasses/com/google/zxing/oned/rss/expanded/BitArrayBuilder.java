package com.google.zxing.oned.rss.expanded;

import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.expanded.ExpandedPair;
import java.util.Vector;

final class BitArrayBuilder {
   static BitArray buildBitArray(Vector var0) {
      int var1 = (var0.size() << 1) - 1;
      if(((ExpandedPair)var0.lastElement()).getRightChar() == null) {
         --var1;
      }

      BitArray var6 = new BitArray(var1 * 12);
      int var3 = ((ExpandedPair)var0.elementAt(0)).getRightChar().getValue();
      int var2 = 11;

      for(var1 = 0; var2 >= 0; ++var1) {
         if((1 << var2 & var3) != 0) {
            var6.set(var1);
         }

         --var2;
      }

      for(var3 = 1; var3 < var0.size(); var1 = var2) {
         ExpandedPair var7 = (ExpandedPair)var0.elementAt(var3);
         int var4 = var7.getLeftChar().getValue();

         for(var2 = 11; var2 >= 0; ++var1) {
            if((1 << var2 & var4) != 0) {
               var6.set(var1);
            }

            --var2;
         }

         var2 = var1;
         if(var7.getRightChar() != null) {
            int var5 = var7.getRightChar().getValue();
            var4 = 11;

            while(true) {
               var2 = var1;
               if(var4 < 0) {
                  break;
               }

               if((1 << var4 & var5) != 0) {
                  var6.set(var1);
               }

               ++var1;
               --var4;
            }
         }

         ++var3;
      }

      return var6;
   }
}
