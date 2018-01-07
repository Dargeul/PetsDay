package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.gypc.petsday.adapter.HotSpotAdapter;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.model.RemoteDBOperationResponse;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;
import com.example.gypc.petsday.utils.ImageUriConverter;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;

import java.util.ArrayList;
import java.util.HashMap;
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
import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by StellaSong on 2017/12/19.
 */

public class PetDetailActivity extends AppCompatActivity {
    private LineChartView lineChart;
    private RecyclerView hotSpotRecyclerView;
    private HotSpotAdapter hotSpotAdapter;

    private List<Hotspot> datas;

    String[] date = {"5-23","5-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","1-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","4-22","9-22","10-22","11-22","zxc"};//X轴的标注
    int[] score= {74,22,18,79,20,74,20,74,42,90,74,42,90,50,42,90,33,10,74,22,18,79,20,74,22,18,79,20};//图表的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    private ImageView petAvatarImageView;
    private TextView petTypeTextView;
    private TextView petWeightTextView;
    private TextView petBirthTextView;
    private TextView petSexTextView;
    private ImageButton backBtn;
    private ImageView followIV;
    private TextView detail_followTV;

    private int petId;
    private boolean islike;
    private int countLike;
    private String userID;
    private String ownerID;

    private ObjectService objectService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_detail);

        objectService = ObjectServiceFactory.getService();

        lineChart = (LineChartView)findViewById(R.id.chart);
        hotSpotRecyclerView = (RecyclerView)findViewById(R.id.petHotSpot);

        petAvatarImageView = (ImageView)findViewById(R.id.petAvatar);
        petTypeTextView = (TextView)findViewById(R.id.petType);
        petWeightTextView = (TextView)findViewById(R.id.petWeight);
        petBirthTextView = (TextView)findViewById(R.id.petBirth);
        petSexTextView = (TextView)findViewById(R.id.petSex);
        backBtn = (ImageButton)findViewById(R.id.backBtn);
        followIV = (ImageView)findViewById(R.id.followIV);
        detail_followTV = (TextView)findViewById(R.id.detail_followTV);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle dataBundle = getIntent().getExtras();
        petId = dataBundle.getInt("pet_id");
        String imageFilename = dataBundle.getString("pet_photo");
        try {
            Uri avatarUri = ImageUriConverter.getCacheFileUriFromName(this, imageFilename);
            if (avatarUri == null)
                throw new Exception("no image from cache");
            Glide.with(this)
                    .load(avatarUri)
                    .asBitmap()
                    .into(petAvatarImageView);
        } catch (Exception e) {
            Log.e("PetDetailActivity", "onCreate", e);
            String path = ImageUriConverter.getImgRemoteUriFromName(imageFilename);
            Glide.with(this)
                    .load(path)
                    .asBitmap()
                    .into(petAvatarImageView);
        }
        petTypeTextView.setText(dataBundle.getString("pet_type"));
        petWeightTextView.setText(dataBundle.getString("pet_weight"));
        petBirthTextView.setText(dataBundle.getString("pet_birth"));
        petSexTextView.setText(dataBundle.getString("pet_sex"));
        detail_followTV.setText(dataBundle.getString("count"));
        userID = AppContext.getInstance().getLoginUserInfo().get("user_id").toString();
        ownerID = String.valueOf(dataBundle.getInt("pet_owner"));
        islike = false;
        countLike = Integer.valueOf(dataBundle.getString("count")).intValue();
        if (!userID.equals(ownerID))
            checkIsFollowedPet(); //判断是否是未关注的宠物，若是将爱心设为空

        followIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 未关注且不是自己的宠物，可以点击关注
                if (!userID.equals(ownerID)) {
                    if (!islike) {
                        followPet();
                    } else {
                        unfollowPet();
                    }
                }
            }
        });

        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化

        /*
        * 以下是动态数据列表和Adapter的设置。
        * 动态列表的数据类型是hotspot这个model
        * */
        datas = new ArrayList<>();

        MyLayoutManager layoutManager = new MyLayoutManager(PetDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hotSpotRecyclerView.setLayoutManager(layoutManager);
        hotSpotAdapter = new HotSpotAdapter(R.layout.hotspot_item_others, datas);
        //设置动画
        hotSpotAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置动画循环
        hotSpotAdapter.isFirstOnly(false);
        //设置第一帧没有动画出现
        hotSpotAdapter.setNotDoAnimationCount(0);
        //点击列表项弹出toast
        hotSpotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(PetDetailActivity.this,HotSpotDetailActivity.class);

                Hotspot item = (Hotspot) adapter.getItem(position);
                Bundle bundle = item.getBundle();
                bundle.putInt("position", position);
                bundle.putBoolean("fromHotspotFragment", false);
                intent.putExtras(bundle);

                PetDetailActivity.this.startActivityForResult(intent, MainActivity.HOTSPOT_DETAIL_REQ_CODE);
                Toast.makeText(PetDetailActivity.this, "HotSpot:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        hotSpotRecyclerView.setAdapter(hotSpotAdapter);
        initHotspotList();
    }

    private void initHotspotList() {
        ObjectServiceFactory.getService()
                .getHotspotByPetId(String.valueOf(petId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Hotspot>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("PetDetailActivity", "initHotspotList: complete");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("PetDetailActivity", "initHotspotList", throwable);
                    }

                    @Override
                    public void onNext(List<Hotspot> hotspots) {
                        datas.addAll(hotspots);
                        hotSpotAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void checkIsFollowedPet() {
        objectService
                .getPetListForUser(
                        userID,
                        String.valueOf(ObjectServiceFactory.GET_LIKE_PET_STATUS_CODE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Pet>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("PetDetailActivity", "checkIsFollowedPet: complete");
                        if (!islike) {
                            try {
                                followIV.setImageResource(R.drawable.like);
                                Toast.makeText(PetDetailActivity.this, "is unfollow", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("PetDetailActivity", e.getMessage(), e);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("PetDetailActivity", "checkIsFollowedPet", throwable);
                    }

                    @Override
                    public void onNext(List<Pet> pets) {
                        for (Pet i:pets) {
                            if (i.getPet_id() == petId) {
                                islike = true;
                            }
                        }
                    }
                });
    }

    private void followPet() {
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
                        Log.i("PetDetailActivity", "followPet: complete");
                        Toast.makeText(PetDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("PetDetailActivity", "followPet", throwable);
                    }

                    @Override
                    public void onNext(Result<Integer> integerResult) {
                        try {
                            followIV.setImageResource(R.drawable.like_fill);
                            islike = true;
                            detail_followTV.setText(String.valueOf(++countLike));
                        } catch (Exception e) {
                            Log.e("PetDetailActivity", e.getMessage(), e);
                        }
                    }
                });
    }

    private void unfollowPet() {
        objectService
                .cancelUserFansPet(
                        String.valueOf(petId), userID
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemoteDBOperationResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.i("PetDetailActivity", "unfollowPet: complete");
                        Toast.makeText(PetDetailActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("PetDetailActivity", "unfollowPet", throwable);
                    }

                    @Override
                    public void onNext(RemoteDBOperationResponse r) {
                        try {
                            followIV.setImageResource(R.drawable.like);
                            islike = false;
                            detail_followTV.setText(String.valueOf(--countLike));
                        } catch (Exception e) {
                            Log.e("PetDetailActivity", e.getMessage(), e);
                        }
                    }
                });
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

//	    axisX.setName("遛宠");  //表格名称
//      axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("时长");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据）
         * 当数据点个数小于（29）的时候,缩小到极致hellochart默认的是所有显示
         * 当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }

    class MyLayoutManager extends LinearLayoutManager {
        public MyLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            final int width = RecyclerView.LayoutManager.chooseSize(widthSpec,
                    getPaddingLeft() + getPaddingRight(),
                    ViewCompat.getMinimumWidth(hotSpotRecyclerView));
            final int height = RecyclerView.LayoutManager.chooseSize(heightSpec,
                    getPaddingTop() + getPaddingBottom(),
                    ViewCompat.getMinimumHeight(hotSpotRecyclerView));
            setMeasuredDimension(width, height * datas.size());
        }
    }
}
