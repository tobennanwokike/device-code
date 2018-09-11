package android.support.v7.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
   public static final int ARROW_DIRECTION_END = 3;
   public static final int ARROW_DIRECTION_LEFT = 0;
   public static final int ARROW_DIRECTION_RIGHT = 1;
   public static final int ARROW_DIRECTION_START = 2;
   private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0D);
   private float mArrowHeadLength;
   private float mArrowShaftLength;
   private float mBarGap;
   private float mBarLength;
   private int mDirection = 2;
   private float mMaxCutForBarSize;
   private final Paint mPaint = new Paint();
   private final Path mPath = new Path();
   private float mProgress;
   private final int mSize;
   private boolean mSpin;
   private boolean mVerticalMirror = false;

   public DrawerArrowDrawable(Context var1) {
      this.mPaint.setStyle(Style.STROKE);
      this.mPaint.setStrokeJoin(Join.MITER);
      this.mPaint.setStrokeCap(Cap.BUTT);
      this.mPaint.setAntiAlias(true);
      TypedArray var2 = var1.getTheme().obtainStyledAttributes((AttributeSet)null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
      this.setColor(var2.getColor(R.styleable.DrawerArrowToggle_color, 0));
      this.setBarThickness(var2.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0F));
      this.setSpinEnabled(var2.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
      this.setGapSize((float)Math.round(var2.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0F)));
      this.mSize = var2.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
      this.mBarLength = (float)Math.round(var2.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0F));
      this.mArrowHeadLength = (float)Math.round(var2.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0F));
      this.mArrowShaftLength = var2.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0F);
      var2.recycle();
   }

   private static float lerp(float var0, float var1, float var2) {
      return (var1 - var0) * var2 + var0;
   }

   public void draw(Canvas var1) {
      Rect var10 = this.getBounds();
      boolean var9;
      switch(this.mDirection) {
      case 0:
         var9 = false;
         break;
      case 1:
         var9 = true;
         break;
      case 2:
      default:
         if(DrawableCompat.getLayoutDirection(this) == 1) {
            var9 = true;
         } else {
            var9 = false;
         }
         break;
      case 3:
         if(DrawableCompat.getLayoutDirection(this) == 0) {
            var9 = true;
         } else {
            var9 = false;
         }
      }

      float var2 = (float)Math.sqrt((double)(this.mArrowHeadLength * this.mArrowHeadLength * 2.0F));
      float var6 = lerp(this.mBarLength, var2, this.mProgress);
      float var4 = lerp(this.mBarLength, this.mArrowShaftLength, this.mProgress);
      float var5 = (float)Math.round(lerp(0.0F, this.mMaxCutForBarSize, this.mProgress));
      float var7 = lerp(0.0F, ARROW_HEAD_ANGLE, this.mProgress);
      if(var9) {
         var2 = 0.0F;
      } else {
         var2 = -180.0F;
      }

      float var3;
      if(var9) {
         var3 = 180.0F;
      } else {
         var3 = 0.0F;
      }

      var2 = lerp(var2, var3, this.mProgress);
      var3 = (float)Math.round((double)var6 * Math.cos((double)var7));
      var7 = (float)Math.round((double)var6 * Math.sin((double)var7));
      this.mPath.rewind();
      var6 = lerp(this.mBarGap + this.mPaint.getStrokeWidth(), -this.mMaxCutForBarSize, this.mProgress);
      float var8 = -var4 / 2.0F;
      this.mPath.moveTo(var8 + var5, 0.0F);
      this.mPath.rLineTo(var4 - 2.0F * var5, 0.0F);
      this.mPath.moveTo(var8, var6);
      this.mPath.rLineTo(var3, var7);
      this.mPath.moveTo(var8, -var6);
      this.mPath.rLineTo(var3, -var7);
      this.mPath.close();
      var1.save();
      var3 = this.mPaint.getStrokeWidth();
      var3 = (float)((double)((float)((int)((float)var10.height() - 3.0F * var3 - this.mBarGap * 2.0F) / 4 * 2)) + (double)var3 * 1.5D + (double)this.mBarGap);
      var1.translate((float)var10.centerX(), var3);
      if(this.mSpin) {
         byte var11;
         if(this.mVerticalMirror ^ var9) {
            var11 = -1;
         } else {
            var11 = 1;
         }

         var1.rotate((float)var11 * var2);
      } else if(var9) {
         var1.rotate(180.0F);
      }

      var1.drawPath(this.mPath, this.mPaint);
      var1.restore();
   }

   public float getArrowHeadLength() {
      return this.mArrowHeadLength;
   }

   public float getArrowShaftLength() {
      return this.mArrowShaftLength;
   }

   public float getBarLength() {
      return this.mBarLength;
   }

   public float getBarThickness() {
      return this.mPaint.getStrokeWidth();
   }

   @ColorInt
   public int getColor() {
      return this.mPaint.getColor();
   }

   public int getDirection() {
      return this.mDirection;
   }

   public float getGapSize() {
      return this.mBarGap;
   }

   public int getIntrinsicHeight() {
      return this.mSize;
   }

   public int getIntrinsicWidth() {
      return this.mSize;
   }

   public int getOpacity() {
      return -3;
   }

   public final Paint getPaint() {
      return this.mPaint;
   }

   @FloatRange(
      from = 0.0D,
      to = 1.0D
   )
   public float getProgress() {
      return this.mProgress;
   }

   public boolean isSpinEnabled() {
      return this.mSpin;
   }

   public void setAlpha(int var1) {
      if(var1 != this.mPaint.getAlpha()) {
         this.mPaint.setAlpha(var1);
         this.invalidateSelf();
      }

   }

   public void setArrowHeadLength(float var1) {
      if(this.mArrowHeadLength != var1) {
         this.mArrowHeadLength = var1;
         this.invalidateSelf();
      }

   }

   public void setArrowShaftLength(float var1) {
      if(this.mArrowShaftLength != var1) {
         this.mArrowShaftLength = var1;
         this.invalidateSelf();
      }

   }

   public void setBarLength(float var1) {
      if(this.mBarLength != var1) {
         this.mBarLength = var1;
         this.invalidateSelf();
      }

   }

   public void setBarThickness(float var1) {
      if(this.mPaint.getStrokeWidth() != var1) {
         this.mPaint.setStrokeWidth(var1);
         this.mMaxCutForBarSize = (float)((double)(var1 / 2.0F) * Math.cos((double)ARROW_HEAD_ANGLE));
         this.invalidateSelf();
      }

   }

   public void setColor(@ColorInt int var1) {
      if(var1 != this.mPaint.getColor()) {
         this.mPaint.setColor(var1);
         this.invalidateSelf();
      }

   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
      this.invalidateSelf();
   }

   public void setDirection(int var1) {
      if(var1 != this.mDirection) {
         this.mDirection = var1;
         this.invalidateSelf();
      }

   }

   public void setGapSize(float var1) {
      if(var1 != this.mBarGap) {
         this.mBarGap = var1;
         this.invalidateSelf();
      }

   }

   public void setProgress(@FloatRange(
   from = 0.0D,
   to = 1.0D
) float var1) {
      if(this.mProgress != var1) {
         this.mProgress = var1;
         this.invalidateSelf();
      }

   }

   public void setSpinEnabled(boolean var1) {
      if(this.mSpin != var1) {
         this.mSpin = var1;
         this.invalidateSelf();
      }

   }

   public void setVerticalMirror(boolean var1) {
      if(this.mVerticalMirror != var1) {
         this.mVerticalMirror = var1;
         this.invalidateSelf();
      }

   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public @interface ArrowDirection {
   }
}
