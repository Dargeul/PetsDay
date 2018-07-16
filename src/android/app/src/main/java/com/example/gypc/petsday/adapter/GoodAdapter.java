package com.example.gypc.petsday.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.Good;

import java.util.List;

/**
 * Created by gypc on 2017/12/30.
 */

public class GoodAdapter extends BaseQuickAdapter<Good,BaseViewHolder> {
    public GoodAdapter(@LayoutRes int layoutResId, @Nullable List<Good> data){
        super(layoutResId, data);
    }
    public GoodAdapter(@LayoutRes int layoutResId){
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Good item){
        helper.setText(R.id.goodnameTV,item.getGood_name());
        helper.setText(R.id.goodpriceTV,"￥ "+item.getGood_price());
        helper.setText(R.id.goodcountTV,"共"+item.getGood_count()+" 个");
        helper.setText(R.id.goodinfoTV,item.getGood_info());
        int position = helper.getLayoutPosition();
    }


}
