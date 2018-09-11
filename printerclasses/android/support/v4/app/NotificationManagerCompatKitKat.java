package android.support.v4.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import java.lang.reflect.InvocationTargetException;

class NotificationManagerCompatKitKat {
   private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
   private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

   public static boolean areNotificationsEnabled(Context var0) {
      AppOpsManager var3 = (AppOpsManager)var0.getSystemService("appops");
      ApplicationInfo var4 = var0.getApplicationInfo();
      String var11 = var0.getApplicationContext().getPackageName();
      int var1 = var4.uid;

      boolean var2;
      label37: {
         try {
            Class var12 = Class.forName(AppOpsManager.class.getName());
            var1 = ((Integer)var12.getMethod("checkOpNoThrow", new Class[]{Integer.TYPE, Integer.TYPE, String.class}).invoke(var3, new Object[]{Integer.valueOf(((Integer)var12.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(var1), var11})).intValue();
            break label37;
         } catch (ClassNotFoundException var5) {
            ;
         } catch (NoSuchMethodException var6) {
            ;
         } catch (NoSuchFieldException var7) {
            ;
         } catch (InvocationTargetException var8) {
            ;
         } catch (IllegalAccessException var9) {
            ;
         } catch (RuntimeException var10) {
            ;
         }

         var2 = true;
         return var2;
      }

      if(var1 == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
