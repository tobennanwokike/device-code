package android.support.v4.animation;

import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v4.animation.AnimatorProvider;
import android.support.v4.animation.GingerbreadAnimatorCompatProvider;
import android.support.v4.animation.HoneycombMr1AnimatorCompatProvider;
import android.support.v4.animation.ValueAnimatorCompat;
import android.view.View;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public final class AnimatorCompatHelper {
   private static final AnimatorProvider IMPL;

   static {
      if(VERSION.SDK_INT >= 12) {
         IMPL = new HoneycombMr1AnimatorCompatProvider();
      } else {
         IMPL = new GingerbreadAnimatorCompatProvider();
      }

   }

   public static void clearInterpolator(View var0) {
      IMPL.clearInterpolator(var0);
   }

   public static ValueAnimatorCompat emptyValueAnimator() {
      return IMPL.emptyValueAnimator();
   }
}
