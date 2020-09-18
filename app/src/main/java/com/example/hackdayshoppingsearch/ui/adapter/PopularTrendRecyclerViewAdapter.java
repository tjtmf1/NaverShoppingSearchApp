package com.example.hackdayshoppingsearch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackdayshoppingsearch.ui.listener.MainActivityListener;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.ui.viewholder.BaseViewHolder;
import com.example.hackdayshoppingsearch.ui.viewholder.PopularTrendHeaderViewHolder;
import com.example.hackdayshoppingsearch.ui.viewholder.PopularTrendViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PopularTrendRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<String> items;
    private MainActivityListener mainActivityListener;

    public PopularTrendRecyclerViewAdapter() {
        items = new ArrayList<>();
    }

    public PopularTrendRecyclerViewAdapter setMainActivityListener(MainActivityListener mainActivityListener) {
        this.mainActivityListener = mainActivityListener;
        return this;
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        items.add(item);
        notifyItemInserted(items.size());
    }

    public void clearItem() {
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case BaseViewHolder.VIEWTYPE_HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.popular_trend_list_header, parent, false);
                return new PopularTrendHeaderViewHolder(view);
            default:
            case BaseViewHolder.VIEWTYPE_DEFAULT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.popular_trend_item, parent, false);
                return new PopularTrendViewHolder(view)
                        .setMainActivityListener(mainActivityListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return BaseViewHolder.VIEWTYPE_HEADER;
        } else {
            return BaseViewHolder.VIEWTYPE_DEFAULT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if(position == 0) {
            holder.bindData(null);
        } else {
            holder.bindData(items.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }
}
