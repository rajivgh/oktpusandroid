package com.app.oktpus.utils;

import android.content.Context;

import com.app.oktpus.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
/*import com.facebook.stetho.okhttp.StethoInterceptor;*/
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;

/**
 * Created by Gyandeep on 6/9/17.
 */

public class CustomGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                context.getResources().getInteger(R.integer.glide_diskcache_size)));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        OkHttpClient client = new OkHttpClient();
        //client.networkInterceptors().add(new StethoInterceptor());
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }
}
