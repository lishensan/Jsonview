package com.mountain.jsview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mountain.jsview.ImageLoader;

public class JsVirtualImageView extends JsVirtualView<ImageView> {

    private int imageAlpha;
    private boolean imageAlphaFlag;

    private Uri mImageUri;
    private boolean mImageUriFlag;
    private String mImageUrL;
    private boolean mImageUrLFlag;

    private Drawable mImageDrawable;
    private boolean mImageDrawableFlag;

    private Bitmap mImageBitmap;
    private boolean mImageBitmapFlag;


    public void setImageURI(@Nullable Uri uri) {
        mImageUri = uri;
        this.mImageUriFlag = true;
    }
    public void setImageUrl(@Nullable String url) {
        mImageUrL = url;
        this.mImageUrLFlag = true;
    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        this.mImageDrawable = drawable;
        this.mImageDrawableFlag = true;
    }

    public void setImageBitmap(Bitmap bm) {
        this.mImageBitmap = bm;
        this.mImageBitmapFlag = true;
    }

    public void setImageAlpha(int alpha) {
        this.imageAlphaFlag = true;
        this.imageAlpha = alpha;
    }

    @Override
    public void updateToNativeView(ImageView imageView) {
        super.updateToNativeView(imageView);
        if (imageAlphaFlag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView.setImageAlpha(imageAlpha);
            }
        }
        if (mImageBitmapFlag) {
            imageView.setImageBitmap(mImageBitmap);
        }
        if (mImageDrawableFlag) {
            imageView.setImageDrawable(mImageDrawable);
        }
        if (mImageUriFlag) {
            imageView.setImageURI(mImageUri);
        }
        if (mImageUrLFlag) {
            ImageLoader.loadImage(imageView, mImageUrL);
        }
    }

    @Override
    public ImageView createNativeView(Context context, ViewGroup.LayoutParams params) {
        return new ImageView(context);
    }
}
