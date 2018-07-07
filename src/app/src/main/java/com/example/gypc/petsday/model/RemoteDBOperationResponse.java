package com.example.gypc.petsday.model;

/**
 * Created by XUJIJUN on 2018/1/3.
 */

// 服务端更新、删除数据返回类
public class RemoteDBOperationResponse {
    private int affectedRows;  // 数据库中影响的列数

    public RemoteDBOperationResponse(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    // 判断操作是否成功
    public boolean isSuccess() {
        return affectedRows == 1;
    }
}
