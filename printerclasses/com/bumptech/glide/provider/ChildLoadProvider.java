package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.LoadProvider;

public class ChildLoadProvider implements LoadProvider, Cloneable {
   private ResourceDecoder cacheDecoder;
   private ResourceEncoder encoder;
   private final LoadProvider parent;
   private ResourceDecoder sourceDecoder;
   private Encoder sourceEncoder;
   private ResourceTranscoder transcoder;

   public ChildLoadProvider(LoadProvider var1) {
      this.parent = var1;
   }

   public ChildLoadProvider clone() {
      try {
         ChildLoadProvider var1 = (ChildLoadProvider)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public ResourceDecoder getCacheDecoder() {
      ResourceDecoder var1;
      if(this.cacheDecoder != null) {
         var1 = this.cacheDecoder;
      } else {
         var1 = this.parent.getCacheDecoder();
      }

      return var1;
   }

   public ResourceEncoder getEncoder() {
      ResourceEncoder var1;
      if(this.encoder != null) {
         var1 = this.encoder;
      } else {
         var1 = this.parent.getEncoder();
      }

      return var1;
   }

   public ModelLoader getModelLoader() {
      return this.parent.getModelLoader();
   }

   public ResourceDecoder getSourceDecoder() {
      ResourceDecoder var1;
      if(this.sourceDecoder != null) {
         var1 = this.sourceDecoder;
      } else {
         var1 = this.parent.getSourceDecoder();
      }

      return var1;
   }

   public Encoder getSourceEncoder() {
      Encoder var1;
      if(this.sourceEncoder != null) {
         var1 = this.sourceEncoder;
      } else {
         var1 = this.parent.getSourceEncoder();
      }

      return var1;
   }

   public ResourceTranscoder getTranscoder() {
      ResourceTranscoder var1;
      if(this.transcoder != null) {
         var1 = this.transcoder;
      } else {
         var1 = this.parent.getTranscoder();
      }

      return var1;
   }

   public void setCacheDecoder(ResourceDecoder var1) {
      this.cacheDecoder = var1;
   }

   public void setEncoder(ResourceEncoder var1) {
      this.encoder = var1;
   }

   public void setSourceDecoder(ResourceDecoder var1) {
      this.sourceDecoder = var1;
   }

   public void setSourceEncoder(Encoder var1) {
      this.sourceEncoder = var1;
   }

   public void setTranscoder(ResourceTranscoder var1) {
      this.transcoder = var1;
   }
}
