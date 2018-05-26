package com.mountain.jsview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

public class JsVirtualLinearLayout extends JsVirtualViewGroup<LinearLayout> {


    @Override
    public LinearLayout createNativeView(Context context, ViewGroup.LayoutParams params) {
        LinearLayout linearLayout = new LinearLayout(context);
        List<JsVirtualView> jsVirtualViews = getJsVirtualViews();
        for (JsVirtualView jsVirtualView : jsVirtualViews) {
            View nativeView = jsVirtualView.createNativeView(context, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(nativeView);
        }
        return linearLayout;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);

    }

    public void setLayoutParams(String layoutParamsJson) {
        super.setLayoutParams(layoutParamsJson);
    }
}
