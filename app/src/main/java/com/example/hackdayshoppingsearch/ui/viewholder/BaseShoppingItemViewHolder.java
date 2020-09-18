package com.example.hackdayshoppingsearch.ui.viewholder;

import android.view.View;

import com.example.hackdayshoppingsearch.ui.listener.MainActivityListener;
import com.example.hackdayshoppingsearch.container.ShoppingItem;

import androidx.annotation.NonNull;

public abstract class BaseShoppingItemViewHolder extends BaseViewHolder<ShoppingItem> implements View.OnClickListener {
    protected ShoppingItem item;
    protected MainActivityListener mainActivityListener;

    public BaseShoppingItemViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bindData(ShoppingItem item) {
        this.item = item;
        bindDataToView(item);
    }

    protected abstract void bindDataToView(ShoppingItem item);

    public BaseShoppingItemViewHolder setMainActivityListener(MainActivityListener mainActivityListener) {
        this.mainActivityListener = mainActivityListener;
        return this;
    }

    @Override
    public void onClick(View view) {
        if(mainActivityListener != null) {
            mainActivityListener.onShoppingItemClicked(item);
        }
    }
}
