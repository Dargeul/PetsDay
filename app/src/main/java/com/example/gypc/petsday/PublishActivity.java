package com.example.gypc.petsday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by gypc on 2017/12/11.
 */

public class PublishActivity extends AppCompatActivity {
    private ImageView choosePicture;
    private ImageView choosePet;
    private ImageView publish;
    private ImageView back;
    public void initWidget(){
        choosePet = (ImageView)findViewById(R.id.choosePet);
        choosePicture = (ImageView)findViewById(R.id.choosePicture);
        publish = (ImageView)findViewById(R.id.publish);
        back = (ImageView)findViewById(R.id.back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotspot_publish);
        initWidget();
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishActivity.this,ChoosePictureActivity.class);
                startActivity(intent);
            }
        });

        choosePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });
    }
}
