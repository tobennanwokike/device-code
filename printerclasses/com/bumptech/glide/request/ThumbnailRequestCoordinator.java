package com.bumptech.glide.request;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
   private RequestCoordinator coordinator;
   private Request full;
   private Request thumb;

   public ThumbnailRequestCoordinator() {
      this((RequestCoordinator)null);
   }

   public ThumbnailRequestCoordinator(RequestCoordinator var1) {
      this.coordinator = var1;
   }

   private boolean parentCanNotifyStatusChanged() {
      boolean var1;
      if(this.coordinator != null && !this.coordinator.canNotifyStatusChanged(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean parentCanSetImage() {
      boolean var1;
      if(this.coordinator != null && !this.coordinator.canSetImage(this)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean parentIsAnyResourceSet() {
      boolean var1;
      if(this.coordinator != null && this.coordinator.isAnyResourceSet()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void begin() {
      if(!this.thumb.isRunning()) {
         this.thumb.begin();
      }

      if(!this.full.isRunning()) {
         this.full.begin();
      }

   }

   public boolean canNotifyStatusChanged(Request var1) {
      boolean var2;
      if(this.parentCanNotifyStatusChanged() && var1.equals(this.full) && !this.isAnyResourceSet()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean canSetImage(Request var1) {
      boolean var2;
      if(!this.parentCanSetImage() || !var1.equals(this.full) && this.full.isResourceSet()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void clear() {
      this.thumb.clear();
      this.full.clear();
   }

   public boolean isAnyResourceSet() {
      boolean var1;
      if(!this.parentIsAnyResourceSet() && !this.isResourceSet()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isCancelled() {
      return this.full.isCancelled();
   }

   public boolean isComplete() {
      boolean var1;
      if(!this.full.isComplete() && !this.thumb.isComplete()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isFailed() {
      return this.full.isFailed();
   }

   public boolean isPaused() {
      return this.full.isPaused();
   }

   public boolean isResourceSet() {
      boolean var1;
      if(!this.full.isResourceSet() && !this.thumb.isResourceSet()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isRunning() {
      return this.full.isRunning();
   }

   public void onRequestSuccess(Request var1) {
      if(!var1.equals(this.thumb)) {
         if(this.coordinator != null) {
            this.coordinator.onRequestSuccess(this);
         }

         if(!this.thumb.isComplete()) {
            this.thumb.clear();
         }
      }

   }

   public void pause() {
      this.full.pause();
      this.thumb.pause();
   }

   public void recycle() {
      this.full.recycle();
      this.thumb.recycle();
   }

   public void setRequests(Request var1, Request var2) {
      this.full = var1;
      this.thumb = var2;
   }
}
