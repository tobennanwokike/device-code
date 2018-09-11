package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.util.Log;
import com.bumptech.glide.load.engine.executor.Prioritized;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FifoPriorityThreadPoolExecutor extends ThreadPoolExecutor {
   private static final String TAG = "PriorityExecutor";
   private final AtomicInteger ordering;
   private final FifoPriorityThreadPoolExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy;

   public FifoPriorityThreadPoolExecutor(int var1) {
      this(var1, FifoPriorityThreadPoolExecutor.UncaughtThrowableStrategy.LOG);
   }

   public FifoPriorityThreadPoolExecutor(int var1, int var2, long var3, TimeUnit var5, ThreadFactory var6, FifoPriorityThreadPoolExecutor.UncaughtThrowableStrategy var7) {
      super(var1, var2, var3, var5, new PriorityBlockingQueue(), var6);
      this.ordering = new AtomicInteger();
      this.uncaughtThrowableStrategy = var7;
   }

   public FifoPriorityThreadPoolExecutor(int var1, FifoPriorityThreadPoolExecutor.UncaughtThrowableStrategy var2) {
      this(var1, var1, 0L, TimeUnit.MILLISECONDS, new FifoPriorityThreadPoolExecutor.DefaultThreadFactory(), var2);
   }

   protected void afterExecute(Runnable var1, Throwable var2) {
      super.afterExecute(var1, var2);
      if(var2 == null && var1 instanceof Future) {
         Future var5 = (Future)var1;
         if(var5.isDone() && !var5.isCancelled()) {
            try {
               var5.get();
            } catch (InterruptedException var3) {
               this.uncaughtThrowableStrategy.handle(var3);
            } catch (ExecutionException var4) {
               this.uncaughtThrowableStrategy.handle(var4);
            }
         }
      }

   }

   protected RunnableFuture newTaskFor(Runnable var1, Object var2) {
      return new FifoPriorityThreadPoolExecutor.LoadTask(var1, var2, this.ordering.getAndIncrement());
   }

   public static class DefaultThreadFactory implements ThreadFactory {
      int threadNum = 0;

      public Thread newThread(final Runnable var1) {
         Thread var2 = new Thread(var1, "fifo-pool-thread-" + this.threadNum) {
            public void run() {
               Process.setThreadPriority(10);
               super.run();
            }
         };
         ++this.threadNum;
         return var2;
      }
   }

   static class LoadTask extends FutureTask implements Comparable {
      private final int order;
      private final int priority;

      public LoadTask(Runnable var1, Object var2, int var3) {
         super(var1, var2);
         if(!(var1 instanceof Prioritized)) {
            throw new IllegalArgumentException("FifoPriorityThreadPoolExecutor must be given Runnables that implement Prioritized");
         } else {
            this.priority = ((Prioritized)var1).getPriority();
            this.order = var3;
         }
      }

      public int compareTo(FifoPriorityThreadPoolExecutor.LoadTask var1) {
         int var3 = this.priority - var1.priority;
         int var2 = var3;
         if(var3 == 0) {
            var2 = this.order - var1.order;
         }

         return var2;
      }

      public boolean equals(Object var1) {
         boolean var3 = false;
         boolean var2 = var3;
         if(var1 instanceof FifoPriorityThreadPoolExecutor.LoadTask) {
            FifoPriorityThreadPoolExecutor.LoadTask var4 = (FifoPriorityThreadPoolExecutor.LoadTask)var1;
            var2 = var3;
            if(this.order == var4.order) {
               var2 = var3;
               if(this.priority == var4.priority) {
                  var2 = true;
               }
            }
         }

         return var2;
      }

      public int hashCode() {
         return this.priority * 31 + this.order;
      }
   }

   public static enum UncaughtThrowableStrategy {
      IGNORE,
      LOG {
         protected void handle(Throwable var1) {
            if(Log.isLoggable("PriorityExecutor", 6)) {
               Log.e("PriorityExecutor", "Request threw uncaught throwable", var1);
            }

         }
      },
      THROW {
         protected void handle(Throwable var1) {
            super.handle(var1);
            throw new RuntimeException(var1);
         }
      };

      private UncaughtThrowableStrategy() {
      }

      // $FF: synthetic method
      UncaughtThrowableStrategy(Object var3) {
         this();
      }

      protected void handle(Throwable var1) {
      }
   }
}
