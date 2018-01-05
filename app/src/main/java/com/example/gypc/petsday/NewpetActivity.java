package com.example.gypc.petsday;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.gypc.petsday.factory.ImageServiceFactory;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.helper.GifSizeFilter;
import com.example.gypc.petsday.helper.GlideImageLoader;
import com.example.gypc.petsday.helper.XCRoundImageView;
import com.example.gypc.petsday.model.RemoteDBOperationResponse;
import com.example.gypc.petsday.service.ImageService;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.ImageMultipartGenerator;
import com.example.gypc.petsday.utils.ImageUriConverter;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;
import com.kevin.crop.UCrop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import cn.aigestudio.datepicker.cons.DPMode;
//import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by StellaSong on 2017/12/18.
 */

public class NewpetActivity extends BaseActivity {

    private XCRoundImageView headRIV;
    private EditText nicknameET;
    private EditText typeET;
    private RadioGroup sexRG;
    private EditText weightEditText;
    private EditText dateET;
    private Button newPetSubmitBtn;
    private DatePicker dialogDatePicker;

    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CAMERA = 32;

    private ImageService imageService;
    private ObjectService objectService;

    private String avatarUploadResultString;

    public static final int SUCCESS_RES_CODE = 4;

    private int failUploadTimes = 0;

    private boolean
            isAvatarUploadComplete = false,
            isFormUploadComplete = false,
            isAvatarUploadOK = false,
            isFormUploadOK = false;

    private boolean isAvatarFromCache = false;


    private int positionInList = -1;
    private int petId = -1;
    private String imageFilename;
    private String nickname;
    private String type;
    private String sex;
    private int weight;
    private String birth;
    private int owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_newpet);

        imageService = ImageServiceFactory.getService();
        objectService = ObjectServiceFactory.getService();

        headRIV = (XCRoundImageView)findViewById(R.id.headRIV);
        nicknameET = (EditText)findViewById(R.id.nicknameET);
        typeET = (EditText)findViewById(R.id.typeET);
        sexRG = (RadioGroup)findViewById(R.id.sexRG);
        weightEditText = (EditText)findViewById(R.id.weightEditText);
        dateET = (EditText) findViewById(R.id.dateET);
        newPetSubmitBtn = (Button)findViewById(R.id.newPetSubmitBtn);

        headRIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicturesActionSheet();
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        newPetSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        initInfoEdit();
    }

    private void initInfoEdit() {
        Bundle initDataBundle = getIntent().getExtras();

        if (initDataBundle == null) {
            Log.e("NewpetActivity", "initInfoEdit:bundle null");
            return;
        }

        owner = initDataBundle.getInt("owner");

        if (!initDataBundle.containsKey("id")) {
            return;
        }
        positionInList = initDataBundle.getInt("position");
        petId = initDataBundle.getInt("id");
        imageFilename = initDataBundle.getString("photo");
        nickname = initDataBundle.getString("nickname");
        type = initDataBundle.getString("type");
        sex = initDataBundle.getBoolean("sex") ? "boy" : "girl";
        weight = initDataBundle.getInt("weight");
        birth = initDataBundle.getString("birth");


        try {
             Uri avatarUri = ImageUriConverter.getCacheFileUriFromName(this, imageFilename);
             if (avatarUri == null)
                 throw new Exception("no image from cache");
             display(avatarUri);
        } catch (Exception e) {
            Log.e("NewpetActivity", "initInfoEdit", e);
            String path = ImageUriConverter.getImgRemoteUriFromName(imageFilename);
            displayAvatarFromRemote(path);
        }

        nicknameET.setText(nickname);
        typeET.setText(type);
        sexRG.check(sex.equals("boy") ? R.id.boyRB : R.id.girlRB);
        weightEditText.setText(String.valueOf(weight));
        dateET.setText(birth);
    }

    private void showDatePickerDialog() {
        View view = LayoutInflater.from(NewpetActivity.this).inflate(R.layout.date_picker_dialog, null);
        dialogDatePicker = (DatePicker)view.findViewById(R.id.dialogDatePicker);
        dialogDatePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewpetActivity.this);
        dialogBuilder.setView(view);
        dialogBuilder
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = String.valueOf(dialogDatePicker.getYear()) + "-" +
                                String.valueOf(dialogDatePicker.getMonth() + 1) + "-" +
                                String.valueOf(dialogDatePicker.getDayOfMonth());
                        dateET.setText(date);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void submitForm() {
        nickname = nicknameET.getText().toString();
        type = typeET.getText().toString();
        sex = (sexRG.getCheckedRadioButtonId() == R.id.boyRB) ? "boy" : "girl";
        String weightText = weightEditText.getText().toString();
        birth = dateET.getText().toString();

        if (nickname.isEmpty()) {
            msgNotify("请输入昵称！");
        } else if (type.isEmpty()) {
            msgNotify("请输入宠物类型！");
        } else if (weightText.isEmpty()) {
            msgNotify("请输入宠物体重！");
        } else if ((weight = Integer.parseInt(weightText)) <= 0) {
            msgNotify("请输入正确的宠物体重！");
        } else if (birth.isEmpty()) {
            msgNotify("请选择宠物生日！");
        } else if (imageFilename == null){
            msgNotify("请选择头像图片！");
        } else {
            try {
                uploadData();
            } catch (Exception e) {
                Log.e("NewpetActivity", "uploadData", e);
            }
        }
    }

    private void uploadData() {
        newPetSubmitBtn.setEnabled(false);
        avatarUploadResultString = "";
        uploadAvatar();
        uploadForm();
    }

    private void navigateToPrePage() {
        Bundle bundle = new Bundle();

        if (positionInList != -1)
            bundle.putInt("position", positionInList);
        bundle.putInt("id", petId);
        bundle.putString("photo", imageFilename);
        bundle.putString("nickname", nickname);
        bundle.putString("type", type);
        bundle.putBoolean("sex", sex.equals("boy"));
        bundle.putInt("weight", weight);
        bundle.putString("birth", birth);
        bundle.putInt("owner", owner);

        Intent data = new Intent();
        data.putExtras(bundle);
        setResult(SUCCESS_RES_CODE, data);
        finish();
    }

    private void submitFinish() {
        if (isFormUploadComplete && isAvatarUploadComplete) {
            if (!isFormUploadOK) {
                msgNotify("信息上传失败，请重试！");
                newPetSubmitBtn.setEnabled(true);
            } else if (!isAvatarUploadOK) {
                msgNotify("头像上传失败，请重试！");
                newPetSubmitBtn.setEnabled(true);
            } else {
                navigateToPrePage();
            }
        }
    }

    private void uploadFormFinish() {
        isFormUploadComplete = true;
        submitFinish();
    }

    private void uploadForm() {
        if (isFormUploadOK)
            return;

        HashMap<String, Object> petData = new HashMap<>();
        petData.put("pet_id", petId);
        petData.put("pet_photo", imageFilename);
        petData.put("pet_nickname", nickname);
        petData.put("pet_type", type);
        petData.put("pet_sex", sex);
        petData.put("pet_weight", weight);
        petData.put("pet_birth", birth);
        petData.put("pet_owner", owner);

        if (petId == -1) {
            objectService
                    .insertPet(JSONRequestBodyGenerator.getJsonObjBody(petData))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result<Integer>>() {
                        @Override
                        public void onCompleted() {
                            uploadFormFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("NewpetActivity", "insertPet", e);
                        }

                        @Override
                        public void onNext(Result<Integer> integerResult) {
                            if (integerResult.isError()) {
                                Log.e("NewpetActivity", "insertPet", integerResult.error());
                            }
                            if (integerResult.response() == null)
                                return;
                            petId = integerResult.response().body();
                            isFormUploadOK = true;
                        }
                    });
        } else {
            objectService
                    .updatePet(JSONRequestBodyGenerator.getJsonObjBody(petData))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RemoteDBOperationResponse>() {
                        @Override
                        public void onCompleted() {
                            uploadFormFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("NewpetActivity", "updatePet", e);
                        }

                        @Override
                        public void onNext(RemoteDBOperationResponse response) {
                            isFormUploadOK = response.isSuccess();
                        }
                    });
        }

        isFormUploadComplete = true;
    }

    private void uploadAvatarFinish() {
        if (isAvatarUploadOK) {
            isAvatarUploadComplete = true;
            submitFinish();
        } else {
            if (failUploadTimes == 3) {
                isAvatarUploadComplete = true;
                submitFinish();
            } else {
                failUploadTimes++;
                uploadAvatar();
            }
        }
    }

    private void uploadAvatar() {
        if (isAvatarUploadOK)
            return;
        if (!isAvatarFromCache) {
            isAvatarUploadOK = true;
            uploadAvatarFinish();
            return;
        }
        imageService
                .uploadAvatar(
                        ImageMultipartGenerator.getParts(
                                ImageUriConverter.getCacheFileUriFromName(this, imageFilename).getPath()
                        )
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<String>>() {
                    @Override
                    public void onCompleted() {
                        uploadAvatarFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NewpetActivity", "uploadAvatar", e);
                    }

                    @Override
                    public void onNext(Result<String> stringResult) {
                        if (stringResult.isError()) {
                            Log.e("NewpetActivity", "uploadAvatar: onNext: ", stringResult.error());
                        }
                        if (stringResult.response() == null)
                            return;
                        avatarUploadResultString = stringResult.response().body();
                        Log.i("NewpetActivity", "uploadAvatar: " + avatarUploadResultString);
                        isAvatarUploadOK = avatarUploadResultString.equals(ImageServiceFactory.SUCCESS);
                    }
                });
    }

    private void msgNotify(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                0x0002);
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
                        .addFilter(new GifSizeFilter(320, 320,
                                5 * VanMediaFilter.K * VanMediaFilter.K))
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
        imageFilename = "IMG_" + dateFormat.format(new Date());

        Log.i("NewpetActivity", "startCropActivity: filename = " + imageFilename);

        Uri uri = ImageUriConverter.getCacheFileUriFromName(this, imageFilename);
        UCrop.of(source, uri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1024, 1024)
                .withTargetActivity(VanCropActivity.class)
                .start(this);
    }

    private void display(Uri uri) {
        imageFilename = ImageUriConverter.getFilenameFromUri(uri);
        Log.i("NewpetActivity", "display: filename = " + imageFilename);

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .into(headRIV);
        isAvatarFromCache = true;
    }

    private void displayAvatarFromRemote(String path) {
        Glide.with(this)
                .load(path)
                .asBitmap()
                .into(headRIV);
    }
}
