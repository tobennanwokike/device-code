package com.bumptech.glide.load.model;

import com.bumptech.glide.load.data.DataFetcher;

public interface ModelLoader {
   DataFetcher getResourceFetcher(Object var1, int var2, int var3);
}
