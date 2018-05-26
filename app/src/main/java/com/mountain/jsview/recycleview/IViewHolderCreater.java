package com.mountain.jsview.recycleview;

import android.view.ViewGroup;

import com.mountain.jsview.recycleview.impl.ViewHolder;

public interface IViewHolderCreater {
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
}
