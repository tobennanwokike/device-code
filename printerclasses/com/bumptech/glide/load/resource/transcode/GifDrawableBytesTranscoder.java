package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bytes.BytesResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

public class GifDrawableBytesTranscoder implements ResourceTranscoder {
   public String getId() {
      return "GifDrawableBytesTranscoder.com.bumptech.glide.load.resource.transcode";
   }

   public Resource transcode(Resource var1) {
      return new BytesResource(((GifDrawable)var1.get()).getData());
   }
}
