package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R;
import android.support.v7.text.AllCapsTransformationMethod;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextHelperV17;
import android.support.v7.widget.TintInfo;
import android.support.v7.widget.TintTypedArray;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelper {
   private TintInfo mDrawableBottomTint;
   private TintInfo mDrawableLeftTint;
   private TintInfo mDrawableRightTint;
   private TintInfo mDrawableTopTint;
   final TextView mView;

   AppCompatTextHelper(TextView var1) {
      this.mView = var1;
   }

   static AppCompatTextHelper create(TextView var0) {
      Object var1;
      if(VERSION.SDK_INT >= 17) {
         var1 = new AppCompatTextHelperV17(var0);
      } else {
         var1 = new AppCompatTextHelper(var0);
      }

      return (AppCompatTextHelper)var1;
   }

   protected static TintInfo createTintInfo(Context var0, AppCompatDrawableManager var1, int var2) {
      ColorStateList var4 = var1.getTintList(var0, var2);
      TintInfo var3;
      if(var4 != null) {
         var3 = new TintInfo();
         var3.mHasTintList = true;
         var3.mTintList = var4;
      } else {
         var3 = null;
      }

      return var3;
   }

   final void applyCompoundDrawableTint(Drawable var1, TintInfo var2) {
      if(var1 != null && var2 != null) {
         AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
      }

   }

   void applyCompoundDrawablesTints() {
      if(this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
         Drawable[] var1 = this.mView.getCompoundDrawables();
         this.applyCompoundDrawableTint(var1[0], this.mDrawableLeftTint);
         this.applyCompoundDrawableTint(var1[1], this.mDrawableTopTint);
         this.applyCompoundDrawableTint(var1[2], this.mDrawableRightTint);
         this.applyCompoundDrawableTint(var1[3], this.mDrawableBottomTint);
      }

   }

   void loadFromAttributes(AttributeSet var1, int var2) {
      Context var14 = this.mView.getContext();
      AppCompatDrawableManager var9 = AppCompatDrawableManager.get();
      TintTypedArray var10 = TintTypedArray.obtainStyledAttributes(var14, var1, R.styleable.AppCompatTextHelper, var2, 0);
      int var5 = var10.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
      if(var10.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
         this.mDrawableLeftTint = createTintInfo(var14, var9, var10.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
      }

      if(var10.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
         this.mDrawableTopTint = createTintInfo(var14, var9, var10.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
      }

      if(var10.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
         this.mDrawableRightTint = createTintInfo(var14, var9, var10.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
      }

      if(var10.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
         this.mDrawableBottomTint = createTintInfo(var14, var9, var10.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
      }

      var10.recycle();
      boolean var8 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
      boolean var6 = false;
      boolean var7 = false;
      boolean var3 = false;
      boolean var4 = false;
      ColorStateList var11 = null;
      ColorStateList var18 = null;
      Object var13 = null;
      ColorStateList var19 = null;
      TintTypedArray var12 = null;
      if(var5 != -1) {
         TintTypedArray var15 = TintTypedArray.obtainStyledAttributes(var14, var5, R.styleable.TextAppearance);
         var6 = var7;
         var3 = var4;
         if(!var8) {
            var6 = var7;
            var3 = var4;
            if(var15.hasValue(R.styleable.TextAppearance_textAllCaps)) {
               var3 = true;
               var6 = var15.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            }
         }

         var18 = var11;
         var11 = var12;
         if(VERSION.SDK_INT < 23) {
            var19 = (ColorStateList)var13;
            if(var15.hasValue(R.styleable.TextAppearance_android_textColor)) {
               var19 = var15.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }

            var18 = var19;
            var11 = var12;
            if(var15.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
               var11 = var15.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
               var18 = var19;
            }
         }

         var15.recycle();
         var19 = var11;
      }

      var12 = TintTypedArray.obtainStyledAttributes(var14, var1, R.styleable.TextAppearance, var2, 0);
      var7 = var6;
      boolean var17 = var3;
      if(!var8) {
         var7 = var6;
         var17 = var3;
         if(var12.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            var17 = true;
            var7 = var12.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
         }
      }

      var11 = var18;
      ColorStateList var16 = var19;
      if(VERSION.SDK_INT < 23) {
         if(var12.hasValue(R.styleable.TextAppearance_android_textColor)) {
            var18 = var12.getColorStateList(R.styleable.TextAppearance_android_textColor);
         }

         var11 = var18;
         var16 = var19;
         if(var12.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
            var16 = var12.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            var11 = var18;
         }
      }

      var12.recycle();
      if(var11 != null) {
         this.mView.setTextColor(var11);
      }

      if(var16 != null) {
         this.mView.setHintTextColor(var16);
      }

      if(!var8 && var17) {
         this.setAllCaps(var7);
      }

   }

   void onSetTextAppearance(Context var1, int var2) {
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, var2, R.styleable.TextAppearance);
      if(var4.hasValue(R.styleable.TextAppearance_textAllCaps)) {
         this.setAllCaps(var4.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
      }

      if(VERSION.SDK_INT < 23 && var4.hasValue(R.styleable.TextAppearance_android_textColor)) {
         ColorStateList var3 = var4.getColorStateList(R.styleable.TextAppearance_android_textColor);
         if(var3 != null) {
            this.mView.setTextColor(var3);
         }
      }

      var4.recycle();
   }

   void setAllCaps(boolean var1) {
      TextView var3 = this.mView;
      AllCapsTransformationMethod var2;
      if(var1) {
         var2 = new AllCapsTransformationMethod(this.mView.getContext());
      } else {
         var2 = null;
      }

      var3.setTransformationMethod(var2);
   }
}
