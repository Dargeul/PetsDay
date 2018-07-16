package models

import "github.com/astaxie/beego/orm"

type Good struct {
	GoodName  string `json:"good_name"`
	GoodPrice string `json:"good_price"`
	GoodId    int    `json:"good_id"`
	GoodCount int    `json:"good_count"`
	GoodInfo  string `json:"good_info"`
}

func GetGoods() (*[]Good, error) {
	o := orm.NewOrm()
	var goods []Good
	_, err := o.Raw("SELECT * FROM good").QueryRows(&goods)
	return &goods, err
}

func InsertGood(good *Good) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO good (good_count, good_info, good_name, good_price) VALUES (?, ?, ?, ?);", good.GoodCount, good.GoodInfo, good.GoodName, good.GoodPrice).Exec()
	insertId, _:= res.LastInsertId()
	return insertId, err
}