package com.example.hackdayshoppingsearch.ui.listener;

import com.example.hackdayshoppingsearch.container.ShoppingItem;

import java.util.List;

public interface SearchResultFragmentListener {
    List<ShoppingItem> getItems(int pageIndex);
    int getTotalDataCount();
    void putItems(int pageIndex, List<ShoppingItem> items);
    void setTotalDataCount(int totalDataCount);
    void setCurrentPage(int pageIndex);
    void clearSavedData();
}
