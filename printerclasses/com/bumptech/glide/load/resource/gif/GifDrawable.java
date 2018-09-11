package com.bumptech.glide.load.resource.gif;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.view.Gravity;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifFrameLoader;

public class GifDrawable extends GlideDrawable implements GifFrameLoader.FrameCallback {
   private boolean applyGravity;
   private final GifDecoder decoder;
   private final Rect destRect;
   private final GifFrameLoader frameLoader;
   private boolean isRecycled;
   private boolean isRunning;
   private boolean isStarted;
   private boolean isVisible;
   private int loopCount;
   private int maxLoopCount;
   private final Paint paint;
   private final GifDrawable.GifState state;

   public GifDrawable(Context var1, GifDecoder.BitmapProvider var2, BitmapPool var3, Transformation var4, int var5, int var6, GifHeader var7, byte[] var8, Bitmap var9) {
      this(new GifDrawable.GifState(var7, var8, var1, var4, var5, var6, var2, var3, var9));
   }

   GifDrawable(GifDecoder var1, GifFrameLoader var2, Bitmap var3, BitmapPool var4, Paint var5) {
      this.destRect = new Rect();
      this.isVisible = true;
      this.maxLoopCount = -1;
      this.decoder = var1;
      this.frameLoader = var2;
      this.state = new GifDrawable.GifState((GifDrawable.GifState)null);
      this.paint = var5;
      this.state.bitmapPool = var4;
      this.state.firstFrame = var3;
   }

   GifDrawable(GifDrawable.GifState var1) {
      this.destRect = new Rect();
      this.isVisible = true;
      this.maxLoopCount = -1;
      if(var1 == null) {
         throw new NullPointerException("GifState must not be null");
      } else {
         this.state = var1;
         this.decoder = new GifDecoder(var1.bitmapProvider);
         this.paint = new Paint();
         this.decoder.setData(var1.gifHeader, var1.data);
         this.frameLoader = new GifFrameLoader(var1.context, this, this.decoder, var1.targetWidth, var1.targetHeight);
         this.frameLoader.setFrameTransformation(var1.frameTransformation);
      }
   }

   public GifDrawable(GifDrawable var1, Bitmap var2, Transformation var3) {
      this(new GifDrawable.GifState(var1.state.gifHeader, var1.state.data, var1.state.context, var3, var1.state.targetWidth, var1.state.targetHeight, var1.state.bitmapProvider, var1.state.bitmapPool, var2));
   }

   private void reset() {
      this.frameLoader.clear();
      this.invalidateSelf();
   }

   private void resetLoopCount() {
      this.loopCount = 0;
   }

   private void startRunning() {
      if(this.decoder.getFrameCount() == 1) {
         this.invalidateSelf();
      } else if(!this.isRunning) {
         this.isRunning = true;
         this.frameLoader.start();
         this.invalidateSelf();
      }

   }

   private void stopRunning() {
      this.isRunning = false;
      this.frameLoader.stop();
   }

   public void draw(Canvas var1) {
      if(!this.isRecycled) {
         if(this.applyGravity) {
            Gravity.apply(119, this.getIntrinsicWidth(), this.getIntrinsicHeight(), this.getBounds(), this.destRect);
            this.applyGravity = false;
         }

         Bitmap var2 = this.frameLoader.getCurrentFrame();
         if(var2 == null) {
            var2 = this.state.firstFrame;
         }

         var1.drawBitmap(var2, (Rect)null, this.destRect, this.paint);
      }

   }

   public ConstantState getConstantState() {
      return this.state;
   }

   public byte[] getData() {
      return this.state.data;
   }

   public GifDecoder getDecoder() {
      return this.decoder;
   }

   public Bitmap getFirstFrame() {
      return this.state.firstFrame;
   }

   public int getFrameCount() {
      return this.decoder.getFrameCount();
   }

   public Transformation getFrameTransformation() {
      return this.state.frameTransformation;
   }

   public int getIntrinsicHeight() {
      return this.state.firstFrame.getHeight();
   }

   public int getIntrinsicWidth() {
      return this.state.firstFrame.getWidth();
   }

   public int getOpacity() {
      return -2;
   }

   public boolean isAnimated() {
      return true;
   }

   boolean isRecycled() {
      return this.isRecycled;
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   protected void onBoundsChange(Rect var1) {
      super.onBoundsChange(var1);
      this.applyGravity = true;
   }

   @TargetApi(11)
   public void onFrameReady(int var1) {
      if(VERSION.SDK_INT >= 11 && this.getCallback() == null) {
         this.stop();
         this.reset();
      } else {
         this.invalidateSelf();
         if(var1 == this.decoder.getFrameCount() - 1) {
            ++this.loopCount;
         }

         if(this.maxLoopCount != -1 && this.loopCount >= this.maxLoopCount) {
            this.stop();
         }
      }

   }

   public void recycle() {
      this.isRecycled = true;
      this.state.bitmapPool.put(this.state.firstFrame);
      this.frameLoader.clear();
      this.frameLoader.stop();
   }

   public void setAlpha(int var1) {
      this.paint.setAlpha(var1);
   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
   }

   public void setFrameTransformation(Transformation var1, Bitmap var2) {
      if(var2 == null) {
         throw new NullPointerException("The first frame of the GIF must not be null");
      } else if(var1 == null) {
         throw new NullPointerException("The frame transformation must not be null");
      } else {
         this.state.frameTransformation = var1;
         this.state.firstFrame = var2;
         this.frameLoader.setFrameTransformation(var1);
      }
   }

   void setIsRunning(boolean var1) {
      this.isRunning = var1;
   }

   public void setLoopCount(int var1) {
      if(var1 <= 0 && var1 != -1 && var1 != 0) {
         throw new IllegalArgumentException("Loop count must be greater than 0, or equal to GlideDrawable.LOOP_FOREVER, or equal to GlideDrawable.LOOP_INTRINSIC");
      } else {
         if(var1 == 0) {
            this.maxLoopCount = this.decoder.getLoopCount();
         } else {
            this.maxLoopCount = var1;
         }

      }
   }

   public boolean setVisible(boolean var1, boolean var2) {
      this.isVisible = var1;
      if(!var1) {
         this.stopRunning();
      } else if(this.isStarted) {
         this.startRunning();
      }

      return super.setVisible(var1, var2);
   }

   public void start() {
      this.isStarted = true;
      this.resetLoopCount();
      if(this.isVisible) {
         this.startRunning();
      }

   }

   public void stop() {
      this.isStarted = false;
      this.stopRunning();
      if(VERSION.SDK_INT < 11) {
         this.reset();
      }

   }

   static class GifState extends ConstantState {
      private static final int GRAVITY = 119;
      BitmapPool bitmapPool;
      GifDecoder.BitmapProvider bitmapProvider;
      Context context;
      byte[] data;
      Bitmap firstFrame;
      Transformation frameTransformation;
      GifHeader gifHeader;
      int targetHeight;
      int targetWidth;

      public GifState(GifHeader var1, byte[] var2, Context var3, Transformation var4, int var5, int var6, GifDecoder.BitmapProvider var7, BitmapPool var8, Bitmap var9) {
         if(var9 == null) {
            throw new NullPointerException("The first frame of the GIF must not be null");
         } else {
            this.gifHeader = var1;
            this.data = var2;
            this.bitmapPool = var8;
            this.firstFrame = var9;
            this.context = var3.getApplicationContext();
            this.frameTransformation = var4;
            this.targetWidth = var5;
            this.targetHeight = var6;
            this.bitmapProvider = var7;
         }
      }

      public GifState(GifDrawable.GifState var1) {
         if(var1 != null) {
            this.gifHeader = var1.gifHeader;
            this.data = var1.data;
            this.context = var1.context;
            this.frameTransformation = var1.frameTransformation;
            this.targetWidth = var1.targetWidth;
            this.targetHeight = var1.targetHeight;
            this.bitmapProvider = var1.bitmapProvider;
            this.bitmapPool = var1.bitmapPool;
            this.firstFrame = var1.firstFrame;
         }

      }

      public int getChangingConfigurations() {
         return 0;
      }

      public Drawable newDrawable() {
         return new GifDrawable(this);
      }

      public Drawable newDrawable(Resources var1) {
         return this.newDrawable();
      }
   }
}
