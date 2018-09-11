package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.NavItemSelectedListener;
import android.support.v7.appcompat.R;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window.Callback;
import android.widget.SpinnerAdapter;
import java.util.ArrayList;

class ToolbarActionBar extends ActionBar {
   DecorToolbar mDecorToolbar;
   private boolean mLastMenuVisibility;
   private ListMenuPresenter mListMenuPresenter;
   private boolean mMenuCallbackSet;
   private final Toolbar.OnMenuItemClickListener mMenuClicker = new Toolbar.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem var1) {
         return ToolbarActionBar.this.mWindowCallback.onMenuItemSelected(0, var1);
      }
   };
   private final Runnable mMenuInvalidator = new Runnable() {
      public void run() {
         ToolbarActionBar.this.populateOptionsMenu();
      }
   };
   private ArrayList mMenuVisibilityListeners = new ArrayList();
   boolean mToolbarMenuPrepared;
   Callback mWindowCallback;

   public ToolbarActionBar(Toolbar var1, CharSequence var2, Callback var3) {
      this.mDecorToolbar = new ToolbarWidgetWrapper(var1, false);
      this.mWindowCallback = new ToolbarActionBar.ToolbarCallbackWrapper(var3);
      this.mDecorToolbar.setWindowCallback(this.mWindowCallback);
      var1.setOnMenuItemClickListener(this.mMenuClicker);
      this.mDecorToolbar.setWindowTitle(var2);
   }

   private void ensureListMenuPresenter(Menu var1) {
      if(this.mListMenuPresenter == null && var1 instanceof MenuBuilder) {
         MenuBuilder var2 = (MenuBuilder)var1;
         Context var4 = this.mDecorToolbar.getContext();
         TypedValue var3 = new TypedValue();
         Theme var5 = var4.getResources().newTheme();
         var5.setTo(var4.getTheme());
         var5.resolveAttribute(R.attr.actionBarPopupTheme, var3, true);
         if(var3.resourceId != 0) {
            var5.applyStyle(var3.resourceId, true);
         }

         var5.resolveAttribute(R.attr.panelMenuListTheme, var3, true);
         if(var3.resourceId != 0) {
            var5.applyStyle(var3.resourceId, true);
         } else {
            var5.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
         }

         ContextThemeWrapper var6 = new ContextThemeWrapper(var4, 0);
         var6.getTheme().setTo(var5);
         this.mListMenuPresenter = new ListMenuPresenter(var6, R.layout.abc_list_menu_item_layout);
         this.mListMenuPresenter.setCallback(new ToolbarActionBar.PanelMenuPresenterCallback());
         var2.addMenuPresenter(this.mListMenuPresenter);
      }

   }

   private Menu getMenu() {
      if(!this.mMenuCallbackSet) {
         this.mDecorToolbar.setMenuCallbacks(new ToolbarActionBar.ActionMenuPresenterCallback(), new ToolbarActionBar.MenuBuilderCallback());
         this.mMenuCallbackSet = true;
      }

      return this.mDecorToolbar.getMenu();
   }

   public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1) {
      this.mMenuVisibilityListeners.add(var1);
   }

   public void addTab(ActionBar.Tab var1) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public void addTab(ActionBar.Tab var1, int var2) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public void addTab(ActionBar.Tab var1, int var2, boolean var3) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public void addTab(ActionBar.Tab var1, boolean var2) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public boolean collapseActionView() {
      boolean var1;
      if(this.mDecorToolbar.hasExpandedActionView()) {
         this.mDecorToolbar.collapseActionView();
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void dispatchMenuVisibilityChanged(boolean var1) {
      if(var1 != this.mLastMenuVisibility) {
         this.mLastMenuVisibility = var1;
         int var3 = this.mMenuVisibilityListeners.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            ((ActionBar.OnMenuVisibilityListener)this.mMenuVisibilityListeners.get(var2)).onMenuVisibilityChanged(var1);
         }
      }

   }

   public View getCustomView() {
      return this.mDecorToolbar.getCustomView();
   }

   public int getDisplayOptions() {
      return this.mDecorToolbar.getDisplayOptions();
   }

   public float getElevation() {
      return ViewCompat.getElevation(this.mDecorToolbar.getViewGroup());
   }

   public int getHeight() {
      return this.mDecorToolbar.getHeight();
   }

   View getListMenuView(Menu var1) {
      Object var3 = null;
      this.ensureListMenuPresenter(var1);
      View var2 = (View)var3;
      if(var1 != null) {
         if(this.mListMenuPresenter == null) {
            var2 = (View)var3;
         } else {
            var2 = (View)var3;
            if(this.mListMenuPresenter.getAdapter().getCount() > 0) {
               var2 = (View)this.mListMenuPresenter.getMenuView(this.mDecorToolbar.getViewGroup());
            }
         }
      }

      return var2;
   }

   public int getNavigationItemCount() {
      return 0;
   }

   public int getNavigationMode() {
      return 0;
   }

   public int getSelectedNavigationIndex() {
      return -1;
   }

   public ActionBar.Tab getSelectedTab() {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public CharSequence getSubtitle() {
      return this.mDecorToolbar.getSubtitle();
   }

   public ActionBar.Tab getTabAt(int var1) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public int getTabCount() {
      return 0;
   }

   public Context getThemedContext() {
      return this.mDecorToolbar.getContext();
   }

   public CharSequence getTitle() {
      return this.mDecorToolbar.getTitle();
   }

   public Callback getWrappedWindowCallback() {
      return this.mWindowCallback;
   }

   public void hide() {
      this.mDecorToolbar.setVisibility(8);
   }

   public boolean invalidateOptionsMenu() {
      this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
      ViewCompat.postOnAnimation(this.mDecorToolbar.getViewGroup(), this.mMenuInvalidator);
      return true;
   }

   public boolean isShowing() {
      boolean var1;
      if(this.mDecorToolbar.getVisibility() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isTitleTruncated() {
      return super.isTitleTruncated();
   }

   public ActionBar.Tab newTab() {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
   }

   void onDestroy() {
      this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
   }

   public boolean onKeyShortcut(int var1, KeyEvent var2) {
      Menu var5 = this.getMenu();
      if(var5 != null) {
         int var3;
         if(var2 != null) {
            var3 = var2.getDeviceId();
         } else {
            var3 = -1;
         }

         boolean var4;
         if(KeyCharacterMap.load(var3).getKeyboardType() != 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.setQwertyMode(var4);
         var5.performShortcut(var1, var2, 0);
      }

      return true;
   }

   public boolean onMenuKeyEvent(KeyEvent var1) {
      if(var1.getAction() == 1) {
         this.openOptionsMenu();
      }

      return true;
   }

   public boolean openOptionsMenu() {
      return this.mDecorToolbar.showOverflowMenu();
   }

   void populateOptionsMenu() {
      MenuBuilder var1 = null;
      Menu var2 = this.getMenu();
      if(var2 instanceof MenuBuilder) {
         var1 = (MenuBuilder)var2;
      }

      if(var1 != null) {
         var1.stopDispatchingItemsChanged();
      }

      try {
         var2.clear();
         if(!this.mWindowCallback.onCreatePanelMenu(0, var2) || !this.mWindowCallback.onPreparePanel(0, (View)null, var2)) {
            var2.clear();
         }
      } finally {
         if(var1 != null) {
            var1.startDispatchingItemsChanged();
         }

      }

   }

   public void removeAllTabs() {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1) {
      this.mMenuVisibilityListeners.remove(var1);
   }

   public void removeTab(ActionBar.Tab var1) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public void removeTabAt(int var1) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public boolean requestFocus() {
      ViewGroup var2 = this.mDecorToolbar.getViewGroup();
      boolean var1;
      if(var2 != null && !var2.hasFocus()) {
         var2.requestFocus();
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void selectTab(ActionBar.Tab var1) {
      throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
   }

   public void setBackgroundDrawable(@Nullable Drawable var1) {
      this.mDecorToolbar.setBackgroundDrawable(var1);
   }

   public void setCustomView(int var1) {
      this.setCustomView(LayoutInflater.from(this.mDecorToolbar.getContext()).inflate(var1, this.mDecorToolbar.getViewGroup(), false));
   }

   public void setCustomView(View var1) {
      this.setCustomView(var1, new ActionBar.LayoutParams(-2, -2));
   }

   public void setCustomView(View var1, ActionBar.LayoutParams var2) {
      if(var1 != null) {
         var1.setLayoutParams(var2);
      }

      this.mDecorToolbar.setCustomView(var1);
   }

   public void setDefaultDisplayHomeAsUpEnabled(boolean var1) {
   }

   public void setDisplayHomeAsUpEnabled(boolean var1) {
      byte var2;
      if(var1) {
         var2 = 4;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 4);
   }

   public void setDisplayOptions(int var1) {
      this.setDisplayOptions(var1, -1);
   }

   public void setDisplayOptions(int var1, int var2) {
      int var3 = this.mDecorToolbar.getDisplayOptions();
      this.mDecorToolbar.setDisplayOptions(var1 & var2 | ~var2 & var3);
   }

   public void setDisplayShowCustomEnabled(boolean var1) {
      byte var2;
      if(var1) {
         var2 = 16;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 16);
   }

   public void setDisplayShowHomeEnabled(boolean var1) {
      byte var2;
      if(var1) {
         var2 = 2;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 2);
   }

   public void setDisplayShowTitleEnabled(boolean var1) {
      byte var2;
      if(var1) {
         var2 = 8;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 8);
   }

   public void setDisplayUseLogoEnabled(boolean var1) {
      byte var2;
      if(var1) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 1);
   }

   public void setElevation(float var1) {
      ViewCompat.setElevation(this.mDecorToolbar.getViewGroup(), var1);
   }

   public void setHomeActionContentDescription(int var1) {
      this.mDecorToolbar.setNavigationContentDescription(var1);
   }

   public void setHomeActionContentDescription(CharSequence var1) {
      this.mDecorToolbar.setNavigationContentDescription(var1);
   }

   public void setHomeAsUpIndicator(int var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeAsUpIndicator(Drawable var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeButtonEnabled(boolean var1) {
   }

   public void setIcon(int var1) {
      this.mDecorToolbar.setIcon(var1);
   }

   public void setIcon(Drawable var1) {
      this.mDecorToolbar.setIcon(var1);
   }

   public void setListNavigationCallbacks(SpinnerAdapter var1, ActionBar.OnNavigationListener var2) {
      this.mDecorToolbar.setDropdownParams(var1, new NavItemSelectedListener(var2));
   }

   public void setLogo(int var1) {
      this.mDecorToolbar.setLogo(var1);
   }

   public void setLogo(Drawable var1) {
      this.mDecorToolbar.setLogo(var1);
   }

   public void setNavigationMode(int var1) {
      if(var1 == 2) {
         throw new IllegalArgumentException("Tabs not supported in this configuration");
      } else {
         this.mDecorToolbar.setNavigationMode(var1);
      }
   }

   public void setSelectedNavigationItem(int var1) {
      switch(this.mDecorToolbar.getNavigationMode()) {
      case 1:
         this.mDecorToolbar.setDropdownSelectedPosition(var1);
         return;
      default:
         throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
      }
   }

   public void setShowHideAnimationEnabled(boolean var1) {
   }

   public void setSplitBackgroundDrawable(Drawable var1) {
   }

   public void setStackedBackgroundDrawable(Drawable var1) {
   }

   public void setSubtitle(int var1) {
      DecorToolbar var3 = this.mDecorToolbar;
      CharSequence var2;
      if(var1 != 0) {
         var2 = this.mDecorToolbar.getContext().getText(var1);
      } else {
         var2 = null;
      }

      var3.setSubtitle(var2);
   }

   public void setSubtitle(CharSequence var1) {
      this.mDecorToolbar.setSubtitle(var1);
   }

   public void setTitle(int var1) {
      DecorToolbar var3 = this.mDecorToolbar;
      CharSequence var2;
      if(var1 != 0) {
         var2 = this.mDecorToolbar.getContext().getText(var1);
      } else {
         var2 = null;
      }

      var3.setTitle(var2);
   }

   public void setTitle(CharSequence var1) {
      this.mDecorToolbar.setTitle(var1);
   }

   public void setWindowTitle(CharSequence var1) {
      this.mDecorToolbar.setWindowTitle(var1);
   }

   public void show() {
      this.mDecorToolbar.setVisibility(0);
   }

   private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      private boolean mClosingActionMenu;

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         if(!this.mClosingActionMenu) {
            this.mClosingActionMenu = true;
            ToolbarActionBar.this.mDecorToolbar.dismissPopupMenus();
            if(ToolbarActionBar.this.mWindowCallback != null) {
               ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, var1);
            }

            this.mClosingActionMenu = false;
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         boolean var2;
         if(ToolbarActionBar.this.mWindowCallback != null) {
            ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, var1);
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   private final class MenuBuilderCallback implements MenuBuilder.Callback {
      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         return false;
      }

      public void onMenuModeChange(MenuBuilder var1) {
         if(ToolbarActionBar.this.mWindowCallback != null) {
            if(ToolbarActionBar.this.mDecorToolbar.isOverflowMenuShowing()) {
               ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, var1);
            } else if(ToolbarActionBar.this.mWindowCallback.onPreparePanel(0, (View)null, var1)) {
               ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, var1);
            }
         }

      }
   }

   private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         if(ToolbarActionBar.this.mWindowCallback != null) {
            ToolbarActionBar.this.mWindowCallback.onPanelClosed(0, var1);
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         if(var1 == null && ToolbarActionBar.this.mWindowCallback != null) {
            ToolbarActionBar.this.mWindowCallback.onMenuOpened(0, var1);
         }

         return true;
      }
   }

   private class ToolbarCallbackWrapper extends WindowCallbackWrapper {
      public ToolbarCallbackWrapper(Callback var2) {
         super(var2);
      }

      public View onCreatePanelView(int var1) {
         View var3;
         switch(var1) {
         case 0:
            Menu var2 = ToolbarActionBar.this.mDecorToolbar.getMenu();
            if(this.onPreparePanel(var1, (View)null, var2) && this.onMenuOpened(var1, var2)) {
               var3 = ToolbarActionBar.this.getListMenuView(var2);
               break;
            }
         default:
            var3 = super.onCreatePanelView(var1);
         }

         return var3;
      }

      public boolean onPreparePanel(int var1, View var2, Menu var3) {
         boolean var4 = super.onPreparePanel(var1, var2, var3);
         if(var4 && !ToolbarActionBar.this.mToolbarMenuPrepared) {
            ToolbarActionBar.this.mDecorToolbar.setMenuPrepared();
            ToolbarActionBar.this.mToolbarMenuPrepared = true;
         }

         return var4;
      }
   }
}
