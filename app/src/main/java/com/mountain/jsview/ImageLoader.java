package com.mountain.jsview;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {
    public static void loadImage(ImageView imageView, String url) {
        int resourceId = R.mipmap.ic_launcher;
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(resourceId) // 加载过程中显示的图片drawable(resource)
                .error(resourceId) // 加载错误则显示这个
                .into(imageView);
    }
}
