package com.example.gypc.petsday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.User;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XUJIJUN on 2017/12/18.
 */

public class LoginActivity extends AppCompatActivity {

    private Button confirmBtn;
    private EditText usernameEditText;
    private EditText pwdEditText;

    public static final int LOGIN_OK = 5;

    private ObjectService objectService;

    private int userId;
    private String username;
    private String password;
    private String nickname;

    private static final int SUBMIT_ERROR = -1;
    private static final int SUBMIT_INFO_WRONG = 0;
    private static final int SUBMIT_OK = 1;
    private int submitResponseStatusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initControls();
        objectService = ObjectServiceFactory.getService();
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



    private void loginSuccess() {
        if (submitResponseStatusCode == SUBMIT_OK) {
            HashMap<String, String> infoMap = new HashMap<>();
            infoMap.put("user_id", String.valueOf(userId));
            infoMap.put("username", username);
            infoMap.put("user_nickname", nickname);
            infoMap.put("password", password);
            AppContext.getInstance().setLoginUserInfo(infoMap);
            setResult(LoginActivity.LOGIN_OK);
            finish();
        } else if (submitResponseStatusCode == SUBMIT_ERROR) {
            confirmBtn.setEnabled(true);
            msgNotify("提交错误，请重试！");
        } else if (submitResponseStatusCode == SUBMIT_INFO_WRONG) {
            confirmBtn.setEnabled(true);
            msgNotify("用户名或密码错误！");
        }
    }

    private void confirmLogin() {
        username = usernameEditText.getText().toString();
        password = pwdEditText.getText().toString();

        if (username.isEmpty()) {
            msgNotify("用户名不得为空！");
            return;
        }

        if (password.isEmpty()) {
            msgNotify("密码不得为空！");
            return;
        }

        submitInfo();
    }

    private void submitInfo() {
        confirmBtn.setEnabled(false);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("username", username);
        dataMap.put("password", password);
//        dataMap.put("status", ObjectServiceFactory.LOGIN_STATUS_CODE);

        objectService
                .userLogin(JSONRequestBodyGenerator.getJsonObjBody(dataMap))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<User>>() {
                    @Override
                    public void onCompleted() {
                        loginSuccess();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        submitResponseStatusCode = SUBMIT_ERROR;
                        Log.e("LoginActivity", "submitInfo", throwable);
                    }

                    @Override
                    public void onNext(Result<User> userResult) {
//                        try {
//                            String resData = responseBody.string();
//                            JSONArray jsonArray = new JSONArray(resData);
//                            if (jsonArray.length() == 0) {
//                                submitResponseStatusCode = SUBMIT_INFO_WRONG;
//                                return;
//                            }
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                            userId = jsonObject.getInt("user_id");
//                            nickname = jsonObject.getString("user_nickname");
//                            submitResponseStatusCode = SUBMIT_OK;
//                        } catch (Exception e) {
//                            submitResponseStatusCode = SUBMIT_ERROR;
//                            Log.e("LoginActivity", "submitInfo", e);
//                        }
                        if (userResult.isError()) {
                            Log.e("LoginActivity", "submitInfo", userResult.error());
                            submitResponseStatusCode = SUBMIT_ERROR;
                        } else {
                            if (userResult.response() == null || userResult.response().body() == null) {
                                submitResponseStatusCode = SUBMIT_INFO_WRONG;
                            } else {
                                userId = userResult.response().body().getUser_id();
                                nickname = userResult.response().body().getUser_nickname();
                                submitResponseStatusCode = SUBMIT_OK;
                            }
                        }
                    }
                });
    }
}
