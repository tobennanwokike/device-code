package android.support.v7.view.menu;

import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class MenuAdapter extends BaseAdapter {
   static final int ITEM_LAYOUT;
   MenuBuilder mAdapterMenu;
   private int mExpandedIndex = -1;
   private boolean mForceShowIcon;
   private final LayoutInflater mInflater;
   private final boolean mOverflowOnly;

   static {
      ITEM_LAYOUT = R.layout.abc_popup_menu_item_layout;
   }

   public MenuAdapter(MenuBuilder var1, LayoutInflater var2, boolean var3) {
      this.mOverflowOnly = var3;
      this.mInflater = var2;
      this.mAdapterMenu = var1;
      this.findExpandedIndex();
   }

   void findExpandedIndex() {
      MenuItemImpl var3 = this.mAdapterMenu.getExpandedItem();
      if(var3 != null) {
         ArrayList var4 = this.mAdapterMenu.getNonActionItems();
         int var2 = var4.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            if((MenuItemImpl)var4.get(var1) == var3) {
               this.mExpandedIndex = var1;
               return;
            }
         }
      }

      this.mExpandedIndex = -1;
   }

   public MenuBuilder getAdapterMenu() {
      return this.mAdapterMenu;
   }

   public int getCount() {
      ArrayList var2;
      if(this.mOverflowOnly) {
         var2 = this.mAdapterMenu.getNonActionItems();
      } else {
         var2 = this.mAdapterMenu.getVisibleItems();
      }

      int var1;
      if(this.mExpandedIndex < 0) {
         var1 = var2.size();
      } else {
         var1 = var2.size() - 1;
      }

      return var1;
   }

   public boolean getForceShowIcon() {
      return this.mForceShowIcon;
   }

   public MenuItemImpl getItem(int var1) {
      ArrayList var3;
      if(this.mOverflowOnly) {
         var3 = this.mAdapterMenu.getNonActionItems();
      } else {
         var3 = this.mAdapterMenu.getVisibleItems();
      }

      int var2 = var1;
      if(this.mExpandedIndex >= 0) {
         var2 = var1;
         if(var1 >= this.mExpandedIndex) {
            var2 = var1 + 1;
         }
      }

      return (MenuItemImpl)var3.get(var2);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      View var4 = var2;
      if(var2 == null) {
         var4 = this.mInflater.inflate(ITEM_LAYOUT, var3, false);
      }

      MenuView.ItemView var5 = (MenuView.ItemView)var4;
      if(this.mForceShowIcon) {
         ((ListMenuItemView)var4).setForceShowIcon(true);
      }

      var5.initialize(this.getItem(var1), 0);
      return var4;
   }

   public void notifyDataSetChanged() {
      this.findExpandedIndex();
      super.notifyDataSetChanged();
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
   }
}
