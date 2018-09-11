package com.bumptech.glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawableResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class GlideBitmapDrawableTranscoder implements ResourceTranscoder {
   private final BitmapPool bitmapPool;
   private final Resources resources;

   public GlideBitmapDrawableTranscoder(Context var1) {
      this(var1.getResources(), Glide.get(var1).getBitmapPool());
   }

   public GlideBitmapDrawableTranscoder(Resources var1, BitmapPool var2) {
      this.resources = var1;
      this.bitmapPool = var2;
   }

   public String getId() {
      return "GlideBitmapDrawableTranscoder.com.bumptech.glide.load.resource.transcode";
   }

   public Resource transcode(Resource var1) {
      return new GlideBitmapDrawableResource(new GlideBitmapDrawable(this.resources, (Bitmap)var1.get()), this.bitmapPool);
   }
}
