package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class WifiParsedResult extends ParsedResult {
   private final String networkEncryption;
   private final String password;
   private final String ssid;

   public WifiParsedResult(String var1, String var2, String var3) {
      super(ParsedResultType.WIFI);
      this.ssid = var2;
      this.networkEncryption = var1;
      this.password = var3;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(80);
      maybeAppend(this.ssid, var1);
      maybeAppend(this.networkEncryption, var1);
      maybeAppend(this.password, var1);
      return var1.toString();
   }

   public String getNetworkEncryption() {
      return this.networkEncryption;
   }

   public String getPassword() {
      return this.password;
   }

   public String getSsid() {
      return this.ssid;
   }
}
