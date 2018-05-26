package com.mountain.jsview.recycleview;

import android.util.SparseArray;

public class ViewHolderManager {


    public ViewHolderManager() {
    }

    private SparseArray<IViewHolderCreater> mViewHolderCreaterSparseArray = new SparseArray<>();

    public void registViewHolderCreater(int type, IViewHolderCreater creater) {
        this.mViewHolderCreaterSparseArray.put(type, creater);
    }

    public IViewHolderCreater getViewHolderManager(int type) {
        return this.mViewHolderCreaterSparseArray.get(type);
    }
}
