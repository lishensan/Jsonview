package com.mountain.JsView;

import android.util.Log;

import com.google.gson.Gson;
import com.mountain.JsView.recycleview.DataSource;
import com.mountain.JsView.recycleview.impl.ViewHolder;
import com.mountain.JsView.recycleview.impl.ViewModel;

public class JsViewModelBridge implements IJsViewModelBridge<ViewHolder> {
    public static Gson gson = new Gson();
    private JsInterface mJsInterface;
    private static final String TAG = "JsViewModelBridge";
    public JsViewModelBridge(JsInterface jsInterface) {
        this.mJsInterface = jsInterface;
    }

//    @Override
//    public ViewModel.ViewHolder onCreateView(String dataSource) {
//        mJsEngine.invokeJsFunc("onCreateView", dataSource);
//        return null;
//    }

    @Override
    public void onBindViewHolder(ViewModel model, ViewHolder holder) {
        int holderCode = mJsInterface.registObj(holder);
        DataSource dataSource = model.getDataSource();
        String dataJson = gson.toJson(dataSource);
        Log.d(TAG,dataJson);
        mJsInterface.getJsEngine().invokeJsFunc("onBindViewHolder", String.valueOf(holderCode), dataJson);
    }

}
