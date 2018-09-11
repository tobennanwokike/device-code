package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompatHoneycombMR2 {
   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      boolean var2 = true;
      NetworkInfo var3 = var0.getActiveNetworkInfo();
      boolean var1;
      if(var3 == null) {
         var1 = var2;
      } else {
         var1 = var2;
         switch(var3.getType()) {
         case 0:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            break;
         case 1:
         case 7:
         case 9:
            var1 = false;
            break;
         case 8:
         default:
            var1 = var2;
         }
      }

      return var1;
   }
}
