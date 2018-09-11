package com.bumptech.glide.manager;

import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;

class ApplicationLifecycle implements Lifecycle {
   public void addListener(LifecycleListener var1) {
      var1.onStart();
   }
}
