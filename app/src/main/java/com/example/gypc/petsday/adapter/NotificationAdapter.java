package com.example.gypc.petsday.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.Notification;

import java.util.List;

/**
 * Created by gypc on 2017/12/30.
 */

public class NotificationAdapter  extends BaseQuickAdapter<Notification,BaseViewHolder> {
    public NotificationAdapter(@LayoutRes int layoutResId, @Nullable List<Notification> data){
        super(layoutResId, data);
    }

    public NotificationAdapter(@LayoutRes int layoutResId){
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper,Notification item){
        helper.setText(R.id.userNickname, item.getCom_user());
        helper.setText(R.id.commentTime,item.getCom_time());
        helper.setText(R.id.comment,item.getCom_content());
        int position = helper.getLayoutPosition();
    }
}