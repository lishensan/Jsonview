package com.mountain.jsview.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

public class JsVirtualTextView extends JsVirtualView<TextView> {
    private int mTextColor;
    private boolean mTextColorFlag;
    private CharSequence mText;
    private boolean mTextflag;

    private float mTextSize;
    private boolean mTextSizeFlag;


    public void setTextColor(int color) {
        this.mTextColor = color;
        this.mTextColorFlag = true;
    }

    public void setText(String string){
        this.mText = string;
        this.mTextflag = true;

    }
    public void setText(CharSequence string){
        this.mText = string;
        this.mTextflag = true;
    }


    public void setTextSize(float size) {
        this.mTextSize = size;
        this.mTextSizeFlag = true;
    }

    @Override
    public void updateToNativeView(TextView textView) {
        super.updateToNativeView(textView);
        if (mTextSizeFlag) {
            textView.setTextSize(mTextSize);
        }
        if (mTextColorFlag) {
            textView.setTextColor(mTextColor);
        }
        if (mTextflag){
            textView.setText(mText);
        }
    }

    @Override
    public TextView createNativeView(Context context, ViewGroup.LayoutParams params) {
        return new TextView(context);
    }
}
