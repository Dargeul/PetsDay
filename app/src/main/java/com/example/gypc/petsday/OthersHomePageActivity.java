package com.example.gypc.petsday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.HSDetailPetAdapter;
import com.example.gypc.petsday.adapter.OthersPetAdapter;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.service.ObjectService;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gypc on 2017/12/31.
 */

public class OthersHomePageActivity extends AppCompatActivity {
    private OthersPetAdapter othersPetAdapter;
    private RecyclerView petsListRV;
    private ImageView back;
    private TextView userNickname;

    private Bundle hotspotDeatilInfo;

    private List<Pet> othersPet;

    private ObjectService objectService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_homepage);

        objectService = ObjectServiceFactory.getService();
        hotspotDeatilInfo = getIntent().getExtras();

        petsListRV = (RecyclerView)findViewById(R.id.petListRV);
        back = (ImageView)findViewById(R.id.back);
        userNickname = (TextView)findViewById(R.id.userNickname);

        userNickname.setText(hotspotDeatilInfo.getString("user_nickname"));

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(OthersHomePageActivity.this);
        othersPetAdapter = new OthersPetAdapter(R.layout.others_pet);
        petsListRV.setLayoutManager(layoutManager1);
        getOthersPet();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击列表项弹出toast
        othersPetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(OthersHomePageActivity.this, PetDetailActivity.class);

                Pet item = othersPet.get(position);
                Bundle bundle1 = item.getBundle();

                Log.i("OthersHomePageActivity","count: " + String.valueOf(item.getCount()));
                Log.i("OthersHomePageActivity","pet_photo: " + item.getPet_photo());

                intent.putExtras(bundle1);

                OthersHomePageActivity.this.startActivity(intent);

                Toast.makeText(OthersHomePageActivity.this, "OthersPet:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOthersPet() {
        objectService
                .getPetListForUser(
                        String.valueOf(hotspotDeatilInfo.getInt("hs_user")),
                        String.valueOf(ObjectServiceFactory.GET_OWN_PET_STATUS_CODE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Pet>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("OthersHomePageActivity", "getOthersPet: complete, followpets.size() = " + String.valueOf(othersPet.size()));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("OthersHomePageActivity", "getOthersPet", throwable);
                    }

                    @Override
                    public void onNext(List<Pet> pets) {
                        othersPet = new ArrayList(pets);
                        othersPetAdapter.addData(pets);
                        petsListRV.setAdapter(othersPetAdapter);
                    }
                });
    }
}
