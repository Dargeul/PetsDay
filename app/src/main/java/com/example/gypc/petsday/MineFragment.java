package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Date;
import java.util.List;

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

    public static final int ADD_NEW_PET_REQ_CODE = 1;

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
        final String bitmap = "https://f11.baidu.com/it/u=3240141704,604792825&fm=72";
        mypets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy", new Date(), bitmap, 666));
        mypets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy", new Date(), bitmap, 666));
        followpets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy", new Date(), bitmap, 666));
        followpets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy", new Date(), bitmap, 666));
        followpets.add(new Pet(1, "Toto", 1, "Cat",
                12, "boy", new Date(), bitmap, 666));

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
                startActivityForResult(intent, MineFragment.ADD_NEW_PET_REQ_CODE);
            }
        });

        // 设置myPet列表点击监听事件
        myPetAdapter.setOnItemClickListener(new MyPetAdapter.OnItemClickListener() {
            @Override
            public void onClickList(int position) {
                Toast.makeText(context, "myPet列表第" + position + "项被点击了", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), PetDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onClickEditButton(int position) {
                Toast.makeText(context, "myPet列表第" + position + "项编辑按钮被点击了", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), NewpetActivity.class);
                startActivity(intent);
            }

            @Override
            public void onClickDeleteButton(int position) {
                Toast.makeText(context, "myPet列表第" + position + "项删除按钮被点击了", Toast.LENGTH_LONG).show();
                mypets.remove(position);
                myPetAdapter.notifyDataSetChanged();
            }
        });

        // 设置followPet列表点击监听事件
        followPetAdapter.setOnItemClickListener(new FollowPetAdapter.OnItemClickListener() {
            @Override
            public void onClickList(int position) {
                Toast.makeText(context, "followPet列表第" + position + "项被点击了", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), PetDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onClickDeleteButton(int position) {
                Toast.makeText(context, "followPet列表第" + position + "项删除按钮被点击了", Toast.LENGTH_LONG).show();
                followpets.remove(position);
                followPetAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
    }
}
