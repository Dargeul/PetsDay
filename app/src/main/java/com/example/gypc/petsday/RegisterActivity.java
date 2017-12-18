package com.example.gypc.petsday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by XUJIJUN on 2017/12/18.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText nicknameEditText;
    private EditText pwdEditText;
    private EditText confirmPwdEditText;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initControls();
    }

    private void msgNotify(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String submitRegister(String name, String nickname, String pwd) {
        return "";
    }

    private void registerOk() {
        startActivity(new Intent(RegisterActivity.this, LoginRegisterNavigateActivity.class));
    }

    private void confirmRegister() {
        String username = usernameEditText.getText().toString();
        String nickname = nicknameEditText.getText().toString();
        String password = pwdEditText.getText().toString();
        String confirmPassword = confirmPwdEditText.getText().toString();

        if (username.isEmpty()) {
            msgNotify("用户名不得为空！");
            return;
        }

        if (nickname.isEmpty()) {
            msgNotify("昵称不得为空！");
            return;
        }

        if (password.isEmpty()) {
            msgNotify("密码不得为空！");
            return;
        }

        if (confirmPassword.isEmpty()) {
            msgNotify("确认密码不得为空！");
            return;
        }

        if (!password.equals(confirmPassword)) {
            msgNotify("两次输入密码不同！");
            return;
        }

        String submitStatus = submitRegister(username, nickname, password);
        if (submitStatus.equals("ok")) {
            registerOk();
        } else if (submitStatus.equals("name")) {
            msgNotify("用户名已存在！");
        }
    }

    private void initControls() {
        usernameEditText = (EditText)findViewById(R.id.registerUsernameEditText);
        nicknameEditText = (EditText)findViewById(R.id.registerUsernameEditText);
        pwdEditText = (EditText)findViewById(R.id.registerPwdEditText);
        confirmPwdEditText = (EditText)findViewById(R.id.confirmPwdEditText);
        confirmBtn = (Button)findViewById(R.id.registerBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRegister();
            }
        });
    }
}
