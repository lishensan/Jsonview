package com.mountain.jsview;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {
    public static void loadImage(ImageView imageView, String url) {

        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
