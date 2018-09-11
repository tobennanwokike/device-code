package com.smartdevice.aidltestdemo.scan;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;

public class ClientConfig {
   public static final String APPEND_RINGTONE = "append_ringtone";
   public static final String APPEND_VIBRATE = "append_vibate";
   public static final String CONTINUE_SCAN = "continue_scan";
   public static final String DATA_APPEND_ENTER = "data_append_enter";
   public static final String OPEN_SCAN = "open_scan";
   private static final String PREFERENCES_NAME = "com.zkc.smartsdk";
   public static final String RESET = "reset";
   public static final String SCAN_REPEAT = "scan_repeat";
   private static Map configs = new HashMap();
   private static Context mContext;
   private static SharedPreferences mSharedPreferences;

   public static void clearAccount() {
   }

   public static Boolean getBoolean(String var0) {
      return getBoolean(var0, false);
   }

   public static Boolean getBoolean(String param0, boolean param1) {
      // $FF: Couldn't be decompiled
   }

   public static Double getDouble(String var0) {
      return getDouble(var0, 0.0D);
   }

   public static Double getDouble(String param0, double param1) {
      // $FF: Couldn't be decompiled
   }

   public static float getFloat(String var0) {
      return getFloat(var0, 0.0F);
   }

   public static float getFloat(String param0, float param1) {
      // $FF: Couldn't be decompiled
   }

   public static int getInt(String var0) {
      return getInt(var0, 0);
   }

   public static int getInt(String param0, int param1) {
      // $FF: Couldn't be decompiled
   }

   public static long getLong(String var0) {
      return getLong(var0, 0L);
   }

   public static long getLong(String param0, long param1) {
      // $FF: Couldn't be decompiled
   }

   private static SharedPreferences getSharedPreferences() {
      // $FF: Couldn't be decompiled
   }

   public static String getString(String var0) {
      return getString(var0, "");
   }

   public static String getString(String param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean hasValue(String var0) {
      boolean var1;
      if(mSharedPreferences != null) {
         var1 = mSharedPreferences.contains(var0);
      } else {
         var1 = false;
      }

      return var1;
   }

   public static void init(Context param0) {
      // $FF: Couldn't be decompiled
   }

   private static void initDefaultValue() {
   }

   public static void setValue(String param0, Object param1) {
      // $FF: Couldn't be decompiled
   }
}
