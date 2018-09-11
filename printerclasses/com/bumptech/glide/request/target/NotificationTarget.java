package com.bumptech.glide.request.target;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class NotificationTarget extends SimpleTarget {
   private final Context context;
   private final Notification notification;
   private final int notificationId;
   private final RemoteViews remoteViews;
   private final int viewId;

   public NotificationTarget(Context var1, RemoteViews var2, int var3, int var4, int var5, Notification var6, int var7) {
      super(var4, var5);
      if(var1 == null) {
         throw new NullPointerException("Context must not be null!");
      } else if(var6 == null) {
         throw new NullPointerException("Notification object can not be null!");
      } else if(var2 == null) {
         throw new NullPointerException("RemoteViews object can not be null!");
      } else {
         this.context = var1;
         this.viewId = var3;
         this.notification = var6;
         this.notificationId = var7;
         this.remoteViews = var2;
      }
   }

   public NotificationTarget(Context var1, RemoteViews var2, int var3, Notification var4, int var5) {
      this(var1, var2, var3, Integer.MIN_VALUE, Integer.MIN_VALUE, var4, var5);
   }

   private void update() {
      ((NotificationManager)this.context.getSystemService("notification")).notify(this.notificationId, this.notification);
   }

   public void onResourceReady(Bitmap var1, GlideAnimation var2) {
      this.remoteViews.setImageViewBitmap(this.viewId, var1);
      this.update();
   }
}
