package com.bumptech.glide.load;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public interface Key {
   String STRING_CHARSET_NAME = "UTF-8";

   boolean equals(Object var1);

   int hashCode();

   void updateDiskCacheKey(MessageDigest var1) throws UnsupportedEncodingException;
}
