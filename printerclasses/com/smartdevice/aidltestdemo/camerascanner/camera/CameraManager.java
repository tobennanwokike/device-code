package com.smartdevice.aidltestdemo.camerascanner.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;
import com.smartdevice.aidltestdemo.camerascanner.camera.AutoFocusCallback;
import com.smartdevice.aidltestdemo.camerascanner.camera.CameraConfigurationManager;
import com.smartdevice.aidltestdemo.camerascanner.camera.FlashlightManager;
import com.smartdevice.aidltestdemo.camerascanner.camera.PlanarYUVLuminanceSource;
import com.smartdevice.aidltestdemo.camerascanner.camera.PreviewCallback;
import com.smartdevice.aidltestdemo.util.SystemUtil;
import java.io.IOException;

public final class CameraManager {
   private static final int MAX_FRAME_HEIGHT = 640;
   private static final int MAX_FRAME_WIDTH = 960;
   private static final int MIN_FRAME_HEIGHT = 240;
   private static final int MIN_FRAME_WIDTH = 240;
   static final int SDK_INT;
   private static final String TAG = CameraManager.class.getSimpleName();
   private static CameraManager cameraManager;
   private final AutoFocusCallback autoFocusCallback;
   private Camera camera;
   private final CameraConfigurationManager configManager;
   private final Context context;
   private Rect framingRect;
   private Rect framingRectInPreview;
   private boolean initialized;
   private final PreviewCallback previewCallback;
   private boolean previewing;
   private final boolean useOneShotPreviewCallback;

   static {
      int var0;
      try {
         var0 = Integer.parseInt(VERSION.SDK);
      } catch (NumberFormatException var2) {
         var0 = 10000;
      }

      SDK_INT = var0;
   }

   private CameraManager(Context var1) {
      this.context = var1;
      this.configManager = new CameraConfigurationManager(var1);
      boolean var2;
      if(Integer.parseInt(VERSION.SDK) > 3) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.useOneShotPreviewCallback = var2;
      this.previewCallback = new PreviewCallback(this.configManager, this.useOneShotPreviewCallback);
      this.autoFocusCallback = new AutoFocusCallback();
   }

   public static CameraManager get() {
      return cameraManager;
   }

   public static void init(Context var0) {
      if(cameraManager == null) {
         cameraManager = new CameraManager(var0);
      }

   }

   public PlanarYUVLuminanceSource buildLuminanceSource(byte[] var1, int var2, int var3) {
      Rect var5 = this.getFramingRectInPreview();
      int var4 = this.configManager.getPreviewFormat();
      String var6 = this.configManager.getPreviewFormatString();
      PlanarYUVLuminanceSource var7;
      switch(var4) {
      case 16:
      case 17:
         var7 = new PlanarYUVLuminanceSource(var1, var2, var3, var5.left, var5.top, var5.width(), var5.height());
         break;
      default:
         if(!"yuv420p".equals(var6)) {
            throw new IllegalArgumentException("Unsupported picture format: " + var4 + '/' + var6);
         }

         var7 = new PlanarYUVLuminanceSource(var1, var2, var3, var5.left, var5.top, var5.width(), var5.height());
      }

      return var7;
   }

   public void closeDriver() {
      if(this.camera != null) {
         FlashlightManager.disableFlashlight();
         this.camera.release();
         this.camera = null;
      }

   }

   public Rect getFramingRect() {
      Point var5 = this.configManager.getScreenResolution();
      Rect var6;
      if(this.framingRect == null) {
         if(this.camera == null) {
            var6 = null;
            return var6;
         }

         int var2 = var5.x * 15 / 16;
         int var1;
         if(var2 < 240) {
            var1 = 240;
         } else {
            var1 = var2;
            if(var2 > 960) {
               var1 = 960;
            }
         }

         int var3 = var5.y * 15 / 16;
         if(var3 < 240) {
            var2 = 240;
         } else {
            var2 = var3;
            if(var3 > 640) {
               var2 = 640;
            }
         }

         var3 = (var5.x - var1) / 2;
         int var4 = (var5.y - var2) / 2;
         this.framingRect = new Rect(var3, var4, var3 + var1, var4 + var2);
         Log.d(TAG, "Calculated framing rect: " + this.framingRect);
      }

      var6 = this.framingRect;
      return var6;
   }

   public Rect getFramingRectInPreview() {
      if(this.framingRectInPreview == null) {
         Rect var3 = new Rect(this.getFramingRect());
         Point var4 = this.configManager.getCameraResolution();
         Point var2 = this.configManager.getScreenResolution();
         int var1 = SystemUtil.getScreenOrientent(this.context);
         if(var1 != 1 && var1 != 3) {
            var3.left = var3.left * var4.x / var2.x;
            var3.right = var3.right * var4.x / var2.x;
            var3.top = var3.top * var4.y / var2.y;
            var3.bottom = var3.bottom * var4.y / var2.y;
         } else {
            var3.left = var3.left * var4.y / var2.x;
            var3.right = var3.right * var4.y / var2.x;
            var3.top = var3.top * var4.x / var2.y;
            var3.bottom = var3.bottom * var4.x / var2.y;
         }

         this.framingRectInPreview = var3;
      }

      return this.framingRectInPreview;
   }

   public void openDriver(SurfaceHolder var1) throws IOException {
      if(this.camera == null) {
         this.camera = Camera.open();
         if(this.camera == null) {
            Toast.makeText(this.context, this.context.getString(2131558459), 0).show();
         } else {
            this.camera.setPreviewDisplay(var1);
            if(!this.initialized) {
               this.initialized = true;
               int var2 = SystemUtil.getScreenOrientent(this.context);
               if(var2 != 0 && var2 != 2) {
                  if(var2 == 1 || var2 == 3) {
                     this.configManager.initFromCameraParametersV(this.camera);
                  }
               } else {
                  this.configManager.initFromCameraParametersH(this.camera);
               }
            }

            this.configManager.setDesiredCameraParameters(this.camera);
            FlashlightManager.enableFlashlight();
         }
      }

   }

   public void requestAutoFocus(Handler var1, int var2) {
      if(this.camera != null && this.previewing) {
         this.autoFocusCallback.setHandler(var1, var2);
         this.camera.autoFocus(this.autoFocusCallback);
      }

   }

   public void requestPreviewFrame(Handler var1, int var2) {
      if(this.camera != null && this.previewing) {
         this.previewCallback.setHandler(var1, var2);
         if(this.useOneShotPreviewCallback) {
            this.camera.setOneShotPreviewCallback(this.previewCallback);
         } else {
            this.camera.setPreviewCallback(this.previewCallback);
         }
      }

   }

   public void startPreview() {
      if(this.camera != null && !this.previewing) {
         this.camera.startPreview();
         this.previewing = true;
      }

   }

   public void stopPreview() {
      if(this.camera != null && this.previewing) {
         if(!this.useOneShotPreviewCallback) {
            this.camera.setPreviewCallback((android.hardware.Camera.PreviewCallback)null);
         }

         this.camera.stopPreview();
         this.previewCallback.setHandler((Handler)null, 0);
         this.autoFocusCallback.setHandler((Handler)null, 0);
         this.previewing = false;
      }

   }
}
