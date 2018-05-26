package com.mountain.jsview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface INativeViewCreator<T extends View> {

    T createNativeView(Context context, ViewGroup.LayoutParams params);
}
