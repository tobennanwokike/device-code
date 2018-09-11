package com.bumptech.glide.provider;

import com.bumptech.glide.provider.DataLoadProvider;
import com.bumptech.glide.util.MultiClassKey;
import java.util.HashMap;
import java.util.Map;

public class DataLoadProviderRegistry {
   private static final MultiClassKey GET_KEY = new MultiClassKey();
   private final Map providers = new HashMap();

   public DataLoadProvider get(Class param1, Class param2) {
      // $FF: Couldn't be decompiled
   }

   public void register(Class var1, Class var2, DataLoadProvider var3) {
      this.providers.put(new MultiClassKey(var1, var2), var3);
   }
}
