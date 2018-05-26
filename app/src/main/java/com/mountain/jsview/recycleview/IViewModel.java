package com.mountain.jsview.recycleview;

import android.support.v7.widget.RecyclerView;

public interface IViewModel<T, VH extends RecyclerView.ViewHolder> {
    int getViewType();
    void onBindViewHolder(VH holder, int position);

}
