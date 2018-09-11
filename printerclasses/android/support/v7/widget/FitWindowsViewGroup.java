package android.support.v7.widget;

import android.graphics.Rect;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public interface FitWindowsViewGroup {
   void setOnFitSystemWindowsListener(FitWindowsViewGroup.OnFitSystemWindowsListener var1);

   public interface OnFitSystemWindowsListener {
      void onFitSystemWindows(Rect var1);
   }
}
