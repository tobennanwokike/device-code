package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.NullResourceEncoder;
import com.bumptech.glide.load.resource.file.FileDecoder;
import com.bumptech.glide.provider.DataLoadProvider;
import java.io.InputStream;

public class StreamFileDataLoadProvider implements DataLoadProvider {
   private static final StreamFileDataLoadProvider.ErrorSourceDecoder ERROR_DECODER = new StreamFileDataLoadProvider.ErrorSourceDecoder();
   private final ResourceDecoder cacheDecoder = new FileDecoder();
   private final Encoder encoder = new StreamEncoder();

   public ResourceDecoder getCacheDecoder() {
      return this.cacheDecoder;
   }

   public ResourceEncoder getEncoder() {
      return NullResourceEncoder.get();
   }

   public ResourceDecoder getSourceDecoder() {
      return ERROR_DECODER;
   }

   public Encoder getSourceEncoder() {
      return this.encoder;
   }

   private static class ErrorSourceDecoder implements ResourceDecoder {
      private ErrorSourceDecoder() {
      }

      // $FF: synthetic method
      ErrorSourceDecoder(Object var1) {
         this();
      }

      public Resource decode(InputStream var1, int var2, int var3) {
         throw new Error("You cannot decode a File from an InputStream by default, try either #diskCacheStratey(DiskCacheStrategy.SOURCE) to avoid this call or #decoder(ResourceDecoder) to replace this Decoder");
      }

      public String getId() {
         return "";
      }
   }
}
