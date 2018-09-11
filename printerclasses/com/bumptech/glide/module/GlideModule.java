package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

public interface GlideModule {
   void applyOptions(Context var1, GlideBuilder var2);

   void registerComponents(Context var1, Glide var2);
}
