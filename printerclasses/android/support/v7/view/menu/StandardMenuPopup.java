package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopup;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.MenuPopupWindow;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnKeyListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

final class StandardMenuPopup extends MenuPopup implements OnDismissListener, OnItemClickListener, MenuPresenter, OnKeyListener {
   private final MenuAdapter mAdapter;
   private View mAnchorView;
   private int mContentWidth;
   private final Context mContext;
   private int mDropDownGravity = 0;
   private final OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener() {
      public void onGlobalLayout() {
         if(StandardMenuPopup.this.isShowing() && !StandardMenuPopup.this.mPopup.isModal()) {
            View var1 = StandardMenuPopup.this.mShownAnchorView;
            if(var1 != null && var1.isShown()) {
               StandardMenuPopup.this.mPopup.show();
            } else {
               StandardMenuPopup.this.dismiss();
            }
         }

      }
   };
   private boolean mHasContentWidth;
   private final MenuBuilder mMenu;
   private OnDismissListener mOnDismissListener;
   private final boolean mOverflowOnly;
   final MenuPopupWindow mPopup;
   private final int mPopupMaxWidth;
   private final int mPopupStyleAttr;
   private final int mPopupStyleRes;
   private MenuPresenter.Callback mPresenterCallback;
   private boolean mShowTitle;
   View mShownAnchorView;
   private ViewTreeObserver mTreeObserver;
   private boolean mWasDismissed;

   public StandardMenuPopup(Context var1, MenuBuilder var2, View var3, int var4, int var5, boolean var6) {
      this.mContext = var1;
      this.mMenu = var2;
      this.mOverflowOnly = var6;
      this.mAdapter = new MenuAdapter(var2, LayoutInflater.from(var1), this.mOverflowOnly);
      this.mPopupStyleAttr = var4;
      this.mPopupStyleRes = var5;
      Resources var7 = var1.getResources();
      this.mPopupMaxWidth = Math.max(var7.getDisplayMetrics().widthPixels / 2, var7.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
      this.mAnchorView = var3;
      this.mPopup = new MenuPopupWindow(this.mContext, (AttributeSet)null, this.mPopupStyleAttr, this.mPopupStyleRes);
      var2.addMenuPresenter(this, var1);
   }

   private boolean tryShow() {
      boolean var2 = true;
      if(!this.isShowing()) {
         if(!this.mWasDismissed && this.mAnchorView != null) {
            this.mShownAnchorView = this.mAnchorView;
            this.mPopup.setOnDismissListener(this);
            this.mPopup.setOnItemClickListener(this);
            this.mPopup.setModal(true);
            View var3 = this.mShownAnchorView;
            boolean var1;
            if(this.mTreeObserver == null) {
               var1 = true;
            } else {
               var1 = false;
            }

            this.mTreeObserver = var3.getViewTreeObserver();
            if(var1) {
               this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }

            this.mPopup.setAnchorView(var3);
            this.mPopup.setDropDownGravity(this.mDropDownGravity);
            if(!this.mHasContentWidth) {
               this.mContentWidth = measureIndividualMenuWidth(this.mAdapter, (ViewGroup)null, this.mContext, this.mPopupMaxWidth);
               this.mHasContentWidth = true;
            }

            this.mPopup.setContentWidth(this.mContentWidth);
            this.mPopup.setInputMethodMode(2);
            this.mPopup.setEpicenterBounds(this.getEpicenterBounds());
            this.mPopup.show();
            ListView var6 = this.mPopup.getListView();
            var6.setOnKeyListener(this);
            if(this.mShowTitle && this.mMenu.getHeaderTitle() != null) {
               FrameLayout var5 = (FrameLayout)LayoutInflater.from(this.mContext).inflate(R.layout.abc_popup_menu_header_item_layout, var6, false);
               TextView var4 = (TextView)var5.findViewById(16908310);
               if(var4 != null) {
                  var4.setText(this.mMenu.getHeaderTitle());
               }

               var5.setEnabled(false);
               var6.addHeaderView(var5, (Object)null, false);
            }

            this.mPopup.setAdapter(this.mAdapter);
            this.mPopup.show();
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public void addMenu(MenuBuilder var1) {
   }

   public void dismiss() {
      if(this.isShowing()) {
         this.mPopup.dismiss();
      }

   }

   public boolean flagActionItems() {
      return false;
   }

   public ListView getListView() {
      return this.mPopup.getListView();
   }

   public boolean isShowing() {
      boolean var1;
      if(!this.mWasDismissed && this.mPopup.isShowing()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      if(var1 == this.mMenu) {
         this.dismiss();
         if(this.mPresenterCallback != null) {
            this.mPresenterCallback.onCloseMenu(var1, var2);
         }
      }

   }

   public void onDismiss() {
      this.mWasDismissed = true;
      this.mMenu.close();
      if(this.mTreeObserver != null) {
         if(!this.mTreeObserver.isAlive()) {
            this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
         }

         this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
         this.mTreeObserver = null;
      }

      if(this.mOnDismissListener != null) {
         this.mOnDismissListener.onDismiss();
      }

   }

   public boolean onKey(View var1, int var2, KeyEvent var3) {
      boolean var4 = true;
      if(var3.getAction() == 1 && var2 == 82) {
         this.dismiss();
      } else {
         var4 = false;
      }

      return var4;
   }

   public void onRestoreInstanceState(Parcelable var1) {
   }

   public Parcelable onSaveInstanceState() {
      return null;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      boolean var2;
      if(var1.hasVisibleItems()) {
         MenuPopupHelper var3 = new MenuPopupHelper(this.mContext, var1, this.mShownAnchorView, this.mOverflowOnly, this.mPopupStyleAttr, this.mPopupStyleRes);
         var3.setPresenterCallback(this.mPresenterCallback);
         var3.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(var1));
         var3.setOnDismissListener(this.mOnDismissListener);
         this.mOnDismissListener = null;
         this.mMenu.close(false);
         if(var3.tryShow(this.mPopup.getHorizontalOffset(), this.mPopup.getVerticalOffset())) {
            if(this.mPresenterCallback != null) {
               this.mPresenterCallback.onOpenSubMenu(var1);
            }

            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public void setAnchorView(View var1) {
      this.mAnchorView = var1;
   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
   }

   public void setForceShowIcon(boolean var1) {
      this.mAdapter.setForceShowIcon(var1);
   }

   public void setGravity(int var1) {
      this.mDropDownGravity = var1;
   }

   public void setHorizontalOffset(int var1) {
      this.mPopup.setHorizontalOffset(var1);
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setShowTitle(boolean var1) {
      this.mShowTitle = var1;
   }

   public void setVerticalOffset(int var1) {
      this.mPopup.setVerticalOffset(var1);
   }

   public void show() {
      if(!this.tryShow()) {
         throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
      }
   }

   public void updateMenuView(boolean var1) {
      this.mHasContentWidth = false;
      if(this.mAdapter != null) {
         this.mAdapter.notifyDataSetChanged();
      }

   }
}
