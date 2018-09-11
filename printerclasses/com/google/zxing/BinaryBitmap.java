package com.google.zxing;

import com.google.zxing.Binarizer;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

public final class BinaryBitmap {
   private final Binarizer binarizer;
   private BitMatrix matrix;

   public BinaryBitmap(Binarizer var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("Binarizer must be non-null.");
      } else {
         this.binarizer = var1;
         this.matrix = null;
      }
   }

   public BinaryBitmap crop(int var1, int var2, int var3, int var4) {
      LuminanceSource var5 = this.binarizer.getLuminanceSource().crop(var1, var2, var3, var4);
      return new BinaryBitmap(this.binarizer.createBinarizer(var5));
   }

   public BitMatrix getBlackMatrix() throws NotFoundException {
      if(this.matrix == null) {
         this.matrix = this.binarizer.getBlackMatrix();
      }

      return this.matrix;
   }

   public BitArray getBlackRow(int var1, BitArray var2) throws NotFoundException {
      return this.binarizer.getBlackRow(var1, var2);
   }

   public int getHeight() {
      return this.binarizer.getLuminanceSource().getHeight();
   }

   public int getWidth() {
      return this.binarizer.getLuminanceSource().getWidth();
   }

   public boolean isCropSupported() {
      return this.binarizer.getLuminanceSource().isCropSupported();
   }

   public boolean isRotateSupported() {
      return this.binarizer.getLuminanceSource().isRotateSupported();
   }

   public BinaryBitmap rotateCounterClockwise() {
      LuminanceSource var1 = this.binarizer.getLuminanceSource().rotateCounterClockwise();
      return new BinaryBitmap(this.binarizer.createBinarizer(var1));
   }
}
