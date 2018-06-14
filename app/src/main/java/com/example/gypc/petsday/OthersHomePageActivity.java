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
import com.example.gypc.petsday.model.RemoteDBOperationResponse;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.adapter.rxjava.Result;
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
    private int userID;
    private List<Pet> othersPet;
    private List<Pet> followPet;
    private Pet petFollow;
    private boolean isClicked = false;

    private ImageView likeIV;
    private TextView followTV;
    private ObjectService objectService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_homepage);

        objectService = ObjectServiceFactory.getService();
        followPet = AppContext.getInstance().getFollowpets();

        hotspotDeatilInfo = getIntent().getExtras();

        petsListRV = (RecyclerView)findViewById(R.id.petListRV);
        back = (ImageView)findViewById(R.id.back);
        userNickname = (TextView)findViewById(R.id.userNickname);
        userNickname.setText(hotspotDeatilInfo.getString("user_nickname"));

        userID = (int) AppContext.getInstance().getLoginUserInfo().get("user_id");

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(OthersHomePageActivity.this);
        othersPetAdapter = new OthersPetAdapter(R.layout.others_pet);
        petsListRV.setLayoutManager(layoutManager1);
        getOthersPet();
        getFollowPet();
        petsListRV.setAdapter(othersPetAdapter);

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

        othersPetAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                petFollow = othersPetAdapter.getItem(i);
                likeIV = (ImageView)view;
                followTV = (TextView)othersPetAdapter.getViewByPosition(petsListRV,i,R.id.followTV);
                isClicked = true;
                getFollowPet();
            }
        });
    }

    private void changePetState(){
        boolean isFollowed = false;
        for(Pet pet1:othersPet){
            for(Pet pet2:followPet){
                if(pet1.getPet_id() == pet2.getPet_id()){
                    ImageView imageView = (ImageView)othersPetAdapter.getViewByPosition(petsListRV,othersPet.indexOf(pet1),R.id.loveIV);
                    imageView.setImageResource(R.drawable.like_fill);
                    isFollowed = true;
                }
            }
            if(isFollowed == false){
                ImageView imageView = (ImageView)othersPetAdapter.getViewByPosition(petsListRV,othersPet.indexOf(pet1),R.id.loveIV);
                imageView.setImageResource(R.drawable.like);
            }
            else{
                isFollowed = false;
            }
        }
    }

    public void getOthersPet() {
        objectService
                .getPetListForUser(hotspotDeatilInfo.getInt("hs_user"))
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
                        if (pets == null)
                            pets = new ArrayList<>();
                        othersPet = new ArrayList(pets);
                        othersPetAdapter.setNewData(pets);
                        changePetState();
                    }
                });
    }

    public void getFollowPet(){
        objectService
                .getUserFollowPetList(userID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Pet>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("OthersHomePageActivity", "getFollowPet: complete, followpets.size() = " + String.valueOf(followPet.size()));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("OthersHomePageActivity", "getFollowPet", throwable);
                    }

                    @Override
                    public void onNext(List<Pet> pets) {
                        if (pets == null)
                            pets = new ArrayList<>();
                        followPet = new ArrayList(pets);
                        if(isClicked){
                            if(checkIsFollowed() != true){
                                follow(petFollow.getPet_id());
                            }else{
                                unfollow(petFollow.getPet_id());
                            }
                            isClicked = false;
                        }
                        changePetState();
                    }
                });
    }

    private boolean checkIsFollowed(){
        boolean isFollowed = false;
        for(Pet pet:followPet){
            if(petFollow.getPet_id() == pet.getPet_id())
                isFollowed = true;
        }
        return isFollowed;
    }

    private void follow(int petId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("pet_id", String.valueOf(petId));
        data.put("user_id", userID);

        objectService
                .userFansPet(JSONRequestBodyGenerator.getJsonObjBody(data))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<Integer>>() {
                    @Override
                    public void onCompleted() {
                        getFollowPet();
                        getOthersPet();
                        Log.i("PetDetailActivity", "followPet: complete");
                        Toast.makeText(OthersHomePageActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("PetDetailActivity", "followPet", throwable);
                    }

                    @Override
                    public void onNext(Result<Integer> integerResult) {
                        try {
                            likeIV.setImageResource(R.drawable.like_fill);
                        } catch (Exception e) {
                            Log.e("PetDetailActivity", e.getMessage(), e);
                        }
                    }
                });
    }

    private void unfollow(int petId) {
        objectService
                .cancelUserFansPet(
                        petId, userID
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        getFollowPet();
                        getOthersPet();
                        Log.i("PetDetailActivity", "unfollowPet: complete");
                        Toast.makeText(OthersHomePageActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("PetDetailActivity", "unfollowPet", throwable);
                    }

                    @Override
                    public void onNext(Boolean ok) {
                        try {
                            likeIV.setImageResource(R.drawable.like);
                        } catch (Exception e) {
                            Log.e("PetDetailActivity", e.getMessage(), e);
                        }
                    }
                });
    }

    @Override
    protected void onResume(){
        super.onResume();
        getFollowPet();
        getOthersPet();
    }
}
