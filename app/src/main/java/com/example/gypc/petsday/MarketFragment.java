package com.example.gypc.petsday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.GoodAdapter;
import com.example.gypc.petsday.helper.GlideImageLoader;
import com.example.gypc.petsday.model.Good;
import com.example.gypc.petsday.utils.AppContext;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gypc on 2017/12/7.
 */

public class MarketFragment extends Fragment {
    private Banner banner;
    private RecyclerView goodRV;
    private List<Good> goods;
    private GoodAdapter goodAdapter;

    private AppContext app;

    private static MarketFragment instance;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = this;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_market, container, false);
        banner = (Banner) view.findViewById(R.id.banner);
        goodRV = (RecyclerView) view.findViewById(R.id.goodsRV);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app = AppContext.getInstance();
        goods = app.getGoods();
        goods.add(new Good("奶粉",32,1,24,"适合猫咪的奶粉"));
        goods.add(new Good("猫粮",22,2,24,"天然材料，无添加的健康猫粮"));
        goods.add(new Good("项圈",42,3,22,"狗的项圈，具有定位和拍照等功能"));
        goods.add(new Good("猫砂盆",38,4,25,"无"));
        goods.add(new Good("猫砂",30,5,24,"无"));
        goods.add(new Good("洗浴球",82,6,14,"无"));
        goods.add(new Good("口哨",3,7,14,"具有响亮的声音"));
        goods.add(new Good("磨牙棒",32,8,24,"乌龟的磨牙棒"));
        goods.add(new Good("逗猫棒",22,9,54,"适合猫咪，与猫同乐"));
        goods.add(new Good("鸟笼",32,10,4,"规格不一，数量有限"));
        goods.add(new Good("奶粉",32,1,24,"适合猫咪的奶粉"));
        goods.add(new Good("猫粮",22,2,24,"天然材料，无添加的健康猫粮"));
        goods.add(new Good("项圈",42,3,22,"狗的项圈，具有定位和拍照等功能"));
        goods.add(new Good("猫砂盆",38,4,25,"无"));
        goods.add(new Good("猫砂",30,5,24,"无"));
        goods.add(new Good("洗浴球",82,6,14,"无"));
        goods.add(new Good("口哨",3,7,14,"具有响亮的声音"));
        goods.add(new Good("磨牙棒",32,8,24,"乌龟的磨牙棒"));
        goods.add(new Good("逗猫棒",22,9,54,"适合猫咪，与猫同乐"));
        goods.add(new Good("鸟笼",32,10,4,"规格不一，数量有限"));

        MyLayoutManager layoutManager = new MyLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goodRV.setLayoutManager(layoutManager);
        goodAdapter = new GoodAdapter(R.layout.good_item, goods);
        //设置动画
        goodAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置动画循环
        goodAdapter.isFirstOnly(false);
        //设置第一帧没有动画出现
        goodAdapter.setNotDoAnimationCount(0);
        goodRV.setAdapter(goodAdapter);
    }

    public static MarketFragment getInstance() {
        return instance;
    }

    class MyLayoutManager extends LinearLayoutManager {
        public MyLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            final int width = RecyclerView.LayoutManager.chooseSize(widthSpec,
                    getPaddingLeft() + getPaddingRight(),
                    ViewCompat.getMinimumWidth(goodRV));
            final int height = RecyclerView.LayoutManager.chooseSize(heightSpec,
                    getPaddingTop() + getPaddingBottom(),
                    ViewCompat.getMinimumHeight(goodRV));
            setMeasuredDimension(width, height * goods.size());
        }
    }
}
