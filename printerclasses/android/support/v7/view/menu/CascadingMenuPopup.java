package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopup;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.MenuPopupWindow;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnKeyListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, OnKeyListener, OnDismissListener {
   static final int HORIZ_POSITION_LEFT = 0;
   static final int HORIZ_POSITION_RIGHT = 1;
   static final int SUBMENU_TIMEOUT_MS = 200;
   private View mAnchorView;
   private final Context mContext;
   private int mDropDownGravity = 0;
   private boolean mForceShowIcon;
   private final OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener() {
      public void onGlobalLayout() {
         if(CascadingMenuPopup.this.isShowing() && CascadingMenuPopup.this.mShowingMenus.size() > 0 && !((CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(0)).window.isModal()) {
            View var1 = CascadingMenuPopup.this.mShownAnchorView;
            if(var1 != null && var1.isShown()) {
               Iterator var2 = CascadingMenuPopup.this.mShowingMenus.iterator();

               while(var2.hasNext()) {
                  ((CascadingMenuPopup.CascadingMenuInfo)var2.next()).window.show();
               }
            } else {
               CascadingMenuPopup.this.dismiss();
            }
         }

      }
   };
   private boolean mHasXOffset;
   private boolean mHasYOffset;
   private int mLastPosition;
   private final MenuItemHoverListener mMenuItemHoverListener = new MenuItemHoverListener() {
      public void onItemHoverEnter(@NonNull final MenuBuilder var1, @NonNull final MenuItem var2) {
         CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages((Object)null);
         byte var5 = -1;
         int var3 = 0;
         int var6 = CascadingMenuPopup.this.mShowingMenus.size();

         int var4;
         while(true) {
            var4 = var5;
            if(var3 >= var6) {
               break;
            }

            if(var1 == ((CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(var3)).menu) {
               var4 = var3;
               break;
            }

            ++var3;
         }

         if(var4 != -1) {
            var3 = var4 + 1;
            final CascadingMenuPopup.CascadingMenuInfo var9;
            if(var3 < CascadingMenuPopup.this.mShowingMenus.size()) {
               var9 = (CascadingMenuPopup.CascadingMenuInfo)CascadingMenuPopup.this.mShowingMenus.get(var3);
            } else {
               var9 = null;
            }

            Runnable var10 = new Runnable() {
               public void run() {
                  if(var9 != null) {
                     CascadingMenuPopup.this.mShouldCloseImmediately = true;
                     var9.menu.close(false);
                     CascadingMenuPopup.this.mShouldCloseImmediately = false;
                  }

                  if(var2.isEnabled() && var2.hasSubMenu()) {
                     var1.performItemAction(var2, 0);
                  }

               }
            };
            long var7 = SystemClock.uptimeMillis();
            CascadingMenuPopup.this.mSubMenuHoverHandler.postAtTime(var10, var1, var7 + 200L);
         }

      }

      public void onItemHoverExit(@NonNull MenuBuilder var1, @NonNull MenuItem var2) {
         CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(var1);
      }
   };
   private final int mMenuMaxWidth;
   private OnDismissListener mOnDismissListener;
   private final boolean mOverflowOnly;
   private final List mPendingMenus = new LinkedList();
   private final int mPopupStyleAttr;
   private final int mPopupStyleRes;
   private MenuPresenter.Callback mPresenterCallback;
   private int mRawDropDownGravity = 0;
   boolean mShouldCloseImmediately;
   private boolean mShowTitle;
   final List mShowingMenus = new ArrayList();
   View mShownAnchorView;
   final Handler mSubMenuHoverHandler;
   private ViewTreeObserver mTreeObserver;
   private int mXOffset;
   private int mYOffset;

   public CascadingMenuPopup(@NonNull Context var1, @NonNull View var2, @AttrRes int var3, @StyleRes int var4, boolean var5) {
      this.mContext = var1;
      this.mAnchorView = var2;
      this.mPopupStyleAttr = var3;
      this.mPopupStyleRes = var4;
      this.mOverflowOnly = var5;
      this.mForceShowIcon = false;
      this.mLastPosition = this.getInitialMenuPosition();
      Resources var6 = var1.getResources();
      this.mMenuMaxWidth = Math.max(var6.getDisplayMetrics().widthPixels / 2, var6.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
      this.mSubMenuHoverHandler = new Handler();
   }

   private MenuPopupWindow createPopupWindow() {
      MenuPopupWindow var1 = new MenuPopupWindow(this.mContext, (AttributeSet)null, this.mPopupStyleAttr, this.mPopupStyleRes);
      var1.setHoverListener(this.mMenuItemHoverListener);
      var1.setOnItemClickListener(this);
      var1.setOnDismissListener(this);
      var1.setAnchorView(this.mAnchorView);
      var1.setDropDownGravity(this.mDropDownGravity);
      var1.setModal(true);
      return var1;
   }

   private int findIndexOfAddedMenu(@NonNull MenuBuilder var1) {
      int var2 = 0;
      int var3 = this.mShowingMenus.size();

      while(true) {
         if(var2 >= var3) {
            var2 = -1;
            break;
         }

         if(var1 == ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var2)).menu) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   private MenuItem findMenuItemForSubmenu(@NonNull MenuBuilder var1, @NonNull MenuBuilder var2) {
      int var3 = 0;
      int var4 = var1.size();

      MenuItem var6;
      while(true) {
         if(var3 >= var4) {
            var6 = null;
            break;
         }

         MenuItem var5 = var1.getItem(var3);
         if(var5.hasSubMenu() && var2 == var5.getSubMenu()) {
            var6 = var5;
            break;
         }

         ++var3;
      }

      return var6;
   }

   @Nullable
   private View findParentViewForSubmenu(@NonNull CascadingMenuPopup.CascadingMenuInfo var1, @NonNull MenuBuilder var2) {
      Object var8 = null;
      MenuItem var13 = this.findMenuItemForSubmenu(var1.menu, var2);
      View var10;
      if(var13 == null) {
         var10 = (View)var8;
      } else {
         ListView var9 = var1.getListView();
         ListAdapter var11 = var9.getAdapter();
         int var4;
         MenuAdapter var14;
         if(var11 instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter var12 = (HeaderViewListAdapter)var11;
            var4 = var12.getHeadersCount();
            var14 = (MenuAdapter)var12.getWrappedAdapter();
         } else {
            var4 = 0;
            var14 = (MenuAdapter)var11;
         }

         byte var6 = -1;
         int var3 = 0;
         int var7 = var14.getCount();

         int var5;
         while(true) {
            var5 = var6;
            if(var3 >= var7) {
               break;
            }

            if(var13 == var14.getItem(var3)) {
               var5 = var3;
               break;
            }

            ++var3;
         }

         var10 = (View)var8;
         if(var5 != -1) {
            var3 = var5 + var4 - var9.getFirstVisiblePosition();
            var10 = (View)var8;
            if(var3 >= 0) {
               var10 = (View)var8;
               if(var3 < var9.getChildCount()) {
                  var10 = var9.getChildAt(var3);
               }
            }
         }
      }

      return var10;
   }

   private int getInitialMenuPosition() {
      byte var1 = 1;
      if(ViewCompat.getLayoutDirection(this.mAnchorView) == 1) {
         var1 = 0;
      }

      return var1;
   }

   private int getNextMenuPosition(int var1) {
      ListView var2 = ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(this.mShowingMenus.size() - 1)).getListView();
      int[] var4 = new int[2];
      var2.getLocationOnScreen(var4);
      Rect var3 = new Rect();
      this.mShownAnchorView.getWindowVisibleDisplayFrame(var3);
      byte var5;
      if(this.mLastPosition == 1) {
         if(var4[0] + var2.getWidth() + var1 > var3.right) {
            var5 = 0;
         } else {
            var5 = 1;
         }
      } else if(var4[0] - var1 < 0) {
         var5 = 1;
      } else {
         var5 = 0;
      }

      return var5;
   }

   private void showMenu(@NonNull MenuBuilder var1) {
      LayoutInflater var10 = LayoutInflater.from(this.mContext);
      MenuAdapter var7 = new MenuAdapter(var1, var10, this.mOverflowOnly);
      if(!this.isShowing() && this.mForceShowIcon) {
         var7.setForceShowIcon(true);
      } else if(this.isShowing()) {
         var7.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(var1));
      }

      int var5 = measureIndividualMenuWidth(var7, (ViewGroup)null, this.mContext, this.mMenuMaxWidth);
      MenuPopupWindow var9 = this.createPopupWindow();
      var9.setAdapter(var7);
      var9.setContentWidth(var5);
      var9.setDropDownGravity(this.mDropDownGravity);
      View var8;
      CascadingMenuPopup.CascadingMenuInfo var13;
      if(this.mShowingMenus.size() > 0) {
         var13 = (CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(this.mShowingMenus.size() - 1);
         var8 = this.findParentViewForSubmenu(var13, var1);
      } else {
         var13 = null;
         var8 = null;
      }

      if(var8 != null) {
         var9.setTouchModal(false);
         var9.setEnterTransition((Object)null);
         int var3 = this.getNextMenuPosition(var5);
         boolean var2;
         if(var3 == 1) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.mLastPosition = var3;
         int[] var11 = new int[2];
         var8.getLocationInWindow(var11);
         int var6 = var13.window.getHorizontalOffset() + var11[0];
         int var4 = var13.window.getVerticalOffset();
         var3 = var11[1];
         int var12;
         if((this.mDropDownGravity & 5) == 5) {
            if(var2) {
               var12 = var6 + var5;
            } else {
               var12 = var6 - var8.getWidth();
            }
         } else if(var2) {
            var12 = var6 + var8.getWidth();
         } else {
            var12 = var6 - var5;
         }

         var9.setHorizontalOffset(var12);
         var9.setVerticalOffset(var4 + var3);
      } else {
         if(this.mHasXOffset) {
            var9.setHorizontalOffset(this.mXOffset);
         }

         if(this.mHasYOffset) {
            var9.setVerticalOffset(this.mYOffset);
         }

         var9.setEpicenterBounds(this.getEpicenterBounds());
      }

      CascadingMenuPopup.CascadingMenuInfo var15 = new CascadingMenuPopup.CascadingMenuInfo(var9, var1, this.mLastPosition);
      this.mShowingMenus.add(var15);
      var9.show();
      if(var13 == null && this.mShowTitle && var1.getHeaderTitle() != null) {
         ListView var14 = var9.getListView();
         FrameLayout var17 = (FrameLayout)var10.inflate(R.layout.abc_popup_menu_header_item_layout, var14, false);
         TextView var16 = (TextView)var17.findViewById(16908310);
         var17.setEnabled(false);
         var16.setText(var1.getHeaderTitle());
         var14.addHeaderView(var17, (Object)null, false);
         var9.show();
      }

   }

   public void addMenu(MenuBuilder var1) {
      var1.addMenuPresenter(this, this.mContext);
      if(this.isShowing()) {
         this.showMenu(var1);
      } else {
         this.mPendingMenus.add(var1);
      }

   }

   protected boolean closeMenuOnSubMenuOpened() {
      return false;
   }

   public void dismiss() {
      int var1 = this.mShowingMenus.size();
      if(var1 > 0) {
         CascadingMenuPopup.CascadingMenuInfo[] var3 = (CascadingMenuPopup.CascadingMenuInfo[])this.mShowingMenus.toArray(new CascadingMenuPopup.CascadingMenuInfo[var1]);
         --var1;

         for(; var1 >= 0; --var1) {
            CascadingMenuPopup.CascadingMenuInfo var2 = var3[var1];
            if(var2.window.isShowing()) {
               var2.window.dismiss();
            }
         }
      }

   }

   public boolean flagActionItems() {
      return false;
   }

   public ListView getListView() {
      ListView var1;
      if(this.mShowingMenus.isEmpty()) {
         var1 = null;
      } else {
         var1 = ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(this.mShowingMenus.size() - 1)).getListView();
      }

      return var1;
   }

   public boolean isShowing() {
      boolean var1;
      if(this.mShowingMenus.size() > 0 && ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(0)).window.isShowing()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      int var4 = this.findIndexOfAddedMenu(var1);
      if(var4 >= 0) {
         int var3 = var4 + 1;
         if(var3 < this.mShowingMenus.size()) {
            ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var3)).menu.close(false);
         }

         CascadingMenuPopup.CascadingMenuInfo var5 = (CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.remove(var4);
         var5.menu.removeMenuPresenter(this);
         if(this.mShouldCloseImmediately) {
            var5.window.setExitTransition((Object)null);
            var5.window.setAnimationStyle(0);
         }

         var5.window.dismiss();
         var3 = this.mShowingMenus.size();
         if(var3 > 0) {
            this.mLastPosition = ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var3 - 1)).position;
         } else {
            this.mLastPosition = this.getInitialMenuPosition();
         }

         if(var3 == 0) {
            this.dismiss();
            if(this.mPresenterCallback != null) {
               this.mPresenterCallback.onCloseMenu(var1, true);
            }

            if(this.mTreeObserver != null) {
               if(this.mTreeObserver.isAlive()) {
                  this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
               }

               this.mTreeObserver = null;
            }

            this.mOnDismissListener.onDismiss();
         } else if(var2) {
            ((CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(0)).menu.close(false);
         }
      }

   }

   public void onDismiss() {
      Object var4 = null;
      int var1 = 0;
      int var2 = this.mShowingMenus.size();

      CascadingMenuPopup.CascadingMenuInfo var3;
      while(true) {
         var3 = (CascadingMenuPopup.CascadingMenuInfo)var4;
         if(var1 >= var2) {
            break;
         }

         var3 = (CascadingMenuPopup.CascadingMenuInfo)this.mShowingMenus.get(var1);
         if(!var3.window.isShowing()) {
            break;
         }

         ++var1;
      }

      if(var3 != null) {
         var3.menu.close(false);
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
      boolean var3 = true;
      Iterator var4 = this.mShowingMenus.iterator();

      boolean var2;
      while(true) {
         if(var4.hasNext()) {
            CascadingMenuPopup.CascadingMenuInfo var5 = (CascadingMenuPopup.CascadingMenuInfo)var4.next();
            if(var1 != var5.menu) {
               continue;
            }

            var5.getListView().requestFocus();
            var2 = var3;
            break;
         }

         if(var1.hasVisibleItems()) {
            this.addMenu(var1);
            var2 = var3;
            if(this.mPresenterCallback != null) {
               this.mPresenterCallback.onOpenSubMenu(var1);
               var2 = var3;
            }
         } else {
            var2 = false;
         }
         break;
      }

      return var2;
   }

   public void setAnchorView(@NonNull View var1) {
      if(this.mAnchorView != var1) {
         this.mAnchorView = var1;
         this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView));
      }

   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
   }

   public void setGravity(int var1) {
      if(this.mRawDropDownGravity != var1) {
         this.mRawDropDownGravity = var1;
         this.mDropDownGravity = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this.mAnchorView));
      }

   }

   public void setHorizontalOffset(int var1) {
      this.mHasXOffset = true;
      this.mXOffset = var1;
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mOnDismissListener = var1;
   }

   public void setShowTitle(boolean var1) {
      this.mShowTitle = var1;
   }

   public void setVerticalOffset(int var1) {
      this.mHasYOffset = true;
      this.mYOffset = var1;
   }

   public void show() {
      if(!this.isShowing()) {
         Iterator var2 = this.mPendingMenus.iterator();

         while(var2.hasNext()) {
            this.showMenu((MenuBuilder)var2.next());
         }

         this.mPendingMenus.clear();
         this.mShownAnchorView = this.mAnchorView;
         if(this.mShownAnchorView != null) {
            boolean var1;
            if(this.mTreeObserver == null) {
               var1 = true;
            } else {
               var1 = false;
            }

            this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            if(var1) {
               this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }
         }
      }

   }

   public void updateMenuView(boolean var1) {
      Iterator var2 = this.mShowingMenus.iterator();

      while(var2.hasNext()) {
         toMenuAdapter(((CascadingMenuPopup.CascadingMenuInfo)var2.next()).getListView().getAdapter()).notifyDataSetChanged();
      }

   }

   private static class CascadingMenuInfo {
      public final MenuBuilder menu;
      public final int position;
      public final MenuPopupWindow window;

      public CascadingMenuInfo(@NonNull MenuPopupWindow var1, @NonNull MenuBuilder var2, int var3) {
         this.window = var1;
         this.menu = var2;
         this.position = var3;
      }

      public ListView getListView() {
         return this.window.getListView();
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface HorizPosition {
   }
}
