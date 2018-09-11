package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.widget.TintableCompoundButton;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatCompoundButtonHelper;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class AppCompatRadioButton extends RadioButton implements TintableCompoundButton {
   private AppCompatCompoundButtonHelper mCompoundButtonHelper;

   public AppCompatRadioButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatRadioButton(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.radioButtonStyle);
   }

   public AppCompatRadioButton(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper(this);
      this.mCompoundButtonHelper.loadFromAttributes(var2, var3);
   }

   public int getCompoundPaddingLeft() {
      int var2 = super.getCompoundPaddingLeft();
      int var1 = var2;
      if(this.mCompoundButtonHelper != null) {
         var1 = this.mCompoundButtonHelper.getCompoundPaddingLeft(var2);
      }

      return var1;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public ColorStateList getSupportButtonTintList() {
      ColorStateList var1;
      if(this.mCompoundButtonHelper != null) {
         var1 = this.mCompoundButtonHelper.getSupportButtonTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public Mode getSupportButtonTintMode() {
      Mode var1;
      if(this.mCompoundButtonHelper != null) {
         var1 = this.mCompoundButtonHelper.getSupportButtonTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   public void setButtonDrawable(@DrawableRes int var1) {
      this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setButtonDrawable(Drawable var1) {
      super.setButtonDrawable(var1);
      if(this.mCompoundButtonHelper != null) {
         this.mCompoundButtonHelper.onSetButtonDrawable();
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setSupportButtonTintList(@Nullable ColorStateList var1) {
      if(this.mCompoundButtonHelper != null) {
         this.mCompoundButtonHelper.setSupportButtonTintList(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void setSupportButtonTintMode(@Nullable Mode var1) {
      if(this.mCompoundButtonHelper != null) {
         this.mCompoundButtonHelper.setSupportButtonTintMode(var1);
      }

   }
}
