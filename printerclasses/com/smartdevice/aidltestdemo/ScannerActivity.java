package com.smartdevice.aidltestdemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.smartdevice.aidl.ICallBack;
import com.smartdevice.aidltestdemo.BaseActivity;
import com.smartdevice.aidltestdemo.scan.ClientConfig;
import com.smartdevice.aidltestdemo.scan.ScanInstructionActivity;
import com.smartdevice.aidltestdemo.scan.ScanSetActivity;

public class ScannerActivity extends BaseActivity implements OnClickListener {
   private Button btn_clear;
   private Button btn_instruction;
   private Button btn_scan;
   private Button btn_set;
   private EditText et_code;
   private String firstCodeStr = "";
   ICallBack.Stub mCallback = new ICallBack.Stub() {
      public void onReturnValue(byte[] var1, int var2) throws RemoteException {
         String var4 = new String(var1, 0, var2);
         if(ClientConfig.getBoolean("scan_repeat").booleanValue() && ScannerActivity.this.firstCodeStr.equals(var4)) {
            ScannerActivity.this.vibrator.vibrate(100L);
         }

         if(ClientConfig.getBoolean("append_ringtone").booleanValue()) {
            ScannerActivity.this.player.start();
         }

         if(ClientConfig.getBoolean("append_vibate").booleanValue()) {
            ScannerActivity.this.vibrator.vibrate(100L);
         }

         ScannerActivity.this.firstCodeStr = var4;
         StringBuilder var3 = new StringBuilder();
         ScannerActivity var5 = ScannerActivity.this;
         var5.text = var3.append(var5.text).append(var4).append("\n").toString();
         ScannerActivity.this.sendMessage(23, ScannerActivity.this.text);
      }
   };
   MediaPlayer player;
   int receiver = 0;
   int send = 0;
   public String text = "";
   TextView tv_receiver;
   TextView tv_send;
   Vibrator vibrator;

   private void initEvent() {
      this.btn_scan.setOnClickListener(this);
      this.btn_clear.setOnClickListener(this);
      this.btn_instruction.setOnClickListener(this);
      this.btn_set.setOnClickListener(this);
   }

   private void initView() {
      this.et_code = (EditText)this.findViewById(2131230850);
      this.btn_scan = (Button)this.findViewById(2131230790);
      this.btn_clear = (Button)this.findViewById(2131230771);
      this.btn_instruction = (Button)this.findViewById(2131230776);
      this.btn_set = (Button)this.findViewById(2131230792);
      this.tv_receiver = (TextView)this.findViewById(2131230977);
      this.tv_send = (TextView)this.findViewById(2131230978);
   }

   private void registerCallbackAndInitScan() {
      try {
         mIzkcService.registerCallBack("Scanner", this.mCallback);
         mIzkcService.openScan(ClientConfig.getBoolean("open_scan").booleanValue());
         mIzkcService.dataAppendEnter(ClientConfig.getBoolean("data_append_enter").booleanValue());
         this.btn_scan.setEnabled(true);
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

   }

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         Toast.makeText(this, this.getString(2131558483), 0).show();
         this.registerCallbackAndInitScan();
         break;
      case 18:
         Toast.makeText(this, this.getString(2131558482), 0).show();
         break;
      case 23:
         this.et_code.setText((String)var1.obj);
         this.tv_receiver.setText("R:" + this.et_code.getText().length());
      }

   }

   public void onClick(View var1) {
      Object var2 = null;
      Intent var4;
      switch(var1.getId()) {
      case 2131230771:
         this.et_code.setText("");
         this.text = "";
         this.tv_receiver.setText("R:  ");
         this.tv_send.setText("S:  ");
         this.send = 0;
         this.receiver = 0;
         var4 = (Intent)var2;
         break;
      case 2131230776:
         var4 = new Intent(this, ScanInstructionActivity.class);
         break;
      case 2131230790:
         try {
            mIzkcService.scan();
         } catch (RemoteException var3) {
            var3.printStackTrace();
            var4 = (Intent)var2;
            break;
         }

         var4 = (Intent)var2;
         break;
      case 2131230792:
         var4 = new Intent(this, ScanSetActivity.class);
         var4.putExtra(BaseActivity.MODULE_FLAG, 4);
         break;
      default:
         var4 = (Intent)var2;
      }

      if(var4 != null) {
         this.startActivity(var4);
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361833);
      this.initView();
      this.initEvent();
      this.btn_scan.setEnabled(false);
      this.player = MediaPlayer.create(this.getApplicationContext(), 2131492865);
      this.vibrator = (Vibrator)this.getSystemService("vibrator");
   }

   protected void onDestroy() {
      try {
         mIzkcService.unregisterCallBack("Scanner", this.mCallback);
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

      super.onDestroy();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      return super.onKeyDown(var1, var2);
   }

   protected void onResume() {
      super.onResume();
   }
}
