package android.support.v4.print;

import android.content.Context;
import android.support.v4.print.PrintHelperApi23;

class PrintHelperApi24 extends PrintHelperApi23 {
   PrintHelperApi24(Context var1) {
      super(var1);
      this.mIsMinMarginsHandlingCorrect = true;
      this.mPrintActivityRespectsOrientation = true;
   }
}
