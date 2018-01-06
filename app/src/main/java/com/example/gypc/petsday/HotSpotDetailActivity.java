package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gypc.petsday.adapter.HSDetailPetAdapter;
import com.example.gypc.petsday.adapter.CommentAdapter;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Comment;
import com.example.gypc.petsday.model.HotspotLike;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.model.RemoteDBOperationResponse;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;
import com.example.gypc.petsday.utils.ImageUriConverter;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gypc on 2017/12/28.
 */

public class HotSpotDetailActivity extends AppCompatActivity {

    public static final int UPDATE_HOTSPOT_ITEM_SUCCESS = 1;

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
    private ImageButton shareImageBtn;
    private ImageButton likeImageBtn;
    private ImageButton commentImageBtn;
    private EditText yourCommentET;
    private Button submitCommentBtn;

    private Bundle hotspotInfo;
    private int hotspotId;
    private int userId;
    private boolean isCommentOK = false;
    private int newComId;
    private boolean isNotificationSentOk = false;
    private boolean isHotspotLike;
    private int likeId;
    private boolean isLikeOK = false;
    private boolean isCancelLikeOK = false;
    private String userNickname;
    private boolean initIsHotspotLike;

    private void initWidget(){
        userNicknameTV = (TextView)findViewById(R.id.userNickname);
        publishTimeTV = (TextView)findViewById(R.id.PublishTime);
        hotSpotContentTV = (TextView)findViewById(R.id.hotSpotContent);
        commentAreaRV = (RecyclerView)findViewById(R.id.commentArea);
        petNickNameRV = (RecyclerView)findViewById(R.id.petNickName);
        hotspotImageIV = (ImageView)findViewById(R.id.hotspotDetailImageView);
        backIV = (ImageView)findViewById(R.id.back);
        shareImageBtn = (ImageButton) findViewById(R.id.shareImageBtn);
        likeImageBtn = (ImageButton) findViewById(R.id.likeImageBtn);
        commentImageBtn = (ImageButton)findViewById(R.id.commentImageBtn);
        yourCommentET = (EditText)findViewById(R.id.yourComment);
        submitCommentBtn = (Button)findViewById(R.id.submitCommentBtn);

        userNicknameTV.setText(hotspotInfo.getString("user_nickname"));
        hotSpotContentTV.setText(hotspotInfo.getString("hs_content"));
        publishTimeTV.setText(hotspotInfo.getString("hs_time"));

        try {
            Uri imageUri = ImageUriConverter.getCacheFileUriFromName(this, hotspotInfo.getString("hs_photo"));
            if (imageUri == null)
                throw new Exception("no image from cache");
            displayImageFromCache(imageUri);
        } catch (Exception e) {
            Log.e("HotSpotDetailActivity", "initWidget", e);
            String path = ImageUriConverter.getImgRemoteUriFromName(hotspotInfo.getString("hs_photo"));
            displayImageFromRemote(path);
        }

        likeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLike();
            }
        });

        submitCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });
    }

    private void toggleLike() {
        isHotspotLike = !isHotspotLike;
        if (isHotspotLike) {
            likeHotspot();
        } else {
            cancelLikeHotspot();
        }
    }

    private void cancelLikeHotspot() {
        objectService
                .cancelLike(String.valueOf(likeId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemoteDBOperationResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.i("HotSpotDetailActivity", "cancelLikeHotspot: complete");
                        if (isCancelLikeOK) {
                            likeImageBtn.setBackgroundResource(R.drawable.praise);
                            AppContext.getInstance().cancelLikeHotspot(hotspotId);
                        } else {
                            msgNotify("操作失败，请重试！");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("HotSpotDetailActivity", "cancelLikeHotspot: onError", throwable);
                    }

                    @Override
                    public void onNext(RemoteDBOperationResponse response) {
                        isCancelLikeOK = response.isSuccess();
                    }
                });
    }

    private void likeHotspot() {
        isLikeOK = false;
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("like_user", userId);
        dataMap.put("like_hotspot", hotspotId);

        objectService
                .likeHotspot(JSONRequestBodyGenerator.getJsonObjBody(dataMap))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<Integer>>() {
                    @Override
                    public void onCompleted() {
                        if (isLikeOK) {
                            likeImageBtn.setBackgroundResource(R.drawable.praise_fill);
                            AppContext.getInstance().likeHotspot(new HotspotLike(likeId, hotspotId, userId));
                        } else {
                            msgNotify("操作失败，请重试！");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("HotSpotDetailActivity", "likeHotspot: onError", throwable);
                    }

                    @Override
                    public void onNext(Result<Integer> integerResult) {
                        if (integerResult.isError()) {
                            Log.e("HotSpotDetailActivity", "likeHotspot: onNext", integerResult.error());
                        }
                        if (integerResult.response() == null || integerResult.response().body() == null)
                            return;
                        likeId = integerResult.response().body();
                        isLikeOK = true;
                    }
                });
    }

    private void msgNotify(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void submitComment() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(yourCommentET.getWindowToken(), 0);
        if (isCommentOK) {
            submitCommentBtn.setEnabled(false);
            sendCommendNotification(newComId);
            return;
        }
        isCommentOK = false;
        final String commentText = yourCommentET.getText().toString();
        if (commentText.isEmpty()) {
            msgNotify("请输入评论内容！");
        } else {
            Log.i("HotSpotDetailActivity", "submitComment: 评论提交中");

            submitCommentBtn.setEnabled(false);

            final String commentTime = getTimeString();
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("com_time", commentTime);
            dataMap.put("com_user", userId);
            dataMap.put("com_hs", hotspotId);
            dataMap.put("com_content", commentText);

            objectService
                    .insertComment(
                            JSONRequestBodyGenerator.getJsonObjBody(dataMap)
                    )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result<Integer>>() {
                        @Override
                        public void onCompleted() {
                            submitCommentBtn.setEnabled(true);
                            if (isCommentOK) {
                                sendCommendNotification(newComId);
                            } else {
                                msgNotify("评论提交失败，请重试！");
                                submitCommentBtn.setEnabled(true);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.e("HotSpotDetailActivity", "submitComment", throwable);
                        }

                        @Override
                        public void onNext(Result<Integer> integerResult) {
                            if (integerResult.isError()) {
                                Log.e("HotSpotDetailActivity", "submitComment", integerResult.error());
                            }
                            if (integerResult.response() == null || integerResult.response().body() == null)
                                return;
                            isCommentOK = true;
                            newComId = integerResult.response().body();
                            commentsList.add(new Comment(newComId, hotspotId, userId, commentTime, commentText, userNickname));
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void sendCommendNotification(int commendId) {
        isNotificationSentOk = false;

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("notice_status", ObjectServiceFactory.SEND_COMMENT_NOTIFICATION_STATUS_CODE);
        dataMap.put("notice_user", hotspotInfo.getInt("hs_user"));
        dataMap.put("notice_comment", commendId);

        objectService
                .postNotification(JSONRequestBodyGenerator.getJsonObjBody(dataMap))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<Integer>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("HotSpotDetailActivity", "sendCommendNotification: complete");
                        submitCommentBtn.setEnabled(true);
                        if (isNotificationSentOk) {
                            isCommentOK = false;
                            msgNotify("评论成功！");
                            yourCommentET.clearFocus();
                            yourCommentET.setText("");
                        } else {
                            msgNotify("评论提交失败，请重试！");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("HotSpotDetailActivity", "sendCommendNotification: onError", throwable);
                    }

                    @Override
                    public void onNext(Result<Integer> integerResult) {
                        if (integerResult.isError()) {
                            Log.e("HotSpotDetailActivity", "sendCommendNotification: onNext", integerResult.error());
                        }
                        if (integerResult.response() == null || integerResult.response().body() == null)
                            return;
                        int notificationId = integerResult.response().body();
                        if (notificationId >= 0)
                            isNotificationSentOk = true;
                    }
                });
    }

    private String getTimeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String time = dateFormat.format(new Date());
        return time;
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
                        commentsList.addAll(comments);
                        Log.i("HotSpotDetailActivity", "initCommentList: commentsList.size() = " + commentsList.size());
                        commentAdapter.setNewData(commentsList);
                    }
                });
    }

    private void initPetList() {
        objectService
                .getPetListForHotspot(String.valueOf(hotspotId))
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
                        pets_choose.addAll(pets);
                        Log.i("HotSpotDetailActivity", "initPetList: pets_choose.size() = " + pets_choose.size());
                        hsDetailPetAdapter.setNewData(pets_choose);
                    }
                });
    }

    private void initLikeStatus() {
        initIsHotspotLike = isHotspotLike = false;
        List<HotspotLike> likeList = AppContext.getInstance().getInitLikeList();
        for (HotspotLike like : likeList) {
            if (like.getLike_hotspot() == hotspotId) {
                isHotspotLike = true;
                likeId = like.getLike_id();
            }
        }
        if (isHotspotLike) {
            initIsHotspotLike = true;
            likeImageBtn.setBackgroundResource(R.drawable.praise_fill);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotspot_detail);

        hotspotInfo = getIntent().getExtras();
        hotspotId = hotspotInfo.getInt("hs_id");
        userId = (int) AppContext.getInstance().getLoginUserInfo().get("user_id");
        userNickname = hotspotInfo.getString("user_nickname");

        initWidget();
        initLikeStatus();

        LinearLayoutManager layoutManager = new LinearLayoutManager(HotSpotDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        commentAdapter = new CommentAdapter(R.layout.hotspot_detail_comment);
        commentAdapter.addData(commentsList);
        commentAreaRV.setLayoutManager(layoutManager);
        commentAreaRV.setAdapter(commentAdapter);

        MyLayoutManager layoutManager1 = new MyLayoutManager(HotSpotDetailActivity.this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        hsDetailPetAdapter = new HSDetailPetAdapter(R.layout.hotspot_detail_pet);
        hsDetailPetAdapter.addData(pets_choose);
        petNickNameRV.setLayoutManager(layoutManager1);
        petNickNameRV.setAdapter(hsDetailPetAdapter);

        commentImageBtn.setOnClickListener(new View.OnClickListener() {
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
                goBackMainPage();
            }
        });

        initCommentList();
        initPetList();
    }

    @Override
    public void onBackPressed() {
        goBackMainPage();
    }

    private void goBackMainPage() {
        Bundle data = new Bundle();
        if (hotspotInfo.getBoolean("fromHotspotFragment")) {
            data.putInt("position", hotspotInfo.getInt("position"));
            data.putBoolean("fromHotspotFragment", true);
        }
        else
            data.putInt("hs_id", hotspotId);
        data.putInt("countComment", commentsList.size());
        int countLikeChange = 0;
        if (initIsHotspotLike && !isHotspotLike)
            countLikeChange = -1;
        else if (!initIsHotspotLike && isHotspotLike)
            countLikeChange = 1;
        data.putInt("countLikeChange", countLikeChange);

        Intent intent = new Intent();
        intent.putExtras(data);
        setResult(UPDATE_HOTSPOT_ITEM_SUCCESS, intent);
        finish();
    }

    class MyLayoutManager extends LinearLayoutManager {
        public MyLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            final int width = RecyclerView.LayoutManager.chooseSize(widthSpec,
                    getPaddingLeft() + getPaddingRight(),
                    ViewCompat.getMinimumWidth(commentAreaRV));
            final int height = RecyclerView.LayoutManager.chooseSize(heightSpec,
                    getPaddingTop() + getPaddingBottom(),
                    ViewCompat.getMinimumHeight(commentAreaRV));
            setMeasuredDimension(width, height * 3);
        }
    }
}
