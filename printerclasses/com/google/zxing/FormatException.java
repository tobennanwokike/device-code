package com.google.zxing;

import com.google.zxing.ReaderException;

public final class FormatException extends ReaderException {
   private static final FormatException instance = new FormatException();

   public static FormatException getFormatInstance() {
      return instance;
   }
}
