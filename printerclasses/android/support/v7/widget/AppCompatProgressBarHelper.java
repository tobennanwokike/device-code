package android.support.v7.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.ProgressBar;

class AppCompatProgressBarHelper {
   private static final int[] TINT_ATTRS = new int[]{16843067, 16843068};
   private Bitmap mSampleTile;
   private final ProgressBar mView;

   AppCompatProgressBarHelper(ProgressBar var1) {
      this.mView = var1;
   }

   private Shape getDrawableShape() {
      return new RoundRectShape(new float[]{5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F, 5.0F}, (RectF)null, (float[])null);
   }

   private Drawable tileify(Drawable var1, boolean var2) {
      Drawable var6;
      if(var1 instanceof DrawableWrapper) {
         var6 = ((DrawableWrapper)var1).getWrappedDrawable();
         if(var6 != null) {
            var6 = this.tileify(var6, var2);
            ((DrawableWrapper)var1).setWrappedDrawable(var6);
         }
      } else if(var1 instanceof LayerDrawable) {
         LayerDrawable var7 = (LayerDrawable)var1;
         int var4 = var7.getNumberOfLayers();
         Drawable[] var8 = new Drawable[var4];

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            int var5 = var7.getId(var3);
            var6 = var7.getDrawable(var3);
            if(var5 != 16908301 && var5 != 16908303) {
               var2 = false;
            } else {
               var2 = true;
            }

            var8[var3] = this.tileify(var6, var2);
         }

         LayerDrawable var10 = new LayerDrawable(var8);
         var3 = 0;

         while(true) {
            var1 = var10;
            if(var3 >= var4) {
               break;
            }

            var10.setId(var3, var7.getId(var3));
            ++var3;
         }
      } else if(var1 instanceof BitmapDrawable) {
         BitmapDrawable var9 = (BitmapDrawable)var1;
         Bitmap var11 = var9.getBitmap();
         if(this.mSampleTile == null) {
            this.mSampleTile = var11;
         }

         ShapeDrawable var12 = new ShapeDrawable(this.getDrawableShape());
         BitmapShader var13 = new BitmapShader(var11, TileMode.REPEAT, TileMode.CLAMP);
         var12.getPaint().setShader(var13);
         var12.getPaint().setColorFilter(var9.getPaint().getColorFilter());
         var1 = var12;
         if(var2) {
            var1 = new ClipDrawable(var12, 3, 1);
         }
      }

      return (Drawable)var1;
   }

   private Drawable tileifyIndeterminate(Drawable var1) {
      Object var4 = var1;
      if(var1 instanceof AnimationDrawable) {
         AnimationDrawable var5 = (AnimationDrawable)var1;
         int var3 = var5.getNumberOfFrames();
         var4 = new AnimationDrawable();
         ((AnimationDrawable)var4).setOneShot(var5.isOneShot());

         for(int var2 = 0; var2 < var3; ++var2) {
            var1 = this.tileify(var5.getFrame(var2), true);
            var1.setLevel(10000);
            ((AnimationDrawable)var4).addFrame(var1, var5.getDuration(var2));
         }

         ((AnimationDrawable)var4).setLevel(10000);
      }

      return (Drawable)var4;
   }

   Bitmap getSampleTime() {
      return this.mSampleTile;
   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, TINT_ATTRS, var2, 0);
      Drawable var3 = var4.getDrawableIfKnown(0);
      if(var3 != null) {
         this.mView.setIndeterminateDrawable(this.tileifyIndeterminate(var3));
      }

      var3 = var4.getDrawableIfKnown(1);
      if(var3 != null) {
         this.mView.setProgressDrawable(this.tileify(var3, false));
      }

      var4.recycle();
   }
}
