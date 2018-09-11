package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;

public interface DataFetcher {
   void cancel();

   void cleanup();

   String getId();

   Object loadData(Priority var1) throws Exception;
}
