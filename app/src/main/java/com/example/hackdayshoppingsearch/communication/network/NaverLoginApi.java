package com.example.hackdayshoppingsearch.communication.network;

import com.example.hackdayshoppingsearch.container.message.RefreshTokenMessage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NaverLoginApi {

    @GET("token")
    Call<RefreshTokenMessage> refreshAccessToken(@QueryMap Map<String, String> query);
}
