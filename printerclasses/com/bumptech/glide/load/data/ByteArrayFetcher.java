package com.bumptech.glide.load.data;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayFetcher implements DataFetcher {
   private final byte[] bytes;
   private final String id;

   public ByteArrayFetcher(byte[] var1, String var2) {
      this.bytes = var1;
      this.id = var2;
   }

   public void cancel() {
   }

   public void cleanup() {
   }

   public String getId() {
      return this.id;
   }

   public InputStream loadData(Priority var1) {
      return new ByteArrayInputStream(this.bytes);
   }
}
