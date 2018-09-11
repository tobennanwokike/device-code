package com.google.zxing.oned.rss.expanded;

import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;

final class ExpandedPair {
   private final FinderPattern finderPattern;
   private final DataCharacter leftChar;
   private final boolean mayBeLast;
   private final DataCharacter rightChar;

   ExpandedPair(DataCharacter var1, DataCharacter var2, FinderPattern var3, boolean var4) {
      this.leftChar = var1;
      this.rightChar = var2;
      this.finderPattern = var3;
      this.mayBeLast = var4;
   }

   FinderPattern getFinderPattern() {
      return this.finderPattern;
   }

   DataCharacter getLeftChar() {
      return this.leftChar;
   }

   DataCharacter getRightChar() {
      return this.rightChar;
   }

   boolean mayBeLast() {
      return this.mayBeLast;
   }

   public boolean mustBeLast() {
      boolean var1;
      if(this.rightChar == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
