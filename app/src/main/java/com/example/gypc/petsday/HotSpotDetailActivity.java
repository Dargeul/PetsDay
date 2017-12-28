package com.example.gypc.petsday;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.HSDetailPetAdapter;
import com.example.gypc.petsday.adapter.CommentAdapter;
import com.example.gypc.petsday.model.Comment;
import com.example.gypc.petsday.model.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gypc on 2017/12/28.
 */

public class HotSpotDetailActivity extends AppCompatActivity {
    private List<Comment> comments = new ArrayList<Comment>(){
        {
            add(new Comment(1,1,"Amy","2017-12-28","Nothing"));
            add(new Comment(2,2,"Cmy","2017-12-28","EMMMMMM"));
            add(new Comment(3,3,"Bmy","2017-12-28","EEEEEEE"));
        }
    };

    final String bitmap = "https://f11.baidu.com/it/u=3240141704,604792825&fm=72";
    private List<Pet> pets_choose = new ArrayList<Pet>(){
        {
            add(new Pet(1, "Toto", 1, "Cat",
                    12, "boy", "2017-12-12", bitmap, 666));
        }
    };
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

    private void initWidget(){
        userNicknameTV = (TextView)findViewById(R.id.userNickname);
        publishTimeTV = (TextView)findViewById(R.id.PublishTime);
        hotSpotContentTV = (TextView)findViewById(R.id.hotSpotContent);
        commentAreaRV = (RecyclerView)findViewById(R.id.commentArea);
        petNickNameRV = (RecyclerView)findViewById(R.id.petNickName);
        hotspotImageIV = (ImageView)findViewById(R.id.hotspotImage);
        backIV = (ImageView)findViewById(R.id.back);
        shareIV = (ImageView)findViewById(R.id.share);
        likeIV = (ImageView)findViewById(R.id.like);
        commentIV = (ImageView)findViewById(R.id.comment);
        yourCommentET = (EditText)findViewById(R.id.yourComment);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotspot_detail);

        initWidget();

        LinearLayoutManager layoutManager = new LinearLayoutManager(HotSpotDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        commentAdapter = new CommentAdapter(R.layout.hotspot_detail_comment);
        commentAdapter.addData(comments);
        commentAreaRV.setLayoutManager(layoutManager);
        commentAreaRV.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String str = commentAdapter.getItem(i).getCom_user()+"";
                yourCommentET.requestFocus();
                yourCommentET.setHint("回复 "+str);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(yourCommentET,inputMethodManager.SHOW_IMPLICIT);
            }
        });

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
    }

}
