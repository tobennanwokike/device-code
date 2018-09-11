package com.smartdevice.aidltestdemo.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class LoadingDialog extends Dialog {
   private TextView tvContent;
   private View view;

   public LoadingDialog(Context var1) {
      this(var1, 2131624284);
   }

   public LoadingDialog(Context var1, int var2) {
      super(var1, var2);
      this.view = View.inflate(var1, 2131361839, (ViewGroup)null);
      this.setCanceledOnTouchOutside(false);
   }

   private void init(View var1) {
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      LayoutParams var2 = new LayoutParams(-2, -2);
      this.setContentView(this.view, var2);
      this.init(this.view);
   }

   public void setAlpha(float var1) {
      android.view.WindowManager.LayoutParams var2 = this.getWindow().getAttributes();
      var2.alpha = var1;
      this.getWindow().setAttributes(var2);
   }

   public void setContent(int var1) {
      if(this.tvContent == null) {
         this.tvContent = (TextView)this.view.findViewById(2131230973);
      }

      this.tvContent.setText(var1);
   }

   public void setContent(String var1) {
      if(this.tvContent == null) {
         this.tvContent = (TextView)this.view.findViewById(2131230973);
      }

      this.tvContent.setText(var1);
   }
}
