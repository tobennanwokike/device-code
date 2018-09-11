package com.google.zxing.common;

import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class DetectorResult {
   private final BitMatrix bits;
   private final ResultPoint[] points;

   public DetectorResult(BitMatrix var1, ResultPoint[] var2) {
      this.bits = var1;
      this.points = var2;
   }

   public BitMatrix getBits() {
      return this.bits;
   }

   public ResultPoint[] getPoints() {
      return this.points;
   }
}
