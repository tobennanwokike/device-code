package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public final class EmptySignature implements Key {
   private static final EmptySignature EMPTY_KEY = new EmptySignature();

   public static EmptySignature obtain() {
      return EMPTY_KEY;
   }

   public void updateDiskCacheKey(MessageDigest var1) throws UnsupportedEncodingException {
   }
}
