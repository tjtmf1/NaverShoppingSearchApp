package com.example.hackdayshoppingsearch.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.hackdayshoppingsearch.ui.listener.MainActivityListener;
import com.example.hackdayshoppingsearch.R;

import androidx.annotation.NonNull;

public class PopularTrendViewHolder extends BaseViewHolder<String> implements View.OnClickListener {
    private String item;
    private TextView titleTextView;
    private MainActivityListener mainActivityListener;

    public PopularTrendViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.trend_title_text_view);
        itemView.setOnClickListener(this);
    }

    public PopularTrendViewHolder setMainActivityListener(MainActivityListener mainActivityListener) {
        this.mainActivityListener = mainActivityListener;
        return this;
    }

    @Override
    public void bindData(String string) {
        this.item = string;
        titleTextView.setText(string);
    }

    @Override
    public void onClick(View v) {
        if(mainActivityListener != null) {
            mainActivityListener.onTrendItemClicked(item);
        }
    }
}
