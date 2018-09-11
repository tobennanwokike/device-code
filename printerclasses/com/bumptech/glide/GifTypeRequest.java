package com.bumptech.glide;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.FixedLoadProvider;
import java.io.InputStream;

public class GifTypeRequest extends GifRequestBuilder {
   private final RequestManager.OptionsApplier optionsApplier;
   private final ModelLoader streamModelLoader;

   GifTypeRequest(GenericRequestBuilder var1, ModelLoader var2, RequestManager.OptionsApplier var3) {
      super(buildProvider(var1.glide, var2, GifDrawable.class, (ResourceTranscoder)null), GifDrawable.class, var1);
      this.streamModelLoader = var2;
      this.optionsApplier = var3;
      this.crossFade();
   }

   private static FixedLoadProvider buildProvider(Glide var0, ModelLoader var1, Class var2, ResourceTranscoder var3) {
      FixedLoadProvider var5;
      if(var1 == null) {
         var5 = null;
      } else {
         ResourceTranscoder var4 = var3;
         if(var3 == null) {
            var4 = var0.buildTranscoder(GifDrawable.class, var2);
         }

         var5 = new FixedLoadProvider(var1, var4, var0.buildDataProvider(InputStream.class, GifDrawable.class));
      }

      return var5;
   }

   public GenericRequestBuilder toBytes() {
      return this.transcode(new GifDrawableBytesTranscoder(), byte[].class);
   }

   public GenericRequestBuilder transcode(ResourceTranscoder var1, Class var2) {
      FixedLoadProvider var3 = buildProvider(this.glide, this.streamModelLoader, var2, var1);
      return this.optionsApplier.apply(new GenericRequestBuilder(var3, var2, this));
   }
}
