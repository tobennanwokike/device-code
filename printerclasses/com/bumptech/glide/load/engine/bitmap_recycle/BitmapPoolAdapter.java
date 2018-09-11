package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public class BitmapPoolAdapter implements BitmapPool {
   public void clearMemory() {
   }

   public Bitmap get(int var1, int var2, Config var3) {
      return null;
   }

   public Bitmap getDirty(int var1, int var2, Config var3) {
      return null;
   }

   public int getMaxSize() {
      return 0;
   }

   public boolean put(Bitmap var1) {
      return false;
   }

   public void setSizeMultiplier(float var1) {
   }

   public void trimMemory(int var1) {
   }
}
