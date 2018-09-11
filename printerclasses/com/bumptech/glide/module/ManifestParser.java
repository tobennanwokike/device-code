package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.module.GlideModule;
import java.util.List;

public final class ManifestParser {
   private static final String GLIDE_MODULE_VALUE = "GlideModule";
   private final Context context;

   public ManifestParser(Context var1) {
      this.context = var1;
   }

   private static GlideModule parseModule(String var0) {
      Class var5;
      try {
         var5 = Class.forName(var0);
      } catch (ClassNotFoundException var4) {
         throw new IllegalArgumentException("Unable to find GlideModule implementation", var4);
      }

      Object var1;
      try {
         var1 = var5.newInstance();
      } catch (InstantiationException var2) {
         throw new RuntimeException("Unable to instantiate GlideModule implementation for " + var5, var2);
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("Unable to instantiate GlideModule implementation for " + var5, var3);
      }

      if(!(var1 instanceof GlideModule)) {
         throw new RuntimeException("Expected instanceof GlideModule, but found: " + var1);
      } else {
         return (GlideModule)var1;
      }
   }

   public List parse() {
      // $FF: Couldn't be decompiled
   }
}
