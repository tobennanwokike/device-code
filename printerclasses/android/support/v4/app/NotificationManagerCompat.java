package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompatApi24;
import android.support.v4.app.NotificationManagerCompatKitKat;
import android.support.v4.os.BuildCompat;
import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class NotificationManagerCompat {
   public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
   public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
   private static final NotificationManagerCompat.Impl IMPL;
   public static final int IMPORTANCE_DEFAULT = 3;
   public static final int IMPORTANCE_HIGH = 4;
   public static final int IMPORTANCE_LOW = 2;
   public static final int IMPORTANCE_MAX = 5;
   public static final int IMPORTANCE_MIN = 1;
   public static final int IMPORTANCE_NONE = 0;
   public static final int IMPORTANCE_UNSPECIFIED = -1000;
   static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;
   private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
   static final int SIDE_CHANNEL_BIND_FLAGS;
   private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
   private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
   private static final String TAG = "NotifManCompat";
   private static Set sEnabledNotificationListenerPackages = new HashSet();
   private static String sEnabledNotificationListeners;
   private static final Object sEnabledNotificationListenersLock = new Object();
   private static final Object sLock = new Object();
   private static NotificationManagerCompat.SideChannelManager sSideChannelManager;
   private final Context mContext;
   private final NotificationManager mNotificationManager;

   static {
      if(BuildCompat.isAtLeastN()) {
         IMPL = new NotificationManagerCompat.ImplApi24();
      } else if(VERSION.SDK_INT >= 19) {
         IMPL = new NotificationManagerCompat.ImplKitKat();
      } else if(VERSION.SDK_INT >= 14) {
         IMPL = new NotificationManagerCompat.ImplIceCreamSandwich();
      } else {
         IMPL = new NotificationManagerCompat.ImplBase();
      }

      SIDE_CHANNEL_BIND_FLAGS = IMPL.getSideChannelBindFlags();
   }

   private NotificationManagerCompat(Context var1) {
      this.mContext = var1;
      this.mNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
   }

   public static NotificationManagerCompat from(Context var0) {
      return new NotificationManagerCompat(var0);
   }

   public static Set getEnabledListenerPackages(Context param0) {
      // $FF: Couldn't be decompiled
   }

   private void pushSideChannelQueue(NotificationManagerCompat.Task param1) {
      // $FF: Couldn't be decompiled
   }

   private static boolean useSideChannelForNotification(Notification var0) {
      Bundle var2 = NotificationCompat.getExtras(var0);
      boolean var1;
      if(var2 != null && var2.getBoolean("android.support.useSideChannel")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean areNotificationsEnabled() {
      return IMPL.areNotificationsEnabled(this.mContext, this.mNotificationManager);
   }

   public void cancel(int var1) {
      this.cancel((String)null, var1);
   }

   public void cancel(String var1, int var2) {
      IMPL.cancelNotification(this.mNotificationManager, var1, var2);
      if(VERSION.SDK_INT <= 19) {
         this.pushSideChannelQueue(new NotificationManagerCompat.CancelTask(this.mContext.getPackageName(), var2, var1));
      }

   }

   public void cancelAll() {
      this.mNotificationManager.cancelAll();
      if(VERSION.SDK_INT <= 19) {
         this.pushSideChannelQueue(new NotificationManagerCompat.CancelTask(this.mContext.getPackageName()));
      }

   }

   public int getImportance() {
      return IMPL.getImportance(this.mNotificationManager);
   }

   public void notify(int var1, Notification var2) {
      this.notify((String)null, var1, var2);
   }

   public void notify(String var1, int var2, Notification var3) {
      if(useSideChannelForNotification(var3)) {
         this.pushSideChannelQueue(new NotificationManagerCompat.NotifyTask(this.mContext.getPackageName(), var2, var1, var3));
         IMPL.cancelNotification(this.mNotificationManager, var1, var2);
      } else {
         IMPL.postNotification(this.mNotificationManager, var1, var2, var3);
      }

   }

   private static class CancelTask implements NotificationManagerCompat.Task {
      final boolean all;
      final int id;
      final String packageName;
      final String tag;

      public CancelTask(String var1) {
         this.packageName = var1;
         this.id = 0;
         this.tag = null;
         this.all = true;
      }

      public CancelTask(String var1, int var2, String var3) {
         this.packageName = var1;
         this.id = var2;
         this.tag = var3;
         this.all = false;
      }

      public void send(INotificationSideChannel var1) throws RemoteException {
         if(this.all) {
            var1.cancelAll(this.packageName);
         } else {
            var1.cancel(this.packageName, this.id, this.tag);
         }

      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("CancelTask[");
         var1.append("packageName:").append(this.packageName);
         var1.append(", id:").append(this.id);
         var1.append(", tag:").append(this.tag);
         var1.append(", all:").append(this.all);
         var1.append("]");
         return var1.toString();
      }
   }

   interface Impl {
      boolean areNotificationsEnabled(Context var1, NotificationManager var2);

      void cancelNotification(NotificationManager var1, String var2, int var3);

      int getImportance(NotificationManager var1);

      int getSideChannelBindFlags();

      void postNotification(NotificationManager var1, String var2, int var3, Notification var4);
   }

   static class ImplApi24 extends NotificationManagerCompat.ImplKitKat {
      public boolean areNotificationsEnabled(Context var1, NotificationManager var2) {
         return NotificationManagerCompatApi24.areNotificationsEnabled(var2);
      }

      public int getImportance(NotificationManager var1) {
         return NotificationManagerCompatApi24.getImportance(var1);
      }
   }

   static class ImplBase implements NotificationManagerCompat.Impl {
      public boolean areNotificationsEnabled(Context var1, NotificationManager var2) {
         return true;
      }

      public void cancelNotification(NotificationManager var1, String var2, int var3) {
         var1.cancel(var2, var3);
      }

      public int getImportance(NotificationManager var1) {
         return -1000;
      }

      public int getSideChannelBindFlags() {
         return 1;
      }

      public void postNotification(NotificationManager var1, String var2, int var3, Notification var4) {
         var1.notify(var2, var3, var4);
      }
   }

   static class ImplIceCreamSandwich extends NotificationManagerCompat.ImplBase {
      public int getSideChannelBindFlags() {
         return 33;
      }
   }

   static class ImplKitKat extends NotificationManagerCompat.ImplIceCreamSandwich {
      public boolean areNotificationsEnabled(Context var1, NotificationManager var2) {
         return NotificationManagerCompatKitKat.areNotificationsEnabled(var1);
      }
   }

   private static class NotifyTask implements NotificationManagerCompat.Task {
      final int id;
      final Notification notif;
      final String packageName;
      final String tag;

      public NotifyTask(String var1, int var2, String var3, Notification var4) {
         this.packageName = var1;
         this.id = var2;
         this.tag = var3;
         this.notif = var4;
      }

      public void send(INotificationSideChannel var1) throws RemoteException {
         var1.notify(this.packageName, this.id, this.tag, this.notif);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("NotifyTask[");
         var1.append("packageName:").append(this.packageName);
         var1.append(", id:").append(this.id);
         var1.append(", tag:").append(this.tag);
         var1.append("]");
         return var1.toString();
      }
   }

   private static class ServiceConnectedEvent {
      final ComponentName componentName;
      final IBinder iBinder;

      public ServiceConnectedEvent(ComponentName var1, IBinder var2) {
         this.componentName = var1;
         this.iBinder = var2;
      }
   }

   private static class SideChannelManager implements Callback, ServiceConnection {
      private static final String KEY_BINDER = "binder";
      private static final int MSG_QUEUE_TASK = 0;
      private static final int MSG_RETRY_LISTENER_QUEUE = 3;
      private static final int MSG_SERVICE_CONNECTED = 1;
      private static final int MSG_SERVICE_DISCONNECTED = 2;
      private Set mCachedEnabledPackages = new HashSet();
      private final Context mContext;
      private final Handler mHandler;
      private final HandlerThread mHandlerThread;
      private final Map mRecordMap = new HashMap();

      public SideChannelManager(Context var1) {
         this.mContext = var1;
         this.mHandlerThread = new HandlerThread("NotificationManagerCompat");
         this.mHandlerThread.start();
         this.mHandler = new Handler(this.mHandlerThread.getLooper(), this);
      }

      private boolean ensureServiceBound(NotificationManagerCompat.ListenerRecord var1) {
         boolean var2;
         if(var1.bound) {
            var2 = true;
         } else {
            Intent var3 = (new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")).setComponent(var1.componentName);
            var1.bound = this.mContext.bindService(var3, this, NotificationManagerCompat.SIDE_CHANNEL_BIND_FLAGS);
            if(var1.bound) {
               var1.retryCount = 0;
            } else {
               Log.w("NotifManCompat", "Unable to bind to listener " + var1.componentName);
               this.mContext.unbindService(this);
            }

            var2 = var1.bound;
         }

         return var2;
      }

      private void ensureServiceUnbound(NotificationManagerCompat.ListenerRecord var1) {
         if(var1.bound) {
            this.mContext.unbindService(this);
            var1.bound = false;
         }

         var1.service = null;
      }

      private void handleQueueTask(NotificationManagerCompat.Task var1) {
         this.updateListenerMap();
         Iterator var3 = this.mRecordMap.values().iterator();

         while(var3.hasNext()) {
            NotificationManagerCompat.ListenerRecord var2 = (NotificationManagerCompat.ListenerRecord)var3.next();
            var2.taskQueue.add(var1);
            this.processListenerQueue(var2);
         }

      }

      private void handleRetryListenerQueue(ComponentName var1) {
         NotificationManagerCompat.ListenerRecord var2 = (NotificationManagerCompat.ListenerRecord)this.mRecordMap.get(var1);
         if(var2 != null) {
            this.processListenerQueue(var2);
         }

      }

      private void handleServiceConnected(ComponentName var1, IBinder var2) {
         NotificationManagerCompat.ListenerRecord var3 = (NotificationManagerCompat.ListenerRecord)this.mRecordMap.get(var1);
         if(var3 != null) {
            var3.service = INotificationSideChannel.Stub.asInterface(var2);
            var3.retryCount = 0;
            this.processListenerQueue(var3);
         }

      }

      private void handleServiceDisconnected(ComponentName var1) {
         NotificationManagerCompat.ListenerRecord var2 = (NotificationManagerCompat.ListenerRecord)this.mRecordMap.get(var1);
         if(var2 != null) {
            this.ensureServiceUnbound(var2);
         }

      }

      private void processListenerQueue(NotificationManagerCompat.ListenerRecord var1) {
         if(Log.isLoggable("NotifManCompat", 3)) {
            Log.d("NotifManCompat", "Processing component " + var1.componentName + ", " + var1.taskQueue.size() + " queued tasks");
         }

         if(!var1.taskQueue.isEmpty()) {
            if(this.ensureServiceBound(var1) && var1.service != null) {
               while(true) {
                  NotificationManagerCompat.Task var3 = (NotificationManagerCompat.Task)var1.taskQueue.peek();
                  if(var3 == null) {
                     break;
                  }

                  try {
                     if(Log.isLoggable("NotifManCompat", 3)) {
                        StringBuilder var2 = new StringBuilder();
                        Log.d("NotifManCompat", var2.append("Sending task ").append(var3).toString());
                     }

                     var3.send(var1.service);
                     var1.taskQueue.remove();
                  } catch (DeadObjectException var4) {
                     if(Log.isLoggable("NotifManCompat", 3)) {
                        Log.d("NotifManCompat", "Remote service has died: " + var1.componentName);
                     }
                     break;
                  } catch (RemoteException var5) {
                     Log.w("NotifManCompat", "RemoteException communicating with " + var1.componentName, var5);
                     break;
                  }
               }

               if(!var1.taskQueue.isEmpty()) {
                  this.scheduleListenerRetry(var1);
               }
            } else {
               this.scheduleListenerRetry(var1);
            }
         }

      }

      private void scheduleListenerRetry(NotificationManagerCompat.ListenerRecord var1) {
         if(!this.mHandler.hasMessages(3, var1.componentName)) {
            ++var1.retryCount;
            if(var1.retryCount > 6) {
               Log.w("NotifManCompat", "Giving up on delivering " + var1.taskQueue.size() + " tasks to " + var1.componentName + " after " + var1.retryCount + " retries");
               var1.taskQueue.clear();
            } else {
               int var2 = (1 << var1.retryCount - 1) * 1000;
               if(Log.isLoggable("NotifManCompat", 3)) {
                  Log.d("NotifManCompat", "Scheduling retry for " + var2 + " ms");
               }

               Message var3 = this.mHandler.obtainMessage(3, var1.componentName);
               this.mHandler.sendMessageDelayed(var3, (long)var2);
            }
         }

      }

      private void updateListenerMap() {
         Set var2 = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
         if(!var2.equals(this.mCachedEnabledPackages)) {
            this.mCachedEnabledPackages = var2;
            List var3 = this.mContext.getPackageManager().queryIntentServices((new Intent()).setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 4);
            HashSet var1 = new HashSet();
            Iterator var5 = var3.iterator();

            while(var5.hasNext()) {
               ResolveInfo var8 = (ResolveInfo)var5.next();
               if(var2.contains(var8.serviceInfo.packageName)) {
                  ComponentName var4 = new ComponentName(var8.serviceInfo.packageName, var8.serviceInfo.name);
                  if(var8.serviceInfo.permission != null) {
                     Log.w("NotifManCompat", "Permission present on component " + var4 + ", not adding listener record.");
                  } else {
                     var1.add(var4);
                  }
               }
            }

            Iterator var9 = var1.iterator();

            while(var9.hasNext()) {
               ComponentName var6 = (ComponentName)var9.next();
               if(!this.mRecordMap.containsKey(var6)) {
                  if(Log.isLoggable("NotifManCompat", 3)) {
                     Log.d("NotifManCompat", "Adding listener record for " + var6);
                  }

                  this.mRecordMap.put(var6, new NotificationManagerCompat.ListenerRecord(var6));
               }
            }

            Iterator var7 = this.mRecordMap.entrySet().iterator();

            while(var7.hasNext()) {
               Entry var10 = (Entry)var7.next();
               if(!var1.contains(var10.getKey())) {
                  if(Log.isLoggable("NotifManCompat", 3)) {
                     Log.d("NotifManCompat", "Removing listener record for " + var10.getKey());
                  }

                  this.ensureServiceUnbound((NotificationManagerCompat.ListenerRecord)var10.getValue());
                  var7.remove();
               }
            }
         }

      }

      public boolean handleMessage(Message var1) {
         boolean var2;
         switch(var1.what) {
         case 0:
            this.handleQueueTask((NotificationManagerCompat.Task)var1.obj);
            var2 = true;
            break;
         case 1:
            NotificationManagerCompat.ServiceConnectedEvent var3 = (NotificationManagerCompat.ServiceConnectedEvent)var1.obj;
            this.handleServiceConnected(var3.componentName, var3.iBinder);
            var2 = true;
            break;
         case 2:
            this.handleServiceDisconnected((ComponentName)var1.obj);
            var2 = true;
            break;
         case 3:
            this.handleRetryListenerQueue((ComponentName)var1.obj);
            var2 = true;
            break;
         default:
            var2 = false;
         }

         return var2;
      }

      public void onServiceConnected(ComponentName var1, IBinder var2) {
         if(Log.isLoggable("NotifManCompat", 3)) {
            Log.d("NotifManCompat", "Connected to service " + var1);
         }

         this.mHandler.obtainMessage(1, new NotificationManagerCompat.ServiceConnectedEvent(var1, var2)).sendToTarget();
      }

      public void onServiceDisconnected(ComponentName var1) {
         if(Log.isLoggable("NotifManCompat", 3)) {
            Log.d("NotifManCompat", "Disconnected from service " + var1);
         }

         this.mHandler.obtainMessage(2, var1).sendToTarget();
      }

      public void queueTask(NotificationManagerCompat.Task var1) {
         this.mHandler.obtainMessage(0, var1).sendToTarget();
      }
   }

   private static class ListenerRecord {
      public boolean bound = false;
      public final ComponentName componentName;
      public int retryCount = 0;
      public INotificationSideChannel service;
      public LinkedList taskQueue = new LinkedList();

      public ListenerRecord(ComponentName var1) {
         this.componentName = var1;
      }
   }

   private interface Task {
      void send(INotificationSideChannel var1) throws RemoteException;
   }
}
