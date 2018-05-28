package com.mountain.jsview.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class JsListView extends ListView {
    public JsListView(Context context) {
        this(context,null);
    }

    public JsListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(JsListViewAdapter adapter) {
        super.setAdapter(adapter);
    }
}
