package com.example.hackdayshoppingsearch.container.message;

import com.example.hackdayshoppingsearch.common.pattern.Buildable;

import java.util.HashMap;
import java.util.Map;

public class ShoppingSearchQueryMessage extends ConvertibleJsonMessage {
    private String query;
    private String display;
    private String start;
    private String sort;

    private ShoppingSearchQueryMessage(Builder builder) {
        this.query = builder.query;
        this.display = builder.display;
        this.start = builder.start;
        this.sort = builder.sort;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("display", display);
        map.put("start", start);
        map.put("sort", sort);
        return map;
    }

    public static class Builder implements Buildable {
        private String query;
        private String display = "10";
        private String start = "1";
        private String sort = "sim";

        public Builder(String query) {
            this.query = query;
        }

        public Builder displayCount(int displayCount) {
            if(!isValidDisplayCount(displayCount)) {
                displayCount = 10;
            }
            this.display = Integer.valueOf(displayCount).toString();
            return this;
        }

        private boolean isValidDisplayCount(int displayCount) {
            return displayCount >= 1 && displayCount <= 100;
        }

        public Builder startPosition(int startPosition) {
            if(!isValidStartPosition(startPosition)) {
                startPosition = 1;
            }
            this.start = Integer.valueOf(startPosition).toString();
            return this;
        }

        private boolean isValidStartPosition(int startPosition) {
            return startPosition >= 1 && startPosition <= 1000;
        }

        public Builder sort(String sort) {
            if(isValidSort(sort)) {
                this.sort = sort;
            }
            return this;
        }

        private boolean isValidSort(String sort) {
            return sort.equals("sim") || sort.equals("date") || sort.equals("asc") || sort.equals("dsc");
        }

        @Override
        public ShoppingSearchQueryMessage build() {
            return new ShoppingSearchQueryMessage(this);
        }
    }
}
