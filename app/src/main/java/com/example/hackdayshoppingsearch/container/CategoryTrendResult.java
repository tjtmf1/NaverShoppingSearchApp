package com.example.hackdayshoppingsearch.container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryTrendResult {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("data")
    @Expose
    private List<TrendRatioData> data = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<TrendRatioData> getData() {
        return data;
    }

    public void setData(List<TrendRatioData> data) {
        this.data = data;
    }
}
