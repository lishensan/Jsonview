package com.mountain.jsview.listview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mountain.jsview.recycleview.ViewHolderManager;
import com.mountain.jsview.recycleview.impl.ViewHolder;
import com.mountain.jsview.recycleview.impl.ViewModel;

import java.util.Collections;
import java.util.List;

public class JsListViewAdapter extends BaseAdapter {
    private List<ViewModel> mViewModels = Collections.emptyList();
    private ViewHolderManager mViewHolderManager;

    public JsListViewAdapter() {
        mViewHolderManager = new ViewHolderManager();
    }

    public void setViewModels(List<ViewModel> viewModels) {
        mViewModels = viewModels;
        for (ViewModel viewModel : mViewModels) {
            int viewType = viewModel.getViewType();
            mViewHolderManager.registViewHolderCreater(viewType, viewModel);
        }
    }

    @Override
    public int getCount() {
        return mViewModels.size();
    }

    @Override
    public ViewModel getItem(int position) {
        return mViewModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 100;
    }

    @Override
    public int getItemViewType(int position) {
        return mViewModels.get(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        ViewHolder viewHolder = mViewHolderManager.getViewHolderManager(itemViewType).onCreateViewHolder(parent, itemViewType);
        ViewModel viewModel = mViewModels.get(position);
        viewModel.onBindViewHolder(viewHolder, position);
        viewHolder.itemView.setTag(itemViewType);
        return viewHolder.itemView;
    }
}
