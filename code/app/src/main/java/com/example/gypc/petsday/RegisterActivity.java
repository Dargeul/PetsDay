package com.example.gypc.petsday;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.User;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;

import org.json.JSONArray;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XUJIJUN on 2017/12/18.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText nicknameEditText;
    private EditText pwdEditText;
    private EditText confirmPwdEditText;
    private Button confirmBtn;

    public static final int REGISTER_OK = 6;

    private boolean userExists = true;
    private boolean isRegisterOk = false;

    private String username;
    private String nickname;
    private String password;

    private ObjectService objectService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initControls();
        objectService = ObjectServiceFactory.getService();
    }

    private void msgNotify(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void submitInfo() {
        if (userExists) {
            confirmBtn.setEnabled(true);
            msgNotify("用户名已存在！");
            return;
        }
        isRegisterOk = false;
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("username", username);
        dataMap.put("user_nickname", nickname);
        dataMap.put("password", password);
//        dataMap.put("status", ObjectServiceFactory.REGISTER_STATUS_CODE);
        objectService
                .userRegister(JSONRequestBodyGenerator.getJsonObjBody(dataMap))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<Integer>>() {
                    @Override
                    public void onCompleted() {
                        registerOk();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RegisterActivity", "submitInfo", e);
                    }

                    @Override
                    public void onNext(Result<Integer> integerResult) {
                        if (integerResult.isError()) {
                            Log.e("RegisterActivity", "submitInfo", integerResult.error());
                        }
                        if (integerResult.response() == null || integerResult.response().body() == null)
                            return;
                        if (integerResult.response().body() >= 0)
                            isRegisterOk = true;
                    }
                });
    }

    private void validateUsernameExistence() {
        userExists = true;
//        HashMap<String, Object> dataMap = new HashMap<>();
//        dataMap.put("username", username);
//        dataMap.put("status", ObjectServiceFactory.USERNAME_VALIDATE_STATUS_CODE);
        objectService
                .queryUsername(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<User>>() {
                    @Override
                    public void onCompleted() {
                        submitInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RegisterActivity", "validateUsernameExistence", e);
                    }

                    @Override
                    public void onNext(Result<User> userResult) {
//                        try {
//                            String resData = responseBody.string();
//                            JSONArray jsonArray = new JSONArray(resData);
//                            if (jsonArray.length() == 0) {
//                                userExists = false;
//                            }
//                        } catch (Exception e) {
//                            Log.e("RegisterActivity", "validateUsernameExistence", e);
//                        }
                        if (userResult.isError()) {
                            Log.e("RegisterActivity", "validateUsernameExistence", userResult.error());
                        }
                        if (userResult.response() == null || userResult.response().body() == null) {
                            userExists = false;
                            return;
                        }
                    }
                });
    }

    private void submitRegister() {
        confirmBtn.setEnabled(false);
        validateUsernameExistence();
    }

    private void registerOk() {
        if (!isRegisterOk) {
            confirmBtn.setEnabled(true);
            msgNotify("注册失败，请重试！");
            return;
        }
        msgNotify("注册成功！");
        setResult(RegisterActivity.REGISTER_OK);
        finish();
    }

    private void confirmRegister() {
        username = usernameEditText.getText().toString();
        nickname = nicknameEditText.getText().toString();
        password = pwdEditText.getText().toString();
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

        submitRegister();
    }

    private void initControls() {
        usernameEditText = (EditText)findViewById(R.id.registerUsernameEditText);
        nicknameEditText = (EditText)findViewById(R.id.registerNicknameEditText);
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
