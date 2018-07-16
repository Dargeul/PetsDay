> phpmyadmin管理界面
>
> url:120.78.169.206/phpmyadmin
>
> username:root
>
> password:xiaomixm
>
> database:cat


> **内容更新**
> - 修复通知返回评论者nickname的bug
> - 增加删除关注信息的接口


> - 更新了批量上传动态宠物关联相关信息
> - 动态返回表里面增加了相应的评论数


> - 查询动态,评论,通知,宠物都返回了用户昵称的字段
> - 增加了查看用户关注的宠物列表的接口
> - 增加了批量增加关注功能的接口

### 获取列表当中的相关数据

```bash
curl -X GET  120.78.169.206:3000/user
curl -X GET  120.78.169.206:3000/pet
curl -X GET  120.78.169.206:3000/comment
curl -X GET  120.78.169.206:3000/hotspot
curl -X GET  120.78.169.206:3000/pet_and_hotspot
curl -X GET  120.78.169.206:3000/good
curl -X GET  120.78.169.206:3000/pet_and_user
curl -X GET  120.78.169.206:3000/notification
curl -X GET  120.78.169.206:3000/\like
```
### 插入列表中的项

注意:
- 注意相关的外键约束关系和主键设置
- 成功都会返回id,插入之后直接返回(int)表示在表格当中的id

```bash
//0表示插入
curl -X POST --data "username=123&&password=丁同学&&user_nickname=0&&status=0" 120.78.169.206:3000/user

curl -X POST --data "pet_nickname=小美&&pet_owner=1&&pet_type=美短&&pet_weight=20&&pet_sex=female&&pet_birth=2010-01-02&&pet_photo=test.jpg" 120.78.169.206:3000/pet

curl -X POST --data "hs_time=2010-01-02 00:00:11&&hs_user=4&&hs_content=text4" 120.78.169.206:3000/hotspot

curl -X POST --data "com_time=2010-01-02&&com_user=3&&com_hs=4&&com_content=test4" 120.78.169.206:3000/comment

curl -X POST --data "multiPost=[{\"pet_id\":\"1\",\"hs_id\":\"1\"},{\"pet_id\":\"1\",\"hs_id\":\"1\"},{\"pet_id\":\"1\",\"hs_id\":\"1\"}]" 120.78.169.206:3000/pet_and_hotspot

curl -X POST --data "pet_id=2&&user_id=2" 120.78.169.206:3000/pet_and_user

curl -X POST --data "notice_status=0&&notice_user=1&&notice_comment=3" 120.78.169.206:3000/notification

curl -X POST --data "like_user=2&&like_hotspot=4" 120.78.169.206:3000/like


curl -X POST --data "good_name=猫粮4&&good_price=400&&good_count=10&&good_info=还行" 120.78.169.206:3000/good

```

### 修改功能
- 修改用户信息
- 修改宠物信息
- 更新通知已读未读

```bash
curl -X PUT --data "password=345&&user_nickname=甲同学1&&user_id=1" 120.78.169.206:3000/user

curl -X PUT --data "pet_nickname=小黑&&pet_owner=1&&pet_type=暹罗&&pet_weight=30&&pet_sex=male&&pet_birth=2010-01-02&&pet_id=1&&pet_photo=test2.jpg" 120.78.169.206:3000/pet

curl -X PUT --data "notice_status=0&&notice_id=1" 120.78.169.206:3000/notification
```

### 删除功能
- 删除评论
- 删除点赞
- 删除关注信息

```bash
curl -X DELETE 120.78.169.206:3000/comment\?com_id=1
curl -X DELETE 120.78.169.206:3000/like\?like_id=1
curl -X DELETE 120.78.169.206:3000/pet_and_user\?pet_id=1&&user_id=1
```

### 功能点

> 动态的查询(**增加了count字段表示点赞数**)

- 根据宠物id选择相关动态
- 根据页数加载动态
- 加载最新动态
- 根据id查询动态信息

```bash
curl -X GET 120.78.169.206:3000/hotspot\?pet_id=1
curl -X GET 120.78.169.206:3000/hotspot\?page=0
curl -X GET 120.78.169.206:3000/hotspot
curl -X GET 120.78.169.206:3000/hotspot\?hs_id=1
```

> 登录注册验证(返回查询的数组)

```
//登录用户名和密码验证
curl -X POST --data "password=1234&&username=1&&status=1" 120.78.169.206:3000/user
//注册用户名唯一
curl -X POST --data "username=name1&&status=2" 120.78.169.206:3000/user
```

> 查看动态详情里面的相关评论

```
curl -X GET 120.78.169.206:3000/comment\?hs_id=2
```

> 根据用户id查看拥有的宠物(**增加了count字段表示粉丝数**)

- 查看用户拥有的宠物(status=1)
- 查看用户关注的宠物(status=0)

```
curl -X GET 120.78.169.206:3000/pet\?user_id=1&&status=1
curl -X GET 120.78.169.206:3000/pet\?user_id=1&&status=0
```

> 查看相关动态关联的宠物(**增加了count字段表示粉丝数**)

```
curl -X GET 120.78.169.206:3000/pet\?hs_id=1
```

> 看看用户通知(相关评论内容也可以看到)

```
curl -X GET 120.78.169.206:3000/notification\?user_id=1
```

> 查看宠物的粉丝

```
curl -X GET 120.78.169.206:3000/pet\?pet_id=1
```

> 查看相关用户点赞的条目

```
curl -X GET 120.78.169.206:3000/like\?user_id=1
```
