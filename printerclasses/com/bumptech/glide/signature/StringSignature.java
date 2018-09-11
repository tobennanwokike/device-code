package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class StringSignature implements Key {
   private final String signature;

   public StringSignature(String var1) {
      if(var1 == null) {
         throw new NullPointerException("Signature cannot be null!");
      } else {
         this.signature = var1;
      }
   }

   public boolean equals(Object var1) {
      boolean var2;
      if(this == var1) {
         var2 = true;
      } else if(var1 != null && this.getClass() == var1.getClass()) {
         StringSignature var3 = (StringSignature)var1;
         var2 = this.signature.equals(var3.signature);
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return this.signature.hashCode();
   }

   public String toString() {
      return "StringSignature{signature=\'" + this.signature + '\'' + '}';
   }

   public void updateDiskCacheKey(MessageDigest var1) throws UnsupportedEncodingException {
      var1.update(this.signature.getBytes("UTF-8"));
   }
}
