package com.smartdevice.aidltestdemo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorFactory {
   private static ExecutorService EXCUTOR_LOGIC_POOL = Executors.newFixedThreadPool(10);
   private static ExecutorService EXCUTOR_THREAD_POOL = Executors.newFixedThreadPool(10);

   public static void executeLogic(Runnable var0) {
      if(var0 != null) {
         EXCUTOR_LOGIC_POOL.execute(var0);
      }

   }

   public static void executeThread(Runnable var0) {
      if(var0 != null) {
         EXCUTOR_THREAD_POOL.execute(var0);
      }

   }
}
