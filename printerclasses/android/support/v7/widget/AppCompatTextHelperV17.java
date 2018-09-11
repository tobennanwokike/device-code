package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextHelper;
import android.support.v7.widget.TintInfo;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelperV17 extends AppCompatTextHelper {
   private TintInfo mDrawableEndTint;
   private TintInfo mDrawableStartTint;

   AppCompatTextHelperV17(TextView var1) {
      super(var1);
   }

   void applyCompoundDrawablesTints() {
      super.applyCompoundDrawablesTints();
      if(this.mDrawableStartTint != null || this.mDrawableEndTint != null) {
         Drawable[] var1 = this.mView.getCompoundDrawablesRelative();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableStartTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableEndTint);
      }

   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      super.loadFromAttributes(var1, var2);
      Context var4 = this.mView.getContext();
      AppCompatDrawableManager var3 = AppCompatDrawableManager.get();
      TypedArray var5 = var4.obtainStyledAttributes(var1, R.styleable.AppCompatTextHelper, var2, 0);
      if(var5.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
         this.mDrawableStartTint = createTintInfo(var4, var3, var5.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
      }

      if(var5.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
         this.mDrawableEndTint = createTintInfo(var4, var3, var5.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
      }

      var5.recycle();
   }
}
