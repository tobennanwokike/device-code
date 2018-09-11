package com.google.zxing;

public abstract class LuminanceSource {
   private final int height;
   private final int width;

   protected LuminanceSource(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public LuminanceSource crop(int var1, int var2, int var3, int var4) {
      throw new RuntimeException("This luminance source does not support cropping.");
   }

   public final int getHeight() {
      return this.height;
   }

   public abstract byte[] getMatrix();

   public abstract byte[] getRow(int var1, byte[] var2);

   public final int getWidth() {
      return this.width;
   }

   public boolean isCropSupported() {
      return false;
   }

   public boolean isRotateSupported() {
      return false;
   }

   public LuminanceSource rotateCounterClockwise() {
      throw new RuntimeException("This luminance source does not support rotation.");
   }
}
