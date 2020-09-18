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
        List<Float> longTermMovingAverageValues = new ArrayList<>();
        List<Float> shortTermMovingAverageValues = new ArrayList<>();
        for(TrendRatioData data : trendRatioData) {
            longTermMovingAverage.addValue(data.getRatio().floatValue());
            shortTermMovingAverage.addValue(data.getRatio().floatValue());
            longTermMovingAverageValues.add(longTermMovingAverage.getAverage());
            shortTermMovingAverageValues.add(shortTermMovingAverage.getAverage());
        }
        return isAfterGoldenCrossing(longTermMovingAverageValues, shortTermMovingAverageValues);
    }

    public boolean isAfterGoldenCrossing(List<Float> longTerm, List<Float> shortTerm) {
        return longTerm.get(longTerm.size() - 1) < shortTerm.get(shortTerm.size() - 1);
    }

}
