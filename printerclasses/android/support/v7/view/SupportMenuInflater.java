package android.support.v7.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class SupportMenuInflater extends MenuInflater {
   static final Class[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
   static final Class[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[]{Context.class};
   static final String LOG_TAG = "SupportMenuInflater";
   static final int NO_ID = 0;
   private static final String XML_GROUP = "group";
   private static final String XML_ITEM = "item";
   private static final String XML_MENU = "menu";
   final Object[] mActionProviderConstructorArguments;
   final Object[] mActionViewConstructorArguments;
   Context mContext;
   private Object mRealOwner;

   static {
      ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
   }

   public SupportMenuInflater(Context var1) {
      super(var1);
      this.mContext = var1;
      this.mActionViewConstructorArguments = new Object[]{var1};
      this.mActionProviderConstructorArguments = this.mActionViewConstructorArguments;
   }

   private Object findRealOwner(Object var1) {
      Object var2;
      if(var1 instanceof Activity) {
         var2 = var1;
      } else {
         var2 = var1;
         if(var1 instanceof ContextWrapper) {
            var2 = this.findRealOwner(((ContextWrapper)var1).getBaseContext());
         }
      }

      return var2;
   }

   private void parseMenu(XmlPullParser var1, AttributeSet var2, Menu var3) throws XmlPullParserException, IOException {
      SupportMenuInflater.MenuState var10 = new SupportMenuInflater.MenuState(var3);
      int var5 = var1.getEventType();
      boolean var6 = false;
      String var9 = null;

      int var4;
      String var12;
      do {
         if(var5 == 2) {
            var12 = var1.getName();
            if(!var12.equals("menu")) {
               throw new RuntimeException("Expecting menu, got " + var12);
            }

            var4 = var1.next();
            break;
         }

         var4 = var1.next();
         var5 = var4;
      } while(var4 != 1);

      boolean var14 = false;

      for(int var8 = var4; !var14; var9 = var12) {
         boolean var7;
         boolean var13;
         switch(var8) {
         case 1:
            throw new RuntimeException("Unexpected end of document");
         case 2:
            var13 = var6;
            var7 = var14;
            var12 = var9;
            if(!var6) {
               var12 = var1.getName();
               if(var12.equals("group")) {
                  var10.readGroup(var2);
                  var13 = var6;
                  var7 = var14;
                  var12 = var9;
               } else if(var12.equals("item")) {
                  var10.readItem(var2);
                  var13 = var6;
                  var7 = var14;
                  var12 = var9;
               } else if(var12.equals("menu")) {
                  this.parseMenu(var1, var2, var10.addSubMenuItem());
                  var13 = var6;
                  var7 = var14;
                  var12 = var9;
               } else {
                  var13 = true;
                  var7 = var14;
               }
            }
            break;
         case 3:
            String var11 = var1.getName();
            if(var6 && var11.equals(var9)) {
               var13 = false;
               var12 = null;
               var7 = var14;
            } else if(var11.equals("group")) {
               var10.resetGroup();
               var13 = var6;
               var7 = var14;
               var12 = var9;
            } else if(var11.equals("item")) {
               var13 = var6;
               var7 = var14;
               var12 = var9;
               if(!var10.hasAddedItem()) {
                  if(var10.itemActionProvider != null && var10.itemActionProvider.hasSubMenu()) {
                     var10.addSubMenuItem();
                     var13 = var6;
                     var7 = var14;
                     var12 = var9;
                  } else {
                     var10.addItem();
                     var13 = var6;
                     var7 = var14;
                     var12 = var9;
                  }
               }
            } else {
               var13 = var6;
               var7 = var14;
               var12 = var9;
               if(var11.equals("menu")) {
                  var7 = true;
                  var13 = var6;
                  var12 = var9;
               }
            }
            break;
         default:
            var12 = var9;
            var7 = var14;
            var13 = var6;
         }

         var8 = var1.next();
         var6 = var13;
         var14 = var7;
      }

   }

   Object getRealOwner() {
      if(this.mRealOwner == null) {
         this.mRealOwner = this.findRealOwner(this.mContext);
      }

      return this.mRealOwner;
   }

   public void inflate(int param1, Menu param2) {
      // $FF: Couldn't be decompiled
   }

   private static class InflatedOnMenuItemClickListener implements OnMenuItemClickListener {
      private static final Class[] PARAM_TYPES = new Class[]{MenuItem.class};
      private Method mMethod;
      private Object mRealOwner;

      public InflatedOnMenuItemClickListener(Object var1, String var2) {
         this.mRealOwner = var1;
         Class var3 = var1.getClass();

         try {
            this.mMethod = var3.getMethod(var2, PARAM_TYPES);
         } catch (Exception var4) {
            InflateException var5 = new InflateException("Couldn\'t resolve menu item onClick handler " + var2 + " in class " + var3.getName());
            var5.initCause(var4);
            throw var5;
         }
      }

      public boolean onMenuItemClick(MenuItem var1) {
         boolean var2 = true;

         try {
            if(this.mMethod.getReturnType() == Boolean.TYPE) {
               var2 = ((Boolean)this.mMethod.invoke(this.mRealOwner, new Object[]{var1})).booleanValue();
            } else {
               this.mMethod.invoke(this.mRealOwner, new Object[]{var1});
            }

            return var2;
         } catch (Exception var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   private class MenuState {
      private static final int defaultGroupId = 0;
      private static final int defaultItemCategory = 0;
      private static final int defaultItemCheckable = 0;
      private static final boolean defaultItemChecked = false;
      private static final boolean defaultItemEnabled = true;
      private static final int defaultItemId = 0;
      private static final int defaultItemOrder = 0;
      private static final boolean defaultItemVisible = true;
      private int groupCategory;
      private int groupCheckable;
      private boolean groupEnabled;
      private int groupId;
      private int groupOrder;
      private boolean groupVisible;
      ActionProvider itemActionProvider;
      private String itemActionProviderClassName;
      private String itemActionViewClassName;
      private int itemActionViewLayout;
      private boolean itemAdded;
      private char itemAlphabeticShortcut;
      private int itemCategoryOrder;
      private int itemCheckable;
      private boolean itemChecked;
      private boolean itemEnabled;
      private int itemIconResId;
      private int itemId;
      private String itemListenerMethodName;
      private char itemNumericShortcut;
      private int itemShowAsAction;
      private CharSequence itemTitle;
      private CharSequence itemTitleCondensed;
      private boolean itemVisible;
      private Menu menu;

      public MenuState(Menu var2) {
         this.menu = var2;
         this.resetGroup();
      }

      private char getShortcut(String var1) {
         char var2 = 0;
         if(var1 != null) {
            var2 = var1.charAt(0);
         }

         return var2;
      }

      private Object newInstance(String var1, Class[] var2, Object[] var3) {
         Object var5;
         Object var7;
         try {
            Constructor var6 = SupportMenuInflater.this.mContext.getClassLoader().loadClass(var1).getConstructor(var2);
            var6.setAccessible(true);
            var7 = var6.newInstance(var3);
         } catch (Exception var4) {
            Log.w("SupportMenuInflater", "Cannot instantiate class: " + var1, var4);
            var5 = null;
            return var5;
         }

         var5 = var7;
         return var5;
      }

      private void setItem(MenuItem var1) {
         MenuItem var4 = var1.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled);
         boolean var3;
         if(this.itemCheckable >= 1) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4.setCheckable(var3).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId).setAlphabeticShortcut(this.itemAlphabeticShortcut).setNumericShortcut(this.itemNumericShortcut);
         if(this.itemShowAsAction >= 0) {
            MenuItemCompat.setShowAsAction(var1, this.itemShowAsAction);
         }

         if(this.itemListenerMethodName != null) {
            if(SupportMenuInflater.this.mContext.isRestricted()) {
               throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
            }

            var1.setOnMenuItemClickListener(new SupportMenuInflater.InflatedOnMenuItemClickListener(SupportMenuInflater.this.getRealOwner(), this.itemListenerMethodName));
         }

         if(var1 instanceof MenuItemImpl) {
            MenuItemImpl var5 = (MenuItemImpl)var1;
         }

         if(this.itemCheckable >= 2) {
            if(var1 instanceof MenuItemImpl) {
               ((MenuItemImpl)var1).setExclusiveCheckable(true);
            } else if(var1 instanceof MenuItemWrapperICS) {
               ((MenuItemWrapperICS)var1).setExclusiveCheckable(true);
            }
         }

         boolean var2 = false;
         if(this.itemActionViewClassName != null) {
            MenuItemCompat.setActionView(var1, (View)this.newInstance(this.itemActionViewClassName, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
            var2 = true;
         }

         if(this.itemActionViewLayout > 0) {
            if(!var2) {
               MenuItemCompat.setActionView(var1, this.itemActionViewLayout);
            } else {
               Log.w("SupportMenuInflater", "Ignoring attribute \'itemActionViewLayout\'. Action view already specified.");
            }
         }

         if(this.itemActionProvider != null) {
            MenuItemCompat.setActionProvider(var1, this.itemActionProvider);
         }

      }

      public void addItem() {
         this.itemAdded = true;
         this.setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
      }

      public SubMenu addSubMenuItem() {
         this.itemAdded = true;
         SubMenu var1 = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
         this.setItem(var1.getItem());
         return var1;
      }

      public boolean hasAddedItem() {
         return this.itemAdded;
      }

      public void readGroup(AttributeSet var1) {
         TypedArray var2 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R.styleable.MenuGroup);
         this.groupId = var2.getResourceId(R.styleable.MenuGroup_android_id, 0);
         this.groupCategory = var2.getInt(R.styleable.MenuGroup_android_menuCategory, 0);
         this.groupOrder = var2.getInt(R.styleable.MenuGroup_android_orderInCategory, 0);
         this.groupCheckable = var2.getInt(R.styleable.MenuGroup_android_checkableBehavior, 0);
         this.groupVisible = var2.getBoolean(R.styleable.MenuGroup_android_visible, true);
         this.groupEnabled = var2.getBoolean(R.styleable.MenuGroup_android_enabled, true);
         var2.recycle();
      }

      public void readItem(AttributeSet var1) {
         TypedArray var3 = SupportMenuInflater.this.mContext.obtainStyledAttributes(var1, R.styleable.MenuItem);
         this.itemId = var3.getResourceId(R.styleable.MenuItem_android_id, 0);
         this.itemCategoryOrder = -65536 & var3.getInt(R.styleable.MenuItem_android_menuCategory, this.groupCategory) | '\uffff' & var3.getInt(R.styleable.MenuItem_android_orderInCategory, this.groupOrder);
         this.itemTitle = var3.getText(R.styleable.MenuItem_android_title);
         this.itemTitleCondensed = var3.getText(R.styleable.MenuItem_android_titleCondensed);
         this.itemIconResId = var3.getResourceId(R.styleable.MenuItem_android_icon, 0);
         this.itemAlphabeticShortcut = this.getShortcut(var3.getString(R.styleable.MenuItem_android_alphabeticShortcut));
         this.itemNumericShortcut = this.getShortcut(var3.getString(R.styleable.MenuItem_android_numericShortcut));
         if(var3.hasValue(R.styleable.MenuItem_android_checkable)) {
            byte var2;
            if(var3.getBoolean(R.styleable.MenuItem_android_checkable, false)) {
               var2 = 1;
            } else {
               var2 = 0;
            }

            this.itemCheckable = var2;
         } else {
            this.itemCheckable = this.groupCheckable;
         }

         this.itemChecked = var3.getBoolean(R.styleable.MenuItem_android_checked, false);
         this.itemVisible = var3.getBoolean(R.styleable.MenuItem_android_visible, this.groupVisible);
         this.itemEnabled = var3.getBoolean(R.styleable.MenuItem_android_enabled, this.groupEnabled);
         this.itemShowAsAction = var3.getInt(R.styleable.MenuItem_showAsAction, -1);
         this.itemListenerMethodName = var3.getString(R.styleable.MenuItem_android_onClick);
         this.itemActionViewLayout = var3.getResourceId(R.styleable.MenuItem_actionLayout, 0);
         this.itemActionViewClassName = var3.getString(R.styleable.MenuItem_actionViewClass);
         this.itemActionProviderClassName = var3.getString(R.styleable.MenuItem_actionProviderClass);
         boolean var4;
         if(this.itemActionProviderClassName != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         if(var4 && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
            this.itemActionProvider = (ActionProvider)this.newInstance(this.itemActionProviderClassName, SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionProviderConstructorArguments);
         } else {
            if(var4) {
               Log.w("SupportMenuInflater", "Ignoring attribute \'actionProviderClass\'. Action view already specified.");
            }

            this.itemActionProvider = null;
         }

         var3.recycle();
         this.itemAdded = false;
      }

      public void resetGroup() {
         this.groupId = 0;
         this.groupCategory = 0;
         this.groupOrder = 0;
         this.groupCheckable = 0;
         this.groupVisible = true;
         this.groupEnabled = true;
      }
   }
}
