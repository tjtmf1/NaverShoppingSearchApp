package com.example.hackdayshoppingsearch.communication.network;

import com.example.hackdayshoppingsearch.container.message.RetrieveTrendQueryMessage;
import com.example.hackdayshoppingsearch.container.message.RetrieveUserProfileMessage;
import com.example.hackdayshoppingsearch.container.message.ShoppingInsightCategoryTrendMessage;
import com.example.hackdayshoppingsearch.container.message.ShoppingSearchMessage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface NaverApi {
    String clientId = "7rQpj4I5zu9U_7vktMNP";
    String clientSecret = "ZI3pVa2MYW";

    @GET("search/shop.json")
    @Headers({
            "X-Naver-Client-Id: " + clientId,
            "X-Naver-Client-Secret: " + clientSecret,
            "Cache-Control: max-age=640000"
    })
    Call<ShoppingSearchMessage> doShoppingSearch(@QueryMap Map<String, String> query);

    @GET("nid/me")
    Call<RetrieveUserProfileMessage> retrieveUserProfile(@Header("Authorization") String authorization);

    @POST("datalab/shopping/categories")
    @Headers({
            "X-Naver-Client-Id: " + clientId,
            "X-Naver-Client-Secret: " + clientSecret,
            "Cache-Control: max-age=640000"
    })
    Call<ShoppingInsightCategoryTrendMessage> retrieveCategoryTrend(
            @Body RetrieveTrendQueryMessage retrieveTrendQueryMessage);


}
