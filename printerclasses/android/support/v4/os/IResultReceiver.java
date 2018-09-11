package android.support.v4.os;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IResultReceiver extends IInterface {
   void send(int var1, Bundle var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IResultReceiver {
      private static final String DESCRIPTOR = "android.support.v4.os.IResultReceiver";
      static final int TRANSACTION_send = 1;

      public Stub() {
         this.attachInterface(this, "android.support.v4.os.IResultReceiver");
      }

      public static IResultReceiver asInterface(IBinder var0) {
         Object var2;
         if(var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.os.IResultReceiver");
            if(var1 != null && var1 instanceof IResultReceiver) {
               var2 = (IResultReceiver)var1;
            } else {
               var2 = new IResultReceiver.Proxy(var0);
            }
         }

         return (IResultReceiver)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var5 = true;
         switch(var1) {
         case 1:
            var2.enforceInterface("android.support.v4.os.IResultReceiver");
            var1 = var2.readInt();
            Bundle var6;
            if(var2.readInt() != 0) {
               var6 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var6 = null;
            }

            this.send(var1, var6);
            break;
         case 1598968902:
            var3.writeString("android.support.v4.os.IResultReceiver");
            break;
         default:
            var5 = super.onTransact(var1, var2, var3, var4);
         }

         return var5;
      }
   }

   private static class Proxy implements IResultReceiver {
      private IBinder mRemote;

      Proxy(IBinder var1) {
         this.mRemote = var1;
      }

      public IBinder asBinder() {
         return this.mRemote;
      }

      public String getInterfaceDescriptor() {
         return "android.support.v4.os.IResultReceiver";
      }

      public void send(int param1, Bundle param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }
   }
}
