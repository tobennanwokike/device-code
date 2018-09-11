package com.google.zxing;

public class ResultPoint {
   private final float x;
   private final float y;

   public ResultPoint(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   private static float crossProductZ(ResultPoint var0, ResultPoint var1, ResultPoint var2) {
      float var4 = var1.x;
      float var6 = var1.y;
      float var7 = var2.x;
      float var3 = var0.y;
      float var5 = var2.y;
      return (var7 - var4) * (var3 - var6) - (var0.x - var4) * (var5 - var6);
   }

   public static float distance(ResultPoint var0, ResultPoint var1) {
      float var3 = var0.getX() - var1.getX();
      float var2 = var0.getY() - var1.getY();
      return (float)Math.sqrt((double)(var3 * var3 + var2 * var2));
   }

   public static void orderBestPatterns(ResultPoint[] var0) {
      float var3 = distance(var0[0], var0[1]);
      float var2 = distance(var0[1], var0[2]);
      float var1 = distance(var0[0], var0[2]);
      ResultPoint var4;
      ResultPoint var5;
      ResultPoint var6;
      if(var2 >= var3 && var2 >= var1) {
         var6 = var0[0];
         var5 = var0[1];
         var4 = var0[2];
      } else if(var1 >= var2 && var1 >= var3) {
         var6 = var0[1];
         var5 = var0[0];
         var4 = var0[2];
      } else {
         var6 = var0[2];
         var5 = var0[0];
         var4 = var0[1];
      }

      if(crossProductZ(var5, var6, var4) >= 0.0F) {
         ResultPoint var7 = var4;
         var4 = var5;
         var5 = var7;
      }

      var0[0] = var4;
      var0[1] = var6;
      var0[2] = var5;
   }

   public boolean equals(Object var1) {
      boolean var3 = false;
      boolean var2 = var3;
      if(var1 instanceof ResultPoint) {
         ResultPoint var4 = (ResultPoint)var1;
         var2 = var3;
         if(this.x == var4.x) {
            var2 = var3;
            if(this.y == var4.y) {
               var2 = true;
            }
         }
      }

      return var2;
   }

   public final float getX() {
      return this.x;
   }

   public final float getY() {
      return this.y;
   }

   public int hashCode() {
      return Float.floatToIntBits(this.x) * 31 + Float.floatToIntBits(this.y);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(25);
      var1.append('(');
      var1.append(this.x);
      var1.append(',');
      var1.append(this.y);
      var1.append(')');
      return var1.toString();
   }
}
