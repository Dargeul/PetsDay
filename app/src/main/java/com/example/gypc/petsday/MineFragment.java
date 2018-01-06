package com.example.gypc.petsday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gypc.petsday.adapter.FollowPetAdapter;
import com.example.gypc.petsday.adapter.MyPetAdapter;
import com.example.gypc.petsday.factory.ObjectServiceFactory;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.model.RemoteDBOperationResponse;
import com.example.gypc.petsday.service.ObjectService;
import com.example.gypc.petsday.utils.AppContext;
import com.example.gypc.petsday.utils.JSONRequestBodyGenerator;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gypc on 2017/12/7.
 */

public class MineFragment extends Fragment {
    private LinearLayout renameLL;
    private LinearLayout newNameLL;
    private EditText newNameET;
    private ImageView newNameIV;
    private LinearLayout addpetLL;
    private TextView mypetTV;
    private TextView followpetTV;
    private RecyclerView mypetRV;
    private RecyclerView followpetRV;

    private TextView mineNameTextView;

    private List<Pet> mypets;
    private List<Pet> followpets;
    private MyPetAdapter myPetAdapter;
    private FollowPetAdapter followPetAdapter;

    private AppContext app;
    private Context context;
    private ObjectService objectService;

    private boolean isFormUploadOK = false;
    private boolean isLoadMyPet = false;
    private boolean isLoadFollowPet = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine,container,false);
        context = this.getContext();
        app = AppContext.getInstance();
        objectService = ObjectServiceFactory.getService();

        // 绑定xml
        mypets = app.getMypets();
        followpets = app.getFollowpets();
        renameLL = (LinearLayout)view.findViewById(R.id.renameLL);
        newNameET = (EditText)view.findViewById(R.id.newNameET);
        newNameLL = (LinearLayout)view.findViewById(R.id.newNameLL);
        newNameIV = (ImageView)view.findViewById(R.id.newNameIV);
        addpetLL = (LinearLayout)view.findViewById(R.id.addpetLL);
        mypetTV = (TextView)view.findViewById(R.id.mypetTV);
        followpetTV = (TextView)view.findViewById(R.id.followpetTV);
        mypetRV = (RecyclerView)view.findViewById(R.id.mypetRV);
        followpetRV = (RecyclerView)view.findViewById(R.id.followpetRV);
        mineNameTextView = (TextView)view.findViewById(R.id.mineNameTextView);

        mineNameTextView.setText(AppContext.getInstance().getLoginUserInfo().get("user_nickname").toString());

        // 设置adapter
        mypetRV.setLayoutManager(new LinearLayoutManager(context));
        myPetAdapter = new MyPetAdapter(mypets, context);
        mypetRV.setAdapter(myPetAdapter);
        followpetRV.setLayoutManager(new LinearLayoutManager(context));
        followPetAdapter = new FollowPetAdapter(followpets, context);
        followpetRV.setAdapter(followPetAdapter);

        addpetLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewpetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("owner", getOwnerId());
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, MainActivity.ADD_PET_REQ_CODE);
            }
        });

        // 设置myPet列表点击监听事件
        myPetAdapter.setOnItemClickListener(new MyPetAdapter.OnItemClickListener() {
            @Override
            public void onClickList(int position) {
                Toast.makeText(context, "myPet列表第" + position + "项被点击了", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), PetDetailActivity.class);

                Bundle bundle = new Bundle();
                Pet item = mypets.get(position);
                bundle.putInt("pet_id", item.getPet_id());
                bundle.putString("pet_type", item.getPet_type());
                bundle.putString("pet_photo", item.getPet_photo());
                bundle.putString("pet_weight", String.valueOf(item.getPet_weight()));
                bundle.putString("pet_birth", item.getPet_birth());
                bundle.putString("pet_sex", item.getPet_sex());

                intent.putExtras(bundle);

                getActivity().startActivity(intent);
            }

            @Override
            public void onLongClickList(int position) {
                Bundle bundle = new Bundle();
                Pet pet = mypets.get(position);
                bundle.putInt("position", position);
                bundle.putInt("id", pet.getPet_id());
                bundle.putString("photo", pet.getPet_photo());
                bundle.putString("nickname", pet.getPet_nickname());
                bundle.putString("type", pet.getPet_type());
                bundle.putBoolean("sex", pet.getPet_sex().equals("boy"));
                bundle.putInt("weight", pet.getPet_weight());
                bundle.putString("birth", pet.getPet_birth());
                bundle.putInt("owner", pet.getPet_owner());

                Intent intent = new Intent(getActivity(), NewpetActivity.class);
                intent.putExtras(bundle);

                getActivity().startActivityForResult(intent, MainActivity.EDIT_PET_CODE);
            }
        });

        // 设置followPet列表点击监听事件
        followPetAdapter.setOnItemClickListener(new FollowPetAdapter.OnItemClickListener() {
            @Override
            public void onClickList(int position) {
                Toast.makeText(context, "followPet列表第" + position + "项被点击了", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), PetDetailActivity.class);
                startActivity(intent);
            }
        });

        //点击修改昵称
        renameLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNameET.setText(mineNameTextView.getText());
                newNameLL.setVisibility(View.VISIBLE);
                mineNameTextView.setVisibility(View.INVISIBLE);
            }
        });

        //点击确定完成修改
        newNameIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadForm();
                mineNameTextView.setText(newNameET.getText().toString());
                newNameLL.setVisibility(View.INVISIBLE);
                mineNameTextView.setVisibility(View.VISIBLE);
            }
        });

        mypetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypetRV.setVisibility(View.VISIBLE);
                followpetRV.setVisibility(View.INVISIBLE);
            }
        });

        followpetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypetRV.setVisibility(View.INVISIBLE);
                followpetRV.setVisibility(View.VISIBLE);
            }
        });

        instance = this;

        return view;
    }

    private int getOwnerId() {
        return (int)AppContext.getInstance().getLoginUserInfo().get("user_id");
    }

    private static MineFragment instance;
    public static MineFragment getInstance() {
        return instance;
    }

    public void addPet(Bundle bundle) {
        // ???
        int petFollow = 0;

        Pet petToAdded = new Pet(
                bundle.getInt("id"),
                bundle.getString("nickname"),
                bundle.getInt("owner"),
                bundle.getString("type"),
                bundle.getInt("weight"),
                bundle.getBoolean("sex") ? "boy" : "girl",
                bundle.getString("birth"),
                bundle.getString("photo"),
                petFollow
        );
        mypets.add(petToAdded);
        myPetAdapter.notifyDataSetChanged();
    }

    public void updatePet(Bundle bundle) {
        int pos = bundle.getInt("position");
        Pet oldPet = mypets.get(pos);
        oldPet.setPet_nickname(bundle.getString("nickname"));
        oldPet.setPet_type(bundle.getString("type"));
        oldPet.setPet_weight(bundle.getInt("weight"));
        oldPet.setPet_sex(bundle.getBoolean("sex") ? "boy" : "girl");
        oldPet.setPet_birth(bundle.getString("birth"));
        oldPet.setPet_photo(bundle.getString("photo"));
        mypets.set(pos, oldPet);
        myPetAdapter.notifyDataSetChanged();
    }

    private void uploadForm() {
        if (isFormUploadOK) return;

        final String nickname = newNameET.getText().toString();

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("user_id", AppContext.getInstance().getLoginUserInfo().get("user_id").toString());
        userData.put("username", AppContext.getInstance().getLoginUserInfo().get("username").toString());
        userData.put("password", AppContext.getInstance().getLoginUserInfo().get("password").toString());
        userData.put("user_nickname", nickname);

        objectService
                .updateUser(JSONRequestBodyGenerator.getJsonObjBody(userData))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RemoteDBOperationResponse>() {
                    @Override
                    public void onCompleted() {
                        if (!isFormUploadOK) {
                            Toast.makeText(context, "修改昵称失败，请重试！", Toast.LENGTH_SHORT).show();
                        } else {
                            HashMap<String, Object> info = app.getLoginUserInfo();
                            info.put("user_nickname", nickname);
                            app.updateUserInfo(info);  // 更新全局用户信息

                            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("MineFragment", "updateNickname", e);
                    }

                    @Override
                    public void onNext(RemoteDBOperationResponse dboResult) {
                        if (!dboResult.isSuccess()) {
                            Log.i("MineFragment", "updateNickname");
                        }

                        HashMap<String, String> userObj = new HashMap<>();
                        userObj.put("user_id", AppContext.getInstance().getLoginUserInfo().get("user_id").toString());
                        userObj.put("username", AppContext.getInstance().getLoginUserInfo().get("username").toString());
                        userObj.put("password", AppContext.getInstance().getLoginUserInfo().get("password").toString());
                        userObj.put("user_nickname", newNameET.getText().toString());
                        AppContext.getInstance().setLoginUserInfo(userObj);

                        isFormUploadOK = true;
                    }
                });
    }
}