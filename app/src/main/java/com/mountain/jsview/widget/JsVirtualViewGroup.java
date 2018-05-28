package com.mountain.jsview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mountain.jsview.exception.JsViewException;

import java.util.ArrayList;
import java.util.List;

public abstract class JsVirtualViewGroup<T extends ViewGroup> extends JsVirtualView<T> {

    private List<JsVirtualView> mJsVirtualViews = new ArrayList<>(8);

    protected List<JsVirtualView> getJsVirtualViews() {
        return mJsVirtualViews;
    }

    public void addView(JsVirtualView virtualView) {
        if (virtualView.getParent() != null) {
            throw new JsViewException(virtualView + " has parent");
        }
        virtualView.setParent(this);
        mJsVirtualViews.add(virtualView);
    }

    public JsVirtualView getChildAt(int index) {
        return mJsVirtualViews.get(index);
    }

    public boolean removeView(JsVirtualView jsVirtualView) {
        if (mJsVirtualViews.remove(jsVirtualView)) {
            jsVirtualView.setParent(null);
            return true;
        }
        return false;
    }

    public int getCount() {
        return mJsVirtualViews.size();
    }


    @Override
    public abstract T createNativeView(Context context, ViewGroup.LayoutParams params);

    @Override
    public void updateToNativeView(T nativeView) {
        super.updateToNativeView(nativeView);
        int childCount = nativeView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View nativeChildView = nativeView.getChildAt(i);
            JsVirtualView childAt = getChildAt(i);
            childAt.updateToNativeView(nativeChildView);
        }
    }
}
