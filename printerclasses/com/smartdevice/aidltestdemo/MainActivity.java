package com.smartdevice.aidltestdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.smartdevice.aidltestdemo.BaseActivity;
import com.smartdevice.aidltestdemo.CaptureActivity;
import com.smartdevice.aidltestdemo.CustomerScreenActivity;
import com.smartdevice.aidltestdemo.FuncAdapter;
import com.smartdevice.aidltestdemo.IDCardActivity;
import com.smartdevice.aidltestdemo.ManeticCardActivity;
import com.smartdevice.aidltestdemo.NfcActivity;
import com.smartdevice.aidltestdemo.PSAMActivity;
import com.smartdevice.aidltestdemo.PSAMActivity2;
import com.smartdevice.aidltestdemo.PSAMActivityV2;
import com.smartdevice.aidltestdemo.PrinterActivity;
import com.smartdevice.aidltestdemo.ScannerActivity;
import com.smartdevice.aidltestdemo.util.SystemUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {
   public static String PRODUCT_NAME = "product_name";
   List data = new ArrayList();
   private ListView listView;
   FuncAdapter mFuncAdapter = null;
   MainActivity.ScreenOnOffReceiver mReceiver = null;
   private String product_name;
   private boolean runFlag = true;
   private boolean testFlag = false;
   private TextView tv_device_model;

   private void initData(int var1) {
      HashMap var4 = new HashMap();
      var4.put("id", "printer");
      var4.put("name", "打印机 printer");
      HashMap var8 = new HashMap();
      var8.put("id", "Scanner");
      var8.put("name", "扫描-扫描模块 Scanner");
      HashMap var7 = new HashMap();
      var7.put("id", "camerascanner");
      var7.put("name", "扫描-摄像头 camera scan");
      HashMap var9 = new HashMap();
      var9.put("id", "psam");
      var9.put("name", "接触式PSAM卡 PSAM Card");
      HashMap var3 = new HashMap();
      var3.put("id", "psamv2");
      var3.put("name", "接触式PSAM卡V2协议 PSAM Card V2");
      HashMap var11 = new HashMap();
      var11.put("id", "magneticcard");
      var11.put("name", "磁条卡(I2C) Magnetic card");
      HashMap var2 = new HashMap();
      var2.put("id", "cscreen");
      var2.put("name", "客显屏幕 Customer Screen");
      HashMap var6 = new HashMap();
      var6.put("id", "nfc");
      var6.put("name", "NFC测试 NFC Test");
      HashMap var5 = new HashMap();
      var5.put("id", "serialport");
      var5.put("name", "串口工具 Serialport tools");
      HashMap var10 = new HashMap();
      var10.put("id", "idcard");
      var10.put("name", "二代身份证模块 ID Card");
      switch(var1) {
      case 701:
         this.data.add(var4);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var11);
         this.data.add(var6);
         break;
      case 800:
         this.data.add(var4);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var11);
         this.data.add(var2);
         this.data.add(var6);
         this.data.add(var10);
         break;
      case 888:
         this.data.add(var4);
         this.data.add(var8);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var2);
         this.data.add(var6);
         this.data.add(var10);
         break;
      case 900:
      case 5501:
         this.data.add(var4);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var2);
         this.data.add(var6);
         break;
      case 3501:
      case 3503:
         this.data.add(var8);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var6);
         break;
      case 3502:
         this.data.add(var4);
         this.data.add(var8);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var11);
         this.data.add(var6);
         this.data.add(var10);
         break;
      case 3504:
         this.data.add(var4);
         this.data.add(var8);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var6);
         this.data.add(var10);
         break;
      case 3505:
      case 3506:
         this.data.add(var4);
         this.data.add(var8);
         this.data.add(var7);
         this.data.add(var9);
         this.data.add(var3);
         this.data.add(var6);
      }

      if(this.testFlag) {
         this.data.add(var5);
      }

   }

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         this.tv_device_model.setText(this.getString(2131558446) + DEVICE_MODEL);
         this.initData(DEVICE_MODEL);
         this.mFuncAdapter.notifyDataSetChanged();
      default:
      }
   }

   protected void onCreate(Bundle var1) {
      this.setContentView(2131361822);
      this.listView = (ListView)this.findViewById(2131230882);
      this.tv_device_model = (TextView)this.findViewById(2131230972);
      this.product_name = this.getIntent().getStringExtra(PRODUCT_NAME);
      this.tv_device_model.setText(2131558491);
      this.mFuncAdapter = new FuncAdapter(this, this.data);
      this.listView.setAdapter(this.mFuncAdapter);
      this.listView.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            Intent var6 = null;
            String var7 = (String)((HashMap)MainActivity.this.data.get(var3)).get("id");
            if("printer".equals(var7)) {
               var6 = new Intent(MainActivity.this, PrinterActivity.class);
               var6.putExtra(BaseActivity.MODULE_FLAG, 0);
            } else if("Scanner".equals(var7)) {
               var6 = new Intent(MainActivity.this, ScannerActivity.class);
               var6.putExtra(BaseActivity.MODULE_FLAG, 4);
            } else if("camerascanner".equals(var7)) {
               var6 = new Intent(MainActivity.this, CaptureActivity.class);
            } else if("psam".equals(var7)) {
               if(BaseActivity.DEVICE_MODEL != 3502 && BaseActivity.DEVICE_MODEL != 900) {
                  var6 = new Intent(MainActivity.this, PSAMActivity2.class);
               } else {
                  var6 = new Intent(MainActivity.this, PSAMActivity.class);
               }

               var6.putExtra(BaseActivity.MODULE_FLAG, 1);
            } else if("psamv2".equals(var7)) {
               var6 = new Intent(MainActivity.this, PSAMActivityV2.class);
               var6.putExtra(BaseActivity.MODULE_FLAG, 1);
            } else if("magneticcard".equals(var7)) {
               var6 = new Intent(MainActivity.this, ManeticCardActivity.class);
               var6.putExtra(BaseActivity.MODULE_FLAG, 2);
            } else if("cscreen".equals(var7)) {
               var6 = new Intent(MainActivity.this, CustomerScreenActivity.class);
               var6.putExtra(BaseActivity.MODULE_FLAG, 3);
            } else if("nfc".equals(var7)) {
               var6 = new Intent(MainActivity.this, NfcActivity.class);
            } else if("serialport".equals(var7)) {
               SystemUtil.startActivityForName(MainActivity.this, "com.zkc.serialdebugtools.SerialPortActivity");
            } else if("idcard".equals(var7)) {
               var6 = new Intent(MainActivity.this, IDCardActivity.class);
               var6.putExtra(BaseActivity.MODULE_FLAG, 5);
            }

            if(var6 != null) {
               MainActivity.this.startActivity(var6);
            }

         }
      });
      BaseActivity.module_flag = 8;
      super.onCreate(var1);
   }

   protected void onDestroy() {
      try {
         if(mIzkcService != null) {
            mIzkcService.setModuleFlag(9);
         }
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

      super.onDestroy();
   }

   protected void onResume() {
      super.onResume();
   }

   public class ScreenOnOffReceiver extends BroadcastReceiver {
      private static final String TAG = "ScreenOnOffReceiver";

      public void onReceive(Context var1, Intent var2) {
         if(var2.getAction().equals("android.intent.action.SCREEN_ON")) {
            ;
         }

      }
   }
}
