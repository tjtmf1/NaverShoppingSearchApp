package com.example.hackdayshoppingsearch.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;

import com.example.hackdayshoppingsearch.container.LoginUser;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.container.message.RefreshTokenMessage;
import com.example.hackdayshoppingsearch.container.message.RefreshTokenQueryMessage;
import com.example.hackdayshoppingsearch.container.message.RetrieveUserProfileMessage;
import com.example.hackdayshoppingsearch.common.SharedPreferenceManager;
import com.example.hackdayshoppingsearch.container.UserProfile;
import com.example.hackdayshoppingsearch.communication.network.NaverApi;
import com.example.hackdayshoppingsearch.communication.network.NaverApiClient;
import com.example.hackdayshoppingsearch.communication.network.NaverLoginApi;
import com.example.hackdayshoppingsearch.communication.network.RetrofitClient;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements Callback<RefreshTokenMessage> {
    private final int EXPIRE_TIME_MILLISECOND = 1000;
    private boolean isTimeExpire = false;
    private boolean isRetrieveFinish = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setNetworkCache();
        setUserProfileIfExist();
        startTimer(EXPIRE_TIME_MILLISECOND);
    }

    private void setNetworkCache() {
        int cacheSize = 10 * 1024 * 1024;
        RetrofitClient.setCache(getCacheDir(), cacheSize);
    }

    private String getLoginRefreshToken() {
        return SharedPreferenceManager.getString(this, "refreshToken");
    }

    private void setUserProfileIfExist() {
        String refreshToken = getLoginRefreshToken();
        if(!refreshToken.equals("")) {
            isRetrieveFinish = false;
            requestAccessToken(refreshToken);
        } else {
            isRetrieveFinish = true;
            finishActivityIfAllDone();
        }
    }

    private void requestAccessToken(String refreshToken) {
        NaverLoginApi loginApi = NaverApiClient.getNaverLoginApiClient();
        RefreshTokenQueryMessage queryMessage = new RefreshTokenQueryMessage.Builder(refreshToken).build();
        loginApi.refreshAccessToken(queryMessage.toMap()).enqueue(this);
    }

    private TimerTask createTimerTaskOnExpire() {
        return new TimerTask() {
            @Override
            public void run() {
                isTimeExpire = true;
                finishActivityIfAllDone();
            }
        };
    }

    private void startTimer(int expireTime) {
        Timer timer = new Timer();
        TimerTask timerTask = createTimerTaskOnExpire();
        timer.schedule(timerTask, expireTime);
    }

    private void onRefreshSuccess(Response<RefreshTokenMessage> response) {
        RefreshTokenMessage message = response.body();
        if(message != null) {
            setLoginUser(message);
            requestUserProfile();
        }

    }

    private void requestUserProfile() {
        LoginUser loginUser = LoginUser.getInstance();
        NaverApi naverApi = NaverApiClient.getNaverApiClient();
        naverApi.retrieveUserProfile(loginUser.getAuthorization()).enqueue(createRetrieveUserProfileCallback());
    }

    private void onRetrieveSuccess(Response<RetrieveUserProfileMessage> response) {
        isRetrieveFinish = true;
        UserProfile userProfile = response.body().getUserProfile();
        LoginUser loginUser = LoginUser.getInstance();
        loginUser.setUserProfile(userProfile);
    }

    private void onRetrieveError(Response<RetrieveUserProfileMessage> response) {
        isRetrieveFinish = true;
    }

    private Callback<RetrieveUserProfileMessage> createRetrieveUserProfileCallback() {
        return new Callback<RetrieveUserProfileMessage>() {
            @Override
            public void onResponse(Call<RetrieveUserProfileMessage> call, Response<RetrieveUserProfileMessage> response) {
                if(response.isSuccessful()) {
                    onRetrieveSuccess(response);
                } else {
                    onRetrieveError(response);
                }
                finishActivityIfAllDone();
            }

            @Override
            public void onFailure(Call<RetrieveUserProfileMessage> call, Throwable t) {
                isRetrieveFinish = true;
                finishActivityIfAllDone();
            }
        };
    }

    private void setLoginUser(RefreshTokenMessage message) {
        LoginUser loginUser = LoginUser.getInstance();
        loginUser.setAccessToken(message.getAccessToken());
        loginUser.setRefreshToken(message.getRefreshToken());
        loginUser.setExpiresAtFromExpiresIn(Integer.valueOf(message.getExpiresIn()));
        loginUser.setTokenType(message.getTokenType());
    }

    private void onRefreshError(Response<RefreshTokenMessage> response) {
        isRetrieveFinish = true;
        finishActivityIfAllDone();
    }

    private void finishActivityIfAllDone() {
        if(isRetrieveFinish && isTimeExpire) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onResponse(Call<RefreshTokenMessage> call, Response<RefreshTokenMessage> response) {
        if(response.isSuccessful()) {
            onRefreshSuccess(response);
        } else {
            onRefreshError(response);
        }
    }

    @Override
    public void onFailure(Call<RefreshTokenMessage> call, Throwable t) {
        isRetrieveFinish = true;
        finishActivityIfAllDone();
    }
}
