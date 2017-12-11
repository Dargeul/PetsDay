package com.example.gypc.petsday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by gypc on 2017/12/7.
 */

public class MarketFragment extends Fragment {
    private Banner banner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_market,container,false);
        banner = (Banner) view.findViewById(R.id.banner);

        initBanner();

        return view;
    }

    private void initBanner() {
        //资源文件
        List<Integer> images = new ArrayList<Integer>();
        images.add(R.mipmap.banner1);
        images.add(R.mipmap.banner2);
        images.add(R.mipmap.banner3);
        images.add(R.mipmap.banner4);
        images.add(R.mipmap.banner5);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
}
