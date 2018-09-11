package com.smartdevice.aidltestdemo;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.smartdevice.aidltestdemo.BaseActivity;

public class PSAMActivity2 extends BaseActivity implements OnClickListener {
   protected static final String TAG = "MainActivity";
   Button btn_init;
   Button btn_random;
   Button button_power_off;
   Button button_power_on;
   Button button_send;
   int cardLocation = 2;
   EditText editText1;
   EditText editText_cmd;
   int[] fd = new int[1];
   RadioGroup radioGroupCard;

   public static String bytes2HexString(byte[] var0) {
      StringBuffer var4 = new StringBuffer();

      for(int var1 = 0; var1 < var0.length; ++var1) {
         String var3 = Integer.toHexString(var0[var1] & 255);
         String var2 = var3;
         if(var3.length() == 1) {
            var2 = '0' + var3;
         }

         var4.append(var2.toUpperCase());
      }

      return var4.toString();
   }

   public static String bytes2HexString(byte[] var0, int var1) {
      StringBuffer var5 = new StringBuffer();

      for(int var2 = 0; var2 < var1; ++var2) {
         String var4 = Integer.toHexString(var0[var2] & 255);
         String var3 = var4;
         if(var4.length() == 1) {
            var3 = '0' + var4;
         }

         var5.append(var3.toUpperCase());
      }

      return var5.toString();
   }

   private void closeCard() {
      // $FF: Couldn't be decompiled
   }

   public static byte[] hexString2Bytes(String var0) {
      int var2 = var0.length() / 2;
      byte[] var3 = new byte[var2];

      for(int var1 = 0; var1 < var2; ++var1) {
         var3[var1] = Integer.valueOf(var0.substring(var1 * 2, var1 * 2 + 2), 16).byteValue();
      }

      return var3;
   }

   private void initView() {
      this.editText1 = (EditText)this.findViewById(2131230841);
      this.editText_cmd = (EditText)this.findViewById(2131230842);
      this.button_power_on = (Button)this.findViewById(2131230802);
      this.button_power_on.setOnClickListener(this);
      this.button_power_off = (Button)this.findViewById(2131230801);
      this.button_power_off.setOnClickListener(this);
      this.btn_init = (Button)this.findViewById(2131230800);
      this.btn_init.setOnClickListener(this);
      this.btn_random = (Button)this.findViewById(2131230787);
      this.btn_random.setOnClickListener(this);
      this.button_send = (Button)this.findViewById(2131230803);
      this.button_send.setOnClickListener(this);
      this.radioGroupCard = (RadioGroup)this.findViewById(2131230905);
      this.radioGroupCard.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(RadioGroup var1, int var2) {
            RadioButton var3 = (RadioButton)PSAMActivity2.this.findViewById(var1.getCheckedRadioButtonId());
            PSAMActivity2.this.cardLocation = Integer.parseInt(var3.getTag().toString()) * 16 + 2;
         }
      });
   }

   private void openCard() {
      // $FF: Couldn't be decompiled
   }

   private void reset() {
      if(this.fd[0] > 0) {
         byte[] var1 = new byte[256];
         int[] var3 = new int[1];

         try {
            mIzkcService.ResetCard2((long)this.fd[0], var1, var3);
         } catch (RemoteException var4) {
            var4.printStackTrace();
         }

         if(var1 != null) {
            this.editText1.setText(bytes2HexString(var1, var3[0]).toString());
         }
      }

   }

   private void sendApdu() {
      if(this.fd[0] > 0) {
         byte[] var3 = hexString2Bytes(this.editText_cmd.getText().toString());
         byte[] var1 = new byte[256];
         int[] var2 = new int[1];

         try {
            mIzkcService.CardApdu2((long)this.fd[0], var3, var3.length, var1, var2);
         } catch (RemoteException var4) {
            var4.printStackTrace();
         }

         if(var1 != null) {
            this.editText1.setText(bytes2HexString(var1, var2[0]).toString());
         }
      }

   }

   private void test() {
      if(this.fd[0] > 0) {
         byte[] var3 = new byte[]{(byte)0, (byte)-124, (byte)0, (byte)0, (byte)8};
         byte[] var1 = new byte[256];
         int[] var2 = new int[1];

         try {
            mIzkcService.CardApdu2((long)this.fd[0], var3, var3.length, var1, var2);
         } catch (RemoteException var4) {
            var4.printStackTrace();
         }

         if(var1 != null) {
            this.editText1.setText(bytes2HexString(var1, var2[0]).toString());
         }
      }

   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131230787:
         this.test();
         break;
      case 2131230800:
         this.reset();
         break;
      case 2131230801:
         this.closeCard();
         break;
      case 2131230802:
         this.openCard();
         break;
      case 2131230803:
         this.sendApdu();
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361828);
      this.initView();
   }

   protected void onDestroy() {
      try {
         mIzkcService.CloseCard2((long)this.fd[0], false);
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

      super.onDestroy();
   }
}
