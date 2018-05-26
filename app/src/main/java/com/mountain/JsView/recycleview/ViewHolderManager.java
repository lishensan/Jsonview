package com.mountain.JsView.recycleview;

import android.util.SparseArray;

import com.mountain.JsView.recycleview.impl.ViewHolderCreater;

public class ViewHolderManager {

    public static final ViewHolderManager manager = new ViewHolderManager();

    public static ViewHolderManager getInstance() {
        return manager;
    }

    private ViewHolderManager() {
        initCreater();
    }

    private void initCreater() {
        registViewHolderCreater(0, new ViewHolderCreater());
    }

    private SparseArray<IViewHolderCreater> mViewHolderCreaterSparseArray = new SparseArray<>();

    public void registViewHolderCreater(int type, IViewHolderCreater creater) {
        this.mViewHolderCreaterSparseArray.put(type, creater);
    }

    public IViewHolderCreater getViewHolderManager(int type) {
        return this.mViewHolderCreaterSparseArray.get(type);
    }
}
