package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.NotificationAdapter;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.model.UserNotification;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gypc on 2017/12/13.
 */

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView notificationsRV;
    private RecyclerView notificationsReadedRV;
    private TextView tips;
    private ImageView back;

    private NotificationAdapter notificationAdapter;
    private NotificationAdapter notificationReadedAdapter;
    private List<UserNotification> notifications;//通知列表
    private List<UserNotification> notificationsNotReaded;//未读列表
    private List<UserNotification> notificationsReaded;//已读列表

    private AppContext app;
    private ObjectService objectService;
    private int userId;


    private static NotificationActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();

        //网络请求
        getNotification();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(){
        instance = NotificationActivity.this;

        objectService = ObjectServiceFactory.getService();
        app = AppContext.getInstance();
        notifications = app.getNotifications();

        notificationsNotReaded = new ArrayList<UserNotification>();
        notificationsReaded = new ArrayList<UserNotification>();

        notificationsRV = (RecyclerView)findViewById(R.id.notificationRV);
        notificationsReadedRV = (RecyclerView)findViewById(R.id.notificationReadedRV);

        tips = (TextView)findViewById(R.id.tips);
        back = (ImageView)findViewById(R.id.back);
    }

    private void getNotification(){
        userId = (int) app.getLoginUserInfo().get("user_id");
        objectService
                .getNotificationByUserId(userId + "")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UserNotification>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NotificationActivity","getNotification", e);
                    }

                    @Override
                    public void onNext(List<UserNotification> userNotifications) {
                        try{
                            notifications.removeAll(notifications);
                            notifications.addAll(userNotifications);
                            //区分已读和未读通知
                            identifyNotification();
                            //设置两种通知列表的adapter
                            setNoticeAdapter();
                        }catch (Exception e){
                            Log.e("NotificationActivity","getNotification",e);
                        }
                    }
                });
    }

    private void identifyNotification(){
        if(notifications.size() == 0){
            tips.setText("---当前没有任何通知---");
        }else{
            tips.setText("---以下是已读通知---");
        }
        for(int index = 0; index < notifications.size(); index++){
            if(notifications.get(index).getNotice_status() == 0){
                notificationsNotReaded.add(notifications.get(index));
            } else {
                notificationsReaded.add(notifications.get(index));
            }
        }
    }

    private void setNoticeAdapter(){
        MyLayoutManager layoutManager = new MyLayoutManager(NotificationActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationsRV.setLayoutManager(layoutManager);
        notificationAdapter = new NotificationAdapter(R.layout.notification_item, notificationsNotReaded);
        //设置动画
        notificationAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置动画循环
        notificationAdapter.isFirstOnly(false);
        //设置第一帧没有动画出现
        notificationAdapter.setNotDoAnimationCount(0);
        notificationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                jumpToHotspotDdetail(notificationsNotReaded.get(i).getCom_hs());
                Toast.makeText(NotificationActivity.this, "notificationsNotReaded:" + i, Toast.LENGTH_SHORT).show();
            }
        });
        notificationsRV.setAdapter(notificationAdapter);


        //设置已读的列表
        MyLayoutManagerForReaded myLayoutManager = new MyLayoutManagerForReaded(NotificationActivity.this);
        myLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationsReadedRV.setLayoutManager(myLayoutManager);
        notificationReadedAdapter = new NotificationAdapter(R.layout.notification_item_readed, notificationsReaded);
        notificationReadedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(NotificationActivity.this,HotSpotDetailActivity.class);
                startActivity(intent);
            }
        });
        notificationsReadedRV.setAdapter(notificationReadedAdapter);
    }

    class MyLayoutManager extends LinearLayoutManager {
        public MyLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            final int width = RecyclerView.LayoutManager.chooseSize(widthSpec,
                    getPaddingLeft() + getPaddingRight(),
                    ViewCompat.getMinimumWidth(notificationsRV));
            final int height = RecyclerView.LayoutManager.chooseSize(heightSpec,
                    getPaddingTop() + getPaddingBottom(),
                    ViewCompat.getMinimumHeight(notificationsRV));
            setMeasuredDimension(width, height * notificationsNotReaded.size());
        }
    }

    private void jumpToHotspotDdetail(final int hs_id) {
        objectService
                .getHotspotById(String.valueOf(hs_id))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Hotspot>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("NotificationActivity", "getHotspotById", throwable);
                    }

                    @Override
                    public void onNext(List<Hotspot> hs) {
                        if (!hs.isEmpty()) {
                            Intent intent = new Intent(NotificationActivity.this, HotSpotDetailActivity.class);

                            Bundle bundle = hs.get(0).getBundle();
                            bundle.putInt("hs_id", hs_id);
                            bundle.putBoolean("fromHotspotFragment", false);
                            intent.putExtras(bundle);

                            NotificationActivity.this.startActivityForResult(intent, MainActivity.HOTSPOT_DETAIL_REQ_CODE);

                        }
                    }
                });
    }

    //使Recyclerview一定程度上自适应。
    class MyLayoutManagerForReaded extends LinearLayoutManager {
        public MyLayoutManagerForReaded(Context context) {
            super(context);
        }
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            final int width = RecyclerView.LayoutManager.chooseSize(widthSpec,
                    getPaddingLeft() + getPaddingRight(),
                    ViewCompat.getMinimumWidth(notificationsReadedRV));
            final int height = RecyclerView.LayoutManager.chooseSize(heightSpec,
                    getPaddingTop() + getPaddingBottom(),
                    ViewCompat.getMinimumHeight(notificationsReadedRV));
            setMeasuredDimension(width, height * notificationsReaded.size());
        }
    }
}
