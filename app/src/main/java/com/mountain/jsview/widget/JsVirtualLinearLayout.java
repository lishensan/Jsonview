package com.mountain.jsview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mountain.jsview.JsonUtil;

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

    //    public static final int HORIZONTAL = 0;
//    public static final int VERTICAL = 1;
    private Integer mOrientation;

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    @Override
    public void updateToNativeView(LinearLayout nativeView) {
        super.updateToNativeView(nativeView);
        if (mOrientation != null) {
            nativeView.setOrientation(mOrientation);
        }
    }

    @Override
    protected void updateLayoutParams(View childView, JsVirtualView.LayoutParams vLayoutParams) {
        super.updateLayoutParams(childView, vLayoutParams);
        if (vLayoutParams instanceof LayoutParams) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childView.getLayoutParams();
            layoutParams.weight = ((LayoutParams) vLayoutParams).weight;
        }
    }

    public static class LayoutParams extends JsVirtualView.LayoutParams {
        public int weight;
    }

    @Override
    protected JsVirtualView.LayoutParams createLayoutParams(String layoutParamsJson) {
        return JsonUtil.fromJson(layoutParamsJson, LayoutParams.class);
    }
}
