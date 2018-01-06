package com.example.gypc.petsday;

import android.content.Intent;
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
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gypc on 2017/12/7.
 */

public class HotSpotFragment extends Fragment {
    private ImageView addIV;
    private ImageView notificationIV;
    private RecyclerView hotSpotRecyclerView;
    private HotSpotAdapter hotSpotAdapter;
    private EasyRefreshLayout easyRefreshLayout;

    private List<Hotspot> datas;

    private AppContext app;

    private static HotSpotFragment instance;

    private ObjectService objectService = ObjectServiceFactory.getService();

    private int pageNumber = 0;

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
//        final String bmp = "keji";
        datas = app.getInitHotspots();
        Log.i("HotSpotFragment", "onActivityCreated: datas.size() = " + datas.size());

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

                Hotspot item = (Hotspot) adapter.getItem(position);
                Bundle bundle = item.getBundle();
                bundle.putInt("position", position);
                intent.putExtras(bundle);

                getActivity().startActivityForResult(intent, MainActivity.HOTSPOT_DETAIL_REQ_CODE);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRemoteOldHotspot();
                    }
                }, 2000);


            }


            // 下拉刷新，每次刷新都会新出现2个列表项，并且更新到动态列表中
            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRemoteNewHotspot();
                    }
                }, 1000);
            }
        });

        hotSpotRecyclerView.setAdapter(hotSpotAdapter);

    }

    private void getRemoteOldHotspot() {
        objectService
                .getHotspotListByPageNumber(String.valueOf(pageNumber++))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Hotspot>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("HotSpotFragment", "getRemoteOldHotspot: complete");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("HotSpotFragment", "getRemoteOldHotspot", throwable);
                    }

                    @Override
                    public void onNext(List<Hotspot> hotspots) {
                        Log.i("HotSpotFragment", "getRemoteOldHotspot: onNext: list size = " + String.valueOf(hotspots.size()));
                        final List<Hotspot> list = new ArrayList<Hotspot>();
                        refreshDataIntoTail(hotspots);
                        easyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
                            @Override
                            public void complete() {
                                hotSpotAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                            }
                        }, 500);
                    }
                });
    }

    private void getRemoteNewHotspot() {
        objectService
                .getNewestHotspotList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Hotspot>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("HotSpotFragment", "getRemoteNewHotspot: complete");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("HotSpotFragment", "getRemoteNewHotspot", throwable);
                    }

                    @Override
                    public void onNext(List<Hotspot> hotspots) {
                        Log.i("HotSpotFragment", "getRemoteNewHotspot: onNext: list size = " + String.valueOf(hotspots.size()));
                        refreshDataIntoHead(hotspots);
                        //刷新每次都用setNewData重新加载数据
                        hotSpotAdapter.notifyDataSetChanged();
                        easyRefreshLayout.refreshComplete();
                        Toast.makeText(getContext(), "refresh success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean isDuplicateHotspotInDatas(int hotspotId) {
        for (Hotspot item : datas) {
            if (item.getHs_id() == hotspotId)
                return true;
        }
        return false;
    }

    public void refreshDataIntoHead(List<Hotspot> hotspots) {
        List<Hotspot> list = new ArrayList<>();
        list.addAll(hotspots);
        for (Hotspot item : datas) {
            if (!isDuplicateHotspotInDatas(item.getHs_id()))
                list.add(item);
        }
        datas.clear();
        datas.addAll(list);
    }

    public void refreshDataIntoTail(List<Hotspot> hotspots) {
        for (Hotspot item : hotspots) {
            if (!isDuplicateHotspotInDatas(item.getHs_id()))
                datas.add(item);
        }
    }

    public static HotSpotFragment getInstance() {
        return instance;
    }

    public void addHotspot() {
        getRemoteNewHotspot();
    }

    public void initDatas(List<Hotspot> datas) {
        this.datas = datas;
        hotSpotAdapter.setNewData(this.datas);
    }

    public void updateDataFromDetailPage(Bundle data) {
        int position = data.getInt("position");
        int countComment = data.getInt("countComment");
        int countLikeChange = data.getInt("countLikeChange");
        Hotspot item = this.datas.get(position);
        item.setCountComment(countComment);
        item.setCountLike(item.getCountLike() + countLikeChange);
        this.datas.set(position, item);
        hotSpotAdapter.notifyDataSetChanged();
    }
}
