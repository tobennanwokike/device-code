package com.bumptech.glide.manager;

import android.content.Context;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.DefaultConnectivityMonitor;
import com.bumptech.glide.manager.NullConnectivityMonitor;

public class ConnectivityMonitorFactory {
   public ConnectivityMonitor build(Context var1, ConnectivityMonitor.ConnectivityListener var2) {
      boolean var3;
      if(var1.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Object var4;
      if(var3) {
         var4 = new DefaultConnectivityMonitor(var1, var2);
      } else {
         var4 = new NullConnectivityMonitor();
      }

      return (ConnectivityMonitor)var4;
   }
}
