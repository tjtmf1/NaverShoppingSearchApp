package com.example.hackdayshoppingsearch.communication.network;

public class NaverApiClient {
    private final static String naverApiBaseUrl = "https://openapi.naver.com/v1/";
    private final static String naverLoginApiBaseUrl = "https://nid.naver.com/oauth2.0/";

    public static NaverApi getNaverApiClient() {
        return RetrofitClient.getInstance(naverApiBaseUrl).create(NaverApi.class);
    }

    public static NaverLoginApi getNaverLoginApiClient() {
        return RetrofitClient.getInstance(naverLoginApiBaseUrl).create(NaverLoginApi.class);
    }
}
