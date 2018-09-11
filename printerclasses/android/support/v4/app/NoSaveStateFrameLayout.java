package android.support.v4.app;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

class NoSaveStateFrameLayout extends FrameLayout {
   public NoSaveStateFrameLayout(Context var1) {
      super(var1);
   }

   static ViewGroup wrap(View var0) {
      NoSaveStateFrameLayout var2 = new NoSaveStateFrameLayout(var0.getContext());
      LayoutParams var1 = var0.getLayoutParams();
      if(var1 != null) {
         var2.setLayoutParams(var1);
      }

      var0.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
      var2.addView(var0);
      return var2;
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.dispatchThawSelfOnly(var1);
   }

   protected void dispatchSaveInstanceState(SparseArray var1) {
      this.dispatchFreezeSelfOnly(var1);
   }
}
