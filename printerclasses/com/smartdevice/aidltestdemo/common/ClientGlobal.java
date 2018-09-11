package com.smartdevice.aidltestdemo.common;

import android.os.Environment;
import java.io.File;

public class ClientGlobal {
   public static class Path {
      public static final String ClientDir;
      public static final String SDCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();

      static {
         ClientDir = SDCardDir + File.separator + "ZkcService";
      }
   }
}
