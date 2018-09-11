package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class DiskCacheWriteLocker {
   private final Map locks = new HashMap();
   private final DiskCacheWriteLocker.WriteLockPool writeLockPool = new DiskCacheWriteLocker.WriteLockPool();

   void acquire(Key param1) {
      // $FF: Couldn't be decompiled
   }

   void release(Key param1) {
      // $FF: Couldn't be decompiled
   }

   private static class WriteLock {
      int interestedThreads;
      final Lock lock;

      private WriteLock() {
         this.lock = new ReentrantLock();
      }

      // $FF: synthetic method
      WriteLock(Object var1) {
         this();
      }
   }

   private static class WriteLockPool {
      private static final int MAX_POOL_SIZE = 10;
      private final Queue pool;

      private WriteLockPool() {
         this.pool = new ArrayDeque();
      }

      // $FF: synthetic method
      WriteLockPool(Object var1) {
         this();
      }

      DiskCacheWriteLocker.WriteLock obtain() {
         // $FF: Couldn't be decompiled
      }

      void offer(DiskCacheWriteLocker.WriteLock param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
