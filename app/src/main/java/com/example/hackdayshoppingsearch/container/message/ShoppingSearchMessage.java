package com.example.hackdayshoppingsearch.container.message;

import com.example.hackdayshoppingsearch.container.ShoppingItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoppingSearchMessage extends ConvertibleJsonMessage {
    @SerializedName("lastBuildDate")
    @Expose
    private String lastBuildDate;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("start")
    @Expose
    private Integer start;
    @SerializedName("display")
    @Expose
    private Integer display;
    @SerializedName("items")
    @Expose
    private List<ShoppingItem> shoppingItems = null;

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public List<ShoppingItem> getItems() {
        return shoppingItems;
    }

    public void setItems(List<ShoppingItem> items) {
        this.shoppingItems = items;
    }
}
