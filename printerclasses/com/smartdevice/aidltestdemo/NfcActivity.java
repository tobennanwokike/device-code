package com.smartdevice.aidltestdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.smartdevice.aidltestdemo.nfc.NFCAcmdActivity;
import com.smartdevice.aidltestdemo.nfc.NFCActivity;

public class NfcActivity extends Activity implements OnClickListener {
   private Button btn_nfca;
   private Button btn_nfca_cmd;

   private void initView() {
      this.btn_nfca = (Button)this.findViewById(2131230777);
      this.btn_nfca_cmd = (Button)this.findViewById(2131230778);
      this.btn_nfca.setOnClickListener(this);
      this.btn_nfca_cmd.setOnClickListener(this);
   }

   public void onClick(View var1) {
      Object var2 = null;
      Intent var3;
      switch(var1.getId()) {
      case 2131230777:
         var3 = new Intent(this, NFCActivity.class);
         break;
      case 2131230778:
         var3 = new Intent(this, NFCAcmdActivity.class);
         break;
      default:
         var3 = (Intent)var2;
      }

      if(var3 != null) {
         this.startActivity(var3);
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361823);
      this.initView();
   }
}
