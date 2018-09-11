package com.smartdevice.aidltestdemo.camerascanner.camera;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.google.zxing.LuminanceSource;

public final class PlanarYUVLuminanceSource extends LuminanceSource {
   private final int dataHeight;
   private final int dataWidth;
   private final int left;
   private final int top;
   private final byte[] yuvData;

   public PlanarYUVLuminanceSource(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      super(var6, var7);
      if(var4 + var6 <= var2 && var5 + var7 <= var3) {
         this.yuvData = var1;
         this.dataWidth = var2;
         this.dataHeight = var3;
         this.left = var4;
         this.top = var5;
      } else {
         throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
      }
   }

   public int getDataHeight() {
      return this.dataHeight;
   }

   public int getDataWidth() {
      return this.dataWidth;
   }

   public byte[] getMatrix() {
      int var4 = this.getWidth();
      int var3 = this.getHeight();
      byte[] var5;
      if(var4 == this.dataWidth && var3 == this.dataHeight) {
         var5 = this.yuvData;
      } else {
         int var1 = var4 * var3;
         byte[] var6 = new byte[var1];
         int var2 = this.top * this.dataWidth + this.left;
         if(var4 == this.dataWidth) {
            System.arraycopy(this.yuvData, var2, var6, 0, var1);
            var5 = var6;
         } else {
            byte[] var7 = this.yuvData;
            var1 = 0;

            while(true) {
               var5 = var6;
               if(var1 >= var3) {
                  break;
               }

               System.arraycopy(var7, var2, var6, var1 * var4, var4);
               var2 += this.dataWidth;
               ++var1;
            }
         }
      }

      return var5;
   }

   public byte[] getRow(int var1, byte[] var2) {
      if(var1 >= 0 && var1 < this.getHeight()) {
         int var5;
         byte[] var7;
         label14: {
            var5 = this.getWidth();
            if(var2 != null) {
               var7 = var2;
               if(var2.length >= var5) {
                  break label14;
               }
            }

            var7 = new byte[var5];
         }

         int var3 = this.top;
         int var4 = this.dataWidth;
         int var6 = this.left;
         System.arraycopy(this.yuvData, (var3 + var1) * var4 + var6, var7, 0, var5);
         return var7;
      } else {
         throw new IllegalArgumentException("Requested row is outside the image: " + var1);
      }
   }

   public boolean isCropSupported() {
      return true;
   }

   public Bitmap renderCroppedGreyscaleBitmap() {
      int var4 = this.getWidth();
      int var5 = this.getHeight();
      int[] var6 = new int[var4 * var5];
      byte[] var7 = this.yuvData;
      int var2 = this.top * this.dataWidth + this.left;

      for(int var1 = 0; var1 < var5; ++var1) {
         for(int var3 = 0; var3 < var4; ++var3) {
            var6[var1 * var4 + var3] = -16777216 | 65793 * (var7[var2 + var3] & 255);
         }

         var2 += this.dataWidth;
      }

      Bitmap var8 = Bitmap.createBitmap(var4, var5, Config.ARGB_8888);
      var8.setPixels(var6, 0, var4, 0, 0, var4, var5);
      return var8;
   }
}
