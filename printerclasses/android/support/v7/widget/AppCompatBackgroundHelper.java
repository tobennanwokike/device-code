package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.TintInfo;
import android.util.AttributeSet;
import android.view.View;

class AppCompatBackgroundHelper {
   private int mBackgroundResId = -1;
   private AppCompatBackgroundHelper.BackgroundTintInfo mBackgroundTint;
   private final AppCompatDrawableManager mDrawableManager;
   private AppCompatBackgroundHelper.BackgroundTintInfo mInternalBackgroundTint;
   private AppCompatBackgroundHelper.BackgroundTintInfo mTmpInfo;
   private final View mView;

   AppCompatBackgroundHelper(View var1) {
      this.mView = var1;
      this.mDrawableManager = AppCompatDrawableManager.get();
   }

   private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable var1) {
      boolean var2 = true;
      if(this.mTmpInfo == null) {
         this.mTmpInfo = new AppCompatBackgroundHelper.BackgroundTintInfo();
      }

      AppCompatBackgroundHelper.BackgroundTintInfo var3 = this.mTmpInfo;
      var3.clear();
      ColorStateList var4 = ViewCompat.getBackgroundTintList(this.mView);
      if(var4 != null) {
         var3.mHasTintList = true;
         var3.mTintList = var4;
      }

      Mode var5 = ViewCompat.getBackgroundTintMode(this.mView);
      if(var5 != null) {
         var3.mHasTintMode = true;
         var3.mTintMode = var5;
      }

      if(!var3.mHasTintList && !var3.mHasTintMode) {
         var2 = false;
      } else {
         AppCompatDrawableManager.tintDrawable(var1, var3, this.mView.getDrawableState());
      }

      return var2;
   }

   private boolean shouldApplyFrameworkTintUsingColorFilter() {
      boolean var3 = true;
      int var1 = VERSION.SDK_INT;
      boolean var2;
      if(var1 < 21) {
         var2 = false;
      } else {
         var2 = var3;
         if(var1 != 21) {
            var2 = var3;
            if(this.mInternalBackgroundTint == null) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   private boolean updateBackgroundTint() {
      boolean var1 = true;
      if(this.mBackgroundTint != null && this.mBackgroundTint.mHasTintList) {
         if(this.mBackgroundResId >= 0) {
            ColorStateList var2 = this.mDrawableManager.getTintList(this.mView.getContext(), this.mBackgroundResId, this.mBackgroundTint.mOriginalTintList);
            if(var2 != null) {
               this.mBackgroundTint.mTintList = var2;
               return var1;
            }
         }

         if(this.mBackgroundTint.mTintList != this.mBackgroundTint.mOriginalTintList) {
            this.mBackgroundTint.mTintList = this.mBackgroundTint.mOriginalTintList;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   void applySupportBackgroundTint() {
      Drawable var1 = this.mView.getBackground();
      if(var1 != null && (!this.shouldApplyFrameworkTintUsingColorFilter() || !this.applyFrameworkTintUsingColorFilter(var1))) {
         if(this.mBackgroundTint != null) {
            AppCompatDrawableManager.tintDrawable(var1, this.mBackgroundTint, this.mView.getDrawableState());
         } else if(this.mInternalBackgroundTint != null) {
            AppCompatDrawableManager.tintDrawable(var1, this.mInternalBackgroundTint, this.mView.getDrawableState());
         }
      }

   }

   ColorStateList getSupportBackgroundTintList() {
      ColorStateList var1;
      if(this.mBackgroundTint != null) {
         var1 = this.mBackgroundTint.mTintList;
      } else {
         var1 = null;
      }

      return var1;
   }

   Mode getSupportBackgroundTintMode() {
      Mode var1;
      if(this.mBackgroundTint != null) {
         var1 = this.mBackgroundTint.mTintMode;
      } else {
         var1 = null;
      }

      return var1;
   }

   void loadFromAttributes(AttributeSet param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   void onSetBackgroundDrawable(Drawable var1) {
      this.mBackgroundResId = -1;
      this.setInternalBackgroundTint((ColorStateList)null);
      if(this.updateBackgroundTint()) {
         this.applySupportBackgroundTint();
      }

   }

   void onSetBackgroundResource(int var1) {
      this.mBackgroundResId = var1;
      ColorStateList var2;
      if(this.mDrawableManager != null) {
         var2 = this.mDrawableManager.getTintList(this.mView.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setInternalBackgroundTint(var2);
      if(this.updateBackgroundTint()) {
         this.applySupportBackgroundTint();
      }

   }

   void setInternalBackgroundTint(ColorStateList var1) {
      if(var1 != null) {
         if(this.mInternalBackgroundTint == null) {
            this.mInternalBackgroundTint = new AppCompatBackgroundHelper.BackgroundTintInfo();
         }

         this.mInternalBackgroundTint.mTintList = var1;
         this.mInternalBackgroundTint.mHasTintList = true;
      } else {
         this.mInternalBackgroundTint = null;
      }

      this.applySupportBackgroundTint();
   }

   void setSupportBackgroundTintList(ColorStateList var1) {
      if(this.mBackgroundTint == null) {
         this.mBackgroundTint = new AppCompatBackgroundHelper.BackgroundTintInfo();
      }

      this.mBackgroundTint.mOriginalTintList = var1;
      this.mBackgroundTint.mTintList = null;
      this.mBackgroundTint.mHasTintList = true;
      if(this.updateBackgroundTint()) {
         this.applySupportBackgroundTint();
      }

   }

   void setSupportBackgroundTintMode(Mode var1) {
      if(this.mBackgroundTint == null) {
         this.mBackgroundTint = new AppCompatBackgroundHelper.BackgroundTintInfo();
      }

      this.mBackgroundTint.mTintMode = var1;
      this.mBackgroundTint.mHasTintMode = true;
      this.applySupportBackgroundTint();
   }

   private static class BackgroundTintInfo extends TintInfo {
      public ColorStateList mOriginalTintList;

      void clear() {
         super.clear();
         this.mOriginalTintList = null;
      }
   }
}
