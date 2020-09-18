package com.example.hackdayshoppingsearch.container.message;

import com.example.hackdayshoppingsearch.common.pattern.Buildable;

import java.util.HashMap;
import java.util.Map;

public class RefreshTokenQueryMessage {
    private String grantType;
    private String clientId;
    private String clientSecret;
    private String refreshToken;

    private RefreshTokenQueryMessage(Builder builder) {
        this.grantType = builder.grantType;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.refreshToken = builder.refreshToken;
    }

    public Map<String, String>  toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", grantType);
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("refresh_token", refreshToken);
        return map;
    }

    public static class Builder implements Buildable<RefreshTokenQueryMessage> {
        private String grantType;
        private String clientId;
        private String clientSecret;
        private String refreshToken;

        public Builder(String refreshToken) {
            grantType = "refresh_token";
            clientId = "7rQpj4I5zu9U_7vktMNP";
            clientSecret = "ZI3pVa2MYW";
            this.refreshToken = refreshToken;
        }

        @Override
        public RefreshTokenQueryMessage build() {
            return new RefreshTokenQueryMessage(this);
        }
    }
}
