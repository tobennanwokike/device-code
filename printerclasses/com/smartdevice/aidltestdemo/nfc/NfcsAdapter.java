package com.smartdevice.aidltestdemo.nfc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smartdevice.aidltestdemo.nfc.Nfc;
import java.util.List;

public class NfcsAdapter extends BaseAdapter {
   private Context context;
   private List list;

   public NfcsAdapter(List var1, Context var2) {
      this.list = var1;
      this.context = var2;
   }

   public int getCount() {
      return this.list.size();
   }

   public Object getItem(int var1) {
      return Integer.valueOf(var1);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   @SuppressLint({"ResourceAsColor"})
   public View getView(int var1, View var2, ViewGroup var3) {
      var2 = LayoutInflater.from(this.context).inflate(2131361841, (ViewGroup)null);
      ((TextView)var2.findViewById(2131230974)).setText(((Nfc)this.list.get(var1)).getContent());
      return var2;
   }
}
