package com.bumptech.glide;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GenericTranscodeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorModelLoader;
import com.bumptech.glide.load.model.stream.MediaStoreStreamLoader;
import com.bumptech.glide.load.model.stream.StreamByteArrayLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.signature.ApplicationVersionSignature;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.StringSignature;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.UUID;

public class RequestManager implements LifecycleListener {
   private final Context context;
   private final Glide glide;
   private final Lifecycle lifecycle;
   private RequestManager.DefaultOptions options;
   private final RequestManager.OptionsApplier optionsApplier;
   private final RequestTracker requestTracker;
   private final RequestManagerTreeNode treeNode;

   public RequestManager(Context var1, Lifecycle var2, RequestManagerTreeNode var3) {
      this(var1, var2, var3, new RequestTracker(), new ConnectivityMonitorFactory());
   }

   RequestManager(Context var1, final Lifecycle var2, RequestManagerTreeNode var3, RequestTracker var4, ConnectivityMonitorFactory var5) {
      this.context = var1.getApplicationContext();
      this.lifecycle = var2;
      this.treeNode = var3;
      this.requestTracker = var4;
      this.glide = Glide.get(var1);
      this.optionsApplier = new RequestManager.OptionsApplier();
      ConnectivityMonitor var6 = var5.build(var1, new RequestManager.RequestManagerConnectivityListener(var4));
      if(Util.isOnBackgroundThread()) {
         (new Handler(Looper.getMainLooper())).post(new Runnable() {
            public void run() {
               var2.addListener(RequestManager.this);
            }
         });
      } else {
         var2.addListener(this);
      }

      var2.addListener(var6);
   }

   private static Class getSafeClass(Object var0) {
      Class var1;
      if(var0 != null) {
         var1 = var0.getClass();
      } else {
         var1 = null;
      }

      return var1;
   }

   private DrawableTypeRequest loadGeneric(Class var1) {
      ModelLoader var2 = Glide.buildStreamModelLoader(var1, this.context);
      ModelLoader var3 = Glide.buildFileDescriptorModelLoader(var1, this.context);
      if(var1 != null && var2 == null && var3 == null) {
         throw new IllegalArgumentException("Unknown type " + var1 + ". You must provide a Model of a type for" + " which there is a registered ModelLoader, if you are using a custom model, you must first call" + " Glide#register with a ModelLoaderFactory for your custom model class");
      } else {
         return (DrawableTypeRequest)this.optionsApplier.apply(new DrawableTypeRequest(var1, var2, var3, this.context, this.glide, this.requestTracker, this.lifecycle, this.optionsApplier));
      }
   }

   public DrawableTypeRequest from(Class var1) {
      return this.loadGeneric(var1);
   }

   public DrawableTypeRequest fromBytes() {
      return (DrawableTypeRequest)this.loadGeneric(byte[].class).signature(new StringSignature(UUID.randomUUID().toString())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
   }

   public DrawableTypeRequest fromFile() {
      return this.loadGeneric(File.class);
   }

   public DrawableTypeRequest fromMediaStore() {
      ModelLoader var1 = Glide.buildStreamModelLoader(Uri.class, this.context);
      MediaStoreStreamLoader var3 = new MediaStoreStreamLoader(this.context, var1);
      ModelLoader var2 = Glide.buildFileDescriptorModelLoader(Uri.class, this.context);
      return (DrawableTypeRequest)this.optionsApplier.apply(new DrawableTypeRequest(Uri.class, var3, var2, this.context, this.glide, this.requestTracker, this.lifecycle, this.optionsApplier));
   }

   public DrawableTypeRequest fromResource() {
      return (DrawableTypeRequest)this.loadGeneric(Integer.class).signature(ApplicationVersionSignature.obtain(this.context));
   }

   public DrawableTypeRequest fromString() {
      return this.loadGeneric(String.class);
   }

   public DrawableTypeRequest fromUri() {
      return this.loadGeneric(Uri.class);
   }

   @Deprecated
   public DrawableTypeRequest fromUrl() {
      return this.loadGeneric(URL.class);
   }

   public boolean isPaused() {
      Util.assertMainThread();
      return this.requestTracker.isPaused();
   }

   public DrawableTypeRequest load(Uri var1) {
      return (DrawableTypeRequest)this.fromUri().load(var1);
   }

   public DrawableTypeRequest load(File var1) {
      return (DrawableTypeRequest)this.fromFile().load(var1);
   }

   public DrawableTypeRequest load(Integer var1) {
      return (DrawableTypeRequest)this.fromResource().load(var1);
   }

   public DrawableTypeRequest load(Object var1) {
      return (DrawableTypeRequest)this.loadGeneric(getSafeClass(var1)).load(var1);
   }

   public DrawableTypeRequest load(String var1) {
      return (DrawableTypeRequest)this.fromString().load(var1);
   }

   @Deprecated
   public DrawableTypeRequest load(URL var1) {
      return (DrawableTypeRequest)this.fromUrl().load(var1);
   }

   public DrawableTypeRequest load(byte[] var1) {
      return (DrawableTypeRequest)this.fromBytes().load(var1);
   }

   @Deprecated
   public DrawableTypeRequest load(byte[] var1, String var2) {
      return (DrawableTypeRequest)this.load(var1).signature(new StringSignature(var2));
   }

   public DrawableTypeRequest loadFromMediaStore(Uri var1) {
      return (DrawableTypeRequest)this.fromMediaStore().load(var1);
   }

   @Deprecated
   public DrawableTypeRequest loadFromMediaStore(Uri var1, String var2, long var3, int var5) {
      MediaStoreSignature var6 = new MediaStoreSignature(var2, var3, var5);
      return (DrawableTypeRequest)this.loadFromMediaStore(var1).signature(var6);
   }

   public void onDestroy() {
      this.requestTracker.clearRequests();
   }

   public void onLowMemory() {
      this.glide.clearMemory();
   }

   public void onStart() {
      this.resumeRequests();
   }

   public void onStop() {
      this.pauseRequests();
   }

   public void onTrimMemory(int var1) {
      this.glide.trimMemory(var1);
   }

   public void pauseRequests() {
      Util.assertMainThread();
      this.requestTracker.pauseRequests();
   }

   public void pauseRequestsRecursive() {
      Util.assertMainThread();
      this.pauseRequests();
      Iterator var1 = this.treeNode.getDescendants().iterator();

      while(var1.hasNext()) {
         ((RequestManager)var1.next()).pauseRequests();
      }

   }

   public void resumeRequests() {
      Util.assertMainThread();
      this.requestTracker.resumeRequests();
   }

   public void resumeRequestsRecursive() {
      Util.assertMainThread();
      this.resumeRequests();
      Iterator var1 = this.treeNode.getDescendants().iterator();

      while(var1.hasNext()) {
         ((RequestManager)var1.next()).resumeRequests();
      }

   }

   public void setDefaultOptions(RequestManager.DefaultOptions var1) {
      this.options = var1;
   }

   public RequestManager.GenericModelRequest using(ModelLoader var1, Class var2) {
      return new RequestManager.GenericModelRequest(var1, var2);
   }

   public RequestManager.ImageModelRequest using(StreamByteArrayLoader var1) {
      return new RequestManager.ImageModelRequest(var1);
   }

   public RequestManager.ImageModelRequest using(StreamModelLoader var1) {
      return new RequestManager.ImageModelRequest(var1);
   }

   public RequestManager.VideoModelRequest using(FileDescriptorModelLoader var1) {
      return new RequestManager.VideoModelRequest(var1);
   }

   public interface DefaultOptions {
      void apply(GenericRequestBuilder var1);
   }

   public final class GenericModelRequest {
      private final Class dataClass;
      private final ModelLoader modelLoader;

      GenericModelRequest(ModelLoader var2, Class var3) {
         this.modelLoader = var2;
         this.dataClass = var3;
      }

      public RequestManager.GenericTypeRequest from(Class var1) {
         return new RequestManager.GenericTypeRequest(var1);
      }

      public RequestManager.GenericTypeRequest load(Object var1) {
         return new RequestManager.GenericTypeRequest(var1);
      }
   }

   public final class GenericTypeRequest {
      private final Object model;
      private final Class modelClass;
      private final boolean providedModel;

      GenericTypeRequest(Class var2) {
         this.providedModel = false;
         this.model = null;
         this.modelClass = var2;
      }

      GenericTypeRequest(Object var2) {
         this.providedModel = true;
         this.model = var2;
         this.modelClass = RequestManager.getSafeClass(var2);
      }

      public GenericTranscodeRequest as(Class var1) {
         GenericTranscodeRequest var2 = (GenericTranscodeRequest)RequestManager.super.this$0.optionsApplier.apply(new GenericTranscodeRequest(RequestManager.super.this$0.context, RequestManager.super.this$0.glide, this.modelClass, RequestManager.super.modelLoader, RequestManager.super.dataClass, var1, RequestManager.super.this$0.requestTracker, RequestManager.super.this$0.lifecycle, RequestManager.super.this$0.optionsApplier));
         if(this.providedModel) {
            var2.load(this.model);
         }

         return var2;
      }
   }

   public final class ImageModelRequest {
      private final ModelLoader loader;

      ImageModelRequest(ModelLoader var2) {
         this.loader = var2;
      }

      public DrawableTypeRequest from(Class var1) {
         return (DrawableTypeRequest)RequestManager.this.optionsApplier.apply(new DrawableTypeRequest(var1, this.loader, (ModelLoader)null, RequestManager.this.context, RequestManager.this.glide, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier));
      }

      public DrawableTypeRequest load(Object var1) {
         return (DrawableTypeRequest)this.from(RequestManager.getSafeClass(var1)).load(var1);
      }
   }

   class OptionsApplier {
      public GenericRequestBuilder apply(GenericRequestBuilder var1) {
         if(RequestManager.this.options != null) {
            RequestManager.this.options.apply(var1);
         }

         return var1;
      }
   }

   private static class RequestManagerConnectivityListener implements ConnectivityMonitor.ConnectivityListener {
      private final RequestTracker requestTracker;

      public RequestManagerConnectivityListener(RequestTracker var1) {
         this.requestTracker = var1;
      }

      public void onConnectivityChanged(boolean var1) {
         if(var1) {
            this.requestTracker.restartRequests();
         }

      }
   }

   public final class VideoModelRequest {
      private final ModelLoader loader;

      VideoModelRequest(ModelLoader var2) {
         this.loader = var2;
      }

      public DrawableTypeRequest load(Object var1) {
         return (DrawableTypeRequest)((DrawableTypeRequest)RequestManager.this.optionsApplier.apply(new DrawableTypeRequest(RequestManager.getSafeClass(var1), (ModelLoader)null, this.loader, RequestManager.this.context, RequestManager.this.glide, RequestManager.this.requestTracker, RequestManager.this.lifecycle, RequestManager.this.optionsApplier))).load(var1);
      }
   }
}
