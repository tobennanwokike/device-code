package com.bumptech.glide.request;

import com.bumptech.glide.request.target.Target;

public interface RequestListener {
   boolean onException(Exception var1, Object var2, Target var3, boolean var4);

   boolean onResourceReady(Object var1, Object var2, Target var3, boolean var4, boolean var5);
}
