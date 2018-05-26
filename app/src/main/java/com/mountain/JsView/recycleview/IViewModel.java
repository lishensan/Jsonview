package com.mountain.JsView.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface IViewModel<T, VH extends RecyclerView.ViewHolder> {
    T getDataSource();

    int getViewType();

    void onBindViewHolder(VH holder, int position);

}
