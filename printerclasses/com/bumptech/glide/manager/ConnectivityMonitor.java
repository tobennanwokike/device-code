package com.bumptech.glide.manager;

import com.bumptech.glide.manager.LifecycleListener;

public interface ConnectivityMonitor extends LifecycleListener {
   public interface ConnectivityListener {
      void onConnectivityChanged(boolean var1);
   }
}
