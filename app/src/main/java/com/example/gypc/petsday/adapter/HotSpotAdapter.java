package com.example.gypc.petsday.adapter;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.utils.ImageUriConverter;

import java.util.List;

/**
 * Created by gypc on 2017/12/21.
 */

public class HotSpotAdapter extends BaseQuickAdapter<Hotspot,BaseViewHolder> {
    public HotSpotAdapter(@LayoutRes int layoutResId, @Nullable List<Hotspot>data){
        super(layoutResId, data);
    }

    /*继承了BaseQucikAdapter，主要是convert函数的填充。
    * 详情可见：http://www.jianshu.com/p/b343fcff51b0
    * */

    @Override
    protected void convert(BaseViewHolder helper,Hotspot item){
        //将xml中的数据项和model里面的数据绑在一起
        helper.setText(R.id.hotSpotContent, item.getHs_content());
//        Glide.with(this.mContext).load(item.getHs_photo()).priority(Priority.HIGH)
//                .into((ImageView) helper.itemView.findViewById(R.id.hotspotImage));

        Uri localImageUri;
        String remoteImagePath;

        localImageUri = ImageUriConverter.getCacheFileUriFromName(this.mContext, item.getHs_photo());
        remoteImagePath = ImageUriConverter.getImgRemoteUriFromName(item.getHs_photo());

        Log.i("HotSpotAdapter", "imageUri: " + ((localImageUri == null) ? remoteImagePath : localImageUri.getPath()));

        Glide
                .with((this.mContext))
                .load(localImageUri != null ? localImageUri : remoteImagePath)
                .placeholder(R.mipmap.pet_default)
                .error(R.drawable.error_img)
                .priority(Priority.HIGH)
                .into((ImageView) helper.itemView.findViewById(R.id.hotspotImage));

        helper.setText(R.id.userNickname, "" + item.getUser_nickname());
        helper.setText(R.id.publishTime, item.getHs_time());
        helper.setText(R.id.likeNum, "" + item.getCountLike());
        helper.setText(R.id.commentNum, "" + item.getCountComment());
        int position = helper.getLayoutPosition();
    }
}
