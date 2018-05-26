package com.mountain.JsView.recycleview.impl;

import android.view.ViewGroup;
import android.widget.TextView;

import com.mountain.JsView.recycleview.IViewHolderCreater;

public class ViewHolderCreater implements IViewHolderCreater{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        ViewHolder viewHolder = new ViewHolder(textView);
        viewHolder.addView(textView);
        return viewHolder;
    }
}
