package com.example.gypc.petsday.helper;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.alley.van.helper.VanImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by StellaSong on 2017/12/9.
 */

public class GlideImageLoader extends ImageLoader implements VanImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context).load(path).into(imageView);
        //Picasso.with(context).load(path).into(imageView);
    }

    @Override
    public void loadThumbnail(Context context, int resize, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()  // some .jpeg files are actually gif
                .override(resize, resize)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifThumbnail(Context context, int resize, ImageView imageView,
                                         Uri uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()
                .override(resize, resize)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .into(imageView);
    }

    @Override
    public void loadAnimatedGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asGif()
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }

}
