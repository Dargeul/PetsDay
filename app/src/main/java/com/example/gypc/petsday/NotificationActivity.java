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
import com.example.gypc.petsday.model.RemoteDBOperationResponse;
import com.example.gypc.petsday.model.UserNotification;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;

import java.util.ArrayList;
import java.util.HashMap;
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
    private ImageView backIV;

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
        backIV = (ImageView)findViewById(R.id.back);
        tips = (TextView)findViewById(R.id.tips);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNotificationState();
                finish();
            }
        });
    }

    private void getNotification(){
        userId = (int) app.getLoginUserInfo().get("user_id");
        objectService
                .getNotificationByUserId(userId+"")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UserNotification>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NotificationActivity","getNotification",e);
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


    public void getHotSpot(String hotspotId){
        objectService
                .getHotspotById(hotspotId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Hotspot>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NotificationActivity","getHotSpot",e);
                    }

                    @Override
                    public void onNext(List<Hotspot> hotspots) {
                        try{
                            Intent intent = new Intent(NotificationActivity.this,HotSpotDetailActivity.class);

                            Bundle bundle = hotspots.get(0).getBundle();
                            bundle.putBoolean("fromNotificationActivity", true);
                            intent.putExtras(bundle);

                            startActivityForResult(intent,MainActivity.HOTSPOT_DETAIL_REQ_CODE);
                        }catch (Exception e){
                            Log.e("NotificationActivity","getHotSpot",e);
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
               getHotSpot(String.valueOf(notificationAdapter.getItem(i).getCom_hs()));
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
                getHotSpot(String.valueOf(notificationReadedAdapter.getItem(i).getCom_hs()));
            }
        });
        notificationsReadedRV.setAdapter(notificationReadedAdapter);
    }

    private void changeNotificationState(){
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for(UserNotification userNotification:notificationsNotReaded){
            HashMap<String, Object> UserNotificationData = new HashMap<>();
            UserNotificationData.put("notice_status",1);
            UserNotificationData.put("notice_user",userNotification.getNotice_user());
            UserNotificationData.put("user_nickname",userNotification.getUser_nickname());
            UserNotificationData.put("notice_comment",userNotification.getNotice_comment());
            UserNotificationData.put("notice_id",userNotification.getNotice_id());
            UserNotificationData.put("com_time",userNotification.getCom_time());
            UserNotificationData.put("com_user",userNotification.getCom_user());
            UserNotificationData.put("com_hs",userNotification.getCom_hs());
            UserNotificationData.put("com_content",userNotification.getCom_content());

            objectService
                    .haveReadNotification(JSONRequestBodyGenerator.getJsonObjBody(UserNotificationData))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RemoteDBOperationResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("NotificationActivity","changeNotificationState",e);
                        }

                        @Override
                        public void onNext(RemoteDBOperationResponse remoteDBOperationResponse) {
                            if(!remoteDBOperationResponse.isSuccess()){
                                Toast.makeText(NotificationActivity.this,"通知状态改变失败",Toast.LENGTH_SHORT);
                            }
                            else{
                                Toast.makeText(NotificationActivity.this,"通知状态改变成功",Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        changeNotificationState();
        finish();
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
