package com.example.gypc.petsday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.ChoosePetAdapter;
import com.example.gypc.petsday.model.Pet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gypc on 2017/12/13.
 */

public class ChoosePetActivity extends AppCompatActivity {

    private ImageView finishChooseIV;
    private ImageView backIV;
    private RecyclerView petList;
    private ChoosePetAdapter choosePetAdapter;

    private List<Pet> pets;
    private List<Pet> pets_choose;


    public void initWidget(){
        finishChooseIV = (ImageView)findViewById(R.id.finishChoose);
        backIV = (ImageView)findViewById(R.id.back);
        petList = (RecyclerView) findViewById(R.id.petList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotspot_publish_choosepet);

        initWidget();

        //----------测试用------------
        final String bitmap = "https://f11.baidu.com/it/u=3240141704,604792825&fm=72";
        pets = new ArrayList<Pet>(){
            {
                add(new Pet(1, "Toto", 1, "Cat",
                        12, "boy", "2017-12-12", bitmap, 666));
                add(new Pet(1, "Toto", 1, "Cat",
                        12, "boy", "2017-12-12", bitmap, 666));
            }
        };
        pets_choose = new ArrayList<Pet>();
        //----------测试用-----------

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChoosePetActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        choosePetAdapter = new ChoosePetAdapter(R.layout.hotspot_publish_pet);

        choosePetAdapter.addData(pets);
        petList.setLayoutManager(layoutManager);
        petList.setAdapter(choosePetAdapter);

        choosePetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //checkbox状态更改
                CheckBox c = (CheckBox) view.findViewById(R.id.checkbox);
                c.setChecked(!c.isChecked());

                //--------测试数据添加---------
                if(c.isChecked()){
                    pets_choose.add(pets.get(position));
                    Toast.makeText(ChoosePetActivity.this,pets.get(position).getPet_nickname()+"被选中",Toast.LENGTH_SHORT).show();
                }
                else{
                    pets_choose.remove(pets.get(position));
                    Toast.makeText(ChoosePetActivity.this,pets.get(position).getPet_nickname()+"去除选中",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //确定键，点击完数据传回发布动态页面
        finishChooseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
