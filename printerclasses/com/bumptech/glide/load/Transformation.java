package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.Resource;

public interface Transformation {
   String getId();

   Resource transform(Resource var1, int var2, int var3);
}
