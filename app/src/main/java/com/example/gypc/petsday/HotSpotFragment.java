package com.example.gypc.petsday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.HotSpotAdapter;
import com.example.gypc.petsday.model.hotspot;
import com.example.gypc.petsday.utils.AppContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gypc on 2017/12/7.
 */

public class HotSpotFragment extends Fragment {
    private ImageView addIV;
    private ImageView notificationIV;
    private RecyclerView hotSpotRecyclerView;
    private HotSpotAdapter hotSpotAdapter;
    private EasyRefreshLayout easyRefreshLayout;

    private List<hotspot> datas;

    private AppContext app;

    private static HotSpotFragment instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        instance = this;
        return inflater.inflate(R.layout.fragment_hotspot, container, false);
    }

    public void initWidget(){
        addIV =(ImageView) getView().findViewById(R.id.publish);
        notificationIV = (ImageView)getView().findViewById(R.id.notification);
        hotSpotRecyclerView = (RecyclerView)getView().findViewById(R.id.hotSpotRecyclerView);
        easyRefreshLayout = (EasyRefreshLayout)getView().findViewById(R.id.easylayout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app = AppContext.getInstance();

        initWidget();

        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PublishActivity.class);
                getActivity().startActivityForResult(intent, MainActivity.NEW_HOTSPOT_REQ_CODE);
            }
        });

        notificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NotificationActivity.class);
                startActivity(intent);
            }
        });

        /*
        * 以下是动态数据列表和Adapter的设置。
        * 动态列表的数据类型是hotspot这个model
        * */
        //final Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.pet_photo_1);
        final String bmp = "keji";
        datas = app.getDatas();
        datas.add(new hotspot(new Date().toString(), 1, "I like it", 1, bmp, 888, 666, true));
        datas.add(new hotspot(new Date().toString(), 1, "I like it", 1, bmp, 888, 666, true));
        datas.add(new hotspot(new Date().toString(), 1, "I like it", 1, bmp, 888, 666, true));
        datas.add(new hotspot(new Date().toString(), 1, "I like it", 1, bmp, 888, 666, true));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hotSpotRecyclerView.setLayoutManager(layoutManager);
        hotSpotAdapter = new HotSpotAdapter(R.layout.hotspot_item_others, datas);
        //设置动画
        hotSpotAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置动画循环
        hotSpotAdapter.isFirstOnly(false);
        //设置第一帧没有动画出现
        hotSpotAdapter.setNotDoAnimationCount(0);
        //点击列表项弹出toast
        hotSpotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(),HotSpotDetailActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "HotSpot:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        /*
        *设置上拉加载，每次加载会添加2个列表项，并且更新到动态列表中
        *之后写到网络请求也在这里面写
         */
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                final List<hotspot> list = new ArrayList<>();
                //final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pet_photo_2);
                final String bitmap = "keji";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //添加2个列表项到动态的数据列表中
                        for (int j = 0; j < 2; j++) {
                            list.add(new hotspot(new Date().toString(), 1, "Loaded", 1, bitmap, 888, 666, true));
                            datas.add(new hotspot(new Date().toString(), 1, "Loaded", 1, bitmap, 888, 666, true));;
                        }
                        easyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
                            @Override
                            public void complete() {
                                hotSpotAdapter.getData().addAll(list);
                                hotSpotAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                            }
                        }, 500);

                    }
                }, 2000);


            }


            // 下拉刷新，每次刷新都会新出现2个列表项，并且更新到动态列表中
            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //添加2个列表项到动态的数据列表中
                        //final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pet_photo_3);
                        final String bitmap = "keji";
                        List<hotspot> list = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            list.add(new hotspot(new Date().toString(), 1, "Refresh", 1, bitmap, 888, 666, true));
                        }
                        list.addAll(datas);
                        datas.removeAll(datas);
                        datas.addAll(list);
                        //刷新每次都用setNewData重新加载数据
                        hotSpotAdapter.setNewData(list);
                        easyRefreshLayout.refreshComplete();
                        Toast.makeText(getContext(), "refresh success", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });

        hotSpotRecyclerView.setAdapter(hotSpotAdapter);

    }

    public static HotSpotFragment getInstance() {
        return instance;
    }

    public void addHotspot(Bundle bundle) {
        /*
        * bundle.putInt("id", hotspotId);
            bundle.putString("content", publishContent);
            bundle.putString("photo", imageFilename);
            bundle.putString("time", publishTime);
        * */
        String data =
                "id = " + String.valueOf(bundle.getInt("id")) + "\n" +
                "content = " + bundle.getString("content") + "\n" +
                "photo = " + bundle.getString("photo") + "\n" +
                "time = " + bundle.getString("time");
        Log.i("HotSpotFragment", "addHotspot: data:\n" + data);
    }
}
