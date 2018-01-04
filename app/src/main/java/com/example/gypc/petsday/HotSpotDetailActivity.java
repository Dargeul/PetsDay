package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.HSDetailPetAdapter;
import com.example.gypc.petsday.adapter.CommentAdapter;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Comment;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.ImageUriConverter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gypc on 2017/12/28.
 */

public class HotSpotDetailActivity extends AppCompatActivity {

    private ObjectService objectService = ObjectServiceFactory.getService();

    private List<Comment> commentsList = new ArrayList<>();

    private List<Pet> pets_choose = new ArrayList<>();


    private HSDetailPetAdapter hsDetailPetAdapter; //因为绑定的数据相同，所以就使用这个adapter，后面如果有修改的话，再一起改动。
    private CommentAdapter commentAdapter;

    private TextView userNicknameTV;
    private TextView publishTimeTV;
    private TextView hotSpotContentTV;
    private RecyclerView commentAreaRV;
    private RecyclerView petNickNameRV;
    private ImageView hotspotImageIV;
    private ImageView backIV;
    private ImageView shareIV;
    private ImageView likeIV;
    private ImageView commentIV;
    private EditText yourCommentET;

    private Bundle hotspotInfo;

    private void initWidget(){
        userNicknameTV = (TextView)findViewById(R.id.userNickname);
        publishTimeTV = (TextView)findViewById(R.id.PublishTime);
        hotSpotContentTV = (TextView)findViewById(R.id.hotSpotContent);
        commentAreaRV = (RecyclerView)findViewById(R.id.commentArea);
        petNickNameRV = (RecyclerView)findViewById(R.id.petNickName);
        hotspotImageIV = (ImageView)findViewById(R.id.hotspotDetailImageView);
        backIV = (ImageView)findViewById(R.id.back);
        shareIV = (ImageView)findViewById(R.id.share);
        likeIV = (ImageView)findViewById(R.id.like);
        commentIV = (ImageView)findViewById(R.id.comment);
        yourCommentET = (EditText)findViewById(R.id.yourComment);

//        userNicknameTV.setText(hotspotInfo.getString("user_nickname"));
        hotSpotContentTV.setText(hotspotInfo.getString("hs_content"));
        publishTimeTV.setText(hotspotInfo.getString("hs_time"));

        try {
            Uri imageUri = ImageUriConverter.getCacheFileUriFromName(this, hotspotInfo.getString("hs_photo"));
            if (imageUri == null)
                throw new Exception("no image frome cache");
            displayImageFromCache(imageUri);
        } catch (Exception e) {
            Log.e("HotSpotDetailActivity", "initWidget", e);
            String path = ImageUriConverter.getImgRemoteUriFromName(hotspotInfo.getString("hs_photo"));
            displayImageFromRemote(path);
        }
    }

    private void displayImageFromCache(Uri uri) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .into(hotspotImageIV);
    }

    private void displayImageFromRemote(String path) {
        Glide.with(this)
                .load(path)
                .asBitmap()
                .placeholder(R.drawable.error_img)
                .error(R.drawable.error_img)
                .into(hotspotImageIV);
    }

    private void initCommentList() {
        objectService
                .getCommentListByHotspot(String.valueOf(hotspotInfo.getInt("hs_id")))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("HotSpotDetailActivity", "initCommentList: complete");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("HotSpotDetailActivity", "initCommentList", throwable);
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        Log.i("HotSpotDetailActivity", "initCommentList: comments.size() = " + comments.size());
                        commentsList = comments;
                        commentAdapter.setNewData(commentsList);
                    }
                });
    }

    private void initPetList() {
        objectService
                .getPetListForHotspot(String.valueOf(hotspotInfo.getInt("hs_id")))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Pet>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("HotSpotDetailActivity", "initPetList: complete");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("HotSpotDetailActivity", "initPetList", throwable);
                    }

                    @Override
                    public void onNext(List<Pet> pets) {
                        Log.i("HotSpotDetailActivity", "initPetList: pets.size() = " + pets.size());
                        pets_choose = pets;
                        hsDetailPetAdapter.setNewData(pets_choose);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotspot_detail);

        hotspotInfo = getIntent().getExtras();

        initWidget();

        LinearLayoutManager layoutManager = new LinearLayoutManager(HotSpotDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        commentAdapter = new CommentAdapter(R.layout.hotspot_detail_comment);
        commentAdapter.addData(commentsList);
        commentAreaRV.setLayoutManager(layoutManager);
        commentAreaRV.setAdapter(commentAdapter);

        initCommentList();
        initPetList();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(HotSpotDetailActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        hsDetailPetAdapter = new HSDetailPetAdapter(R.layout.hotspot_detail_pet);
        hsDetailPetAdapter.addData(pets_choose);
        petNickNameRV.setLayoutManager(layoutManager1);
        petNickNameRV.setAdapter(hsDetailPetAdapter);

        commentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourCommentET.requestFocus();
                yourCommentET.setHint("你的评论");
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(yourCommentET,inputMethodManager.SHOW_IMPLICIT);
            }
        });

        userNicknameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotSpotDetailActivity.this,OthersHomePageActivity.class);
                startActivity(intent);
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
