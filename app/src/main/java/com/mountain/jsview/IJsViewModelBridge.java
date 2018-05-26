package com.mountain.jsview;

import com.mountain.jsview.recycleview.impl.ViewHolder;
import com.mountain.jsview.recycleview.impl.ViewModel;

public interface IJsViewModelBridge<VH extends ViewHolder> {

//    VH onCreateView(String dataSource);


    void onBindViewHolder(ViewModel model, ViewHolder holder);

}
