package com.example.hackdayshoppingsearch.task;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.hackdayshoppingsearch.communication.database.DatabaseHelper;
import com.example.hackdayshoppingsearch.container.QueryHistory;

import java.util.List;

public class SelectSearchHistoryAsyncTask extends BaseAsyncTask<Void, Void, List<QueryHistory>> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String id;
    public SelectSearchHistoryAsyncTask(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    @Override
    protected List<QueryHistory> doInBackground(Void... voids) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getSearchHistory(id);
    }
}
