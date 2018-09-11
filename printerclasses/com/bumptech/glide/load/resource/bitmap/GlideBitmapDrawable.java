package com.bumptech.glide.load.resource.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.view.Gravity;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;

public class GlideBitmapDrawable extends GlideDrawable {
   private boolean applyGravity;
   private final Rect destRect;
   private int height;
   private boolean mutated;
   private GlideBitmapDrawable.BitmapState state;
   private int width;

   public GlideBitmapDrawable(Resources var1, Bitmap var2) {
      this(var1, new GlideBitmapDrawable.BitmapState(var2));
   }

   GlideBitmapDrawable(Resources var1, GlideBitmapDrawable.BitmapState var2) {
      this.destRect = new Rect();
      if(var2 == null) {
         throw new NullPointerException("BitmapState must not be null");
      } else {
         this.state = var2;
         int var3;
         if(var1 != null) {
            var3 = var1.getDisplayMetrics().densityDpi;
            if(var3 == 0) {
               var3 = 160;
            }

            var2.targetDensity = var3;
         } else {
            var3 = var2.targetDensity;
         }

         this.width = var2.bitmap.getScaledWidth(var3);
         this.height = var2.bitmap.getScaledHeight(var3);
      }
   }

   public void draw(Canvas var1) {
      if(this.applyGravity) {
         Gravity.apply(119, this.width, this.height, this.getBounds(), this.destRect);
         this.applyGravity = false;
      }

      var1.drawBitmap(this.state.bitmap, (Rect)null, this.destRect, this.state.paint);
   }

   public Bitmap getBitmap() {
      return this.state.bitmap;
   }

   public ConstantState getConstantState() {
      return this.state;
   }

   public int getIntrinsicHeight() {
      return this.height;
   }

   public int getIntrinsicWidth() {
      return this.width;
   }

   public int getOpacity() {
      Bitmap var2 = this.state.bitmap;
      byte var1;
      if(var2 != null && !var2.hasAlpha() && this.state.paint.getAlpha() >= 255) {
         var1 = -1;
      } else {
         var1 = -3;
      }

      return var1;
   }

   public boolean isAnimated() {
      return false;
   }

   public boolean isRunning() {
      return false;
   }

   public Drawable mutate() {
      if(!this.mutated && super.mutate() == this) {
         this.state = new GlideBitmapDrawable.BitmapState(this.state);
         this.mutated = true;
      }

      return this;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.applyGravity = true;
   }

   public void setAlpha(int var1) {
      if(this.state.paint.getAlpha() != var1) {
         this.state.setAlpha(var1);
         this.invalidateSelf();
      }

   }

   public void setColorFilter(ColorFilter var1) {
      this.state.setColorFilter(var1);
      this.invalidateSelf();
   }

   public void setLoopCount(int var1) {
   }

   public void start() {
   }

   public void stop() {
   }

   static class BitmapState extends ConstantState {
      private static final Paint DEFAULT_PAINT = new Paint(6);
      private static final int DEFAULT_PAINT_FLAGS = 6;
      private static final int GRAVITY = 119;
      final Bitmap bitmap;
      Paint paint;
      int targetDensity;

      public BitmapState(Bitmap var1) {
         this.paint = DEFAULT_PAINT;
         this.bitmap = var1;
      }

      BitmapState(GlideBitmapDrawable.BitmapState var1) {
         this(var1.bitmap);
         this.targetDensity = var1.targetDensity;
      }

      public int getChangingConfigurations() {
         return 0;
      }

      void mutatePaint() {
         if(DEFAULT_PAINT == this.paint) {
            this.paint = new Paint(6);
         }

      }

      public Drawable newDrawable() {
         return new GlideBitmapDrawable((Resources)null, this);
      }

      public Drawable newDrawable(Resources var1) {
         return new GlideBitmapDrawable(var1, this);
      }

      void setAlpha(int var1) {
         this.mutatePaint();
         this.paint.setAlpha(var1);
      }

      void setColorFilter(ColorFilter var1) {
         this.mutatePaint();
         this.paint.setColorFilter(var1);
      }
   }
}
