package com.example.hackdayshoppingsearch.container.message;

import com.google.gson.Gson;

import androidx.annotation.NonNull;

public class ConvertibleJsonMessage {
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
