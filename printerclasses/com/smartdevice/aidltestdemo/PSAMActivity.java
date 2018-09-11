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

public class PSAMActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
   public static final String TAG = PSAMActivity.class.getSimpleName();
   Button btn_init;
   Button btn_power_off;
   Button btn_power_on;
   Button btn_random;
   Button btn_send;
   int cardLocation = 1;
   EditText et_cmd;
   EditText et_receiver;
   int powerValue = 2;
   RadioGroup radioGroupCard;
   RadioGroup radioGroupPower;

   private void closePower() {
      // $FF: Couldn't be decompiled
   }

   private void initEvent() {
      this.btn_init.setOnClickListener(this);
      this.btn_random.setOnClickListener(this);
      this.btn_send.setOnClickListener(this);
      this.btn_power_on.setOnClickListener(this);
      this.btn_power_off.setOnClickListener(this);
      this.radioGroupCard.setOnCheckedChangeListener(this);
      this.radioGroupPower.setOnCheckedChangeListener(this);
   }

   private void initView() {
      this.btn_init = (Button)this.findViewById(2131230775);
      this.btn_random = (Button)this.findViewById(2131230787);
      this.btn_send = (Button)this.findViewById(2131230791);
      this.btn_power_on = (Button)this.findViewById(2131230786);
      this.btn_power_off = (Button)this.findViewById(2131230785);
      this.radioGroupCard = (RadioGroup)this.findViewById(2131230905);
      this.radioGroupPower = (RadioGroup)this.findViewById(2131230906);
      this.et_receiver = (EditText)this.findViewById(2131230851);
      this.et_cmd = (EditText)this.findViewById(2131230849);
   }

   private void openPower() {
      // $FF: Couldn't be decompiled
   }

   private void reset() {
      // $FF: Couldn't be decompiled
   }

   private void sendApdu() {
      // $FF: Couldn't be decompiled
   }

   private void testRandom() {
      // $FF: Couldn't be decompiled
   }

   public void onCheckedChanged(RadioGroup var1, int var2) {
      if(var1.getId() == 2131230905) {
         this.cardLocation = Integer.parseInt(((RadioButton)this.findViewById(var1.getCheckedRadioButtonId())).getTag().toString());
         this.closePower();
      } else if(var1.getId() == 2131230906) {
         this.powerValue = Integer.parseInt(((RadioButton)this.findViewById(var1.getCheckedRadioButtonId())).getTag().toString());
      }

   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131230775:
         this.reset();
         break;
      case 2131230785:
         this.closePower();
         break;
      case 2131230786:
         this.openPower();
         break;
      case 2131230787:
         this.testRandom();
         break;
      case 2131230791:
         this.sendApdu();
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361827);
      this.initView();
      this.initEvent();
   }

   protected void onStop() {
      try {
         mIzkcService.CloseCard();
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

      super.onStop();
   }
}
