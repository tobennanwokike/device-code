package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.text.BidiFormatter;
import android.support.v7.app.NotificationCompatImpl21;
import android.support.v7.app.NotificationCompatImpl24;
import android.support.v7.app.NotificationCompatImplBase;
import android.support.v7.app.NotificationCompatImplJellybean;
import android.support.v7.appcompat.R;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.List;

public class NotificationCompat extends android.support.v4.app.NotificationCompat {
   private static void addBigStyleToBuilderJellybean(Notification var0, android.support.v4.app.NotificationCompat.Builder var1) {
      if(var1.mStyle instanceof NotificationCompat.MediaStyle) {
         NotificationCompat.MediaStyle var4 = (NotificationCompat.MediaStyle)var1.mStyle;
         RemoteViews var3;
         if(var1.getBigContentView() != null) {
            var3 = var1.getBigContentView();
         } else {
            var3 = var1.getContentView();
         }

         boolean var2;
         if(var1.mStyle instanceof NotificationCompat.DecoratedMediaCustomViewStyle && var3 != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         NotificationCompatImplBase.overrideMediaBigContentView(var0, var1.mContext, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mNumber, var1.mLargeIcon, var1.mSubText, var1.mUseChronometer, var1.getWhenIfShowing(), var1.getPriority(), 0, var1.mActions, var4.mShowCancelButton, var4.mCancelButtonIntent, var2);
         if(var2) {
            NotificationCompatImplBase.buildIntoRemoteViews(var1.mContext, var0.bigContentView, var3);
         }
      } else if(var1.mStyle instanceof NotificationCompat.DecoratedCustomViewStyle) {
         addDecoratedBigStyleToBuilder(var0, var1);
      }

   }

   private static void addBigStyleToBuilderLollipop(Notification var0, android.support.v4.app.NotificationCompat.Builder var1) {
      RemoteViews var2;
      if(var1.getBigContentView() != null) {
         var2 = var1.getBigContentView();
      } else {
         var2 = var1.getContentView();
      }

      if(var1.mStyle instanceof NotificationCompat.DecoratedMediaCustomViewStyle && var2 != null) {
         NotificationCompatImplBase.overrideMediaBigContentView(var0, var1.mContext, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mNumber, var1.mLargeIcon, var1.mSubText, var1.mUseChronometer, var1.getWhenIfShowing(), var1.getPriority(), 0, var1.mActions, false, (PendingIntent)null, true);
         NotificationCompatImplBase.buildIntoRemoteViews(var1.mContext, var0.bigContentView, var2);
         setBackgroundColor(var1.mContext, var0.bigContentView, var1.getColor());
      } else if(var1.mStyle instanceof NotificationCompat.DecoratedCustomViewStyle) {
         addDecoratedBigStyleToBuilder(var0, var1);
      }

   }

   private static void addDecoratedBigStyleToBuilder(Notification var0, android.support.v4.app.NotificationCompat.Builder var1) {
      RemoteViews var2 = var1.getBigContentView();
      if(var2 == null) {
         var2 = var1.getContentView();
      }

      if(var2 != null) {
         RemoteViews var3 = NotificationCompatImplBase.applyStandardTemplateWithActions(var1.mContext, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mNumber, var0.icon, var1.mLargeIcon, var1.mSubText, var1.mUseChronometer, var1.getWhenIfShowing(), var1.getPriority(), var1.getColor(), R.layout.notification_template_custom_big, false, var1.mActions);
         NotificationCompatImplBase.buildIntoRemoteViews(var1.mContext, var3, var2);
         var0.bigContentView = var3;
      }

   }

   private static void addDecoratedHeadsUpToBuilder(Notification var0, android.support.v4.app.NotificationCompat.Builder var1) {
      RemoteViews var3 = var1.getHeadsUpContentView();
      RemoteViews var2;
      if(var3 != null) {
         var2 = var3;
      } else {
         var2 = var1.getContentView();
      }

      if(var3 != null) {
         var3 = NotificationCompatImplBase.applyStandardTemplateWithActions(var1.mContext, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mNumber, var0.icon, var1.mLargeIcon, var1.mSubText, var1.mUseChronometer, var1.getWhenIfShowing(), var1.getPriority(), var1.getColor(), R.layout.notification_template_custom_big, false, var1.mActions);
         NotificationCompatImplBase.buildIntoRemoteViews(var1.mContext, var3, var2);
         var0.headsUpContentView = var3;
      }

   }

   private static void addHeadsUpToBuilderLollipop(Notification var0, android.support.v4.app.NotificationCompat.Builder var1) {
      RemoteViews var2;
      if(var1.getHeadsUpContentView() != null) {
         var2 = var1.getHeadsUpContentView();
      } else {
         var2 = var1.getContentView();
      }

      if(var1.mStyle instanceof NotificationCompat.DecoratedMediaCustomViewStyle && var2 != null) {
         var0.headsUpContentView = NotificationCompatImplBase.generateMediaBigView(var1.mContext, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mNumber, var1.mLargeIcon, var1.mSubText, var1.mUseChronometer, var1.getWhenIfShowing(), var1.getPriority(), 0, var1.mActions, false, (PendingIntent)null, true);
         NotificationCompatImplBase.buildIntoRemoteViews(var1.mContext, var0.headsUpContentView, var2);
         setBackgroundColor(var1.mContext, var0.headsUpContentView, var1.getColor());
      } else if(var1.mStyle instanceof NotificationCompat.DecoratedCustomViewStyle) {
         addDecoratedHeadsUpToBuilder(var0, var1);
      }

   }

   private static void addMessagingFallBackStyle(android.support.v4.app.NotificationCompat.MessagingStyle var0, NotificationBuilderWithBuilderAccessor var1, android.support.v4.app.NotificationCompat.Builder var2) {
      SpannableStringBuilder var7 = new SpannableStringBuilder();
      List var6 = var0.getMessages();
      boolean var3;
      if(var0.getConversationTitle() == null && !hasMessagesWithoutSender(var0.getMessages())) {
         var3 = false;
      } else {
         var3 = true;
      }

      for(int var4 = var6.size() - 1; var4 >= 0; --var4) {
         android.support.v4.app.NotificationCompat.Message var5 = (android.support.v4.app.NotificationCompat.Message)var6.get(var4);
         CharSequence var8;
         if(var3) {
            var8 = makeMessageLine(var2, var0, var5);
         } else {
            var8 = var5.getText();
         }

         if(var4 != var6.size() - 1) {
            var7.insert(0, "\n");
         }

         var7.insert(0, var8);
      }

      NotificationCompatImplJellybean.addBigTextStyle(var1, var7);
   }

   private static RemoteViews addStyleGetContentViewIcs(NotificationBuilderWithBuilderAccessor var0, android.support.v4.app.NotificationCompat.Builder var1) {
      RemoteViews var4;
      if(var1.mStyle instanceof NotificationCompat.MediaStyle) {
         NotificationCompat.MediaStyle var3 = (NotificationCompat.MediaStyle)var1.mStyle;
         boolean var2;
         if(var1.mStyle instanceof NotificationCompat.DecoratedMediaCustomViewStyle && var1.getContentView() != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         var4 = NotificationCompatImplBase.overrideContentViewMedia(var0, var1.mContext, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mNumber, var1.mLargeIcon, var1.mSubText, var1.mUseChronometer, var1.getWhenIfShowing(), var1.getPriority(), var1.mActions, var3.mActionsToShowInCompact, var3.mShowCancelButton, var3.mCancelButtonIntent, var2);
         if(var2) {
            NotificationCompatImplBase.buildIntoRemoteViews(var1.mContext, var4, var1.getContentView());
            return var4;
         }
      } else if(var1.mStyle instanceof NotificationCompat.DecoratedCustomViewStyle) {
         var4 = getDecoratedContentView(var1);
         return var4;
      }

      var4 = null;
      return var4;
   }

   private static RemoteViews addStyleGetContentViewJellybean(NotificationBuilderWithBuilderAccessor var0, android.support.v4.app.NotificationCompat.Builder var1) {
      if(var1.mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle) {
         addMessagingFallBackStyle((android.support.v4.app.NotificationCompat.MessagingStyle)var1.mStyle, var0, var1);
      }

      return addStyleGetContentViewIcs(var0, var1);
   }

   private static RemoteViews addStyleGetContentViewLollipop(NotificationBuilderWithBuilderAccessor var0, android.support.v4.app.NotificationCompat.Builder var1) {
      RemoteViews var7;
      if(var1.mStyle instanceof NotificationCompat.MediaStyle) {
         NotificationCompat.MediaStyle var6 = (NotificationCompat.MediaStyle)var1.mStyle;
         int[] var5 = var6.mActionsToShowInCompact;
         Object var4;
         if(var6.mToken != null) {
            var4 = var6.mToken.getToken();
         } else {
            var4 = null;
         }

         NotificationCompatImpl21.addMediaStyle(var0, var5, var4);
         boolean var3;
         if(var1.getContentView() != null) {
            var3 = true;
         } else {
            var3 = false;
         }

         boolean var2;
         if(VERSION.SDK_INT >= 21 && VERSION.SDK_INT <= 23) {
            var2 = true;
         } else {
            var2 = false;
         }

         if(var3 || var2 && var1.getBigContentView() != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if(var1.mStyle instanceof NotificationCompat.DecoratedMediaCustomViewStyle && var2) {
            var7 = NotificationCompatImplBase.overrideContentViewMedia(var0, var1.mContext, var1.mContentTitle, var1.mContentText, var1.mContentInfo, var1.mNumber, var1.mLargeIcon, var1.mSubText, var1.mUseChronometer, var1.getWhenIfShowing(), var1.getPriority(), var1.mActions, var6.mActionsToShowInCompact, false, (PendingIntent)null, var3);
            if(var3) {
               NotificationCompatImplBase.buildIntoRemoteViews(var1.mContext, var7, var1.getContentView());
            }

            setBackgroundColor(var1.mContext, var7, var1.getColor());
         } else {
            var7 = null;
         }
      } else if(var1.mStyle instanceof NotificationCompat.DecoratedCustomViewStyle) {
         var7 = getDecoratedContentView(var1);
      } else {
         var7 = addStyleGetContentViewJellybean(var0, var1);
      }

      return var7;
   }

   private static void addStyleToBuilderApi24(NotificationBuilderWithBuilderAccessor var0, android.support.v4.app.NotificationCompat.Builder var1) {
      if(var1.mStyle instanceof NotificationCompat.DecoratedCustomViewStyle) {
         NotificationCompatImpl24.addDecoratedCustomViewStyle(var0);
      } else if(var1.mStyle instanceof NotificationCompat.DecoratedMediaCustomViewStyle) {
         NotificationCompatImpl24.addDecoratedMediaCustomViewStyle(var0);
      } else if(!(var1.mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle)) {
         addStyleGetContentViewLollipop(var0, var1);
      }

   }

   private static android.support.v4.app.NotificationCompat.Message findLatestIncomingMessage(android.support.v4.app.NotificationCompat.MessagingStyle var0) {
      List var2 = var0.getMessages();
      int var1 = var2.size() - 1;

      android.support.v4.app.NotificationCompat.Message var3;
      while(true) {
         if(var1 < 0) {
            if(!var2.isEmpty()) {
               var3 = (android.support.v4.app.NotificationCompat.Message)var2.get(var2.size() - 1);
            } else {
               var3 = null;
            }
            break;
         }

         var3 = (android.support.v4.app.NotificationCompat.Message)var2.get(var1);
         if(!TextUtils.isEmpty(var3.getSender())) {
            break;
         }

         --var1;
      }

      return var3;
   }

   private static RemoteViews getDecoratedContentView(android.support.v4.app.NotificationCompat.Builder var0) {
      RemoteViews var2;
      if(var0.getContentView() == null) {
         var2 = null;
      } else {
         RemoteViews var1 = NotificationCompatImplBase.applyStandardTemplateWithActions(var0.mContext, var0.mContentTitle, var0.mContentText, var0.mContentInfo, var0.mNumber, var0.mNotification.icon, var0.mLargeIcon, var0.mSubText, var0.mUseChronometer, var0.getWhenIfShowing(), var0.getPriority(), var0.getColor(), R.layout.notification_template_custom_big, false, (ArrayList)null);
         NotificationCompatImplBase.buildIntoRemoteViews(var0.mContext, var1, var0.getContentView());
         var2 = var1;
      }

      return var2;
   }

   public static MediaSessionCompat.Token getMediaSession(Notification var0) {
      Bundle var2 = getExtras(var0);
      MediaSessionCompat.Token var4;
      if(var2 != null) {
         if(VERSION.SDK_INT >= 21) {
            Parcelable var3 = var2.getParcelable("android.mediaSession");
            if(var3 != null) {
               var4 = MediaSessionCompat.Token.fromToken(var3);
               return var4;
            }
         } else {
            IBinder var5 = BundleCompat.getBinder(var2, "android.mediaSession");
            if(var5 != null) {
               Parcel var1 = Parcel.obtain();
               var1.writeStrongBinder(var5);
               var1.setDataPosition(0);
               var4 = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(var1);
               var1.recycle();
               return var4;
            }
         }
      }

      var4 = null;
      return var4;
   }

   private static boolean hasMessagesWithoutSender(List var0) {
      int var1 = var0.size() - 1;

      boolean var2;
      while(true) {
         if(var1 < 0) {
            var2 = false;
            break;
         }

         if(((android.support.v4.app.NotificationCompat.Message)var0.get(var1)).getSender() == null) {
            var2 = true;
            break;
         }

         --var1;
      }

      return var2;
   }

   private static TextAppearanceSpan makeFontColorSpan(int var0) {
      return new TextAppearanceSpan((String)null, 0, 0, ColorStateList.valueOf(var0), (ColorStateList)null);
   }

   private static CharSequence makeMessageLine(android.support.v4.app.NotificationCompat.Builder var0, android.support.v4.app.NotificationCompat.MessagingStyle var1, android.support.v4.app.NotificationCompat.Message var2) {
      BidiFormatter var8 = BidiFormatter.getInstance();
      SpannableStringBuilder var7 = new SpannableStringBuilder();
      boolean var4;
      if(VERSION.SDK_INT >= 21) {
         var4 = true;
      } else {
         var4 = false;
      }

      int var3;
      if(!var4 && VERSION.SDK_INT > 10) {
         var3 = -1;
      } else {
         var3 = -16777216;
      }

      Object var6 = var2.getSender();
      int var5 = var3;
      if(TextUtils.isEmpty(var2.getSender())) {
         Object var11;
         if(var1.getUserDisplayName() == null) {
            var11 = "";
         } else {
            var11 = var1.getUserDisplayName();
         }

         var5 = var3;
         var6 = var11;
         if(var4) {
            var5 = var3;
            var6 = var11;
            if(var0.getColor() != 0) {
               var5 = var0.getColor();
               var6 = var11;
            }
         }
      }

      CharSequence var9 = var8.unicodeWrap((CharSequence)var6);
      var7.append(var9);
      var7.setSpan(makeFontColorSpan(var5), var7.length() - var9.length(), var7.length(), 33);
      Object var10;
      if(var2.getText() == null) {
         var10 = "";
      } else {
         var10 = var2.getText();
      }

      var7.append("  ").append(var8.unicodeWrap((CharSequence)var10));
      return var7;
   }

   private static void setBackgroundColor(Context var0, RemoteViews var1, int var2) {
      int var3 = var2;
      if(var2 == 0) {
         var3 = var0.getResources().getColor(R.color.notification_material_background_media_default_color);
      }

      var1.setInt(R.id.status_bar_latest_event_content, "setBackgroundColor", var3);
   }

   private static class Api24Extender extends android.support.v4.app.NotificationCompat.BuilderExtender {
      private Api24Extender() {
      }

      // $FF: synthetic method
      Api24Extender(Object var1) {
         this();
      }

      public Notification build(android.support.v4.app.NotificationCompat.Builder var1, NotificationBuilderWithBuilderAccessor var2) {
         NotificationCompat.addStyleToBuilderApi24(var2, var1);
         return var2.build();
      }
   }

   public static class Builder extends android.support.v4.app.NotificationCompat.Builder {
      public Builder(Context var1) {
         super(var1);
      }

      @RestrictTo({RestrictTo.Scope.GROUP_ID})
      protected android.support.v4.app.NotificationCompat.BuilderExtender getExtender() {
         Object var1;
         if(VERSION.SDK_INT >= 24) {
            var1 = new NotificationCompat.Api24Extender();
         } else if(VERSION.SDK_INT >= 21) {
            var1 = new NotificationCompat.LollipopExtender();
         } else if(VERSION.SDK_INT >= 16) {
            var1 = new NotificationCompat.JellybeanExtender();
         } else if(VERSION.SDK_INT >= 14) {
            var1 = new NotificationCompat.IceCreamSandwichExtender();
         } else {
            var1 = super.getExtender();
         }

         return (android.support.v4.app.NotificationCompat.BuilderExtender)var1;
      }

      @RestrictTo({RestrictTo.Scope.GROUP_ID})
      protected CharSequence resolveText() {
         CharSequence var4;
         if(this.mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle) {
            android.support.v4.app.NotificationCompat.MessagingStyle var2 = (android.support.v4.app.NotificationCompat.MessagingStyle)this.mStyle;
            android.support.v4.app.NotificationCompat.Message var1 = NotificationCompat.findLatestIncomingMessage(var2);
            CharSequence var3 = var2.getConversationTitle();
            if(var1 != null) {
               if(var3 != null) {
                  var4 = NotificationCompat.makeMessageLine(this, var2, var1);
               } else {
                  var4 = var1.getText();
               }

               return var4;
            }
         }

         var4 = super.resolveText();
         return var4;
      }

      @RestrictTo({RestrictTo.Scope.GROUP_ID})
      protected CharSequence resolveTitle() {
         CharSequence var3;
         if(this.mStyle instanceof android.support.v4.app.NotificationCompat.MessagingStyle) {
            android.support.v4.app.NotificationCompat.MessagingStyle var1 = (android.support.v4.app.NotificationCompat.MessagingStyle)this.mStyle;
            android.support.v4.app.NotificationCompat.Message var2 = NotificationCompat.findLatestIncomingMessage(var1);
            var3 = var1.getConversationTitle();
            if(var3 != null || var2 != null) {
               if(var3 == null) {
                  var3 = var2.getSender();
               }

               return var3;
            }
         }

         var3 = super.resolveTitle();
         return var3;
      }
   }

   public static class DecoratedCustomViewStyle extends android.support.v4.app.NotificationCompat.Style {
   }

   public static class DecoratedMediaCustomViewStyle extends NotificationCompat.MediaStyle {
   }

   private static class IceCreamSandwichExtender extends android.support.v4.app.NotificationCompat.BuilderExtender {
      public Notification build(android.support.v4.app.NotificationCompat.Builder var1, NotificationBuilderWithBuilderAccessor var2) {
         RemoteViews var3 = NotificationCompat.addStyleGetContentViewIcs(var2, var1);
         Notification var4 = var2.build();
         if(var3 != null) {
            var4.contentView = var3;
         } else if(var1.getContentView() != null) {
            var4.contentView = var1.getContentView();
         }

         return var4;
      }
   }

   private static class JellybeanExtender extends android.support.v4.app.NotificationCompat.BuilderExtender {
      public Notification build(android.support.v4.app.NotificationCompat.Builder var1, NotificationBuilderWithBuilderAccessor var2) {
         RemoteViews var3 = NotificationCompat.addStyleGetContentViewJellybean(var2, var1);
         Notification var4 = var2.build();
         if(var3 != null) {
            var4.contentView = var3;
         }

         NotificationCompat.addBigStyleToBuilderJellybean(var4, var1);
         return var4;
      }
   }

   private static class LollipopExtender extends android.support.v4.app.NotificationCompat.BuilderExtender {
      public Notification build(android.support.v4.app.NotificationCompat.Builder var1, NotificationBuilderWithBuilderAccessor var2) {
         RemoteViews var3 = NotificationCompat.addStyleGetContentViewLollipop(var2, var1);
         Notification var4 = var2.build();
         if(var3 != null) {
            var4.contentView = var3;
         }

         NotificationCompat.addBigStyleToBuilderLollipop(var4, var1);
         NotificationCompat.addHeadsUpToBuilderLollipop(var4, var1);
         return var4;
      }
   }

   public static class MediaStyle extends android.support.v4.app.NotificationCompat.Style {
      int[] mActionsToShowInCompact = null;
      PendingIntent mCancelButtonIntent;
      boolean mShowCancelButton;
      MediaSessionCompat.Token mToken;

      public MediaStyle() {
      }

      public MediaStyle(android.support.v4.app.NotificationCompat.Builder var1) {
         this.setBuilder(var1);
      }

      public NotificationCompat.MediaStyle setCancelButtonIntent(PendingIntent var1) {
         this.mCancelButtonIntent = var1;
         return this;
      }

      public NotificationCompat.MediaStyle setMediaSession(MediaSessionCompat.Token var1) {
         this.mToken = var1;
         return this;
      }

      public NotificationCompat.MediaStyle setShowActionsInCompactView(int... var1) {
         this.mActionsToShowInCompact = var1;
         return this;
      }

      public NotificationCompat.MediaStyle setShowCancelButton(boolean var1) {
         this.mShowCancelButton = var1;
         return this;
      }
   }
}
