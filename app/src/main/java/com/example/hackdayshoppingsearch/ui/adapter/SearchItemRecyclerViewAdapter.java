package com.example.hackdayshoppingsearch.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackdayshoppingsearch.ui.listener.MainActivityListener;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultFragmentListener;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultPagerAdapterListener;
import com.example.hackdayshoppingsearch.container.ShoppingItem;
import com.example.hackdayshoppingsearch.ui.viewholder.BaseViewHolder;
import com.example.hackdayshoppingsearch.ui.viewholder.SearchItemGridVIewHolder;
import com.example.hackdayshoppingsearch.ui.viewholder.SearchItemListViewHolder;
import com.example.hackdayshoppingsearch.ui.viewholder.SearchResultFooterViewHolder;
import com.example.hackdayshoppingsearch.ui.viewholder.SearchResultHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SearchItemRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public final static int LIST_VIEW_TYPE = 1;
    public final static int GRID_VIEW_TYPE = 2;
    public final static int HEADER_VIEW_TYPE = -1;
    public final static int FOOTER_VIEW_TYPE = -2;
    public final static int SIM_SORT = 0;
    public final static int DATE_SORT = 1;
    public final static int ASC_SORT = 2;
    public final static int DSC_SORT = 3;
    private MainActivityListener mainActivityListener;
    private SearchResultFragmentListener searchResultFragmentListener;
    private SearchResultPagerAdapterListener searchResultPagerAdapterListener;
    private final int viewType;
    private final int sortType;
    private int totalResult = -1;
    private List<ShoppingItem> items;
    private final int curPageIndex;

    public SearchItemRecyclerViewAdapter(int viewType, int curPageIndex, int sortType) {
        items = new ArrayList<>();
        this.viewType = viewType;
        this.curPageIndex = curPageIndex;
        this.sortType = sortType;
    }

    public SearchItemRecyclerViewAdapter setMainActivityListener(MainActivityListener mainActivityListener) {
        this.mainActivityListener = mainActivityListener;
        return this;
    }

    public SearchItemRecyclerViewAdapter setSearchResultPagerAdapterListener(SearchResultPagerAdapterListener searchResultPagerAdapterListener) {
        this.searchResultPagerAdapterListener = searchResultPagerAdapterListener;
        return this;
    }

    public SearchItemRecyclerViewAdapter setSearchResultFragmentListener(SearchResultFragmentListener searchResultFragmentListener) {
        this.searchResultFragmentListener = searchResultFragmentListener;
        return this;
    }

    public void setItems(List<ShoppingItem> items) {
        this.items = items;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.search_result_header, parent, false);
                return new SearchResultHeaderViewHolder(view, this.viewType, sortType)
                        .setSearchResultPagerAdapterListener(searchResultPagerAdapterListener);
            case FOOTER_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.search_result_footer, parent, false);
                return new SearchResultFooterViewHolder(view, searchResultFragmentListener);
            default:
            case LIST_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_search_listitem, parent, false);
                return new SearchItemListViewHolder(view)
                        .setMainActivityListener(mainActivityListener);
            case GRID_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_search_griditem, parent, false);
                return new SearchItemGridVIewHolder(view)
                        .setMainActivityListener(mainActivityListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return HEADER_VIEW_TYPE;
        } else if(position == getItemCount() - 1) {
            return FOOTER_VIEW_TYPE;
        } else {
            return viewType;
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        if(position == 0) {
            if(totalResult != -1) {
                if(holder instanceof SearchResultHeaderViewHolder) {
                    ((SearchResultHeaderViewHolder) holder).setTotalResult(totalResult);
                }
            }
            holder.bindData(null);
        } else if (position == getItemCount() - 1) {
            holder.bindData(curPageIndex + 1);
        } else {
            final ShoppingItem item = items.get(position - 1);
            holder.bindData(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 2;
    }
}
