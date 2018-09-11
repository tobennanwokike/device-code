package com.bumptech.glide;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.bumptech.glide.BitmapOptions;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;

public class BitmapRequestBuilder extends GenericRequestBuilder implements BitmapOptions {
   private final BitmapPool bitmapPool;
   private DecodeFormat decodeFormat;
   private Downsampler downsampler;
   private ResourceDecoder imageDecoder;
   private ResourceDecoder videoDecoder;

   BitmapRequestBuilder(LoadProvider var1, Class var2, GenericRequestBuilder var3) {
      super(var1, var2, var3);
      this.downsampler = Downsampler.AT_LEAST;
      this.bitmapPool = var3.glide.getBitmapPool();
      this.decodeFormat = var3.glide.getDecodeFormat();
      this.imageDecoder = new StreamBitmapDecoder(this.bitmapPool, this.decodeFormat);
      this.videoDecoder = new FileDescriptorBitmapDecoder(this.bitmapPool, this.decodeFormat);
   }

   private BitmapRequestBuilder downsample(Downsampler var1) {
      this.downsampler = var1;
      this.imageDecoder = new StreamBitmapDecoder(var1, this.bitmapPool, this.decodeFormat);
      super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, this.videoDecoder));
      return this;
   }

   public BitmapRequestBuilder animate(int var1) {
      super.animate(var1);
      return this;
   }

   @Deprecated
   public BitmapRequestBuilder animate(Animation var1) {
      super.animate(var1);
      return this;
   }

   public BitmapRequestBuilder animate(ViewPropertyAnimation.Animator var1) {
      super.animate(var1);
      return this;
   }

   void applyCenterCrop() {
      this.centerCrop();
   }

   void applyFitCenter() {
      this.fitCenter();
   }

   public BitmapRequestBuilder approximate() {
      return this.downsample(Downsampler.AT_LEAST);
   }

   public BitmapRequestBuilder asIs() {
      return this.downsample(Downsampler.NONE);
   }

   public BitmapRequestBuilder atMost() {
      return this.downsample(Downsampler.AT_MOST);
   }

   public BitmapRequestBuilder cacheDecoder(ResourceDecoder var1) {
      super.cacheDecoder(var1);
      return this;
   }

   public BitmapRequestBuilder centerCrop() {
      return this.transform(new BitmapTransformation[]{this.glide.getBitmapCenterCrop()});
   }

   public BitmapRequestBuilder clone() {
      return (BitmapRequestBuilder)super.clone();
   }

   public BitmapRequestBuilder decoder(ResourceDecoder var1) {
      super.decoder(var1);
      return this;
   }

   public BitmapRequestBuilder diskCacheStrategy(DiskCacheStrategy var1) {
      super.diskCacheStrategy(var1);
      return this;
   }

   public BitmapRequestBuilder dontAnimate() {
      super.dontAnimate();
      return this;
   }

   public BitmapRequestBuilder dontTransform() {
      super.dontTransform();
      return this;
   }

   public BitmapRequestBuilder encoder(ResourceEncoder var1) {
      super.encoder(var1);
      return this;
   }

   public BitmapRequestBuilder error(int var1) {
      super.error(var1);
      return this;
   }

   public BitmapRequestBuilder error(Drawable var1) {
      super.error(var1);
      return this;
   }

   public BitmapRequestBuilder fallback(int var1) {
      super.fallback(var1);
      return this;
   }

   public BitmapRequestBuilder fallback(Drawable var1) {
      super.fallback(var1);
      return this;
   }

   public BitmapRequestBuilder fitCenter() {
      return this.transform(new BitmapTransformation[]{this.glide.getBitmapFitCenter()});
   }

   public BitmapRequestBuilder format(DecodeFormat var1) {
      this.decodeFormat = var1;
      this.imageDecoder = new StreamBitmapDecoder(this.downsampler, this.bitmapPool, var1);
      this.videoDecoder = new FileDescriptorBitmapDecoder(new VideoBitmapDecoder(), this.bitmapPool, var1);
      super.cacheDecoder(new FileToStreamDecoder(new StreamBitmapDecoder(this.downsampler, this.bitmapPool, var1)));
      super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, this.videoDecoder));
      return this;
   }

   public BitmapRequestBuilder imageDecoder(ResourceDecoder var1) {
      this.imageDecoder = var1;
      super.decoder(new ImageVideoBitmapDecoder(var1, this.videoDecoder));
      return this;
   }

   public Target into(ImageView var1) {
      return super.into(var1);
   }

   public BitmapRequestBuilder listener(RequestListener var1) {
      super.listener(var1);
      return this;
   }

   public BitmapRequestBuilder load(Object var1) {
      super.load(var1);
      return this;
   }

   public BitmapRequestBuilder override(int var1, int var2) {
      super.override(var1, var2);
      return this;
   }

   public BitmapRequestBuilder placeholder(int var1) {
      super.placeholder(var1);
      return this;
   }

   public BitmapRequestBuilder placeholder(Drawable var1) {
      super.placeholder(var1);
      return this;
   }

   public BitmapRequestBuilder priority(Priority var1) {
      super.priority(var1);
      return this;
   }

   public BitmapRequestBuilder signature(Key var1) {
      super.signature(var1);
      return this;
   }

   public BitmapRequestBuilder sizeMultiplier(float var1) {
      super.sizeMultiplier(var1);
      return this;
   }

   public BitmapRequestBuilder skipMemoryCache(boolean var1) {
      super.skipMemoryCache(var1);
      return this;
   }

   public BitmapRequestBuilder sourceEncoder(Encoder var1) {
      super.sourceEncoder(var1);
      return this;
   }

   public BitmapRequestBuilder thumbnail(float var1) {
      super.thumbnail(var1);
      return this;
   }

   public BitmapRequestBuilder thumbnail(BitmapRequestBuilder var1) {
      super.thumbnail(var1);
      return this;
   }

   public BitmapRequestBuilder thumbnail(GenericRequestBuilder var1) {
      super.thumbnail(var1);
      return this;
   }

   public BitmapRequestBuilder transcoder(ResourceTranscoder var1) {
      super.transcoder(var1);
      return this;
   }

   public BitmapRequestBuilder transform(Transformation... var1) {
      super.transform(var1);
      return this;
   }

   public BitmapRequestBuilder transform(BitmapTransformation... var1) {
      super.transform(var1);
      return this;
   }

   public BitmapRequestBuilder videoDecoder(ResourceDecoder var1) {
      this.videoDecoder = var1;
      super.decoder(new ImageVideoBitmapDecoder(this.imageDecoder, var1));
      return this;
   }
}
