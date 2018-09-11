package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import java.io.OutputStream;

public class ImageVideoWrapperEncoder implements Encoder {
   private final Encoder fileDescriptorEncoder;
   private String id;
   private final Encoder streamEncoder;

   public ImageVideoWrapperEncoder(Encoder var1, Encoder var2) {
      this.streamEncoder = var1;
      this.fileDescriptorEncoder = var2;
   }

   public boolean encode(ImageVideoWrapper var1, OutputStream var2) {
      boolean var3;
      if(var1.getStream() != null) {
         var3 = this.streamEncoder.encode(var1.getStream(), var2);
      } else {
         var3 = this.fileDescriptorEncoder.encode(var1.getFileDescriptor(), var2);
      }

      return var3;
   }

   public String getId() {
      if(this.id == null) {
         this.id = this.streamEncoder.getId() + this.fileDescriptorEncoder.getId();
      }

      return this.id;
   }
}
