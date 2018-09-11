package android.support.v7.view.menu;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.KeyEvent.DispatcherState;
import android.view.WindowManager.LayoutParams;

class MenuDialogHelper implements OnKeyListener, OnClickListener, OnDismissListener, MenuPresenter.Callback {
   private AlertDialog mDialog;
   private MenuBuilder mMenu;
   ListMenuPresenter mPresenter;
   private MenuPresenter.Callback mPresenterCallback;

   public MenuDialogHelper(MenuBuilder var1) {
      this.mMenu = var1;
   }

   public void dismiss() {
      if(this.mDialog != null) {
         this.mDialog.dismiss();
      }

   }

   public void onClick(DialogInterface var1, int var2) {
      this.mMenu.performItemAction((MenuItemImpl)this.mPresenter.getAdapter().getItem(var2), 0);
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      if(var2 || var1 == this.mMenu) {
         this.dismiss();
      }

      if(this.mPresenterCallback != null) {
         this.mPresenterCallback.onCloseMenu(var1, var2);
      }

   }

   public void onDismiss(DialogInterface var1) {
      this.mPresenter.onCloseMenu(this.mMenu, true);
   }

   public boolean onKey(DialogInterface var1, int var2, KeyEvent var3) {
      boolean var4 = true;
      if(var2 == 82 || var2 == 4) {
         if(var3.getAction() == 0 && var3.getRepeatCount() == 0) {
            Window var6 = this.mDialog.getWindow();
            if(var6 != null) {
               View var7 = var6.getDecorView();
               if(var7 != null) {
                  DispatcherState var8 = var7.getKeyDispatcherState();
                  if(var8 != null) {
                     var8.startTracking(var3, this);
                     return var4;
                  }
               }
            }
         } else if(var3.getAction() == 1 && !var3.isCanceled()) {
            Window var5 = this.mDialog.getWindow();
            if(var5 != null) {
               View var9 = var5.getDecorView();
               if(var9 != null) {
                  DispatcherState var10 = var9.getKeyDispatcherState();
                  if(var10 != null && var10.isTracking(var3)) {
                     this.mMenu.close(true);
                     var1.dismiss();
                     return var4;
                  }
               }
            }
         }
      }

      var4 = this.mMenu.performShortcut(var2, var3, 0);
      return var4;
   }

   public boolean onOpenSubMenu(MenuBuilder var1) {
      boolean var2;
      if(this.mPresenterCallback != null) {
         var2 = this.mPresenterCallback.onOpenSubMenu(var1);
      } else {
         var2 = false;
      }

      return var2;
   }

   public void setPresenterCallback(MenuPresenter.Callback var1) {
      this.mPresenterCallback = var1;
   }

   public void show(IBinder var1) {
      MenuBuilder var3 = this.mMenu;
      AlertDialog.Builder var4 = new AlertDialog.Builder(var3.getContext());
      this.mPresenter = new ListMenuPresenter(var4.getContext(), R.layout.abc_list_menu_item_layout);
      this.mPresenter.setCallback(this);
      this.mMenu.addMenuPresenter(this.mPresenter);
      var4.setAdapter(this.mPresenter.getAdapter(), this);
      View var2 = var3.getHeaderView();
      if(var2 != null) {
         var4.setCustomTitle(var2);
      } else {
         var4.setIcon(var3.getHeaderIcon()).setTitle(var3.getHeaderTitle());
      }

      var4.setOnKeyListener(this);
      this.mDialog = var4.create();
      this.mDialog.setOnDismissListener(this);
      LayoutParams var5 = this.mDialog.getWindow().getAttributes();
      var5.type = 1003;
      if(var1 != null) {
         var5.token = var1;
      }

      var5.flags |= 131072;
      this.mDialog.show();
   }
}
