package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class MediaStoreSignature implements Key {
   private final long dateModified;
   private final String mimeType;
   private final int orientation;

   public MediaStoreSignature(String var1, long var2, int var4) {
      this.mimeType = var1;
      this.dateModified = var2;
      this.orientation = var4;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if(this != var1) {
         if(var1 != null && this.getClass() == var1.getClass()) {
            MediaStoreSignature var3 = (MediaStoreSignature)var1;
            if(this.dateModified != var3.dateModified) {
               var2 = false;
            } else if(this.orientation != var3.orientation) {
               var2 = false;
            } else {
               if(this.mimeType != null) {
                  if(this.mimeType.equals(var3.mimeType)) {
                     return var2;
                  }
               } else if(var3.mimeType == null) {
                  return var2;
               }

               var2 = false;
            }
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public int hashCode() {
      int var1;
      if(this.mimeType != null) {
         var1 = this.mimeType.hashCode();
      } else {
         var1 = 0;
      }

      return (var1 * 31 + (int)(this.dateModified ^ this.dateModified >>> 32)) * 31 + this.orientation;
   }

   public void updateDiskCacheKey(MessageDigest var1) throws UnsupportedEncodingException {
      var1.update(ByteBuffer.allocate(12).putLong(this.dateModified).putInt(this.orientation).array());
      var1.update(this.mimeType.getBytes("UTF-8"));
   }
}
