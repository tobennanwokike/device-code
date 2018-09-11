package com.google.zxing.oned.rss;

import com.google.zxing.ResultPoint;

public final class FinderPattern {
   private final ResultPoint[] resultPoints;
   private final int[] startEnd;
   private final int value;

   public FinderPattern(int var1, int[] var2, int var3, int var4, int var5) {
      this.value = var1;
      this.startEnd = var2;
      this.resultPoints = new ResultPoint[]{new ResultPoint((float)var3, (float)var5), new ResultPoint((float)var4, (float)var5)};
   }

   public ResultPoint[] getResultPoints() {
      return this.resultPoints;
   }

   public int[] getStartEnd() {
      return this.startEnd;
   }

   public int getValue() {
      return this.value;
   }
}
