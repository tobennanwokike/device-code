package com.bumptech.glide.util;

import com.bumptech.glide.ListPreloader;
import java.util.Arrays;

public class FixedPreloadSizeProvider implements ListPreloader.PreloadSizeProvider {
   private final int[] size;

   public FixedPreloadSizeProvider(int var1, int var2) {
      this.size = new int[]{var1, var2};
   }

   public int[] getPreloadSize(Object var1, int var2, int var3) {
      return Arrays.copyOf(this.size, this.size.length);
   }
}
