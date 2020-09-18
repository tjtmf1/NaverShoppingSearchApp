package com.example.hackdayshoppingsearch.communication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.hackdayshoppingsearch.container.QueryHistory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SearchHistory.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
                        HistoryEntry._ID + " INTEGER PRIMARY KEY, " +
                        HistoryEntry.COLUMN_NAME_ID + " TEXT, " +
                        HistoryEntry.COLUMN_NAME_QUERY + " TEXT, " +
                        HistoryEntry.COLUMN_NAME_TIMESTAMP + " INTEGER)";
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<QueryHistory> getSearchHistory(String id) {
        final String query =
                "SELECT * FROM " + HistoryEntry.TABLE_NAME +
                        " WHERE " + HistoryEntry.COLUMN_NAME_ID + "=\'" + id + "\'" +
                        " ORDER BY " + HistoryEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<QueryHistory> queryHistories = new ArrayList<>();
        while(cursor.moveToNext()) {
            String rowId = cursor.getString(cursor.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_ID));
            String rowQuery = cursor.getString(cursor.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_QUERY));
            long rowTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_TIMESTAMP));
            queryHistories.add(new QueryHistory(rowId, rowQuery, rowTimestamp));
        }
        cursor.close();
        return queryHistories;
    }

    public void insertSearchHistory(QueryHistory history) {
        ContentValues value = new ContentValues();
        value.put(HistoryEntry.COLUMN_NAME_ID, history.getId());
        value.put(HistoryEntry.COLUMN_NAME_QUERY, history.getQuery());
        value.put(HistoryEntry.COLUMN_NAME_TIMESTAMP, history.getTimestamp());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(HistoryEntry.TABLE_NAME, null, value);
        db.close();
    }

    public void deleteSearchHistory(QueryHistory history) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(HistoryEntry.TABLE_NAME, HistoryEntry.COLUMN_NAME_ID + "='" + history.getId() + "' AND " +
                HistoryEntry.COLUMN_NAME_QUERY + "='" + history.getQuery() + "'", null);
        db.close();
    }

    public static class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_QUERY = "searchQuery";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }
}
