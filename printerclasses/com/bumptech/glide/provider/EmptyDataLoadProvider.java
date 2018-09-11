package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.provider.DataLoadProvider;

public class EmptyDataLoadProvider implements DataLoadProvider {
   private static final DataLoadProvider EMPTY_DATA_LOAD_PROVIDER = new EmptyDataLoadProvider();

   public static DataLoadProvider get() {
      return EMPTY_DATA_LOAD_PROVIDER;
   }

   public ResourceDecoder getCacheDecoder() {
      return null;
   }

   public ResourceEncoder getEncoder() {
      return null;
   }

   public ResourceDecoder getSourceDecoder() {
      return null;
   }

   public Encoder getSourceEncoder() {
      return null;
   }
}
