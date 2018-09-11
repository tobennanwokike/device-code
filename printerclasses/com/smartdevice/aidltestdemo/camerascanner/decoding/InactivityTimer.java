package com.smartdevice.aidltestdemo.camerascanner.decoding;

import android.app.Activity;
import com.smartdevice.aidltestdemo.camerascanner.decoding.FinishListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class InactivityTimer {
   private static final int INACTIVITY_DELAY_SECONDS = 300;
   private final Activity activity;
   private ScheduledFuture inactivityFuture = null;
   private final ScheduledExecutorService inactivityTimer = Executors.newSingleThreadScheduledExecutor(new InactivityTimer.DaemonThreadFactory());

   public InactivityTimer(Activity var1) {
      this.activity = var1;
      this.onActivity();
   }

   private void cancel() {
      if(this.inactivityFuture != null) {
         this.inactivityFuture.cancel(true);
         this.inactivityFuture = null;
      }

   }

   public void onActivity() {
      this.cancel();
      this.inactivityFuture = this.inactivityTimer.schedule(new FinishListener(this.activity), 300L, TimeUnit.SECONDS);
   }

   public void shutdown() {
      this.cancel();
      this.inactivityTimer.shutdown();
   }

   private static final class DaemonThreadFactory implements ThreadFactory {
      private DaemonThreadFactory() {
      }

      // $FF: synthetic method
      DaemonThreadFactory(Object var1) {
         this();
      }

      public Thread newThread(Runnable var1) {
         Thread var2 = new Thread(var1);
         var2.setDaemon(true);
         return var2;
      }
   }
}
