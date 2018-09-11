package com.bumptech.glide.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.RequestManagerFragment;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;

public class RequestManagerRetriever implements Callback {
   static final String FRAGMENT_TAG = "com.bumptech.glide.manager";
   private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
   private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
   private static final RequestManagerRetriever INSTANCE = new RequestManagerRetriever();
   private static final String TAG = "RMRetriever";
   private volatile RequestManager applicationManager;
   private final Handler handler = new Handler(Looper.getMainLooper(), this);
   final Map pendingRequestManagerFragments = new HashMap();
   final Map pendingSupportRequestManagerFragments = new HashMap();

   @TargetApi(17)
   private static void assertNotDestroyed(Activity var0) {
      if(VERSION.SDK_INT >= 17 && var0.isDestroyed()) {
         throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
      }
   }

   public static RequestManagerRetriever get() {
      return INSTANCE;
   }

   private RequestManager getApplicationManager(Context param1) {
      // $FF: Couldn't be decompiled
   }

   @TargetApi(11)
   RequestManager fragmentGet(Context var1, FragmentManager var2) {
      RequestManagerFragment var4 = this.getRequestManagerFragment(var2);
      RequestManager var3 = var4.getRequestManager();
      RequestManager var5 = var3;
      if(var3 == null) {
         var5 = new RequestManager(var1, var4.getLifecycle(), var4.getRequestManagerTreeNode());
         var4.setRequestManager(var5);
      }

      return var5;
   }

   @TargetApi(11)
   public RequestManager get(Activity var1) {
      RequestManager var2;
      if(!Util.isOnBackgroundThread() && VERSION.SDK_INT >= 11) {
         assertNotDestroyed(var1);
         var2 = this.fragmentGet(var1, var1.getFragmentManager());
      } else {
         var2 = this.get(var1.getApplicationContext());
      }

      return var2;
   }

   @TargetApi(17)
   public RequestManager get(Fragment var1) {
      if(var1.getActivity() == null) {
         throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
      } else {
         RequestManager var3;
         if(!Util.isOnBackgroundThread() && VERSION.SDK_INT >= 17) {
            FragmentManager var2 = var1.getChildFragmentManager();
            var3 = this.fragmentGet(var1.getActivity(), var2);
         } else {
            var3 = this.get(var1.getActivity().getApplicationContext());
         }

         return var3;
      }
   }

   public RequestManager get(Context var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("You cannot start a load on a null Context");
      } else {
         RequestManager var2;
         if(Util.isOnMainThread() && !(var1 instanceof Application)) {
            if(var1 instanceof FragmentActivity) {
               var2 = this.get((FragmentActivity)var1);
               return var2;
            }

            if(var1 instanceof Activity) {
               var2 = this.get((Activity)var1);
               return var2;
            }

            if(var1 instanceof ContextWrapper) {
               var2 = this.get(((ContextWrapper)var1).getBaseContext());
               return var2;
            }
         }

         var2 = this.getApplicationManager(var1);
         return var2;
      }
   }

   public RequestManager get(android.support.v4.app.Fragment var1) {
      if(var1.getActivity() == null) {
         throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
      } else {
         RequestManager var3;
         if(Util.isOnBackgroundThread()) {
            var3 = this.get(var1.getActivity().getApplicationContext());
         } else {
            android.support.v4.app.FragmentManager var2 = var1.getChildFragmentManager();
            var3 = this.supportFragmentGet(var1.getActivity(), var2);
         }

         return var3;
      }
   }

   public RequestManager get(FragmentActivity var1) {
      RequestManager var2;
      if(Util.isOnBackgroundThread()) {
         var2 = this.get(var1.getApplicationContext());
      } else {
         assertNotDestroyed(var1);
         var2 = this.supportFragmentGet(var1, var1.getSupportFragmentManager());
      }

      return var2;
   }

   @TargetApi(17)
   RequestManagerFragment getRequestManagerFragment(FragmentManager var1) {
      RequestManagerFragment var3 = (RequestManagerFragment)var1.findFragmentByTag("com.bumptech.glide.manager");
      RequestManagerFragment var2 = var3;
      if(var3 == null) {
         var3 = (RequestManagerFragment)this.pendingRequestManagerFragments.get(var1);
         var2 = var3;
         if(var3 == null) {
            var2 = new RequestManagerFragment();
            this.pendingRequestManagerFragments.put(var1, var2);
            var1.beginTransaction().add(var2, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(1, var1).sendToTarget();
         }
      }

      return var2;
   }

   SupportRequestManagerFragment getSupportRequestManagerFragment(android.support.v4.app.FragmentManager var1) {
      SupportRequestManagerFragment var3 = (SupportRequestManagerFragment)var1.findFragmentByTag("com.bumptech.glide.manager");
      SupportRequestManagerFragment var2 = var3;
      if(var3 == null) {
         var3 = (SupportRequestManagerFragment)this.pendingSupportRequestManagerFragments.get(var1);
         var2 = var3;
         if(var3 == null) {
            var2 = new SupportRequestManagerFragment();
            this.pendingSupportRequestManagerFragments.put(var1, var2);
            var1.beginTransaction().add(var2, "com.bumptech.glide.manager").commitAllowingStateLoss();
            this.handler.obtainMessage(2, var1).sendToTarget();
         }
      }

      return var2;
   }

   public boolean handleMessage(Message var1) {
      boolean var2 = true;
      Object var3 = null;
      Object var4 = null;
      Object var5;
      switch(var1.what) {
      case 1:
         FragmentManager var7 = (FragmentManager)var1.obj;
         var5 = var7;
         var3 = this.pendingRequestManagerFragments.remove(var7);
         break;
      case 2:
         android.support.v4.app.FragmentManager var6 = (android.support.v4.app.FragmentManager)var1.obj;
         var5 = var6;
         var3 = this.pendingSupportRequestManagerFragments.remove(var6);
         break;
      default:
         var2 = false;
         var5 = var4;
      }

      if(var2 && var3 == null && Log.isLoggable("RMRetriever", 5)) {
         Log.w("RMRetriever", "Failed to remove expected request manager fragment, manager: " + var5);
      }

      return var2;
   }

   RequestManager supportFragmentGet(Context var1, android.support.v4.app.FragmentManager var2) {
      SupportRequestManagerFragment var4 = this.getSupportRequestManagerFragment(var2);
      RequestManager var3 = var4.getRequestManager();
      RequestManager var5 = var3;
      if(var3 == null) {
         var5 = new RequestManager(var1, var4.getLifecycle(), var4.getRequestManagerTreeNode());
         var4.setRequestManager(var5);
      }

      return var5;
   }
}
