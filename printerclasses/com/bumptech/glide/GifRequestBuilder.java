package com.bumptech.glide;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import com.bumptech.glide.BitmapOptions;
import com.bumptech.glide.DrawableOptions;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

public class GifRequestBuilder extends GenericRequestBuilder implements BitmapOptions, DrawableOptions {
   GifRequestBuilder(LoadProvider var1, Class var2, GenericRequestBuilder var3) {
      super(var1, var2, var3);
   }

   private GifDrawableTransformation[] toGifTransformations(Transformation[] var1) {
      GifDrawableTransformation[] var3 = new GifDrawableTransformation[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var3[var2] = new GifDrawableTransformation(var1[var2], this.glide.getBitmapPool());
      }

      return var3;
   }

   public GifRequestBuilder animate(int var1) {
      super.animate(var1);
      return this;
   }

   @Deprecated
   public GifRequestBuilder animate(Animation var1) {
      super.animate(var1);
      return this;
   }

   public GifRequestBuilder animate(ViewPropertyAnimation.Animator var1) {
      super.animate(var1);
      return this;
   }

   void applyCenterCrop() {
      this.centerCrop();
   }

   void applyFitCenter() {
      this.fitCenter();
   }

   public GifRequestBuilder cacheDecoder(ResourceDecoder var1) {
      super.cacheDecoder(var1);
      return this;
   }

   public GifRequestBuilder centerCrop() {
      return this.transformFrame(new BitmapTransformation[]{this.glide.getBitmapCenterCrop()});
   }

   public GifRequestBuilder clone() {
      return (GifRequestBuilder)super.clone();
   }

   public GifRequestBuilder crossFade() {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory()));
      return this;
   }

   public GifRequestBuilder crossFade(int var1) {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory(var1)));
      return this;
   }

   public GifRequestBuilder crossFade(int var1, int var2) {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory(this.context, var1, var2)));
      return this;
   }

   @Deprecated
   public GifRequestBuilder crossFade(Animation var1, int var2) {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory(var1, var2)));
      return this;
   }

   public GifRequestBuilder decoder(ResourceDecoder var1) {
      super.decoder(var1);
      return this;
   }

   public GifRequestBuilder diskCacheStrategy(DiskCacheStrategy var1) {
      super.diskCacheStrategy(var1);
      return this;
   }

   public GifRequestBuilder dontAnimate() {
      super.dontAnimate();
      return this;
   }

   public GifRequestBuilder dontTransform() {
      super.dontTransform();
      return this;
   }

   public GifRequestBuilder encoder(ResourceEncoder var1) {
      super.encoder(var1);
      return this;
   }

   public GifRequestBuilder error(int var1) {
      super.error(var1);
      return this;
   }

   public GifRequestBuilder error(Drawable var1) {
      super.error(var1);
      return this;
   }

   public GifRequestBuilder fallback(int var1) {
      super.fallback(var1);
      return this;
   }

   public GifRequestBuilder fallback(Drawable var1) {
      super.fallback(var1);
      return this;
   }

   public GifRequestBuilder fitCenter() {
      return this.transformFrame(new BitmapTransformation[]{this.glide.getBitmapFitCenter()});
   }

   public GifRequestBuilder listener(RequestListener var1) {
      super.listener(var1);
      return this;
   }

   public GifRequestBuilder load(Object var1) {
      super.load(var1);
      return this;
   }

   public GifRequestBuilder override(int var1, int var2) {
      super.override(var1, var2);
      return this;
   }

   public GifRequestBuilder placeholder(int var1) {
      super.placeholder(var1);
      return this;
   }

   public GifRequestBuilder placeholder(Drawable var1) {
      super.placeholder(var1);
      return this;
   }

   public GifRequestBuilder priority(Priority var1) {
      super.priority(var1);
      return this;
   }

   public GifRequestBuilder signature(Key var1) {
      super.signature(var1);
      return this;
   }

   public GifRequestBuilder sizeMultiplier(float var1) {
      super.sizeMultiplier(var1);
      return this;
   }

   public GifRequestBuilder skipMemoryCache(boolean var1) {
      super.skipMemoryCache(var1);
      return this;
   }

   public GifRequestBuilder sourceEncoder(Encoder var1) {
      super.sourceEncoder(var1);
      return this;
   }

   public GifRequestBuilder thumbnail(float var1) {
      super.thumbnail(var1);
      return this;
   }

   public GifRequestBuilder thumbnail(GenericRequestBuilder var1) {
      super.thumbnail(var1);
      return this;
   }

   public GifRequestBuilder thumbnail(GifRequestBuilder var1) {
      super.thumbnail(var1);
      return this;
   }

   public GifRequestBuilder transcoder(ResourceTranscoder var1) {
      super.transcoder(var1);
      return this;
   }

   public GifRequestBuilder transform(Transformation... var1) {
      super.transform(var1);
      return this;
   }

   public GifRequestBuilder transformFrame(Transformation... var1) {
      return this.transform(this.toGifTransformations(var1));
   }

   public GifRequestBuilder transformFrame(BitmapTransformation... var1) {
      return this.transform(this.toGifTransformations(var1));
   }
}
