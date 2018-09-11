package com.smartdevice.aidltestdemo.language;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smartdevice.aidltestdemo.language.LanguageModel;
import java.util.List;

public class SpinnerAdapterLanguage extends BaseAdapter {
   private List languageModelList;
   private Context mContext;
   private int view;

   public SpinnerAdapterLanguage(Context var1, int var2, List var3) {
      this.mContext = var1;
      this.view = var2;
      this.languageModelList = var3;
   }

   public int getCount() {
      return this.languageModelList.size();
   }

   public Object getItem(int var1) {
      return this.languageModelList.get(var1);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      var2 = LayoutInflater.from(this.mContext).inflate(this.view, (ViewGroup)null);
      if(var2 != null) {
         ((TextView)var2.findViewById(2131230965)).setText(((LanguageModel)this.languageModelList.get(var1)).description + "(" + ((LanguageModel)this.languageModelList.get(var1)).language + ")");
      }

      return var2;
   }
}
