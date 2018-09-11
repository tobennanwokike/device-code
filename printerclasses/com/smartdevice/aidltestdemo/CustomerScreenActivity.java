package com.smartdevice.aidltestdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.smartdevice.aidltestdemo.BaseActivity;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerScreenActivity extends BaseActivity implements OnClickListener {
   CustomerScreenActivity.ThreadAuto _thAuto;
   private Button btn_dot;
   private Button btn_rgb565;
   private Button btn_rgb565_location;
   private Button btn_updatelogo;
   private CheckBox cb_auto;
   int[] colorArray = new int[]{-16776961, -16711936, -16776961, -256, -1};
   int colorIndex = 0;
   private ImageView iv_diaplay;

   private Bitmap copressTo100k(Bitmap param1) {
      // $FF: Couldn't be decompiled
   }

   private void enableWiget(boolean var1) {
      this.btn_dot.setEnabled(var1);
      this.btn_rgb565.setEnabled(var1);
      this.btn_rgb565_location.setEnabled(var1);
      this.btn_updatelogo.setEnabled(var1);
      this.cb_auto.setEnabled(false);
   }

   private Bitmap getBitmap() {
      Bitmap var4 = Bitmap.createBitmap(480, 272, Config.RGB_565);
      Canvas var1 = new Canvas(var4);
      Paint var3 = new Paint();
      var3.setColor(-16777216);
      var3.setTextSize(80.0F);
      String var2 = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
      var1.drawColor(-1);
      var1.drawText(var2, 10.0F, 100.0F, var3);
      return var4;
   }

   private void initEvent() {
      this.btn_dot.setOnClickListener(this);
      this.btn_rgb565.setOnClickListener(this);
      this.btn_rgb565_location.setOnClickListener(this);
      this.btn_updatelogo.setOnClickListener(this);
      this.cb_auto.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton var1, boolean var2) {
            if(var2) {
               CustomerScreenActivity.this._thAuto = CustomerScreenActivity.this.new ThreadAuto();
               CustomerScreenActivity.this._thAuto.start();
            } else if(CustomerScreenActivity.this._thAuto != null) {
               CustomerScreenActivity.this._thAuto.interrupt();
               CustomerScreenActivity.this._thAuto = null;
            }

         }
      });
   }

   private void initView() {
      this.btn_dot = (Button)this.findViewById(2131230772);
      this.btn_rgb565 = (Button)this.findViewById(2131230788);
      this.btn_rgb565_location = (Button)this.findViewById(2131230789);
      this.btn_updatelogo = (Button)this.findViewById(2131230793);
      this.cb_auto = (CheckBox)this.findViewById(2131230816);
      this.iv_diaplay = (ImageView)this.findViewById(2131230869);
   }

   private void showDot() {
      if(this.colorIndex < this.colorArray.length - 1) {
         ++this.colorIndex;
      } else {
         this.colorIndex = 0;
      }

      Bitmap var1 = this.getBitmap();

      try {
         if(mIzkcService.showDotImage(this.colorArray[this.colorIndex], -16777216, var1)) {
            this.iv_diaplay.setImageBitmap(var1);
         }
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

   }

   private void showRGB565() {
      Bitmap var1 = BitmapFactory.decodeResource(this.getResources(), 2131165280);

      try {
         if(mIzkcService.showRGB565Image(var1)) {
            this.iv_diaplay.setImageBitmap(var1);
         }
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

   }

   private void showRGb565Location() {
      Bitmap var1 = BitmapFactory.decodeResource(this.getResources(), 2131165271);

      try {
         if(mIzkcService.showRGB565ImageCenter(var1)) {
            this.iv_diaplay.setImageBitmap(var1);
         }
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

   }

   private void updateLogo() {
      Bitmap var1 = BitmapFactory.decodeResource(this.getResources(), 2131165269);

      try {
         if(mIzkcService.updateLogo(var1)) {
            this.iv_diaplay.setImageBitmap(var1);
         }
      } catch (RemoteException var2) {
         var2.printStackTrace();
      }

   }

   private void updateLogoByPath() {
      String var1 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fj.jpg";
      if((new File(var1)).exists()) {
         try {
            if(mIzkcService.updateLogoByPath(var1)) {
               Bitmap var3 = BitmapFactory.decodeFile(var1);
               this.iv_diaplay.setImageBitmap(var3);
            }
         } catch (RemoteException var2) {
            var2.printStackTrace();
         }
      }

   }

   protected void handleStateMessage(Message var1) {
      super.handleStateMessage(var1);
      switch(var1.what) {
      case 17:
         if(mIzkcService != null) {
            try {
               mIzkcService.openBackLight(1);
            } catch (RemoteException var2) {
               var2.printStackTrace();
            }
         }

         this.enableWiget(true);
      default:
      }
   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131230772:
         this.showDot();
         break;
      case 2131230788:
         this.showRGB565();
         break;
      case 2131230789:
         this.showRGb565Location();
         break;
      case 2131230793:
         this.updateLogo();
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361819);
      this.initView();
      this.initEvent();
      this.enableWiget(false);
   }

   protected void onStop() {
      if(this._thAuto != null) {
         this._thAuto.interrupt();
         this._thAuto = null;
      }

      super.onStop();
   }

   public class ThreadAuto extends Thread {
      public void run() {
         super.run();

         for(; !interrupted(); SystemClock.sleep(2000L)) {
            if(CustomerScreenActivity.this.colorIndex < CustomerScreenActivity.this.colorArray.length - 1) {
               CustomerScreenActivity var1 = CustomerScreenActivity.this;
               ++var1.colorIndex;
            } else {
               CustomerScreenActivity.this.colorIndex = 0;
            }

            Bitmap var3 = CustomerScreenActivity.this.getBitmap();
            if(BaseActivity.mIzkcService != null) {
               try {
                  BaseActivity.mIzkcService.showDotImage(CustomerScreenActivity.this.colorArray[CustomerScreenActivity.this.colorIndex], -16777216, var3);
               } catch (RemoteException var2) {
                  var2.printStackTrace();
               }
            }
         }

      }
   }
}
