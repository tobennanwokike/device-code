package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;

public interface DataLoadProvider {
   ResourceDecoder getCacheDecoder();

   ResourceEncoder getEncoder();

   ResourceDecoder getSourceDecoder();

   Encoder getSourceEncoder();
}
