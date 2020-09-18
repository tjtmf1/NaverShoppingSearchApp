package com.example.hackdayshoppingsearch.container;

public class QueryHistory {
    private String id;
    private String query;
    private long timestamp;

    public QueryHistory(String id, String query, long timestamp) {
        this.id = id;
        this.query = query;
        this.timestamp = timestamp;
    }


    public String getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
