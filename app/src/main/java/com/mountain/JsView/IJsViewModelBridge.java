package com.mountain.JsView;

import com.mountain.JsView.recycleview.impl.ViewHolder;
import com.mountain.JsView.recycleview.impl.ViewModel;

public interface IJsViewModelBridge<VH extends ViewHolder> {

//    VH onCreateView(String dataSource);


    void onBindViewHolder(ViewModel model, ViewHolder holder);

}
