package com.bumptech.glide.load.model;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;

public class ResourceLoader implements ModelLoader {
   private static final String TAG = "ResourceLoader";
   private final Resources resources;
   private final ModelLoader uriLoader;

   public ResourceLoader(Context var1, ModelLoader var2) {
      this(var1.getResources(), var2);
   }

   public ResourceLoader(Resources var1, ModelLoader var2) {
      this.resources = var1;
      this.uriLoader = var2;
   }

   public DataFetcher getResourceFetcher(Integer var1, int var2, int var3) {
      Object var5 = null;

      Uri var4;
      try {
         StringBuilder var9 = new StringBuilder();
         var4 = Uri.parse(var9.append("android.resource://").append(this.resources.getResourcePackageName(var1.intValue())).append('/').append(this.resources.getResourceTypeName(var1.intValue())).append('/').append(this.resources.getResourceEntryName(var1.intValue())).toString());
      } catch (NotFoundException var7) {
         var4 = (Uri)var5;
         if(Log.isLoggable("ResourceLoader", 5)) {
            Log.w("ResourceLoader", "Received invalid resource id: " + var1, var7);
            var4 = (Uri)var5;
         }
      }

      DataFetcher var8;
      if(var4 != null) {
         var8 = this.uriLoader.getResourceFetcher(var4, var2, var3);
      } else {
         var8 = null;
      }

      return var8;
   }
}
