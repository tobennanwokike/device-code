package com.smartdevice.aidltestdemo;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.smartdevice.aidltestdemo.BaseActivity;
import com.smartdevice.aidltestdemo.util.ExecutorFactory;

public class IDCardActivity extends BaseActivity implements OnClickListener {
   private Button btnTurnOff;
   private Button btnTurnOn;
   private Button btn_getIdentifyInfo;
   private ImageView img_header;
   boolean linkSuccess = false;
   private TextView tv_sender_info;

   private void initView() {
      this.btn_getIdentifyInfo = (Button)this.findViewById(2131230773);
      this.btn_getIdentifyInfo.setEnabled(false);
      this.btn_getIdentifyInfo.setOnClickListener(this);
      this.tv_sender_info = (TextView)this.findViewById(2131230979);
      this.img_header = (ImageView)this.findViewById(2131230864);
   }

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         this.linkSuccess = true;
         this.btn_getIdentifyInfo.setEnabled(true);
         break;
      case 18:
         this.linkSuccess = false;
         break;
      case 19:
         this.btn_getIdentifyInfo.setEnabled(true);
         String var3 = (String)var1.obj;
         if(!TextUtils.isEmpty(var3)) {
            this.tv_sender_info.setText(var3);

            try {
               this.img_header.setImageBitmap(mIzkcService.getHeader());
            } catch (RemoteException var2) {
               var2.printStackTrace();
            }
         }
      }

   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131230773:
         this.btn_getIdentifyInfo.setEnabled(false);
         this.tv_sender_info.setText("");
         if(this.linkSuccess) {
            (new Thread(new Runnable() {
               public void run() {
                  // $FF: Couldn't be decompiled
               }
            })).start();
         } else {
            Toast.makeText(this, "未连接", 0).show();
         }
      default:
      }
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361820);
      this.initView();
   }

   protected void onDestroy() {
      if(mIzkcService != null) {
         try {
            mIzkcService.turnOff();
            mIzkcService.setModuleFlag(8);
         } catch (RemoteException var2) {
            var2.printStackTrace();
         }
      }

      super.onDestroy();
   }

   protected void onResume() {
      super.onResume();
      ExecutorFactory.executeLogic(new Runnable() {
         public void run() {
            try {
               SystemClock.sleep(2000L);
               if(BaseActivity.mIzkcService != null) {
                  BaseActivity.mIzkcService.turnOn();
               }
            } catch (RemoteException var2) {
               var2.printStackTrace();
            }

         }
      });
   }
}
