package com.bumptech.glide.request.target;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class AppWidgetTarget extends SimpleTarget {
   private final ComponentName componentName;
   private final Context context;
   private final RemoteViews remoteViews;
   private final int viewId;
   private final int[] widgetIds;

   public AppWidgetTarget(Context var1, RemoteViews var2, int var3, int var4, int var5, ComponentName var6) {
      super(var4, var5);
      if(var1 == null) {
         throw new NullPointerException("Context can not be null!");
      } else if(var6 == null) {
         throw new NullPointerException("ComponentName can not be null!");
      } else if(var2 == null) {
         throw new NullPointerException("RemoteViews object can not be null!");
      } else {
         this.context = var1;
         this.remoteViews = var2;
         this.viewId = var3;
         this.componentName = var6;
         this.widgetIds = null;
      }
   }

   public AppWidgetTarget(Context var1, RemoteViews var2, int var3, int var4, int var5, int... var6) {
      super(var4, var5);
      if(var1 == null) {
         throw new NullPointerException("Context can not be null!");
      } else if(var6 == null) {
         throw new NullPointerException("WidgetIds can not be null!");
      } else if(var6.length == 0) {
         throw new IllegalArgumentException("WidgetIds must have length > 0");
      } else if(var2 == null) {
         throw new NullPointerException("RemoteViews object can not be null!");
      } else {
         this.context = var1;
         this.remoteViews = var2;
         this.viewId = var3;
         this.widgetIds = var6;
         this.componentName = null;
      }
   }

   public AppWidgetTarget(Context var1, RemoteViews var2, int var3, ComponentName var4) {
      this(var1, var2, var3, Integer.MIN_VALUE, Integer.MIN_VALUE, var4);
   }

   public AppWidgetTarget(Context var1, RemoteViews var2, int var3, int... var4) {
      this(var1, var2, var3, Integer.MIN_VALUE, Integer.MIN_VALUE, var4);
   }

   private void update() {
      AppWidgetManager var1 = AppWidgetManager.getInstance(this.context);
      if(this.componentName != null) {
         var1.updateAppWidget(this.componentName, this.remoteViews);
      } else {
         var1.updateAppWidget(this.widgetIds, this.remoteViews);
      }

   }

   public void onResourceReady(Bitmap var1, GlideAnimation var2) {
      this.remoteViews.setImageViewBitmap(this.viewId, var1);
      this.update();
   }
}
