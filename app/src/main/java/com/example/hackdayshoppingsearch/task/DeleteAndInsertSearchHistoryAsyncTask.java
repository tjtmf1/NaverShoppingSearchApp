package com.example.hackdayshoppingsearch.task;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.hackdayshoppingsearch.communication.database.DatabaseHelper;
import com.example.hackdayshoppingsearch.container.QueryHistory;

public class DeleteAndInsertSearchHistoryAsyncTask extends BaseAsyncTask<Void, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private QueryHistory queryHistory;
    public DeleteAndInsertSearchHistoryAsyncTask(Context context, QueryHistory queryHistory) {
        this.context = context;
        this.queryHistory = queryHistory;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteSearchHistory(queryHistory);
        databaseHelper.insertSearchHistory(queryHistory);
        databaseHelper.close();
        return null;
    }
}
