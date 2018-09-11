package com.bumptech.glide;

import android.content.Context;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DownloadOptions;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericTranscodeRequest;
import com.bumptech.glide.GifTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.ImageVideoModelLoader;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.provider.FixedLoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.io.InputStream;

public class DrawableTypeRequest extends DrawableRequestBuilder implements DownloadOptions {
   private final ModelLoader fileDescriptorModelLoader;
   private final RequestManager.OptionsApplier optionsApplier;
   private final ModelLoader streamModelLoader;

   DrawableTypeRequest(Class var1, ModelLoader var2, ModelLoader var3, Context var4, Glide var5, RequestTracker var6, Lifecycle var7, RequestManager.OptionsApplier var8) {
      super(var4, var1, buildProvider(var5, var2, var3, GifBitmapWrapper.class, GlideDrawable.class, (ResourceTranscoder)null), var5, var6, var7);
      this.streamModelLoader = var2;
      this.fileDescriptorModelLoader = var3;
      this.optionsApplier = var8;
   }

   private static FixedLoadProvider buildProvider(Glide var0, ModelLoader var1, ModelLoader var2, Class var3, Class var4, ResourceTranscoder var5) {
      FixedLoadProvider var8;
      if(var1 == null && var2 == null) {
         var8 = null;
      } else {
         ResourceTranscoder var6 = var5;
         if(var5 == null) {
            var6 = var0.buildTranscoder(var3, var4);
         }

         DataLoadProvider var7 = var0.buildDataProvider(ImageVideoWrapper.class, var3);
         var8 = new FixedLoadProvider(new ImageVideoModelLoader(var1, var2), var6, var7);
      }

      return var8;
   }

   private GenericTranscodeRequest getDownloadOnlyRequest() {
      return (GenericTranscodeRequest)this.optionsApplier.apply(new GenericTranscodeRequest(File.class, this, this.streamModelLoader, InputStream.class, File.class, this.optionsApplier));
   }

   public BitmapTypeRequest asBitmap() {
      return (BitmapTypeRequest)this.optionsApplier.apply(new BitmapTypeRequest(this, this.streamModelLoader, this.fileDescriptorModelLoader, this.optionsApplier));
   }

   public GifTypeRequest asGif() {
      return (GifTypeRequest)this.optionsApplier.apply(new GifTypeRequest(this, this.streamModelLoader, this.optionsApplier));
   }

   public FutureTarget downloadOnly(int var1, int var2) {
      return this.getDownloadOnlyRequest().downloadOnly(var1, var2);
   }

   public Target downloadOnly(Target var1) {
      return this.getDownloadOnlyRequest().downloadOnly(var1);
   }
}
