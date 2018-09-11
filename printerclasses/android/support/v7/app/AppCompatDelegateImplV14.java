package android.support.v7.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegateImplBase;
import android.support.v7.app.AppCompatDelegateImplV11;
import android.support.v7.app.TwilightManager;
import android.support.v7.view.SupportActionModeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Window;
import android.view.Window.Callback;

class AppCompatDelegateImplV14 extends AppCompatDelegateImplV11 {
   private static final boolean FLUSH_RESOURCE_CACHES_ON_NIGHT_CHANGE = true;
   private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
   private boolean mApplyDayNightCalled;
   private AppCompatDelegateImplV14.AutoNightModeManager mAutoNightModeManager;
   private boolean mHandleNativeActionModes = true;
   private int mLocalNightMode = -100;

   AppCompatDelegateImplV14(Context var1, Window var2, AppCompatCallback var3) {
      super(var1, var2, var3);
   }

   private void ensureAutoNightModeManager() {
      if(this.mAutoNightModeManager == null) {
         this.mAutoNightModeManager = new AppCompatDelegateImplV14.AutoNightModeManager(TwilightManager.getInstance(this.mContext));
      }

   }

   private int getNightMode() {
      int var1;
      if(this.mLocalNightMode != -100) {
         var1 = this.mLocalNightMode;
      } else {
         var1 = getDefaultNightMode();
      }

      return var1;
   }

   private boolean shouldRecreateOnNightModeChange() {
      boolean var2 = true;
      if(this.mApplyDayNightCalled && this.mContext instanceof Activity) {
         PackageManager var4 = this.mContext.getPackageManager();

         int var1;
         try {
            ComponentName var3 = new ComponentName(this.mContext, this.mContext.getClass());
            var1 = var4.getActivityInfo(var3, 0).configChanges;
         } catch (NameNotFoundException var5) {
            Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", var5);
            return var2;
         }

         if((var1 & 512) != 0) {
            var2 = false;
         }
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean updateForNightMode(int var1) {
      Resources var5 = this.mContext.getResources();
      Configuration var6 = var5.getConfiguration();
      int var3 = var6.uiMode;
      byte var8;
      if(var1 == 2) {
         var8 = 32;
      } else {
         var8 = 16;
      }

      boolean var4;
      if((var3 & 48) != var8) {
         if(this.shouldRecreateOnNightModeChange()) {
            ((Activity)this.mContext).recreate();
         } else {
            var6 = new Configuration(var6);
            DisplayMetrics var7 = var5.getDisplayMetrics();
            float var2 = var6.fontScale;
            var6.uiMode = var6.uiMode & -49 | var8;
            var6.fontScale = 2.0F * var2;
            var5.updateConfiguration(var6, var7);
            var6.fontScale = var2;
            var5.updateConfiguration(var6, var7);
         }

         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public boolean applyDayNight() {
      boolean var3 = false;
      int var2 = this.getNightMode();
      int var1 = this.mapNightMode(var2);
      if(var1 != -1) {
         var3 = this.updateForNightMode(var1);
      }

      if(var2 == 0) {
         this.ensureAutoNightModeManager();
         this.mAutoNightModeManager.setup();
      }

      this.mApplyDayNightCalled = true;
      return var3;
   }

   @VisibleForTesting
   final AppCompatDelegateImplV14.AutoNightModeManager getAutoNightModeManager() {
      this.ensureAutoNightModeManager();
      return this.mAutoNightModeManager;
   }

   public boolean isHandleNativeActionModesEnabled() {
      return this.mHandleNativeActionModes;
   }

   int mapNightMode(int var1) {
      switch(var1) {
      case -100:
         var1 = -1;
         break;
      case 0:
         this.ensureAutoNightModeManager();
         var1 = this.mAutoNightModeManager.getApplyableNightMode();
      }

      return var1;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if(var1 != null && this.mLocalNightMode == -100) {
         this.mLocalNightMode = var1.getInt("appcompat:local_night_mode", -100);
      }

   }

   public void onDestroy() {
      super.onDestroy();
      if(this.mAutoNightModeManager != null) {
         this.mAutoNightModeManager.cleanup();
      }

   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      if(this.mLocalNightMode != -100) {
         var1.putInt("appcompat:local_night_mode", this.mLocalNightMode);
      }

   }

   public void onStart() {
      super.onStart();
      this.applyDayNight();
   }

   public void onStop() {
      super.onStop();
      if(this.mAutoNightModeManager != null) {
         this.mAutoNightModeManager.cleanup();
      }

   }

   public void setHandleNativeActionModesEnabled(boolean var1) {
      this.mHandleNativeActionModes = var1;
   }

   public void setLocalNightMode(int var1) {
      switch(var1) {
      case -1:
      case 0:
      case 1:
      case 2:
         if(this.mLocalNightMode != var1) {
            this.mLocalNightMode = var1;
            if(this.mApplyDayNightCalled) {
               this.applyDayNight();
            }
         }
         break;
      default:
         Log.i("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
      }

   }

   Callback wrapWindowCallback(Callback var1) {
      return new AppCompatDelegateImplV14.AppCompatWindowCallbackV14(var1);
   }

   class AppCompatWindowCallbackV14 extends AppCompatDelegateImplBase.AppCompatWindowCallbackBase {
      AppCompatWindowCallbackV14(Callback var2) {
         super();
      }

      public ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1) {
         ActionMode var2;
         if(AppCompatDelegateImplV14.this.isHandleNativeActionModesEnabled()) {
            var2 = this.startAsSupportActionMode(var1);
         } else {
            var2 = super.onWindowStartingActionMode(var1);
         }

         return var2;
      }

      final ActionMode startAsSupportActionMode(android.view.ActionMode.Callback var1) {
         SupportActionModeWrapper.CallbackWrapper var3 = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImplV14.this.mContext, var1);
         android.support.v7.view.ActionMode var2 = AppCompatDelegateImplV14.this.startSupportActionMode(var3);
         ActionMode var4;
         if(var2 != null) {
            var4 = var3.getActionModeWrapper(var2);
         } else {
            var4 = null;
         }

         return var4;
      }
   }

   @VisibleForTesting
   final class AutoNightModeManager {
      private BroadcastReceiver mAutoTimeChangeReceiver;
      private IntentFilter mAutoTimeChangeReceiverFilter;
      private boolean mIsNight;
      private TwilightManager mTwilightManager;

      AutoNightModeManager(@NonNull TwilightManager var2) {
         this.mTwilightManager = var2;
         this.mIsNight = var2.isNight();
      }

      final void cleanup() {
         if(this.mAutoTimeChangeReceiver != null) {
            AppCompatDelegateImplV14.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
            this.mAutoTimeChangeReceiver = null;
         }

      }

      final void dispatchTimeChanged() {
         boolean var1 = this.mTwilightManager.isNight();
         if(var1 != this.mIsNight) {
            this.mIsNight = var1;
            AppCompatDelegateImplV14.this.applyDayNight();
         }

      }

      final int getApplyableNightMode() {
         byte var1;
         if(this.mIsNight) {
            var1 = 2;
         } else {
            var1 = 1;
         }

         return var1;
      }

      final void setup() {
         this.cleanup();
         if(this.mAutoTimeChangeReceiver == null) {
            this.mAutoTimeChangeReceiver = new BroadcastReceiver() {
               public void onReceive(Context var1, Intent var2) {
                  AutoNightModeManager.this.dispatchTimeChanged();
               }
            };
         }

         if(this.mAutoTimeChangeReceiverFilter == null) {
            this.mAutoTimeChangeReceiverFilter = new IntentFilter();
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_SET");
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
         }

         AppCompatDelegateImplV14.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
      }
   }
}
