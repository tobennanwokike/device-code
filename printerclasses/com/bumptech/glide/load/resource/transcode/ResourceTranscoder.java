package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.engine.Resource;

public interface ResourceTranscoder {
   String getId();

   Resource transcode(Resource var1);
}
