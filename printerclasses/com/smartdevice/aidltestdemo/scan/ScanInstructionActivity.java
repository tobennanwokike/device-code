package com.smartdevice.aidltestdemo.scan;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ScanInstructionActivity extends Activity {
   private WebView webview_readme;

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131361864);
      this.webview_readme = (WebView)this.findViewById(2131230987);
      this.webview_readme.loadUrl(this.getString(2131558490));
   }
}
