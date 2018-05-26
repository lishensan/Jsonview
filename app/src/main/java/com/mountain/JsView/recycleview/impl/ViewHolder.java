package com.mountain.JsView.recycleview.impl;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ViewHolder extends RecyclerView.ViewHolder {
    List<View> mViews = new ArrayList<>(8);

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public void addView(View view) {
        this.mViews.add(view);
    }

    @SuppressWarnings("unused")
    public View getViewByPostition(int pos) {
        return mViews.get(pos);
    }
}