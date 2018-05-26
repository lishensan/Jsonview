package com.mountain.jsview.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.google.gson.JsonObject;
import com.mountain.jsview.JsonUtil;

public class JsVirtualView<T extends View> implements INativeViewCreator<T> {

    protected Drawable background;
    private boolean backgroundFlag;
    protected ViewGroup.LayoutParams params;
    private boolean paramsFlag;
    protected float alpha;
    private boolean alphaFlag;
    protected Animation animation;
    private boolean animationFlag;
    protected View.OnClickListener mOnClickListener;
    private boolean mOnClickListenerFlag;
    protected View.OnLongClickListener mOnLongClickListener;
    private boolean mOnLongClickListenerFlag;
    protected int backgroundColor;
    private boolean backgroundColorFlag;
    protected Drawable foreground;
    private boolean foregroundFlag;
    protected int visibility;
    private boolean visibilityFlag;
    protected int id;
    private boolean idFlag;
    private int viewType;


    protected LayoutParams mLayoutParams;

    public static class LayoutParams {
        public Integer width;
        public Integer height;
        public Integer align;
        public Integer innnerAlign;

    }

    public final void setLayoutParams(String layoutParamsJson) {
        mLayoutParams = createLayoutParams(layoutParamsJson);
    }

    protected LayoutParams createLayoutParams(String layoutParamsJson) {
        if (parent != null) {
            return parent.createLayoutParams(layoutParamsJson);
        }
        return JsonUtil.fromJson(layoutParamsJson, LayoutParams.class);
    }

    public void batchSetAttr() {

    }


    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    private JsVirtualView parent;

    public void setParent(JsVirtualView parent) {
        this.parent = parent;
    }

    public JsVirtualView getParent() {
        return parent;
    }

    public void setBackgroundDrawable(Drawable background) {
        backgroundFlag = true;
        this.background = background;
    }

    public void setAlpha(float alpha) {
        alphaFlag = true;
        this.alpha = alpha;
    }

    public void setAnimation(Animation animation) {
        animationFlag = true;
        this.animation = animation;
    }

    public void setOnClickListener(@Nullable View.OnClickListener l) {
        mOnClickListenerFlag = true;
        this.mOnClickListener = l;
    }

    public void setOnLongClickListener(@Nullable View.OnLongClickListener l) {
        mOnLongClickListenerFlag = true;
        this.mOnLongClickListener = l;
    }


    public void setBackgroundColor(int color) {
        backgroundColorFlag = true;
        this.backgroundColor = color;
    }

    public void setForeground(Drawable foreground) {
        foregroundFlag = true;
        this.foreground = foreground;
    }

    public void setVisibility(int visibility) {
        visibilityFlag = true;
        this.visibility = visibility;
    }


    public void setId(int id) {
        idFlag = true;
        this.id = id;
    }


    public void updateToNativeView(T nativeView) {
        if (nativeView != null) {
            if (backgroundFlag) {
                nativeView.setBackgroundDrawable(background);
            }
            if (paramsFlag) {
                nativeView.setLayoutParams(params);
            }
            if (alphaFlag) {
                nativeView.setAlpha(alpha);
            }
            if (animationFlag) {
                nativeView.setAnimation(animation);
            }
            if (mOnClickListenerFlag) {
                nativeView.setOnClickListener(mOnClickListener);
            }
            if (backgroundColorFlag) {
                nativeView.setBackgroundColor(backgroundColor);
            }
            if (foregroundFlag) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    nativeView.setForeground(foreground);
                }
            }
            if (mOnLongClickListenerFlag) {
                nativeView.setOnLongClickListener(mOnLongClickListener);
            }
            if (visibilityFlag) {
                nativeView.setVisibility(visibility);
            }
            if (idFlag) {
                nativeView.setId(id);
            }
            updateLayoutParams(nativeView, mLayoutParams);
        }
    }

    protected void updateLayoutParams(View nativeView, LayoutParams vLayoutParams) {
        if (parent != null) {
            //父类处理属性，更新到子控件
            parent.updateLayoutParams(nativeView, vLayoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = nativeView.getLayoutParams();
            if (layoutParams != null && vLayoutParams != null) {
                if (vLayoutParams.width != null) {
                    layoutParams.width = vLayoutParams.width;
                }
                if (vLayoutParams.height != null) {
                    layoutParams.height = vLayoutParams.height;
                }
                //TODO:
            }
        }
    }

    @Override
    public T createNativeView(Context context, ViewGroup.LayoutParams params) {
        return (T) new View(context);
    }
}
