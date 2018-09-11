package android.support.v4.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompatApi21;
import android.support.v4.app.ActivityCompatApi23;
import android.support.v4.app.ActivityCompatHoneycomb;
import android.support.v4.app.BaseFragmentActivityJB;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class FragmentActivity extends BaseFragmentActivityJB implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompatApi23.RequestPermissionsRequestCodeValidator {
   static final String ALLOCATED_REQUEST_INDICIES_TAG = "android:support:request_indicies";
   static final String FRAGMENTS_TAG = "android:support:fragments";
   private static final int HONEYCOMB = 11;
   static final int MAX_NUM_PENDING_FRAGMENT_ACTIVITY_RESULTS = 65534;
   static final int MSG_REALLY_STOPPED = 1;
   static final int MSG_RESUME_PENDING = 2;
   static final String NEXT_CANDIDATE_REQUEST_INDEX_TAG = "android:support:next_request_index";
   static final String REQUEST_FRAGMENT_WHO_TAG = "android:support:request_fragment_who";
   private static final String TAG = "FragmentActivity";
   boolean mCreated;
   final FragmentController mFragments = FragmentController.createController(new FragmentActivity.HostCallbacks());
   final Handler mHandler = new Handler() {
      public void handleMessage(Message var1) {
         switch(var1.what) {
         case 1:
            if(FragmentActivity.this.mStopped) {
               FragmentActivity.this.doReallyStop(false);
            }
            break;
         case 2:
            FragmentActivity.this.onResumeFragments();
            FragmentActivity.this.mFragments.execPendingActions();
            break;
         default:
            super.handleMessage(var1);
         }

      }
   };
   MediaControllerCompat mMediaController;
   int mNextCandidateRequestIndex;
   boolean mOptionsMenuInvalidated;
   SparseArrayCompat mPendingFragmentActivityResults;
   boolean mReallyStopped;
   boolean mRequestedPermissionsFromFragment;
   boolean mResumed;
   boolean mRetaining;
   boolean mStopped;

   private int allocateRequestIndex(Fragment var1) {
      if(this.mPendingFragmentActivityResults.size() >= '\ufffe') {
         throw new IllegalStateException("Too many pending Fragment activity results.");
      } else {
         while(this.mPendingFragmentActivityResults.indexOfKey(this.mNextCandidateRequestIndex) >= 0) {
            this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % '\ufffe';
         }

         int var2 = this.mNextCandidateRequestIndex;
         this.mPendingFragmentActivityResults.put(var2, var1.mWho);
         this.mNextCandidateRequestIndex = (this.mNextCandidateRequestIndex + 1) % '\ufffe';
         return var2;
      }
   }

   private void dumpViewHierarchy(String var1, PrintWriter var2, View var3) {
      var2.print(var1);
      if(var3 == null) {
         var2.println("null");
      } else {
         var2.println(viewToString(var3));
         if(var3 instanceof ViewGroup) {
            ViewGroup var6 = (ViewGroup)var3;
            int var5 = var6.getChildCount();
            if(var5 > 0) {
               var1 = var1 + "  ";

               for(int var4 = 0; var4 < var5; ++var4) {
                  this.dumpViewHierarchy(var1, var2, var6.getChildAt(var4));
               }
            }
         }
      }

   }

   private static String viewToString(View param0) {
      // $FF: Couldn't be decompiled
   }

   final View dispatchFragmentsOnCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.mFragments.onCreateView(var1, var2, var3, var4);
   }

   void doReallyStop(boolean var1) {
      if(!this.mReallyStopped) {
         this.mReallyStopped = true;
         this.mRetaining = var1;
         this.mHandler.removeMessages(1);
         this.onReallyStop();
      } else if(var1) {
         this.mFragments.doLoaderStart();
         this.mFragments.doLoaderStop(true);
      }

   }

   public void dump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      if(VERSION.SDK_INT >= 11) {
         ;
      }

      var3.print(var1);
      var3.print("Local FragmentActivity ");
      var3.print(Integer.toHexString(System.identityHashCode(this)));
      var3.println(" State:");
      String var5 = var1 + "  ";
      var3.print(var5);
      var3.print("mCreated=");
      var3.print(this.mCreated);
      var3.print("mResumed=");
      var3.print(this.mResumed);
      var3.print(" mStopped=");
      var3.print(this.mStopped);
      var3.print(" mReallyStopped=");
      var3.println(this.mReallyStopped);
      this.mFragments.dumpLoaders(var5, var2, var3, var4);
      this.mFragments.getSupportFragmentManager().dump(var1, var2, var3, var4);
      var3.print(var1);
      var3.println("View Hierarchy:");
      this.dumpViewHierarchy(var1 + "  ", var3, this.getWindow().getDecorView());
   }

   public Object getLastCustomNonConfigurationInstance() {
      FragmentActivity.NonConfigurationInstances var1 = (FragmentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
      Object var2;
      if(var1 != null) {
         var2 = var1.custom;
      } else {
         var2 = null;
      }

      return var2;
   }

   public FragmentManager getSupportFragmentManager() {
      return this.mFragments.getSupportFragmentManager();
   }

   public LoaderManager getSupportLoaderManager() {
      return this.mFragments.getSupportLoaderManager();
   }

   public final MediaControllerCompat getSupportMediaController() {
      return this.mMediaController;
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      this.mFragments.noteStateNotSaved();
      int var4 = var1 >> 16;
      if(var4 != 0) {
         --var4;
         String var6 = (String)this.mPendingFragmentActivityResults.get(var4);
         this.mPendingFragmentActivityResults.remove(var4);
         if(var6 == null) {
            Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
         } else {
            Fragment var5 = this.mFragments.findFragmentByWho(var6);
            if(var5 == null) {
               Log.w("FragmentActivity", "Activity result no fragment exists for who: " + var6);
            } else {
               var5.onActivityResult('\uffff' & var1, var2, var3);
            }
         }
      } else {
         super.onActivityResult(var1, var2, var3);
      }

   }

   public void onAttachFragment(Fragment var1) {
   }

   public void onBackPressed() {
      if(!this.mFragments.getSupportFragmentManager().popBackStackImmediate()) {
         super.onBackPressed();
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.mFragments.dispatchConfigurationChanged(var1);
   }

   protected void onCreate(@Nullable Bundle var1) {
      FragmentManagerNonConfig var3 = null;
      this.mFragments.attachHost((Fragment)null);
      super.onCreate(var1);
      FragmentActivity.NonConfigurationInstances var6 = (FragmentActivity.NonConfigurationInstances)this.getLastNonConfigurationInstance();
      if(var6 != null) {
         this.mFragments.restoreLoaderNonConfig(var6.loaders);
      }

      if(var1 != null) {
         Parcelable var5 = var1.getParcelable("android:support:fragments");
         FragmentController var4 = this.mFragments;
         if(var6 != null) {
            var3 = var6.fragments;
         }

         var4.restoreAllState(var5, var3);
         if(var1.containsKey("android:support:next_request_index")) {
            this.mNextCandidateRequestIndex = var1.getInt("android:support:next_request_index");
            int[] var8 = var1.getIntArray("android:support:request_indicies");
            String[] var7 = var1.getStringArray("android:support:request_fragment_who");
            if(var8 != null && var7 != null && var8.length == var7.length) {
               this.mPendingFragmentActivityResults = new SparseArrayCompat(var8.length);

               for(int var2 = 0; var2 < var8.length; ++var2) {
                  this.mPendingFragmentActivityResults.put(var8[var2], var7[var2]);
               }
            } else {
               Log.w("FragmentActivity", "Invalid requestCode mapping in savedInstanceState.");
            }
         }
      }

      if(this.mPendingFragmentActivityResults == null) {
         this.mPendingFragmentActivityResults = new SparseArrayCompat();
         this.mNextCandidateRequestIndex = 0;
      }

      this.mFragments.dispatchCreate();
   }

   public boolean onCreatePanelMenu(int var1, Menu var2) {
      boolean var3;
      if(var1 == 0) {
         var3 = super.onCreatePanelMenu(var1, var2) | this.mFragments.dispatchCreateOptionsMenu(var2, this.getMenuInflater());
         if(VERSION.SDK_INT < 11) {
            var3 = true;
         }
      } else {
         var3 = super.onCreatePanelMenu(var1, var2);
      }

      return var3;
   }

   protected void onDestroy() {
      super.onDestroy();
      this.doReallyStop(false);
      this.mFragments.dispatchDestroy();
      this.mFragments.doLoaderDestroy();
   }

   public void onLowMemory() {
      super.onLowMemory();
      this.mFragments.dispatchLowMemory();
   }

   public boolean onMenuItemSelected(int var1, MenuItem var2) {
      boolean var3;
      if(super.onMenuItemSelected(var1, var2)) {
         var3 = true;
      } else {
         switch(var1) {
         case 0:
            var3 = this.mFragments.dispatchOptionsItemSelected(var2);
            break;
         case 6:
            var3 = this.mFragments.dispatchContextItemSelected(var2);
            break;
         default:
            var3 = false;
         }
      }

      return var3;
   }

   @CallSuper
   public void onMultiWindowModeChanged(boolean var1) {
      this.mFragments.dispatchMultiWindowModeChanged(var1);
   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      this.mFragments.noteStateNotSaved();
   }

   public void onPanelClosed(int var1, Menu var2) {
      switch(var1) {
      case 0:
         this.mFragments.dispatchOptionsMenuClosed(var2);
      default:
         super.onPanelClosed(var1, var2);
      }
   }

   protected void onPause() {
      super.onPause();
      this.mResumed = false;
      if(this.mHandler.hasMessages(2)) {
         this.mHandler.removeMessages(2);
         this.onResumeFragments();
      }

      this.mFragments.dispatchPause();
   }

   @CallSuper
   public void onPictureInPictureModeChanged(boolean var1) {
      this.mFragments.dispatchPictureInPictureModeChanged(var1);
   }

   protected void onPostResume() {
      super.onPostResume();
      this.mHandler.removeMessages(2);
      this.onResumeFragments();
      this.mFragments.execPendingActions();
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   protected boolean onPrepareOptionsPanel(View var1, Menu var2) {
      return super.onPreparePanel(0, var1, var2);
   }

   public boolean onPreparePanel(int var1, View var2, Menu var3) {
      boolean var4;
      if(var1 == 0 && var3 != null) {
         if(this.mOptionsMenuInvalidated) {
            this.mOptionsMenuInvalidated = false;
            var3.clear();
            this.onCreatePanelMenu(var1, var3);
         }

         var4 = this.onPrepareOptionsPanel(var2, var3) | this.mFragments.dispatchPrepareOptionsMenu(var3);
      } else {
         var4 = super.onPreparePanel(var1, var2, var3);
      }

      return var4;
   }

   void onReallyStop() {
      this.mFragments.doLoaderStop(this.mRetaining);
      this.mFragments.dispatchReallyStop();
   }

   public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
      int var4 = var1 >> 16 & '\uffff';
      if(var4 != 0) {
         --var4;
         String var5 = (String)this.mPendingFragmentActivityResults.get(var4);
         this.mPendingFragmentActivityResults.remove(var4);
         if(var5 == null) {
            Log.w("FragmentActivity", "Activity result delivered for unknown Fragment.");
         } else {
            Fragment var6 = this.mFragments.findFragmentByWho(var5);
            if(var6 == null) {
               Log.w("FragmentActivity", "Activity result no fragment exists for who: " + var5);
            } else {
               var6.onRequestPermissionsResult(var1 & '\uffff', var2, var3);
            }
         }
      }

   }

   protected void onResume() {
      super.onResume();
      this.mHandler.sendEmptyMessage(2);
      this.mResumed = true;
      this.mFragments.execPendingActions();
   }

   protected void onResumeFragments() {
      this.mFragments.dispatchResume();
   }

   public Object onRetainCustomNonConfigurationInstance() {
      return null;
   }

   public final Object onRetainNonConfigurationInstance() {
      if(this.mStopped) {
         this.doReallyStop(true);
      }

      Object var2 = this.onRetainCustomNonConfigurationInstance();
      FragmentManagerNonConfig var4 = this.mFragments.retainNestedNonConfig();
      SimpleArrayMap var3 = this.mFragments.retainLoaderNonConfig();
      FragmentActivity.NonConfigurationInstances var1;
      if(var4 == null && var3 == null && var2 == null) {
         var1 = null;
      } else {
         var1 = new FragmentActivity.NonConfigurationInstances();
         var1.custom = var2;
         var1.fragments = var4;
         var1.loaders = var3;
      }

      return var1;
   }

   protected void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      Parcelable var3 = this.mFragments.saveAllState();
      if(var3 != null) {
         var1.putParcelable("android:support:fragments", var3);
      }

      if(this.mPendingFragmentActivityResults.size() > 0) {
         var1.putInt("android:support:next_request_index", this.mNextCandidateRequestIndex);
         int[] var4 = new int[this.mPendingFragmentActivityResults.size()];
         String[] var5 = new String[this.mPendingFragmentActivityResults.size()];

         for(int var2 = 0; var2 < this.mPendingFragmentActivityResults.size(); ++var2) {
            var4[var2] = this.mPendingFragmentActivityResults.keyAt(var2);
            var5[var2] = (String)this.mPendingFragmentActivityResults.valueAt(var2);
         }

         var1.putIntArray("android:support:request_indicies", var4);
         var1.putStringArray("android:support:request_fragment_who", var5);
      }

   }

   protected void onStart() {
      super.onStart();
      this.mStopped = false;
      this.mReallyStopped = false;
      this.mHandler.removeMessages(1);
      if(!this.mCreated) {
         this.mCreated = true;
         this.mFragments.dispatchActivityCreated();
      }

      this.mFragments.noteStateNotSaved();
      this.mFragments.execPendingActions();
      this.mFragments.doLoaderStart();
      this.mFragments.dispatchStart();
      this.mFragments.reportLoaderStart();
   }

   public void onStateNotSaved() {
      this.mFragments.noteStateNotSaved();
   }

   protected void onStop() {
      super.onStop();
      this.mStopped = true;
      this.mHandler.sendEmptyMessage(1);
      this.mFragments.dispatchStop();
   }

   void requestPermissionsFromFragment(Fragment var1, String[] var2, int var3) {
      if(var3 == -1) {
         ActivityCompat.requestPermissions(this, var2, var3);
      } else {
         checkForValidRequestCode(var3);

         try {
            this.mRequestedPermissionsFromFragment = true;
            ActivityCompat.requestPermissions(this, var2, (this.allocateRequestIndex(var1) + 1 << 16) + ('\uffff' & var3));
         } finally {
            this.mRequestedPermissionsFromFragment = false;
         }
      }

   }

   public void setEnterSharedElementCallback(SharedElementCallback var1) {
      ActivityCompat.setEnterSharedElementCallback(this, var1);
   }

   public void setExitSharedElementCallback(SharedElementCallback var1) {
      ActivityCompat.setExitSharedElementCallback(this, var1);
   }

   public final void setSupportMediaController(MediaControllerCompat var1) {
      this.mMediaController = var1;
      if(VERSION.SDK_INT >= 21) {
         ActivityCompatApi21.setMediaController(this, var1.getMediaController());
      }

   }

   public void startActivityForResult(Intent var1, int var2) {
      if(!this.mStartedActivityFromFragment && var2 != -1) {
         checkForValidRequestCode(var2);
      }

      super.startActivityForResult(var1, var2);
   }

   public void startActivityFromFragment(Fragment var1, Intent var2, int var3) {
      this.startActivityFromFragment(var1, var2, var3, (Bundle)null);
   }

   public void startActivityFromFragment(Fragment param1, Intent param2, int param3, @Nullable Bundle param4) {
      // $FF: Couldn't be decompiled
   }

   public void startIntentSenderFromFragment(Fragment param1, IntentSender param2, int param3, @Nullable Intent param4, int param5, int param6, int param7, Bundle param8) throws SendIntentException {
      // $FF: Couldn't be decompiled
   }

   public void supportFinishAfterTransition() {
      ActivityCompat.finishAfterTransition(this);
   }

   public void supportInvalidateOptionsMenu() {
      if(VERSION.SDK_INT >= 11) {
         ActivityCompatHoneycomb.invalidateOptionsMenu(this);
      } else {
         this.mOptionsMenuInvalidated = true;
      }

   }

   public void supportPostponeEnterTransition() {
      ActivityCompat.postponeEnterTransition(this);
   }

   public void supportStartPostponedEnterTransition() {
      ActivityCompat.startPostponedEnterTransition(this);
   }

   public final void validateRequestPermissionsRequestCode(int var1) {
      if(!this.mRequestedPermissionsFromFragment && var1 != -1) {
         checkForValidRequestCode(var1);
      }

   }

   class HostCallbacks extends FragmentHostCallback {
      public HostCallbacks() {
         super(FragmentActivity.this);
      }

      public void onAttachFragment(Fragment var1) {
         FragmentActivity.this.onAttachFragment(var1);
      }

      public void onDump(String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
         FragmentActivity.this.dump(var1, var2, var3, var4);
      }

      @Nullable
      public View onFindViewById(int var1) {
         return FragmentActivity.this.findViewById(var1);
      }

      public FragmentActivity onGetHost() {
         return FragmentActivity.this;
      }

      public LayoutInflater onGetLayoutInflater() {
         return FragmentActivity.this.getLayoutInflater().cloneInContext(FragmentActivity.this);
      }

      public int onGetWindowAnimations() {
         Window var2 = FragmentActivity.this.getWindow();
         int var1;
         if(var2 == null) {
            var1 = 0;
         } else {
            var1 = var2.getAttributes().windowAnimations;
         }

         return var1;
      }

      public boolean onHasView() {
         Window var2 = FragmentActivity.this.getWindow();
         boolean var1;
         if(var2 != null && var2.peekDecorView() != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean onHasWindowAnimations() {
         boolean var1;
         if(FragmentActivity.this.getWindow() != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void onRequestPermissionsFromFragment(@NonNull Fragment var1, @NonNull String[] var2, int var3) {
         FragmentActivity.this.requestPermissionsFromFragment(var1, var2, var3);
      }

      public boolean onShouldSaveFragmentState(Fragment var1) {
         boolean var2;
         if(!FragmentActivity.this.isFinishing()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean onShouldShowRequestPermissionRationale(@NonNull String var1) {
         return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, var1);
      }

      public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3) {
         FragmentActivity.this.startActivityFromFragment(var1, var2, var3);
      }

      public void onStartActivityFromFragment(Fragment var1, Intent var2, int var3, @Nullable Bundle var4) {
         FragmentActivity.this.startActivityFromFragment(var1, var2, var3, var4);
      }

      public void onStartIntentSenderFromFragment(Fragment var1, IntentSender var2, int var3, @Nullable Intent var4, int var5, int var6, int var7, Bundle var8) throws SendIntentException {
         FragmentActivity.this.startIntentSenderFromFragment(var1, var2, var3, var4, var5, var6, var7, var8);
      }

      public void onSupportInvalidateOptionsMenu() {
         FragmentActivity.this.supportInvalidateOptionsMenu();
      }
   }

   static final class NonConfigurationInstances {
      Object custom;
      FragmentManagerNonConfig fragments;
      SimpleArrayMap loaders;
   }
}
