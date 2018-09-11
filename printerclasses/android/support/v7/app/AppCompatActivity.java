package android.support.v7.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.KeyEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class AppCompatActivity extends FragmentActivity implements AppCompatCallback, TaskStackBuilder.SupportParentable, ActionBarDrawerToggle.DelegateProvider {
   private AppCompatDelegate mDelegate;
   private boolean mEatKeyUpEvent;
   private Resources mResources;
   private int mThemeId = 0;

   public void addContentView(View var1, LayoutParams var2) {
      this.getDelegate().addContentView(var1, var2);
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var3 = true;
      if(KeyEventCompat.isCtrlPressed(var1) && var1.getUnicodeChar(var1.getMetaState() & -28673) == 60) {
         int var2 = var1.getAction();
         if(var2 == 0) {
            ActionBar var4 = this.getSupportActionBar();
            if(var4 != null && var4.isShowing() && var4.requestFocus()) {
               this.mEatKeyUpEvent = true;
               return var3;
            }
         } else if(var2 == 1 && this.mEatKeyUpEvent) {
            this.mEatKeyUpEvent = false;
            return var3;
         }
      }

      var3 = super.dispatchKeyEvent(var1);
      return var3;
   }

   public View findViewById(@IdRes int var1) {
      return this.getDelegate().findViewById(var1);
   }

   @NonNull
   public AppCompatDelegate getDelegate() {
      if(this.mDelegate == null) {
         this.mDelegate = AppCompatDelegate.create((Activity)this, this);
      }

      return this.mDelegate;
   }

   @Nullable
   public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
      return this.getDelegate().getDrawerToggleDelegate();
   }

   public MenuInflater getMenuInflater() {
      return this.getDelegate().getMenuInflater();
   }

   public Resources getResources() {
      if(this.mResources == null && VectorEnabledTintResources.shouldBeUsed()) {
         this.mResources = new VectorEnabledTintResources(this, super.getResources());
      }

      Resources var1;
      if(this.mResources == null) {
         var1 = super.getResources();
      } else {
         var1 = this.mResources;
      }

      return var1;
   }

   @Nullable
   public ActionBar getSupportActionBar() {
      return this.getDelegate().getSupportActionBar();
   }

   @Nullable
   public Intent getSupportParentActivityIntent() {
      return NavUtils.getParentActivityIntent(this);
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public void invalidateOptionsMenu() {
      this.getDelegate().invalidateOptionsMenu();
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.getDelegate().onConfigurationChanged(var1);
      if(this.mResources != null) {
         DisplayMetrics var2 = super.getResources().getDisplayMetrics();
         this.mResources.updateConfiguration(var1, var2);
      }

   }

   public void onContentChanged() {
      this.onSupportContentChanged();
   }

   protected void onCreate(@Nullable Bundle var1) {
      AppCompatDelegate var2 = this.getDelegate();
      var2.installViewFactory();
      var2.onCreate(var1);
      if(var2.applyDayNight() && this.mThemeId != 0) {
         if(VERSION.SDK_INT >= 23) {
            this.onApplyThemeResource(this.getTheme(), this.mThemeId, false);
         } else {
            this.setTheme(this.mThemeId);
         }
      }

      super.onCreate(var1);
   }

   public void onCreateSupportNavigateUpTaskStack(@NonNull TaskStackBuilder var1) {
      var1.addParentStack((Activity)this);
   }

   protected void onDestroy() {
      super.onDestroy();
      this.getDelegate().onDestroy();
   }

   public final boolean onMenuItemSelected(int var1, MenuItem var2) {
      boolean var3;
      if(super.onMenuItemSelected(var1, var2)) {
         var3 = true;
      } else {
         ActionBar var4 = this.getSupportActionBar();
         if(var2.getItemId() == 16908332 && var4 != null && (var4.getDisplayOptions() & 4) != 0) {
            var3 = this.onSupportNavigateUp();
         } else {
            var3 = false;
         }
      }

      return var3;
   }

   public boolean onMenuOpened(int var1, Menu var2) {
      return super.onMenuOpened(var1, var2);
   }

   public void onPanelClosed(int var1, Menu var2) {
      super.onPanelClosed(var1, var2);
   }

   protected void onPostCreate(@Nullable Bundle var1) {
      super.onPostCreate(var1);
      this.getDelegate().onPostCreate(var1);
   }

   protected void onPostResume() {
      super.onPostResume();
      this.getDelegate().onPostResume();
   }

   public void onPrepareSupportNavigateUpTaskStack(@NonNull TaskStackBuilder var1) {
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      this.getDelegate().onSaveInstanceState(var1);
   }

   protected void onStart() {
      super.onStart();
      this.getDelegate().onStart();
   }

   protected void onStop() {
      super.onStop();
      this.getDelegate().onStop();
   }

   @CallSuper
   public void onSupportActionModeFinished(@NonNull ActionMode var1) {
   }

   @CallSuper
   public void onSupportActionModeStarted(@NonNull ActionMode var1) {
   }

   @Deprecated
   public void onSupportContentChanged() {
   }

   public boolean onSupportNavigateUp() {
      Intent var2 = this.getSupportParentActivityIntent();
      boolean var1;
      if(var2 != null) {
         if(this.supportShouldUpRecreateTask(var2)) {
            TaskStackBuilder var4 = TaskStackBuilder.create(this);
            this.onCreateSupportNavigateUpTaskStack(var4);
            this.onPrepareSupportNavigateUpTaskStack(var4);
            var4.startActivities();

            try {
               ActivityCompat.finishAffinity(this);
            } catch (IllegalStateException var3) {
               this.finish();
            }
         } else {
            this.supportNavigateUpTo(var2);
         }

         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected void onTitleChanged(CharSequence var1, int var2) {
      super.onTitleChanged(var1, var2);
      this.getDelegate().setTitle(var1);
   }

   @Nullable
   public ActionMode onWindowStartingSupportActionMode(@NonNull ActionMode.Callback var1) {
      return null;
   }

   public void setContentView(@LayoutRes int var1) {
      this.getDelegate().setContentView(var1);
   }

   public void setContentView(View var1) {
      this.getDelegate().setContentView(var1);
   }

   public void setContentView(View var1, LayoutParams var2) {
      this.getDelegate().setContentView(var1, var2);
   }

   public void setSupportActionBar(@Nullable Toolbar var1) {
      this.getDelegate().setSupportActionBar(var1);
   }

   @Deprecated
   public void setSupportProgress(int var1) {
   }

   @Deprecated
   public void setSupportProgressBarIndeterminate(boolean var1) {
   }

   @Deprecated
   public void setSupportProgressBarIndeterminateVisibility(boolean var1) {
   }

   @Deprecated
   public void setSupportProgressBarVisibility(boolean var1) {
   }

   public void setTheme(@StyleRes int var1) {
      super.setTheme(var1);
      this.mThemeId = var1;
   }

   @Nullable
   public ActionMode startSupportActionMode(@NonNull ActionMode.Callback var1) {
      return this.getDelegate().startSupportActionMode(var1);
   }

   public void supportInvalidateOptionsMenu() {
      this.getDelegate().invalidateOptionsMenu();
   }

   public void supportNavigateUpTo(@NonNull Intent var1) {
      NavUtils.navigateUpTo(this, var1);
   }

   public boolean supportRequestWindowFeature(int var1) {
      return this.getDelegate().requestWindowFeature(var1);
   }

   public boolean supportShouldUpRecreateTask(@NonNull Intent var1) {
      return NavUtils.shouldUpRecreateTask(this, var1);
   }
}
