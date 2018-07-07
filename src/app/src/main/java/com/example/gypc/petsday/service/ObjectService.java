package com.example.gypc.petsday.service;

import com.example.gypc.petsday.model.Comment;
import com.example.gypc.petsday.model.Hotspot;
import com.example.gypc.petsday.model.HotspotLike;
import com.example.gypc.petsday.model.Pet;
import com.example.gypc.petsday.model.RemoteDBOperationResponse;
import com.example.gypc.petsday.model.User;
import com.example.gypc.petsday.model.UserNotification;

import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XUJIJUN on 2017/12/30.
 */

public interface ObjectService {
    @GET("/user/username/{username}")
    Observable<Result<User>> queryUsername(@Path("username") String username);  // 查询用户名是否存在

    @POST("/user")
    Observable<Result<Integer>> userRegister(@Body RequestBody jsonBody);  // 用户注册，成功返回user_id

    @POST("/user/check")
    Observable<Result<User>> userLogin(@Body RequestBody jsonBody);  // 用户登录

    @PATCH("/user/{userId}")
    Observable<Boolean> updateUser(@Path("userId") int userId, @Body RequestBody jsonBody);  // 用户信息更新

    @POST("/pet")
    Observable<Result<Integer>> insertPet(@Body RequestBody jsonBody);  // 添加宠物，成功返回pet_id

    @PATCH("/pet/{petId}")
    Observable<Boolean> updatePet(@Path("petId") int petId, @Body RequestBody jsonBody);  // 更新pet

    @GET("/pet/possess")
    Observable<List<Pet>> getPetListForUser(@Query("user_id") int userId);  // 获取用户拥有的宠物列表

    @GET("/pet/follow")
    Observable<List<Pet>> getUserFollowPetList(@Query("user_id") int userId);  // 获取用户关注的宠物列表

    @GET("/pet")
    Observable<List<Pet>> getPetListForHotspot(@Query("hs_id") String hotspotId);  // 查询动态关联的宠物list

    @POST("/hotspot")
    Observable<Result<Integer>> insertHotspot(@Body RequestBody jsonBody);  // 添加动态，成功返回动态id

    @GET("/hotspot")
    Observable<List<Hotspot>> getHotspotByPetId(@Query("pet_id") String petId); // 根据宠物id获取动态list

    @GET("/hotspot")
    Observable<List<Hotspot>> getHotspotListByPageNumber(@Query("page") int pageNumber);  // 根据页码获取动态list

    @GET("/hotspot")
    Observable<List<Hotspot>> getNewestHotspotList();  // 获取最新动态list

    @GET("/hotspot/{hotspotId}")
    Observable<List<Hotspot>> getHotspotById(@Path("hotspotId") String hotspotId);  // 根据id查询动态信息

    @POST("/pet_and_hotspot")
    Observable<Result<Boolean>> combinePetAndHotspot(@Body RequestBody jsonBody);  // 关联宠物和动态

    @POST("/pet_and_user")
    Observable<Result<Integer>> userFansPet(@Body RequestBody jsonBody);  // 用户关注宠物

    @DELETE("/pet_and_user")
    Observable<Boolean> cancelUserFansPet(@Query("pet_id") int petId, @Query("user_id") int userId);  // 用户取消关注宠物

    @GET("/comment")
    Observable<List<Comment>> getCommentListByHotspot(@Query("hs_id") String hotspotId);  // 根据动态id查询评论

    @POST("/comment")
    Observable<Result<Integer>> insertComment(@Body RequestBody jsonBody); // 添加评论，成功返回评论id

    @DELETE("/comment/{comId}")
    Observable<Boolean> deleteCommentById(@Path("comId") int commentId);  // 根据评论id删除评论

    @GET("/notification")
    Observable<List<UserNotification>> getNotificationByUserId(@Query("user_id") String userId);  // 根据用户id获取通知list(相关评论内容也可以看到)

    @POST("/notification")
    Observable<Result<Integer>> postNotification(@Body RequestBody jsonBody);  // 发送评论通知，成功返回通知id

    @PATCH("/notification/{noticeId}")
    Observable<Boolean> haveReadNotification(@Path("noticeId") int noticeId, @Body RequestBody jsonBody);  // 通知已读

    @GET("/like")
    Observable<List<HotspotLike>> getLikeListByUserId(@Query("user_id") String userId);  // 使用userId获取点赞列表

    @POST("/like")
    Observable<Result<Integer>> likeHotspot(@Body RequestBody jsonBody);  // 给评论点赞，成功返回点赞id

    @DELETE("/like/{likeId}")
    Observable<Boolean> cancelLike(@Path("likeId") int likeId);  // 根据点赞的id取消点赞

    @POST("/good")
    Observable<Result<Integer>> publishGood(@Body RequestBody jsonBody);  // 发布商品，成功返回商品id
}
