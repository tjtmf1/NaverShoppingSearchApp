package com.example.hackdayshoppingsearch.ui.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultFragmentListener;
import com.example.hackdayshoppingsearch.container.SerializableSparseArray;
import com.example.hackdayshoppingsearch.container.ShoppingItem;
import com.example.hackdayshoppingsearch.ui.adapter.SearchResultPagerAdapter;

import java.util.List;

public class SearchResultFragment extends Fragment implements SearchResultFragmentListener {
    private String inputQuery;
    private final int dataPerPage = 20;
    private SerializableSparseArray<List<ShoppingItem>> savedItems;
    private int savedTotalDataCount;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchResultFragment() {
        savedItems = new SerializableSparseArray<>();
    }

    public static SearchResultFragment newInstance(String inputQuery) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString("inputQuery", inputQuery);
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
        inputQuery = args.getString("inputQuery");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("SearchResultFragment", "onCreateView");
        final View view = inflater.inflate(R.layout.fragment_serarch_result, container, false);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        Activity activity = getActivity();
        if(activity != null) {
            SearchResultPagerAdapter adapter =
                    new SearchResultPagerAdapter(getActivity().getSupportFragmentManager(), inputQuery, dataPerPage)
                    .setSearchResultFragmentListener(this);
            viewPager.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void setCurrentPage(int pageIndex) {
        View view = getView();
        if(view != null) {
            ViewPager viewPager = view.findViewById(R.id.view_pager);
            viewPager.setCurrentItem(pageIndex);
        }
    }

    @Override
    public void clearSavedData() {
        savedItems.clear();
        savedTotalDataCount = 0;
    }

    @Override
    public List<ShoppingItem> getItems(int pageIndex) {
        return savedItems.get(pageIndex);
    }

    @Override
    public int getTotalDataCount() {
        return savedTotalDataCount;
    }

    @Override
    public void putItems(int pageIndex, List<ShoppingItem> items) {
        savedItems.append(pageIndex, items);
    }

    @Override
    public void setTotalDataCount(int totalDataCount) {
        savedTotalDataCount = totalDataCount;
    }
}
