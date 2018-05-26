package com.mountain.JsView.recycleview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.mountain.JsView.recycleview.impl.ViewHolder;
import com.mountain.JsView.recycleview.impl.ViewModel;

import java.util.List;

public class JsRecycleViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<ViewModel> mViewModels;

    public JsRecycleViewAdapter() {
    }

    public void setViewModels(List<ViewModel> viewModels) {
        mViewModels = viewModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolderManager.getInstance().getViewHolderManager(viewType).onCreateViewHolder(parent, viewType);
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
