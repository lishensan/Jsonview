package com.mountain.JsView.recycleview.impl;

import com.mountain.JsView.IJsViewModelBridge;
import com.mountain.JsView.recycleview.DataSource;
import com.mountain.JsView.recycleview.IViewModel;

public class ViewModel implements IViewModel<DataSource, ViewHolder> {
    private IJsViewModelBridge mJsViewModelBridge;

    public ViewModel(DataSource dataSource, IJsViewModelBridge jsViewModelBridge) {
        this.mDataSource = dataSource;
        this.mJsViewModelBridge = jsViewModelBridge;
    }

    private DataSource mDataSource;

    @Override
    public DataSource getDataSource() {
        return mDataSource;
    }

    @Override
    public int getViewType() {
        return (int) mDataSource.getData("view_type");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mJsViewModelBridge.onBindViewHolder(this, holder);
    }

}
