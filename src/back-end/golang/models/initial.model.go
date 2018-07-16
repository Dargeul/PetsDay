package models

import (
	"io/ioutil"
	"strings"

	"github.com/astaxie/beego/orm"
)

func CreateDatabase() {
	o := orm.NewOrm()
	file, err := ioutil.ReadFile("static/create.sql")
	if nil != err {
		return
	}
	tables := strings.Split(string(file), ";")
	for _, table := range tables {
		o.Raw(table).Exec()
	}
}

func CreateInitialData() {
	o := orm.NewOrm()
	o.Raw("INSERT INTO `user` (username, password, user_nickname) VALUES ('name1' ,'1234', '甲同学'), ( 'name2' ,'1234', '乙同学'), ('name3' ,'1234', '丙同学');").Exec()
	o.Raw("INSERT INTO `pet` (pet_nickname, pet_owner, pet_type, pet_weight, pet_sex, pet_birth, pet_photo) VALUES ('小英','1','英短', '20', 'female', '2010-01-02', 'test.jpg'), ('小橘', '1', '橘猫', '20', 'female', '2010-01-02', 'test.jpg'), ('小波', '1', '波斯', '20', 'female', '2010-01-02', 'test.jpg');").Exec()
	for i := 100; i <= 200; i++ {
		o.Raw("INSERT INTO `hotspot` (hs_time, hs_user, hs_content, hs_photo) VALUES ('2010-01-02', '1', ?, 'test.jpg'), ('2010-01-02', '2', ?,'test.jpg'), ('2010-01-02', '3', ?,'test.jpg');", i, i, i).Exec()
	}
	o.Raw("INSERT INTO `comment` (com_time, com_user, com_hs, com_content) VALUES ('2010-01-02', '1', '1', '111111'), ('2010-01-02', '2', '2', '22222'), ('2010-01-02', '3', '3', '33333');").Exec()
	o.Raw("INSERT INTO `pet_and_user` (pet_id, user_id) VALUES ( '1', '1'), ( '1', '2'), ('1', '3'), ('2', '1'), ('3', '1');").Exec()
	o.Raw("INSERT INTO `notification` (notice_status, notice_user, notice_comment) VALUES ( '1', '1', '1'), ( '1', '1', '2'), ('0', '1', '3'), ('0', '2', '1'), ('1', '3', '1');").Exec()
	o.Raw("INSERT INTO `like` (like_user, like_hotspot) VALUES ( '1', '1'), ( '1', '2'), ('1', '3'), ('2', '1'), ('3', '1');").Exec()
	o.Raw("INSERT INTO `pet_and_hotspot` (pet_id, hs_id) VALUES ( '1', '1'), ( '1', '2'), ('1', '3'), ('2', '1'), ('3', '1');").Exec()
	o.Raw("INSERT INTO `good` (good_name, good_price, good_count, good_info) VALUES ('猫粮1', '100', '10', '还行'), ('猫粮2', '200', '10', '还行'), ('猫粮3', '300', '10', '还行');").Exec()
}
