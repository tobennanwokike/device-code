package com.bumptech.glide.load.model;

import android.content.Context;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;

public interface ModelLoaderFactory {
   ModelLoader build(Context var1, GenericLoaderFactory var2);

   void teardown();
}
