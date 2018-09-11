package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.ImageVideoModelLoader;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.provider.FixedLoadProvider;

public class BitmapTypeRequest extends BitmapRequestBuilder {
   private final ModelLoader fileDescriptorModelLoader;
   private final Glide glide;
   private final RequestManager.OptionsApplier optionsApplier;
   private final ModelLoader streamModelLoader;

   BitmapTypeRequest(GenericRequestBuilder var1, ModelLoader var2, ModelLoader var3, RequestManager.OptionsApplier var4) {
      super(buildProvider(var1.glide, var2, var3, Bitmap.class, (ResourceTranscoder)null), Bitmap.class, var1);
      this.streamModelLoader = var2;
      this.fileDescriptorModelLoader = var3;
      this.glide = var1.glide;
      this.optionsApplier = var4;
   }

   private static FixedLoadProvider buildProvider(Glide var0, ModelLoader var1, ModelLoader var2, Class var3, ResourceTranscoder var4) {
      FixedLoadProvider var7;
      if(var1 == null && var2 == null) {
         var7 = null;
      } else {
         ResourceTranscoder var5 = var4;
         if(var4 == null) {
            var5 = var0.buildTranscoder(Bitmap.class, var3);
         }

         DataLoadProvider var6 = var0.buildDataProvider(ImageVideoWrapper.class, Bitmap.class);
         var7 = new FixedLoadProvider(new ImageVideoModelLoader(var1, var2), var5, var6);
      }

      return var7;
   }

   public BitmapRequestBuilder toBytes() {
      return this.transcode(new BitmapBytesTranscoder(), byte[].class);
   }

   public BitmapRequestBuilder toBytes(CompressFormat var1, int var2) {
      return this.transcode(new BitmapBytesTranscoder(var1, var2), byte[].class);
   }

   public BitmapRequestBuilder transcode(ResourceTranscoder var1, Class var2) {
      return (BitmapRequestBuilder)this.optionsApplier.apply(new BitmapRequestBuilder(buildProvider(this.glide, this.streamModelLoader, this.fileDescriptorModelLoader, var2, var1), var2, this));
   }
}
