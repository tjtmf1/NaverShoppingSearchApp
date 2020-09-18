package com.example.hackdayshoppingsearch.ui.viewholder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultPagerAdapterListener;
import com.example.hackdayshoppingsearch.ui.adapter.SearchItemRecyclerViewAdapter;

import java.util.Locale;

import androidx.annotation.NonNull;

public class SearchResultHeaderViewHolder extends BaseViewHolder {
    private SearchResultPagerAdapterListener searchResultPagerAdapterListener;
    private int totalResult;
    public SearchResultHeaderViewHolder(@NonNull View itemView, int viewType, int sortType) {
        super(itemView);
        setButtons(viewType);
        setSortText(sortType);
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    private void setButtons(int viewType) {
        Button listButton = itemView.findViewById(R.id.list_button);
        Button gridButton = itemView.findViewById(R.id.grid_button);
        if(viewType == SearchItemRecyclerViewAdapter.LIST_VIEW_TYPE) {
            listButton.setBackgroundResource(R.drawable.list_blue);
            gridButton.setBackgroundResource(R.drawable.grid_gray);
            gridButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchResultPagerAdapterListener.onLayoutChange(true);
                }
            });
        } else {
            listButton.setBackgroundResource(R.drawable.list_gray);
            gridButton.setBackgroundResource(R.drawable.grid_blue);
            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchResultPagerAdapterListener.onLayoutChange(false);
                }
            });
        }
    }

    private void setSortText(int sortType) {
        TextView simTextView = itemView.findViewById(R.id.sim_text_view);
        TextView dateTextView = itemView.findViewById(R.id.date_text_view);
        TextView ascTextView = itemView.findViewById(R.id.asc_text_view);
        TextView dscTextView = itemView.findViewById(R.id.dsc_text_view);
        simTextView.setOnClickListener(createSortTextClickedListener());
        dateTextView.setOnClickListener(createSortTextClickedListener());
        ascTextView.setOnClickListener(createSortTextClickedListener());
        dscTextView.setOnClickListener(createSortTextClickedListener());

        TextView textView;
        switch (sortType) {
            default:
            case SearchItemRecyclerViewAdapter.SIM_SORT:
                textView = simTextView;
                break;
            case SearchItemRecyclerViewAdapter.DATE_SORT:
                textView = dateTextView;
                break;
            case SearchItemRecyclerViewAdapter.ASC_SORT:
                textView = ascTextView;
                break;
            case SearchItemRecyclerViewAdapter.DSC_SORT:
                textView = dscTextView;
                break;
        }
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
    }

    private void setResultCountText(int totalResult) {
        TextView textView = itemView.findViewById(R.id.result_count);
        String resultCountText = String.format(Locale.getDefault(), "검색결과: %,d개", totalResult);
        textView.setText(resultCountText);
    }

    public SearchResultHeaderViewHolder setSearchResultPagerAdapterListener(SearchResultPagerAdapterListener searchResultPagerAdapterListener) {
        this.searchResultPagerAdapterListener = searchResultPagerAdapterListener;
        return this;
    }

    @Override
    public void bindData(Object o) {
        if(totalResult != -1) {
            setResultCountText(totalResult);
        }
    }

    public View.OnClickListener createSortTextClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sortType;
                switch (v.getId()) {
                    default:
                    case R.id.sim_text_view:
                        sortType = SearchItemRecyclerViewAdapter.SIM_SORT;
                        break;
                    case R.id.date_text_view:
                        sortType = SearchItemRecyclerViewAdapter.DATE_SORT;
                        break;
                    case R.id.asc_text_view:
                        sortType = SearchItemRecyclerViewAdapter.ASC_SORT;
                        break;
                    case R.id.dsc_text_view:
                        sortType = SearchItemRecyclerViewAdapter.DSC_SORT;
                        break;
                }
                searchResultPagerAdapterListener.onSortChanged(sortType);
            }
        };
    }
}
