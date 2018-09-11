package com.google.zxing;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import java.util.Hashtable;

public interface Reader {
   Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException;

   Result decode(BinaryBitmap var1, Hashtable var2) throws NotFoundException, ChecksumException, FormatException;

   void reset();
}
