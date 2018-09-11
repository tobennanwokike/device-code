package com.smartdevice.aidltestdemo.camerascanner.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.smartdevice.aidltestdemo.camerascanner.camera.CameraManager;
import com.smartdevice.aidltestdemo.util.SystemUtil;
import java.util.regex.Pattern;

final class CameraConfigurationManager {
   private static final Pattern COMMA_PATTERN = Pattern.compile(",");
   private static final int DESIRED_SHARPNESS = 30;
   private static final String TAG = CameraConfigurationManager.class.getSimpleName();
   private static final int TEN_DESIRED_ZOOM = 27;
   private Point cameraResolution;
   private final Context context;
   private int previewFormat;
   private String previewFormatString;
   private Point screenResolution;

   CameraConfigurationManager(Context var1) {
      this.context = var1;
   }

   private static int findBestMotZoomValue(CharSequence var0, int var1) {
      int var4 = 0;
      String[] var9 = COMMA_PATTERN.split(var0);
      int var8 = var9.length;
      int var5 = 0;

      while(true) {
         if(var5 >= var8) {
            var1 = var4;
            break;
         }

         String var11 = var9[var5].trim();

         double var2;
         try {
            var2 = Double.parseDouble(var11);
         } catch (NumberFormatException var10) {
            break;
         }

         int var7 = (int)(10.0D * var2);
         int var6 = var4;
         if(Math.abs((double)var1 - var2) < (double)Math.abs(var1 - var4)) {
            var6 = var7;
         }

         ++var5;
         var4 = var6;
      }

      return var1;
   }

   private static Point findBestPreviewSizeValue(CharSequence var0, Point var1) {
      int var3 = 0;
      int var2 = 0;
      int var5 = Integer.MAX_VALUE;
      String[] var12 = COMMA_PATTERN.split(var0);
      int var10 = var12.length;
      int var4 = 0;

      int var6;
      int var7;
      while(true) {
         var7 = var3;
         var6 = var2;
         if(var4 >= var10) {
            break;
         }

         String var11 = var12[var4].trim();
         var6 = var11.indexOf(120);
         if(var6 < 0) {
            Log.w(TAG, "Bad preview-size: " + var11);
            var7 = var5;
         } else {
            label43: {
               int var8;
               try {
                  var8 = Integer.parseInt(var11.substring(0, var6));
                  var6 = Integer.parseInt(var11.substring(var6 + 1));
               } catch (NumberFormatException var13) {
                  Log.w(TAG, "Bad preview-size: " + var11);
                  var7 = var5;
                  break label43;
               }

               int var9 = Math.abs(var8 - var1.x) + Math.abs(var6 - var1.y);
               if(var9 == 0) {
                  var7 = var8;
                  break;
               }

               var7 = var5;
               if(var9 < var5) {
                  var3 = var8;
                  var2 = var6;
                  var7 = var9;
               }
            }
         }

         ++var4;
         var5 = var7;
      }

      Point var14;
      if(var7 > 0 && var6 > 0) {
         var14 = new Point(var7, var6);
      } else {
         var14 = null;
      }

      return var14;
   }

   private static Point getCameraResolution(Parameters var0, Point var1) {
      String var3 = var0.get("preview-size-values");
      String var2 = var3;
      if(var3 == null) {
         var2 = var0.get("preview-size-value");
      }

      Point var4 = null;
      if(var2 != null) {
         Log.d(TAG, "preview-size-values parameter: " + var2);
         var4 = findBestPreviewSizeValue(var2, var1);
      }

      Point var5 = var4;
      if(var4 == null) {
         var5 = new Point(var1.x >> 3 << 3, var1.y >> 3 << 3);
      }

      return var5;
   }

   private void setFlash(Parameters var1) {
      if(Build.MODEL.contains("Behold II") && CameraManager.SDK_INT == 3) {
         var1.set("flash-value", 1);
      } else {
         var1.set("flash-value", 2);
      }

      var1.set("flash-mode", "off");
   }

   private void setZoom(Parameters var1) {
      String var7 = var1.get("zoom-supported");
      if(var7 == null || Boolean.parseBoolean(var7)) {
         byte var6 = 27;
         var7 = var1.get("max-zoom");
         int var4 = var6;
         int var5;
         if(var7 != null) {
            label71: {
               double var2;
               try {
                  var2 = Double.parseDouble(var7);
               } catch (NumberFormatException var13) {
                  Log.w(TAG, "Bad max-zoom: " + var7);
                  var4 = var6;
                  break label71;
               }

               var5 = (int)(10.0D * var2);
               var4 = var6;
               if(27 > var5) {
                  var4 = var5;
               }
            }
         }

         String var8 = var1.get("taking-picture-zoom-max");
         var5 = var4;
         int var14;
         if(var8 != null) {
            label72: {
               try {
                  var14 = Integer.parseInt(var8);
               } catch (NumberFormatException var12) {
                  Log.w(TAG, "Bad taking-picture-zoom-max: " + var8);
                  var5 = var4;
                  break label72;
               }

               var5 = var4;
               if(var4 > var14) {
                  var5 = var14;
               }
            }
         }

         String var9 = var1.get("mot-zoom-values");
         var4 = var5;
         if(var9 != null) {
            var4 = findBestMotZoomValue(var9, var5);
         }

         String var10 = var1.get("mot-zoom-step");
         var5 = var4;
         if(var10 != null) {
            label73: {
               try {
                  var14 = (int)(10.0D * Double.parseDouble(var10.trim()));
               } catch (NumberFormatException var11) {
                  var5 = var4;
                  break label73;
               }

               var5 = var4;
               if(var14 > 1) {
                  var5 = var4 - var4 % var14;
               }
            }
         }

         if(var7 != null || var9 != null) {
            var1.set("zoom", String.valueOf((double)var5 / 10.0D));
         }

         if(var8 != null) {
            var1.set("taking-picture-zoom", var5);
         }
      }

   }

   Point getCameraResolution() {
      return this.cameraResolution;
   }

   int getPreviewFormat() {
      return this.previewFormat;
   }

   String getPreviewFormatString() {
      return this.previewFormatString;
   }

   Point getScreenResolution() {
      return this.screenResolution;
   }

   void initFromCameraParametersH(Camera var1) {
      Parameters var2 = var1.getParameters();
      this.previewFormat = var2.getPreviewFormat();
      this.previewFormatString = var2.get("preview-format");
      Log.d(TAG, "Default preview format: " + this.previewFormat + '/' + this.previewFormatString);
      Display var3 = ((WindowManager)this.context.getSystemService("window")).getDefaultDisplay();
      this.screenResolution = new Point(var3.getWidth(), var3.getHeight());
      Log.d(TAG, "Screen resolution: " + this.screenResolution);
      this.cameraResolution = getCameraResolution(var2, this.screenResolution);
      Log.d(TAG, "Camera resolution: " + this.screenResolution);
   }

   void initFromCameraParametersV(Camera var1) {
      Parameters var3 = var1.getParameters();
      this.previewFormat = var3.getPreviewFormat();
      this.previewFormatString = var3.get("preview-format");
      Log.d(TAG, "Default preview format: " + this.previewFormat + '/' + this.previewFormatString);
      Display var2 = ((WindowManager)this.context.getSystemService("window")).getDefaultDisplay();
      this.screenResolution = new Point(var2.getWidth(), var2.getHeight());
      Log.d(TAG, "Screen resolution: " + this.screenResolution);
      Point var4 = new Point();
      var4.x = this.screenResolution.x;
      var4.y = this.screenResolution.y;
      if(this.screenResolution.x < this.screenResolution.y) {
         var4.x = this.screenResolution.y;
         var4.y = this.screenResolution.x;
      }

      this.cameraResolution = getCameraResolution(var3, var4);
      this.cameraResolution = getCameraResolution(var3, this.screenResolution);
      Log.d(TAG, "Camera resolution: " + this.screenResolution);
   }

   void setDesiredCameraParameters(Camera var1) {
      Parameters var3 = var1.getParameters();
      Log.d(TAG, "Setting preview size: " + this.cameraResolution);
      var3.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
      this.setFlash(var3);
      int var2 = SystemUtil.getScreenOrientent(this.context);
      if(var2 == 1 || var2 == 3) {
         var1.setDisplayOrientation(90);
      }

      var1.setParameters(var3);
   }
}
