package android.support.v7.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.TwilightCalculator;
import android.util.Log;
import java.util.Calendar;

class TwilightManager {
   private static final int SUNRISE = 6;
   private static final int SUNSET = 22;
   private static final String TAG = "TwilightManager";
   private static TwilightManager sInstance;
   private final Context mContext;
   private final LocationManager mLocationManager;
   private final TwilightManager.TwilightState mTwilightState = new TwilightManager.TwilightState();

   @VisibleForTesting
   TwilightManager(@NonNull Context var1, @NonNull LocationManager var2) {
      this.mContext = var1;
      this.mLocationManager = var2;
   }

   static TwilightManager getInstance(@NonNull Context var0) {
      if(sInstance == null) {
         var0 = var0.getApplicationContext();
         sInstance = new TwilightManager(var0, (LocationManager)var0.getSystemService("location"));
      }

      return sInstance;
   }

   private Location getLastKnownLocation() {
      Location var1 = null;
      Location var3 = null;
      if(PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
         var1 = this.getLastKnownLocationForProvider("network");
      }

      if(PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
         var3 = this.getLastKnownLocationForProvider("gps");
      }

      Location var2;
      if(var3 != null && var1 != null) {
         if(var3.getTime() > var1.getTime()) {
            var2 = var3;
         } else {
            var2 = var1;
         }
      } else {
         var2 = var3;
         if(var3 == null) {
            var2 = var1;
         }
      }

      return var2;
   }

   private Location getLastKnownLocationForProvider(String var1) {
      Location var3;
      if(this.mLocationManager != null) {
         try {
            if(this.mLocationManager.isProviderEnabled(var1)) {
               var3 = this.mLocationManager.getLastKnownLocation(var1);
               return var3;
            }
         } catch (Exception var2) {
            Log.d("TwilightManager", "Failed to get last known location", var2);
         }
      }

      var3 = null;
      return var3;
   }

   private boolean isStateValid() {
      boolean var1;
      if(this.mTwilightState != null && this.mTwilightState.nextUpdate > System.currentTimeMillis()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @VisibleForTesting
   static void setInstance(TwilightManager var0) {
      sInstance = var0;
   }

   private void updateState(@NonNull Location var1) {
      TwilightManager.TwilightState var13 = this.mTwilightState;
      long var2 = System.currentTimeMillis();
      TwilightCalculator var14 = TwilightCalculator.getInstance();
      var14.calculateTwilight(var2 - 86400000L, var1.getLatitude(), var1.getLongitude());
      long var4 = var14.sunset;
      var14.calculateTwilight(var2, var1.getLatitude(), var1.getLongitude());
      boolean var12;
      if(var14.state == 1) {
         var12 = true;
      } else {
         var12 = false;
      }

      long var10 = var14.sunrise;
      long var8 = var14.sunset;
      var14.calculateTwilight(86400000L + var2, var1.getLatitude(), var1.getLongitude());
      long var6 = var14.sunrise;
      if(var10 != -1L && var8 != -1L) {
         if(var2 > var8) {
            var2 = 0L + var6;
         } else if(var2 > var10) {
            var2 = 0L + var8;
         } else {
            var2 = 0L + var10;
         }

         var2 += 60000L;
      } else {
         var2 += 43200000L;
      }

      var13.isNight = var12;
      var13.yesterdaySunset = var4;
      var13.todaySunrise = var10;
      var13.todaySunset = var8;
      var13.tomorrowSunrise = var6;
      var13.nextUpdate = var2;
   }

   boolean isNight() {
      TwilightManager.TwilightState var3 = this.mTwilightState;
      boolean var2;
      if(this.isStateValid()) {
         var2 = var3.isNight;
      } else {
         Location var4 = this.getLastKnownLocation();
         if(var4 != null) {
            this.updateState(var4);
            var2 = var3.isNight;
         } else {
            Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
            int var1 = Calendar.getInstance().get(11);
            if(var1 >= 6 && var1 < 22) {
               var2 = false;
            } else {
               var2 = true;
            }
         }
      }

      return var2;
   }

   private static class TwilightState {
      boolean isNight;
      long nextUpdate;
      long todaySunrise;
      long todaySunset;
      long tomorrowSunrise;
      long yesterdaySunset;
   }
}
