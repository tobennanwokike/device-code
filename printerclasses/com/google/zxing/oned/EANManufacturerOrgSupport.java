package com.google.zxing.oned;

import java.util.Vector;

final class EANManufacturerOrgSupport {
   private final Vector countryIdentifiers = new Vector();
   private final Vector ranges = new Vector();

   private void add(int[] var1, String var2) {
      this.ranges.addElement(var1);
      this.countryIdentifiers.addElement(var2);
   }

   private void initIfNeeded() {
      // $FF: Couldn't be decompiled
   }

   String lookupCountryIdentifier(String var1) {
      this.initIfNeeded();
      int var5 = Integer.parseInt(var1.substring(0, 3));
      int var4 = this.ranges.size();
      int var2 = 0;

      while(true) {
         if(var2 >= var4) {
            var1 = null;
            break;
         }

         int[] var6 = (int[])((int[])this.ranges.elementAt(var2));
         int var3 = var6[0];
         if(var5 < var3) {
            var1 = null;
            break;
         }

         if(var6.length != 1) {
            var3 = var6[1];
         }

         if(var5 <= var3) {
            var1 = (String)this.countryIdentifiers.elementAt(var2);
            break;
         }

         ++var2;
      }

      return var1;
   }
}
