package com.mountain.jsview.recycleview.impl;

import android.view.View;
import android.view.ViewGroup;

import com.mountain.jsview.recycleview.DataSource;
import com.mountain.jsview.recycleview.IViewHolderCreater;
import com.mountain.jsview.recycleview.IViewModel;
import com.mountain.jsview.widget.JsVirtualView;

public class ViewModel implements IViewModel<DataSource, ViewHolder>, IViewHolderCreater {
    private JsVirtualView mJsVirtualView;

    public ViewModel(JsVirtualView jsVirtualView) {
        this.mJsVirtualView = jsVirtualView;
    }


    @Override
    public int getViewType() {
        return mJsVirtualView.getViewType();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mJsVirtualView.updateToNativeView(holder.itemView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View nativeView = mJsVirtualView.createNativeView(parent.getContext(), null);
        return new ViewHolder(nativeView);
    }
}
