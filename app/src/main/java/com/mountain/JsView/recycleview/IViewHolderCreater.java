package com.mountain.JsView.recycleview;

import android.view.ViewGroup;

import com.mountain.JsView.recycleview.impl.ViewHolder;
import com.mountain.JsView.recycleview.impl.ViewModel;

public interface IViewHolderCreater {
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
}
