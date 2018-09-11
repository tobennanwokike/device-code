package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFutureTarget implements FutureTarget, Runnable {
   private static final RequestFutureTarget.Waiter DEFAULT_WAITER = new RequestFutureTarget.Waiter();
   private final boolean assertBackgroundThread;
   private Exception exception;
   private boolean exceptionReceived;
   private final int height;
   private boolean isCancelled;
   private final Handler mainHandler;
   private Request request;
   private Object resource;
   private boolean resultReceived;
   private final RequestFutureTarget.Waiter waiter;
   private final int width;

   public RequestFutureTarget(Handler var1, int var2, int var3) {
      this(var1, var2, var3, true, DEFAULT_WAITER);
   }

   RequestFutureTarget(Handler var1, int var2, int var3, boolean var4, RequestFutureTarget.Waiter var5) {
      this.mainHandler = var1;
      this.width = var2;
      this.height = var3;
      this.assertBackgroundThread = var4;
      this.waiter = var5;
   }

   private Object doGet(Long param1) throws ExecutionException, InterruptedException, TimeoutException {
      // $FF: Couldn't be decompiled
   }

   public boolean cancel(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   public void clear() {
      this.mainHandler.post(this);
   }

   public Object get() throws InterruptedException, ExecutionException {
      try {
         Object var1 = this.doGet((Long)null);
         return var1;
      } catch (TimeoutException var2) {
         throw new AssertionError(var2);
      }
   }

   public Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      return this.doGet(Long.valueOf(var3.toMillis(var1)));
   }

   public Request getRequest() {
      return this.request;
   }

   public void getSize(SizeReadyCallback var1) {
      var1.onSizeReady(this.width, this.height);
   }

   public boolean isCancelled() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.isCancelled;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isDone() {
      synchronized(this){}
      boolean var4 = false;

      boolean var1;
      label45: {
         try {
            var4 = true;
            if(this.isCancelled) {
               var4 = false;
               break label45;
            }

            var1 = this.resultReceived;
            var4 = false;
         } finally {
            if(var4) {
               ;
            }
         }

         if(!var1) {
            var1 = false;
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public void onDestroy() {
   }

   public void onLoadCleared(Drawable var1) {
   }

   public void onLoadFailed(Exception var1, Drawable var2) {
      synchronized(this){}

      try {
         this.exceptionReceived = true;
         this.exception = var1;
         this.waiter.notifyAll(this);
      } finally {
         ;
      }

   }

   public void onLoadStarted(Drawable var1) {
   }

   public void onResourceReady(Object var1, GlideAnimation var2) {
      synchronized(this){}

      try {
         this.resultReceived = true;
         this.resource = var1;
         this.waiter.notifyAll(this);
      } finally {
         ;
      }

   }

   public void onStart() {
   }

   public void onStop() {
   }

   public void run() {
      if(this.request != null) {
         this.request.clear();
         this.cancel(false);
      }

   }

   public void setRequest(Request var1) {
      this.request = var1;
   }

   static class Waiter {
      public void notifyAll(Object var1) {
         var1.notifyAll();
      }

      public void waitForTimeout(Object var1, long var2) throws InterruptedException {
         var1.wait(var2);
      }
   }
}
