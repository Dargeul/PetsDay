package com.example.gypc.petsday.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.gypc.petsday.R;
import com.example.gypc.petsday.model.Pet;

import java.util.List;

/**
 * Created by gypc on 2017/12/26.
 */

public class ChoosePetAdapter extends BaseQuickAdapter<Pet,BaseViewHolder> {
    public ChoosePetAdapter(@LayoutRes int layoutResId, @Nullable List<Pet> data){
        super(layoutResId, data);
    }
    public ChoosePetAdapter(@LayoutRes int layoutResId){
        super(layoutResId);
    }
    @Override
    protected void convert(final BaseViewHolder helper, Pet item){
        helper.setText(R.id.petNickName,item.getPet_nickname());
        int position = helper.getLayoutPosition();
    }


}
