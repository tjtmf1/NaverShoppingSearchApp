package com.example.hackdayshoppingsearch.container;

import android.content.Context;

import com.nhn.android.naverlogin.OAuthLogin;

import java.util.Date;

public class LoginUser {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresAt;
    private UserProfile userProfile;
    public static LoginUser getInstance() {
        return LazyHolder.INSTANCE;
    }
    private static class LazyHolder {
        static final LoginUser INSTANCE = new LoginUser();
    }

    public static void clear() {
        LazyHolder.INSTANCE.setAccessToken(null);
        LazyHolder.INSTANCE.setRefreshToken(null);
        LazyHolder.INSTANCE.setTokenType(null);
        LazyHolder.INSTANCE.setExpiresAt(0);
        LazyHolder.INSTANCE.setUserProfile(null);
    }

    public void importFrom(OAuthLogin oAuthLogin, Context context) {
        accessToken = oAuthLogin.getAccessToken(context);
        refreshToken = oAuthLogin.getRefreshToken(context);
        expiresAt = oAuthLogin.getExpiresAt(context);
        tokenType = oAuthLogin.getTokenType(context);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setExpiresAtFromExpiresIn(int expiresIn) {
        this.expiresAt = new Date().getTime() + expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getAuthorization() {
        return getTokenType() + " " + getAccessToken();
    }
}
