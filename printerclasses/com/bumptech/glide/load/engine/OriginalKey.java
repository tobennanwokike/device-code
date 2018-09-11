package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

class OriginalKey implements Key {
   private final String id;
   private final Key signature;

   public OriginalKey(String var1, Key var2) {
      this.id = var1;
      this.signature = var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if(this != var1) {
         if(var1 != null && this.getClass() == var1.getClass()) {
            OriginalKey var3 = (OriginalKey)var1;
            if(!this.id.equals(var3.id)) {
               var2 = false;
            } else if(!this.signature.equals(var3.signature)) {
               var2 = false;
            }
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public int hashCode() {
      return this.id.hashCode() * 31 + this.signature.hashCode();
   }

   public void updateDiskCacheKey(MessageDigest var1) throws UnsupportedEncodingException {
      var1.update(this.id.getBytes("UTF-8"));
      this.signature.updateDiskCacheKey(var1);
   }
}
