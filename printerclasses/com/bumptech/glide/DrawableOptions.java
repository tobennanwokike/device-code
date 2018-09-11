package com.bumptech.glide;

import android.view.animation.Animation;
import com.bumptech.glide.GenericRequestBuilder;

interface DrawableOptions {
   GenericRequestBuilder crossFade();

   GenericRequestBuilder crossFade(int var1);

   GenericRequestBuilder crossFade(int var1, int var2);

   @Deprecated
   GenericRequestBuilder crossFade(Animation var1, int var2);
}
