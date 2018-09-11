package com.smartdevice.aidltestdemo.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.smartdevice.aidltestdemo.util.FileUtil;
import java.io.File;

public class CmdDialog extends Dialog {
   private CmdDialog.DialogCallBack callback;
   private String cmdStr;
   String[] contents;
   private ListView lvCmdDisplay;
   private Context mContext;

   public CmdDialog(Context var1, CmdDialog.DialogCallBack var2) {
      super(var1, 2131624284);
      this.mContext = var1;
      this.callback = var2;
   }

   private void initData() {
      File var1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "cmd.txt");
      if(var1.exists()) {
         String var3 = FileUtil.convertCodeAndGetText(var1);
         this.contents = var3.split("\\n");
         ArrayAdapter var2 = new ArrayAdapter(this.mContext, 2131361836, 2131230965, this.contents);
         this.lvCmdDisplay.setAdapter(var2);
         Log.e("data", var3);
      } else {
         this.callback.submit("cmd.txt doesn\'t exist");
         this.dismiss();
      }

   }

   private void initView() {
      this.lvCmdDisplay = (ListView)this.findViewById(2131230884);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361838);
      this.setCanceledOnTouchOutside(false);
      this.initView();
      this.initData();
      this.lvCmdDisplay.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            CmdDialog.this.callback.submit(CmdDialog.this.contents[var3].split(",")[1]);
            CmdDialog.this.dismiss();
         }
      });
   }

   public interface DialogCallBack {
      void submit(String var1);
   }
}
