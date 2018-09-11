package com.bumptech.glide.load.model;

import android.net.Uri;

final class AssetUriParser {
   private static final String ASSET_PATH_SEGMENT = "android_asset";
   private static final String ASSET_PREFIX = "file:///android_asset/";
   private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();

   public static boolean isAssetUri(Uri var0) {
      boolean var2 = false;
      boolean var1 = var2;
      if("file".equals(var0.getScheme())) {
         var1 = var2;
         if(!var0.getPathSegments().isEmpty()) {
            var1 = var2;
            if("android_asset".equals(var0.getPathSegments().get(0))) {
               var1 = true;
            }
         }
      }

      return var1;
   }

   public static String toAssetPath(Uri var0) {
      return var0.toString().substring(ASSET_PREFIX_LENGTH);
   }
}
