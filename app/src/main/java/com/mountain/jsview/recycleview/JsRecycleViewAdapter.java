package com.mountain.jsview.recycleview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.mountain.jsview.recycleview.impl.ViewHolder;
import com.mountain.jsview.recycleview.impl.ViewModel;

import java.util.List;

public class JsRecycleViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<ViewModel> mViewModels;
    private ViewHolderManager mViewHolderManager;

    public JsRecycleViewAdapter() {
        mViewHolderManager = new ViewHolderManager();
    }

    public void setViewModels(List<ViewModel> viewModels) {
        mViewModels = viewModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mViewHolderManager.getViewHolderManager(viewType).onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        ViewModel viewModel = mViewModels.get(position);
        int viewType = viewModel.getViewType();
        mViewHolderManager.registViewHolderCreater(viewType, viewModel);
        return viewType;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ViewModel viewModel = mViewModels.get(position);
        viewModel.onBindViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mViewModels.size();
    }
}
