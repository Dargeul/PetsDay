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
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.utils.ImageUriConverter;

import java.util.List;

/**
 * Created by gypc on 2017/12/31.
 */

public class OthersPetAdapter extends BaseQuickAdapter<Pet,BaseViewHolder> {

    public OthersPetAdapter (@LayoutRes int layoutResId, @Nullable List<Pet> data){
        super(layoutResId, data);
    }
    public OthersPetAdapter (@LayoutRes int layoutResId){
            super(layoutResId);
    }
    @Override
    protected void convert(final BaseViewHolder helper, Pet item){
        helper.setText(R.id.petnameTV,item.getPet_nickname());
        helper.setText(R.id.pettypeTV,item.getPet_type());
        helper.setText(R.id.followTV,item.getPet_follow()+"");

        Uri localImageUri;
        String remoteImagePath;

        localImageUri = ImageUriConverter.getCacheFileUriFromName(this.mContext, item.getPet_photo());
        remoteImagePath = ImageUriConverter.getImgRemoteUriFromName(item.getPet_photo());

        Glide
                .with((this.mContext))
                .load(localImageUri != null ? localImageUri : remoteImagePath)
                .placeholder(R.mipmap.pet_default)
                .error(R.drawable.error_img)
                .priority(Priority.HIGH)
                .into((ImageView) helper.itemView.findViewById(R.id.headIV));

        int position = helper.getLayoutPosition();
    }
}

