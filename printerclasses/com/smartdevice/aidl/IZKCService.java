package com.smartdevice.aidl;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.smartdevice.aidl.ICallBack;

public interface IZKCService extends IInterface {
   byte[] CardApdu(byte[] var1) throws RemoteException;

   int CardApdu2(long var1, byte[] var3, int var4, byte[] var5, int[] var6) throws RemoteException;

   byte[] CardApdu3(long var1, byte[] var3, int var4) throws RemoteException;

   int Close(long var1) throws RemoteException;

   int CloseCard() throws RemoteException;

   int CloseCard2(long var1, boolean var3) throws RemoteException;

   int Open() throws RemoteException;

   byte[] ResetCard(int var1) throws RemoteException;

   int ResetCard2(long var1, byte[] var3, int[] var4) throws RemoteException;

   byte[] ResetCard3(long var1, int var3, int var4) throws RemoteException;

   void appendRingTone(boolean var1) throws RemoteException;

   boolean checkPrinterAvailable() throws RemoteException;

   void continueScan(boolean var1) throws RemoteException;

   Bitmap createBarCode(String var1, int var2, int var3, int var4, boolean var5) throws RemoteException;

   Bitmap createQRCode(String var1, int var2, int var3) throws RemoteException;

   void dataAppendEnter(boolean var1) throws RemoteException;

   void generateSpace() throws RemoteException;

   int getDeviceModel() throws RemoteException;

   void getFirmwareVersion() throws RemoteException;

   String getFirmwareVersion1() throws RemoteException;

   String getFirmwareVersion2() throws RemoteException;

   Bitmap getHeader() throws RemoteException;

   String getIdentifyInfo() throws RemoteException;

   String getPrinterStatus() throws RemoteException;

   String getServiceVersion() throws RemoteException;

   boolean isScanning() throws RemoteException;

   boolean isTaskRunning() throws RemoteException;

   void openBackLight(int var1) throws RemoteException;

   int openCard(int var1) throws RemoteException;

   int openCard2(int[] var1, int var2) throws RemoteException;

   int openCard3(long var1, int var3) throws RemoteException;

   void openScan(boolean var1) throws RemoteException;

   boolean printBarCode(String var1) throws RemoteException;

   void printBitmap(Bitmap var1) throws RemoteException;

   void printBitmapAlgin(Bitmap var1, int var2, int var3, int var4) throws RemoteException;

   void printColumnsText(String[] var1, int[] var2, int[] var3) throws RemoteException;

   void printGBKText(String var1) throws RemoteException;

   boolean printImageGray(Bitmap var1) throws RemoteException;

   boolean printQrCode(String var1) throws RemoteException;

   boolean printRasterImage(Bitmap var1) throws RemoteException;

   void printTextAlgin(String var1, int var2, int var3, int var4) throws RemoteException;

   void printTextWithFont(String var1, int var2, int var3) throws RemoteException;

   void printUnicodeText(String var1) throws RemoteException;

   boolean printUnicode_1F30(String var1) throws RemoteException;

   void printerInit() throws RemoteException;

   void printerSelfChecking() throws RemoteException;

   void recoveryFactorySet(boolean var1) throws RemoteException;

   void registerCallBack(String var1, ICallBack var2) throws RemoteException;

   void scan() throws RemoteException;

   void scanRepeatHint(boolean var1) throws RemoteException;

   byte[] sendCommand(byte[] var1) throws RemoteException;

   void sendRAWData(String var1, byte[] var2) throws RemoteException;

   void setAlignment(int var1) throws RemoteException;

   void setFontSize(int var1) throws RemoteException;

   boolean setGPIO(int var1, int var2) throws RemoteException;

   boolean setModuleFlag(int var1) throws RemoteException;

   boolean setPrinterLanguage(String var1, int var2) throws RemoteException;

   void setTypeface(int var1) throws RemoteException;

   boolean showDotImage(int var1, int var2, Bitmap var3) throws RemoteException;

   boolean showRGB565Image(Bitmap var1) throws RemoteException;

   boolean showRGB565ImageByPath(String var1) throws RemoteException;

   boolean showRGB565ImageCenter(Bitmap var1) throws RemoteException;

   boolean showRGB565ImageLocation(Bitmap var1, int var2, int var3, int var4, int var5) throws RemoteException;

   void stopRunningTask() throws RemoteException;

   boolean turnOff() throws RemoteException;

   boolean turnOn() throws RemoteException;

   void unregisterCallBack(String var1, ICallBack var2) throws RemoteException;

   boolean updateLogo(Bitmap var1) throws RemoteException;

   boolean updateLogoByPath(String var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IZKCService {
      private static final String DESCRIPTOR = "com.smartdevice.aidl.IZKCService";
      static final int TRANSACTION_CardApdu = 54;
      static final int TRANSACTION_CardApdu2 = 55;
      static final int TRANSACTION_CardApdu3 = 56;
      static final int TRANSACTION_Close = 44;
      static final int TRANSACTION_CloseCard = 49;
      static final int TRANSACTION_CloseCard2 = 50;
      static final int TRANSACTION_Open = 43;
      static final int TRANSACTION_ResetCard = 51;
      static final int TRANSACTION_ResetCard2 = 52;
      static final int TRANSACTION_ResetCard3 = 53;
      static final int TRANSACTION_appendRingTone = 60;
      static final int TRANSACTION_checkPrinterAvailable = 12;
      static final int TRANSACTION_continueScan = 61;
      static final int TRANSACTION_createBarCode = 25;
      static final int TRANSACTION_createQRCode = 26;
      static final int TRANSACTION_dataAppendEnter = 59;
      static final int TRANSACTION_generateSpace = 27;
      static final int TRANSACTION_getDeviceModel = 8;
      static final int TRANSACTION_getFirmwareVersion = 6;
      static final int TRANSACTION_getFirmwareVersion1 = 31;
      static final int TRANSACTION_getFirmwareVersion2 = 32;
      static final int TRANSACTION_getHeader = 69;
      static final int TRANSACTION_getIdentifyInfo = 66;
      static final int TRANSACTION_getPrinterStatus = 10;
      static final int TRANSACTION_getServiceVersion = 7;
      static final int TRANSACTION_isScanning = 65;
      static final int TRANSACTION_isTaskRunning = 5;
      static final int TRANSACTION_openBackLight = 35;
      static final int TRANSACTION_openCard = 46;
      static final int TRANSACTION_openCard2 = 47;
      static final int TRANSACTION_openCard3 = 48;
      static final int TRANSACTION_openScan = 57;
      static final int TRANSACTION_printBarCode = 33;
      static final int TRANSACTION_printBitmap = 23;
      static final int TRANSACTION_printBitmapAlgin = 24;
      static final int TRANSACTION_printColumnsText = 22;
      static final int TRANSACTION_printGBKText = 18;
      static final int TRANSACTION_printImageGray = 28;
      static final int TRANSACTION_printQrCode = 34;
      static final int TRANSACTION_printRasterImage = 29;
      static final int TRANSACTION_printTextAlgin = 21;
      static final int TRANSACTION_printTextWithFont = 20;
      static final int TRANSACTION_printUnicodeText = 19;
      static final int TRANSACTION_printUnicode_1F30 = 30;
      static final int TRANSACTION_printerInit = 9;
      static final int TRANSACTION_printerSelfChecking = 11;
      static final int TRANSACTION_recoveryFactorySet = 63;
      static final int TRANSACTION_registerCallBack = 2;
      static final int TRANSACTION_scan = 58;
      static final int TRANSACTION_scanRepeatHint = 62;
      static final int TRANSACTION_sendCommand = 64;
      static final int TRANSACTION_sendRAWData = 13;
      static final int TRANSACTION_setAlignment = 15;
      static final int TRANSACTION_setFontSize = 17;
      static final int TRANSACTION_setGPIO = 45;
      static final int TRANSACTION_setModuleFlag = 1;
      static final int TRANSACTION_setPrinterLanguage = 14;
      static final int TRANSACTION_setTypeface = 16;
      static final int TRANSACTION_showDotImage = 41;
      static final int TRANSACTION_showRGB565Image = 36;
      static final int TRANSACTION_showRGB565ImageByPath = 37;
      static final int TRANSACTION_showRGB565ImageCenter = 42;
      static final int TRANSACTION_showRGB565ImageLocation = 38;
      static final int TRANSACTION_stopRunningTask = 4;
      static final int TRANSACTION_turnOff = 68;
      static final int TRANSACTION_turnOn = 67;
      static final int TRANSACTION_unregisterCallBack = 3;
      static final int TRANSACTION_updateLogo = 39;
      static final int TRANSACTION_updateLogoByPath = 40;

      public Stub() {
         this.attachInterface(this, "com.smartdevice.aidl.IZKCService");
      }

      public static IZKCService asInterface(IBinder var0) {
         Object var2;
         if(var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("com.smartdevice.aidl.IZKCService");
            if(var1 != null && var1 instanceof IZKCService) {
               var2 = (IZKCService)var1;
            } else {
               var2 = new IZKCService.Proxy(var0);
            }
         }

         return (IZKCService)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         boolean var6;
         long var7;
         byte[] var9;
         byte var11;
         Bitmap var12;
         String var13;
         byte[] var14;
         int[] var15;
         Bitmap var17;
         switch(var1) {
         case 1:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.setModuleFlag(var2.readInt());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 2:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.registerCallBack(var2.readString(), ICallBack.Stub.asInterface(var2.readStrongBinder()));
            var3.writeNoException();
            var6 = true;
            break;
         case 3:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.unregisterCallBack(var2.readString(), ICallBack.Stub.asInterface(var2.readStrongBinder()));
            var3.writeNoException();
            var6 = true;
            break;
         case 4:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.stopRunningTask();
            var3.writeNoException();
            var6 = true;
            break;
         case 5:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.isTaskRunning();
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 6:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.getFirmwareVersion();
            var3.writeNoException();
            var6 = true;
            break;
         case 7:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var13 = this.getServiceVersion();
            var3.writeNoException();
            var3.writeString(var13);
            var6 = true;
            break;
         case 8:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var1 = this.getDeviceModel();
            var3.writeNoException();
            var3.writeInt(var1);
            var6 = true;
            break;
         case 9:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.printerInit();
            var3.writeNoException();
            var6 = true;
            break;
         case 10:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var13 = this.getPrinterStatus();
            var3.writeNoException();
            var3.writeString(var13);
            var6 = true;
            break;
         case 11:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.printerSelfChecking();
            var3.writeNoException();
            var6 = true;
            break;
         case 12:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.checkPrinterAvailable();
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 13:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.sendRAWData(var2.readString(), var2.createByteArray());
            var3.writeNoException();
            var6 = true;
            break;
         case 14:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.setPrinterLanguage(var2.readString(), var2.readInt());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 15:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.setAlignment(var2.readInt());
            var3.writeNoException();
            var6 = true;
            break;
         case 16:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.setTypeface(var2.readInt());
            var3.writeNoException();
            var6 = true;
            break;
         case 17:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.setFontSize(var2.readInt());
            var3.writeNoException();
            var6 = true;
            break;
         case 18:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.printGBKText(var2.readString());
            var3.writeNoException();
            var6 = true;
            break;
         case 19:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.printUnicodeText(var2.readString());
            var3.writeNoException();
            var6 = true;
            break;
         case 20:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.printTextWithFont(var2.readString(), var2.readInt(), var2.readInt());
            var3.writeNoException();
            var6 = true;
            break;
         case 21:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.printTextAlgin(var2.readString(), var2.readInt(), var2.readInt(), var2.readInt());
            var3.writeNoException();
            var6 = true;
            break;
         case 22:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.printColumnsText(var2.createStringArray(), var2.createIntArray(), var2.createIntArray());
            var3.writeNoException();
            var6 = true;
            break;
         case 23:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var12 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            this.printBitmap(var12);
            var3.writeNoException();
            var6 = true;
            break;
         case 24:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var17 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var17 = null;
            }

            this.printBitmapAlgin(var17, var2.readInt(), var2.readInt(), var2.readInt());
            var3.writeNoException();
            var6 = true;
            break;
         case 25:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            String var18 = var2.readString();
            var4 = var2.readInt();
            int var5 = var2.readInt();
            var1 = var2.readInt();
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            var12 = this.createBarCode(var18, var4, var5, var1, var6);
            var3.writeNoException();
            if(var12 != null) {
               var3.writeInt(1);
               var12.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }

            var6 = true;
            break;
         case 26:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var12 = this.createQRCode(var2.readString(), var2.readInt(), var2.readInt());
            var3.writeNoException();
            if(var12 != null) {
               var3.writeInt(1);
               var12.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }

            var6 = true;
            break;
         case 27:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.generateSpace();
            var3.writeNoException();
            var6 = true;
            break;
         case 28:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var12 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            var6 = this.printImageGray(var12);
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 29:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var12 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            var6 = this.printRasterImage(var12);
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 30:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.printUnicode_1F30(var2.readString());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 31:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var13 = this.getFirmwareVersion1();
            var3.writeNoException();
            var3.writeString(var13);
            var6 = true;
            break;
         case 32:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var13 = this.getFirmwareVersion2();
            var3.writeNoException();
            var3.writeString(var13);
            var6 = true;
            break;
         case 33:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.printBarCode(var2.readString());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 34:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.printQrCode(var2.readString());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 35:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.openBackLight(var2.readInt());
            var3.writeNoException();
            var6 = true;
            break;
         case 36:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var12 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            var6 = this.showRGB565Image(var12);
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 37:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.showRGB565ImageByPath(var2.readString());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 38:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var17 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var17 = null;
            }

            var6 = this.showRGB565ImageLocation(var17, var2.readInt(), var2.readInt(), var2.readInt(), var2.readInt());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 39:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var12 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            var6 = this.updateLogo(var12);
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 40:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.updateLogoByPath(var2.readString());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 41:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var4 = var2.readInt();
            var1 = var2.readInt();
            if(var2.readInt() != 0) {
               var12 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            var6 = this.showDotImage(var4, var1, var12);
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 42:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var12 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
            } else {
               var12 = null;
            }

            var6 = this.showRGB565ImageCenter(var12);
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 43:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var1 = this.Open();
            var3.writeNoException();
            var3.writeInt(var1);
            var6 = true;
            break;
         case 44:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var1 = this.Close(var2.readLong());
            var3.writeNoException();
            var3.writeInt(var1);
            var6 = true;
            break;
         case 45:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.setGPIO(var2.readInt(), var2.readInt());
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 46:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var1 = this.openCard(var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            var6 = true;
            break;
         case 47:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            int[] var16 = var2.createIntArray();
            var1 = this.openCard2(var16, var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            var3.writeIntArray(var16);
            var6 = true;
            break;
         case 48:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var1 = this.openCard3(var2.readLong(), var2.readInt());
            var3.writeNoException();
            var3.writeInt(var1);
            var6 = true;
            break;
         case 49:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var1 = this.CloseCard();
            var3.writeNoException();
            var3.writeInt(var1);
            var6 = true;
            break;
         case 50:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var7 = var2.readLong();
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            var1 = this.CloseCard2(var7, var6);
            var3.writeNoException();
            var3.writeInt(var1);
            var6 = true;
            break;
         case 51:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var14 = this.ResetCard(var2.readInt());
            var3.writeNoException();
            var3.writeByteArray(var14);
            var6 = true;
            break;
         case 52:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var7 = var2.readLong();
            var9 = var2.createByteArray();
            var15 = var2.createIntArray();
            var1 = this.ResetCard2(var7, var9, var15);
            var3.writeNoException();
            var3.writeInt(var1);
            var3.writeByteArray(var9);
            var3.writeIntArray(var15);
            var6 = true;
            break;
         case 53:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var14 = this.ResetCard3(var2.readLong(), var2.readInt(), var2.readInt());
            var3.writeNoException();
            var3.writeByteArray(var14);
            var6 = true;
            break;
         case 54:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var14 = this.CardApdu(var2.createByteArray());
            var3.writeNoException();
            var3.writeByteArray(var14);
            var6 = true;
            break;
         case 55:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var7 = var2.readLong();
            byte[] var10 = var2.createByteArray();
            var1 = var2.readInt();
            var9 = var2.createByteArray();
            var15 = var2.createIntArray();
            var1 = this.CardApdu2(var7, var10, var1, var9, var15);
            var3.writeNoException();
            var3.writeInt(var1);
            var3.writeByteArray(var9);
            var3.writeIntArray(var15);
            var6 = true;
            break;
         case 56:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var14 = this.CardApdu3(var2.readLong(), var2.createByteArray(), var2.readInt());
            var3.writeNoException();
            var3.writeByteArray(var14);
            var6 = true;
            break;
         case 57:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.openScan(var6);
            var3.writeNoException();
            var6 = true;
            break;
         case 58:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            this.scan();
            var3.writeNoException();
            var6 = true;
            break;
         case 59:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.dataAppendEnter(var6);
            var3.writeNoException();
            var6 = true;
            break;
         case 60:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.appendRingTone(var6);
            var3.writeNoException();
            var6 = true;
            break;
         case 61:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.continueScan(var6);
            var3.writeNoException();
            var6 = true;
            break;
         case 62:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.scanRepeatHint(var6);
            var3.writeNoException();
            var6 = true;
            break;
         case 63:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            if(var2.readInt() != 0) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.recoveryFactorySet(var6);
            var3.writeNoException();
            var6 = true;
            break;
         case 64:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var14 = this.sendCommand(var2.createByteArray());
            var3.writeNoException();
            var3.writeByteArray(var14);
            var6 = true;
            break;
         case 65:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.isScanning();
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 66:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var13 = this.getIdentifyInfo();
            var3.writeNoException();
            var3.writeString(var13);
            var6 = true;
            break;
         case 67:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.turnOn();
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 68:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var6 = this.turnOff();
            var3.writeNoException();
            if(var6) {
               var11 = 1;
            } else {
               var11 = 0;
            }

            var3.writeInt(var11);
            var6 = true;
            break;
         case 69:
            var2.enforceInterface("com.smartdevice.aidl.IZKCService");
            var12 = this.getHeader();
            var3.writeNoException();
            if(var12 != null) {
               var3.writeInt(1);
               var12.writeToParcel(var3, 1);
            } else {
               var3.writeInt(0);
            }

            var6 = true;
            break;
         case 1598968902:
            var3.writeString("com.smartdevice.aidl.IZKCService");
            var6 = true;
            break;
         default:
            var6 = super.onTransact(var1, var2, var3, var4);
         }

         return var6;
      }
   }

   private static class Proxy implements IZKCService {
      private IBinder mRemote;

      Proxy(IBinder var1) {
         this.mRemote = var1;
      }

      public byte[] CardApdu(byte[] var1) throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var2.writeByteArray(var1);
            this.mRemote.transact(54, var2, var3, 0);
            var3.readException();
            var1 = var3.createByteArray();
         } finally {
            var3.recycle();
            var2.recycle();
         }

         return var1;
      }

      public int CardApdu2(long var1, byte[] var3, int var4, byte[] var5, int[] var6) throws RemoteException {
         Parcel var7 = Parcel.obtain();
         Parcel var8 = Parcel.obtain();

         try {
            var7.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var7.writeLong(var1);
            var7.writeByteArray(var3);
            var7.writeInt(var4);
            var7.writeByteArray(var5);
            var7.writeIntArray(var6);
            this.mRemote.transact(55, var7, var8, 0);
            var8.readException();
            var4 = var8.readInt();
            var8.readByteArray(var5);
            var8.readIntArray(var6);
         } finally {
            var8.recycle();
            var7.recycle();
         }

         return var4;
      }

      public byte[] CardApdu3(long var1, byte[] var3, int var4) throws RemoteException {
         Parcel var6 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();

         try {
            var6.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var6.writeLong(var1);
            var6.writeByteArray(var3);
            var6.writeInt(var4);
            this.mRemote.transact(56, var6, var5, 0);
            var5.readException();
            var3 = var5.createByteArray();
         } finally {
            var5.recycle();
            var6.recycle();
         }

         return var3;
      }

      public int Close(long var1) throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var6 = Parcel.obtain();

         int var3;
         try {
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeLong(var1);
            this.mRemote.transact(44, var4, var6, 0);
            var6.readException();
            var3 = var6.readInt();
         } finally {
            var6.recycle();
            var4.recycle();
         }

         return var3;
      }

      public int CloseCard() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         int var1;
         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(49, var2, var3, 0);
            var3.readException();
            var1 = var3.readInt();
         } finally {
            var3.recycle();
            var2.recycle();
         }

         return var1;
      }

      public int CloseCard2(long param1, boolean param3) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public int Open() throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         int var1;
         try {
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(43, var4, var2, 0);
            var2.readException();
            var1 = var2.readInt();
         } finally {
            var2.recycle();
            var4.recycle();
         }

         return var1;
      }

      public byte[] ResetCard(int var1) throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         byte[] var4;
         try {
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var3.writeInt(var1);
            this.mRemote.transact(51, var3, var2, 0);
            var2.readException();
            var4 = var2.createByteArray();
         } finally {
            var2.recycle();
            var3.recycle();
         }

         return var4;
      }

      public int ResetCard2(long var1, byte[] var3, int[] var4) throws RemoteException {
         Parcel var6 = Parcel.obtain();
         Parcel var7 = Parcel.obtain();

         int var5;
         try {
            var6.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var6.writeLong(var1);
            var6.writeByteArray(var3);
            var6.writeIntArray(var4);
            this.mRemote.transact(52, var6, var7, 0);
            var7.readException();
            var5 = var7.readInt();
            var7.readByteArray(var3);
            var7.readIntArray(var4);
         } finally {
            var7.recycle();
            var6.recycle();
         }

         return var5;
      }

      public byte[] ResetCard3(long var1, int var3, int var4) throws RemoteException {
         Parcel var5 = Parcel.obtain();
         Parcel var6 = Parcel.obtain();

         byte[] var7;
         try {
            var5.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var5.writeLong(var1);
            var5.writeInt(var3);
            var5.writeInt(var4);
            this.mRemote.transact(53, var5, var6, 0);
            var6.readException();
            var7 = var6.createByteArray();
         } finally {
            var6.recycle();
            var5.recycle();
         }

         return var7;
      }

      public void appendRingTone(boolean param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public IBinder asBinder() {
         return this.mRemote;
      }

      public boolean checkPrinterAvailable() throws RemoteException {
         boolean var2 = false;
         Parcel var3 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();
         boolean var7 = false;

         int var1;
         try {
            var7 = true;
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(12, var3, var4, 0);
            var4.readException();
            var1 = var4.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var4.recycle();
               var3.recycle();
            }
         }

         if(var1 != 0) {
            var2 = true;
         }

         var4.recycle();
         var3.recycle();
         return var2;
      }

      public void continueScan(boolean param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public Bitmap createBarCode(String param1, int param2, int param3, int param4, boolean param5) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public Bitmap createQRCode(String var1, int var2, int var3) throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         Bitmap var9;
         label36: {
            try {
               var7 = true;
               var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
               var4.writeString(var1);
               var4.writeInt(var2);
               var4.writeInt(var3);
               this.mRemote.transact(26, var4, var5, 0);
               var5.readException();
               if(var5.readInt() != 0) {
                  var9 = (Bitmap)Bitmap.CREATOR.createFromParcel(var5);
                  var7 = false;
                  break label36;
               }

               var7 = false;
            } finally {
               if(var7) {
                  var5.recycle();
                  var4.recycle();
               }
            }

            var9 = null;
         }

         var5.recycle();
         var4.recycle();
         return var9;
      }

      public void dataAppendEnter(boolean param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void generateSpace() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var1 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(27, var2, var1, 0);
            var1.readException();
         } finally {
            var1.recycle();
            var2.recycle();
         }

      }

      public int getDeviceModel() throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         int var1;
         try {
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(8, var4, var2, 0);
            var2.readException();
            var1 = var2.readInt();
         } finally {
            var2.recycle();
            var4.recycle();
         }

         return var1;
      }

      public void getFirmwareVersion() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(6, var3, var2, 0);
            var2.readException();
         } finally {
            var2.recycle();
            var3.recycle();
         }

      }

      public String getFirmwareVersion1() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         String var3;
         try {
            var1.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(31, var1, var2, 0);
            var2.readException();
            var3 = var2.readString();
         } finally {
            var2.recycle();
            var1.recycle();
         }

         return var3;
      }

      public String getFirmwareVersion2() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var1 = Parcel.obtain();

         String var3;
         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(32, var2, var1, 0);
            var1.readException();
            var3 = var1.readString();
         } finally {
            var1.recycle();
            var2.recycle();
         }

         return var3;
      }

      public Bitmap getHeader() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();
         boolean var5 = false;

         Bitmap var1;
         label36: {
            try {
               var5 = true;
               var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
               this.mRemote.transact(69, var3, var2, 0);
               var2.readException();
               if(var2.readInt() != 0) {
                  var1 = (Bitmap)Bitmap.CREATOR.createFromParcel(var2);
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

      public String getIdentifyInfo() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         String var3;
         try {
            var1.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(66, var1, var2, 0);
            var2.readException();
            var3 = var2.readString();
         } finally {
            var2.recycle();
            var1.recycle();
         }

         return var3;
      }

      public String getInterfaceDescriptor() {
         return "com.smartdevice.aidl.IZKCService";
      }

      public String getPrinterStatus() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         String var3;
         try {
            var1.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(10, var1, var2, 0);
            var2.readException();
            var3 = var2.readString();
         } finally {
            var2.recycle();
            var1.recycle();
         }

         return var3;
      }

      public String getServiceVersion() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         String var3;
         try {
            var1.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(7, var1, var2, 0);
            var2.readException();
            var3 = var2.readString();
         } finally {
            var2.recycle();
            var1.recycle();
         }

         return var3;
      }

      public boolean isScanning() throws RemoteException {
         boolean var2 = false;
         Parcel var3 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();
         boolean var7 = false;

         int var1;
         try {
            var7 = true;
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(65, var3, var4, 0);
            var4.readException();
            var1 = var4.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var4.recycle();
               var3.recycle();
            }
         }

         if(var1 != 0) {
            var2 = true;
         }

         var4.recycle();
         var3.recycle();
         return var2;
      }

      public boolean isTaskRunning() throws RemoteException {
         boolean var2 = false;
         Parcel var3 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         int var1;
         try {
            var7 = true;
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
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

      public void openBackLight(int var1) throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         try {
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeInt(var1);
            this.mRemote.transact(35, var4, var2, 0);
            var2.readException();
         } finally {
            var2.recycle();
            var4.recycle();
         }

      }

      public int openCard(int var1) throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         try {
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeInt(var1);
            this.mRemote.transact(46, var4, var2, 0);
            var2.readException();
            var1 = var2.readInt();
         } finally {
            var2.recycle();
            var4.recycle();
         }

         return var1;
      }

      public int openCard2(int[] var1, int var2) throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var3.writeIntArray(var1);
            var3.writeInt(var2);
            this.mRemote.transact(47, var3, var4, 0);
            var4.readException();
            var2 = var4.readInt();
            var4.readIntArray(var1);
         } finally {
            var4.recycle();
            var3.recycle();
         }

         return var2;
      }

      public int openCard3(long var1, int var3) throws RemoteException {
         Parcel var6 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var6.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var6.writeLong(var1);
            var6.writeInt(var3);
            this.mRemote.transact(48, var6, var4, 0);
            var4.readException();
            var3 = var4.readInt();
         } finally {
            var4.recycle();
            var6.recycle();
         }

         return var3;
      }

      public void openScan(boolean param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean printBarCode(String var1) throws RemoteException {
         boolean var3 = false;
         Parcel var4 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         int var2;
         try {
            var7 = true;
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeString(var1);
            this.mRemote.transact(33, var4, var5, 0);
            var5.readException();
            var2 = var5.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var5.recycle();
               var4.recycle();
            }
         }

         if(var2 != 0) {
            var3 = true;
         }

         var5.recycle();
         var4.recycle();
         return var3;
      }

      public void printBitmap(Bitmap param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void printBitmapAlgin(Bitmap param1, int param2, int param3, int param4) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void printColumnsText(String[] var1, int[] var2, int[] var3) throws RemoteException {
         Parcel var4 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();

         try {
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeStringArray(var1);
            var4.writeIntArray(var2);
            var4.writeIntArray(var3);
            this.mRemote.transact(22, var4, var5, 0);
            var5.readException();
         } finally {
            var5.recycle();
            var4.recycle();
         }

      }

      public void printGBKText(String var1) throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var2.writeString(var1);
            this.mRemote.transact(18, var2, var3, 0);
            var3.readException();
         } finally {
            var3.recycle();
            var2.recycle();
         }

      }

      public boolean printImageGray(Bitmap param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean printQrCode(String var1) throws RemoteException {
         boolean var3 = false;
         Parcel var4 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         int var2;
         try {
            var7 = true;
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeString(var1);
            this.mRemote.transact(34, var4, var5, 0);
            var5.readException();
            var2 = var5.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var5.recycle();
               var4.recycle();
            }
         }

         if(var2 != 0) {
            var3 = true;
         }

         var5.recycle();
         var4.recycle();
         return var3;
      }

      public boolean printRasterImage(Bitmap param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void printTextAlgin(String var1, int var2, int var3, int var4) throws RemoteException {
         Parcel var6 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();

         try {
            var6.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var6.writeString(var1);
            var6.writeInt(var2);
            var6.writeInt(var3);
            var6.writeInt(var4);
            this.mRemote.transact(21, var6, var5, 0);
            var5.readException();
         } finally {
            var5.recycle();
            var6.recycle();
         }

      }

      public void printTextWithFont(String var1, int var2, int var3) throws RemoteException {
         Parcel var5 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var5.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var5.writeString(var1);
            var5.writeInt(var2);
            var5.writeInt(var3);
            this.mRemote.transact(20, var5, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var5.recycle();
         }

      }

      public void printUnicodeText(String var1) throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var2.writeString(var1);
            this.mRemote.transact(19, var2, var3, 0);
            var3.readException();
         } finally {
            var3.recycle();
            var2.recycle();
         }

      }

      public boolean printUnicode_1F30(String var1) throws RemoteException {
         boolean var3 = false;
         Parcel var4 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         int var2;
         try {
            var7 = true;
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeString(var1);
            this.mRemote.transact(30, var4, var5, 0);
            var5.readException();
            var2 = var5.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var5.recycle();
               var4.recycle();
            }
         }

         if(var2 != 0) {
            var3 = true;
         }

         var5.recycle();
         var4.recycle();
         return var3;
      }

      public void printerInit() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         try {
            var1.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(9, var1, var2, 0);
            var2.readException();
         } finally {
            var2.recycle();
            var1.recycle();
         }

      }

      public void printerSelfChecking() throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var1 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(11, var2, var1, 0);
            var1.readException();
         } finally {
            var1.recycle();
            var2.recycle();
         }

      }

      public void recoveryFactorySet(boolean param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void registerCallBack(String param1, ICallBack param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void scan() throws RemoteException {
         Parcel var1 = Parcel.obtain();
         Parcel var2 = Parcel.obtain();

         try {
            var1.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(58, var1, var2, 0);
            var2.readException();
         } finally {
            var2.recycle();
            var1.recycle();
         }

      }

      public void scanRepeatHint(boolean param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public byte[] sendCommand(byte[] var1) throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var3 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var2.writeByteArray(var1);
            this.mRemote.transact(64, var2, var3, 0);
            var3.readException();
            var1 = var3.createByteArray();
         } finally {
            var3.recycle();
            var2.recycle();
         }

         return var1;
      }

      public void sendRAWData(String var1, byte[] var2) throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var3.writeString(var1);
            var3.writeByteArray(var2);
            this.mRemote.transact(13, var3, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var3.recycle();
         }

      }

      public void setAlignment(int var1) throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var3.writeInt(var1);
            this.mRemote.transact(15, var3, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var3.recycle();
         }

      }

      public void setFontSize(int var1) throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var2.writeInt(var1);
            this.mRemote.transact(17, var2, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var2.recycle();
         }

      }

      public boolean setGPIO(int var1, int var2) throws RemoteException {
         boolean var3 = false;
         Parcel var5 = Parcel.obtain();
         Parcel var6 = Parcel.obtain();
         boolean var8 = false;

         try {
            var8 = true;
            var5.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var5.writeInt(var1);
            var5.writeInt(var2);
            this.mRemote.transact(45, var5, var6, 0);
            var6.readException();
            var1 = var6.readInt();
            var8 = false;
         } finally {
            if(var8) {
               var6.recycle();
               var5.recycle();
            }
         }

         if(var1 != 0) {
            var3 = true;
         }

         var6.recycle();
         var5.recycle();
         return var3;
      }

      public boolean setModuleFlag(int var1) throws RemoteException {
         boolean var2 = true;
         Parcel var4 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         try {
            var7 = true;
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeInt(var1);
            this.mRemote.transact(1, var4, var5, 0);
            var5.readException();
            var1 = var5.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var5.recycle();
               var4.recycle();
            }
         }

         if(var1 == 0) {
            var2 = false;
         }

         var5.recycle();
         var4.recycle();
         return var2;
      }

      public boolean setPrinterLanguage(String var1, int var2) throws RemoteException {
         boolean var3 = false;
         Parcel var5 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();
         boolean var7 = false;

         try {
            var7 = true;
            var5.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var5.writeString(var1);
            var5.writeInt(var2);
            this.mRemote.transact(14, var5, var4, 0);
            var4.readException();
            var2 = var4.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var4.recycle();
               var5.recycle();
            }
         }

         if(var2 != 0) {
            var3 = true;
         }

         var4.recycle();
         var5.recycle();
         return var3;
      }

      public void setTypeface(int var1) throws RemoteException {
         Parcel var2 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();

         try {
            var2.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var2.writeInt(var1);
            this.mRemote.transact(16, var2, var4, 0);
            var4.readException();
         } finally {
            var4.recycle();
            var2.recycle();
         }

      }

      public boolean showDotImage(int param1, int param2, Bitmap param3) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean showRGB565Image(Bitmap param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean showRGB565ImageByPath(String var1) throws RemoteException {
         boolean var3 = false;
         Parcel var5 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();
         boolean var7 = false;

         int var2;
         try {
            var7 = true;
            var5.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var5.writeString(var1);
            this.mRemote.transact(37, var5, var4, 0);
            var4.readException();
            var2 = var4.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var4.recycle();
               var5.recycle();
            }
         }

         if(var2 != 0) {
            var3 = true;
         }

         var4.recycle();
         var5.recycle();
         return var3;
      }

      public boolean showRGB565ImageCenter(Bitmap param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean showRGB565ImageLocation(Bitmap param1, int param2, int param3, int param4, int param5) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public void stopRunningTask() throws RemoteException {
         Parcel var3 = Parcel.obtain();
         Parcel var1 = Parcel.obtain();

         try {
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(4, var3, var1, 0);
            var1.readException();
         } finally {
            var1.recycle();
            var3.recycle();
         }

      }

      public boolean turnOff() throws RemoteException {
         boolean var2 = false;
         Parcel var3 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();
         boolean var7 = false;

         int var1;
         try {
            var7 = true;
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(68, var3, var4, 0);
            var4.readException();
            var1 = var4.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var4.recycle();
               var3.recycle();
            }
         }

         if(var1 != 0) {
            var2 = true;
         }

         var4.recycle();
         var3.recycle();
         return var2;
      }

      public boolean turnOn() throws RemoteException {
         boolean var2 = false;
         Parcel var3 = Parcel.obtain();
         Parcel var4 = Parcel.obtain();
         boolean var7 = false;

         int var1;
         try {
            var7 = true;
            var3.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            this.mRemote.transact(67, var3, var4, 0);
            var4.readException();
            var1 = var4.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var4.recycle();
               var3.recycle();
            }
         }

         if(var1 != 0) {
            var2 = true;
         }

         var4.recycle();
         var3.recycle();
         return var2;
      }

      public void unregisterCallBack(String param1, ICallBack param2) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean updateLogo(Bitmap param1) throws RemoteException {
         // $FF: Couldn't be decompiled
      }

      public boolean updateLogoByPath(String var1) throws RemoteException {
         boolean var3 = false;
         Parcel var4 = Parcel.obtain();
         Parcel var5 = Parcel.obtain();
         boolean var7 = false;

         int var2;
         try {
            var7 = true;
            var4.writeInterfaceToken("com.smartdevice.aidl.IZKCService");
            var4.writeString(var1);
            this.mRemote.transact(40, var4, var5, 0);
            var5.readException();
            var2 = var5.readInt();
            var7 = false;
         } finally {
            if(var7) {
               var5.recycle();
               var4.recycle();
            }
         }

         if(var2 != 0) {
            var3 = true;
         }

         var5.recycle();
         var4.recycle();
         return var3;
      }
   }
}
