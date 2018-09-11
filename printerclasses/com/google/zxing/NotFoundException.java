package com.google.zxing;

import com.google.zxing.ReaderException;

public final class NotFoundException extends ReaderException {
   private static final NotFoundException instance = new NotFoundException();

   public static NotFoundException getNotFoundInstance() {
      return instance;
   }
}
