package com.example.hackdayshoppingsearch.container.message;

import com.example.hackdayshoppingsearch.common.pattern.Buildable;
import com.example.hackdayshoppingsearch.container.CategoryParam;

import java.util.ArrayList;
import java.util.List;

public class RetrieveTrendQueryMessage {
    private String startDate;
    private String endDate;
    private String timeUnit;
    private List<CategoryParam> category;
    private String device;
    private String gender;
    private List<String> ages;

    private RetrieveTrendQueryMessage(Builder builder) {
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.timeUnit = builder.timeUnit;
        this.category = builder.category;
        this.device = builder.device;
        this.gender = builder.gender;
        this.ages = builder.ages;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public String getDevice() {
        return device;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getAges() {
        return ages;
    }

    public List<CategoryParam> getCategory() {
        return category;
    }

    public static class Builder implements Buildable<RetrieveTrendQueryMessage> {
        private String startDate;
        private String endDate;
        private String timeUnit;
        private List<CategoryParam> category;
        private String device;
        private String gender;
        private List<String> ages;

        public Builder(String startDate, String endDate, String timeUnit, List<CategoryParam> category) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.timeUnit = timeUnit;
            this.category = category;
        }

        public Builder device(String device) {
            this.device = device;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder addAges(String age) {
            if(age != null) {
                if(ages == null) {
                    ages = new ArrayList<>();
                }
                ages.add(age);
            }
            return this;
        }

        @Override
        public RetrieveTrendQueryMessage build() {
            return new RetrieveTrendQueryMessage(this);
        }
    }
}
