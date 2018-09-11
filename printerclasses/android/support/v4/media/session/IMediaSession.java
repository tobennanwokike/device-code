package android.support.v4.media.session;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public interface IMediaSession extends IInterface {
   void adjustVolume(int var1, int var2, String var3) throws RemoteException;

   void fastForward() throws RemoteException;

   Bundle getExtras() throws RemoteException;

   long getFlags() throws RemoteException;

   PendingIntent getLaunchPendingIntent() throws RemoteException;

   MediaMetadataCompat getMetadata() throws RemoteException;

   String getPackageName() throws RemoteException;

   PlaybackStateCompat getPlaybackState() throws RemoteException;

   List getQueue() throws RemoteException;

   CharSequence getQueueTitle() throws RemoteException;

   int getRatingType() throws RemoteException;

   String getTag() throws RemoteException;

   ParcelableVolumeInfo getVolumeAttributes() throws RemoteException;

   boolean isTransportControlEnabled() throws RemoteException;

   void next() throws RemoteException;

   void pause() throws RemoteException;

   void play() throws RemoteException;

   void playFromMediaId(String var1, Bundle var2) throws RemoteException;

   void playFromSearch(String var1, Bundle var2) throws RemoteException;

   void playFromUri(Uri var1, Bundle var2) throws RemoteException;

   void prepare() throws RemoteException;

   void prepareFromMediaId(String var1, Bundle var2) throws RemoteException;

   void prepareFromSearch(String var1, Bundle var2) throws RemoteException;

   void prepareFromUri(Uri var1, Bundle var2) throws RemoteException;

   void previous() throws RemoteException;

   void rate(RatingCompat var1) throws RemoteException;

   void registerCallbackListener(IMediaControllerCallback var1) throws RemoteException;

   void rewind() throws RemoteException;

   void seekTo(long var1) throws RemoteException;

   void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) throws RemoteException;

   void sendCustomAction(String var1, Bundle var2) throws RemoteException;

   boolean sendMediaButton(KeyEvent var1) throws RemoteException;

   void setVolumeTo(int var1, int var2, String var3) throws RemoteException;

   void skipToQueueItem(long var1) throws RemoteException;

   void stop() throws RemoteException;

   void unregisterCallbackListener(IMediaControllerCallback var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IMediaSession {
      private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaSession";
      static final int TRANSACTION_adjustVolume = 11;
      static final int TRANSACTION_fastForward = 22;
      static final int TRANSACTION_getExtras = 31;
      static final int TRANSACTION_getFlags = 9;
      static final int TRANSACTION_getLaunchPendingIntent = 8;
      static final int TRANSACTION_getMetadata = 27;
      static final int TRANSACTION_getPackageName = 6;
      static final int TRANSACTION_getPlaybackState = 28;
      static final int TRANSACTION_getQueue = 29;
      static final int TRANSACTION_getQueueTitle = 30;
      static final int TRANSACTION_getRatingType = 32;
      static final int TRANSACTION_getTag = 7;
      static final int TRANSACTION_getVolumeAttributes = 10;
      static final int TRANSACTION_isTransportControlEnabled = 5;
      static final int TRANSACTION_next = 20;
      static final int TRANSACTION_pause = 18;
      static final int TRANSACTION_play = 13;
      static final int TRANSACTION_playFromMediaId = 14;
      static final int TRANSACTION_playFromSearch = 15;
      static final int TRANSACTION_playFromUri = 16;
      static final int TRANSACTION_prepare = 33;
      static final int TRANSACTION_prepareFromMediaId = 34;
      static final int TRANSACTION_prepareFromSearch = 35;
      static final int TRANSACTION_prepareFromUri = 36;
      static final int TRANSACTION_previous = 21;
      static final int TRANSACTION_rate = 25;
      static final int TRANSACTION_registerCallbackListener = 3;
      static final int TRANSACTION_rewind = 23;
      static final int TRANSACTION_seekTo = 24;
      static final int TRANSACTION_sendCommand = 1;
      static final int TRANSACTION_sendCustomAction = 26;
      static final int TRANSACTION_sendMediaButton = 2;
      static final int TRANSACTION_setVolumeTo = 12;
      static final int TRANSACTION_skipToQueueItem = 17;
      static final int TRANSACTION_stop = 19;
      static final int TRANSACTION_unregisterCallbackListener = 4;

      public Stub() {
         this.attachInterface(this, "android.support.v4.media.session.IMediaSession");
      }

      public static IMediaSession asInterface(IBinder var0) {
         Object var2;
         if(var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.media.session.IMediaSession");
            if(var1 != null && var1 instanceof IMediaSession) {
               var2 = (IMediaSession)var1;
            } else {
               var2 = new IMediaSession.Proxy(var0);
            }
         }

         return (IMediaSession)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         byte var6 = 0;
         byte var5 = 0;
         boolean var7 = true;
         boolean var8;
         Uri var11;
         byte var13;
         Bundle var14;
         String var21;
         String var23;
         switch(var1) {
         case 1:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            String var12 = var2.readString();
            Bundle var26;
            if(var2.readInt() != 0) {
               var26 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var26 = null;
            }

            MediaSessionCompat.ResultReceiverWrapper var25;
            if(var2.readInt() != 0) {
               var25 = (MediaSessionCompat.ResultReceiverWrapper)MediaSessionCompat.ResultReceiverWrapper.CREATOR.createFromParcel(var2);
            } else {
               var25 = null;
            }

            this.sendCommand(var12, var26, var25);
            var3.writeNoException();
            break;
         case 2:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            KeyEvent var24;
            if(var2.readInt() != 0) {
               var24 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(var2);
            } else {
               var24 = null;
            }

            var8 = this.sendMediaButton(var24);
            var3.writeNoException();
            var13 = var5;
            if(var8) {
               var13 = 1;
            }

            var3.writeInt(var13);
            break;
         case 3:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.registerCallbackListener(IMediaControllerCallback.Stub.asInterface(var2.readStrongBinder()));
            var3.writeNoException();
            break;
         case 4:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.unregisterCallbackListener(IMediaControllerCallback.Stub.asInterface(var2.readStrongBinder()));
            var3.writeNoException();
            break;
         case 5:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var8 = this.isTransportControlEnabled();
            var3.writeNoException();
            var13 = var6;
            if(var8) {
               var13 = 1;
            }

            var3.writeInt(var13);
            break;
         case 6:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var23 = this.getPackageName();
            var3.writeNoException();
            var3.writeString(var23);
            break;
         case 7:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var23 = this.getTag();
            var3.writeNoException();
            var3.writeString(var23);
            break;
         case 8:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            PendingIntent var22 = this.getLaunchPendingIntent();
            var3.writeNoException();
            if(var22 != null) {
               var3.writeInt(1);
               var22.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 9:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            long var9 = this.getFlags();
            var3.writeNoException();
            var3.writeLong(var9);
            break;
         case 10:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            ParcelableVolumeInfo var20 = this.getVolumeAttributes();
            var3.writeNoException();
            if(var20 != null) {
               var3.writeInt(1);
               var20.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 11:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.adjustVolume(var2.readInt(), var2.readInt(), var2.readString());
            var3.writeNoException();
            break;
         case 12:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.setVolumeTo(var2.readInt(), var2.readInt(), var2.readString());
            var3.writeNoException();
            break;
         case 13:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.play();
            var3.writeNoException();
            break;
         case 14:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var21 = var2.readString();
            if(var2.readInt() != 0) {
               var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var14 = null;
            }

            this.playFromMediaId(var21, var14);
            var3.writeNoException();
            break;
         case 15:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var21 = var2.readString();
            if(var2.readInt() != 0) {
               var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var14 = null;
            }

            this.playFromSearch(var21, var14);
            var3.writeNoException();
            break;
         case 16:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if(var2.readInt() != 0) {
               var11 = (Uri)Uri.CREATOR.createFromParcel(var2);
            } else {
               var11 = null;
            }

            if(var2.readInt() != 0) {
               var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var14 = null;
            }

            this.playFromUri(var11, var14);
            var3.writeNoException();
            break;
         case 17:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.skipToQueueItem(var2.readLong());
            var3.writeNoException();
            break;
         case 18:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.pause();
            var3.writeNoException();
            break;
         case 19:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.stop();
            var3.writeNoException();
            break;
         case 20:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.next();
            var3.writeNoException();
            break;
         case 21:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.previous();
            var3.writeNoException();
            break;
         case 22:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.fastForward();
            var3.writeNoException();
            break;
         case 23:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.rewind();
            var3.writeNoException();
            break;
         case 24:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.seekTo(var2.readLong());
            var3.writeNoException();
            break;
         case 25:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            RatingCompat var19;
            if(var2.readInt() != 0) {
               var19 = (RatingCompat)RatingCompat.CREATOR.createFromParcel(var2);
            } else {
               var19 = null;
            }

            this.rate(var19);
            var3.writeNoException();
            break;
         case 26:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var21 = var2.readString();
            if(var2.readInt() != 0) {
               var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var14 = null;
            }

            this.sendCustomAction(var21, var14);
            var3.writeNoException();
            break;
         case 27:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            MediaMetadataCompat var18 = this.getMetadata();
            var3.writeNoException();
            if(var18 != null) {
               var3.writeInt(1);
               var18.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 28:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            PlaybackStateCompat var17 = this.getPlaybackState();
            var3.writeNoException();
            if(var17 != null) {
               var3.writeInt(1);
               var17.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 29:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            List var16 = this.getQueue();
            var3.writeNoException();
            var3.writeTypedList(var16);
            break;
         case 30:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            CharSequence var15 = this.getQueueTitle();
            var3.writeNoException();
            if(var15 != null) {
               var3.writeInt(1);
               TextUtils.writeToParcel(var15, var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 31:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var14 = this.getExtras();
            var3.writeNoException();
            if(var14 != null) {
               var3.writeInt(1);
               var14.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }
            break;
         case 32:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var1 = this.getRatingType();
            var3.writeNoException();
            var3.writeInt(var1);
            break;
         case 33:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.prepare();
            var3.writeNoException();
            break;
         case 34:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var21 = var2.readString();
            if(var2.readInt() != 0) {
               var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var14 = null;
            }

            this.prepareFromMediaId(var21, var14);
            var3.writeNoException();
            break;
         case 35:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var21 = var2.readString();
            if(var2.readInt() != 0) {
               var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var14 = null;
            }

            this.prepareFromSearch(var21, var14);
            var3.writeNoException();
            break;
         case 36:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if(var2.readInt() != 0) {
               var11 = (Uri)Uri.CREATOR.createFromParcel(var2);
            } else {
               var11 = null;
            }

            if(var2.readInt() != 0) {
               var14 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var14 = null;
            }

            this.prepareFromUri(var11, var14);
            var3.writeNoException();
            break;
         case 1598968902:
            var3.writeString("android.support.v4.media.session.IMediaSession");
            break;
         default:
            var7 = super.onTransact(var1, var2, var3, var4);
         }

         return var7;
      }
   }

   private static class Proxy implements IMediaSession {
      private IBinder mRemote;

      Proxy(IBinder var1) {
         this.mRemote = var1;
      }

      public void adjustVolume(int var1, int var2, String var3) throws RemoteException {
         Parcel var5 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var5.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            var5.writeInt(var1);
            var5.writeInt(var2);
            var5.writeString(var3);
            this.mRemote.transact(11, var5, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var5.recycle();
         }

      }

      public IBinder asBinder() {
         return this.mRemote;
      }

      public void fastForward() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(22, var2, var3, 0);
            var3.readException();
         } finally {
            var3.recycle();
            var2.recycle();
         }

      }

      public Bundle getExtras() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();
         boolean var5 = false;

         Bundle var1;
         label36: {
            try {
               var5 = true;
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(31, var2, var3, 0);
               var3.readException();
               if(var3.readInt() != 0) {
                  var1 = (Bundle)Bundle.CREATOR.createFromParcel(var3);
                  var5 = false;
                  break label36;
               }

               var5 = false;
            } finally {
               if(var5) {
                  var3.recycle();
                  var2.recycle();
               }
            }

            var1 = null;
         }

         var3.recycle();
         var2.recycle();
         return var1;
      }

      public long getFlags() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();

         long var1;
         try {
            var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(9, var3, var5, 0);
            var5.readException();
            var1 = var5.readLong();
         } finally {
            var5.recycle();
            var3.recycle();
         }

         return var1;
      }

      public String getInterfaceDescriptor() {
         return "android.support.v4.media.session.IMediaSession";
      }

      public PendingIntent getLaunchPendingIntent() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();
         boolean var5 = false;

         PendingIntent var1;
         label36: {
            try {
               var5 = true;
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(8, var3, var2, 0);
               var2.readException();
               if(var2.readInt() != 0) {
                  var1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(var2);
                  var5 = false;
                  break label36;
               }

               var5 = false;
            } finally {
               if(var5) {
                  var2.recycle();
                  var3.recycle();
               }
            }

            var1 = null;
         }

         var2.recycle();
         var3.recycle();
         return var1;
      }

      public MediaMetadataCompat getMetadata() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();
         boolean var5 = false;

         MediaMetadataCompat var1;
         label36: {
            try {
               var5 = true;
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(27, var3, var2, 0);
               var2.readException();
               if(var2.readInt() != 0) {
                  var1 = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(var2);
                  var5 = false;
                  break label36;
               }

               var5 = false;
            } finally {
               if(var5) {
                  var2.recycle();
                  var3.recycle();
               }
            }

            var1 = null;
         }

         var2.recycle();
         var3.recycle();
         return var1;
      }

      public String getPackageName() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         String var3;
         try {
            var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(6, var1, var2, 0);
            var2.readException();
            var3 = var2.readString();
         } finally {
            var2.recycle();
            var1.recycle();
         }

         return var3;
      }

      public PlaybackStateCompat getPlaybackState() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();
         boolean var5 = false;

         PlaybackStateCompat var1;
         label36: {
            try {
               var5 = true;
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(28, var2, var3, 0);
               var3.readException();
               if(var3.readInt() != 0) {
                  var1 = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(var3);
                  var5 = false;
                  break label36;
               }

               var5 = false;
            } finally {
               if(var5) {
                  var3.recycle();
                  var2.recycle();
               }
            }

            var1 = null;
         }

         var3.recycle();
         var2.recycle();
         return var1;
      }

      public List getQueue() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         ArrayList var3;
         try {
            var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(29, var1, var2, 0);
            var2.readException();
            var3 = var2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR);
         } finally {
            var2.recycle();
            var1.recycle();
         }

         return var3;
      }

      public CharSequence getQueueTitle() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();
         boolean var5 = false;

         CharSequence var1;
         label36: {
            try {
               var5 = true;
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(30, var2, var3, 0);
               var3.readException();
               if(var3.readInt() != 0) {
                  var1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var3);
                  var5 = false;
                  break label36;
               }

               var5 = false;
            } finally {
               if(var5) {
                  var3.recycle();
                  var2.recycle();
               }
            }

            var1 = null;
         }

         var3.recycle();
         var2.recycle();
         return var1;
      }

      public int getRatingType() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         int var1;
         try {
            var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(32, var2, var3, 0);
            var3.readException();
            var1 = var3.readInt();
         } finally {
            var3.recycle();
            var2.recycle();
         }

         return var1;
      }

      public String getTag() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         String var3;
         try {
            var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(7, var1, var2, 0);
            var2.readException();
            var3 = var2.readString();
         } finally {
            var2.recycle();
            var1.recycle();
         }

         return var3;
      }

      public ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();
         boolean var5 = false;

         ParcelableVolumeInfo var1;
         label36: {
            try {
               var5 = true;
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(10, var2, var3, 0);
               var3.readException();
               if(var3.readInt() != 0) {
                  var1 = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(var3);
                  var5 = false;
                  break label36;
               }

               var5 = false;
            } finally {
               if(var5) {
                  var3.recycle();
                  var2.recycle();
               }
            }

            var1 = null;
         }

         var3.recycle();
         var2.recycle();
         return var1;
      }

      public boolean isTransportControlEnabled() throws RemoteException {
         boolean var2 = false;
         Parcel var3 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         int var1;
         try {
            var7 = true;
            var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(5, var3, var5, 0);
            var5.readException();
            var1 = var5.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var5.recycle();
               var3.recycle();
            }
         }

         if(var1 != 0) {
            var2 = true;
         }

         var5.recycle();
         var3.recycle();
         return var2;
      }

      public void next() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(20, var3, var2, 0);
            var2.readException();
         } finally {
            var2.recycle();
            var3.recycle();
         }

      }

      public void pause() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var1 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(18, var3, var1, 0);
            var1.readException();
         } finally {
            var1.recycle();
            var3.recycle();
         }

      }

      public void play() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var1 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(13, var3, var1, 0);
            var1.readException();
         } finally {
            var1.recycle();
            var3.recycle();
         }

      }

      public void playFromMediaId(String param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void playFromSearch(String param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void playFromUri(Uri param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void prepare() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         try {
            var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(33, var1, var2, 0);
            var2.readException();
         } finally {
            var2.recycle();
            var1.recycle();
         }

      }

      public void prepareFromMediaId(String param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void prepareFromSearch(String param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void prepareFromUri(Uri param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void previous() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(21, var1, var3, 0);
            var3.readException();
         } finally {
            var3.recycle();
            var1.recycle();
         }

      }

      public void rate(RatingCompat param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void registerCallbackListener(IMediaControllerCallback param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void rewind() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(23, var1, var3, 0);
            var3.readException();
         } finally {
            var3.recycle();
            var1.recycle();
         }

      }

      public void seekTo(long var1) throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            var3.writeLong(var1);
            this.mRemote.transact(24, var3, var5, 0);
            var5.readException();
         } finally {
            var5.recycle();
            var3.recycle();
         }

      }

      public void sendCommand(String param1, Bundle param2, MediaSessionCompat.ResultReceiverWrapper param3) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void sendCustomAction(String param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean sendMediaButton(KeyEvent param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void setVolumeTo(int var1, int var2, String var3) throws RemoteException {
         Parcel var5 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var5.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            var5.writeInt(var1);
            var5.writeInt(var2);
            var5.writeString(var3);
            this.mRemote.transact(12, var5, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var5.recycle();
         }

      }

      public void skipToQueueItem(long var1) throws RemoteException {
         Parcel var5 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var5.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            var5.writeLong(var1);
            this.mRemote.transact(17, var5, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var5.recycle();
         }

      }

      public void stop() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
            this.mRemote.transact(19, var2, var3, 0);
            var3.readException();
         } finally {
            var3.recycle();
            var2.recycle();
         }

      }

      public void unregisterCallbackListener(IMediaControllerCallback param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }
   }
}
