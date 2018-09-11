package com.bumptech.glide.load;

import java.io.OutputStream;

public interface Encoder {
   boolean encode(Object var1, OutputStream var2);

   String getId();
}
