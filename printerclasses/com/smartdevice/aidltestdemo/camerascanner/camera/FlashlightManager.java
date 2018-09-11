package com.smartdevice.aidltestdemo.camerascanner.camera;

import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class FlashlightManager {
   private static final String TAG = FlashlightManager.class.getSimpleName();
   private static final Object iHardwareService = getHardwareService();
   private static final Method setFlashEnabledMethod;

   static {
      setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService);
      if(iHardwareService == null) {
         Log.v(TAG, "This device does supports control of a flashlight");
      } else {
         Log.v(TAG, "This device does not support control of a flashlight");
      }

   }

   static void disableFlashlight() {
      setFlashlight(false);
   }

   static void enableFlashlight() {
      setFlashlight(true);
   }

   private static Object getHardwareService() {
      Object var1 = null;
      Class var0 = maybeForName("android.os.ServiceManager");
      Object var4;
      if(var0 == null) {
         var4 = var1;
      } else {
         Method var2 = maybeGetMethod(var0, "getService", new Class[]{String.class});
         var4 = var1;
         if(var2 != null) {
            Object var5 = invoke(var2, (Object)null, new Object[]{"hardware"});
            var4 = var1;
            if(var5 != null) {
               Class var3 = maybeForName("android.os.IHardwareService$Stub");
               var4 = var1;
               if(var3 != null) {
                  Method var6 = maybeGetMethod(var3, "asInterface", new Class[]{IBinder.class});
                  var4 = var1;
                  if(var6 != null) {
                     var4 = invoke(var6, (Object)null, new Object[]{var5});
                  }
               }
            }
         }
      }

      return var4;
   }

   private static Method getSetFlashEnabledMethod(Object var0) {
      Method var1;
      if(var0 == null) {
         var1 = null;
      } else {
         var1 = maybeGetMethod(var0.getClass(), "setFlashlightEnabled", new Class[]{Boolean.TYPE});
      }

      return var1;
   }

   private static Object invoke(Method var0, Object var1, Object... var2) {
      Object var3 = null;

      Object var7;
      try {
         var1 = var0.invoke(var1, var2);
      } catch (IllegalAccessException var4) {
         Log.w(TAG, "Unexpected error while invoking " + var0, var4);
         var7 = var3;
         return var7;
      } catch (InvocationTargetException var5) {
         Log.w(TAG, "Unexpected error while invoking " + var0, var5.getCause());
         var7 = var3;
         return var7;
      } catch (RuntimeException var6) {
         Log.w(TAG, "Unexpected error while invoking " + var0, var6);
         var7 = var3;
         return var7;
      }

      var7 = var1;
      return var7;
   }

   private static Class maybeForName(String var0) {
      Object var1 = null;

      Class var2;
      Class var5;
      try {
         var2 = Class.forName(var0);
      } catch (ClassNotFoundException var3) {
         var5 = (Class)var1;
         return var5;
      } catch (RuntimeException var4) {
         Log.w(TAG, "Unexpected error while finding class " + var0, var4);
         var5 = (Class)var1;
         return var5;
      }

      var5 = var2;
      return var5;
   }

   private static Method maybeGetMethod(Class var0, String var1, Class... var2) {
      Object var3 = null;

      Method var6;
      try {
         var6 = var0.getMethod(var1, var2);
      } catch (NoSuchMethodException var4) {
         var6 = (Method)var3;
      } catch (RuntimeException var5) {
         Log.w(TAG, "Unexpected error while finding method " + var1, var5);
         var6 = (Method)var3;
      }

      return var6;
   }

   private static void setFlashlight(boolean var0) {
      if(iHardwareService != null) {
         invoke(setFlashEnabledMethod, iHardwareService, new Object[]{Boolean.valueOf(var0)});
      }

   }
}
