package com.smartdevice.aidltestdemo.camerascanner.view;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.smartdevice.aidltestdemo.camerascanner.view.ViewfinderView;

public final class ViewfinderResultPointCallback implements ResultPointCallback {
   private final ViewfinderView viewfinderView;

   public ViewfinderResultPointCallback(ViewfinderView var1) {
      this.viewfinderView = var1;
   }

   public void foundPossibleResultPoint(ResultPoint var1) {
      this.viewfinderView.addPossibleResultPoint(var1);
   }
}
