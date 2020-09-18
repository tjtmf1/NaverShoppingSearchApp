package com.example.hackdayshoppingsearch.ui.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.container.ShoppingItem;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShoppingItemDetailFragment extends Fragment implements View.OnClickListener {
    private ShoppingItem item;

    public ShoppingItemDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_item_detail, container, false);
    }

    public static ShoppingItemDetailFragment newInstance(ShoppingItem item) {
        ShoppingItemDetailFragment fragment = new ShoppingItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args == null) {
            throw new IllegalArgumentException("Argument bundle object is null");
        }
        item = (ShoppingItem) args.getSerializable("item");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
    }

    private void bindView(View view) {
        ImageView imageView = view.findViewById(R.id.item_detail_image_view);
        TextView categoryTextView = view.findViewById(R.id.item_detail_category_text_view);
        TextView titleTextView = view.findViewById(R.id.item_detail_title_text_view);
        TextView priceTextView = view.findViewById(R.id.item_detail_price_text_view);
        TextView brandTextView = view.findViewById(R.id.item_detail_brand_text_view);
        TextView mallTextView = view.findViewById(R.id.item_detail_mall_text_view);
        Button linkButton = view.findViewById(R.id.item_detail_link_button);

        Glide.with(view).load(item.getImage()).into(imageView);
        titleTextView.setText(Html.fromHtml(item.getTitleExceptHtmlTag()));
        categoryTextView.setText(item.getFullCategory());
        String priceText = String.format(Locale.getDefault(), "%,d원", Integer.valueOf(item.getLprice()));
        priceTextView.setText(priceText);
        brandTextView.setText(item.getBrand());
        mallTextView.setText(item.getMallName());
        linkButton.setOnClickListener(this);
    }

    private void goToWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_detail_link_button:
                goToWebPage(item.getLink());
            default:
        }
    }
}
