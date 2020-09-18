package com.example.hackdayshoppingsearch.ui.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.ui.listener.SearchResultFragmentListener;

import java.util.Locale;

import androidx.annotation.NonNull;

public class SearchResultFooterViewHolder extends BaseViewHolder<Integer> {
    private TextView[] pageTextView;
    public SearchResultFooterViewHolder(@NonNull View itemView, final SearchResultFragmentListener listener) {
        super(itemView);
        pageTextView = new TextView[5];
        pageTextView[0] = itemView.findViewById(R.id.PageText1);
        pageTextView[1] = itemView.findViewById(R.id.PageText2);
        pageTextView[2] = itemView.findViewById(R.id.PageText3);
        pageTextView[3] = itemView.findViewById(R.id.PageText4);
        pageTextView[4] = itemView.findViewById(R.id.PageText5);
        for(final TextView textView : pageTextView) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pageString = textView.getText().toString();
                    if(!TextUtils.isEmpty(pageString)) {
                        int pageIndex = Integer.valueOf(pageString) - 1;
                        if (listener != null) {
                            listener.setCurrentPage(pageIndex);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void bindData(Integer curPage) {
        int pageForShow = curPage - 2;
        for(final TextView textView : pageTextView) {
            String pageStringForShow = "";
            if(pageForShow > 0) {
                pageStringForShow = String.format(Locale.getDefault(),"%d", pageForShow);
            }
            textView.setText(pageStringForShow);
            pageForShow += 1;
        }
    }
}
