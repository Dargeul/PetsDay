package com.example.gypc.petsday.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.hotspot;

import java.util.List;

/**
 * Created by gypc on 2017/12/21.
 */

public class HotSpotAdapter extends BaseQuickAdapter<hotspot,BaseViewHolder> {
    public HotSpotAdapter(@LayoutRes int layoutResId, @Nullable List<hotspot>data){
        super(layoutResId, data);
    }

    /*继承了BaseQucikAdapter，主要是convert函数的填充。
    * 详情可见：http://www.jianshu.com/p/b343fcff51b0
    * */

    @Override
    protected void convert(BaseViewHolder helper,hotspot item){
        //将xml中的数据项和model里面的数据绑在一起
        helper.setText(R.id.hotSpotContent,item.getHs_content())
                .setImageBitmap(R.id.hotspotImage,item.getHs_photo());
        int position = helper.getLayoutPosition();
    }
}
