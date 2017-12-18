package com.example.gypc.petsday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by XUJIJUN on 2017/12/18.
 */

public class LoginActivity extends AppCompatActivity {

    private Button confirmBtn;
    private EditText usernameEditText;
    private EditText pwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initControls();
    }

    private void initControls() {
        confirmBtn = (Button)findViewById(R.id.loginBtn);
        usernameEditText = (EditText) findViewById(R.id.loginUsernameEditText);
        pwdEditText = (EditText)findViewById(R.id.loginPwdEditText);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogin();
            }
        });
    }

    private void msgNotify(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String submitUserInfo(String name, String pwd) {
        return "";
    }

    private void loginSuccess(String username) {

    }

    private void confirmLogin() {
        String username = usernameEditText.getText().toString();
        String password = pwdEditText.getText().toString();

        if (username.isEmpty()) {
            msgNotify("用户名不得为空！");
            return;
        }

        if (password.isEmpty()) {
            msgNotify("密码不得为空！");
            return;
        }

        String submitStatus = submitUserInfo(username, password);

        if (submitStatus.equals("ok")) {
            loginSuccess(username);
        } else if (submitStatus.equals("username")) {
            msgNotify("用户名不存在！");
        } else if (submitStatus.equals("password")) {
            msgNotify("密码不正确！");
        }
    }
}
