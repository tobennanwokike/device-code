package android.support.v7.app;

import android.app.Notification.BigTextStyle;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

class NotificationCompatImplJellybean {
   public static void addBigTextStyle(NotificationBuilderWithBuilderAccessor var0, CharSequence var1) {
      (new BigTextStyle(var0.getBuilder())).bigText(var1);
   }
}
