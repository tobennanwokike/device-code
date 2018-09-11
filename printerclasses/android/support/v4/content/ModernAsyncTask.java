package android.support.v4.content;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask {
   private static final int CORE_POOL_SIZE = 5;
   private static final int KEEP_ALIVE = 1;
   private static final String LOG_TAG = "AsyncTask";
   private static final int MAXIMUM_POOL_SIZE = 128;
   private static final int MESSAGE_POST_PROGRESS = 2;
   private static final int MESSAGE_POST_RESULT = 1;
   public static final Executor THREAD_POOL_EXECUTOR;
   private static volatile Executor sDefaultExecutor;
   private static ModernAsyncTask.InternalHandler sHandler;
   private static final BlockingQueue sPoolWorkQueue = new LinkedBlockingQueue(10);
   private static final ThreadFactory sThreadFactory = new ThreadFactory() {
      private final AtomicInteger mCount = new AtomicInteger(1);

      public Thread newThread(Runnable var1) {
         return new Thread(var1, "ModernAsyncTask #" + this.mCount.getAndIncrement());
      }
   };
   private final AtomicBoolean mCancelled;
   private final FutureTask mFuture;
   private volatile ModernAsyncTask.Status mStatus;
   private final AtomicBoolean mTaskInvoked;
   private final ModernAsyncTask.WorkerRunnable mWorker;

   static {
      THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
      sDefaultExecutor = THREAD_POOL_EXECUTOR;
   }

   public ModernAsyncTask() {
      this.mStatus = ModernAsyncTask.Status.PENDING;
      this.mCancelled = new AtomicBoolean();
      this.mTaskInvoked = new AtomicBoolean();
      this.mWorker = new ModernAsyncTask.WorkerRunnable() {
         public Object call() throws Exception {
            // $FF: Couldn't be decompiled
         }
      };
      this.mFuture = new FutureTask(this.mWorker) {
         protected void done() {
            try {
               Object var1 = this.get();
               ModernAsyncTask.this.postResultIfNotInvoked(var1);
            } catch (InterruptedException var2) {
               Log.w("AsyncTask", var2);
            } catch (ExecutionException var3) {
               throw new RuntimeException("An error occurred while executing doInBackground()", var3.getCause());
            } catch (CancellationException var4) {
               ModernAsyncTask.this.postResultIfNotInvoked((Object)null);
            } catch (Throwable var5) {
               throw new RuntimeException("An error occurred while executing doInBackground()", var5);
            }

         }
      };
   }

   // $FF: synthetic method
   static AtomicBoolean access$000(ModernAsyncTask var0) {
      return var0.mTaskInvoked;
   }

   // $FF: synthetic method
   static AtomicBoolean access$100(ModernAsyncTask var0) {
      return var0.mCancelled;
   }

   public static void execute(Runnable var0) {
      sDefaultExecutor.execute(var0);
   }

   private static Handler getHandler() {
      // $FF: Couldn't be decompiled
   }

   @RestrictTo({RestrictTo.Scope.GROUP_ID})
   public static void setDefaultExecutor(Executor var0) {
      sDefaultExecutor = var0;
   }

   public final boolean cancel(boolean var1) {
      this.mCancelled.set(true);
      return this.mFuture.cancel(var1);
   }

   protected abstract Object doInBackground(Object... var1);

   public final ModernAsyncTask execute(Object... var1) {
      return this.executeOnExecutor(sDefaultExecutor, var1);
   }

   public final ModernAsyncTask executeOnExecutor(Executor var1, Object... var2) {
      if(this.mStatus != ModernAsyncTask.Status.PENDING) {
         switch(null.$SwitchMap$android$support$v4$content$ModernAsyncTask$Status[this.mStatus.ordinal()]) {
         case 1:
            throw new IllegalStateException("Cannot execute task: the task is already running.");
         case 2:
            throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
         }
      }

      this.mStatus = ModernAsyncTask.Status.RUNNING;
      this.onPreExecute();
      this.mWorker.mParams = var2;
      var1.execute(this.mFuture);
      return this;
   }

   void finish(Object var1) {
      if(this.isCancelled()) {
         this.onCancelled(var1);
      } else {
         this.onPostExecute(var1);
      }

      this.mStatus = ModernAsyncTask.Status.FINISHED;
   }

   public final Object get() throws InterruptedException, ExecutionException {
      return this.mFuture.get();
   }

   public final Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      return this.mFuture.get(var1, var3);
   }

   public final ModernAsyncTask.Status getStatus() {
      return this.mStatus;
   }

   public final boolean isCancelled() {
      return this.mCancelled.get();
   }

   protected void onCancelled() {
   }

   protected void onCancelled(Object var1) {
      this.onCancelled();
   }

   protected void onPostExecute(Object var1) {
   }

   protected void onPreExecute() {
   }

   protected void onProgressUpdate(Object... var1) {
   }

   Object postResult(Object var1) {
      getHandler().obtainMessage(1, new ModernAsyncTask.AsyncTaskResult(this, new Object[]{var1})).sendToTarget();
      return var1;
   }

   void postResultIfNotInvoked(Object var1) {
      if(!this.mTaskInvoked.get()) {
         this.postResult(var1);
      }

   }

   protected final void publishProgress(Object... var1) {
      if(!this.isCancelled()) {
         getHandler().obtainMessage(2, new ModernAsyncTask.AsyncTaskResult(this, var1)).sendToTarget();
      }

   }

   private static class AsyncTaskResult {
      final Object[] mData;
      final ModernAsyncTask mTask;

      AsyncTaskResult(ModernAsyncTask var1, Object... var2) {
         this.mTask = var1;
         this.mData = var2;
      }
   }

   private static class InternalHandler extends Handler {
      public InternalHandler() {
         super(Looper.getMainLooper());
      }

      public void handleMessage(Message var1) {
         ModernAsyncTask.AsyncTaskResult var2 = (ModernAsyncTask.AsyncTaskResult)var1.obj;
         switch(var1.what) {
         case 1:
            var2.mTask.finish(var2.mData[0]);
            break;
         case 2:
            var2.mTask.onProgressUpdate(var2.mData);
         }

      }
   }

   public static enum Status {
      FINISHED,
      PENDING,
      RUNNING;
   }

   private abstract static class WorkerRunnable implements Callable {
      Object[] mParams;
   }
}
