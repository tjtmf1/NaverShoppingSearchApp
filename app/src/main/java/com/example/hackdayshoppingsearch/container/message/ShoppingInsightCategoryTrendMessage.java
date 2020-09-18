package com.example.hackdayshoppingsearch.container.message;

import com.example.hackdayshoppingsearch.container.CategoryTrendResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoppingInsightCategoryTrendMessage {
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("timeUnit")
    @Expose
    private String timeUnit;
    @SerializedName("results")
    @Expose
    private List<CategoryTrendResult> results = null;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public List<CategoryTrendResult> getResults() {
        return results;
    }

    public void setResults(List<CategoryTrendResult> results) {
        this.results = results;
    }
}
