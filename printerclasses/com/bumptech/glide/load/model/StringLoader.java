package com.bumptech.glide.load.model;

import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;

public class StringLoader implements ModelLoader {
   private final ModelLoader uriLoader;

   public StringLoader(ModelLoader var1) {
      this.uriLoader = var1;
   }

   private static Uri toFileUri(String var0) {
      return Uri.fromFile(new File(var0));
   }

   public DataFetcher getResourceFetcher(String var1, int var2, int var3) {
      DataFetcher var6;
      if(TextUtils.isEmpty(var1)) {
         var6 = null;
      } else {
         Uri var4;
         if(var1.startsWith("/")) {
            var4 = toFileUri(var1);
         } else {
            Uri var5 = Uri.parse(var1);
            var4 = var5;
            if(var5.getScheme() == null) {
               var4 = toFileUri(var1);
            }
         }

         var6 = this.uriLoader.getResourceFetcher(var4, var2, var3);
      }

      return var6;
   }
}
