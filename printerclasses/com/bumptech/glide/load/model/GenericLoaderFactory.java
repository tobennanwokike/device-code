package com.bumptech.glide.load.model;

import android.content.Context;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenericLoaderFactory {
   private static final ModelLoader NULL_MODEL_LOADER = new ModelLoader() {
      public DataFetcher getResourceFetcher(Object var1, int var2, int var3) {
         throw new NoSuchMethodError("This should never be called!");
      }

      public String toString() {
         return "NULL_MODEL_LOADER";
      }
   };
   private final Map cachedModelLoaders = new HashMap();
   private final Context context;
   private final Map modelClassToResourceFactories = new HashMap();

   public GenericLoaderFactory(Context var1) {
      this.context = var1.getApplicationContext();
   }

   private void cacheModelLoader(Class var1, Class var2, ModelLoader var3) {
      Map var5 = (Map)this.cachedModelLoaders.get(var1);
      Object var4 = var5;
      if(var5 == null) {
         var4 = new HashMap();
         this.cachedModelLoaders.put(var1, var4);
      }

      ((Map)var4).put(var2, var3);
   }

   private void cacheNullLoader(Class var1, Class var2) {
      this.cacheModelLoader(var1, var2, NULL_MODEL_LOADER);
   }

   private ModelLoader getCachedLoader(Class var1, Class var2) {
      Map var3 = (Map)this.cachedModelLoaders.get(var1);
      ModelLoader var4 = null;
      if(var3 != null) {
         var4 = (ModelLoader)var3.get(var2);
      }

      return var4;
   }

   private ModelLoaderFactory getFactory(Class var1, Class var2) {
      Map var4 = (Map)this.modelClassToResourceFactories.get(var1);
      ModelLoaderFactory var3 = null;
      if(var4 != null) {
         var3 = (ModelLoaderFactory)var4.get(var2);
      }

      ModelLoaderFactory var7 = var3;
      if(var3 == null) {
         Iterator var5 = this.modelClassToResourceFactories.keySet().iterator();

         while(true) {
            var7 = var3;
            if(!var5.hasNext()) {
               break;
            }

            Class var6 = (Class)var5.next();
            if(var6.isAssignableFrom(var1)) {
               var4 = (Map)this.modelClassToResourceFactories.get(var6);
               if(var4 != null) {
                  var7 = (ModelLoaderFactory)var4.get(var2);
                  var3 = var7;
                  if(var7 != null) {
                     break;
                  }
               }
            }
         }
      }

      return var7;
   }

   public ModelLoader buildModelLoader(Class param1, Class param2) {
      // $FF: Couldn't be decompiled
   }

   @Deprecated
   public ModelLoader buildModelLoader(Class var1, Class var2, Context var3) {
      synchronized(this){}

      ModelLoader var6;
      try {
         var6 = this.buildModelLoader(var1, var2);
      } finally {
         ;
      }

      return var6;
   }

   public ModelLoaderFactory register(Class param1, Class param2, ModelLoaderFactory param3) {
      // $FF: Couldn't be decompiled
   }

   public ModelLoaderFactory unregister(Class param1, Class param2) {
      // $FF: Couldn't be decompiled
   }
}
