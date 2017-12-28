package com.example.gypc.petsday.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.Comment;

import java.util.List;

/**
 * Created by gypc on 2017/12/28.
 */

public class CommentAdapter extends BaseQuickAdapter<Comment,BaseViewHolder> {
    public CommentAdapter(@LayoutRes int layoutResId, @Nullable List<Comment> data){
        super(layoutResId, data);
        }

    public CommentAdapter(@LayoutRes int layoutResId){
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper,Comment item){

        helper.setText(R.id.userNickname, item.getCom_user());
        helper.setText(R.id.commentTime,item.getCom_time());
        helper.setText(R.id.comment,item.getCom_content());
        int position = helper.getLayoutPosition();
        helper.setText(R.id.layerNum,position+1+" L");
    }
}
