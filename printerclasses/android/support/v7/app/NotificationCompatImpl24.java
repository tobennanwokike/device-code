package android.support.v7.app;

import android.app.Notification.DecoratedCustomViewStyle;
import android.app.Notification.DecoratedMediaCustomViewStyle;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

class NotificationCompatImpl24 {
   public static void addDecoratedCustomViewStyle(NotificationBuilderWithBuilderAccessor var0) {
      var0.getBuilder().setStyle(new DecoratedCustomViewStyle());
   }

   public static void addDecoratedMediaCustomViewStyle(NotificationBuilderWithBuilderAccessor var0) {
      var0.getBuilder().setStyle(new DecoratedMediaCustomViewStyle());
   }
}
