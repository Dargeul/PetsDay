package models

import "github.com/astaxie/beego/orm"

type Like struct {
	LikeUser    int `json:"like_user"`
	LikeHotspot int `json:"like_hotspot"`
	LikeId      int `json:"like_id"`
}

func GetLikes() (*[]Like, error) {
	o := orm.NewOrm()
	var likes []Like
	_, err := o.Raw("SELECT * FROM `like`").QueryRows(&likes)
	return &likes, err
}

func InsertLike(like *Like) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO `like` (like_hotspot, like_user) VALUES (?, ?);", like.LikeHotspot, like.LikeUser).Exec()
	insertId, _ := res.LastInsertId()
	return insertId, err
}

func DeleteLike(id int) error {
	o := orm.NewOrm()
	_, err := o.Raw("DELETE FROM `like` WHERE like_id = ?", id).Exec()
	return err
}

func GetLikesByUserId(id int) (*[]Like, error) {
	o := orm.NewOrm()
	var likes []Like
	_, err := o.Raw("SELECT * FROM `like` WHERE like_user = ?", id).QueryRows(&likes)
	return &likes, err
}
