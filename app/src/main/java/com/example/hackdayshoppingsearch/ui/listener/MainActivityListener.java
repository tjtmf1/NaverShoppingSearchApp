package com.example.hackdayshoppingsearch.ui.listener;

import com.example.hackdayshoppingsearch.container.ShoppingItem;

public interface MainActivityListener {
    void onShoppingItemClicked(ShoppingItem item);
    void onTrendItemClicked(String item);
}
