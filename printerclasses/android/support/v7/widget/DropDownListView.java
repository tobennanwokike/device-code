package android.support.v7.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.appcompat.R;
import android.support.v7.widget.ListViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

class DropDownListView extends ListViewCompat {
   private ViewPropertyAnimatorCompat mClickAnimation;
   private boolean mDrawsInPressedState;
   private boolean mHijackFocus;
   private boolean mListSelectionHidden;
   private ListViewAutoScrollHelper mScrollHelper;

   public DropDownListView(Context var1, boolean var2) {
      super(var1, (AttributeSet)null, R.attr.dropDownListViewStyle);
      this.mHijackFocus = var2;
      this.setCacheColorHint(0);
   }

   private void clearPressedItem() {
      this.mDrawsInPressedState = false;
      this.setPressed(false);
      this.drawableStateChanged();
      View var1 = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
      if(var1 != null) {
         var1.setPressed(false);
      }

      if(this.mClickAnimation != null) {
         this.mClickAnimation.cancel();
         this.mClickAnimation = null;
      }

   }

   private void clickPressedItem(View var1, int var2) {
      this.performItemClick(var1, var2, this.getItemIdAtPosition(var2));
   }

   private void setPressedItem(View var1, int var2, float var3, float var4) {
      this.mDrawsInPressedState = true;
      if(VERSION.SDK_INT >= 21) {
         this.drawableHotspotChanged(var3, var4);
      }

      if(!this.isPressed()) {
         this.setPressed(true);
      }

      this.layoutChildren();
      if(this.mMotionPosition != -1) {
         View var7 = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
         if(var7 != null && var7 != var1 && var7.isPressed()) {
            var7.setPressed(false);
         }
      }

      this.mMotionPosition = var2;
      float var6 = (float)var1.getLeft();
      float var5 = (float)var1.getTop();
      if(VERSION.SDK_INT >= 21) {
         var1.drawableHotspotChanged(var3 - var6, var4 - var5);
      }

      if(!var1.isPressed()) {
         var1.setPressed(true);
      }

      this.positionSelectorLikeTouchCompat(var2, var1, var3, var4);
      this.setSelectorEnabled(false);
      this.refreshDrawableState();
   }

   public boolean hasFocus() {
      boolean var1;
      if(!this.mHijackFocus && !super.hasFocus()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean hasWindowFocus() {
      boolean var1;
      if(!this.mHijackFocus && !super.hasWindowFocus()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isFocused() {
      boolean var1;
      if(!this.mHijackFocus && !super.isFocused()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isInTouchMode() {
      boolean var1;
      if((!this.mHijackFocus || !this.mListSelectionHidden) && !super.isInTouchMode()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean onForwardedEvent(MotionEvent var1, int var2) {
      boolean var7 = true;
      boolean var8 = true;
      boolean var3 = false;
      int var4 = MotionEventCompat.getActionMasked(var1);
      boolean var10;
      switch(var4) {
      case 1:
         var7 = false;
      case 2:
         int var5 = var1.findPointerIndex(var2);
         if(var5 < 0) {
            var7 = false;
            var10 = var3;
         } else {
            var2 = (int)var1.getX(var5);
            int var6 = (int)var1.getY(var5);
            var5 = this.pointToPosition(var2, var6);
            if(var5 == -1) {
               var10 = true;
            } else {
               View var9 = this.getChildAt(var5 - this.getFirstVisiblePosition());
               this.setPressedItem(var9, var5, (float)var2, (float)var6);
               var8 = true;
               var10 = var3;
               var7 = var8;
               if(var4 == 1) {
                  this.clickPressedItem(var9, var5);
                  var10 = var3;
                  var7 = var8;
               }
            }
         }
         break;
      case 3:
         var7 = false;
         var10 = var3;
         break;
      default:
         var7 = var8;
         var10 = var3;
      }

      if(!var7 || var10) {
         this.clearPressedItem();
      }

      if(var7) {
         if(this.mScrollHelper == null) {
            this.mScrollHelper = new ListViewAutoScrollHelper(this);
         }

         this.mScrollHelper.setEnabled(true);
         this.mScrollHelper.onTouch(this, var1);
      } else if(this.mScrollHelper != null) {
         this.mScrollHelper.setEnabled(false);
      }

      return var7;
   }

   void setListSelectionHidden(boolean var1) {
      this.mListSelectionHidden = var1;
   }

   protected boolean touchModeDrawsInPressedStateCompat() {
      boolean var1;
      if(!this.mDrawsInPressedState && !super.touchModeDrawsInPressedStateCompat()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }
}
