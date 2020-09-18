package com.example.hackdayshoppingsearch.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public static final int VIEWTYPE_DEFAULT = 0;
    public static final int VIEWTYPE_HEADER = -1;
    public static final int VIEWTYPE_FOOTER = -2;
    //TODO VIewHolder concrete class 들의 memory leak?
    public final View view;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public abstract void bindData(T t);
}
