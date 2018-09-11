package android.support.v7.view.menu;

import android.content.Context;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v4.util.ArrayMap;
import android.support.v7.view.menu.BaseWrapper;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.Iterator;
import java.util.Map;

abstract class BaseMenuWrapper extends BaseWrapper {
   final Context mContext;
   private Map mMenuItems;
   private Map mSubMenus;

   BaseMenuWrapper(Context var1, Object var2) {
      super(var2);
      this.mContext = var1;
   }

   final MenuItem getMenuItemWrapper(MenuItem var1) {
      if(var1 instanceof SupportMenuItem) {
         SupportMenuItem var3 = (SupportMenuItem)var1;
         if(this.mMenuItems == null) {
            this.mMenuItems = new ArrayMap();
         }

         MenuItem var2 = (MenuItem)this.mMenuItems.get(var1);
         var1 = var2;
         if(var2 == null) {
            var1 = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, var3);
            this.mMenuItems.put(var3, var1);
         }
      }

      return var1;
   }

   final SubMenu getSubMenuWrapper(SubMenu var1) {
      if(var1 instanceof SupportSubMenu) {
         SupportSubMenu var3 = (SupportSubMenu)var1;
         if(this.mSubMenus == null) {
            this.mSubMenus = new ArrayMap();
         }

         SubMenu var2 = (SubMenu)this.mSubMenus.get(var3);
         var1 = var2;
         if(var2 == null) {
            var1 = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, var3);
            this.mSubMenus.put(var3, var1);
         }
      }

      return var1;
   }

   final void internalClear() {
      if(this.mMenuItems != null) {
         this.mMenuItems.clear();
      }

      if(this.mSubMenus != null) {
         this.mSubMenus.clear();
      }

   }

   final void internalRemoveGroup(int var1) {
      if(this.mMenuItems != null) {
         Iterator var2 = this.mMenuItems.keySet().iterator();

         while(var2.hasNext()) {
            if(var1 == ((MenuItem)var2.next()).getGroupId()) {
               var2.remove();
            }
         }
      }

   }

   final void internalRemoveItem(int var1) {
      if(this.mMenuItems != null) {
         Iterator var2 = this.mMenuItems.keySet().iterator();

         while(var2.hasNext()) {
            if(var1 == ((MenuItem)var2.next()).getItemId()) {
               var2.remove();
               break;
            }
         }
      }

   }
}
