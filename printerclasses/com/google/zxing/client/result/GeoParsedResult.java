package com.google.zxing.client.result;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

public final class GeoParsedResult extends ParsedResult {
   private final double altitude;
   private final double latitude;
   private final double longitude;
   private final String query;

   GeoParsedResult(double var1, double var3, double var5, String var7) {
      super(ParsedResultType.GEO);
      this.latitude = var1;
      this.longitude = var3;
      this.altitude = var5;
      this.query = var7;
   }

   public double getAltitude() {
      return this.altitude;
   }

   public String getDisplayResult() {
      StringBuffer var1 = new StringBuffer(20);
      var1.append(this.latitude);
      var1.append(", ");
      var1.append(this.longitude);
      if(this.altitude > 0.0D) {
         var1.append(", ");
         var1.append(this.altitude);
         var1.append('m');
      }

      if(this.query != null) {
         var1.append(" (");
         var1.append(this.query);
         var1.append(')');
      }

      return var1.toString();
   }

   public String getGeoURI() {
      StringBuffer var1 = new StringBuffer();
      var1.append("geo:");
      var1.append(this.latitude);
      var1.append(',');
      var1.append(this.longitude);
      if(this.altitude > 0.0D) {
         var1.append(',');
         var1.append(this.altitude);
      }

      if(this.query != null) {
         var1.append('?');
         var1.append(this.query);
      }

      return var1.toString();
   }

   public double getLatitude() {
      return this.latitude;
   }

   public double getLongitude() {
      return this.longitude;
   }

   public String getQuery() {
      return this.query;
   }
}
