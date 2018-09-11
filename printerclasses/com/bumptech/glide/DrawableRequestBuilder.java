package com.bumptech.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.bumptech.glide.BitmapOptions;
import com.bumptech.glide.DrawableOptions;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperTransformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;

public class DrawableRequestBuilder extends GenericRequestBuilder implements BitmapOptions, DrawableOptions {
   DrawableRequestBuilder(Context var1, Class var2, LoadProvider var3, Glide var4, RequestTracker var5, Lifecycle var6) {
      super(var1, var2, var3, GlideDrawable.class, var4, var5, var6);
      this.crossFade();
   }

   public DrawableRequestBuilder animate(int var1) {
      super.animate(var1);
      return this;
   }

   @Deprecated
   public DrawableRequestBuilder animate(Animation var1) {
      super.animate(var1);
      return this;
   }

   public DrawableRequestBuilder animate(ViewPropertyAnimation.Animator var1) {
      super.animate(var1);
      return this;
   }

   void applyCenterCrop() {
      this.centerCrop();
   }

   void applyFitCenter() {
      this.fitCenter();
   }

   public DrawableRequestBuilder bitmapTransform(Transformation... var1) {
      GifBitmapWrapperTransformation[] var3 = new GifBitmapWrapperTransformation[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var3[var2] = new GifBitmapWrapperTransformation(this.glide.getBitmapPool(), var1[var2]);
      }

      return this.transform((Transformation[])var3);
   }

   public DrawableRequestBuilder cacheDecoder(ResourceDecoder var1) {
      super.cacheDecoder(var1);
      return this;
   }

   public DrawableRequestBuilder centerCrop() {
      return this.transform(new Transformation[]{this.glide.getDrawableCenterCrop()});
   }

   public DrawableRequestBuilder clone() {
      return (DrawableRequestBuilder)super.clone();
   }

   public final DrawableRequestBuilder crossFade() {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory()));
      return this;
   }

   public DrawableRequestBuilder crossFade(int var1) {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory(var1)));
      return this;
   }

   public DrawableRequestBuilder crossFade(int var1, int var2) {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory(this.context, var1, var2)));
      return this;
   }

   @Deprecated
   public DrawableRequestBuilder crossFade(Animation var1, int var2) {
      super.animate((GlideAnimationFactory)(new DrawableCrossFadeFactory(var1, var2)));
      return this;
   }

   public DrawableRequestBuilder decoder(ResourceDecoder var1) {
      super.decoder(var1);
      return this;
   }

   public DrawableRequestBuilder diskCacheStrategy(DiskCacheStrategy var1) {
      super.diskCacheStrategy(var1);
      return this;
   }

   public DrawableRequestBuilder dontAnimate() {
      super.dontAnimate();
      return this;
   }

   public DrawableRequestBuilder dontTransform() {
      super.dontTransform();
      return this;
   }

   public DrawableRequestBuilder encoder(ResourceEncoder var1) {
      super.encoder(var1);
      return this;
   }

   public DrawableRequestBuilder error(int var1) {
      super.error(var1);
      return this;
   }

   public DrawableRequestBuilder error(Drawable var1) {
      super.error(var1);
      return this;
   }

   public DrawableRequestBuilder fallback(int var1) {
      super.fallback(var1);
      return this;
   }

   public DrawableRequestBuilder fallback(Drawable var1) {
      super.fallback(var1);
      return this;
   }

   public DrawableRequestBuilder fitCenter() {
      return this.transform(new Transformation[]{this.glide.getDrawableFitCenter()});
   }

   public Target into(ImageView var1) {
      return super.into(var1);
   }

   public DrawableRequestBuilder listener(RequestListener var1) {
      super.listener(var1);
      return this;
   }

   public DrawableRequestBuilder load(Object var1) {
      super.load(var1);
      return this;
   }

   public DrawableRequestBuilder override(int var1, int var2) {
      super.override(var1, var2);
      return this;
   }

   public DrawableRequestBuilder placeholder(int var1) {
      super.placeholder(var1);
      return this;
   }

   public DrawableRequestBuilder placeholder(Drawable var1) {
      super.placeholder(var1);
      return this;
   }

   public DrawableRequestBuilder priority(Priority var1) {
      super.priority(var1);
      return this;
   }

   public DrawableRequestBuilder signature(Key var1) {
      super.signature(var1);
      return this;
   }

   public DrawableRequestBuilder sizeMultiplier(float var1) {
      super.sizeMultiplier(var1);
      return this;
   }

   public DrawableRequestBuilder skipMemoryCache(boolean var1) {
      super.skipMemoryCache(var1);
      return this;
   }

   public DrawableRequestBuilder sourceEncoder(Encoder var1) {
      super.sourceEncoder(var1);
      return this;
   }

   public DrawableRequestBuilder thumbnail(float var1) {
      super.thumbnail(var1);
      return this;
   }

   public DrawableRequestBuilder thumbnail(DrawableRequestBuilder var1) {
      super.thumbnail(var1);
      return this;
   }

   public DrawableRequestBuilder thumbnail(GenericRequestBuilder var1) {
      super.thumbnail(var1);
      return this;
   }

   public DrawableRequestBuilder transcoder(ResourceTranscoder var1) {
      super.transcoder(var1);
      return this;
   }

   public DrawableRequestBuilder transform(Transformation... var1) {
      super.transform(var1);
      return this;
   }

   public DrawableRequestBuilder transform(BitmapTransformation... var1) {
      return this.bitmapTransform(var1);
   }
}
