package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import java.util.List;

public class MediaButtonReceiver extends BroadcastReceiver {
   private static final String TAG = "MediaButtonReceiver";

   public static PendingIntent buildMediaButtonPendingIntent(Context var0, long var1) {
      ComponentName var3 = getMediaButtonReceiverComponent(var0);
      PendingIntent var4;
      if(var3 == null) {
         Log.w("MediaButtonReceiver", "A unique media button receiver could not be found in the given context, so couldn\'t build a pending intent.");
         var4 = null;
      } else {
         var4 = buildMediaButtonPendingIntent(var0, var3, var1);
      }

      return var4;
   }

   public static PendingIntent buildMediaButtonPendingIntent(Context var0, ComponentName var1, long var2) {
      Intent var5 = null;
      PendingIntent var6;
      if(var1 == null) {
         Log.w("MediaButtonReceiver", "The component name of media button receiver should be provided.");
         var6 = var5;
      } else {
         int var4 = PlaybackStateCompat.toKeyCode(var2);
         if(var4 == 0) {
            Log.w("MediaButtonReceiver", "Cannot build a media button pending intent with the given action: " + var2);
            var6 = var5;
         } else {
            var5 = new Intent("android.intent.action.MEDIA_BUTTON");
            var5.setComponent(var1);
            var5.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, var4));
            var6 = PendingIntent.getBroadcast(var0, var4, var5, 0);
         }
      }

      return var6;
   }

   static ComponentName getMediaButtonReceiverComponent(Context var0) {
      Intent var1 = new Intent("android.intent.action.MEDIA_BUTTON");
      var1.setPackage(var0.getPackageName());
      List var2 = var0.getPackageManager().queryBroadcastReceivers(var1, 0);
      ComponentName var4;
      if(var2.size() == 1) {
         ResolveInfo var3 = (ResolveInfo)var2.get(0);
         var4 = new ComponentName(var3.activityInfo.packageName, var3.activityInfo.name);
      } else {
         if(var2.size() > 1) {
            Log.w("MediaButtonReceiver", "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, returning null.");
         }

         var4 = null;
      }

      return var4;
   }

   public static KeyEvent handleIntent(MediaSessionCompat var0, Intent var1) {
      KeyEvent var2;
      if(var0 != null && var1 != null && "android.intent.action.MEDIA_BUTTON".equals(var1.getAction()) && var1.hasExtra("android.intent.extra.KEY_EVENT")) {
         KeyEvent var3 = (KeyEvent)var1.getParcelableExtra("android.intent.extra.KEY_EVENT");
         var0.getController().dispatchMediaButtonEvent(var3);
         var2 = var3;
      } else {
         var2 = null;
      }

      return var2;
   }

   public void onReceive(Context var1, Intent var2) {
      Intent var5 = new Intent("android.intent.action.MEDIA_BUTTON");
      var5.setPackage(var1.getPackageName());
      PackageManager var6 = var1.getPackageManager();
      List var4 = var6.queryIntentServices(var5, 0);
      List var3 = var4;
      if(var4.isEmpty()) {
         var5.setAction("android.media.browse.MediaBrowserService");
         var3 = var6.queryIntentServices(var5, 0);
      }

      if(var3.isEmpty()) {
         throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or a media browser service implementation");
      } else if(var3.size() != 1) {
         throw new IllegalStateException("Expected 1 Service that handles " + var5.getAction() + ", found " + var3.size());
      } else {
         ResolveInfo var7 = (ResolveInfo)var3.get(0);
         var2.setComponent(new ComponentName(var7.serviceInfo.packageName, var7.serviceInfo.name));
         var1.startService(var2);
      }
   }
}
