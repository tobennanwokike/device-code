package com.bumptech.glide;

import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

interface DownloadOptions {
   FutureTarget downloadOnly(int var1, int var2);

   Target downloadOnly(Target var1);
}
