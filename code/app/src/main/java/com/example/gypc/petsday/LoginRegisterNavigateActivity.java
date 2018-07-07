package com.example.gypc.petsday;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by XUJIJUN on 2017/12/18.
 */

public class LoginRegisterNavigateActivity extends AppCompatActivity {

    private Button loginNavigateBtn;
    private Button registerNavigateBtn;

    public static final int LOGIN_OK = 2;
    public static final int LOGIN_REQ_CODE = 3;
    public static final int REGISTER_REQ_CODE = 4;
    public static final int QUIT_RES_CODE = -1;

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
                startActivityForResult(new Intent(LoginRegisterNavigateActivity.this, LoginActivity.class),
                        LoginRegisterNavigateActivity.LOGIN_REQ_CODE);
            }
        });

        registerNavigateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginRegisterNavigateActivity.this, RegisterActivity.class),
                        LoginRegisterNavigateActivity.REGISTER_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent dataIntent) {
        if (reqCode == LoginRegisterNavigateActivity.LOGIN_REQ_CODE) {
            if (resCode == LoginActivity.LOGIN_OK) {
                setResult(LoginRegisterNavigateActivity.LOGIN_OK);
                finish();
            }
        }
    }

    private void quit() {
        setResult(LoginRegisterNavigateActivity.QUIT_RES_CODE);
        finish();
    }

    private void quitConfirm() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginRegisterNavigateActivity.this);
        alertDialog.setMessage("确认退出？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quit();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        quitConfirm();
    }
}
