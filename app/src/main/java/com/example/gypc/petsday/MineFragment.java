package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gypc.petsday.adapter.FollowPetAdapter;
import com.example.gypc.petsday.adapter.MyPetAdapter;
import com.example.gypc.petsday.model.Pet;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by gypc on 2017/12/7.
 */

public class MineFragment extends Fragment {
    private ImageView editIV;
    private LinearLayout addpetLL;
    private RecyclerView mypetRV;
    private RecyclerView followpetRV;

    private List<Pet> mypets;
    private List<Pet> followpets;
    private MyPetAdapter myPetAdapter;
    private FollowPetAdapter followPetAdapter;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine,container,false);
        context = this.getContext();

        // 绑定xml
        editIV = (ImageView)view.findViewById(R.id.editIV);
        addpetLL = (LinearLayout)view.findViewById(R.id.addpetLL);
        mypetRV = (RecyclerView)view.findViewById(R.id.mypetRV);
        followpetRV = (RecyclerView)view.findViewById(R.id.followpetRV);

        //列表初始化
        mypets = new ArrayList<Pet>();
        followpets = new ArrayList<Pet>();
        mypets.add(new Pet("Toto", "Cat", 666));
        mypets.add(new Pet("Juju", "Rabbit", 777));
        followpets.add(new Pet("Coco", "gia", 766));
        followpets.add(new Pet("Pipi", "bip", 766));
        followpets.add(new Pet("Giga", "oew", 766));

        // 设置adapter
        mypetRV.setLayoutManager(new LinearLayoutManager(context));
        myPetAdapter = new MyPetAdapter(mypets, context);
        mypetRV.setAdapter(myPetAdapter);
        followpetRV.setLayoutManager(new LinearLayoutManager(context));
        followPetAdapter = new FollowPetAdapter(followpets, context);
        followpetRV.setAdapter(followPetAdapter);

        addpetLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewpetActivity.class);
                startActivity(intent);
            }
        });

        // 设置myPet列表点击监听事件
        myPetAdapter.setOnItemClickListener(new MyPetAdapter.OnItemClickListener() {
            @Override
            public void onClickList(int position) {
                Toast.makeText(context, "myPet列表第" + position + "项被点击了", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClickEditButton(int position) {
                Toast.makeText(context, "myPet列表第" + position + "项编辑按钮被点击了", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClickDeleteButton(int position) {
                Toast.makeText(context, "myPet列表第" + position + "项删除按钮被点击了", Toast.LENGTH_LONG).show();
            }
        });

        // 设置followPet列表点击监听事件
        followPetAdapter.setOnItemClickListener(new FollowPetAdapter.OnItemClickListener() {
            @Override
            public void onClickList(int position) {
                Toast.makeText(context, "followPet列表第" + position + "项被点击了", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClickDeleteButton(int position) {
                Toast.makeText(context, "followPet列表第" + position + "项删除按钮被点击了", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
