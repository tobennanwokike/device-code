package com.bumptech.glide.provider;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.provider.DataLoadProvider;

public interface LoadProvider extends DataLoadProvider {
   ModelLoader getModelLoader();

   ResourceTranscoder getTranscoder();
}
