package com.bumptech.glide.request.target;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ViewTarget extends BaseTarget {
   private static final String TAG = "ViewTarget";
   private static boolean isTagUsedAtLeastOnce = false;
   private static Integer tagId = null;
   private final ViewTarget.SizeDeterminer sizeDeterminer;
   protected final View view;

   public ViewTarget(View var1) {
      if(var1 == null) {
         throw new NullPointerException("View must not be null!");
      } else {
         this.view = var1;
         this.sizeDeterminer = new ViewTarget.SizeDeterminer(var1);
      }
   }

   private Object getTag() {
      Object var1;
      if(tagId == null) {
         var1 = this.view.getTag();
      } else {
         var1 = this.view.getTag(tagId.intValue());
      }

      return var1;
   }

   private void setTag(Object var1) {
      if(tagId == null) {
         isTagUsedAtLeastOnce = true;
         this.view.setTag(var1);
      } else {
         this.view.setTag(tagId.intValue(), var1);
      }

   }

   public static void setTagId(int var0) {
      if(tagId == null && !isTagUsedAtLeastOnce) {
         tagId = Integer.valueOf(var0);
      } else {
         throw new IllegalArgumentException("You cannot set the tag id more than once or change the tag id after the first request has been made");
      }
   }

   public Request getRequest() {
      Object var2 = this.getTag();
      Request var1 = null;
      if(var2 != null) {
         if(!(var2 instanceof Request)) {
            throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
         }

         var1 = (Request)var2;
      }

      return var1;
   }

   public void getSize(SizeReadyCallback var1) {
      this.sizeDeterminer.getSize(var1);
   }

   public View getView() {
      return this.view;
   }

   public void setRequest(Request var1) {
      this.setTag(var1);
   }

   public String toString() {
      return "Target for: " + this.view;
   }

   private static class SizeDeterminer {
      private static final int PENDING_SIZE = 0;
      private final List cbs = new ArrayList();
      private Point displayDimens;
      private ViewTarget.SizeDeterminerLayoutListener layoutListener;
      private final View view;

      public SizeDeterminer(View var1) {
         this.view = var1;
      }

      private void checkCurrentDimens() {
         if(!this.cbs.isEmpty()) {
            int var2 = this.getViewWidthOrParam();
            int var1 = this.getViewHeightOrParam();
            if(this.isSizeValid(var2) && this.isSizeValid(var1)) {
               this.notifyCbs(var2, var1);
               ViewTreeObserver var3 = this.view.getViewTreeObserver();
               if(var3.isAlive()) {
                  var3.removeOnPreDrawListener(this.layoutListener);
               }

               this.layoutListener = null;
            }
         }

      }

      @TargetApi(13)
      private Point getDisplayDimens() {
         Point var1;
         if(this.displayDimens != null) {
            var1 = this.displayDimens;
         } else {
            Display var2 = ((WindowManager)this.view.getContext().getSystemService("window")).getDefaultDisplay();
            if(VERSION.SDK_INT >= 13) {
               this.displayDimens = new Point();
               var2.getSize(this.displayDimens);
            } else {
               this.displayDimens = new Point(var2.getWidth(), var2.getHeight());
            }

            var1 = this.displayDimens;
         }

         return var1;
      }

      private int getSizeForParam(int var1, boolean var2) {
         if(var1 == -2) {
            Point var3 = this.getDisplayDimens();
            if(var2) {
               var1 = var3.y;
            } else {
               var1 = var3.x;
            }
         }

         return var1;
      }

      private int getViewHeightOrParam() {
         LayoutParams var2 = this.view.getLayoutParams();
         int var1;
         if(this.isSizeValid(this.view.getHeight())) {
            var1 = this.view.getHeight();
         } else if(var2 != null) {
            var1 = this.getSizeForParam(var2.height, true);
         } else {
            var1 = 0;
         }

         return var1;
      }

      private int getViewWidthOrParam() {
         int var1 = 0;
         LayoutParams var2 = this.view.getLayoutParams();
         if(this.isSizeValid(this.view.getWidth())) {
            var1 = this.view.getWidth();
         } else if(var2 != null) {
            var1 = this.getSizeForParam(var2.width, false);
         }

         return var1;
      }

      private boolean isSizeValid(int var1) {
         boolean var2;
         if(var1 <= 0 && var1 != -2) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      private void notifyCbs(int var1, int var2) {
         Iterator var3 = this.cbs.iterator();

         while(var3.hasNext()) {
            ((SizeReadyCallback)var3.next()).onSizeReady(var1, var2);
         }

         this.cbs.clear();
      }

      public void getSize(SizeReadyCallback var1) {
         int var3 = this.getViewWidthOrParam();
         int var2 = this.getViewHeightOrParam();
         if(this.isSizeValid(var3) && this.isSizeValid(var2)) {
            var1.onSizeReady(var3, var2);
         } else {
            if(!this.cbs.contains(var1)) {
               this.cbs.add(var1);
            }

            if(this.layoutListener == null) {
               ViewTreeObserver var4 = this.view.getViewTreeObserver();
               this.layoutListener = new ViewTarget.SizeDeterminerLayoutListener(this);
               var4.addOnPreDrawListener(this.layoutListener);
            }
         }

      }
   }

   private static class SizeDeterminerLayoutListener implements OnPreDrawListener {
      private final WeakReference sizeDeterminerRef;

      public SizeDeterminerLayoutListener(ViewTarget.SizeDeterminer var1) {
         this.sizeDeterminerRef = new WeakReference(var1);
      }

      public boolean onPreDraw() {
         if(Log.isLoggable("ViewTarget", 2)) {
            Log.v("ViewTarget", "OnGlobalLayoutListener called listener=" + this);
         }

         ViewTarget.SizeDeterminer var1 = (ViewTarget.SizeDeterminer)this.sizeDeterminerRef.get();
         if(var1 != null) {
            var1.checkCurrentDimens();
         }

         return true;
      }
   }
}
