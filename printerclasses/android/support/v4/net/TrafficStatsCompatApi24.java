package android.support.v4.net;

import android.net.TrafficStats;
import android.support.annotation.RestrictTo;
import java.net.DatagramSocket;
import java.net.SocketException;

@RestrictTo({RestrictTo.Scope.GROUP_ID})
public class TrafficStatsCompatApi24 {
   public static void tagDatagramSocket(DatagramSocket var0) throws SocketException {
      TrafficStats.tagDatagramSocket(var0);
   }

   public static void untagDatagramSocket(DatagramSocket var0) throws SocketException {
      TrafficStats.untagDatagramSocket(var0);
   }
}
