package android.support.v4.content;

import android.os.Build.VERSION;
import android.support.v4.content.ExecutorCompatHoneycomb;
import android.support.v4.content.ModernAsyncTask;
import java.util.concurrent.Executor;

public final class ParallelExecutorCompat {
   public static Executor getParallelExecutor() {
      Executor var0;
      if(VERSION.SDK_INT >= 11) {
         var0 = ExecutorCompatHoneycomb.getParallelExecutor();
      } else {
         var0 = ModernAsyncTask.THREAD_POOL_EXECUTOR;
      }

      return var0;
   }
}
