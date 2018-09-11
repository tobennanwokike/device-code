package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.provider.LoadProvider;

public class FixedLoadProvider implements LoadProvider {
   private final DataLoadProvider dataLoadProvider;
   private final ModelLoader modelLoader;
   private final ResourceTranscoder transcoder;

   public FixedLoadProvider(ModelLoader var1, ResourceTranscoder var2, DataLoadProvider var3) {
      if(var1 == null) {
         throw new NullPointerException("ModelLoader must not be null");
      } else {
         this.modelLoader = var1;
         if(var2 == null) {
            throw new NullPointerException("Transcoder must not be null");
         } else {
            this.transcoder = var2;
            if(var3 == null) {
               throw new NullPointerException("DataLoadProvider must not be null");
            } else {
               this.dataLoadProvider = var3;
            }
         }
      }
   }

   public ResourceDecoder getCacheDecoder() {
      return this.dataLoadProvider.getCacheDecoder();
   }

   public ResourceEncoder getEncoder() {
      return this.dataLoadProvider.getEncoder();
   }

   public ModelLoader getModelLoader() {
      return this.modelLoader;
   }

   public ResourceDecoder getSourceDecoder() {
      return this.dataLoadProvider.getSourceDecoder();
   }

   public Encoder getSourceEncoder() {
      return this.dataLoadProvider.getSourceEncoder();
   }

   public ResourceTranscoder getTranscoder() {
      return this.transcoder;
   }
}
