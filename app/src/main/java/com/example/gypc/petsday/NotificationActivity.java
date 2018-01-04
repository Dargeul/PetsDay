package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.GoodAdapter;
import com.example.gypc.petsday.adapter.NotificationAdapter;
import com.example.gypc.petsday.model.Notification;
import com.example.gypc.petsday.model.Notification;
import com.example.gypc.petsday.utils.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gypc on 2017/12/13.
 */

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView notificationsRV;
    private RecyclerView notificationsReadedRV;
    private NotificationAdapter notificationAdapter;
    private NotificationAdapter notificationReadedAdapter;
    private List<Notification> notifications;//通知列表
    private List<Notification> notificationsNotReaded;//未读列表
    private List<Notification> notificationsReaded;//已读列表

    private AppContext app;

    private static NotificationActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        app = AppContext.getInstance();
//        notificationsRV = (RecyclerView)findViewById(R.id.notificationRV);
//        notificationsReadedRV = (RecyclerView)findViewById(R.id.notificationReadedRV);

        //获取通知列表，按照notice_status分为已读和未读两个通知列表。
        notifications = app.getNotifications();

        notificationsNotReaded = new ArrayList<Notification>();
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsNotReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsReaded = new ArrayList<Notification>();
        notificationsReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));
        notificationsReaded.add(new Notification(1,1,1,1,1,"Amy","2017-12-28","Nothing"));

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
                Intent intent = new Intent(NotificationActivity.this,HotSpotDetailActivity.class);
                startActivity(intent);
            }
        });
        notificationsRV.setAdapter(notificationAdapter);


        //设置已读的列表
        MyLayoutManagerForReaded myLayoutManager = new MyLayoutManagerForReaded(NotificationActivity.this);
        myLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationsReadedRV.setLayoutManager(myLayoutManager);
//        notificationReadedAdapter = new NotificationAdapter(R.layout.notification_item_readed, notificationsReaded);
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
