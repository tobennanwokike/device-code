package android.support.v4.net;

import android.os.Build.VERSION;
import android.support.v4.net.TrafficStatsCompatApi24;
import android.support.v4.net.TrafficStatsCompatIcs;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public final class TrafficStatsCompat {
   private static final TrafficStatsCompat.TrafficStatsCompatImpl IMPL;

   static {
      if("N".equals(VERSION.CODENAME)) {
         IMPL = new TrafficStatsCompat.Api24TrafficStatsCompatImpl();
      } else if(VERSION.SDK_INT >= 14) {
         IMPL = new TrafficStatsCompat.IcsTrafficStatsCompatImpl();
      } else {
         IMPL = new TrafficStatsCompat.BaseTrafficStatsCompatImpl();
      }

   }

   public static void clearThreadStatsTag() {
      IMPL.clearThreadStatsTag();
   }

   public static int getThreadStatsTag() {
      return IMPL.getThreadStatsTag();
   }

   public static void incrementOperationCount(int var0) {
      IMPL.incrementOperationCount(var0);
   }

   public static void incrementOperationCount(int var0, int var1) {
      IMPL.incrementOperationCount(var0, var1);
   }

   public static void setThreadStatsTag(int var0) {
      IMPL.setThreadStatsTag(var0);
   }

   public static void tagDatagramSocket(DatagramSocket var0) throws SocketException {
      IMPL.tagDatagramSocket(var0);
   }

   public static void tagSocket(Socket var0) throws SocketException {
      IMPL.tagSocket(var0);
   }

   public static void untagDatagramSocket(DatagramSocket var0) throws SocketException {
      IMPL.untagDatagramSocket(var0);
   }

   public static void untagSocket(Socket var0) throws SocketException {
      IMPL.untagSocket(var0);
   }

   static class Api24TrafficStatsCompatImpl extends TrafficStatsCompat.IcsTrafficStatsCompatImpl {
      public void tagDatagramSocket(DatagramSocket var1) throws SocketException {
         TrafficStatsCompatApi24.tagDatagramSocket(var1);
      }

      public void untagDatagramSocket(DatagramSocket var1) throws SocketException {
         TrafficStatsCompatApi24.untagDatagramSocket(var1);
      }
   }

   static class BaseTrafficStatsCompatImpl implements TrafficStatsCompat.TrafficStatsCompatImpl {
      private ThreadLocal mThreadSocketTags = new ThreadLocal() {
         protected TrafficStatsCompat.SocketTags initialValue() {
            return new TrafficStatsCompat.SocketTags();
         }
      };

      public void clearThreadStatsTag() {
         ((TrafficStatsCompat.SocketTags)this.mThreadSocketTags.get()).statsTag = -1;
      }

      public int getThreadStatsTag() {
         return ((TrafficStatsCompat.SocketTags)this.mThreadSocketTags.get()).statsTag;
      }

      public void incrementOperationCount(int var1) {
      }

      public void incrementOperationCount(int var1, int var2) {
      }

      public void setThreadStatsTag(int var1) {
         ((TrafficStatsCompat.SocketTags)this.mThreadSocketTags.get()).statsTag = var1;
      }

      public void tagDatagramSocket(DatagramSocket var1) {
      }

      public void tagSocket(Socket var1) {
      }

      public void untagDatagramSocket(DatagramSocket var1) {
      }

      public void untagSocket(Socket var1) {
      }
   }

   private static class SocketTags {
      public int statsTag = -1;
   }

   static class IcsTrafficStatsCompatImpl implements TrafficStatsCompat.TrafficStatsCompatImpl {
      public void clearThreadStatsTag() {
         TrafficStatsCompatIcs.clearThreadStatsTag();
      }

      public int getThreadStatsTag() {
         return TrafficStatsCompatIcs.getThreadStatsTag();
      }

      public void incrementOperationCount(int var1) {
         TrafficStatsCompatIcs.incrementOperationCount(var1);
      }

      public void incrementOperationCount(int var1, int var2) {
         TrafficStatsCompatIcs.incrementOperationCount(var1, var2);
      }

      public void setThreadStatsTag(int var1) {
         TrafficStatsCompatIcs.setThreadStatsTag(var1);
      }

      public void tagDatagramSocket(DatagramSocket var1) throws SocketException {
         TrafficStatsCompatIcs.tagDatagramSocket(var1);
      }

      public void tagSocket(Socket var1) throws SocketException {
         TrafficStatsCompatIcs.tagSocket(var1);
      }

      public void untagDatagramSocket(DatagramSocket var1) throws SocketException {
         TrafficStatsCompatIcs.untagDatagramSocket(var1);
      }

      public void untagSocket(Socket var1) throws SocketException {
         TrafficStatsCompatIcs.untagSocket(var1);
      }
   }

   interface TrafficStatsCompatImpl {
      void clearThreadStatsTag();

      int getThreadStatsTag();

      void incrementOperationCount(int var1);

      void incrementOperationCount(int var1, int var2);

      void setThreadStatsTag(int var1);

      void tagDatagramSocket(DatagramSocket var1) throws SocketException;

      void tagSocket(Socket var1) throws SocketException;

      void untagDatagramSocket(DatagramSocket var1) throws SocketException;

      void untagSocket(Socket var1) throws SocketException;
   }
}
