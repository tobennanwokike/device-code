package com.google.zxing;

import com.google.zxing.ReaderException;

public final class ChecksumException extends ReaderException {
   private static final ChecksumException instance = new ChecksumException();

   public static ChecksumException getChecksumInstance() {
      return instance;
   }
}
