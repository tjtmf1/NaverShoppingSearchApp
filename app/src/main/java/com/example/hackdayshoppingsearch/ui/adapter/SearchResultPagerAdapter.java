package com.example.hackdayshoppingsearch.ui.adapter;

import android.util.Log;

import com.example.hackdayshoppingsearch.ui.listener.SearchResultFragmentListener;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultPagerAdapterListener;
import com.example.hackdayshoppingsearch.ui.fragment.SearchResultEachPageFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SearchResultPagerAdapter extends FragmentStatePagerAdapter implements SearchResultPagerAdapterListener {
    private final String inputQuery;
    private boolean isGridLayout = false;
    private int sortType = SearchItemRecyclerViewAdapter.SIM_SORT;
    private SearchResultFragmentListener searchResultFragmentListener;
    private int dataCountPerPage;

    public SearchResultPagerAdapter(FragmentManager fm, String inputQuery, int dataCountPerPage) {
        super(fm);
        this.inputQuery = inputQuery;
        this.dataCountPerPage = dataCountPerPage;
    }

    public SearchResultPagerAdapter setSearchResultFragmentListener(SearchResultFragmentListener searchResultFragmentListener) {
        this.searchResultFragmentListener = searchResultFragmentListener;
        return this;
    }

    public void setDataCountPerPage(int dataCountPerPage) {
        if(1 <= dataCountPerPage && dataCountPerPage <= 100) {
            this.dataCountPerPage = dataCountPerPage;
        } else {
            this.dataCountPerPage = 10;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Log.v("SearchResultPagerAdapte", "getItems: " + position);
        SearchResultEachPageFragment fragment = SearchResultEachPageFragment.newInstance(position, inputQuery, sortType, dataCountPerPage, isGridLayout)
                .setSearchResultPagerAdapterListener(this)
                .setSearchResultFragmentListener(searchResultFragmentListener);
        Log.v("getItems", fragment.toString());
        return fragment;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onLayoutChange(boolean isGridLayout) {
        this.isGridLayout = isGridLayout;
        notifyDataSetChanged();
    }

    @Override
    public void onSortChanged(int sortType) {
        this.sortType = sortType;
        searchResultFragmentListener.setCurrentPage(0);
        searchResultFragmentListener.clearSavedData();
        notifyDataSetChanged();
    }
}
