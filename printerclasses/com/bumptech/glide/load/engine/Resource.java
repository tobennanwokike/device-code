package com.bumptech.glide.load.engine;

public interface Resource {
   Object get();

   int getSize();

   void recycle();
}
