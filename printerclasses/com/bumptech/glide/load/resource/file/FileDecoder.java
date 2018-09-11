package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.file.FileResource;
import java.io.File;

public class FileDecoder implements ResourceDecoder {
   public Resource decode(File var1, int var2, int var3) {
      return new FileResource(var1);
   }

   public String getId() {
      return "";
   }
}
