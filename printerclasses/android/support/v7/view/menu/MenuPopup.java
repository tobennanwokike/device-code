package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

abstract class MenuPopup implements ShowableListMenu, MenuPresenter, OnItemClickListener {
   private Rect mEpicenterBounds;

   protected static int measureIndividualMenuWidth(ListAdapter var0, ViewGroup var1, Context var2, int var3) {
      int var4 = 0;
      Object var13 = null;
      int var8 = 0;
      int var11 = MeasureSpec.makeMeasureSpec(0, 0);
      int var10 = MeasureSpec.makeMeasureSpec(0, 0);
      int var9 = var0.getCount();
      int var6 = 0;
      Object var12 = var1;
      View var14 = (View)var13;

      while(true) {
         if(var6 >= var9) {
            var3 = var4;
            break;
         }

         int var7 = var0.getItemViewType(var6);
         int var5 = var8;
         if(var7 != var8) {
            var5 = var7;
            var14 = null;
         }

         var13 = var12;
         if(var12 == null) {
            var13 = new FrameLayout(var2);
         }

         var14 = var0.getView(var6, var14, (ViewGroup)var13);
         var14.measure(var11, var10);
         var8 = var14.getMeasuredWidth();
         if(var8 >= var3) {
            break;
         }

         var7 = var4;
         if(var8 > var4) {
            var7 = var8;
         }

         ++var6;
         var8 = var5;
         var4 = var7;
         var12 = var13;
      }

      return var3;
   }

   protected static boolean shouldPreserveIconSpacing(MenuBuilder var0) {
      boolean var4 = false;
      int var2 = var0.size();
      int var1 = 0;

      boolean var3;
      while(true) {
         var3 = var4;
         if(var1 >= var2) {
            break;
         }

         MenuItem var5 = var0.getItem(var1);
         if(var5.isVisible() && var5.getIcon() != null) {
            var3 = true;
            break;
         }

         ++var1;
      }

      return var3;
   }

   protected static MenuAdapter toMenuAdapter(ListAdapter var0) {
      MenuAdapter var1;
      if(var0 instanceof HeaderViewListAdapter) {
         var1 = (MenuAdapter)((HeaderViewListAdapter)var0).getWrappedAdapter();
      } else {
         var1 = (MenuAdapter)var0;
      }

      return var1;
   }

   public abstract void addMenu(MenuBuilder var1);

   protected boolean closeMenuOnSubMenuOpened() {
      return true;
   }

   public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public Rect getEpicenterBounds() {
      return this.mEpicenterBounds;
   }

   public int getId() {
      return 0;
   }

   public MenuView getMenuView(ViewGroup var1) {
      throw new UnsupportedOperationException("MenuPopups manage their own views");
   }

   public void initForMenu(@NonNull Context var1, @Nullable MenuBuilder var2) {
   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      ListAdapter var7 = (ListAdapter)var1.getAdapter();
      MenuBuilder var6 = toMenuAdapter(var7).mAdapterMenu;
      MenuItem var8 = (MenuItem)var7.getItem(var3);
      byte var9;
      if(this.closeMenuOnSubMenuOpened()) {
         var9 = 0;
      } else {
         var9 = 4;
      }

      var6.performItemAction(var8, this, var9);
   }

   public abstract void setAnchorView(View var1);

   public void setEpicenterBounds(Rect var1) {
      this.mEpicenterBounds = var1;
   }

   public abstract void setForceShowIcon(boolean var1);

   public abstract void setGravity(int var1);

   public abstract void setHorizontalOffset(int var1);

   public abstract void setOnDismissListener(OnDismissListener var1);

   public abstract void setShowTitle(boolean var1);

   public abstract void setVerticalOffset(int var1);
}
