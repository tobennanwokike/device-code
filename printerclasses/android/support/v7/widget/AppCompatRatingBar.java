package android.support.v7.widget;

import android.content.Context;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatProgressBarHelper;
import android.util.AttributeSet;
import android.widget.RatingBar;

public class AppCompatRatingBar extends RatingBar {
   private AppCompatProgressBarHelper mAppCompatProgressBarHelper;

   public AppCompatRatingBar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatRatingBar(Context var1, AttributeSet var2) {
      this(var1, var2, R.attr.ratingBarStyle);
   }

   public AppCompatRatingBar(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mAppCompatProgressBarHelper = new AppCompatProgressBarHelper(this);
      this.mAppCompatProgressBarHelper.loadFromAttributes(var2, var3);
   }

   protected void onMeasure(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }
}
