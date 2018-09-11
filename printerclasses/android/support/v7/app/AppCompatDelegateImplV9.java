package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegateImplBase;
import android.support.v7.app.AppCompatViewInflater;
import android.support.v7.app.ToolbarActionBar;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.LayoutInflater.Factory;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window.Callback;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

class AppCompatDelegateImplV9 extends AppCompatDelegateImplBase implements MenuBuilder.Callback, LayoutInflaterFactory {
   private AppCompatDelegateImplV9.ActionMenuPresenterCallback mActionMenuPresenterCallback;
   ActionMode mActionMode;
   PopupWindow mActionModePopup;
   ActionBarContextView mActionModeView;
   private AppCompatViewInflater mAppCompatViewInflater;
   private boolean mClosingActionMenu;
   private DecorContentParent mDecorContentParent;
   private boolean mEnableDefaultActionBarUp;
   ViewPropertyAnimatorCompat mFadeAnim = null;
   private boolean mFeatureIndeterminateProgress;
   private boolean mFeatureProgress;
   int mInvalidatePanelMenuFeatures;
   boolean mInvalidatePanelMenuPosted;
   private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
      public void run() {
         if((AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures & 1) != 0) {
            AppCompatDelegateImplV9.this.doInvalidatePanelMenu(0);
         }

         if((AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures & 4096) != 0) {
            AppCompatDelegateImplV9.this.doInvalidatePanelMenu(108);
         }

         AppCompatDelegateImplV9.this.mInvalidatePanelMenuPosted = false;
         AppCompatDelegateImplV9.this.mInvalidatePanelMenuFeatures = 0;
      }
   };
   private boolean mLongPressBackDown;
   private AppCompatDelegateImplV9.PanelMenuPresenterCallback mPanelMenuPresenterCallback;
   private AppCompatDelegateImplV9.PanelFeatureState[] mPanels;
   private AppCompatDelegateImplV9.PanelFeatureState mPreparedPanel;
   Runnable mShowActionModePopup;
   private View mStatusGuard;
   private ViewGroup mSubDecor;
   private boolean mSubDecorInstalled;
   private Rect mTempRect1;
   private Rect mTempRect2;
   private TextView mTitleView;

   AppCompatDelegateImplV9(Context var1, Window var2, AppCompatCallback var3) {
      super(var1, var2, var3);
   }

   private void applyFixedSizeWindow() {
      ContentFrameLayout var1 = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
      View var2 = this.mWindow.getDecorView();
      var1.setDecorPadding(var2.getPaddingLeft(), var2.getPaddingTop(), var2.getPaddingRight(), var2.getPaddingBottom());
      TypedArray var3 = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
      var3.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, var1.getMinWidthMajor());
      var3.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, var1.getMinWidthMinor());
      if(var3.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, var1.getFixedWidthMajor());
      }

      if(var3.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, var1.getFixedWidthMinor());
      }

      if(var3.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, var1.getFixedHeightMajor());
      }

      if(var3.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, var1.getFixedHeightMinor());
      }

      var3.recycle();
      var1.requestLayout();
   }

   private ViewGroup createSubDecor() {
      TypedArray var1 = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
      if(!var1.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
         var1.recycle();
         throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
      } else {
         if(var1.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
            this.requestWindowFeature(1);
         } else if(var1.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
            this.requestWindowFeature(108);
         }

         if(var1.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            this.requestWindowFeature(109);
         }

         if(var1.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            this.requestWindowFeature(10);
         }

         this.mIsFloating = var1.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
         var1.recycle();
         this.mWindow.getDecorView();
         LayoutInflater var2 = LayoutInflater.from(this.mContext);
         ViewGroup var5 = null;
         if(!this.mWindowNoTitle) {
            if(this.mIsFloating) {
               var5 = (ViewGroup)var2.inflate(R.layout.abc_dialog_title_material, (ViewGroup)null);
               this.mOverlayActionBar = false;
               this.mHasActionBar = false;
            } else if(this.mHasActionBar) {
               TypedValue var6 = new TypedValue();
               this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, var6, true);
               Object var7;
               if(var6.resourceId != 0) {
                  var7 = new ContextThemeWrapper(this.mContext, var6.resourceId);
               } else {
                  var7 = this.mContext;
               }

               ViewGroup var8 = (ViewGroup)LayoutInflater.from((Context)var7).inflate(R.layout.abc_screen_toolbar, (ViewGroup)null);
               this.mDecorContentParent = (DecorContentParent)var8.findViewById(R.id.decor_content_parent);
               this.mDecorContentParent.setWindowCallback(this.getWindowCallback());
               if(this.mOverlayActionBar) {
                  this.mDecorContentParent.initFeature(109);
               }

               if(this.mFeatureProgress) {
                  this.mDecorContentParent.initFeature(2);
               }

               var5 = var8;
               if(this.mFeatureIndeterminateProgress) {
                  this.mDecorContentParent.initFeature(5);
                  var5 = var8;
               }
            }
         } else {
            if(this.mOverlayActionMode) {
               var5 = (ViewGroup)var2.inflate(R.layout.abc_screen_simple_overlay_action_mode, (ViewGroup)null);
            } else {
               var5 = (ViewGroup)var2.inflate(R.layout.abc_screen_simple, (ViewGroup)null);
            }

            if(VERSION.SDK_INT >= 21) {
               ViewCompat.setOnApplyWindowInsetsListener(var5, new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     int var4 = var2.getSystemWindowInsetTop();
                     int var3 = AppCompatDelegateImplV9.this.updateStatusGuard(var4);
                     WindowInsetsCompat var5 = var2;
                     if(var4 != var3) {
                        var5 = var2.replaceSystemWindowInsets(var2.getSystemWindowInsetLeft(), var3, var2.getSystemWindowInsetRight(), var2.getSystemWindowInsetBottom());
                     }

                     return ViewCompat.onApplyWindowInsets(var1, var5);
                  }
               });
            } else {
               ((FitWindowsViewGroup)var5).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                  public void onFitSystemWindows(Rect var1) {
                     var1.top = AppCompatDelegateImplV9.this.updateStatusGuard(var1.top);
                  }
               });
            }
         }

         if(var5 == null) {
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.mHasActionBar + ", windowActionBarOverlay: " + this.mOverlayActionBar + ", android:windowIsFloating: " + this.mIsFloating + ", windowActionModeOverlay: " + this.mOverlayActionMode + ", windowNoTitle: " + this.mWindowNoTitle + " }");
         } else {
            if(this.mDecorContentParent == null) {
               this.mTitleView = (TextView)var5.findViewById(R.id.title);
            }

            ViewUtils.makeOptionalFitsSystemWindows(var5);
            ContentFrameLayout var9 = (ContentFrameLayout)var5.findViewById(R.id.action_bar_activity_content);
            ViewGroup var4 = (ViewGroup)this.mWindow.findViewById(16908290);
            if(var4 != null) {
               while(var4.getChildCount() > 0) {
                  View var3 = var4.getChildAt(0);
                  var4.removeViewAt(0);
                  var9.addView(var3);
               }

               var4.setId(-1);
               var9.setId(16908290);
               if(var4 instanceof FrameLayout) {
                  ((FrameLayout)var4).setForeground((Drawable)null);
               }
            }

            this.mWindow.setContentView(var5);
            var9.setAttachListener(new ContentFrameLayout.OnAttachListener() {
               public void onAttachedFromWindow() {
               }

               public void onDetachedFromWindow() {
                  AppCompatDelegateImplV9.this.dismissPopups();
               }
            });
            return var5;
         }
      }
   }

   private void ensureSubDecor() {
      if(!this.mSubDecorInstalled) {
         this.mSubDecor = this.createSubDecor();
         CharSequence var1 = this.getTitle();
         if(!TextUtils.isEmpty(var1)) {
            this.onTitleChanged(var1);
         }

         this.applyFixedSizeWindow();
         this.onSubDecorInstalled(this.mSubDecor);
         this.mSubDecorInstalled = true;
         AppCompatDelegateImplV9.PanelFeatureState var2 = this.getPanelState(0, false);
         if(!this.isDestroyed() && (var2 == null || var2.menu == null)) {
            this.invalidatePanelMenu(108);
         }
      }

   }

   private boolean initializePanelContent(AppCompatDelegateImplV9.PanelFeatureState var1) {
      boolean var2 = true;
      if(var1.createdPanelView != null) {
         var1.shownPanelView = var1.createdPanelView;
      } else if(var1.menu == null) {
         var2 = false;
      } else {
         if(this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new AppCompatDelegateImplV9.PanelMenuPresenterCallback();
         }

         var1.shownPanelView = (View)var1.getListMenuView(this.mPanelMenuPresenterCallback);
         if(var1.shownPanelView == null) {
            var2 = false;
         }
      }

      return var2;
   }

   private boolean initializePanelDecor(AppCompatDelegateImplV9.PanelFeatureState var1) {
      var1.setStyle(this.getActionBarThemedContext());
      var1.decorView = new AppCompatDelegateImplV9.ListMenuDecorView(var1.listPresenterContext);
      var1.gravity = 81;
      return true;
   }

   private boolean initializePanelMenu(AppCompatDelegateImplV9.PanelFeatureState var1) {
      Object var2;
      label28: {
         Context var4 = this.mContext;
         if(var1.featureId != 0) {
            var2 = var4;
            if(var1.featureId != 108) {
               break label28;
            }
         }

         var2 = var4;
         if(this.mDecorContentParent != null) {
            TypedValue var5 = new TypedValue();
            Theme var6 = var4.getTheme();
            var6.resolveAttribute(R.attr.actionBarTheme, var5, true);
            Theme var7 = null;
            if(var5.resourceId != 0) {
               var7 = var4.getResources().newTheme();
               var7.setTo(var6);
               var7.applyStyle(var5.resourceId, true);
               var7.resolveAttribute(R.attr.actionBarWidgetTheme, var5, true);
            } else {
               var6.resolveAttribute(R.attr.actionBarWidgetTheme, var5, true);
            }

            Theme var3 = var7;
            if(var5.resourceId != 0) {
               var3 = var7;
               if(var7 == null) {
                  var3 = var4.getResources().newTheme();
                  var3.setTo(var6);
               }

               var3.applyStyle(var5.resourceId, true);
            }

            var2 = var4;
            if(var3 != null) {
               var2 = new ContextThemeWrapper(var4, 0);
               ((Context)var2).getTheme().setTo(var3);
            }
         }
      }

      MenuBuilder var8 = new MenuBuilder((Context)var2);
      var8.setCallback(this);
      var1.setMenu(var8);
      return true;
   }

   private void invalidatePanelMenu(int var1) {
      this.mInvalidatePanelMenuFeatures |= 1 << var1;
      if(!this.mInvalidatePanelMenuPosted) {
         ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
         this.mInvalidatePanelMenuPosted = true;
      }

   }

   private boolean onKeyDownPanel(int var1, KeyEvent var2) {
      boolean var3;
      if(var2.getRepeatCount() == 0) {
         AppCompatDelegateImplV9.PanelFeatureState var4 = this.getPanelState(var1, true);
         if(!var4.isOpen) {
            var3 = this.preparePanel(var4, var2);
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   private boolean onKeyUpPanel(int var1, KeyEvent var2) {
      boolean var4;
      if(this.mActionMode != null) {
         var4 = false;
      } else {
         boolean var5 = false;
         AppCompatDelegateImplV9.PanelFeatureState var6 = this.getPanelState(var1, true);
         boolean var3;
         if(var1 == 0 && this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && !ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext))) {
            if(!this.mDecorContentParent.isOverflowMenuShowing()) {
               var3 = var5;
               if(!this.isDestroyed()) {
                  var3 = var5;
                  if(this.preparePanel(var6, var2)) {
                     var3 = this.mDecorContentParent.showOverflowMenu();
                  }
               }
            } else {
               var3 = this.mDecorContentParent.hideOverflowMenu();
            }
         } else if(!var6.isOpen && !var6.isHandled) {
            var3 = var5;
            if(var6.isPrepared) {
               var4 = true;
               if(var6.refreshMenuContent) {
                  var6.isPrepared = false;
                  var4 = this.preparePanel(var6, var2);
               }

               var3 = var5;
               if(var4) {
                  this.openPanel(var6, var2);
                  var3 = true;
               }
            }
         } else {
            var3 = var6.isOpen;
            this.closePanel(var6, true);
         }

         var4 = var3;
         if(var3) {
            AudioManager var7 = (AudioManager)this.mContext.getSystemService("audio");
            if(var7 != null) {
               var7.playSoundEffect(0);
               var4 = var3;
            } else {
               Log.w("AppCompatDelegate", "Couldn\'t get audio manager");
               var4 = var3;
            }
         }
      }

      return var4;
   }

   private void openPanel(AppCompatDelegateImplV9.PanelFeatureState var1, KeyEvent var2) {
      if(!var1.isOpen && !this.isDestroyed()) {
         if(var1.featureId == 0) {
            Context var5 = this.mContext;
            boolean var3;
            if((var5.getResources().getConfiguration().screenLayout & 15) == 4) {
               var3 = true;
            } else {
               var3 = false;
            }

            boolean var4;
            if(var5.getApplicationInfo().targetSdkVersion >= 11) {
               var4 = true;
            } else {
               var4 = false;
            }

            if(var3 && var4) {
               return;
            }
         }

         Callback var12 = this.getWindowCallback();
         if(var12 != null && !var12.onMenuOpened(var1.featureId, var1.menu)) {
            this.closePanel(var1, true);
         } else {
            WindowManager var6 = (WindowManager)this.mContext.getSystemService("window");
            if(var6 != null && this.preparePanel(var1, var2)) {
               byte var11 = -2;
               LayoutParams var7;
               byte var10;
               if(var1.decorView != null && !var1.refreshDecorView) {
                  var10 = var11;
                  if(var1.createdPanelView != null) {
                     var7 = var1.createdPanelView.getLayoutParams();
                     var10 = var11;
                     if(var7 != null) {
                        var10 = var11;
                        if(var7.width == -1) {
                           var10 = -1;
                        }
                     }
                  }
               } else {
                  if(var1.decorView == null) {
                     if(!this.initializePanelDecor(var1) || var1.decorView == null) {
                        return;
                     }
                  } else if(var1.refreshDecorView && var1.decorView.getChildCount() > 0) {
                     var1.decorView.removeAllViews();
                  }

                  if(!this.initializePanelContent(var1) || !var1.hasPanelItems()) {
                     return;
                  }

                  LayoutParams var13 = var1.shownPanelView.getLayoutParams();
                  var7 = var13;
                  if(var13 == null) {
                     var7 = new LayoutParams(-2, -2);
                  }

                  int var9 = var1.background;
                  var1.decorView.setBackgroundResource(var9);
                  ViewParent var14 = var1.shownPanelView.getParent();
                  if(var14 != null && var14 instanceof ViewGroup) {
                     ((ViewGroup)var14).removeView(var1.shownPanelView);
                  }

                  var1.decorView.addView(var1.shownPanelView, var7);
                  var10 = var11;
                  if(!var1.shownPanelView.hasFocus()) {
                     var1.shownPanelView.requestFocus();
                     var10 = var11;
                  }
               }

               var1.isHandled = false;
               android.view.WindowManager.LayoutParams var8 = new android.view.WindowManager.LayoutParams(var10, -2, var1.x, var1.y, 1002, 8519680, -3);
               var8.gravity = var1.gravity;
               var8.windowAnimations = var1.windowAnimations;
               var6.addView(var1.decorView, var8);
               var1.isOpen = true;
            }
         }
      }

   }

   private boolean performPanelShortcut(AppCompatDelegateImplV9.PanelFeatureState var1, int var2, KeyEvent var3, int var4) {
      boolean var6;
      if(var3.isSystem()) {
         var6 = false;
      } else {
         boolean var5;
         label22: {
            var6 = false;
            if(!var1.isPrepared) {
               var5 = var6;
               if(!this.preparePanel(var1, var3)) {
                  break label22;
               }
            }

            var5 = var6;
            if(var1.menu != null) {
               var5 = var1.menu.performShortcut(var2, var3, var4);
            }
         }

         var6 = var5;
         if(var5) {
            var6 = var5;
            if((var4 & 1) == 0) {
               var6 = var5;
               if(this.mDecorContentParent == null) {
                  this.closePanel(var1, true);
                  var6 = var5;
               }
            }
         }
      }

      return var6;
   }

   private boolean preparePanel(AppCompatDelegateImplV9.PanelFeatureState var1, KeyEvent var2) {
      boolean var5 = false;
      boolean var4;
      if(this.isDestroyed()) {
         var4 = var5;
      } else if(var1.isPrepared) {
         var4 = true;
      } else {
         if(this.mPreparedPanel != null && this.mPreparedPanel != var1) {
            this.closePanel(this.mPreparedPanel, false);
         }

         Callback var6 = this.getWindowCallback();
         if(var6 != null) {
            var1.createdPanelView = var6.onCreatePanelView(var1.featureId);
         }

         boolean var3;
         if(var1.featureId != 0 && var1.featureId != 108) {
            var3 = false;
         } else {
            var3 = true;
         }

         if(var3 && this.mDecorContentParent != null) {
            this.mDecorContentParent.setMenuPrepared();
         }

         if(var1.createdPanelView == null && (!var3 || !(this.peekSupportActionBar() instanceof ToolbarActionBar))) {
            if(var1.menu == null || var1.refreshMenuContent) {
               if(var1.menu == null) {
                  var4 = var5;
                  if(!this.initializePanelMenu(var1)) {
                     return var4;
                  }

                  var4 = var5;
                  if(var1.menu == null) {
                     return var4;
                  }
               }

               if(var3 && this.mDecorContentParent != null) {
                  if(this.mActionMenuPresenterCallback == null) {
                     this.mActionMenuPresenterCallback = new AppCompatDelegateImplV9.ActionMenuPresenterCallback();
                  }

                  this.mDecorContentParent.setMenu(var1.menu, this.mActionMenuPresenterCallback);
               }

               var1.menu.stopDispatchingItemsChanged();
               if(!var6.onCreatePanelMenu(var1.featureId, var1.menu)) {
                  var1.setMenu((MenuBuilder)null);
                  var4 = var5;
                  if(var3) {
                     var4 = var5;
                     if(this.mDecorContentParent != null) {
                        this.mDecorContentParent.setMenu((Menu)null, this.mActionMenuPresenterCallback);
                        var4 = var5;
                        return var4;
                     }
                  }

                  return var4;
               }

               var1.refreshMenuContent = false;
            }

            var1.menu.stopDispatchingItemsChanged();
            if(var1.frozenActionViewState != null) {
               var1.menu.restoreActionViewStates(var1.frozenActionViewState);
               var1.frozenActionViewState = null;
            }

            if(!var6.onPreparePanel(0, var1.createdPanelView, var1.menu)) {
               if(var3 && this.mDecorContentParent != null) {
                  this.mDecorContentParent.setMenu((Menu)null, this.mActionMenuPresenterCallback);
               }

               var1.menu.startDispatchingItemsChanged();
               var4 = var5;
               return var4;
            }

            int var7;
            if(var2 != null) {
               var7 = var2.getDeviceId();
            } else {
               var7 = -1;
            }

            if(KeyCharacterMap.load(var7).getKeyboardType() != 1) {
               var4 = true;
            } else {
               var4 = false;
            }

            var1.qwertyMode = var4;
            var1.menu.setQwertyMode(var1.qwertyMode);
            var1.menu.startDispatchingItemsChanged();
         }

         var1.isPrepared = true;
         var1.isHandled = false;
         this.mPreparedPanel = var1;
         var4 = true;
      }

      return var4;
   }

   private void reopenMenu(MenuBuilder var1, boolean var2) {
      if(this.mDecorContentParent == null || !this.mDecorContentParent.canShowOverflowMenu() || ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext)) && !this.mDecorContentParent.isOverflowMenuShowPending()) {
         AppCompatDelegateImplV9.PanelFeatureState var5 = this.getPanelState(0, true);
         var5.refreshDecorView = true;
         this.closePanel(var5, false);
         this.openPanel(var5, (KeyEvent)null);
      } else {
         Callback var4 = this.getWindowCallback();
         if(this.mDecorContentParent.isOverflowMenuShowing() && var2) {
            this.mDecorContentParent.hideOverflowMenu();
            if(!this.isDestroyed()) {
               var4.onPanelClosed(108, this.getPanelState(0, true).menu);
            }
         } else if(var4 != null && !this.isDestroyed()) {
            if(this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
               this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
               this.mInvalidatePanelMenuRunnable.run();
            }

            AppCompatDelegateImplV9.PanelFeatureState var3 = this.getPanelState(0, true);
            if(var3.menu != null && !var3.refreshMenuContent && var4.onPreparePanel(0, var3.createdPanelView, var3.menu)) {
               var4.onMenuOpened(108, var3.menu);
               this.mDecorContentParent.showOverflowMenu();
            }
         }
      }

   }

   private int sanitizeWindowFeatureId(int var1) {
      int var2;
      if(var1 == 8) {
         Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
         var2 = 108;
      } else {
         var2 = var1;
         if(var1 == 9) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            var2 = 109;
         }
      }

      return var2;
   }

   private boolean shouldInheritContext(ViewParent var1) {
      boolean var2;
      if(var1 == null) {
         var2 = false;
      } else {
         View var3 = this.mWindow.getDecorView();

         while(true) {
            if(var1 == null) {
               var2 = true;
               break;
            }

            if(var1 == var3 || !(var1 instanceof View) || ViewCompat.isAttachedToWindow((View)var1)) {
               var2 = false;
               break;
            }

            var1 = var1.getParent();
         }
      }

      return var2;
   }

   private void throwFeatureRequestIfSubDecorInstalled() {
      if(this.mSubDecorInstalled) {
         throw new AndroidRuntimeException("Window feature must be requested before adding content");
      }
   }

   public void addContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   View callActivityOnCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      if(this.mOriginalWindowCallback instanceof Factory) {
         var1 = ((Factory)this.mOriginalWindowCallback).onCreateView(var2, var3, var4);
         if(var1 != null) {
            return var1;
         }
      }

      var1 = null;
      return var1;
   }

   void callOnPanelClosed(int var1, AppCompatDelegateImplV9.PanelFeatureState var2, Menu var3) {
      AppCompatDelegateImplV9.PanelFeatureState var6 = var2;
      Object var5 = var3;
      if(var3 == null) {
         AppCompatDelegateImplV9.PanelFeatureState var4 = var2;
         if(var2 == null) {
            var4 = var2;
            if(var1 >= 0) {
               var4 = var2;
               if(var1 < this.mPanels.length) {
                  var4 = this.mPanels[var1];
               }
            }
         }

         var6 = var4;
         var5 = var3;
         if(var4 != null) {
            var5 = var4.menu;
            var6 = var4;
         }
      }

      if((var6 == null || var6.isOpen) && !this.isDestroyed()) {
         this.mOriginalWindowCallback.onPanelClosed(var1, (Menu)var5);
      }

   }

   void checkCloseActionMenu(MenuBuilder var1) {
      if(!this.mClosingActionMenu) {
         this.mClosingActionMenu = true;
         this.mDecorContentParent.dismissPopups();
         Callback var2 = this.getWindowCallback();
         if(var2 != null && !this.isDestroyed()) {
            var2.onPanelClosed(108, var1);
         }

         this.mClosingActionMenu = false;
      }

   }

   void closePanel(int var1) {
      this.closePanel(this.getPanelState(var1, true), true);
   }

   void closePanel(AppCompatDelegateImplV9.PanelFeatureState var1, boolean var2) {
      if(var2 && var1.featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.isOverflowMenuShowing()) {
         this.checkCloseActionMenu(var1.menu);
      } else {
         WindowManager var3 = (WindowManager)this.mContext.getSystemService("window");
         if(var3 != null && var1.isOpen && var1.decorView != null) {
            var3.removeView(var1.decorView);
            if(var2) {
               this.callOnPanelClosed(var1.featureId, var1, (Menu)null);
            }
         }

         var1.isPrepared = false;
         var1.isHandled = false;
         var1.isOpen = false;
         var1.shownPanelView = null;
         var1.refreshDecorView = true;
         if(this.mPreparedPanel == var1) {
            this.mPreparedPanel = null;
         }
      }

   }

   public View createView(View var1, String var2, @NonNull Context var3, @NonNull AttributeSet var4) {
      boolean var5;
      if(VERSION.SDK_INT < 21) {
         var5 = true;
      } else {
         var5 = false;
      }

      if(this.mAppCompatViewInflater == null) {
         this.mAppCompatViewInflater = new AppCompatViewInflater();
      }

      boolean var6;
      if(var5 && this.shouldInheritContext((ViewParent)var1)) {
         var6 = true;
      } else {
         var6 = false;
      }

      return this.mAppCompatViewInflater.createView(var1, var2, var3, var4, var6, var5, true, VectorEnabledTintResources.shouldBeUsed());
   }

   void dismissPopups() {
      if(this.mDecorContentParent != null) {
         this.mDecorContentParent.dismissPopups();
      }

      if(this.mActionModePopup != null) {
         this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
         if(this.mActionModePopup.isShowing()) {
            try {
               this.mActionModePopup.dismiss();
            } catch (IllegalArgumentException var2) {
               ;
            }
         }

         this.mActionModePopup = null;
      }

      this.endOnGoingFadeAnimation();
      AppCompatDelegateImplV9.PanelFeatureState var1 = this.getPanelState(0, false);
      if(var1 != null && var1.menu != null) {
         var1.menu.close();
      }

   }

   boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var4 = true;
      if(var1.getKeyCode() != 82 || !this.mOriginalWindowCallback.dispatchKeyEvent(var1)) {
         int var3 = var1.getKeyCode();
         boolean var2;
         if(var1.getAction() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         if(var2) {
            var4 = this.onKeyDown(var3, var1);
         } else {
            var4 = this.onKeyUp(var3, var1);
         }
      }

      return var4;
   }

   void doInvalidatePanelMenu(int var1) {
      AppCompatDelegateImplV9.PanelFeatureState var2 = this.getPanelState(var1, true);
      if(var2.menu != null) {
         Bundle var3 = new Bundle();
         var2.menu.saveActionViewStates(var3);
         if(var3.size() > 0) {
            var2.frozenActionViewState = var3;
         }

         var2.menu.stopDispatchingItemsChanged();
         var2.menu.clear();
      }

      var2.refreshMenuContent = true;
      var2.refreshDecorView = true;
      if((var1 == 108 || var1 == 0) && this.mDecorContentParent != null) {
         var2 = this.getPanelState(0, false);
         if(var2 != null) {
            var2.isPrepared = false;
            this.preparePanel(var2, (KeyEvent)null);
         }
      }

   }

   void endOnGoingFadeAnimation() {
      if(this.mFadeAnim != null) {
         this.mFadeAnim.cancel();
      }

   }

   AppCompatDelegateImplV9.PanelFeatureState findMenuPanel(Menu var1) {
      AppCompatDelegateImplV9.PanelFeatureState[] var5 = this.mPanels;
      int var2;
      if(var5 != null) {
         var2 = var5.length;
      } else {
         var2 = 0;
      }

      int var3 = 0;

      AppCompatDelegateImplV9.PanelFeatureState var6;
      while(true) {
         if(var3 >= var2) {
            var6 = null;
            break;
         }

         AppCompatDelegateImplV9.PanelFeatureState var4 = var5[var3];
         if(var4 != null && var4.menu == var1) {
            var6 = var4;
            break;
         }

         ++var3;
      }

      return var6;
   }

   @Nullable
   public View findViewById(@IdRes int var1) {
      this.ensureSubDecor();
      return this.mWindow.findViewById(var1);
   }

   protected AppCompatDelegateImplV9.PanelFeatureState getPanelState(int var1, boolean var2) {
      AppCompatDelegateImplV9.PanelFeatureState[] var3;
      label22: {
         AppCompatDelegateImplV9.PanelFeatureState[] var4 = this.mPanels;
         if(var4 != null) {
            var3 = var4;
            if(var4.length > var1) {
               break label22;
            }
         }

         AppCompatDelegateImplV9.PanelFeatureState[] var5 = new AppCompatDelegateImplV9.PanelFeatureState[var1 + 1];
         if(var4 != null) {
            System.arraycopy(var4, 0, var5, 0, var4.length);
         }

         var3 = var5;
         this.mPanels = var5;
      }

      AppCompatDelegateImplV9.PanelFeatureState var6 = var3[var1];
      AppCompatDelegateImplV9.PanelFeatureState var7 = var6;
      if(var6 == null) {
         var7 = new AppCompatDelegateImplV9.PanelFeatureState(var1);
         var3[var1] = var7;
      }

      return var7;
   }

   ViewGroup getSubDecor() {
      return this.mSubDecor;
   }

   public boolean hasWindowFeature(int var1) {
      var1 = this.sanitizeWindowFeatureId(var1);
      boolean var2;
      switch(var1) {
      case 1:
         var2 = this.mWindowNoTitle;
         break;
      case 2:
         var2 = this.mFeatureProgress;
         break;
      case 5:
         var2 = this.mFeatureIndeterminateProgress;
         break;
      case 10:
         var2 = this.mOverlayActionMode;
         break;
      case 108:
         var2 = this.mHasActionBar;
         break;
      case 109:
         var2 = this.mOverlayActionBar;
         break;
      default:
         var2 = this.mWindow.hasFeature(var1);
      }

      return var2;
   }

   public void initWindowDecorActionBar() {
      this.ensureSubDecor();
      if(this.mHasActionBar && this.mActionBar == null) {
         if(this.mOriginalWindowCallback instanceof Activity) {
            this.mActionBar = new WindowDecorActionBar((Activity)this.mOriginalWindowCallback, this.mOverlayActionBar);
         } else if(this.mOriginalWindowCallback instanceof Dialog) {
            this.mActionBar = new WindowDecorActionBar((Dialog)this.mOriginalWindowCallback);
         }

         if(this.mActionBar != null) {
            this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
         }
      }

   }

   public void installViewFactory() {
      LayoutInflater var1 = LayoutInflater.from(this.mContext);
      if(var1.getFactory() == null) {
         LayoutInflaterCompat.setFactory(var1, this);
      } else if(!(LayoutInflaterCompat.getFactory(var1) instanceof AppCompatDelegateImplV9)) {
         Log.i("AppCompatDelegate", "The Activity\'s LayoutInflater already has a Factory installed so we can not install AppCompat\'s");
      }

   }

   public void invalidateOptionsMenu() {
      ActionBar var1 = this.getSupportActionBar();
      if(var1 == null || !var1.invalidateOptionsMenu()) {
         this.invalidatePanelMenu(0);
      }

   }

   boolean onBackPressed() {
      boolean var1 = true;
      if(this.mActionMode != null) {
         this.mActionMode.finish();
      } else {
         ActionBar var2 = this.getSupportActionBar();
         if(var2 == null || !var2.collapseActionView()) {
            var1 = false;
         }
      }

      return var1;
   }

   public void onConfigurationChanged(Configuration var1) {
      if(this.mHasActionBar && this.mSubDecorInstalled) {
         ActionBar var2 = this.getSupportActionBar();
         if(var2 != null) {
            var2.onConfigurationChanged(var1);
         }
      }

      AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
      this.applyDayNight();
   }

   public void onCreate(Bundle var1) {
      if(this.mOriginalWindowCallback instanceof Activity && NavUtils.getParentActivityName((Activity)this.mOriginalWindowCallback) != null) {
         ActionBar var2 = this.peekSupportActionBar();
         if(var2 == null) {
            this.mEnableDefaultActionBarUp = true;
         } else {
            var2.setDefaultDisplayHomeAsUpEnabled(true);
         }
      }

   }

   public final View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      View var5 = this.callActivityOnCreateView(var1, var2, var3, var4);
      if(var5 != null) {
         var1 = var5;
      } else {
         var1 = this.createView(var1, var2, var3, var4);
      }

      return var1;
   }

   public void onDestroy() {
      if(this.mInvalidatePanelMenuPosted) {
         this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
      }

      super.onDestroy();
      if(this.mActionBar != null) {
         this.mActionBar.onDestroy();
      }

   }

   boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var4 = true;
      boolean var3 = true;
      switch(var1) {
      case 4:
         if((var2.getFlags() & 128) != 0) {
            var3 = var4;
         } else {
            var3 = false;
         }

         this.mLongPressBackDown = var3;
      default:
         if(VERSION.SDK_INT < 11) {
            this.onKeyShortcut(var1, var2);
         }

         var3 = false;
         break;
      case 82:
         this.onKeyDownPanel(0, var2);
      }

      return var3;
   }

   boolean onKeyShortcut(int var1, KeyEvent var2) {
      boolean var4 = true;
      ActionBar var6 = this.getSupportActionBar();
      boolean var3;
      if(var6 != null && var6.onKeyShortcut(var1, var2)) {
         var3 = var4;
      } else if(this.mPreparedPanel != null && this.performPanelShortcut(this.mPreparedPanel, var2.getKeyCode(), var2, 1)) {
         var3 = var4;
         if(this.mPreparedPanel != null) {
            this.mPreparedPanel.isHandled = true;
            var3 = var4;
         }
      } else {
         if(this.mPreparedPanel == null) {
            AppCompatDelegateImplV9.PanelFeatureState var7 = this.getPanelState(0, true);
            this.preparePanel(var7, var2);
            boolean var5 = this.performPanelShortcut(var7, var2.getKeyCode(), var2, 1);
            var7.isPrepared = false;
            var3 = var4;
            if(var5) {
               return var3;
            }
         }

         var3 = false;
      }

      return var3;
   }

   boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var4 = true;
      boolean var3;
      switch(var1) {
      case 4:
         boolean var5 = this.mLongPressBackDown;
         this.mLongPressBackDown = false;
         AppCompatDelegateImplV9.PanelFeatureState var6 = this.getPanelState(0, false);
         if(var6 != null && var6.isOpen) {
            var3 = var4;
            if(!var5) {
               this.closePanel(var6, true);
               var3 = var4;
            }
            break;
         } else if(this.onBackPressed()) {
            var3 = var4;
            break;
         }
      default:
         var3 = false;
         break;
      case 82:
         this.onKeyUpPanel(0, var2);
         var3 = var4;
      }

      return var3;
   }

   public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      Callback var4 = this.getWindowCallback();
      boolean var3;
      if(var4 != null && !this.isDestroyed()) {
         AppCompatDelegateImplV9.PanelFeatureState var5 = this.findMenuPanel(var1.getRootMenu());
         if(var5 != null) {
            var3 = var4.onMenuItemSelected(var5.featureId, var2);
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   public void onMenuModeChange(MenuBuilder var1) {
      this.reopenMenu(var1, true);
   }

   boolean onMenuOpened(int var1, Menu var2) {
      boolean var4 = true;
      boolean var3;
      if(var1 == 108) {
         ActionBar var5 = this.getSupportActionBar();
         var3 = var4;
         if(var5 != null) {
            var5.dispatchMenuVisibilityChanged(true);
            var3 = var4;
         }
      } else {
         var3 = false;
      }

      return var3;
   }

   void onPanelClosed(int var1, Menu var2) {
      if(var1 == 108) {
         ActionBar var3 = this.getSupportActionBar();
         if(var3 != null) {
            var3.dispatchMenuVisibilityChanged(false);
         }
      } else if(var1 == 0) {
         AppCompatDelegateImplV9.PanelFeatureState var4 = this.getPanelState(var1, true);
         if(var4.isOpen) {
            this.closePanel(var4, false);
         }
      }

   }

   public void onPostCreate(Bundle var1) {
      this.ensureSubDecor();
   }

   public void onPostResume() {
      ActionBar var1 = this.getSupportActionBar();
      if(var1 != null) {
         var1.setShowHideAnimationEnabled(true);
      }

   }

   public void onStop() {
      ActionBar var1 = this.getSupportActionBar();
      if(var1 != null) {
         var1.setShowHideAnimationEnabled(false);
      }

   }

   void onSubDecorInstalled(ViewGroup var1) {
   }

   void onTitleChanged(CharSequence var1) {
      if(this.mDecorContentParent != null) {
         this.mDecorContentParent.setWindowTitle(var1);
      } else if(this.peekSupportActionBar() != null) {
         this.peekSupportActionBar().setWindowTitle(var1);
      } else if(this.mTitleView != null) {
         this.mTitleView.setText(var1);
      }

   }

   public boolean requestWindowFeature(int var1) {
      boolean var2 = false;
      var1 = this.sanitizeWindowFeatureId(var1);
      if(!this.mWindowNoTitle || var1 != 108) {
         if(this.mHasActionBar && var1 == 1) {
            this.mHasActionBar = false;
         }

         switch(var1) {
         case 1:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mWindowNoTitle = true;
            var2 = true;
            break;
         case 2:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureProgress = true;
            var2 = true;
            break;
         case 5:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureIndeterminateProgress = true;
            var2 = true;
            break;
         case 10:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionMode = true;
            var2 = true;
            break;
         case 108:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mHasActionBar = true;
            var2 = true;
            break;
         case 109:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionBar = true;
            var2 = true;
            break;
         default:
            var2 = this.mWindow.requestFeature(var1);
         }
      }

      return var2;
   }

   public void setContentView(int var1) {
      this.ensureSubDecor();
      ViewGroup var2 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var2.removeAllViews();
      LayoutInflater.from(this.mContext).inflate(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setContentView(View var1) {
      this.ensureSubDecor();
      ViewGroup var2 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var2.removeAllViews();
      var2.addView(var1);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ViewGroup var3 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var3.removeAllViews();
      var3.addView(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setSupportActionBar(Toolbar var1) {
      if(this.mOriginalWindowCallback instanceof Activity) {
         ActionBar var2 = this.getSupportActionBar();
         if(var2 instanceof WindowDecorActionBar) {
            throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
         }

         this.mMenuInflater = null;
         if(var2 != null) {
            var2.onDestroy();
         }

         if(var1 != null) {
            ToolbarActionBar var3 = new ToolbarActionBar(var1, ((Activity)this.mContext).getTitle(), this.mAppCompatWindowCallback);
            this.mActionBar = var3;
            this.mWindow.setCallback(var3.getWrappedWindowCallback());
         } else {
            this.mActionBar = null;
            this.mWindow.setCallback(this.mAppCompatWindowCallback);
         }

         this.invalidateOptionsMenu();
      }

   }

   final boolean shouldAnimateActionModeView() {
      boolean var1;
      if(this.mSubDecorInstalled && this.mSubDecor != null && ViewCompat.isLaidOut(this.mSubDecor)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public ActionMode startSupportActionMode(@NonNull ActionMode.Callback var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("ActionMode callback can not be null.");
      } else {
         if(this.mActionMode != null) {
            this.mActionMode.finish();
         }

         AppCompatDelegateImplV9.ActionModeCallbackWrapperV9 var2 = new AppCompatDelegateImplV9.ActionModeCallbackWrapperV9(var1);
         ActionBar var3 = this.getSupportActionBar();
         if(var3 != null) {
            this.mActionMode = var3.startActionMode(var2);
            if(this.mActionMode != null && this.mAppCompatCallback != null) {
               this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
            }
         }

         if(this.mActionMode == null) {
            this.mActionMode = this.startSupportActionModeFromWindow(var2);
         }

         return this.mActionMode;
      }
   }

   ActionMode startSupportActionModeFromWindow(@NonNull ActionMode.Callback var1) {
      this.endOnGoingFadeAnimation();
      if(this.mActionMode != null) {
         this.mActionMode.finish();
      }

      Object var4 = var1;
      if(!(var1 instanceof AppCompatDelegateImplV9.ActionModeCallbackWrapperV9)) {
         var4 = new AppCompatDelegateImplV9.ActionModeCallbackWrapperV9(var1);
      }

      TypedValue var5 = null;
      ActionMode var8 = var5;
      if(this.mAppCompatCallback != null) {
         var8 = var5;
         if(!this.isDestroyed()) {
            try {
               var8 = this.mAppCompatCallback.onWindowStartingSupportActionMode((ActionMode.Callback)var4);
            } catch (AbstractMethodError var7) {
               var8 = var5;
            }
         }
      }

      if(var8 != null) {
         this.mActionMode = var8;
      } else {
         if(this.mActionModeView == null) {
            if(this.mIsFloating) {
               var5 = new TypedValue();
               Theme var9 = this.mContext.getTheme();
               var9.resolveAttribute(R.attr.actionBarTheme, var5, true);
               Object var10;
               if(var5.resourceId != 0) {
                  Theme var6 = this.mContext.getResources().newTheme();
                  var6.setTo(var9);
                  var6.applyStyle(var5.resourceId, true);
                  var10 = new ContextThemeWrapper(this.mContext, 0);
                  ((Context)var10).getTheme().setTo(var6);
               } else {
                  var10 = this.mContext;
               }

               this.mActionModeView = new ActionBarContextView((Context)var10);
               this.mActionModePopup = new PopupWindow((Context)var10, (AttributeSet)null, R.attr.actionModePopupWindowStyle);
               PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
               this.mActionModePopup.setContentView(this.mActionModeView);
               this.mActionModePopup.setWidth(-1);
               ((Context)var10).getTheme().resolveAttribute(R.attr.actionBarSize, var5, true);
               int var2 = TypedValue.complexToDimensionPixelSize(var5.data, ((Context)var10).getResources().getDisplayMetrics());
               this.mActionModeView.setContentHeight(var2);
               this.mActionModePopup.setHeight(-2);
               this.mShowActionModePopup = new Runnable() {
                  public void run() {
                     AppCompatDelegateImplV9.this.mActionModePopup.showAtLocation(AppCompatDelegateImplV9.this.mActionModeView, 55, 0, 0);
                     AppCompatDelegateImplV9.this.endOnGoingFadeAnimation();
                     if(AppCompatDelegateImplV9.this.shouldAnimateActionModeView()) {
                        ViewCompat.setAlpha(AppCompatDelegateImplV9.this.mActionModeView, 0.0F);
                        AppCompatDelegateImplV9.this.mFadeAnim = ViewCompat.animate(AppCompatDelegateImplV9.this.mActionModeView).alpha(1.0F);
                        AppCompatDelegateImplV9.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                           public void onAnimationEnd(View var1) {
                              ViewCompat.setAlpha(AppCompatDelegateImplV9.this.mActionModeView, 1.0F);
                              AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                              AppCompatDelegateImplV9.this.mFadeAnim = null;
                           }

                           public void onAnimationStart(View var1) {
                              AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                           }
                        });
                     } else {
                        ViewCompat.setAlpha(AppCompatDelegateImplV9.this.mActionModeView, 1.0F);
                        AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                     }

                  }
               };
            } else {
               ViewStubCompat var11 = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
               if(var11 != null) {
                  var11.setLayoutInflater(LayoutInflater.from(this.getActionBarThemedContext()));
                  this.mActionModeView = (ActionBarContextView)var11.inflate();
               }
            }
         }

         if(this.mActionModeView != null) {
            this.endOnGoingFadeAnimation();
            this.mActionModeView.killMode();
            Context var12 = this.mActionModeView.getContext();
            ActionBarContextView var14 = this.mActionModeView;
            boolean var3;
            if(this.mActionModePopup == null) {
               var3 = true;
            } else {
               var3 = false;
            }

            StandaloneActionMode var13 = new StandaloneActionMode(var12, var14, (ActionMode.Callback)var4, var3);
            if(((ActionMode.Callback)var4).onCreateActionMode(var13, var13.getMenu())) {
               var13.invalidate();
               this.mActionModeView.initForMode(var13);
               this.mActionMode = var13;
               if(this.shouldAnimateActionModeView()) {
                  ViewCompat.setAlpha(this.mActionModeView, 0.0F);
                  this.mFadeAnim = ViewCompat.animate(this.mActionModeView).alpha(1.0F);
                  this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                     public void onAnimationEnd(View var1) {
                        ViewCompat.setAlpha(AppCompatDelegateImplV9.this.mActionModeView, 1.0F);
                        AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                        AppCompatDelegateImplV9.this.mFadeAnim = null;
                     }

                     public void onAnimationStart(View var1) {
                        AppCompatDelegateImplV9.this.mActionModeView.setVisibility(0);
                        AppCompatDelegateImplV9.this.mActionModeView.sendAccessibilityEvent(32);
                        if(AppCompatDelegateImplV9.this.mActionModeView.getParent() instanceof View) {
                           ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV9.this.mActionModeView.getParent());
                        }

                     }
                  });
               } else {
                  ViewCompat.setAlpha(this.mActionModeView, 1.0F);
                  this.mActionModeView.setVisibility(0);
                  this.mActionModeView.sendAccessibilityEvent(32);
                  if(this.mActionModeView.getParent() instanceof View) {
                     ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent());
                  }
               }

               if(this.mActionModePopup != null) {
                  this.mWindow.getDecorView().post(this.mShowActionModePopup);
               }
            } else {
               this.mActionMode = null;
            }
         }
      }

      if(this.mActionMode != null && this.mAppCompatCallback != null) {
         this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
      }

      return this.mActionMode;
   }

   int updateStatusGuard(int var1) {
      byte var7 = 0;
      boolean var3 = false;
      boolean var8 = false;
      boolean var6 = var3;
      int var2 = var1;
      if(this.mActionModeView != null) {
         var6 = var3;
         var2 = var1;
         if(this.mActionModeView.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams var9 = (MarginLayoutParams)this.mActionModeView.getLayoutParams();
            var3 = false;
            boolean var13 = false;
            boolean var4;
            int var5;
            if(this.mActionModeView.isShown()) {
               if(this.mTempRect1 == null) {
                  this.mTempRect1 = new Rect();
                  this.mTempRect2 = new Rect();
               }

               Rect var10 = this.mTempRect1;
               Rect var11 = this.mTempRect2;
               var10.set(0, var1, 0, 0);
               ViewUtils.computeFitSystemWindows(this.mSubDecor, var10, var11);
               int var14;
               if(var11.top == 0) {
                  var14 = var1;
               } else {
                  var14 = 0;
               }

               if(var9.topMargin != var14) {
                  var3 = true;
                  var9.topMargin = var1;
                  if(this.mStatusGuard == null) {
                     this.mStatusGuard = new View(this.mContext);
                     this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
                     this.mSubDecor.addView(this.mStatusGuard, -1, new LayoutParams(-1, var1));
                     var13 = var3;
                  } else {
                     LayoutParams var16 = this.mStatusGuard.getLayoutParams();
                     var13 = var3;
                     if(var16.height != var1) {
                        var16.height = var1;
                        this.mStatusGuard.setLayoutParams(var16);
                        var13 = var3;
                     }
                  }
               }

               if(this.mStatusGuard != null) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var3 = var13;
               var4 = var6;
               var5 = var1;
               if(!this.mOverlayActionMode) {
                  var3 = var13;
                  var4 = var6;
                  var5 = var1;
                  if(var6) {
                     var5 = 0;
                     var4 = var6;
                     var3 = var13;
                  }
               }
            } else {
               var4 = var8;
               var5 = var1;
               if(var9.topMargin != 0) {
                  var3 = true;
                  var9.topMargin = 0;
                  var4 = var8;
                  var5 = var1;
               }
            }

            var6 = var4;
            var2 = var5;
            if(var3) {
               this.mActionModeView.setLayoutParams(var9);
               var2 = var5;
               var6 = var4;
            }
         }
      }

      if(this.mStatusGuard != null) {
         View var15 = this.mStatusGuard;
         byte var12;
         if(var6) {
            var12 = var7;
         } else {
            var12 = 8;
         }

         var15.setVisibility(var12);
      }

      return var2;
   }

   private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         AppCompatDelegateImplV9.this.checkCloseActionMenu(var1);
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         Callback var2 = AppCompatDelegateImplV9.this.getWindowCallback();
         if(var2 != null) {
            var2.onMenuOpened(108, var1);
         }

         return true;
      }
   }

   class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
      private ActionMode.Callback mWrapped;

      public ActionModeCallbackWrapperV9(ActionMode.Callback var2) {
         this.mWrapped = var2;
      }

      public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
         return this.mWrapped.onActionItemClicked(var1, var2);
      }

      public boolean onCreateActionMode(ActionMode var1, Menu var2) {
         return this.mWrapped.onCreateActionMode(var1, var2);
      }

      public void onDestroyActionMode(ActionMode var1) {
         this.mWrapped.onDestroyActionMode(var1);
         if(AppCompatDelegateImplV9.this.mActionModePopup != null) {
            AppCompatDelegateImplV9.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImplV9.this.mShowActionModePopup);
         }

         if(AppCompatDelegateImplV9.this.mActionModeView != null) {
            AppCompatDelegateImplV9.this.endOnGoingFadeAnimation();
            AppCompatDelegateImplV9.this.mFadeAnim = ViewCompat.animate(AppCompatDelegateImplV9.this.mActionModeView).alpha(0.0F);
            AppCompatDelegateImplV9.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
               public void onAnimationEnd(View var1) {
                  AppCompatDelegateImplV9.this.mActionModeView.setVisibility(8);
                  if(AppCompatDelegateImplV9.this.mActionModePopup != null) {
                     AppCompatDelegateImplV9.this.mActionModePopup.dismiss();
                  } else if(AppCompatDelegateImplV9.this.mActionModeView.getParent() instanceof View) {
                     ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV9.this.mActionModeView.getParent());
                  }

                  AppCompatDelegateImplV9.this.mActionModeView.removeAllViews();
                  AppCompatDelegateImplV9.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                  AppCompatDelegateImplV9.this.mFadeAnim = null;
               }
            });
         }

         if(AppCompatDelegateImplV9.this.mAppCompatCallback != null) {
            AppCompatDelegateImplV9.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImplV9.this.mActionMode);
         }

         AppCompatDelegateImplV9.this.mActionMode = null;
      }

      public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
         return this.mWrapped.onPrepareActionMode(var1, var2);
      }
   }

   private class ListMenuDecorView extends ContentFrameLayout {
      public ListMenuDecorView(Context var2) {
         super(var2);
      }

      private boolean isOutOfBounds(int var1, int var2) {
         boolean var3;
         if(var1 >= -5 && var2 >= -5 && var1 <= this.getWidth() + 5 && var2 <= this.getHeight() + 5) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         boolean var2;
         if(!AppCompatDelegateImplV9.this.dispatchKeyEvent(var1) && !super.dispatchKeyEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         boolean var2;
         if(var1.getAction() == 0 && this.isOutOfBounds((int)var1.getX(), (int)var1.getY())) {
            AppCompatDelegateImplV9.this.closePanel(0);
            var2 = true;
         } else {
            var2 = super.onInterceptTouchEvent(var1);
         }

         return var2;
      }

      public void setBackgroundResource(int var1) {
         this.setBackgroundDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
      }
   }

   protected static final class PanelFeatureState {
      int background;
      View createdPanelView;
      ViewGroup decorView;
      int featureId;
      Bundle frozenActionViewState;
      Bundle frozenMenuState;
      int gravity;
      boolean isHandled;
      boolean isOpen;
      boolean isPrepared;
      ListMenuPresenter listMenuPresenter;
      Context listPresenterContext;
      MenuBuilder menu;
      public boolean qwertyMode;
      boolean refreshDecorView;
      boolean refreshMenuContent;
      View shownPanelView;
      boolean wasLastOpen;
      int windowAnimations;
      int x;
      int y;

      PanelFeatureState(int var1) {
         this.featureId = var1;
         this.refreshDecorView = false;
      }

      void applyFrozenState() {
         if(this.menu != null && this.frozenMenuState != null) {
            this.menu.restorePresenterStates(this.frozenMenuState);
            this.frozenMenuState = null;
         }

      }

      public void clearMenuPresenters() {
         if(this.menu != null) {
            this.menu.removeMenuPresenter(this.listMenuPresenter);
         }

         this.listMenuPresenter = null;
      }

      MenuView getListMenuView(MenuPresenter.Callback var1) {
         MenuView var2;
         if(this.menu == null) {
            var2 = null;
         } else {
            if(this.listMenuPresenter == null) {
               this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
               this.listMenuPresenter.setCallback(var1);
               this.menu.addMenuPresenter(this.listMenuPresenter);
            }

            var2 = this.listMenuPresenter.getMenuView(this.decorView);
         }

         return var2;
      }

      public boolean hasPanelItems() {
         boolean var2 = true;
         boolean var1;
         if(this.shownPanelView == null) {
            var1 = false;
         } else {
            var1 = var2;
            if(this.createdPanelView == null) {
               var1 = var2;
               if(this.listMenuPresenter.getAdapter().getCount() <= 0) {
                  var1 = false;
               }
            }
         }

         return var1;
      }

      void onRestoreInstanceState(Parcelable var1) {
         AppCompatDelegateImplV9.SavedState var2 = (AppCompatDelegateImplV9.SavedState)var1;
         this.featureId = var2.featureId;
         this.wasLastOpen = var2.isOpen;
         this.frozenMenuState = var2.menuState;
         this.shownPanelView = null;
         this.decorView = null;
      }

      Parcelable onSaveInstanceState() {
         AppCompatDelegateImplV9.SavedState var1 = new AppCompatDelegateImplV9.SavedState();
         var1.featureId = this.featureId;
         var1.isOpen = this.isOpen;
         if(this.menu != null) {
            var1.menuState = new Bundle();
            this.menu.savePresenterStates(var1.menuState);
         }

         return var1;
      }

      void setMenu(MenuBuilder var1) {
         if(var1 != this.menu) {
            if(this.menu != null) {
               this.menu.removeMenuPresenter(this.listMenuPresenter);
            }

            this.menu = var1;
            if(var1 != null && this.listMenuPresenter != null) {
               var1.addMenuPresenter(this.listMenuPresenter);
            }
         }

      }

      void setStyle(Context var1) {
         TypedValue var3 = new TypedValue();
         Theme var2 = var1.getResources().newTheme();
         var2.setTo(var1.getTheme());
         var2.resolveAttribute(R.attr.actionBarPopupTheme, var3, true);
         if(var3.resourceId != 0) {
            var2.applyStyle(var3.resourceId, true);
         }

         var2.resolveAttribute(R.attr.panelMenuListTheme, var3, true);
         if(var3.resourceId != 0) {
            var2.applyStyle(var3.resourceId, true);
         } else {
            var2.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
         }

         ContextThemeWrapper var4 = new ContextThemeWrapper(var1, 0);
         var4.getTheme().setTo(var2);
         this.listPresenterContext = var4;
         TypedArray var5 = var4.obtainStyledAttributes(R.styleable.AppCompatTheme);
         this.background = var5.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
         this.windowAnimations = var5.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
         var5.recycle();
      }
   }

   private static class SavedState implements Parcelable {
      public static final Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
         public AppCompatDelegateImplV9.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return AppCompatDelegateImplV9.SavedState.readFromParcel(var1, var2);
         }

         public AppCompatDelegateImplV9.SavedState[] newArray(int var1) {
            return new AppCompatDelegateImplV9.SavedState[var1];
         }
      });
      int featureId;
      boolean isOpen;
      Bundle menuState;

      static AppCompatDelegateImplV9.SavedState readFromParcel(Parcel var0, ClassLoader var1) {
         boolean var2 = true;
         AppCompatDelegateImplV9.SavedState var3 = new AppCompatDelegateImplV9.SavedState();
         var3.featureId = var0.readInt();
         if(var0.readInt() != 1) {
            var2 = false;
         }

         var3.isOpen = var2;
         if(var3.isOpen) {
            var3.menuState = var0.readBundle(var1);
         }

         return var3;
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.featureId);
         byte var3;
         if(this.isOpen) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         var1.writeInt(var3);
         if(this.isOpen) {
            var1.writeBundle(this.menuState);
         }

      }
   }

   private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         MenuBuilder var4 = var1.getRootMenu();
         boolean var3;
         if(var4 != var1) {
            var3 = true;
         } else {
            var3 = false;
         }

         AppCompatDelegateImplV9 var5 = AppCompatDelegateImplV9.this;
         if(var3) {
            var1 = var4;
         }

         AppCompatDelegateImplV9.PanelFeatureState var6 = var5.findMenuPanel(var1);
         if(var6 != null) {
            if(var3) {
               AppCompatDelegateImplV9.this.callOnPanelClosed(var6.featureId, var6, var4);
               AppCompatDelegateImplV9.this.closePanel(var6, true);
            } else {
               AppCompatDelegateImplV9.this.closePanel(var6, var2);
            }
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         if(var1 == null && AppCompatDelegateImplV9.this.mHasActionBar) {
            Callback var2 = AppCompatDelegateImplV9.this.getWindowCallback();
            if(var2 != null && !AppCompatDelegateImplV9.this.isDestroyed()) {
               var2.onMenuOpened(108, var1);
            }
         }

         return true;
      }
   }
}
