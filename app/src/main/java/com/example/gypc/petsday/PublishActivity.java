package com.example.gypc.petsday;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alley.van.VanGogh;
import com.alley.van.activity.VanCropActivity;
import com.alley.van.helper.VanCropType;
import com.alley.van.helper.VanImageLoader;
import com.alley.van.helper.VanMediaFilter;
import com.alley.van.helper.VanMediaType;
import com.alley.van.model.VanConfig;
import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.example.gypc.petsday.base.BaseActivity;
import com.example.gypc.petsday.factory.ImageServiceFactory;
import com.example.gypc.petsday.helper.GifSizeFilter;
import com.example.gypc.petsday.helper.GlideImageLoader;
import com.example.gypc.petsday.service.ImageService;
import com.example.gypc.petsday.utils.ImageUriConverter;
import com.kevin.crop.UCrop;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by gypc on 2017/12/11.
 */

public class PublishActivity extends BaseActivity {
    private static final int PET_CHOSEN_REQ_CODE = 1;
    public static final int PUBLISH_SUCCESS = 2;

    private ImageView choosePicture;
    private ImageView choosePet;
    private ImageButton submitHotspotBtn;
    private ImageButton newHotspotBackBtn;
    private ImageView hotSpotImage;

    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CAMERA = 32;
//    private UriAdapter mAdapter;

    private TextView displayChosenPetsTextView;

    private EditText publishContentEditText;

    private ArrayList<Integer> chosenPetIds;

    private boolean isFormUploadComplete = false;
    private boolean isImageUploadComplete = false;

    private ImageService imageService;

    private int hotspotId = -1;
    private String imageFilename = "";
    private String publishTime;
    private String publishContent = "";

    public void initWidget(){
        choosePet = (ImageView)findViewById(R.id.choosePet);
        choosePicture = (ImageView)findViewById(R.id.choosePicture);
        submitHotspotBtn = (ImageButton)findViewById(R.id.submitHotspotBtn);
        newHotspotBackBtn = (ImageButton) findViewById(R.id.newHotspotBackBtn);
        hotSpotImage = (ImageView)findViewById(R.id.displayHotspotImage);
        displayChosenPetsTextView = (TextView)findViewById(R.id.displayChosenPetsTextView);
        publishContentEditText = (EditText)findViewById(R.id.publishContentEditText);
        hotSpotImage.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotspot_publish);
        initWidget();
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicturesActionSheet();
            }
        });

        choosePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishActivity.this,ChoosePetActivity.class);
                startActivityForResult(intent, PET_CHOSEN_REQ_CODE);
            }
        });
        hotSpotImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicturesActionSheet();
            }
        });

        newHotspotBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitHotspotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHotspot();
            }
        });

        chosenPetIds = new ArrayList<>();
        imageService = ImageServiceFactory.getService();
    }

    private void submitHotspot() {
        publishContent = publishContentEditText.getText().toString();
        if (publishContent.isEmpty()) {
            msgNotify("请输入动态内容！");
        } else if (chosenPetIds.isEmpty()) {
            msgNotify("请选择宠物！");
        } else if (imageFilename.isEmpty()) {
            msgNotify("请选择照片！");
        } else {
            uploadData();
        }
    }

    private void submitFinish() {
        if (isImageUploadComplete && isFormUploadComplete) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", hotspotId);
            bundle.putString("content", publishContent);
            bundle.putString("photo", imageFilename);
            bundle.putString("time", publishTime);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(PUBLISH_SUCCESS, intent);
            finish();
        }
    }

    private void uploadData() {
        isImageUploadComplete = false;
        isFormUploadComplete = false;
        uploadImage();
        uploadForm();
    }

    private void uploadImageFinish() {
        isImageUploadComplete = true;
        submitFinish();
    }

    private void uploadImage() {
        uploadImageFinish();
    }

    private void uploadFormFinish() {
        isFormUploadComplete = true;
        submitFinish();
    }

    private void uploadForm() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        publishTime = dateFormat.format(new Date());
        uploadFormFinish();
    }

    private void msgNotify(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //==============================动态图片选择=============================================
    public void choosePicturesActionSheet(){
        ActionSheet.createBuilder(PublishActivity.this,getSupportFragmentManager())
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
                VanGogh.from(PublishActivity.this)
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
                VanGogh.from(PublishActivity.this)
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
        } else if (requestCode == PET_CHOSEN_REQ_CODE) {
            Log.i("PublishActivity", "choose pet success");
            Bundle bundle = data.getExtras();
            chosenPetIds = bundle.getIntegerArrayList("petIds");
            ArrayList<String> petNicknames = bundle.getStringArrayList("petNicknames");
            String nicknamesToDisplay = "";
            for (String nickname: petNicknames) {
                nicknamesToDisplay +=  nickname + " ";
            }
            displayChosenPetsTextView.setText(nicknamesToDisplay);
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
        hotSpotImage.setVisibility(View.VISIBLE);
        hotSpotImage.setImageBitmap(bitmap);
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
            Toast.makeText(PublishActivity.this,"无法裁剪图片",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转到裁剪页面
     * @param source 需要裁剪的图片
     */
    private void startCropActivity(Uri source) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        imageFilename = "IMG_" + dateFormat.format(new Date());

        Uri uri = ImageUriConverter.getCacheFileUriFromName(this, imageFilename);
        UCrop.of(source, uri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1024, 1024)
                .withTargetActivity(VanCropActivity.class)
                .start(this);
    }

    private void display(Uri uri) {

        imageFilename = ImageUriConverter.getFilenameFromUri(uri);

        hotSpotImage.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .into(hotSpotImage);
    }
}
