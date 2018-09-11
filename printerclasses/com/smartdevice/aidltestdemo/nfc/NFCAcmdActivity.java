package com.smartdevice.aidltestdemo.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class NFCAcmdActivity extends Activity {
   private static boolean READ_LOCK = false;
   private static final String TAG = null;
   private NfcAdapter mAdapter;
   private IntentFilter[] mFilters;
   private PendingIntent mPendingIntent;
   private String[][] mTechLists;
   private TextView textview;

   private String gb2312ToString(byte[] var1) {
      Object var2 = null;

      String var3;
      String var5;
      try {
         var3 = new String(var1, "gb2312");
      } catch (UnsupportedEncodingException var4) {
         var5 = (String)var2;
         return var5;
      }

      var5 = var3;
      return var5;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361824);
      this.textview = (TextView)this.findViewById(2131230959);
      this.mAdapter = NfcAdapter.getDefaultAdapter(this);
      this.mPendingIntent = PendingIntent.getActivity(this, 0, (new Intent(this, this.getClass())).addFlags(536870912), 0);
      this.mFilters = new IntentFilter[]{new IntentFilter("android.nfc.action.TECH_DISCOVERED")};
      this.mTechLists = new String[][]{{MifareClassic.class.getName()}, {NfcA.class.getName()}};
      if("android.nfc.action.TECH_DISCOVERED".equals(this.getIntent().getAction())) {
         this.resolveIntentNfcA(this.getIntent());
      }

   }

   public void onNewIntent(Intent var1) {
      this.resolveIntentNfcA(var1);
   }

   public void onPause() {
      super.onPause();
      if(this.mAdapter != null) {
         this.mAdapter.disableForegroundDispatch(this);
      }

   }

   public void onResume() {
      super.onResume();
      if(this.mAdapter != null && !this.mAdapter.isEnabled()) {
         ;
      }

      if(this.mAdapter != null) {
         this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent, this.mFilters, this.mTechLists);
      }

   }

   void resolveIntentNfcA(Intent var1) {
      if(!READ_LOCK) {
         READ_LOCK = true;
         Tag var2 = (Tag)var1.getParcelableExtra("android.nfc.extra.TAG");
         if("android.nfc.action.TECH_DISCOVERED".equals(var1.getAction())) {
            Tag var11 = (Tag)var1.getParcelableExtra("android.nfc.extra.TAG");
            Log.i(TAG, Arrays.toString(var11.getTechList()));
            this.textview.setText("");

            try {
               StringBuilder var3;
               try {
                  NfcA var15 = NfcA.get(var2);
                  var15.connect();
                  this.textview.setText("connect to card");
                  if(var15.transceive(new byte[]{(byte)0, (byte)-124, (byte)0, (byte)0, (byte)4})[0] != 0) {
                     byte[] var16 = var15.transceive(new byte[]{(byte)0, (byte)-92, (byte)0, (byte)0, (byte)0});
                     String var4 = TAG;
                     StringBuilder var13 = new StringBuilder();
                     Log.i(var4, var13.append("是否已写入数据").append(var16[0]).toString());
                  }

                  var15.close();
               } catch (IOException var8) {
                  String var14 = TAG;
                  var3 = new StringBuilder();
                  Log.e(var14, var3.append("读卡失败").append(var8.getMessage()).toString());
               } catch (Exception var9) {
                  String var12 = TAG;
                  var3 = new StringBuilder();
                  Log.e(var12, var3.append("读卡失败").append(var9.getMessage()).toString());
               }
            } finally {
               ;
            }
         }

         READ_LOCK = false;
      }

   }

   void resolveIntentNfcB(Intent param1) {
      // $FF: Couldn't be decompiled
   }
}
