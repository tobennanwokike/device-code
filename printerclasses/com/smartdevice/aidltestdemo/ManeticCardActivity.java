package com.smartdevice.aidltestdemo;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.widget.EditText;
import com.smartdevice.aidl.ICallBack;
import com.smartdevice.aidltestdemo.BaseActivity;

public class ManeticCardActivity extends BaseActivity {
   private EditText et_receiver;
   private ICallBack mCallBack = new ICallBack.Stub() {
      public void onReturnValue(byte[] var1, int var2) throws RemoteException {
      }
   };
   private boolean runFlag = true;

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         try {
            mIzkcService.registerCallBack("IC", this.mCallBack);
         } catch (RemoteException var2) {
            var2.printStackTrace();
         }
      default:
      }
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361821);
      this.et_receiver = (EditText)this.findViewById(2131230841);
      this.et_receiver.setCursorVisible(false);
      this.et_receiver.setFocusable(false);
      this.et_receiver.setFocusableInTouchMode(false);
   }

   protected void onStop() {
      try {
         mIzkcService.unregisterCallBack("IC", this.mCallBack);
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

      super.onStop();
   }
}
