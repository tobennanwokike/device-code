package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Notification.BigPictureStyle;
import android.app.Notification.BigTextStyle;
import android.app.Notification.InboxStyle;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.BundleUtil;
import android.support.v4.app.NotificationBuilderWithActions;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.app.RemoteInputCompatBase;
import android.support.v4.app.RemoteInputCompatJellybean;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class NotificationCompatJellybean {
   static final String EXTRA_ACTION_EXTRAS = "android.support.actionExtras";
   static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
   static final String EXTRA_GROUP_KEY = "android.support.groupKey";
   static final String EXTRA_GROUP_SUMMARY = "android.support.isGroupSummary";
   static final String EXTRA_LOCAL_ONLY = "android.support.localOnly";
   static final String EXTRA_REMOTE_INPUTS = "android.support.remoteInputs";
   static final String EXTRA_SORT_KEY = "android.support.sortKey";
   static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
   private static final String KEY_ACTION_INTENT = "actionIntent";
   private static final String KEY_ALLOW_GENERATED_REPLIES = "allowGeneratedReplies";
   private static final String KEY_EXTRAS = "extras";
   private static final String KEY_ICON = "icon";
   private static final String KEY_REMOTE_INPUTS = "remoteInputs";
   private static final String KEY_TITLE = "title";
   public static final String TAG = "NotificationCompat";
   private static Class sActionClass;
   private static Field sActionIconField;
   private static Field sActionIntentField;
   private static Field sActionTitleField;
   private static boolean sActionsAccessFailed;
   private static Field sActionsField;
   private static final Object sActionsLock = new Object();
   private static Field sExtrasField;
   private static boolean sExtrasFieldAccessFailed;
   private static final Object sExtrasLock = new Object();

   public static void addBigPictureStyle(NotificationBuilderWithBuilderAccessor var0, CharSequence var1, boolean var2, CharSequence var3, Bitmap var4, Bitmap var5, boolean var6) {
      BigPictureStyle var7 = (new BigPictureStyle(var0.getBuilder())).setBigContentTitle(var1).bigPicture(var4);
      if(var6) {
         var7.bigLargeIcon(var5);
      }

      if(var2) {
         var7.setSummaryText(var3);
      }

   }

   public static void addBigTextStyle(NotificationBuilderWithBuilderAccessor var0, CharSequence var1, boolean var2, CharSequence var3, CharSequence var4) {
      BigTextStyle var5 = (new BigTextStyle(var0.getBuilder())).setBigContentTitle(var1).bigText(var4);
      if(var2) {
         var5.setSummaryText(var3);
      }

   }

   public static void addInboxStyle(NotificationBuilderWithBuilderAccessor var0, CharSequence var1, boolean var2, CharSequence var3, ArrayList var4) {
      InboxStyle var5 = (new InboxStyle(var0.getBuilder())).setBigContentTitle(var1);
      if(var2) {
         var5.setSummaryText(var3);
      }

      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         var5.addLine((CharSequence)var6.next());
      }

   }

   public static SparseArray buildActionExtrasMap(List var0) {
      SparseArray var3 = null;
      int var1 = 0;

      SparseArray var4;
      for(int var2 = var0.size(); var1 < var2; var3 = var4) {
         Bundle var5 = (Bundle)var0.get(var1);
         var4 = var3;
         if(var5 != null) {
            var4 = var3;
            if(var3 == null) {
               var4 = new SparseArray();
            }

            var4.put(var1, var5);
         }

         ++var1;
      }

      return var3;
   }

   private static boolean ensureActionReflectionReadyLocked() {
      boolean var0 = false;
      boolean var1 = true;
      if(!sActionsAccessFailed) {
         try {
            if(sActionsField == null) {
               sActionClass = Class.forName("android.app.Notification$Action");
               sActionIconField = sActionClass.getDeclaredField("icon");
               sActionTitleField = sActionClass.getDeclaredField("title");
               sActionIntentField = sActionClass.getDeclaredField("actionIntent");
               sActionsField = Notification.class.getDeclaredField("actions");
               sActionsField.setAccessible(true);
            }
         } catch (ClassNotFoundException var3) {
            Log.e("NotificationCompat", "Unable to access notification actions", var3);
            sActionsAccessFailed = true;
         } catch (NoSuchFieldException var4) {
            Log.e("NotificationCompat", "Unable to access notification actions", var4);
            sActionsAccessFailed = true;
         }

         if(!sActionsAccessFailed) {
            var0 = var1;
         } else {
            var0 = false;
         }
      }

      return var0;
   }

   public static NotificationCompatBase.Action getAction(Notification param0, int param1, NotificationCompatBase.Factory param2, RemoteInputCompatBase.Factory param3) {
      // $FF: Couldn't be decompiled
   }

   public static int getActionCount(Notification param0) {
      // $FF: Couldn't be decompiled
   }

   private static NotificationCompatBase.Action getActionFromBundle(Bundle var0, NotificationCompatBase.Factory var1, RemoteInputCompatBase.Factory var2) {
      return var1.build(var0.getInt("icon"), var0.getCharSequence("title"), (PendingIntent)var0.getParcelable("actionIntent"), var0.getBundle("extras"), RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(var0, "remoteInputs"), var2), var0.getBoolean("allowGeneratedReplies"));
   }

   private static Object[] getActionObjectsLocked(Notification param0) {
      // $FF: Couldn't be decompiled
   }

   public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(ArrayList var0, NotificationCompatBase.Factory var1, RemoteInputCompatBase.Factory var2) {
      NotificationCompatBase.Action[] var4;
      if(var0 == null) {
         var4 = null;
      } else {
         NotificationCompatBase.Action[] var5 = var1.newArray(var0.size());
         int var3 = 0;

         while(true) {
            var4 = var5;
            if(var3 >= var5.length) {
               break;
            }

            var5[var3] = getActionFromBundle((Bundle)var0.get(var3), var1, var2);
            ++var3;
         }
      }

      return var4;
   }

   private static Bundle getBundleForAction(NotificationCompatBase.Action var0) {
      Bundle var1 = new Bundle();
      var1.putInt("icon", var0.getIcon());
      var1.putCharSequence("title", var0.getTitle());
      var1.putParcelable("actionIntent", var0.getActionIntent());
      var1.putBundle("extras", var0.getExtras());
      var1.putParcelableArray("remoteInputs", RemoteInputCompatJellybean.toBundleArray(var0.getRemoteInputs()));
      return var1;
   }

   public static Bundle getExtras(Notification param0) {
      // $FF: Couldn't be decompiled
   }

   public static String getGroup(Notification var0) {
      return getExtras(var0).getString("android.support.groupKey");
   }

   public static boolean getLocalOnly(Notification var0) {
      return getExtras(var0).getBoolean("android.support.localOnly");
   }

   public static ArrayList getParcelableArrayListForActions(NotificationCompatBase.Action[] var0) {
      ArrayList var3;
      if(var0 == null) {
         var3 = null;
      } else {
         ArrayList var4 = new ArrayList(var0.length);
         int var2 = var0.length;
         int var1 = 0;

         while(true) {
            var3 = var4;
            if(var1 >= var2) {
               break;
            }

            var4.add(getBundleForAction(var0[var1]));
            ++var1;
         }
      }

      return var3;
   }

   public static String getSortKey(Notification var0) {
      return getExtras(var0).getString("android.support.sortKey");
   }

   public static boolean isGroupSummary(Notification var0) {
      return getExtras(var0).getBoolean("android.support.isGroupSummary");
   }

   public static NotificationCompatBase.Action readAction(NotificationCompatBase.Factory var0, RemoteInputCompatBase.Factory var1, int var2, CharSequence var3, PendingIntent var4, Bundle var5) {
      RemoteInputCompatBase.RemoteInput[] var7 = null;
      boolean var6 = false;
      if(var5 != null) {
         var7 = RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(var5, "android.support.remoteInputs"), var1);
         var6 = var5.getBoolean("android.support.allowGeneratedReplies");
      }

      return var0.build(var2, var3, var4, var5, var7, var6);
   }

   public static Bundle writeActionAndGetExtras(android.app.Notification.Builder var0, NotificationCompatBase.Action var1) {
      var0.addAction(var1.getIcon(), var1.getTitle(), var1.getActionIntent());
      Bundle var2 = new Bundle(var1.getExtras());
      if(var1.getRemoteInputs() != null) {
         var2.putParcelableArray("android.support.remoteInputs", RemoteInputCompatJellybean.toBundleArray(var1.getRemoteInputs()));
      }

      var2.putBoolean("android.support.allowGeneratedReplies", var1.getAllowGeneratedReplies());
      return var2;
   }

   public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
      private android.app.Notification.Builder b;
      private List mActionExtrasList = new ArrayList();
      private RemoteViews mBigContentView;
      private RemoteViews mContentView;
      private final Bundle mExtras;

      public Builder(Context var1, Notification var2, CharSequence var3, CharSequence var4, CharSequence var5, RemoteViews var6, int var7, PendingIntent var8, PendingIntent var9, Bitmap var10, int var11, int var12, boolean var13, boolean var14, int var15, CharSequence var16, boolean var17, Bundle var18, String var19, boolean var20, String var21, RemoteViews var22, RemoteViews var23) {
         android.app.Notification.Builder var25 = (new android.app.Notification.Builder(var1)).setWhen(var2.when).setSmallIcon(var2.icon, var2.iconLevel).setContent(var2.contentView).setTicker(var2.tickerText, var6).setSound(var2.sound, var2.audioStreamType).setVibrate(var2.vibrate).setLights(var2.ledARGB, var2.ledOnMS, var2.ledOffMS);
         boolean var24;
         if((var2.flags & 2) != 0) {
            var24 = true;
         } else {
            var24 = false;
         }

         var25 = var25.setOngoing(var24);
         if((var2.flags & 8) != 0) {
            var24 = true;
         } else {
            var24 = false;
         }

         var25 = var25.setOnlyAlertOnce(var24);
         if((var2.flags & 16) != 0) {
            var24 = true;
         } else {
            var24 = false;
         }

         var25 = var25.setAutoCancel(var24).setDefaults(var2.defaults).setContentTitle(var3).setContentText(var4).setSubText(var16).setContentInfo(var5).setContentIntent(var8).setDeleteIntent(var2.deleteIntent);
         if((var2.flags & 128) != 0) {
            var24 = true;
         } else {
            var24 = false;
         }

         this.b = var25.setFullScreenIntent(var9, var24).setLargeIcon(var10).setNumber(var7).setUsesChronometer(var14).setPriority(var15).setProgress(var11, var12, var13);
         this.mExtras = new Bundle();
         if(var18 != null) {
            this.mExtras.putAll(var18);
         }

         if(var17) {
            this.mExtras.putBoolean("android.support.localOnly", true);
         }

         if(var19 != null) {
            this.mExtras.putString("android.support.groupKey", var19);
            if(var20) {
               this.mExtras.putBoolean("android.support.isGroupSummary", true);
            } else {
               this.mExtras.putBoolean("android.support.useSideChannel", true);
            }
         }

         if(var21 != null) {
            this.mExtras.putString("android.support.sortKey", var21);
         }

         this.mContentView = var22;
         this.mBigContentView = var23;
      }

      public void addAction(NotificationCompatBase.Action var1) {
         this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.b, var1));
      }

      public Notification build() {
         Notification var1 = this.b.build();
         Bundle var2 = NotificationCompatJellybean.getExtras(var1);
         Bundle var5 = new Bundle(this.mExtras);
         Iterator var4 = this.mExtras.keySet().iterator();

         while(var4.hasNext()) {
            String var3 = (String)var4.next();
            if(var2.containsKey(var3)) {
               var5.remove(var3);
            }
         }

         var2.putAll(var5);
         SparseArray var6 = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
         if(var6 != null) {
            NotificationCompatJellybean.getExtras(var1).putSparseParcelableArray("android.support.actionExtras", var6);
         }

         if(this.mContentView != null) {
            var1.contentView = this.mContentView;
         }

         if(this.mBigContentView != null) {
            var1.bigContentView = this.mBigContentView;
         }

         return var1;
      }

      public android.app.Notification.Builder getBuilder() {
         return this.b;
      }
   }
}
