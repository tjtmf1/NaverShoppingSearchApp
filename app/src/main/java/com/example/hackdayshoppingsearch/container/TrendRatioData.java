package com.example.hackdayshoppingsearch.container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendRatioData {
    @SerializedName("period")
    @Expose
    private String period;
    @SerializedName("ratio")
    @Expose
    private Double ratio;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

}
