package android.support.v4.widget;

import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.ListViewCompat;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
   private final ListView mTarget;

   public ListViewAutoScrollHelper(ListView var1) {
      super(var1);
      this.mTarget = var1;
   }

   public boolean canTargetScrollHorizontally(int var1) {
      return false;
   }

   public boolean canTargetScrollVertically(int var1) {
      boolean var6 = false;
      ListView var7 = this.mTarget;
      int var3 = var7.getCount();
      boolean var5;
      if(var3 == 0) {
         var5 = var6;
      } else {
         int var4 = var7.getChildCount();
         int var2 = var7.getFirstVisiblePosition();
         if(var1 > 0) {
            if(var2 + var4 >= var3) {
               var5 = var6;
               if(var7.getChildAt(var4 - 1).getBottom() <= var7.getHeight()) {
                  return var5;
               }
            }
         } else {
            var5 = var6;
            if(var1 >= 0) {
               return var5;
            }

            if(var2 <= 0 && var7.getChildAt(0).getTop() >= 0) {
               var5 = var6;
               return var5;
            }
         }

         var5 = true;
      }

      return var5;
   }

   public void scrollTargetBy(int var1, int var2) {
      ListViewCompat.scrollListBy(this.mTarget, var2);
   }
}
