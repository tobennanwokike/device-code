package com.smartdevice.aidltestdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.smartdevice.aidl.IZKCService;
import com.smartdevice.aidltestdemo.common.MessageCenter;

public class BaseActivity extends Activity {
   public static int DEVICE_MODEL = 0;
   public static String MODULE_FLAG = "module_flag";
   public static IZKCService mIzkcService;
   public static int module_flag = 0;
   private ServiceConnection mServiceConn = new ServiceConnection() {
      public void onServiceConnected(ComponentName var1, IBinder var2) {
         Log.e("client", "onServiceConnected");
         BaseActivity.mIzkcService = IZKCService.Stub.asInterface(var2);
         if(BaseActivity.mIzkcService != null) {
            try {
               Toast.makeText(BaseActivity.this, BaseActivity.this.getString(2131558483), 0).show();
               BaseActivity.DEVICE_MODEL = BaseActivity.mIzkcService.getDeviceModel();
               BaseActivity.mIzkcService.setModuleFlag(BaseActivity.module_flag);
            } catch (RemoteException var3) {
               var3.printStackTrace();
            }

            BaseActivity.this.sendEmptyMessage(17);
         }

      }

      public void onServiceDisconnected(ComponentName var1) {
         Log.e("client", "onServiceDisconnected");
         BaseActivity.mIzkcService = null;
         Toast.makeText(BaseActivity.this, BaseActivity.this.getString(2131558482), 0).show();
         BaseActivity.this.sendEmptyMessage(18);
      }
   };
   private Handler mhanlder;

   public void bindService() {
      Intent var1 = new Intent("com.zkc.aidl.all");
      var1.setPackage("com.smartdevice.aidl");
      this.bindService(var1, this.mServiceConn, 1);
   }

   protected Context getDialogContext() {
      Object var1;
      for(var1 = this; ((Activity)var1).getParent() != null; var1 = ((Activity)var1).getParent()) {
         ;
      }

      Log.d("Dialog", "context:" + var1);
      return (Context)var1;
   }

   protected Handler getHandler() {
      if(this.mhanlder == null) {
         this.mhanlder = new Handler() {
            public void handleMessage(Message var1) {
               BaseActivity.this.handleStateMessage(var1);
            }
         };
      }

      return this.mhanlder;
   }

   protected void handleStateMessage(Message var1) {
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      MessageCenter.getInstance().addHandler(this.getHandler());
      ((InputMethodManager)this.getSystemService("input_method")).toggleSoftInput(0, 2);
      module_flag = this.getIntent().getIntExtra(MODULE_FLAG, 8);
      this.bindService();
   }

   protected void onDestroy() {
      this.unbindService();
      MessageCenter.getInstance().removeHandler(this.getHandler());
      super.onDestroy();
   }

   protected void onResume() {
      super.onResume();
   }

   protected void sendEmptyMessage(int var1) {
      this.getHandler().sendEmptyMessage(var1);
   }

   protected void sendMessage(int var1, Object var2) {
      Message var3 = new Message();
      var3.what = var1;
      var3.obj = var2;
      this.getHandler().sendMessage(var3);
   }

   protected void sendMessage(Message var1) {
      this.getHandler().sendMessage(var1);
   }

   public void unbindService() {
      this.unbindService(this.mServiceConn);
   }
}
