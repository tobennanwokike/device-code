package android.support.v4.app;

import android.app.NotificationManager;

class NotificationManagerCompatApi24 {
   public static boolean areNotificationsEnabled(NotificationManager var0) {
      return var0.areNotificationsEnabled();
   }

   public static int getImportance(NotificationManager var0) {
      return var0.getImportance();
   }
}
