package com.example.hackdayshoppingsearch.common;

import java.util.LinkedList;
import java.util.List;

public class SimpleMovingAverage implements MovingAverage {
    private List<Float> values;
    private final int windowSize;
    private float sum;

    public SimpleMovingAverage(int windowSize) {
        this.windowSize = windowSize;
        values = new LinkedList<>();
        sum = 0;
    }

    @Override
    public float getAverage() {
        return sum / values.size();
    }

    @Override
    public void addValue(float value) {
        sum += value;
        values.add(value);
        if(values.size() > windowSize) {
            sum -= values.get(0);
            values.remove(0);
        }

    }
}
