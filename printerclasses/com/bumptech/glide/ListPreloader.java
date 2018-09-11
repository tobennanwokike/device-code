package com.bumptech.glide;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.List;
import java.util.Queue;

public class ListPreloader implements OnScrollListener {
   private boolean isIncreasing = true;
   private int lastEnd;
   private int lastFirstVisible;
   private int lastStart;
   private final int maxPreload;
   private final ListPreloader.PreloadSizeProvider preloadDimensionProvider;
   private final ListPreloader.PreloadModelProvider preloadModelProvider;
   private final ListPreloader.PreloadTargetQueue preloadTargetQueue;
   private int totalItemCount;

   @Deprecated
   public ListPreloader(int var1) {
      this.preloadModelProvider = new ListPreloader.PreloadModelProvider() {
         public List getPreloadItems(int var1) {
            return ListPreloader.this.getItems(var1, var1 + 1);
         }

         public GenericRequestBuilder getPreloadRequestBuilder(Object var1) {
            return ListPreloader.this.getRequestBuilder(var1);
         }
      };
      this.preloadDimensionProvider = new ListPreloader.PreloadSizeProvider() {
         public int[] getPreloadSize(Object var1, int var2, int var3) {
            return ListPreloader.this.getDimensions(var1);
         }
      };
      this.maxPreload = var1;
      this.preloadTargetQueue = new ListPreloader.PreloadTargetQueue(var1 + 1);
   }

   public ListPreloader(ListPreloader.PreloadModelProvider var1, ListPreloader.PreloadSizeProvider var2, int var3) {
      this.preloadModelProvider = var1;
      this.preloadDimensionProvider = var2;
      this.maxPreload = var3;
      this.preloadTargetQueue = new ListPreloader.PreloadTargetQueue(var3 + 1);
   }

   private void cancelAll() {
      for(int var1 = 0; var1 < this.maxPreload; ++var1) {
         Glide.clear((Target)this.preloadTargetQueue.next(0, 0));
      }

   }

   private void preload(int var1, int var2) {
      int var3;
      int var4;
      if(var1 < var2) {
         var3 = Math.max(this.lastEnd, var1);
         var4 = var2;
      } else {
         var3 = var2;
         var4 = Math.min(this.lastStart, var1);
      }

      var4 = Math.min(this.totalItemCount, var4);
      var3 = Math.min(this.totalItemCount, Math.max(0, var3));
      if(var1 < var2) {
         for(var1 = var3; var1 < var4; ++var1) {
            this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(var1), var1, true);
         }
      } else {
         for(var1 = var4 - 1; var1 >= var3; --var1) {
            this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(var1), var1, false);
         }
      }

      this.lastStart = var3;
      this.lastEnd = var4;
   }

   private void preload(int var1, boolean var2) {
      if(this.isIncreasing != var2) {
         this.isIncreasing = var2;
         this.cancelAll();
      }

      int var3;
      if(var2) {
         var3 = this.maxPreload;
      } else {
         var3 = -this.maxPreload;
      }

      this.preload(var1, var3 + var1);
   }

   private void preloadAdapterPosition(List var1, int var2, boolean var3) {
      int var5 = var1.size();
      int var4;
      if(var3) {
         for(var4 = 0; var4 < var5; ++var4) {
            this.preloadItem(var1.get(var4), var2, var4);
         }
      } else {
         for(var4 = var5 - 1; var4 >= 0; --var4) {
            this.preloadItem(var1.get(var4), var2, var4);
         }
      }

   }

   private void preloadItem(Object var1, int var2, int var3) {
      int[] var4 = this.preloadDimensionProvider.getPreloadSize(var1, var2, var3);
      if(var4 != null) {
         this.preloadModelProvider.getPreloadRequestBuilder(var1).into((Target)this.preloadTargetQueue.next(var4[0], var4[1]));
      }

   }

   @Deprecated
   protected int[] getDimensions(Object var1) {
      throw new IllegalStateException("You must either provide a PreloadDimensionProvider or override getDimensions()");
   }

   @Deprecated
   protected List getItems(int var1, int var2) {
      throw new IllegalStateException("You must either provide a PreloadModelProvider or override getItems()");
   }

   @Deprecated
   protected GenericRequestBuilder getRequestBuilder(Object var1) {
      throw new IllegalStateException("You must either provide a PreloadModelProvider, or override getRequestBuilder()");
   }

   public void onScroll(AbsListView var1, int var2, int var3, int var4) {
      this.totalItemCount = var4;
      if(var2 > this.lastFirstVisible) {
         this.preload(var2 + var3, true);
      } else if(var2 < this.lastFirstVisible) {
         this.preload(var2, false);
      }

      this.lastFirstVisible = var2;
   }

   public void onScrollStateChanged(AbsListView var1, int var2) {
   }

   public interface PreloadModelProvider {
      List getPreloadItems(int var1);

      GenericRequestBuilder getPreloadRequestBuilder(Object var1);
   }

   public interface PreloadSizeProvider {
      int[] getPreloadSize(Object var1, int var2, int var3);
   }

   private static class PreloadTarget extends BaseTarget {
      private int photoHeight;
      private int photoWidth;

      private PreloadTarget() {
      }

      // $FF: synthetic method
      PreloadTarget(Object var1) {
         this();
      }

      public void getSize(SizeReadyCallback var1) {
         var1.onSizeReady(this.photoWidth, this.photoHeight);
      }

      public void onResourceReady(Object var1, GlideAnimation var2) {
      }
   }

   private static final class PreloadTargetQueue {
      private final Queue queue;

      public PreloadTargetQueue(int var1) {
         this.queue = Util.createQueue(var1);

         for(int var2 = 0; var2 < var1; ++var2) {
            this.queue.offer(new ListPreloader.PreloadTarget(null));
         }

      }

      public ListPreloader.PreloadTarget next(int var1, int var2) {
         ListPreloader.PreloadTarget var3 = (ListPreloader.PreloadTarget)this.queue.poll();
         this.queue.offer(var3);
         var3.photoWidth = var1;
         var3.photoHeight = var2;
         return var3;
      }
   }
}
