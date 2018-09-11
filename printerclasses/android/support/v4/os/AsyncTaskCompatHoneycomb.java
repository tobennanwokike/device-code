package android.support.v4.os;

import android.os.AsyncTask;

class AsyncTaskCompatHoneycomb {
   static void executeParallel(AsyncTask var0, Object... var1) {
      var0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, var1);
   }
}
