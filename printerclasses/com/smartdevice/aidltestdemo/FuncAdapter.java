package com.smartdevice.aidltestdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class FuncAdapter extends BaseAdapter {
   private List data;
   private Context mContext;
   private LayoutInflater mLayoutInflater;

   public FuncAdapter(Context var1, List var2) {
      this.mContext = var1;
      this.data = var2;
      this.mLayoutInflater = LayoutInflater.from(var1);
   }

   public int getCount() {
      int var1;
      if(this.data != null) {
         var1 = this.data.size();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public Object getItem(int var1) {
      Object var2;
      if(this.data != null) {
         var2 = this.data.get(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public long getItemId(int var1) {
      return 0L;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      var2 = this.mLayoutInflater.inflate(2131361836, (ViewGroup)null);
      ((TextView)var2.findViewById(2131230965)).setText((CharSequence)((HashMap)this.data.get(var1)).get("name"));
      return var2;
   }
}
