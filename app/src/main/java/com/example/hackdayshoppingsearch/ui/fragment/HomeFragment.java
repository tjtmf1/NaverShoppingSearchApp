package com.example.hackdayshoppingsearch.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.hackdayshoppingsearch.ui.listener.MainActivityListener;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.common.TrendAnalysis;
import com.example.hackdayshoppingsearch.communication.network.NaverApi;
import com.example.hackdayshoppingsearch.communication.network.NaverApiClient;
import com.example.hackdayshoppingsearch.container.CategoryParam;
import com.example.hackdayshoppingsearch.container.LoginUser;
import com.example.hackdayshoppingsearch.container.UserProfile;
import com.example.hackdayshoppingsearch.container.message.RetrieveTrendQueryMessage;
import com.example.hackdayshoppingsearch.container.message.ShoppingInsightCategoryTrendMessage;
import com.example.hackdayshoppingsearch.ui.adapter.PopularTrendRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MainActivityListener mainActivityListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivityListener) {
            mainActivityListener = (MainActivityListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivityListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        View view = getView();
        if(view != null) {
            recyclerView = view.findViewById(R.id.trend_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            PopularTrendRecyclerViewAdapter adapter =
                    new PopularTrendRecyclerViewAdapter()
                    .setMainActivityListener(mainActivityListener);
            recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
            retrieveTrend(getCategoryCode());
        }
    }

    public void showPopularTrendList() {
        retrieveTrend(getCategoryCode());
    }

    private void retrieveTrend(List<CategoryParam> categoryCodes) {
        hideList();
        showLoading();
        resetRecyclerViewItem();
        final NaverApi naverApi = NaverApiClient.getNaverApiClient();
        UserProfile userProfile = LoginUser.getInstance().getUserProfile();
        String startDate = getStartDate();
        String endDate = getEndDate();
        String device = "mo";
        String gender = null, age = null;
        if(userProfile != null) {
            gender = userProfile.getGender().toLowerCase();
            age = userProfile.getAge().substring(0, 2);
        }
        for(CategoryParam categoryCode : categoryCodes) {
            List<CategoryParam> categoryParam = new ArrayList<>(1);
            categoryParam.add(categoryCode);
            RetrieveTrendQueryMessage queryMessage
                    = new RetrieveTrendQueryMessage.Builder(startDate, endDate, "month", categoryParam)
                    .device(device)
                    .addAges(age)
                    .gender(gender)
                    .build();
            naverApi.retrieveCategoryTrend(queryMessage).enqueue(createRetrieveTrendCallback());
        }
    }

    private void resetRecyclerViewItem() {
        PopularTrendRecyclerViewAdapter adapter = (PopularTrendRecyclerViewAdapter) recyclerView.getAdapter();
        if(adapter != null) {
            adapter.clearItem();
        }
    }

    private void onRetrieveTrendSuccess(Response<ShoppingInsightCategoryTrendMessage> response) {
        ShoppingInsightCategoryTrendMessage message = response.body();
        if(message == null) {
            return;
        }
        TrendAnalysis trendAnalysis = TrendAnalysis.from(message.getResults().get(0).getData());
        if(trendAnalysis.isIncreasing(6, 3)) {
            PopularTrendRecyclerViewAdapter adapter = (PopularTrendRecyclerViewAdapter) recyclerView.getAdapter();
            if(adapter == null) {
                return;
            }
            List<String> items = adapter.getItems();
            items.add(message.getResults().get(0).getTitle());
            Collections.sort(items);
            adapter.setItems(items);
        }
    }

    private void onRetrieveTrendError(Response<ShoppingInsightCategoryTrendMessage> response) {
        ResponseBody responseBody = response.errorBody();
        if(responseBody != null) {
            Log.e("RetrieveTrend", responseBody.toString());
        }
    }

    private Callback<ShoppingInsightCategoryTrendMessage> createRetrieveTrendCallback() {
        return new Callback<ShoppingInsightCategoryTrendMessage>() {
            @Override
            public void onResponse(@NonNull Call<ShoppingInsightCategoryTrendMessage> call, @NonNull Response<ShoppingInsightCategoryTrendMessage> response) {
                if(response.isSuccessful()) {
                    onRetrieveTrendSuccess(response);
                } else {
                    onRetrieveTrendError(response);
                }
                hideLoading();
                showList();
            }

            @Override
            public void onFailure(@NonNull Call<ShoppingInsightCategoryTrendMessage> call, @NonNull Throwable t) {
                hideLoading();
                Toast.makeText(getActivity(), "트렌드 정보를 가져오는데 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private List<CategoryParam> getCategoryCode() {
        List<CategoryParam> categoryParams = new ArrayList<>();
        categoryParams.add(new CategoryParam("패션의류", "50000000"));
        categoryParams.add(new CategoryParam("패션잡화", "50000001"));
        categoryParams.add(new CategoryParam("화장품/미용", "50000002"));
        categoryParams.add(new CategoryParam("디지털/가전", "50000003"));
        categoryParams.add(new CategoryParam("가구/인테리어", "50000004"));
        categoryParams.add(new CategoryParam("출산/육아", "50000005"));
        categoryParams.add(new CategoryParam("식품", "50000006"));
        categoryParams.add(new CategoryParam("스포츠/레저", "50000007"));
        categoryParams.add(new CategoryParam("생활/건강", "50000008"));
        categoryParams.add(new CategoryParam("여가/생활편의", "50000009"));
        categoryParams.add(new CategoryParam("면세점", "50000010"));
        return categoryParams;
    }

    private String getStartDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DATE, 1);
        return simpleDateFormat.format(calendar.getTime());
    }

    private String getEndDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return simpleDateFormat.format(calendar.getTime());
    }

    private void showList() {
        View view = getView();
        if(view != null) {
            RecyclerView recyclerView = view.findViewById(R.id.trend_recycler_view);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideList() {
        View view = getView();
        if(view != null) {
            RecyclerView recyclerView = view.findViewById(R.id.trend_recycler_view);
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
}