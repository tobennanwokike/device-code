package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public interface BitmapDecoder {
   Bitmap decode(Object var1, BitmapPool var2, int var3, int var4, DecodeFormat var5) throws Exception;

   String getId();
}
