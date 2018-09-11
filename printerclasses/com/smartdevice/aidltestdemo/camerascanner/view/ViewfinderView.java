package com.smartdevice.aidltestdemo.camerascanner.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.smartdevice.aidltestdemo.camerascanner.camera.CameraManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public final class ViewfinderView extends View {
   private static final long ANIMATION_DELAY = 100L;
   private static final int OPAQUE = 255;
   private static final int[] SCANNER_ALPHA = new int[]{0, 64, 128, 192, 255, 192, 128, 64};
   private final int frameColor;
   private final int laserColor;
   private Collection lastPossibleResultPoints;
   private final int maskColor;
   private final Paint paint = new Paint();
   private Collection possibleResultPoints;
   private Bitmap resultBitmap;
   private final int resultColor;
   private final int resultPointColor;
   private int scannerAlpha;

   public ViewfinderView(Context var1, AttributeSet var2) {
      super(var1, var2);
      Resources var3 = this.getResources();
      this.maskColor = var3.getColor(2131034221);
      this.resultColor = var3.getColor(2131034195);
      this.frameColor = var3.getColor(2131034219);
      this.laserColor = var3.getColor(2131034220);
      this.resultPointColor = var3.getColor(2131034181);
      this.scannerAlpha = 0;
      this.possibleResultPoints = new HashSet(5);
   }

   public void addPossibleResultPoint(ResultPoint var1) {
      this.possibleResultPoints.add(var1);
   }

   public void drawResultBitmap(Bitmap var1) {
      this.resultBitmap = var1;
      this.invalidate();
   }

   public void drawViewfinder() {
      this.resultBitmap = null;
      this.invalidate();
   }

   public void onDraw(Canvas var1) {
      Rect var5 = CameraManager.get().getFramingRect();
      if(var5 != null) {
         int var3 = var1.getWidth();
         int var4 = var1.getHeight();
         Paint var6 = this.paint;
         int var2;
         if(this.resultBitmap != null) {
            var2 = this.resultColor;
         } else {
            var2 = this.maskColor;
         }

         var6.setColor(var2);
         var1.drawRect(0.0F, 0.0F, (float)var3, (float)var5.top, this.paint);
         var1.drawRect(0.0F, (float)var5.top, (float)var5.left, (float)(var5.bottom + 1), this.paint);
         var1.drawRect((float)(var5.right + 1), (float)var5.top, (float)var3, (float)(var5.bottom + 1), this.paint);
         var1.drawRect(0.0F, (float)(var5.bottom + 1), (float)var3, (float)var4, this.paint);
         if(this.resultBitmap != null) {
            this.paint.setAlpha(255);
            var1.drawBitmap(this.resultBitmap, (float)var5.left, (float)var5.top, this.paint);
         } else {
            this.paint.setColor(this.frameColor);
            var1.drawRect((float)var5.left, (float)var5.top, (float)(var5.right + 1), (float)(var5.top + 2), this.paint);
            var1.drawRect((float)var5.left, (float)(var5.top + 2), (float)(var5.left + 2), (float)(var5.bottom - 1), this.paint);
            var1.drawRect((float)(var5.right - 1), (float)var5.top, (float)(var5.right + 1), (float)(var5.bottom - 1), this.paint);
            var1.drawRect((float)var5.left, (float)(var5.bottom - 1), (float)(var5.right + 1), (float)(var5.bottom + 1), this.paint);
            this.paint.setColor(this.laserColor);
            this.paint.setAlpha(SCANNER_ALPHA[this.scannerAlpha]);
            this.scannerAlpha = (this.scannerAlpha + 1) % SCANNER_ALPHA.length;
            var2 = var5.height() / 2 + var5.top;
            var1.drawRect((float)(var5.left + 2), (float)(var2 - 1), (float)(var5.right - 1), (float)(var2 + 2), this.paint);
            Collection var7 = this.possibleResultPoints;
            Collection var9 = this.lastPossibleResultPoints;
            ResultPoint var11;
            if(var7.isEmpty()) {
               this.lastPossibleResultPoints = null;
            } else {
               this.possibleResultPoints = new HashSet(5);
               this.lastPossibleResultPoints = var7;
               this.paint.setAlpha(255);
               this.paint.setColor(this.resultPointColor);
               Iterator var8 = var7.iterator();

               while(var8.hasNext()) {
                  var11 = (ResultPoint)var8.next();
                  var1.drawCircle((float)var5.left + var11.getX(), (float)var5.top + var11.getY(), 6.0F, this.paint);
               }
            }

            if(var9 != null) {
               this.paint.setAlpha(127);
               this.paint.setColor(this.resultPointColor);
               Iterator var10 = var9.iterator();

               while(var10.hasNext()) {
                  var11 = (ResultPoint)var10.next();
                  var1.drawCircle((float)var5.left + var11.getX(), (float)var5.top + var11.getY(), 3.0F, this.paint);
               }
            }

            this.postInvalidateDelayed(100L, var5.left, var5.top, var5.right, var5.bottom);
         }
      }

   }
}
