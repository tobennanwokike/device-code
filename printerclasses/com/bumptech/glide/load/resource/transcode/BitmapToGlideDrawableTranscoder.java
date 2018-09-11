package com.bumptech.glide.load.resource.transcode;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.GlideBitmapDrawableTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class BitmapToGlideDrawableTranscoder implements ResourceTranscoder {
   private final GlideBitmapDrawableTranscoder glideBitmapDrawableTranscoder;

   public BitmapToGlideDrawableTranscoder(Context var1) {
      this(new GlideBitmapDrawableTranscoder(var1));
   }

   public BitmapToGlideDrawableTranscoder(GlideBitmapDrawableTranscoder var1) {
      this.glideBitmapDrawableTranscoder = var1;
   }

   public String getId() {
      return this.glideBitmapDrawableTranscoder.getId();
   }

   public Resource transcode(Resource var1) {
      return this.glideBitmapDrawableTranscoder.transcode(var1);
   }
}
