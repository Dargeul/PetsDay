package com.example.gypc.petsday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by XUJIJUN on 2017/12/18.
 */

public class LoginRegisterNavigateActivity extends AppCompatActivity {

    private Button loginNavigateBtn;
    private Button registerNavigateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_login);

        initControls();
    }

    private void initControls() {
        loginNavigateBtn = (Button)findViewById(R.id.loginNavigateBtn);
        registerNavigateBtn = (Button)findViewById(R.id.registerNavigateBtn);

        loginNavigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegisterNavigateActivity.this, LoginActivity.class));
            }
        });

        registerNavigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegisterNavigateActivity.this, RegisterActivity.class));
            }
        });
    }
}
