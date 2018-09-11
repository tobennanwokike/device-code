package com.smartdevice.aidltestdemo.scan;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.smartdevice.aidltestdemo.BaseActivity;
import com.smartdevice.aidltestdemo.scan.ClientConfig;
import java.io.UnsupportedEncodingException;

public class ScanSetActivity extends BaseActivity implements OnClickListener {
   private Button btnRecoveryFactory;
   private Button button_sendcommand;
   private CheckBox checkBox_hex;
   private CheckBox checkbox_addEnter;
   private CheckBox checkbox_continueScan;
   private CheckBox checkbox_keyBordInput;
   private CheckBox checkbox_openScan;
   private CheckBox checkbox_openSound;
   private CheckBox checkbox_openVibration;
   private CheckBox checkbox_repeatScanTip;
   private CheckBox checkbox_reset;
   private EditText editText_command;
   private boolean runFlag = true;
   private ScrollView scrollView_setting;

   public static byte[] StringToByteArray(String var0) {
      int var2 = var0.length() / 2;
      byte[] var3 = new byte[var2];

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = Integer.valueOf(var0.substring(var1 * 2, var1 * 2 + 2), 16).byteValue();
      }

      return var3;
   }

   // $FF: synthetic method
   static ScrollView access$000(ScanSetActivity var0) {
      return var0.scrollView_setting;
   }

   private void initView() {
      this.scrollView_setting = (ScrollView)this.findViewById(2131230926);
      this.checkBox_hex = (CheckBox)this.findViewById(2131230819);
      this.editText_command = (EditText)this.findViewById(2131230843);
      this.button_sendcommand = (Button)this.findViewById(2131230804);
      this.button_sendcommand.setOnClickListener(this);
      this.checkbox_openScan = (CheckBox)this.findViewById(2131230825);
      this.checkbox_openScan.setOnCheckedChangeListener(new ScanSetActivity.checkBoxCheckedChangeListener());
      this.checkbox_openScan.setChecked(ClientConfig.getBoolean("open_scan").booleanValue());
      this.checkbox_keyBordInput = (CheckBox)this.findViewById(2131230824);
      this.checkbox_keyBordInput.setOnCheckedChangeListener(new ScanSetActivity.checkBoxCheckedChangeListener());
      this.checkbox_keyBordInput.setChecked(ClientConfig.getBoolean("open_scan").booleanValue());
      this.checkbox_addEnter = (CheckBox)this.findViewById(2131230822);
      this.checkbox_addEnter.setOnCheckedChangeListener(new ScanSetActivity.checkBoxCheckedChangeListener());
      this.checkbox_addEnter.setChecked(ClientConfig.getBoolean("data_append_enter").booleanValue());
      this.checkbox_openSound = (CheckBox)this.findViewById(2131230826);
      this.checkbox_openSound.setOnCheckedChangeListener(new ScanSetActivity.checkBoxCheckedChangeListener());
      this.checkbox_openSound.setChecked(ClientConfig.getBoolean("append_ringtone").booleanValue());
      this.checkbox_openVibration = (CheckBox)this.findViewById(2131230827);
      this.checkbox_openVibration.setOnCheckedChangeListener(new ScanSetActivity.checkBoxCheckedChangeListener());
      this.checkbox_openVibration.setChecked(ClientConfig.getBoolean("append_vibate").booleanValue());
      this.checkbox_continueScan = (CheckBox)this.findViewById(2131230823);
      this.checkbox_continueScan.setOnCheckedChangeListener(new ScanSetActivity.checkBoxCheckedChangeListener());
      this.checkbox_continueScan.setChecked(ClientConfig.getBoolean("continue_scan").booleanValue());
      this.checkbox_repeatScanTip = (CheckBox)this.findViewById(2131230828);
      this.checkbox_repeatScanTip.setOnCheckedChangeListener(new ScanSetActivity.checkBoxCheckedChangeListener());
      this.checkbox_repeatScanTip.setChecked(ClientConfig.getBoolean("scan_repeat").booleanValue());
      this.btnRecoveryFactory = (Button)this.findViewById(2131230765);
      this.btnRecoveryFactory.setOnClickListener(this);
   }

   private void sendCommand() {
      try {
         String var1 = this.editText_command.getText().toString();
         if(this.checkBox_hex.isChecked()) {
            if(mIzkcService != null) {
               mIzkcService.sendCommand(StringToByteArray(var1));
            }
         } else {
            try {
               if(mIzkcService != null) {
                  mIzkcService.sendCommand(var1.getBytes("US-ASCII"));
               }
            } catch (UnsupportedEncodingException var2) {
               var2.printStackTrace();
            }
         }
      } catch (Exception var3) {
         Log.e("ScanSetActivity", var3.getMessage());
         Toast.makeText(this, var3.getMessage(), 0).show();
      }

   }

   private void setCheckBoxEnable(boolean var1) {
      this.checkbox_openScan.setEnabled(var1);
   }

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         this.setCheckBoxEnable(true);
      default:
      }
   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131230765:
         try {
            mIzkcService.recoveryFactorySet(true);
         } catch (RemoteException var2) {
            var2.printStackTrace();
         }
         break;
      case 2131230804:
         this.sendCommand();
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361865);
      this.initView();
      this.setCheckBoxEnable(false);
   }

   class checkBoxCheckedChangeListener implements OnCheckedChangeListener {
      public void onCheckedChanged(CompoundButton param1, boolean param2) {
         // $FF: Couldn't be decompiled
      }
   }
}
