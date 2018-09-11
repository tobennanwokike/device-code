package com.bumptech.glide.manager;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RequestTracker {
   private boolean isPaused;
   private final List pendingRequests = new ArrayList();
   private final Set requests = Collections.newSetFromMap(new WeakHashMap());

   void addRequest(Request var1) {
      this.requests.add(var1);
   }

   public void clearRequests() {
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         ((Request)var1.next()).clear();
      }

      this.pendingRequests.clear();
   }

   public boolean isPaused() {
      return this.isPaused;
   }

   public void pauseRequests() {
      this.isPaused = true;
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if(var2.isRunning()) {
            var2.pause();
            this.pendingRequests.add(var2);
         }
      }

   }

   public void removeRequest(Request var1) {
      this.requests.remove(var1);
      this.pendingRequests.remove(var1);
   }

   public void restartRequests() {
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if(!var2.isComplete() && !var2.isCancelled()) {
            var2.pause();
            if(!this.isPaused) {
               var2.begin();
            } else {
               this.pendingRequests.add(var2);
            }
         }
      }

   }

   public void resumeRequests() {
      this.isPaused = false;
      Iterator var2 = Util.getSnapshot(this.requests).iterator();

      while(var2.hasNext()) {
         Request var1 = (Request)var2.next();
         if(!var1.isComplete() && !var1.isCancelled() && !var1.isRunning()) {
            var1.begin();
         }
      }

      this.pendingRequests.clear();
   }

   public void runRequest(Request var1) {
      this.requests.add(var1);
      if(!this.isPaused) {
         var1.begin();
      } else {
         this.pendingRequests.add(var1);
      }

   }
}
