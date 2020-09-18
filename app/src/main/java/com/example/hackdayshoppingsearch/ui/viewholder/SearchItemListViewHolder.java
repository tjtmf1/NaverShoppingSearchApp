package com.example.hackdayshoppingsearch.ui.viewholder;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.container.ShoppingItem;

import java.util.Locale;

public class SearchItemListViewHolder extends BaseShoppingItemViewHolder {

    private final ImageView imageView;
    private final TextView titleTextView;
    private final TextView priceTextView;
    private final TextView categoryTextView;

    public SearchItemListViewHolder(View view) {
        super(view);
        imageView = view.findViewById(R.id.list_item_image);
        titleTextView = view.findViewById(R.id.list_item_title);
        priceTextView = view.findViewById(R.id.list_item_price);
        categoryTextView = view.findViewById(R.id.list_item_category);
    }

    @Override
    protected void bindDataToView(ShoppingItem shoppingItem) {
        Glide.with(view).load(shoppingItem.getImage()).into(imageView);
        titleTextView.setText(Html.fromHtml(shoppingItem.getTitle()));
        String priceText = String.format(Locale.getDefault(), "%,d원", Integer.valueOf(shoppingItem.getLprice()));
        priceTextView.setText(priceText);
        categoryTextView.setText(shoppingItem.getFullCategory());
    }
}
