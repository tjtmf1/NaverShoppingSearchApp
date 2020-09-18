package com.example.hackdayshoppingsearch.container.message;

import com.example.hackdayshoppingsearch.container.UserProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetrieveUserProfileMessage {
    @SerializedName("resultcode")
    @Expose
    private String resultCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private UserProfile userProfile;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultcode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
