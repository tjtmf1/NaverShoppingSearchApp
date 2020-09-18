package com.example.hackdayshoppingsearch.ui.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hackdayshoppingsearch.ui.listener.MainActivityListener;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultFragmentListener;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultPagerAdapterListener;
import com.example.hackdayshoppingsearch.container.ShoppingItem;
import com.example.hackdayshoppingsearch.container.message.ErrorMessage;
import com.example.hackdayshoppingsearch.container.message.ShoppingSearchMessage;
import com.example.hackdayshoppingsearch.container.message.ShoppingSearchQueryMessage;
import com.example.hackdayshoppingsearch.communication.network.NaverApi;
import com.example.hackdayshoppingsearch.communication.network.NaverApiClient;
import com.example.hackdayshoppingsearch.ui.adapter.SearchItemRecyclerViewAdapter;

import java.lang.annotation.Annotation;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultEachPageFragment extends Fragment implements Callback<ShoppingSearchMessage> {
    private MainActivityListener mainActivityListener;
    private SearchResultPagerAdapterListener searchResultPagerAdapterListener;
    private SearchResultFragmentListener searchResultFragmentListener;
    private boolean isGridLayout;
    private int sortType;
    private int dataCountPerPage;
    private int pageIndex;
    private String query;

    public SearchResultEachPageFragment() {
    }

    public static SearchResultEachPageFragment newInstance(int pageIndex, String query, int sortType, int dataCountPerPage, boolean isGridLayout) {
        SearchResultEachPageFragment fragment = new SearchResultEachPageFragment();
        Bundle args = new Bundle();
        args.putInt("pageIndex", pageIndex);
        args.putString("query", query);
        args.putInt("sortType", sortType);
        args.putInt("dataCountPerPage", dataCountPerPage);
        args.putBoolean("isGridLayout", isGridLayout);
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
        pageIndex = args.getInt("pageIndex");
        query = args.getString("query");
        sortType = args.getInt("sortType");
        dataCountPerPage = args.getInt("dataCountPerPage");
        isGridLayout = args.getBoolean("isGridLayout");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityListener) {
            mainActivityListener = (MainActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivityListener = null;
        searchResultPagerAdapterListener = null;
        searchResultFragmentListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivityListener = null;
        searchResultPagerAdapterListener = null;
    }

    public SearchResultEachPageFragment setSearchResultPagerAdapterListener(SearchResultPagerAdapterListener searchResultPagerAdapterListener) {
        this.searchResultPagerAdapterListener = searchResultPagerAdapterListener;
        return this;
    }

    public SearchResultEachPageFragment setSearchResultFragmentListener(SearchResultFragmentListener searchResultFragmentListener) {
        this.searchResultFragmentListener = searchResultFragmentListener;
        return this;
    }

    public void setGridLayout(boolean gridLayout) {
        isGridLayout = gridLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("SearchResultEachPageFra", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_result_each_page, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        setRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPageData(pageIndex, query);
    }

    private void setRecyclerView(final RecyclerView recyclerView) {
        final int GRID_SPAN_COUNT;
        if(getResources().getConfiguration().orientation == Configuration.
                ORIENTATION_LANDSCAPE) {
            GRID_SPAN_COUNT = 4;
        } else {
            GRID_SPAN_COUNT = 2;
        }
        Context context = recyclerView.getContext();
        applyRecyclerViewAdapter(recyclerView);
        RecyclerView.LayoutManager layoutManager;
        if (isGridLayout) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, GRID_SPAN_COUNT);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    if(adapter != null) {
                        switch (adapter.getItemViewType(position)) {
                            case SearchItemRecyclerViewAdapter.HEADER_VIEW_TYPE:
                            case SearchItemRecyclerViewAdapter.FOOTER_VIEW_TYPE:
                                return GRID_SPAN_COUNT;
                            default:
                                return 1;
                        }
                    }
                    return 1;
                }
            });
            layoutManager = gridLayoutManager;
            recyclerView.setBackground(new ColorDrawable(getResources().getColor(R.color.baseBackground)));
        } else {
            layoutManager = new LinearLayoutManager(context);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, 1));
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    private void applyRecyclerViewAdapter(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter;
        if (isGridLayout) {
            adapter = createRecyclerViewAdapter(SearchItemRecyclerViewAdapter.GRID_VIEW_TYPE);
        } else {
            adapter = createRecyclerViewAdapter(SearchItemRecyclerViewAdapter.LIST_VIEW_TYPE);
        }
        recyclerView.setAdapter(adapter);
    }

    private void updateList(List<ShoppingItem> items, int totalResult) {
        Log.v("SearchResultEachPageFra", "updateList");
        View view = getView();
        if(view != null) {
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null) {
                if (adapter instanceof SearchItemRecyclerViewAdapter) {
                    ((SearchItemRecyclerViewAdapter) adapter).setItems(items);
                    ((SearchItemRecyclerViewAdapter) adapter).setTotalResult(totalResult);
                }
                adapter.notifyDataSetChanged();
            }
            Log.v("function", "SearchResultEachPageFragment-updateList: adapter:" + adapter);
        }
    }

    private RecyclerView.Adapter createRecyclerViewAdapter(int viewType) {
        RecyclerView.Adapter adapter = new SearchItemRecyclerViewAdapter(viewType, pageIndex, sortType)
                .setSearchResultFragmentListener(searchResultFragmentListener)
                .setMainActivityListener(mainActivityListener)
                .setSearchResultPagerAdapterListener(searchResultPagerAdapterListener);
        return adapter;
    }

    private void requestPageData(int page, String query) {
        if(searchResultFragmentListener != null) {
            List<ShoppingItem> items = searchResultFragmentListener.getItems(page);
            if (items != null) {
                updateList(items, searchResultFragmentListener.getTotalDataCount());
                return;
            }
        }
        hideList();
        showLoading();
        final NaverApi naverApi = NaverApiClient.getNaverApiClient();
        ShoppingSearchQueryMessage queryMessage =
                new ShoppingSearchQueryMessage.Builder(query)
                .displayCount(dataCountPerPage)
                .sort(getSortString())
                .startPosition(page * dataCountPerPage + 1)
                .build();
        Log.v("Query Submit", "Query : " + queryMessage.toMap());
        naverApi.doShoppingSearch(queryMessage.toMap()).enqueue(this);
    }

    private String getSortString() {
        switch (sortType) {
            default:
            case SearchItemRecyclerViewAdapter.SIM_SORT:
                return "sim";
            case SearchItemRecyclerViewAdapter.DATE_SORT:
                return "date";
            case SearchItemRecyclerViewAdapter.ASC_SORT:
                return "asc";
            case SearchItemRecyclerViewAdapter.DSC_SORT:
                return "dsc";

        }
    }

    private void showList() {
        View view = getView();
        if(view != null) {
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideList() {
        View view = getView();
        if(view != null) {
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void showLoading() {
        View view = getView();
        if(view != null) {
            ProgressBar progressBar = view.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        View view = getView();
        if(view != null) {
            ProgressBar progressBar = view.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showErrorText(String errorMessageForShow) {
        View view = getView();
        if(view != null) {
            TextView textView = view.findViewById(R.id.text_view);
            textView.setText(errorMessageForShow);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResponse(Call<ShoppingSearchMessage> call, Response<ShoppingSearchMessage> response) {
        hideLoading();
        showList();
        Log.v("test", response.toString());
        if(response.isSuccessful()) {
            onResponseSuccess(response);
        } else {
            onResponseError(response);
        }
    }

    @Override
    public void onFailure(Call<ShoppingSearchMessage> call, Throwable t) {
        String errorMessage = "검색도중 문제가 발생했습니다.";
        hideLoading();
        showErrorText(errorMessage);
    }

    private void saveData(List<ShoppingItem> items, int totalDataCount) {
        if(searchResultFragmentListener != null) {
            searchResultFragmentListener.putItems(pageIndex, items);
            searchResultFragmentListener.setTotalDataCount(totalDataCount);
        }
    }

    private void onResponseSuccess(Response<ShoppingSearchMessage> response) {
        ShoppingSearchMessage shoppingSearchMessage = response.body();
        Log.v("Query Response Success", shoppingSearchMessage.toString());
        List<ShoppingItem> items = shoppingSearchMessage.getItems();
        int totalDataCount = shoppingSearchMessage.getTotal();
        updateList(items, totalDataCount);
        saveData(items, totalDataCount);
    }

    private void onResponseError(Response<ShoppingSearchMessage> response) {
        if(response.errorBody() != null) {
            Retrofit retrofit = new Retrofit.Builder().build();
            Converter<ResponseBody, ErrorMessage> errorMessageConverter = retrofit.responseBodyConverter(ErrorMessage.class, new Annotation[0]);
            try {
                ErrorMessage errorMessage = errorMessageConverter.convert(response.errorBody());
                Log.e("Query Response Error", errorMessage.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
