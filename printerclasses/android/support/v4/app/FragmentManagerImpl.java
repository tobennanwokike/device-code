package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.v4.app.BackStackRecord;
import android.support.v4.app.BackStackState;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.app.FragmentManagerState;
import android.support.v4.app.FragmentState;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NoSaveStateFrameLayout;
import android.support.v4.app.SuperNotCalledException;
import android.support.v4.os.BuildCompat;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory {
   static final Interpolator ACCELERATE_CUBIC;
   static final Interpolator ACCELERATE_QUINT;
   static final int ANIM_DUR = 220;
   public static final int ANIM_STYLE_CLOSE_ENTER = 3;
   public static final int ANIM_STYLE_CLOSE_EXIT = 4;
   public static final int ANIM_STYLE_FADE_ENTER = 5;
   public static final int ANIM_STYLE_FADE_EXIT = 6;
   public static final int ANIM_STYLE_OPEN_ENTER = 1;
   public static final int ANIM_STYLE_OPEN_EXIT = 2;
   static boolean DEBUG;
   static final Interpolator DECELERATE_CUBIC;
   static final Interpolator DECELERATE_QUINT;
   static final boolean HONEYCOMB;
   static final String TAG = "FragmentManager";
   static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
   static final String TARGET_STATE_TAG = "android:target_state";
   static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
   static final String VIEW_STATE_TAG = "android:view_state";
   static Field sAnimationListenerField;
   ArrayList mActive;
   ArrayList mAdded;
   ArrayList mAvailBackStackIndices;
   ArrayList mAvailIndices;
   ArrayList mBackStack;
   ArrayList mBackStackChangeListeners;
   ArrayList mBackStackIndices;
   FragmentContainer mContainer;
   FragmentController mController;
   ArrayList mCreatedMenus;
   int mCurState = 0;
   boolean mDestroyed;
   Runnable mExecCommit = new Runnable() {
      public void run() {
         FragmentManagerImpl.this.execPendingActions();
      }
   };
   boolean mExecutingActions;
   boolean mHavePendingDeferredStart;
   FragmentHostCallback mHost;
   boolean mNeedMenuInvalidate;
   String mNoTransactionsBecause;
   Fragment mParent;
   ArrayList mPendingActions;
   SparseArray mStateArray = null;
   Bundle mStateBundle = null;
   boolean mStateSaved;
   Runnable[] mTmpActions;

   static {
      boolean var0 = false;
      DEBUG = false;
      if(VERSION.SDK_INT >= 11) {
         var0 = true;
      }

      HONEYCOMB = var0;
      sAnimationListenerField = null;
      DECELERATE_QUINT = new DecelerateInterpolator(2.5F);
      DECELERATE_CUBIC = new DecelerateInterpolator(1.5F);
      ACCELERATE_QUINT = new AccelerateInterpolator(2.5F);
      ACCELERATE_CUBIC = new AccelerateInterpolator(1.5F);
   }

   private void checkStateLoss() {
      if(this.mStateSaved) {
         throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
      } else if(this.mNoTransactionsBecause != null) {
         throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
      }
   }

   static Animation makeFadeAnimation(Context var0, float var1, float var2) {
      AlphaAnimation var3 = new AlphaAnimation(var1, var2);
      var3.setInterpolator(DECELERATE_CUBIC);
      var3.setDuration(220L);
      return var3;
   }

   static Animation makeOpenCloseAnimation(Context var0, float var1, float var2, float var3, float var4) {
      AnimationSet var7 = new AnimationSet(false);
      ScaleAnimation var5 = new ScaleAnimation(var1, var2, var1, var2, 1, 0.5F, 1, 0.5F);
      var5.setInterpolator(DECELERATE_QUINT);
      var5.setDuration(220L);
      var7.addAnimation(var5);
      AlphaAnimation var6 = new AlphaAnimation(var3, var4);
      var6.setInterpolator(DECELERATE_CUBIC);
      var6.setDuration(220L);
      var7.addAnimation(var6);
      return var7;
   }

   static boolean modifiesAlpha(Animation var0) {
      boolean var3 = true;
      boolean var2;
      if(var0 instanceof AlphaAnimation) {
         var2 = var3;
      } else {
         if(var0 instanceof AnimationSet) {
            List var4 = ((AnimationSet)var0).getAnimations();

            for(int var1 = 0; var1 < var4.size(); ++var1) {
               var2 = var3;
               if(var4.get(var1) instanceof AlphaAnimation) {
                  return var2;
               }
            }
         }

         var2 = false;
      }

      return var2;
   }

   public static int reverseTransit(int var0) {
      byte var1 = 0;
      short var2;
      switch(var0) {
      case 4097:
         var2 = 8194;
         break;
      case 4099:
         var2 = 4099;
         break;
      case 8194:
         var2 = 4097;
         break;
      default:
         var2 = var1;
      }

      return var2;
   }

   private void setHWLayerAnimListenerIfAlpha(View var1, Animation var2) {
      if(var1 != null && var2 != null && shouldRunOnHWLayer(var1, var2)) {
         AnimationListener var3 = null;

         label27: {
            AnimationListener var4;
            try {
               if(sAnimationListenerField == null) {
                  sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                  sAnimationListenerField.setAccessible(true);
               }

               var4 = (AnimationListener)sAnimationListenerField.get(var2);
            } catch (NoSuchFieldException var5) {
               Log.e("FragmentManager", "No field with the name mListener is found in Animation class", var5);
               break label27;
            } catch (IllegalAccessException var6) {
               Log.e("FragmentManager", "Cannot access Animation\'s mListener field", var6);
               break label27;
            }

            var3 = var4;
         }

         ViewCompat.setLayerType(var1, 2, (Paint)null);
         var2.setAnimationListener(new FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(var1, var2, var3));
      }

   }

   static boolean shouldRunOnHWLayer(View var0, Animation var1) {
      boolean var2;
      if(VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(var0) == 0 && ViewCompat.hasOverlappingRendering(var0) && modifiesAlpha(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private void throwException(RuntimeException var1) {
      Log.e("FragmentManager", var1.getMessage());
      Log.e("FragmentManager", "Activity state:");
      PrintWriter var2 = new PrintWriter(new LogWriter("FragmentManager"));
      if(this.mHost != null) {
         try {
            this.mHost.onDump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var4) {
            Log.e("FragmentManager", "Failed dumping state", var4);
         }
      } else {
         try {
            this.dump("  ", (FileDescriptor)null, var2, new String[0]);
         } catch (Exception var3) {
            Log.e("FragmentManager", "Failed dumping state", var3);
         }
      }

      throw var1;
   }

   public static int transitToStyleIndex(int var0, boolean var1) {
      byte var2 = -1;
      byte var3;
      switch(var0) {
      case 4097:
         if(var1) {
            var3 = 1;
         } else {
            var3 = 2;
         }
         break;
      case 4099:
         if(var1) {
            var3 = 5;
         } else {
            var3 = 6;
         }
         break;
      case 8194:
         if(var1) {
            var3 = 3;
         } else {
            var3 = 4;
         }
         break;
      default:
         var3 = var2;
      }

      return var3;
   }

   void addBackStackState(BackStackRecord var1) {
      if(this.mBackStack == null) {
         this.mBackStack = new ArrayList();
      }

      this.mBackStack.add(var1);
      this.reportBackStackChanged();
   }

   public void addFragment(Fragment var1, boolean var2) {
      if(this.mAdded == null) {
         this.mAdded = new ArrayList();
      }

      if(DEBUG) {
         Log.v("FragmentManager", "add: " + var1);
      }

      this.makeActive(var1);
      if(!var1.mDetached) {
         if(this.mAdded.contains(var1)) {
            throw new IllegalStateException("Fragment already added: " + var1);
         }

         this.mAdded.add(var1);
         var1.mAdded = true;
         var1.mRemoving = false;
         if(var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         if(var2) {
            this.moveToState(var1);
         }
      }

   }

   public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      if(this.mBackStackChangeListeners == null) {
         this.mBackStackChangeListeners = new ArrayList();
      }

      this.mBackStackChangeListeners.add(var1);
   }

   public int allocBackStackIndex(BackStackRecord param1) {
      // $FF: Couldn't be decompiled
   }

   public void attachController(FragmentHostCallback var1, FragmentContainer var2, Fragment var3) {
      if(this.mHost != null) {
         throw new IllegalStateException("Already attached");
      } else {
         this.mHost = var1;
         this.mContainer = var2;
         this.mParent = var3;
      }
   }

   public void attachFragment(Fragment var1, int var2, int var3) {
      if(DEBUG) {
         Log.v("FragmentManager", "attach: " + var1);
      }

      if(var1.mDetached) {
         var1.mDetached = false;
         if(!var1.mAdded) {
            if(this.mAdded == null) {
               this.mAdded = new ArrayList();
            }

            if(this.mAdded.contains(var1)) {
               throw new IllegalStateException("Fragment already added: " + var1);
            }

            if(DEBUG) {
               Log.v("FragmentManager", "add from attach: " + var1);
            }

            this.mAdded.add(var1);
            var1.mAdded = true;
            if(var1.mHasMenu && var1.mMenuVisible) {
               this.mNeedMenuInvalidate = true;
            }

            this.moveToState(var1, this.mCurState, var2, var3, false);
         }
      }

   }

   public FragmentTransaction beginTransaction() {
      return new BackStackRecord(this);
   }

   public void detachFragment(Fragment var1, int var2, int var3) {
      if(DEBUG) {
         Log.v("FragmentManager", "detach: " + var1);
      }

      if(!var1.mDetached) {
         var1.mDetached = true;
         if(var1.mAdded) {
            if(this.mAdded != null) {
               if(DEBUG) {
                  Log.v("FragmentManager", "remove from detach: " + var1);
               }

               this.mAdded.remove(var1);
            }

            if(var1.mHasMenu && var1.mMenuVisible) {
               this.mNeedMenuInvalidate = true;
            }

            var1.mAdded = false;
            this.moveToState(var1, 1, var2, var3, false);
         }
      }

   }

   public void dispatchActivityCreated() {
      this.mStateSaved = false;
      this.moveToState(2, false);
   }

   public void dispatchConfigurationChanged(Configuration var1) {
      if(this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if(var3 != null) {
               var3.performConfigurationChanged(var1);
            }
         }
      }

   }

   public boolean dispatchContextItemSelected(MenuItem var1) {
      boolean var3;
      if(this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var4 = (Fragment)this.mAdded.get(var2);
            if(var4 != null && var4.performContextItemSelected(var1)) {
               var3 = true;
               return var3;
            }
         }
      }

      var3 = false;
      return var3;
   }

   public void dispatchCreate() {
      this.mStateSaved = false;
      this.moveToState(1, false);
   }

   public boolean dispatchCreateOptionsMenu(Menu var1, MenuInflater var2) {
      boolean var5 = false;
      boolean var4 = false;
      ArrayList var7 = null;
      ArrayList var6 = null;
      int var3;
      if(this.mAdded != null) {
         var3 = 0;

         while(true) {
            var7 = var6;
            var5 = var4;
            if(var3 >= this.mAdded.size()) {
               break;
            }

            Fragment var8 = (Fragment)this.mAdded.get(var3);
            var7 = var6;
            var5 = var4;
            if(var8 != null) {
               var7 = var6;
               var5 = var4;
               if(var8.performCreateOptionsMenu(var1, var2)) {
                  var5 = true;
                  var7 = var6;
                  if(var6 == null) {
                     var7 = new ArrayList();
                  }

                  var7.add(var8);
               }
            }

            ++var3;
            var6 = var7;
            var4 = var5;
         }
      }

      if(this.mCreatedMenus != null) {
         for(var3 = 0; var3 < this.mCreatedMenus.size(); ++var3) {
            Fragment var9 = (Fragment)this.mCreatedMenus.get(var3);
            if(var7 == null || !var7.contains(var9)) {
               var9.onDestroyOptionsMenu();
            }
         }
      }

      this.mCreatedMenus = var7;
      return var5;
   }

   public void dispatchDestroy() {
      this.mDestroyed = true;
      this.execPendingActions();
      this.moveToState(0, false);
      this.mHost = null;
      this.mContainer = null;
      this.mParent = null;
   }

   public void dispatchDestroyView() {
      this.moveToState(1, false);
   }

   public void dispatchLowMemory() {
      if(this.mAdded != null) {
         for(int var1 = 0; var1 < this.mAdded.size(); ++var1) {
            Fragment var2 = (Fragment)this.mAdded.get(var1);
            if(var2 != null) {
               var2.performLowMemory();
            }
         }
      }

   }

   public void dispatchMultiWindowModeChanged(boolean var1) {
      if(this.mAdded != null) {
         for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if(var3 != null) {
               var3.performMultiWindowModeChanged(var1);
            }
         }
      }

   }

   public boolean dispatchOptionsItemSelected(MenuItem var1) {
      boolean var3;
      if(this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var4 = (Fragment)this.mAdded.get(var2);
            if(var4 != null && var4.performOptionsItemSelected(var1)) {
               var3 = true;
               return var3;
            }
         }
      }

      var3 = false;
      return var3;
   }

   public void dispatchOptionsMenuClosed(Menu var1) {
      if(this.mAdded != null) {
         for(int var2 = 0; var2 < this.mAdded.size(); ++var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if(var3 != null) {
               var3.performOptionsMenuClosed(var1);
            }
         }
      }

   }

   public void dispatchPause() {
      this.moveToState(4, false);
   }

   public void dispatchPictureInPictureModeChanged(boolean var1) {
      if(this.mAdded != null) {
         for(int var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mAdded.get(var2);
            if(var3 != null) {
               var3.performPictureInPictureModeChanged(var1);
            }
         }
      }

   }

   public boolean dispatchPrepareOptionsMenu(Menu var1) {
      boolean var4 = false;
      boolean var3 = false;
      if(this.mAdded != null) {
         int var2 = 0;

         while(true) {
            var4 = var3;
            if(var2 >= this.mAdded.size()) {
               break;
            }

            Fragment var5 = (Fragment)this.mAdded.get(var2);
            var4 = var3;
            if(var5 != null) {
               var4 = var3;
               if(var5.performPrepareOptionsMenu(var1)) {
                  var4 = true;
               }
            }

            ++var2;
            var3 = var4;
         }
      }

      return var4;
   }

   public void dispatchReallyStop() {
      this.moveToState(2, false);
   }

   public void dispatchResume() {
      this.mStateSaved = false;
      this.moveToState(5, false);
   }

   public void dispatchStart() {
      this.mStateSaved = false;
      this.moveToState(4, false);
   }

   public void dispatchStop() {
      this.mStateSaved = true;
      this.moveToState(3, false);
   }

   void doPendingDeferredStart() {
      if(this.mHavePendingDeferredStart) {
         boolean var3 = false;

         boolean var2;
         for(int var1 = 0; var1 < this.mActive.size(); var3 = var2) {
            Fragment var4 = (Fragment)this.mActive.get(var1);
            var2 = var3;
            if(var4 != null) {
               var2 = var3;
               if(var4.mLoaderManager != null) {
                  var2 = var3 | var4.mLoaderManager.hasRunningLoaders();
               }
            }

            ++var1;
         }

         if(!var3) {
            this.mHavePendingDeferredStart = false;
            this.startPendingDeferredFragments();
         }
      }

   }

   public void dump(String param1, FileDescriptor param2, PrintWriter param3, String[] param4) {
      // $FF: Couldn't be decompiled
   }

   public void enqueueAction(Runnable param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public boolean execPendingActions() {
      // $FF: Couldn't be decompiled
   }

   public void execSingleAction(Runnable var1, boolean var2) {
      if(this.mExecutingActions) {
         throw new IllegalStateException("FragmentManager is already executing transactions");
      } else if(Looper.myLooper() != this.mHost.getHandler().getLooper()) {
         throw new IllegalStateException("Must be called from main thread of fragment host");
      } else {
         if(!var2) {
            this.checkStateLoss();
         }

         this.mExecutingActions = true;
         var1.run();
         this.mExecutingActions = false;
         this.doPendingDeferredStart();
      }
   }

   public boolean executePendingTransactions() {
      return this.execPendingActions();
   }

   public Fragment findFragmentById(int var1) {
      int var2;
      Fragment var3;
      if(this.mAdded != null) {
         for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mAdded.get(var2);
            if(var3 != null && var3.mFragmentId == var1) {
               return var3;
            }
         }
      }

      if(this.mActive != null) {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var4 = (Fragment)this.mActive.get(var2);
            if(var4 != null) {
               var3 = var4;
               if(var4.mFragmentId == var1) {
                  return var3;
               }
            }
         }
      }

      var3 = null;
      return var3;
   }

   public Fragment findFragmentByTag(String var1) {
      int var2;
      Fragment var3;
      if(this.mAdded != null && var1 != null) {
         for(var2 = this.mAdded.size() - 1; var2 >= 0; --var2) {
            var3 = (Fragment)this.mAdded.get(var2);
            if(var3 != null && var1.equals(var3.mTag)) {
               return var3;
            }
         }
      }

      if(this.mActive != null && var1 != null) {
         for(var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var4 = (Fragment)this.mActive.get(var2);
            if(var4 != null) {
               var3 = var4;
               if(var1.equals(var4.mTag)) {
                  return var3;
               }
            }
         }
      }

      var3 = null;
      return var3;
   }

   public Fragment findFragmentByWho(String var1) {
      Fragment var4;
      if(this.mActive != null && var1 != null) {
         for(int var2 = this.mActive.size() - 1; var2 >= 0; --var2) {
            Fragment var3 = (Fragment)this.mActive.get(var2);
            if(var3 != null) {
               var3 = var3.findFragmentByWho(var1);
               if(var3 != null) {
                  var4 = var3;
                  return var4;
               }
            }
         }
      }

      var4 = null;
      return var4;
   }

   public void freeBackStackIndex(int param1) {
      // $FF: Couldn't be decompiled
   }

   public FragmentManager.BackStackEntry getBackStackEntryAt(int var1) {
      return (FragmentManager.BackStackEntry)this.mBackStack.get(var1);
   }

   public int getBackStackEntryCount() {
      int var1;
      if(this.mBackStack != null) {
         var1 = this.mBackStack.size();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public Fragment getFragment(Bundle var1, String var2) {
      int var3 = var1.getInt(var2, -1);
      Fragment var5;
      if(var3 == -1) {
         var5 = null;
      } else {
         if(var3 >= this.mActive.size()) {
            this.throwException(new IllegalStateException("Fragment no longer exists for key " + var2 + ": index " + var3));
         }

         Fragment var4 = (Fragment)this.mActive.get(var3);
         var5 = var4;
         if(var4 == null) {
            this.throwException(new IllegalStateException("Fragment no longer exists for key " + var2 + ": index " + var3));
            var5 = var4;
         }
      }

      return var5;
   }

   public List getFragments() {
      return this.mActive;
   }

   LayoutInflaterFactory getLayoutInflaterFactory() {
      return this;
   }

   public void hideFragment(Fragment var1, int var2, int var3) {
      if(DEBUG) {
         Log.v("FragmentManager", "hide: " + var1);
      }

      if(!var1.mHidden) {
         var1.mHidden = true;
         if(var1.mView != null) {
            Animation var4 = this.loadAnimation(var1, var2, false, var3);
            if(var4 != null) {
               this.setHWLayerAnimListenerIfAlpha(var1.mView, var4);
               var1.mView.startAnimation(var4);
            }

            var1.mView.setVisibility(8);
         }

         if(var1.mAdded && var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         var1.onHiddenChanged(true);
      }

   }

   public boolean isDestroyed() {
      return this.mDestroyed;
   }

   boolean isStateAtLeast(int var1) {
      boolean var2;
      if(this.mCurState >= var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   Animation loadAnimation(Fragment var1, int var2, boolean var3, int var4) {
      Animation var5 = var1.onCreateAnimation(var2, var3, var1.mNextAnim);
      Animation var6;
      if(var5 != null) {
         var6 = var5;
      } else {
         if(var1.mNextAnim != 0) {
            var6 = AnimationUtils.loadAnimation(this.mHost.getContext(), var1.mNextAnim);
            if(var6 != null) {
               return var6;
            }
         }

         if(var2 == 0) {
            var6 = null;
         } else {
            var2 = transitToStyleIndex(var2, var3);
            if(var2 < 0) {
               var6 = null;
            } else {
               switch(var2) {
               case 1:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
                  break;
               case 2:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
                  break;
               case 3:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
                  break;
               case 4:
                  var6 = makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
                  break;
               case 5:
                  var6 = makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
                  break;
               case 6:
                  var6 = makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
                  break;
               default:
                  var2 = var4;
                  if(var4 == 0) {
                     var2 = var4;
                     if(this.mHost.onHasWindowAnimations()) {
                        var2 = this.mHost.onGetWindowAnimations();
                     }
                  }

                  if(var2 == 0) {
                     var6 = null;
                  } else {
                     var6 = null;
                  }
               }
            }
         }
      }

      return var6;
   }

   void makeActive(Fragment var1) {
      if(var1.mIndex < 0) {
         if(this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            var1.setIndex(((Integer)this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue(), this.mParent);
            this.mActive.set(var1.mIndex, var1);
         } else {
            if(this.mActive == null) {
               this.mActive = new ArrayList();
            }

            var1.setIndex(this.mActive.size(), this.mParent);
            this.mActive.add(var1);
         }

         if(DEBUG) {
            Log.v("FragmentManager", "Allocated fragment index " + var1);
         }
      }

   }

   void makeInactive(Fragment var1) {
      if(var1.mIndex >= 0) {
         if(DEBUG) {
            Log.v("FragmentManager", "Freeing fragment index " + var1);
         }

         this.mActive.set(var1.mIndex, (Object)null);
         if(this.mAvailIndices == null) {
            this.mAvailIndices = new ArrayList();
         }

         this.mAvailIndices.add(Integer.valueOf(var1.mIndex));
         this.mHost.inactivateFragment(var1.mWho);
         var1.initState();
      }

   }

   void moveToState(int var1, int var2, int var3, boolean var4) {
      if(this.mHost == null && var1 != 0) {
         throw new IllegalStateException("No host");
      } else {
         if(var4 || this.mCurState != var1) {
            this.mCurState = var1;
            if(this.mActive != null) {
               boolean var6 = false;

               boolean var7;
               for(int var5 = 0; var5 < this.mActive.size(); var6 = var7) {
                  Fragment var8 = (Fragment)this.mActive.get(var5);
                  var7 = var6;
                  if(var8 != null) {
                     this.moveToState(var8, var1, var2, var3, false);
                     var7 = var6;
                     if(var8.mLoaderManager != null) {
                        var7 = var6 | var8.mLoaderManager.hasRunningLoaders();
                     }
                  }

                  ++var5;
               }

               if(!var6) {
                  this.startPendingDeferredFragments();
               }

               if(this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                  this.mHost.onSupportInvalidateOptionsMenu();
                  this.mNeedMenuInvalidate = false;
               }
            }
         }

      }
   }

   void moveToState(int var1, boolean var2) {
      this.moveToState(var1, 0, 0, var2);
   }

   void moveToState(Fragment var1) {
      this.moveToState(var1, this.mCurState, 0, 0, false);
   }

   void moveToState(final Fragment var1, int var2, int var3, int var4, boolean var5) {
      int var6;
      label250: {
         if(var1.mAdded) {
            var6 = var2;
            if(!var1.mDetached) {
               break label250;
            }
         }

         var6 = var2;
         if(var2 > 1) {
            var6 = 1;
         }
      }

      int var7 = var6;
      if(var1.mRemoving) {
         var7 = var6;
         if(var6 > var1.mState) {
            var7 = var1.mState;
         }
      }

      var2 = var7;
      if(var1.mDeferStart) {
         var2 = var7;
         if(var1.mState < 4) {
            var2 = var7;
            if(var7 > 3) {
               var2 = 3;
            }
         }
      }

      ViewGroup var11;
      if(var1.mState < var2) {
         if(var1.mFromLayout && !var1.mInLayout) {
            return;
         }

         if(var1.mAnimatingAway != null) {
            var1.mAnimatingAway = null;
            this.moveToState(var1, var1.mStateAfterAnimating, 0, 0, true);
         }

         var6 = var2;
         int var8 = var2;
         int var9 = var2;
         var7 = var2;
         switch(var1.mState) {
         case 0:
            if(DEBUG) {
               Log.v("FragmentManager", "moveto CREATED: " + var1);
            }

            var7 = var2;
            if(var1.mSavedFragmentState != null) {
               var1.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
               var1.mSavedViewState = var1.mSavedFragmentState.getSparseParcelableArray("android:view_state");
               var1.mTarget = this.getFragment(var1.mSavedFragmentState, "android:target_state");
               if(var1.mTarget != null) {
                  var1.mTargetRequestCode = var1.mSavedFragmentState.getInt("android:target_req_state", 0);
               }

               var1.mUserVisibleHint = var1.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
               var7 = var2;
               if(!var1.mUserVisibleHint) {
                  var1.mDeferStart = true;
                  var7 = var2;
                  if(var2 > 3) {
                     var7 = 3;
                  }
               }
            }

            var1.mHost = this.mHost;
            var1.mParentFragment = this.mParent;
            FragmentManagerImpl var10;
            if(this.mParent != null) {
               var10 = this.mParent.mChildFragmentManager;
            } else {
               var10 = this.mHost.getFragmentManagerImpl();
            }

            var1.mFragmentManager = var10;
            var1.mCalled = false;
            var1.onAttach(this.mHost.getContext());
            if(!var1.mCalled) {
               throw new SuperNotCalledException("Fragment " + var1 + " did not call through to super.onAttach()");
            }

            if(var1.mParentFragment == null) {
               this.mHost.onAttachFragment(var1);
            } else {
               var1.mParentFragment.onAttachFragment(var1);
            }

            if(!var1.mRetaining) {
               var1.performCreate(var1.mSavedFragmentState);
            } else {
               var1.restoreChildFragmentState(var1.mSavedFragmentState);
               var1.mState = 1;
            }

            var1.mRetaining = false;
            var6 = var7;
            if(var1.mFromLayout) {
               var1.mView = var1.performCreateView(var1.getLayoutInflater(var1.mSavedFragmentState), (ViewGroup)null, var1.mSavedFragmentState);
               if(var1.mView != null) {
                  var1.mInnerView = var1.mView;
                  if(VERSION.SDK_INT >= 11) {
                     ViewCompat.setSaveFromParentEnabled(var1.mView, false);
                  } else {
                     var1.mView = NoSaveStateFrameLayout.wrap(var1.mView);
                  }

                  if(var1.mHidden) {
                     var1.mView.setVisibility(8);
                  }

                  var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
                  var6 = var7;
               } else {
                  var1.mInnerView = null;
                  var6 = var7;
               }
            }
         case 1:
            var8 = var6;
            if(var6 > 1) {
               if(DEBUG) {
                  Log.v("FragmentManager", "moveto ACTIVITY_CREATED: " + var1);
               }

               if(!var1.mFromLayout) {
                  ViewGroup var13 = null;
                  if(var1.mContainerId != 0) {
                     if(var1.mContainerId == -1) {
                        this.throwException(new IllegalArgumentException("Cannot create fragment " + var1 + " for a container view with no id"));
                     }

                     var11 = (ViewGroup)this.mContainer.onFindViewById(var1.mContainerId);
                     var13 = var11;
                     if(var11 == null) {
                        var13 = var11;
                        if(!var1.mRestored) {
                           String var15;
                           try {
                              var15 = var1.getResources().getResourceName(var1.mContainerId);
                           } catch (NotFoundException var12) {
                              var15 = "unknown";
                           }

                           this.throwException(new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(var1.mContainerId) + " (" + var15 + ") for fragment " + var1));
                           var13 = var11;
                        }
                     }
                  }

                  var1.mContainer = var13;
                  var1.mView = var1.performCreateView(var1.getLayoutInflater(var1.mSavedFragmentState), var13, var1.mSavedFragmentState);
                  if(var1.mView != null) {
                     var1.mInnerView = var1.mView;
                     if(VERSION.SDK_INT >= 11) {
                        ViewCompat.setSaveFromParentEnabled(var1.mView, false);
                     } else {
                        var1.mView = NoSaveStateFrameLayout.wrap(var1.mView);
                     }

                     if(var13 != null) {
                        Animation var14 = this.loadAnimation(var1, var3, true, var4);
                        if(var14 != null) {
                           this.setHWLayerAnimListenerIfAlpha(var1.mView, var14);
                           var1.mView.startAnimation(var14);
                        }

                        var13.addView(var1.mView);
                     }

                     if(var1.mHidden) {
                        var1.mView.setVisibility(8);
                     }

                     var1.onViewCreated(var1.mView, var1.mSavedFragmentState);
                  } else {
                     var1.mInnerView = null;
                  }
               }

               var1.performActivityCreated(var1.mSavedFragmentState);
               if(var1.mView != null) {
                  var1.restoreViewState(var1.mSavedFragmentState);
               }

               var1.mSavedFragmentState = null;
               var8 = var6;
            }
         case 2:
            var9 = var8;
            if(var8 > 2) {
               var1.mState = 3;
               var9 = var8;
            }
         case 3:
            var7 = var9;
            if(var9 > 3) {
               if(DEBUG) {
                  Log.v("FragmentManager", "moveto STARTED: " + var1);
               }

               var1.performStart();
               var7 = var9;
            }
         case 4:
            var6 = var7;
            if(var7 > 4) {
               if(DEBUG) {
                  Log.v("FragmentManager", "moveto RESUMED: " + var1);
               }

               var1.performResume();
               var1.mSavedFragmentState = null;
               var1.mSavedViewState = null;
               var6 = var7;
            }
            break;
         default:
            var6 = var2;
         }
      } else {
         var6 = var2;
         if(var1.mState > var2) {
            switch(var1.mState) {
            case 5:
               if(var2 < 5) {
                  if(DEBUG) {
                     Log.v("FragmentManager", "movefrom RESUMED: " + var1);
                  }

                  var1.performPause();
               }
            case 4:
               if(var2 < 4) {
                  if(DEBUG) {
                     Log.v("FragmentManager", "movefrom STARTED: " + var1);
                  }

                  var1.performStop();
               }
            case 3:
               if(var2 < 3) {
                  if(DEBUG) {
                     Log.v("FragmentManager", "movefrom STOPPED: " + var1);
                  }

                  var1.performReallyStop();
               }
            case 2:
               if(var2 < 2) {
                  if(DEBUG) {
                     Log.v("FragmentManager", "movefrom ACTIVITY_CREATED: " + var1);
                  }

                  if(var1.mView != null && this.mHost.onShouldSaveFragmentState(var1) && var1.mSavedViewState == null) {
                     this.saveFragmentViewState(var1);
                  }

                  var1.performDestroyView();
                  if(var1.mView != null && var1.mContainer != null) {
                     var11 = null;
                     final Animation var16 = var11;
                     if(this.mCurState > 0) {
                        var16 = var11;
                        if(!this.mDestroyed) {
                           var16 = this.loadAnimation(var1, var3, false, var4);
                        }
                     }

                     if(var16 != null) {
                        var1.mAnimatingAway = var1.mView;
                        var1.mStateAfterAnimating = var2;
                        var16.setAnimationListener(new FragmentManagerImpl.AnimateOnHWLayerIfNeededListener(var1.mView, var16) {
                           public void onAnimationEnd(Animation var1x) {
                              super.onAnimationEnd(var1x);
                              if(var1.mAnimatingAway != null) {
                                 var1.mAnimatingAway = null;
                                 FragmentManagerImpl.this.moveToState(var1, var1.mStateAfterAnimating, 0, 0, false);
                              }

                           }
                        });
                        var1.mView.startAnimation(var16);
                     }

                     var1.mContainer.removeView(var1.mView);
                  }

                  var1.mContainer = null;
                  var1.mView = null;
                  var1.mInnerView = null;
               }
            case 1:
               var6 = var2;
               if(var2 < 1) {
                  if(this.mDestroyed && var1.mAnimatingAway != null) {
                     View var17 = var1.mAnimatingAway;
                     var1.mAnimatingAway = null;
                     var17.clearAnimation();
                  }

                  if(var1.mAnimatingAway != null) {
                     var1.mStateAfterAnimating = var2;
                     var6 = 1;
                  } else {
                     if(DEBUG) {
                        Log.v("FragmentManager", "movefrom CREATED: " + var1);
                     }

                     if(!var1.mRetaining) {
                        var1.performDestroy();
                     } else {
                        var1.mState = 0;
                     }

                     var1.performDetach();
                     var6 = var2;
                     if(!var5) {
                        if(!var1.mRetaining) {
                           this.makeInactive(var1);
                           var6 = var2;
                        } else {
                           var1.mHost = null;
                           var1.mParentFragment = null;
                           var1.mFragmentManager = null;
                           var6 = var2;
                        }
                     }
                  }
               }
               break;
            default:
               var6 = var2;
            }
         }
      }

      if(var1.mState != var6) {
         Log.w("FragmentManager", "moveToState: Fragment state for " + var1 + " not updated inline; " + "expected state " + var6 + " found " + var1.mState);
         var1.mState = var6;
      }

   }

   public void noteStateNotSaved() {
      this.mStateSaved = false;
   }

   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      Object var9 = null;
      View var13;
      if(!"fragment".equals(var2)) {
         var13 = (View)var9;
      } else {
         var2 = var4.getAttributeValue((String)null, "class");
         TypedArray var11 = var3.obtainStyledAttributes(var4, FragmentManagerImpl.FragmentTag.Fragment);
         String var8 = var2;
         if(var2 == null) {
            var8 = var11.getString(0);
         }

         int var7 = var11.getResourceId(1, -1);
         String var10 = var11.getString(2);
         var11.recycle();
         var13 = (View)var9;
         if(Fragment.isSupportFragmentClass(this.mHost.getContext(), var8)) {
            int var5;
            if(var1 != null) {
               var5 = var1.getId();
            } else {
               var5 = 0;
            }

            if(var5 == -1 && var7 == -1 && var10 == null) {
               throw new IllegalArgumentException(var4.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + var8);
            }

            Fragment var12;
            if(var7 != -1) {
               var12 = this.findFragmentById(var7);
            } else {
               var12 = null;
            }

            Fragment var14 = var12;
            if(var12 == null) {
               var14 = var12;
               if(var10 != null) {
                  var14 = this.findFragmentByTag(var10);
               }
            }

            var12 = var14;
            if(var14 == null) {
               var12 = var14;
               if(var5 != -1) {
                  var12 = this.findFragmentById(var5);
               }
            }

            if(DEBUG) {
               Log.v("FragmentManager", "onCreateView: id=0x" + Integer.toHexString(var7) + " fname=" + var8 + " existing=" + var12);
            }

            if(var12 == null) {
               var14 = Fragment.instantiate(var3, var8);
               var14.mFromLayout = true;
               int var6;
               if(var7 != 0) {
                  var6 = var7;
               } else {
                  var6 = var5;
               }

               var14.mFragmentId = var6;
               var14.mContainerId = var5;
               var14.mTag = var10;
               var14.mInLayout = true;
               var14.mFragmentManager = this;
               var14.mHost = this.mHost;
               var14.onInflate(this.mHost.getContext(), var4, var14.mSavedFragmentState);
               this.addFragment(var14, true);
            } else {
               if(var12.mInLayout) {
                  throw new IllegalArgumentException(var4.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(var7) + ", tag " + var10 + ", or parent id 0x" + Integer.toHexString(var5) + " with another fragment for " + var8);
               }

               var12.mInLayout = true;
               var12.mHost = this.mHost;
               var14 = var12;
               if(!var12.mRetaining) {
                  var12.onInflate(this.mHost.getContext(), var4, var12.mSavedFragmentState);
                  var14 = var12;
               }
            }

            if(this.mCurState < 1 && var14.mFromLayout) {
               this.moveToState(var14, 1, 0, 0, false);
            } else {
               this.moveToState(var14);
            }

            if(var14.mView == null) {
               throw new IllegalStateException("Fragment " + var8 + " did not create a view.");
            }

            if(var7 != 0) {
               var14.mView.setId(var7);
            }

            if(var14.mView.getTag() == null) {
               var14.mView.setTag(var10);
            }

            var13 = var14.mView;
         }
      }

      return var13;
   }

   public void performPendingDeferredStart(Fragment var1) {
      if(var1.mDeferStart) {
         if(this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
         } else {
            var1.mDeferStart = false;
            this.moveToState(var1, this.mCurState, 0, 0, false);
         }
      }

   }

   public void popBackStack() {
      this.enqueueAction(new Runnable() {
         public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), (String)null, -1, 0);
         }
      }, false);
   }

   public void popBackStack(final int var1, final int var2) {
      if(var1 < 0) {
         throw new IllegalArgumentException("Bad id: " + var1);
      } else {
         this.enqueueAction(new Runnable() {
            public void run() {
               FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), (String)null, var1, var2);
            }
         }, false);
      }
   }

   public void popBackStack(final String var1, final int var2) {
      this.enqueueAction(new Runnable() {
         public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), var1, -1, var2);
         }
      }, false);
   }

   public boolean popBackStackImmediate() {
      this.checkStateLoss();
      this.executePendingTransactions();
      return this.popBackStackState(this.mHost.getHandler(), (String)null, -1, 0);
   }

   public boolean popBackStackImmediate(int var1, int var2) {
      this.checkStateLoss();
      this.executePendingTransactions();
      if(var1 < 0) {
         throw new IllegalArgumentException("Bad id: " + var1);
      } else {
         return this.popBackStackState(this.mHost.getHandler(), (String)null, var1, var2);
      }
   }

   public boolean popBackStackImmediate(String var1, int var2) {
      this.checkStateLoss();
      this.executePendingTransactions();
      return this.popBackStackState(this.mHost.getHandler(), var1, -1, var2);
   }

   boolean popBackStackState(Handler var1, String var2, int var3, int var4) {
      boolean var7;
      if(this.mBackStack == null) {
         var7 = false;
      } else {
         if(var2 == null && var3 < 0 && (var4 & 1) == 0) {
            var3 = this.mBackStack.size() - 1;
            if(var3 < 0) {
               var7 = false;
               return var7;
            }

            BackStackRecord var16 = (BackStackRecord)this.mBackStack.remove(var3);
            SparseArray var14 = new SparseArray();
            SparseArray var15 = new SparseArray();
            if(this.mCurState >= 1) {
               var16.calculateBackFragments(var14, var15);
            }

            var16.popFromBackStack(true, (BackStackRecord.TransitionState)null, var14, var15);
            this.reportBackStackChanged();
         } else {
            int var5 = -1;
            if(var2 != null || var3 >= 0) {
               int var6;
               BackStackRecord var11;
               for(var6 = this.mBackStack.size() - 1; var6 >= 0; --var6) {
                  var11 = (BackStackRecord)this.mBackStack.get(var6);
                  if(var2 != null && var2.equals(var11.getName()) || var3 >= 0 && var3 == var11.mIndex) {
                     break;
                  }
               }

               if(var6 < 0) {
                  var7 = false;
                  return var7;
               }

               var5 = var6;
               if((var4 & 1) != 0) {
                  var4 = var6 - 1;

                  while(true) {
                     var5 = var4;
                     if(var4 < 0) {
                        break;
                     }

                     var11 = (BackStackRecord)this.mBackStack.get(var4);
                     if(var2 == null || !var2.equals(var11.getName())) {
                        var5 = var4;
                        if(var3 < 0) {
                           break;
                        }

                        var5 = var4;
                        if(var3 != var11.mIndex) {
                           break;
                        }
                     }

                     --var4;
                  }
               }
            }

            if(var5 == this.mBackStack.size() - 1) {
               var7 = false;
               return var7;
            }

            ArrayList var13 = new ArrayList();

            for(var3 = this.mBackStack.size() - 1; var3 > var5; --var3) {
               var13.add(this.mBackStack.remove(var3));
            }

            var4 = var13.size() - 1;
            SparseArray var8 = new SparseArray();
            SparseArray var9 = new SparseArray();
            if(this.mCurState >= 1) {
               for(var3 = 0; var3 <= var4; ++var3) {
                  ((BackStackRecord)var13.get(var3)).calculateBackFragments(var8, var9);
               }
            }

            BackStackRecord.TransitionState var12 = null;

            for(var3 = 0; var3 <= var4; ++var3) {
               if(DEBUG) {
                  Log.v("FragmentManager", "Popping back stack state: " + var13.get(var3));
               }

               BackStackRecord var10 = (BackStackRecord)var13.get(var3);
               if(var3 == var4) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               var12 = var10.popFromBackStack(var7, var12, var8, var9);
            }

            this.reportBackStackChanged();
         }

         var7 = true;
      }

      return var7;
   }

   public void putFragment(Bundle var1, String var2, Fragment var3) {
      if(var3.mIndex < 0) {
         this.throwException(new IllegalStateException("Fragment " + var3 + " is not currently in the FragmentManager"));
      }

      var1.putInt(var2, var3.mIndex);
   }

   public void removeFragment(Fragment var1, int var2, int var3) {
      if(DEBUG) {
         Log.v("FragmentManager", "remove: " + var1 + " nesting=" + var1.mBackStackNesting);
      }

      boolean var4;
      if(!var1.isInBackStack()) {
         var4 = true;
      } else {
         var4 = false;
      }

      if(!var1.mDetached || var4) {
         if(this.mAdded != null) {
            this.mAdded.remove(var1);
         }

         if(var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         var1.mAdded = false;
         var1.mRemoving = true;
         byte var5;
         if(var4) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         this.moveToState(var1, var5, var2, var3, false);
      }

   }

   public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener var1) {
      if(this.mBackStackChangeListeners != null) {
         this.mBackStackChangeListeners.remove(var1);
      }

   }

   void reportBackStackChanged() {
      if(this.mBackStackChangeListeners != null) {
         for(int var1 = 0; var1 < this.mBackStackChangeListeners.size(); ++var1) {
            ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(var1)).onBackStackChanged();
         }
      }

   }

   void restoreAllState(Parcelable var1, FragmentManagerNonConfig var2) {
      if(var1 != null) {
         FragmentManagerState var7 = (FragmentManagerState)var1;
         if(var7.mActive != null) {
            List var9 = null;
            int var3;
            int var4;
            List var6;
            FragmentState var8;
            Fragment var10;
            if(var2 != null) {
               var6 = var2.getFragments();
               List var5 = var2.getChildNonConfigs();
               if(var6 != null) {
                  var3 = var6.size();
               } else {
                  var3 = 0;
               }

               var4 = 0;

               while(true) {
                  var9 = var5;
                  if(var4 >= var3) {
                     break;
                  }

                  var10 = (Fragment)var6.get(var4);
                  if(DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: re-attaching retained " + var10);
                  }

                  var8 = var7.mActive[var10.mIndex];
                  var8.mInstance = var10;
                  var10.mSavedViewState = null;
                  var10.mBackStackNesting = 0;
                  var10.mInLayout = false;
                  var10.mAdded = false;
                  var10.mTarget = null;
                  if(var8.mSavedFragmentState != null) {
                     var8.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                     var10.mSavedViewState = var8.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                     var10.mSavedFragmentState = var8.mSavedFragmentState;
                  }

                  ++var4;
               }
            }

            this.mActive = new ArrayList(var7.mActive.length);
            if(this.mAvailIndices != null) {
               this.mAvailIndices.clear();
            }

            for(var3 = 0; var3 < var7.mActive.length; ++var3) {
               var8 = var7.mActive[var3];
               if(var8 != null) {
                  var6 = null;
                  FragmentManagerNonConfig var13 = var6;
                  if(var9 != null) {
                     var13 = var6;
                     if(var3 < var9.size()) {
                        var13 = (FragmentManagerNonConfig)var9.get(var3);
                     }
                  }

                  Fragment var14 = var8.instantiate(this.mHost, this.mParent, var13);
                  if(DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: active #" + var3 + ": " + var14);
                  }

                  this.mActive.add(var14);
                  var8.mInstance = null;
               } else {
                  this.mActive.add((Object)null);
                  if(this.mAvailIndices == null) {
                     this.mAvailIndices = new ArrayList();
                  }

                  if(DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: avail #" + var3);
                  }

                  this.mAvailIndices.add(Integer.valueOf(var3));
               }
            }

            if(var2 != null) {
               var9 = var2.getFragments();
               if(var9 != null) {
                  var3 = var9.size();
               } else {
                  var3 = 0;
               }

               for(var4 = 0; var4 < var3; ++var4) {
                  Fragment var11 = (Fragment)var9.get(var4);
                  if(var11.mTargetIndex >= 0) {
                     if(var11.mTargetIndex < this.mActive.size()) {
                        var11.mTarget = (Fragment)this.mActive.get(var11.mTargetIndex);
                     } else {
                        Log.w("FragmentManager", "Re-attaching retained fragment " + var11 + " target no longer exists: " + var11.mTargetIndex);
                        var11.mTarget = null;
                     }
                  }
               }
            }

            if(var7.mAdded != null) {
               this.mAdded = new ArrayList(var7.mAdded.length);

               for(var3 = 0; var3 < var7.mAdded.length; ++var3) {
                  var10 = (Fragment)this.mActive.get(var7.mAdded[var3]);
                  if(var10 == null) {
                     this.throwException(new IllegalStateException("No instantiated fragment for index #" + var7.mAdded[var3]));
                  }

                  var10.mAdded = true;
                  if(DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: added #" + var3 + ": " + var10);
                  }

                  if(this.mAdded.contains(var10)) {
                     throw new IllegalStateException("Already added!");
                  }

                  this.mAdded.add(var10);
               }
            } else {
               this.mAdded = null;
            }

            if(var7.mBackStack != null) {
               this.mBackStack = new ArrayList(var7.mBackStack.length);

               for(var3 = 0; var3 < var7.mBackStack.length; ++var3) {
                  BackStackRecord var12 = var7.mBackStack[var3].instantiate(this);
                  if(DEBUG) {
                     Log.v("FragmentManager", "restoreAllState: back stack #" + var3 + " (index " + var12.mIndex + "): " + var12);
                     var12.dump("  ", new PrintWriter(new LogWriter("FragmentManager")), false);
                  }

                  this.mBackStack.add(var12);
                  if(var12.mIndex >= 0) {
                     this.setBackStackIndex(var12.mIndex, var12);
                  }
               }
            } else {
               this.mBackStack = null;
            }
         }
      }

   }

   FragmentManagerNonConfig retainNonConfig() {
      ArrayList var7 = null;
      ArrayList var5 = null;
      ArrayList var6 = null;
      ArrayList var4 = null;
      if(this.mActive != null) {
         int var1 = 0;

         while(true) {
            var6 = var4;
            var7 = var5;
            if(var1 >= this.mActive.size()) {
               break;
            }

            Fragment var9 = (Fragment)this.mActive.get(var1);
            ArrayList var8 = var4;
            var7 = var5;
            if(var9 != null) {
               var6 = var5;
               int var2;
               if(var9.mRetainInstance) {
                  var7 = var5;
                  if(var5 == null) {
                     var7 = new ArrayList();
                  }

                  var7.add(var9);
                  var9.mRetaining = true;
                  if(var9.mTarget != null) {
                     var2 = var9.mTarget.mIndex;
                  } else {
                     var2 = -1;
                  }

                  var9.mTargetIndex = var2;
                  var6 = var7;
                  if(DEBUG) {
                     Log.v("FragmentManager", "retainNonConfig: keeping retained " + var9);
                     var6 = var7;
                  }
               }

               boolean var3 = false;
               boolean var10 = var3;
               var5 = var4;
               if(var9.mChildFragmentManager != null) {
                  FragmentManagerNonConfig var12 = var9.mChildFragmentManager.retainNonConfig();
                  var10 = var3;
                  var5 = var4;
                  if(var12 != null) {
                     var5 = var4;
                     if(var4 == null) {
                        var4 = new ArrayList();
                        var2 = 0;

                        while(true) {
                           var5 = var4;
                           if(var2 >= var1) {
                              break;
                           }

                           var4.add((Object)null);
                           ++var2;
                        }
                     }

                     var5.add(var12);
                     var10 = true;
                  }
               }

               var8 = var5;
               var7 = var6;
               if(var5 != null) {
                  var8 = var5;
                  var7 = var6;
                  if(!var10) {
                     var5.add((Object)null);
                     var7 = var6;
                     var8 = var5;
                  }
               }
            }

            ++var1;
            var4 = var8;
            var5 = var7;
         }
      }

      FragmentManagerNonConfig var11;
      if(var7 == null && var6 == null) {
         var11 = null;
      } else {
         var11 = new FragmentManagerNonConfig(var7, var6);
      }

      return var11;
   }

   Parcelable saveAllState() {
      BackStackState[] var6 = null;
      this.execPendingActions();
      if(HONEYCOMB) {
         this.mStateSaved = true;
      }

      FragmentManagerState var5 = var6;
      if(this.mActive != null) {
         if(this.mActive.size() <= 0) {
            var5 = var6;
         } else {
            int var4 = this.mActive.size();
            FragmentState[] var8 = new FragmentState[var4];
            boolean var2 = false;

            int var1;
            FragmentState var7;
            for(var1 = 0; var1 < var4; ++var1) {
               Fragment var10 = (Fragment)this.mActive.get(var1);
               if(var10 != null) {
                  if(var10.mIndex < 0) {
                     this.throwException(new IllegalStateException("Failure saving state: active " + var10 + " has cleared index: " + var10.mIndex));
                  }

                  boolean var3 = true;
                  var7 = new FragmentState(var10);
                  var8[var1] = var7;
                  if(var10.mState > 0 && var7.mSavedFragmentState == null) {
                     var7.mSavedFragmentState = this.saveFragmentBasicState(var10);
                     if(var10.mTarget != null) {
                        if(var10.mTarget.mIndex < 0) {
                           this.throwException(new IllegalStateException("Failure saving state: " + var10 + " has target not in fragment manager: " + var10.mTarget));
                        }

                        if(var7.mSavedFragmentState == null) {
                           var7.mSavedFragmentState = new Bundle();
                        }

                        this.putFragment(var7.mSavedFragmentState, "android:target_state", var10.mTarget);
                        if(var10.mTargetRequestCode != 0) {
                           var7.mSavedFragmentState.putInt("android:target_req_state", var10.mTargetRequestCode);
                        }
                     }
                  } else {
                     var7.mSavedFragmentState = var10.mSavedFragmentState;
                  }

                  var2 = var3;
                  if(DEBUG) {
                     Log.v("FragmentManager", "Saved state of " + var10 + ": " + var7.mSavedFragmentState);
                     var2 = var3;
                  }
               }
            }

            if(!var2) {
               var5 = var6;
               if(DEBUG) {
                  Log.v("FragmentManager", "saveAllState: no fragments!");
                  var5 = var6;
               }
            } else {
               var6 = null;
               var7 = null;
               int[] var11 = (int[])var6;
               int var9;
               if(this.mAdded != null) {
                  var9 = this.mAdded.size();
                  var11 = (int[])var6;
                  if(var9 > 0) {
                     int[] var12 = new int[var9];
                     var1 = 0;

                     while(true) {
                        var11 = var12;
                        if(var1 >= var9) {
                           break;
                        }

                        var12[var1] = ((Fragment)this.mAdded.get(var1)).mIndex;
                        if(var12[var1] < 0) {
                           this.throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(var1) + " has cleared index: " + var12[var1]));
                        }

                        if(DEBUG) {
                           Log.v("FragmentManager", "saveAllState: adding fragment #" + var1 + ": " + this.mAdded.get(var1));
                        }

                        ++var1;
                     }
                  }
               }

               var6 = var7;
               if(this.mBackStack != null) {
                  var9 = this.mBackStack.size();
                  var6 = var7;
                  if(var9 > 0) {
                     BackStackState[] var13 = new BackStackState[var9];
                     var1 = 0;

                     while(true) {
                        var6 = var13;
                        if(var1 >= var9) {
                           break;
                        }

                        var13[var1] = new BackStackState((BackStackRecord)this.mBackStack.get(var1));
                        if(DEBUG) {
                           Log.v("FragmentManager", "saveAllState: adding back stack #" + var1 + ": " + this.mBackStack.get(var1));
                        }

                        ++var1;
                     }
                  }
               }

               FragmentManagerState var14 = new FragmentManagerState();
               var14.mActive = var8;
               var14.mAdded = var11;
               var14.mBackStack = var6;
               var5 = var14;
            }
         }
      }

      return var5;
   }

   Bundle saveFragmentBasicState(Fragment var1) {
      Bundle var3 = null;
      if(this.mStateBundle == null) {
         this.mStateBundle = new Bundle();
      }

      var1.performSaveInstanceState(this.mStateBundle);
      if(!this.mStateBundle.isEmpty()) {
         var3 = this.mStateBundle;
         this.mStateBundle = null;
      }

      if(var1.mView != null) {
         this.saveFragmentViewState(var1);
      }

      Bundle var2 = var3;
      if(var1.mSavedViewState != null) {
         var2 = var3;
         if(var3 == null) {
            var2 = new Bundle();
         }

         var2.putSparseParcelableArray("android:view_state", var1.mSavedViewState);
      }

      var3 = var2;
      if(!var1.mUserVisibleHint) {
         var3 = var2;
         if(var2 == null) {
            var3 = new Bundle();
         }

         var3.putBoolean("android:user_visible_hint", var1.mUserVisibleHint);
      }

      return var3;
   }

   public Fragment.SavedState saveFragmentInstanceState(Fragment var1) {
      Object var3 = null;
      if(var1.mIndex < 0) {
         this.throwException(new IllegalStateException("Fragment " + var1 + " is not currently in the FragmentManager"));
      }

      Fragment.SavedState var2 = (Fragment.SavedState)var3;
      if(var1.mState > 0) {
         Bundle var4 = this.saveFragmentBasicState(var1);
         var2 = (Fragment.SavedState)var3;
         if(var4 != null) {
            var2 = new Fragment.SavedState(var4);
         }
      }

      return var2;
   }

   void saveFragmentViewState(Fragment var1) {
      if(var1.mInnerView != null) {
         if(this.mStateArray == null) {
            this.mStateArray = new SparseArray();
         } else {
            this.mStateArray.clear();
         }

         var1.mInnerView.saveHierarchyState(this.mStateArray);
         if(this.mStateArray.size() > 0) {
            var1.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
         }
      }

   }

   public void setBackStackIndex(int param1, BackStackRecord param2) {
      // $FF: Couldn't be decompiled
   }

   public void showFragment(Fragment var1, int var2, int var3) {
      if(DEBUG) {
         Log.v("FragmentManager", "show: " + var1);
      }

      if(var1.mHidden) {
         var1.mHidden = false;
         if(var1.mView != null) {
            Animation var4 = this.loadAnimation(var1, var2, true, var3);
            if(var4 != null) {
               this.setHWLayerAnimListenerIfAlpha(var1.mView, var4);
               var1.mView.startAnimation(var4);
            }

            var1.mView.setVisibility(0);
         }

         if(var1.mAdded && var1.mHasMenu && var1.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
         }

         var1.onHiddenChanged(false);
      }

   }

   void startPendingDeferredFragments() {
      if(this.mActive != null) {
         for(int var1 = 0; var1 < this.mActive.size(); ++var1) {
            Fragment var2 = (Fragment)this.mActive.get(var1);
            if(var2 != null) {
               this.performPendingDeferredStart(var2);
            }
         }
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(128);
      var1.append("FragmentManager{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" in ");
      if(this.mParent != null) {
         DebugUtils.buildShortClassTag(this.mParent, var1);
      } else {
         DebugUtils.buildShortClassTag(this.mHost, var1);
      }

      var1.append("}}");
      return var1.toString();
   }

   static class AnimateOnHWLayerIfNeededListener implements AnimationListener {
      private AnimationListener mOriginalListener;
      private boolean mShouldRunOnHWLayer;
      View mView;

      public AnimateOnHWLayerIfNeededListener(View var1, Animation var2) {
         if(var1 != null && var2 != null) {
            this.mView = var1;
         }

      }

      public AnimateOnHWLayerIfNeededListener(View var1, Animation var2, AnimationListener var3) {
         if(var1 != null && var2 != null) {
            this.mOriginalListener = var3;
            this.mView = var1;
            this.mShouldRunOnHWLayer = true;
         }

      }

      @CallSuper
      public void onAnimationEnd(Animation var1) {
         if(this.mView != null && this.mShouldRunOnHWLayer) {
            if(!ViewCompat.isAttachedToWindow(this.mView) && !BuildCompat.isAtLeastN()) {
               ViewCompat.setLayerType(this.mView, 0, (Paint)null);
            } else {
               this.mView.post(new Runnable() {
                  public void run() {
                     ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, 0, (Paint)null);
                  }
               });
            }
         }

         if(this.mOriginalListener != null) {
            this.mOriginalListener.onAnimationEnd(var1);
         }

      }

      public void onAnimationRepeat(Animation var1) {
         if(this.mOriginalListener != null) {
            this.mOriginalListener.onAnimationRepeat(var1);
         }

      }

      @CallSuper
      public void onAnimationStart(Animation var1) {
         if(this.mOriginalListener != null) {
            this.mOriginalListener.onAnimationStart(var1);
         }

      }
   }

   static class FragmentTag {
      public static final int[] Fragment = new int[]{16842755, 16842960, 16842961};
      public static final int Fragment_id = 1;
      public static final int Fragment_name = 0;
      public static final int Fragment_tag = 2;
   }
}
