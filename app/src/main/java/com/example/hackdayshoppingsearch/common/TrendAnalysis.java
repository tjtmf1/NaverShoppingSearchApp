package com.example.hackdayshoppingsearch.common;

import com.example.hackdayshoppingsearch.container.TrendRatioData;

import java.util.ArrayList;
import java.util.List;

public class TrendAnalysis {
    private List<TrendRatioData> trendRatioData;

    private TrendAnalysis(List<TrendRatioData> data) {
        this.trendRatioData = data;
    }

    public static TrendAnalysis from(List<TrendRatioData> data) {
        return new TrendAnalysis(data);
    }

    public boolean isIncreasing(int longTerm, int shortTerm) {
        MovingAverage longTermMovingAverage = new SimpleMovingAverage(longTerm);
        MovingAverage shortTermMovingAverage = new SimpleMovingAverage(shortTerm);
        for(TrendRatioData data : trendRatioData) {
            longTermMovingAverage.addValue(data.getRatio().floatValue());
            shortTermMovingAverage.addValue(data.getRatio().floatValue());
        }
        return isAfterGoldenCrossing(longTermMovingAverage.getAverage(), shortTermMovingAverage.getAverage());
    }

    public boolean isAfterGoldenCrossing(float longTermAverage, float shortTermAverage) {
        return longTermAverage < shortTermAverage;
    }

}
