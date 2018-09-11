package com.google.zxing;

import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

public abstract class Binarizer {
   private final LuminanceSource source;

   protected Binarizer(LuminanceSource var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("Source must be non-null.");
      } else {
         this.source = var1;
      }
   }

   public abstract Binarizer createBinarizer(LuminanceSource var1);

   public abstract BitMatrix getBlackMatrix() throws NotFoundException;

   public abstract BitArray getBlackRow(int var1, BitArray var2) throws NotFoundException;

   public LuminanceSource getLuminanceSource() {
      return this.source;
   }
}
