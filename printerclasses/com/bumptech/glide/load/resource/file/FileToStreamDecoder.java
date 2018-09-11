package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileToStreamDecoder implements ResourceDecoder {
   private static final FileToStreamDecoder.FileOpener DEFAULT_FILE_OPENER = new FileToStreamDecoder.FileOpener();
   private final FileToStreamDecoder.FileOpener fileOpener;
   private ResourceDecoder streamDecoder;

   public FileToStreamDecoder(ResourceDecoder var1) {
      this(var1, DEFAULT_FILE_OPENER);
   }

   FileToStreamDecoder(ResourceDecoder var1, FileToStreamDecoder.FileOpener var2) {
      this.streamDecoder = var1;
      this.fileOpener = var2;
   }

   public Resource decode(File param1, int param2, int param3) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public String getId() {
      return "";
   }

   static class FileOpener {
      public InputStream open(File var1) throws FileNotFoundException {
         return new FileInputStream(var1);
      }
   }
}
