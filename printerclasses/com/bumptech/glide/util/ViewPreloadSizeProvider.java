package com.bumptech.glide.util;

import android.view.View;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.Arrays;

public class ViewPreloadSizeProvider implements ListPreloader.PreloadSizeProvider, SizeReadyCallback {
   private int[] size;
   private ViewPreloadSizeProvider.SizeViewTarget viewTarget;

   public ViewPreloadSizeProvider() {
   }

   public ViewPreloadSizeProvider(View var1) {
      this.setView(var1);
   }

   public int[] getPreloadSize(Object var1, int var2, int var3) {
      int[] var4;
      if(this.size == null) {
         var4 = null;
      } else {
         var4 = Arrays.copyOf(this.size, this.size.length);
      }

      return var4;
   }

   public void onSizeReady(int var1, int var2) {
      this.size = new int[]{var1, var2};
      this.viewTarget = null;
   }

   public void setView(View var1) {
      if(this.size == null && this.viewTarget == null) {
         this.viewTarget = new ViewPreloadSizeProvider.SizeViewTarget(var1, this);
      }

   }

   private static final class SizeViewTarget extends ViewTarget {
      public SizeViewTarget(View var1, SizeReadyCallback var2) {
         super(var1);
         this.getSize(var2);
      }

      public void onResourceReady(Object var1, GlideAnimation var2) {
      }
   }
}
