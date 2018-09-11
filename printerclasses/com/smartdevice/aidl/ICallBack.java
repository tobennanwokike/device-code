package com.smartdevice.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICallBack extends IInterface {
   void onReturnValue(byte[] var1, int var2) throws RemoteException;

   public abstract static class Stub extends Binder implements ICallBack {
      private static final String DESCRIPTOR = "com.smartdevice.aidl.ICallBack";
      static final int TRANSACTION_onReturnValue = 1;

      public Stub() {
         this.attachInterface(this, "com.smartdevice.aidl.ICallBack");
      }

      public static ICallBack asInterface(IBinder var0) {
         Object var2;
         if(var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("com.smartdevice.aidl.ICallBack");
            if(var1 != null && var1 instanceof ICallBack) {
               var2 = (ICallBack)var1;
            } else {
               var2 = new ICallBack.Proxy(var0);
            }
         }

         return (ICallBack)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var5 = true;
         switch(var1) {
         case 1:
            var2.enforceInterface("com.smartdevice.aidl.ICallBack");
            this.onReturnValue(var2.createByteArray(), var2.readInt());
            var3.writeNoException();
            break;
         case 1598968902:
            var3.writeString("com.smartdevice.aidl.ICallBack");
            break;
         default:
            var5 = super.onTransact(var1, var2, var3, var4);
         }

         return var5;
      }
   }

   private static class Proxy implements ICallBack {
      private IBinder mRemote;

      Proxy(IBinder var1) {
         this.mRemote = var1;
      }

      public IBinder asBinder() {
         return this.mRemote;
      }

      public String getInterfaceDescriptor() {
         return "com.smartdevice.aidl.ICallBack";
      }

      public void onReturnValue(byte[] var1, int var2) throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var4.writeInterfaceToken("com.smartdevice.aidl.ICallBack");
            var4.writeByteArray(var1);
            var4.writeInt(var2);
            this.mRemote.transact(1, var4, var3, 0);
            var3.readException();
         } finally {
            var3.recycle();
            var4.recycle();
         }

      }
   }
}
