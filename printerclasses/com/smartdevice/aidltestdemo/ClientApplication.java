package com.smartdevice.aidltestdemo;

import android.app.Application;
import com.smartdevice.aidltestdemo.common.ClientGlobal;
import com.smartdevice.aidltestdemo.scan.ClientConfig;
import com.smartdevice.aidltestdemo.util.FileUtil;

public class ClientApplication extends Application {
   private void initDefaultValue() {
      if(!ClientConfig.hasValue("open_scan")) {
         ClientConfig.setValue("open_scan", Boolean.valueOf(true));
      }

      if(!ClientConfig.hasValue("data_append_enter")) {
         ClientConfig.setValue("data_append_enter", Boolean.valueOf(true));
      }

      if(!ClientConfig.hasValue("append_ringtone")) {
         ClientConfig.setValue("append_ringtone", Boolean.valueOf(true));
      }

      if(!ClientConfig.hasValue("append_vibate")) {
         ClientConfig.setValue("append_vibate", Boolean.valueOf(true));
      }

      if(!ClientConfig.hasValue("continue_scan")) {
         ClientConfig.setValue("continue_scan", Boolean.valueOf(false));
      }

      if(!ClientConfig.hasValue("scan_repeat")) {
         ClientConfig.setValue("scan_repeat", Boolean.valueOf(false));
      }

      if(!ClientConfig.hasValue("scan_repeat")) {
         ClientConfig.setValue("scan_repeat", Boolean.valueOf(false));
      }

   }

   private void initSDcard() {
      FileUtil.createDirIfNotExist(ClientGlobal.Path.ClientDir);
   }

   public void onCreate() {
      super.onCreate();
      ClientConfig.init(this.getApplicationContext());
      this.initDefaultValue();
      this.initSDcard();
   }
}
