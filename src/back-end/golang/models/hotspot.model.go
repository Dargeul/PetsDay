package models

import "github.com/astaxie/beego/orm"

type Hotspot struct {
	HsTime       string `json:"hs_time"`
	HsUser       int    `json:"hs_user"`
	HsContent    string `json:"hs_content"`
	HsPhoto      string `json:"hs_photo"`
	HsId         int    `json:"hs_id"`
	UserNickname string `json:"user_nickname"`
	PetId        int    `json:"pet_id,omitempty"`
	CountLike    int    `json:"count_like"`
	CountComment int    `json:"count_comment"`
}

// type Hotspots struct {
// 	Hotspots []Hotspot `json:"hotspots"`
// }

func GetHotspots() (*[]Hotspot, error) {
	o := orm.NewOrm()
	var hotspots []Hotspot
	_, err := o.Raw("SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hs_id, ifnull(count_like, 0) AS count_like , ifnull(count_comment, 0) AS count_comment FROM `hotspot` LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS count_like FROM `like` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS count_comment FROM `comment` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user ORDER BY `hs_id` DESC LIMIT 20").QueryRows(&hotspots)
	return &hotspots, err
}

func GetHotspotsByHsId(hotspotId int) (*[]Hotspot, error) {
	o := orm.NewOrm()
	var hotspots []Hotspot
	_, err := o.Raw("SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hotspot.hs_id, ifnull(count_like, 0) AS count_like , ifnull(count_comment, 0) AS count_comment FROM `hotspot`  LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS count_like FROM `like` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS count_comment FROM `comment` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user WHERE hs_id = ?", hotspotId).QueryRows(&hotspots)
	return &hotspots, err
}

func GetHotspotsByPetId(petId int) (*[]Hotspot, error) {
	o := orm.NewOrm()
	var hotspots []Hotspot
	_, err := o.Raw("SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hotspot.hs_id, pet_id, count_like, ifnull(count_comment, 0) AS count_comment FROM `hotspot` LEFT JOIN pet_and_hotspot ON hotspot.hs_id=pet_and_hotspot.hs_id  LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS count_like FROM `like` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS count_comment FROM `comment` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user WHERE pet_id = ?", petId).QueryRows(&hotspots)
	return &hotspots, err
}

func GetHotspotsByPage(page int) (*[]Hotspot, error) {
	o := orm.NewOrm()
	var hotspots []Hotspot
	_, err := o.Raw("SELECT hs_time, hs_user, user_nickname, hs_content, hs_photo, hs_id, ifnull(count_like, 0) AS count_like, ifnull(count_comment, 0) AS count_comment FROM `hotspot` LEFT JOIN (SELECT like_hotspot, COUNT(like_hotspot) AS count_like FROM `like` GROUP BY like_hotspot) AS count_table ON count_table.like_hotspot=hotspot.hs_id LEFT JOIN (SELECT com_hs, COUNT(com_hs) AS count_comment FROM `comment` GROUP BY com_hs) AS count_comment_table ON count_comment_table.com_hs=hotspot.hs_id LEFT JOIN user ON user.user_id=hs_user WHERE hs_id <= (SELECT COUNT(*) FROM `hotspot`)- ?*20 ORDER BY hs_id DESC LIMIT 20", page).QueryRows(&hotspots)
	return &hotspots, err
}

func InsertHotspot(hotspot *Hotspot) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO hotspot (hs_content, hs_photo, hs_time, hs_user) VALUES (?, ?, ?, ?);", hotspot.HsContent, hotspot.HsPhoto, hotspot.HsTime, hotspot.HsUser).Exec()
	insertId, _ := res.LastInsertId()
	return insertId, err
}
