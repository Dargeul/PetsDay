package com.example.gypc.petsday;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alley.van.VanGogh;
import com.alley.van.activity.VanCropActivity;
import com.alley.van.helper.VanCropType;
import com.alley.van.helper.VanMediaFilter;
import com.alley.van.helper.VanMediaType;
import com.alley.van.model.VanConfig;
import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.example.gypc.petsday.base.BaseActivity;
import com.example.gypc.petsday.helper.GifSizeFilter;
import com.example.gypc.petsday.helper.GlideImageLoader;
import com.example.gypc.petsday.helper.XCRoundImageView;
import com.kevin.crop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by StellaSong on 2017/12/18.
 */

public class NewpetActivity extends BaseActivity {

    private XCRoundImageView headRIV;
    private EditText nicknameET;
    private EditText typeET;
    private RadioGroup sexRG;
    private NumberPicker weightNP;
    private Button dateBT;

    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CAMERA = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_newpet);

        headRIV = (XCRoundImageView)findViewById(R.id.headRIV);
        nicknameET = (EditText)findViewById(R.id.nicknameET);
        typeET = (EditText)findViewById(R.id.typeET);
        sexRG = (RadioGroup)findViewById(R.id.sexRG);
        weightNP = (NumberPicker)findViewById(R.id.weightNP);
        dateBT = (Button) findViewById(R.id.dateBT);

        headRIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicturesActionSheet();
            }
        });

        dateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(NewpetActivity.this).create();
                dialog.show();
                DatePicker picker = new DatePicker(NewpetActivity.this);
                picker.setDate(2017, 12);
                picker.setMode(DPMode.SINGLE);
                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String date) {
                        Toast.makeText(NewpetActivity.this, date, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setContentView(picker, params);
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });
    }

    //==============================动态图片选择=============================================
    public void choosePicturesActionSheet(){
        ActionSheet.createBuilder(NewpetActivity.this,getSupportFragmentManager())
                .setCancelButtonTitle("取消(cancel)")
                .setOtherButtonTitles("拍照(Camera)","打开相册(Open Gallery)")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean b) {
                    }
                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                        switch(i){
                            case 0:
                                pictureForCamera();
                                break;
                            case 1:
                                pictureForVanGogh();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    private void pictureForCamera() {//拍照
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0x0001);
    }

    private void pictureForVanGogh() {//从相册选择
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0x0002);
    }

    @Override
    public void permissionGrant(boolean isGranted, int requestCode) {
        super.permissionGrant(isGranted, requestCode);
        if (!isGranted) {
            return;
        }

        switch (requestCode) {
            case 0x0001://拍照
                VanGogh.from(NewpetActivity.this)
                        .choose(VanMediaType.ofAll())//拍照时，无效
                        .cameraVisible(true, getPackageName())//拍照时，第一个参数无效
                        .withResultSize(1024, 1024)
                        .cropEnable(true, VanCropType.CROP_TYPE_RECTANGLE)//第一个参数为FALSE时，第二个参数无效
                        .theme(R.style.VanTheme_ActivityAnimation)
                        .thumbnailScale(0.85f)
                        .imageLoader(new GlideImageLoader())
                        .forCamera(REQUEST_CODE_CAMERA);
                break;

            case 0x0002://从相册选择
                VanGogh.from(NewpetActivity.this)
                        .choose(VanMediaType.ofAll())
                        .countable(false)//若开启裁剪，则无效
                        .rowCount(2)
                        .cameraVisible(true, getPackageName())//设置在第一个参数为FALSE时，第二个参数无效
                        .withResultSize(1024, 1024)
                        .cropEnable(true, VanCropType.CROP_TYPE_RECTANGLE)//第一个参数为TRUE时，则可选中数量被设为1，此时maxSelectable(9)无效；第一个参数为FALSE时，第二个参数无效
                        .theme(R.style.VanTheme_Dracula)
                        .addFilter(new GifSizeFilter(320, 320, 5 * VanMediaFilter.K * VanMediaFilter.K))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageLoader(new GlideImageLoader())
                        .forResult(REQUEST_CODE_CHOOSE);
                break;

            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == UCrop.REQUEST_CROP) {//拍照并裁剪成功
            handleCropResult(data);
        } else if (requestCode == UCrop.RESULT_ERROR) {//拍照并裁剪失败
            handleCropError(data);
        } else if (requestCode == REQUEST_CODE_CHOOSE) {//从相册选择
            display(VanGogh.obtainResult(data).get(0));
        } else if (requestCode == REQUEST_CODE_CAMERA) {//拍照
            Uri contentUri = VanGogh.obtainCamera();
            if (contentUri == null) {
                return;
            }

            if (!VanConfig.getInstance().cropEnable) {
                ArrayList<Uri> selected = new ArrayList<>();
                selected.add(contentUri);

                display(contentUri);
            } else {//拍照之后跳转到裁剪页面
                startCropActivity(contentUri);
            }
        }
    }

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);

        String filePath = resultUri.getEncodedPath();
        String imagePath = Uri.decode(filePath);

        if (resultUri == null) {
            return;
        }

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        headRIV.setImageBitmap(bitmap);
    }

    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
        } else {
            Toast.makeText(NewpetActivity.this,"无法裁剪图片",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转到裁剪页面
     * @param source 需要裁剪的图片
     */
    private void startCropActivity(Uri source) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String imageFileName = "IMG_" + dateFormat.format(new Date());

        Uri uri = Uri.fromFile(new File(getCacheDir(), imageFileName.concat(".jpeg")));
        UCrop.of(source, uri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1024, 1024)
                .withTargetActivity(VanCropActivity.class)
                .start(this);
    }

    private void display(Uri uri) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .into(headRIV);
    }
}
