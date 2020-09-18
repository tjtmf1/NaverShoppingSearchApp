package com.example.hackdayshoppingsearch.ui.viewholder;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.container.ShoppingItem;

import java.util.Locale;

import androidx.annotation.NonNull;

public class SearchItemGridVIewHolder extends BaseShoppingItemViewHolder {
    private final ImageView imageView;
    private final TextView titleTextView;
    private final TextView priceTextView;
    private final TextView mallTextView;

    public SearchItemGridVIewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = view.findViewById(R.id.grid_item_image);
        titleTextView = view.findViewById(R.id.grid_item_title);
        priceTextView = view.findViewById(R.id.grid_item_price);
        mallTextView = view.findViewById(R.id.grid_item_mall_name);
    }

    @Override
    protected void bindDataToView(ShoppingItem shoppingItem) {
        Glide.with(view).load(shoppingItem.getImage()).into(imageView);
        titleTextView.setText(Html.fromHtml(shoppingItem.getTitle()));
        String priceText = String.format(Locale.getDefault(), "%,d원", Integer.valueOf(shoppingItem.getLprice()));
        priceTextView.setText(priceText);
        mallTextView.setText(shoppingItem.getMallName());
    }
}
