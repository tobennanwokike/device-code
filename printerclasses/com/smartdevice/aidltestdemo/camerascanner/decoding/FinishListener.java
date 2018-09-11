package com.smartdevice.aidltestdemo.camerascanner.decoding;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public final class FinishListener implements OnClickListener, OnCancelListener, Runnable {
   private final Activity activityToFinish;

   public FinishListener(Activity var1) {
      this.activityToFinish = var1;
   }

   public void onCancel(DialogInterface var1) {
      this.run();
   }

   public void onClick(DialogInterface var1, int var2) {
      this.run();
   }

   public void run() {
      this.activityToFinish.finish();
   }
}
