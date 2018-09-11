package com.smartdevice.aidltestdemo;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.smartdevice.aidltestdemo.BaseActivity;

public class PSAMActivityV2 extends BaseActivity implements OnClickListener {
   protected static final String TAG = "MainActivity";
   Button btn_init;
   Button btn_random;
   Button button_power_off;
   Button button_power_on;
   Button button_send;
   int cardLocation = 1;
   EditText editText1;
   EditText editText_cmd;
   long fd = 0L;
   int powerValue = 2;
   RadioGroup radioGroupCard;
   RadioGroup radioGroupPower;
   private boolean runFlag = true;

   private void closeCard() {
      // $FF: Couldn't be decompiled
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
            RadioButton var3 = (RadioButton)PSAMActivityV2.this.findViewById(var1.getCheckedRadioButtonId());
            PSAMActivityV2.this.cardLocation = Integer.parseInt(var3.getTag().toString());
         }
      });
      this.radioGroupPower = (RadioGroup)this.findViewById(2131230906);
      this.radioGroupPower.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(RadioGroup var1, int var2) {
            RadioButton var3 = (RadioButton)PSAMActivityV2.this.findViewById(var1.getCheckedRadioButtonId());
            PSAMActivityV2.this.powerValue = Integer.parseInt(var3.getTag().toString());
         }
      });
   }

   private void openCard() {
      // $FF: Couldn't be decompiled
   }

   private void reset() {
      // $FF: Couldn't be decompiled
   }

   private void sendApdu() {
      // $FF: Couldn't be decompiled
   }

   private void test() {
      // $FF: Couldn't be decompiled
   }

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         try {
            mIzkcService.setGPIO(24, 1);
            this.fd = (long)mIzkcService.Open();
         } catch (RemoteException var2) {
            var2.printStackTrace();
         }
      default:
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
      this.setContentView(2131361829);
      this.initView();
   }

   protected void onDestroy() {
      try {
         mIzkcService.CloseCard2(this.fd, true);
         mIzkcService.Close(this.fd);
         mIzkcService.setGPIO(24, 0);
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

      super.onDestroy();
   }
}
