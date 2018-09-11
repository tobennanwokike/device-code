package android.support.v7.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyCharacterMap.KeyData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class MenuBuilder implements SupportMenu {
   private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
   private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
   private static final String PRESENTER_KEY = "android:menu:presenters";
   private static final String TAG = "MenuBuilder";
   private static final int[] sCategoryToOrder = new int[]{1, 4, 5, 3, 2, 0};
   private ArrayList mActionItems;
   private MenuBuilder.Callback mCallback;
   private final Context mContext;
   private ContextMenuInfo mCurrentMenuInfo;
   private int mDefaultShowAsAction = 0;
   private MenuItemImpl mExpandedItem;
   private SparseArray mFrozenViewStates;
   Drawable mHeaderIcon;
   CharSequence mHeaderTitle;
   View mHeaderView;
   private boolean mIsActionItemsStale;
   private boolean mIsClosing = false;
   private boolean mIsVisibleItemsStale;
   private ArrayList mItems;
   private boolean mItemsChangedWhileDispatchPrevented = false;
   private ArrayList mNonActionItems;
   private boolean mOptionalIconsVisible = false;
   private boolean mOverrideVisibleItems;
   private CopyOnWriteArrayList mPresenters = new CopyOnWriteArrayList();
   private boolean mPreventDispatchingItemsChanged = false;
   private boolean mQwertyMode;
   private final Resources mResources;
   private boolean mShortcutsVisible;
   private ArrayList mTempShortcutItemList = new ArrayList();
   private ArrayList mVisibleItems;

   public MenuBuilder(Context var1) {
      this.mContext = var1;
      this.mResources = var1.getResources();
      this.mItems = new ArrayList();
      this.mVisibleItems = new ArrayList();
      this.mIsVisibleItemsStale = true;
      this.mActionItems = new ArrayList();
      this.mNonActionItems = new ArrayList();
      this.mIsActionItemsStale = true;
      this.setShortcutsVisibleInner(true);
   }

   private MenuItemImpl createNewMenuItem(int var1, int var2, int var3, int var4, CharSequence var5, int var6) {
      return new MenuItemImpl(this, var1, var2, var3, var4, var5, var6);
   }

   private void dispatchPresenterUpdate(boolean var1) {
      if(!this.mPresenters.isEmpty()) {
         this.stopDispatchingItemsChanged();
         Iterator var2 = this.mPresenters.iterator();

         while(var2.hasNext()) {
            WeakReference var4 = (WeakReference)var2.next();
            MenuPresenter var3 = (MenuPresenter)var4.get();
            if(var3 == null) {
               this.mPresenters.remove(var4);
            } else {
               var3.updateMenuView(var1);
            }
         }

         this.startDispatchingItemsChanged();
      }

   }

   private void dispatchRestoreInstanceState(Bundle var1) {
      SparseArray var4 = var1.getSparseParcelableArray("android:menu:presenters");
      if(var4 != null && !this.mPresenters.isEmpty()) {
         Iterator var3 = this.mPresenters.iterator();

         while(var3.hasNext()) {
            WeakReference var5 = (WeakReference)var3.next();
            MenuPresenter var7 = (MenuPresenter)var5.get();
            if(var7 == null) {
               this.mPresenters.remove(var5);
            } else {
               int var2 = var7.getId();
               if(var2 > 0) {
                  Parcelable var6 = (Parcelable)var4.get(var2);
                  if(var6 != null) {
                     var7.onRestoreInstanceState(var6);
                  }
               }
            }
         }
      }

   }

   private void dispatchSaveInstanceState(Bundle var1) {
      if(!this.mPresenters.isEmpty()) {
         SparseArray var4 = new SparseArray();
         Iterator var3 = this.mPresenters.iterator();

         while(var3.hasNext()) {
            WeakReference var5 = (WeakReference)var3.next();
            MenuPresenter var6 = (MenuPresenter)var5.get();
            if(var6 == null) {
               this.mPresenters.remove(var5);
            } else {
               int var2 = var6.getId();
               if(var2 > 0) {
                  Parcelable var7 = var6.onSaveInstanceState();
                  if(var7 != null) {
                     var4.put(var2, var7);
                  }
               }
            }
         }

         var1.putSparseParcelableArray("android:menu:presenters", var4);
      }

   }

   private boolean dispatchSubMenuSelected(SubMenuBuilder var1, MenuPresenter var2) {
      boolean var4;
      if(this.mPresenters.isEmpty()) {
         var4 = false;
      } else {
         boolean var3 = false;
         if(var2 != null) {
            var3 = var2.onSubMenuSelected(var1);
         }

         Iterator var5 = this.mPresenters.iterator();

         while(true) {
            var4 = var3;
            if(!var5.hasNext()) {
               break;
            }

            WeakReference var6 = (WeakReference)var5.next();
            var2 = (MenuPresenter)var6.get();
            if(var2 == null) {
               this.mPresenters.remove(var6);
            } else if(!var3) {
               var3 = var2.onSubMenuSelected(var1);
            }
         }
      }

      return var4;
   }

   private static int findInsertIndex(ArrayList var0, int var1) {
      int var2 = var0.size() - 1;

      while(true) {
         if(var2 < 0) {
            var1 = 0;
            break;
         }

         if(((MenuItemImpl)var0.get(var2)).getOrdering() <= var1) {
            var1 = var2 + 1;
            break;
         }

         --var2;
      }

      return var1;
   }

   private static int getOrdering(int var0) {
      int var1 = (-65536 & var0) >> 16;
      if(var1 >= 0 && var1 < sCategoryToOrder.length) {
         return sCategoryToOrder[var1] << 16 | '\uffff' & var0;
      } else {
         throw new IllegalArgumentException("order does not contain a valid category.");
      }
   }

   private void removeItemAtInt(int var1, boolean var2) {
      if(var1 >= 0 && var1 < this.mItems.size()) {
         this.mItems.remove(var1);
         if(var2) {
            this.onItemsChanged(true);
         }
      }

   }

   private void setHeaderInternal(int var1, CharSequence var2, int var3, Drawable var4, View var5) {
      Resources var6 = this.getResources();
      if(var5 != null) {
         this.mHeaderView = var5;
         this.mHeaderTitle = null;
         this.mHeaderIcon = null;
      } else {
         if(var1 > 0) {
            this.mHeaderTitle = var6.getText(var1);
         } else if(var2 != null) {
            this.mHeaderTitle = var2;
         }

         if(var3 > 0) {
            this.mHeaderIcon = ContextCompat.getDrawable(this.getContext(), var3);
         } else if(var4 != null) {
            this.mHeaderIcon = var4;
         }

         this.mHeaderView = null;
      }

      this.onItemsChanged(false);
   }

   private void setShortcutsVisibleInner(boolean var1) {
      boolean var2 = true;
      if(var1 && this.mResources.getConfiguration().keyboard != 1 && this.mResources.getBoolean(R.bool.abc_config_showMenuShortcutsWhenKeyboardPresent)) {
         var1 = var2;
      } else {
         var1 = false;
      }

      this.mShortcutsVisible = var1;
   }

   public MenuItem add(int var1) {
      return this.addInternal(0, 0, 0, this.mResources.getString(var1));
   }

   public MenuItem add(int var1, int var2, int var3, int var4) {
      return this.addInternal(var1, var2, var3, this.mResources.getString(var4));
   }

   public MenuItem add(int var1, int var2, int var3, CharSequence var4) {
      return this.addInternal(var1, var2, var3, var4);
   }

   public MenuItem add(CharSequence var1) {
      return this.addInternal(0, 0, 0, var1);
   }

   public int addIntentOptions(int var1, int var2, int var3, ComponentName var4, Intent[] var5, Intent var6, int var7, MenuItem[] var8) {
      PackageManager var10 = this.mContext.getPackageManager();
      List var12 = var10.queryIntentActivityOptions(var4, var5, var6, 0);
      int var9;
      if(var12 != null) {
         var9 = var12.size();
      } else {
         var9 = 0;
      }

      if((var7 & 1) == 0) {
         this.removeGroup(var1);
      }

      for(var7 = 0; var7 < var9; ++var7) {
         ResolveInfo var11 = (ResolveInfo)var12.get(var7);
         Intent var13;
         if(var11.specificIndex < 0) {
            var13 = var6;
         } else {
            var13 = var5[var11.specificIndex];
         }

         var13 = new Intent(var13);
         var13.setComponent(new ComponentName(var11.activityInfo.applicationInfo.packageName, var11.activityInfo.name));
         MenuItem var14 = this.add(var1, var2, var3, var11.loadLabel(var10)).setIcon(var11.loadIcon(var10)).setIntent(var13);
         if(var8 != null && var11.specificIndex >= 0) {
            var8[var11.specificIndex] = var14;
         }
      }

      return var9;
   }

   protected MenuItem addInternal(int var1, int var2, int var3, CharSequence var4) {
      int var5 = getOrdering(var3);
      MenuItemImpl var6 = this.createNewMenuItem(var1, var2, var3, var5, var4, this.mDefaultShowAsAction);
      if(this.mCurrentMenuInfo != null) {
         var6.setMenuInfo(this.mCurrentMenuInfo);
      }

      this.mItems.add(findInsertIndex(this.mItems, var5), var6);
      this.onItemsChanged(true);
      return var6;
   }

   public void addMenuPresenter(MenuPresenter var1) {
      this.addMenuPresenter(var1, this.mContext);
   }

   public void addMenuPresenter(MenuPresenter var1, Context var2) {
      this.mPresenters.add(new WeakReference(var1));
      var1.initForMenu(var2, this);
      this.mIsActionItemsStale = true;
   }

   public SubMenu addSubMenu(int var1) {
      return this.addSubMenu(0, 0, 0, this.mResources.getString(var1));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, int var4) {
      return this.addSubMenu(var1, var2, var3, this.mResources.getString(var4));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4) {
      MenuItemImpl var6 = (MenuItemImpl)this.addInternal(var1, var2, var3, var4);
      SubMenuBuilder var5 = new SubMenuBuilder(this.mContext, this, var6);
      var6.setSubMenu(var5);
      return var5;
   }

   public SubMenu addSubMenu(CharSequence var1) {
      return this.addSubMenu(0, 0, 0, var1);
   }

   public void changeMenuMode() {
      if(this.mCallback != null) {
         this.mCallback.onMenuModeChange(this);
      }

   }

   public void clear() {
      if(this.mExpandedItem != null) {
         this.collapseItemActionView(this.mExpandedItem);
      }

      this.mItems.clear();
      this.onItemsChanged(true);
   }

   public void clearAll() {
      this.mPreventDispatchingItemsChanged = true;
      this.clear();
      this.clearHeader();
      this.mPreventDispatchingItemsChanged = false;
      this.mItemsChangedWhileDispatchPrevented = false;
      this.onItemsChanged(true);
   }

   public void clearHeader() {
      this.mHeaderIcon = null;
      this.mHeaderTitle = null;
      this.mHeaderView = null;
      this.onItemsChanged(false);
   }

   public void close() {
      this.close(true);
   }

   public final void close(boolean var1) {
      if(!this.mIsClosing) {
         this.mIsClosing = true;
         Iterator var2 = this.mPresenters.iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            MenuPresenter var4 = (MenuPresenter)var3.get();
            if(var4 == null) {
               this.mPresenters.remove(var3);
            } else {
               var4.onCloseMenu(this, var1);
            }
         }

         this.mIsClosing = false;
      }

   }

   public boolean collapseItemActionView(MenuItemImpl var1) {
      boolean var3;
      if(!this.mPresenters.isEmpty() && this.mExpandedItem == var1) {
         var3 = false;
         this.stopDispatchingItemsChanged();
         Iterator var5 = this.mPresenters.iterator();

         boolean var2;
         while(true) {
            var2 = var3;
            if(!var5.hasNext()) {
               break;
            }

            WeakReference var6 = (WeakReference)var5.next();
            MenuPresenter var4 = (MenuPresenter)var6.get();
            if(var4 == null) {
               this.mPresenters.remove(var6);
            } else {
               var2 = var4.collapseItemActionView(this, var1);
               var3 = var2;
               if(var2) {
                  break;
               }
            }
         }

         this.startDispatchingItemsChanged();
         var3 = var2;
         if(var2) {
            this.mExpandedItem = null;
            var3 = var2;
         }
      } else {
         var3 = false;
      }

      return var3;
   }

   boolean dispatchMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      boolean var3;
      if(this.mCallback != null && this.mCallback.onMenuItemSelected(var1, var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean expandItemActionView(MenuItemImpl var1) {
      boolean var3;
      if(this.mPresenters.isEmpty()) {
         var3 = false;
      } else {
         var3 = false;
         this.stopDispatchingItemsChanged();
         Iterator var4 = this.mPresenters.iterator();

         boolean var2;
         while(true) {
            var2 = var3;
            if(!var4.hasNext()) {
               break;
            }

            WeakReference var6 = (WeakReference)var4.next();
            MenuPresenter var5 = (MenuPresenter)var6.get();
            if(var5 == null) {
               this.mPresenters.remove(var6);
            } else {
               var2 = var5.expandItemActionView(this, var1);
               var3 = var2;
               if(var2) {
                  break;
               }
            }
         }

         this.startDispatchingItemsChanged();
         var3 = var2;
         if(var2) {
            this.mExpandedItem = var1;
            var3 = var2;
         }
      }

      return var3;
   }

   public int findGroupIndex(int var1) {
      return this.findGroupIndex(var1, 0);
   }

   public int findGroupIndex(int var1, int var2) {
      int var4 = this.size();
      int var3 = var2;
      if(var2 < 0) {
         var3 = 0;
      }

      var2 = var3;

      while(true) {
         if(var2 >= var4) {
            var2 = -1;
            break;
         }

         if(((MenuItemImpl)this.mItems.get(var2)).getGroupId() == var1) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   public MenuItem findItem(int var1) {
      int var3 = this.size();
      int var2 = 0;

      Object var4;
      while(true) {
         if(var2 >= var3) {
            var4 = null;
            break;
         }

         var4 = (MenuItemImpl)this.mItems.get(var2);
         if(((MenuItemImpl)var4).getItemId() == var1) {
            break;
         }

         if(((MenuItemImpl)var4).hasSubMenu()) {
            var4 = ((MenuItemImpl)var4).getSubMenu().findItem(var1);
            if(var4 != null) {
               break;
            }
         }

         ++var2;
      }

      return (MenuItem)var4;
   }

   public int findItemIndex(int var1) {
      int var3 = this.size();
      int var2 = 0;

      while(true) {
         if(var2 >= var3) {
            var2 = -1;
            break;
         }

         if(((MenuItemImpl)this.mItems.get(var2)).getItemId() == var1) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   MenuItemImpl findItemWithShortcutForKey(int var1, KeyEvent var2) {
      Object var8 = null;
      ArrayList var9 = this.mTempShortcutItemList;
      var9.clear();
      this.findItemsWithShortcutForKey(var9, var1, var2);
      MenuItemImpl var11;
      if(var9.isEmpty()) {
         var11 = (MenuItemImpl)var8;
      } else {
         int var6 = var2.getMetaState();
         KeyData var10 = new KeyData();
         var2.getKeyData(var10);
         int var5 = var9.size();
         if(var5 == 1) {
            var11 = (MenuItemImpl)var9.get(0);
         } else {
            boolean var7 = this.isQwertyMode();
            int var3 = 0;

            while(true) {
               var11 = (MenuItemImpl)var8;
               if(var3 >= var5) {
                  break;
               }

               var11 = (MenuItemImpl)var9.get(var3);
               char var4;
               if(var7) {
                  var4 = var11.getAlphabeticShortcut();
               } else {
                  var4 = var11.getNumericShortcut();
               }

               if(var4 == var10.meta[0] && (var6 & 2) == 0 || var4 == var10.meta[2] && (var6 & 2) != 0 || var7 && var4 == 8 && var1 == 67) {
                  break;
               }

               ++var3;
            }
         }
      }

      return var11;
   }

   void findItemsWithShortcutForKey(List var1, int var2, KeyEvent var3) {
      boolean var8 = this.isQwertyMode();
      int var7 = var3.getMetaState();
      KeyData var10 = new KeyData();
      if(var3.getKeyData(var10) || var2 == 67) {
         int var6 = this.mItems.size();

         for(int var4 = 0; var4 < var6; ++var4) {
            MenuItemImpl var9 = (MenuItemImpl)this.mItems.get(var4);
            if(var9.hasSubMenu()) {
               ((MenuBuilder)var9.getSubMenu()).findItemsWithShortcutForKey(var1, var2, var3);
            }

            char var5;
            if(var8) {
               var5 = var9.getAlphabeticShortcut();
            } else {
               var5 = var9.getNumericShortcut();
            }

            if((var7 & 5) == 0 && var5 != 0 && (var5 == var10.meta[0] || var5 == var10.meta[2] || var8 && var5 == 8 && var2 == 67) && var9.isEnabled()) {
               var1.add(var9);
            }
         }
      }

   }

   public void flagActionItems() {
      ArrayList var3 = this.getVisibleItems();
      if(this.mIsActionItemsStale) {
         boolean var1 = false;
         Iterator var4 = this.mPresenters.iterator();

         while(var4.hasNext()) {
            WeakReference var6 = (WeakReference)var4.next();
            MenuPresenter var5 = (MenuPresenter)var6.get();
            if(var5 == null) {
               this.mPresenters.remove(var6);
            } else {
               var1 |= var5.flagActionItems();
            }
         }

         if(var1) {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            int var2 = var3.size();

            for(int var7 = 0; var7 < var2; ++var7) {
               MenuItemImpl var8 = (MenuItemImpl)var3.get(var7);
               if(var8.isActionButton()) {
                  this.mActionItems.add(var8);
               } else {
                  this.mNonActionItems.add(var8);
               }
            }
         } else {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            this.mNonActionItems.addAll(this.getVisibleItems());
         }

         this.mIsActionItemsStale = false;
      }

   }

   public ArrayList getActionItems() {
      this.flagActionItems();
      return this.mActionItems;
   }

   protected String getActionViewStatesKey() {
      return "android:menu:actionviewstates";
   }

   public Context getContext() {
      return this.mContext;
   }

   public MenuItemImpl getExpandedItem() {
      return this.mExpandedItem;
   }

   public Drawable getHeaderIcon() {
      return this.mHeaderIcon;
   }

   public CharSequence getHeaderTitle() {
      return this.mHeaderTitle;
   }

   public View getHeaderView() {
      return this.mHeaderView;
   }

   public MenuItem getItem(int var1) {
      return (MenuItem)this.mItems.get(var1);
   }

   public ArrayList getNonActionItems() {
      this.flagActionItems();
      return this.mNonActionItems;
   }

   boolean getOptionalIconsVisible() {
      return this.mOptionalIconsVisible;
   }

   Resources getResources() {
      return this.mResources;
   }

   public MenuBuilder getRootMenu() {
      return this;
   }

   @NonNull
   public ArrayList getVisibleItems() {
      ArrayList var3;
      if(!this.mIsVisibleItemsStale) {
         var3 = this.mVisibleItems;
      } else {
         this.mVisibleItems.clear();
         int var2 = this.mItems.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            MenuItemImpl var4 = (MenuItemImpl)this.mItems.get(var1);
            if(var4.isVisible()) {
               this.mVisibleItems.add(var4);
            }
         }

         this.mIsVisibleItemsStale = false;
         this.mIsActionItemsStale = true;
         var3 = this.mVisibleItems;
      }

      return var3;
   }

   public boolean hasVisibleItems() {
      boolean var4 = true;
      boolean var3;
      if(this.mOverrideVisibleItems) {
         var3 = var4;
      } else {
         int var2 = this.size();
         int var1 = 0;

         while(true) {
            if(var1 >= var2) {
               var3 = false;
               break;
            }

            var3 = var4;
            if(((MenuItemImpl)this.mItems.get(var1)).isVisible()) {
               break;
            }

            ++var1;
         }
      }

      return var3;
   }

   boolean isQwertyMode() {
      return this.mQwertyMode;
   }

   public boolean isShortcutKey(int var1, KeyEvent var2) {
      boolean var3;
      if(this.findItemWithShortcutForKey(var1, var2) != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isShortcutsVisible() {
      return this.mShortcutsVisible;
   }

   void onItemActionRequestChanged(MenuItemImpl var1) {
      this.mIsActionItemsStale = true;
      this.onItemsChanged(true);
   }

   void onItemVisibleChanged(MenuItemImpl var1) {
      this.mIsVisibleItemsStale = true;
      this.onItemsChanged(true);
   }

   public void onItemsChanged(boolean var1) {
      if(!this.mPreventDispatchingItemsChanged) {
         if(var1) {
            this.mIsVisibleItemsStale = true;
            this.mIsActionItemsStale = true;
         }

         this.dispatchPresenterUpdate(var1);
      } else {
         this.mItemsChangedWhileDispatchPrevented = true;
      }

   }

   public boolean performIdentifierAction(int var1, int var2) {
      return this.performItemAction(this.findItem(var1), var2);
   }

   public boolean performItemAction(MenuItem var1, int var2) {
      return this.performItemAction(var1, (MenuPresenter)null, var2);
   }

   public boolean performItemAction(MenuItem var1, MenuPresenter var2, int var3) {
      MenuItemImpl var7 = (MenuItemImpl)var1;
      boolean var5;
      if(var7 != null && var7.isEnabled()) {
         boolean var6 = var7.invoke();
         ActionProvider var8 = var7.getSupportActionProvider();
         boolean var4;
         if(var8 != null && var8.hasSubMenu()) {
            var4 = true;
         } else {
            var4 = false;
         }

         if(var7.hasCollapsibleActionView()) {
            var6 |= var7.expandActionView();
            var5 = var6;
            if(var6) {
               this.close(true);
               var5 = var6;
            }
         } else if(!var7.hasSubMenu() && !var4) {
            var5 = var6;
            if((var3 & 1) == 0) {
               this.close(true);
               var5 = var6;
            }
         } else {
            if((var3 & 4) == 0) {
               this.close(false);
            }

            if(!var7.hasSubMenu()) {
               var7.setSubMenu(new SubMenuBuilder(this.getContext(), this, var7));
            }

            SubMenuBuilder var9 = (SubMenuBuilder)var7.getSubMenu();
            if(var4) {
               var8.onPrepareSubMenu(var9);
            }

            var6 |= this.dispatchSubMenuSelected(var9, var2);
            var5 = var6;
            if(!var6) {
               this.close(true);
               var5 = var6;
            }
         }
      } else {
         var5 = false;
      }

      return var5;
   }

   public boolean performShortcut(int var1, KeyEvent var2, int var3) {
      MenuItemImpl var5 = this.findItemWithShortcutForKey(var1, var2);
      boolean var4 = false;
      if(var5 != null) {
         var4 = this.performItemAction(var5, var3);
      }

      if((var3 & 2) != 0) {
         this.close(true);
      }

      return var4;
   }

   public void removeGroup(int var1) {
      int var3 = this.findGroupIndex(var1);
      if(var3 >= 0) {
         int var4 = this.mItems.size();

         for(int var2 = 0; var2 < var4 - var3 && ((MenuItemImpl)this.mItems.get(var3)).getGroupId() == var1; ++var2) {
            this.removeItemAtInt(var3, false);
         }

         this.onItemsChanged(true);
      }

   }

   public void removeItem(int var1) {
      this.removeItemAtInt(this.findItemIndex(var1), true);
   }

   public void removeItemAt(int var1) {
      this.removeItemAtInt(var1, true);
   }

   public void removeMenuPresenter(MenuPresenter var1) {
      Iterator var3 = this.mPresenters.iterator();

      while(true) {
         WeakReference var2;
         MenuPresenter var4;
         do {
            if(!var3.hasNext()) {
               return;
            }

            var2 = (WeakReference)var3.next();
            var4 = (MenuPresenter)var2.get();
         } while(var4 != null && var4 != var1);

         this.mPresenters.remove(var2);
      }
   }

   public void restoreActionViewStates(Bundle var1) {
      if(var1 != null) {
         SparseArray var5 = var1.getSparseParcelableArray(this.getActionViewStatesKey());
         int var3 = this.size();

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            MenuItem var6 = this.getItem(var2);
            View var4 = MenuItemCompat.getActionView(var6);
            if(var4 != null && var4.getId() != -1) {
               var4.restoreHierarchyState(var5);
            }

            if(var6.hasSubMenu()) {
               ((SubMenuBuilder)var6.getSubMenu()).restoreActionViewStates(var1);
            }
         }

         var2 = var1.getInt("android:menu:expandedactionview");
         if(var2 > 0) {
            MenuItem var7 = this.findItem(var2);
            if(var7 != null) {
               MenuItemCompat.expandActionView(var7);
            }
         }
      }

   }

   public void restorePresenterStates(Bundle var1) {
      this.dispatchRestoreInstanceState(var1);
   }

   public void saveActionViewStates(Bundle var1) {
      SparseArray var5 = null;
      int var3 = this.size();

      SparseArray var6;
      for(int var2 = 0; var2 < var3; var5 = var6) {
         MenuItem var7 = this.getItem(var2);
         View var8 = MenuItemCompat.getActionView(var7);
         var6 = var5;
         if(var8 != null) {
            var6 = var5;
            if(var8.getId() != -1) {
               SparseArray var4 = var5;
               if(var5 == null) {
                  var4 = new SparseArray();
               }

               var8.saveHierarchyState(var4);
               var6 = var4;
               if(MenuItemCompat.isActionViewExpanded(var7)) {
                  var1.putInt("android:menu:expandedactionview", var7.getItemId());
                  var6 = var4;
               }
            }
         }

         if(var7.hasSubMenu()) {
            ((SubMenuBuilder)var7.getSubMenu()).saveActionViewStates(var1);
         }

         ++var2;
      }

      if(var5 != null) {
         var1.putSparseParcelableArray(this.getActionViewStatesKey(), var5);
      }

   }

   public void savePresenterStates(Bundle var1) {
      this.dispatchSaveInstanceState(var1);
   }

   public void setCallback(MenuBuilder.Callback var1) {
      this.mCallback = var1;
   }

   public void setCurrentMenuInfo(ContextMenuInfo var1) {
      this.mCurrentMenuInfo = var1;
   }

   public MenuBuilder setDefaultShowAsAction(int var1) {
      this.mDefaultShowAsAction = var1;
      return this;
   }

   void setExclusiveItemChecked(MenuItem var1) {
      int var4 = var1.getGroupId();
      int var3 = this.mItems.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         MenuItemImpl var6 = (MenuItemImpl)this.mItems.get(var2);
         if(var6.getGroupId() == var4 && var6.isExclusiveCheckable() && var6.isCheckable()) {
            boolean var5;
            if(var6 == var1) {
               var5 = true;
            } else {
               var5 = false;
            }

            var6.setCheckedInt(var5);
         }
      }

   }

   public void setGroupCheckable(int var1, boolean var2, boolean var3) {
      int var5 = this.mItems.size();

      for(int var4 = 0; var4 < var5; ++var4) {
         MenuItemImpl var6 = (MenuItemImpl)this.mItems.get(var4);
         if(var6.getGroupId() == var1) {
            var6.setExclusiveCheckable(var3);
            var6.setCheckable(var2);
         }
      }

   }

   public void setGroupEnabled(int var1, boolean var2) {
      int var4 = this.mItems.size();

      for(int var3 = 0; var3 < var4; ++var3) {
         MenuItemImpl var5 = (MenuItemImpl)this.mItems.get(var3);
         if(var5.getGroupId() == var1) {
            var5.setEnabled(var2);
         }
      }

   }

   public void setGroupVisible(int var1, boolean var2) {
      int var6 = this.mItems.size();
      boolean var4 = false;

      boolean var5;
      for(int var3 = 0; var3 < var6; var4 = var5) {
         MenuItemImpl var7 = (MenuItemImpl)this.mItems.get(var3);
         var5 = var4;
         if(var7.getGroupId() == var1) {
            var5 = var4;
            if(var7.setVisibleInt(var2)) {
               var5 = true;
            }
         }

         ++var3;
      }

      if(var4) {
         this.onItemsChanged(true);
      }

   }

   protected MenuBuilder setHeaderIconInt(int var1) {
      this.setHeaderInternal(0, (CharSequence)null, var1, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderIconInt(Drawable var1) {
      this.setHeaderInternal(0, (CharSequence)null, 0, var1, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderTitleInt(int var1) {
      this.setHeaderInternal(var1, (CharSequence)null, 0, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderTitleInt(CharSequence var1) {
      this.setHeaderInternal(0, var1, 0, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderViewInt(View var1) {
      this.setHeaderInternal(0, (CharSequence)null, 0, (Drawable)null, var1);
      return this;
   }

   public void setOptionalIconsVisible(boolean var1) {
      this.mOptionalIconsVisible = var1;
   }

   public void setOverrideVisibleItems(boolean var1) {
      this.mOverrideVisibleItems = var1;
   }

   public void setQwertyMode(boolean var1) {
      this.mQwertyMode = var1;
      this.onItemsChanged(false);
   }

   public void setShortcutsVisible(boolean var1) {
      if(this.mShortcutsVisible != var1) {
         this.setShortcutsVisibleInner(var1);
         this.onItemsChanged(false);
      }

   }

   public int size() {
      return this.mItems.size();
   }

   public void startDispatchingItemsChanged() {
      this.mPreventDispatchingItemsChanged = false;
      if(this.mItemsChangedWhileDispatchPrevented) {
         this.mItemsChangedWhileDispatchPrevented = false;
         this.onItemsChanged(true);
      }

   }

   public void stopDispatchingItemsChanged() {
      if(!this.mPreventDispatchingItemsChanged) {
         this.mPreventDispatchingItemsChanged = true;
         this.mItemsChangedWhileDispatchPrevented = false;
      }

   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public interface Callback {
      boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2);

      void onMenuModeChange(MenuBuilder var1);
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public interface ItemInvoker {
      boolean invokeItem(MenuItemImpl var1);
   }
}
