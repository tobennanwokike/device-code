package com.bumptech.glide;

import android.content.Context;
import com.bumptech.glide.DownloadOptions;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.provider.FixedLoadProvider;
import com.bumptech.glide.provider.LoadProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import java.io.File;

public class GenericTranscodeRequest extends GenericRequestBuilder implements DownloadOptions {
   private final Class dataClass;
   private final ModelLoader modelLoader;
   private final RequestManager.OptionsApplier optionsApplier;
   private final Class resourceClass;

   GenericTranscodeRequest(Context var1, Glide var2, Class var3, ModelLoader var4, Class var5, Class var6, RequestTracker var7, Lifecycle var8, RequestManager.OptionsApplier var9) {
      super(var1, var3, build(var2, var4, var5, var6, UnitTranscoder.get()), var6, var2, var7, var8);
      this.modelLoader = var4;
      this.dataClass = var5;
      this.resourceClass = var6;
      this.optionsApplier = var9;
   }

   GenericTranscodeRequest(Class var1, GenericRequestBuilder var2, ModelLoader var3, Class var4, Class var5, RequestManager.OptionsApplier var6) {
      super(build(var2.glide, var3, var4, var5, UnitTranscoder.get()), var1, var2);
      this.modelLoader = var3;
      this.dataClass = var4;
      this.resourceClass = var5;
      this.optionsApplier = var6;
   }

   private static LoadProvider build(Glide var0, ModelLoader var1, Class var2, Class var3, ResourceTranscoder var4) {
      return new FixedLoadProvider(var1, var4, var0.buildDataProvider(var2, var3));
   }

   private GenericRequestBuilder getDownloadOnlyRequest() {
      ResourceTranscoder var2 = UnitTranscoder.get();
      DataLoadProvider var1 = this.glide.buildDataProvider(this.dataClass, File.class);
      FixedLoadProvider var3 = new FixedLoadProvider(this.modelLoader, var2, var1);
      return this.optionsApplier.apply(new GenericRequestBuilder(var3, File.class, this)).priority(Priority.LOW).diskCacheStrategy(DiskCacheStrategy.SOURCE).skipMemoryCache(true);
   }

   public FutureTarget downloadOnly(int var1, int var2) {
      return this.getDownloadOnlyRequest().into(var1, var2);
   }

   public Target downloadOnly(Target var1) {
      return this.getDownloadOnlyRequest().into(var1);
   }

   public GenericRequestBuilder transcode(ResourceTranscoder var1, Class var2) {
      LoadProvider var3 = build(this.glide, this.modelLoader, this.dataClass, this.resourceClass, var1);
      return this.optionsApplier.apply(new GenericRequestBuilder(var3, var2, this));
   }
}
