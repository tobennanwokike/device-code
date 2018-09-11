package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ForwardingListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class ActionMenuItemView extends AppCompatTextView implements MenuView.ItemView, OnClickListener, OnLongClickListener, ActionMenuView.ActionMenuChildView {
   private static final int MAX_ICON_SIZE = 32;
   private static final String TAG = "ActionMenuItemView";
   private boolean mAllowTextWithIcon;
   private boolean mExpandedFormat;
   private ForwardingListener mForwardingListener;
   private Drawable mIcon;
   MenuItemImpl mItemData;
   MenuBuilder.ItemInvoker mItemInvoker;
   private int mMaxIconSize;
   private int mMinWidth;
   ActionMenuItemView.PopupCallback mPopupCallback;
   private int mSavedPaddingLeft;
   private CharSequence mTitle;

   public ActionMenuItemView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionMenuItemView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ActionMenuItemView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      Resources var4 = var1.getResources();
      this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
      TypedArray var5 = var1.obtainStyledAttributes(var2, R.styleable.ActionMenuItemView, var3, 0);
      this.mMinWidth = var5.getDimensionPixelSize(R.styleable.ActionMenuItemView_android_minWidth, 0);
      var5.recycle();
      this.mMaxIconSize = (int)(32.0F * var4.getDisplayMetrics().density + 0.5F);
      this.setOnClickListener(this);
      this.setOnLongClickListener(this);
      this.mSavedPaddingLeft = -1;
      this.setSaveEnabled(false);
   }

   private boolean shouldAllowTextWithIcon() {
      Configuration var4 = this.getContext().getResources().getConfiguration();
      int var2 = ConfigurationHelper.getScreenWidthDp(this.getResources());
      int var1 = ConfigurationHelper.getScreenHeightDp(this.getResources());
      boolean var3;
      if(var2 < 480 && (var2 < 640 || var1 < 480) && var4.orientation != 2) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   private void updateTextButtonVisibility() {
      boolean var3 = false;
      boolean var1;
      if(!TextUtils.isEmpty(this.mTitle)) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var2;
      label25: {
         if(this.mIcon != null) {
            var2 = var3;
            if(!this.mItemData.showsTextAsAction()) {
               break label25;
            }

            if(!this.mAllowTextWithIcon) {
               var2 = var3;
               if(!this.mExpandedFormat) {
                  break label25;
               }
            }
         }

         var2 = true;
      }

      CharSequence var4;
      if(var1 & var2) {
         var4 = this.mTitle;
      } else {
         var4 = null;
      }

      this.setText(var4);
   }

   public MenuItemImpl getItemData() {
      return this.mItemData;
   }

   public boolean hasText() {
      boolean var1;
      if(!TextUtils.isEmpty(this.getText())) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void initialize(MenuItemImpl var1, int var2) {
      this.mItemData = var1;
      this.setIcon(var1.getIcon());
      this.setTitle(var1.getTitleForItemView(this));
      this.setId(var1.getItemId());
      byte var3;
      if(var1.isVisible()) {
         var3 = 0;
      } else {
         var3 = 8;
      }

      this.setVisibility(var3);
      this.setEnabled(var1.isEnabled());
      if(var1.hasSubMenu() && this.mForwardingListener == null) {
         this.mForwardingListener = new ActionMenuItemView.ActionMenuItemForwardingListener();
      }

   }

   public boolean needsDividerAfter() {
      return this.hasText();
   }

   public boolean needsDividerBefore() {
      boolean var1;
      if(this.hasText() && this.mItemData.getIcon() == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void onClick(View var1) {
      if(this.mItemInvoker != null) {
         this.mItemInvoker.invokeItem(this.mItemData);
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
      this.updateTextButtonVisibility();
   }

   public boolean onLongClick(View var1) {
      boolean var7 = false;
      if(!this.hasText()) {
         int[] var8 = new int[2];
         Rect var9 = new Rect();
         this.getLocationOnScreen(var8);
         this.getWindowVisibleDisplayFrame(var9);
         Context var10 = this.getContext();
         int var2 = this.getWidth();
         int var5 = this.getHeight();
         int var6 = var8[1];
         int var4 = var5 / 2;
         int var3 = var8[0] + var2 / 2;
         var2 = var3;
         if(ViewCompat.getLayoutDirection(var1) == 0) {
            var2 = var10.getResources().getDisplayMetrics().widthPixels - var3;
         }

         Toast var11 = Toast.makeText(var10, this.mItemData.getTitle(), 0);
         if(var6 + var4 < var9.height()) {
            var11.setGravity(8388661, var2, var8[1] + var5 - var9.top);
         } else {
            var11.setGravity(81, 0, var5);
         }

         var11.show();
         var7 = true;
      }

      return var7;
   }

   protected void onMeasure(int var1, int var2) {
      boolean var5 = this.hasText();
      if(var5 && this.mSavedPaddingLeft >= 0) {
         super.setPadding(this.mSavedPaddingLeft, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
      }

      super.onMeasure(var1, var2);
      int var3 = MeasureSpec.getMode(var1);
      var1 = MeasureSpec.getSize(var1);
      int var4 = this.getMeasuredWidth();
      if(var3 == Integer.MIN_VALUE) {
         var1 = Math.min(var1, this.mMinWidth);
      } else {
         var1 = this.mMinWidth;
      }

      if(var3 != 1073741824 && this.mMinWidth > 0 && var4 < var1) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(var1, 1073741824), var2);
      }

      if(!var5 && this.mIcon != null) {
         super.setPadding((this.getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      super.onRestoreInstanceState((Parcelable)null);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2;
      if(this.mItemData.hasSubMenu() && this.mForwardingListener != null && this.mForwardingListener.onTouch(this, var1)) {
         var2 = true;
      } else {
         var2 = super.onTouchEvent(var1);
      }

      return var2;
   }

   public boolean prefersCondensedTitle() {
      return true;
   }

   public void setCheckable(boolean var1) {
   }

   public void setChecked(boolean var1) {
   }

   public void setExpandedFormat(boolean var1) {
      if(this.mExpandedFormat != var1) {
         this.mExpandedFormat = var1;
         if(this.mItemData != null) {
            this.mItemData.actionFormatChanged();
         }
      }

   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
      if(var1 != null) {
         int var6 = var1.getIntrinsicWidth();
         int var5 = var1.getIntrinsicHeight();
         int var4 = var5;
         int var3 = var6;
         float var2;
         if(var6 > this.mMaxIconSize) {
            var2 = (float)this.mMaxIconSize / (float)var6;
            var3 = this.mMaxIconSize;
            var4 = (int)((float)var5 * var2);
         }

         var6 = var4;
         var5 = var3;
         if(var4 > this.mMaxIconSize) {
            var2 = (float)this.mMaxIconSize / (float)var4;
            var6 = this.mMaxIconSize;
            var5 = (int)((float)var3 * var2);
         }

         var1.setBounds(0, 0, var5, var6);
      }

      this.setCompoundDrawables(var1, (Drawable)null, (Drawable)null, (Drawable)null);
      this.updateTextButtonVisibility();
   }

   public void setItemInvoker(MenuBuilder.ItemInvoker var1) {
      this.mItemInvoker = var1;
   }

   public void setPadding(int var1, int var2, int var3, int var4) {
      this.mSavedPaddingLeft = var1;
      super.setPadding(var1, var2, var3, var4);
   }

   public void setPopupCallback(ActionMenuItemView.PopupCallback var1) {
      this.mPopupCallback = var1;
   }

   public void setShortcut(boolean var1, char var2) {
   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.setContentDescription(this.mTitle);
      this.updateTextButtonVisibility();
   }

   public boolean showsIcon() {
      return true;
   }

   private class ActionMenuItemForwardingListener extends ForwardingListener {
      public ActionMenuItemForwardingListener() {
         super(ActionMenuItemView.this);
      }

      public ShowableListMenu getPopup() {
         ShowableListMenu var1;
         if(ActionMenuItemView.this.mPopupCallback != null) {
            var1 = ActionMenuItemView.this.mPopupCallback.getPopup();
         } else {
            var1 = null;
         }

         return var1;
      }

      protected boolean onForwardingStarted() {
         boolean var2 = false;
         boolean var1 = var2;
         if(ActionMenuItemView.this.mItemInvoker != null) {
            var1 = var2;
            if(ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData)) {
               ShowableListMenu var3 = this.getPopup();
               var1 = var2;
               if(var3 != null) {
                  var1 = var2;
                  if(var3.isShowing()) {
                     var1 = true;
                  }
               }
            }
         }

         return var1;
      }
   }

   public abstract static class PopupCallback {
      public abstract ShowableListMenu getPopup();
   }
}
