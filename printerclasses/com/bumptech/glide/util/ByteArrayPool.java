package com.bumptech.glide.util;

import com.bumptech.glide.util.Util;
import java.util.Queue;

public final class ByteArrayPool {
   private static final ByteArrayPool BYTE_ARRAY_POOL = new ByteArrayPool();
   private static final int MAX_BYTE_ARRAY_COUNT = 32;
   private static final int MAX_SIZE = 2146304;
   private static final String TAG = "ByteArrayPool";
   private static final int TEMP_BYTES_SIZE = 65536;
   private final Queue tempQueue = Util.createQueue(0);

   public static ByteArrayPool get() {
      return BYTE_ARRAY_POOL;
   }

   public void clear() {
      // $FF: Couldn't be decompiled
   }

   public byte[] getBytes() {
      // $FF: Couldn't be decompiled
   }

   public boolean releaseBytes(byte[] param1) {
      // $FF: Couldn't be decompiled
   }
}
