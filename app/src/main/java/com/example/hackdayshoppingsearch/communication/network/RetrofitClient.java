package com.example.hackdayshoppingsearch.communication.network;

import android.net.http.HttpResponseCache;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Map<String, Retrofit> retrofitMap = new HashMap<>();
    private static File cacheDir;
    private static int cacheSize;

    public static void setCache(File _cacheDir, int _cacheSize) {
        cacheDir = _cacheDir;
        cacheSize = _cacheSize;
    }

    public static Retrofit getInstance(String baseUrl) {
        Retrofit retrofit = retrofitMap.get(baseUrl);
        if (retrofit==null) {
            synchronized (Integer.valueOf(baseUrl.hashCode())) {
                if(retrofit == null) {
                    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create());
                    if(cacheDir != null) {
                        Cache cache = new Cache(cacheDir, cacheSize);
                        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                .cache(cache)
                                .build();
                        retrofitBuilder.client(okHttpClient);
                    }
                    retrofit = retrofitBuilder.build();
                    retrofitMap.put(baseUrl, retrofit);
                }
            }
        }
        return retrofit;
    }
}
