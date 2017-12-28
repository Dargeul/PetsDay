package com.example.gypc.petsday.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.Pet;

import java.util.List;

/**
 * Created by gypc on 2017/12/28.
 */

public class HSDetailPetAdapter extends BaseQuickAdapter<Pet,BaseViewHolder> {
    public HSDetailPetAdapter(@LayoutRes int layoutResId, @Nullable List<Pet> data){
        super(layoutResId, data);
    }
    public HSDetailPetAdapter(@LayoutRes int layoutResId){
        super(layoutResId);
    }
    @Override
    protected void convert(final BaseViewHolder helper, Pet item){
        helper.setText(R.id.petNickName,item.getPet_nickname());
        helper.setText(R.id.petType,item.getPet_type());
        int position = helper.getLayoutPosition();
    }


}
