package models

import "github.com/astaxie/beego/orm"

type Comment struct {
	ComTime      string `json:"com_time"`
	ComUser      int    `json:"com_user"`
	ComHs        int    `json:"com_hs"`
	ComContent   string `json:"com_content"`
	ComId        int    `json:"com_id"`
	UserNickname string `json:"user_nickname,omitempty"`
}

func GetComments() (*[]Comment, error) {
	o := orm.NewOrm()
	var comments []Comment
	_, err := o.Raw("SELECT * FROM comment").QueryRows(&comments)
	return &comments, err
}

func InsertComment(comment *Comment) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO comment (com_content, com_hs, com_time, com_user) VALUES (?, ?, ?, ?);", comment.ComContent, comment.ComHs, comment.ComTime, comment.ComUser).Exec()
	insertId, _ := res.LastInsertId()
	return insertId, err
}

func DeleteComment(id int) error {
	o := orm.NewOrm()
	_, err := o.Raw("DELETE FROM comment WHERE com_id = ?", id).Exec()
	return err
}

func GetCommentsByHsId(id int) (*[]Comment, error) {
	o := orm.NewOrm()
	var comments []Comment
	_, err := o.Raw("SELECT com_time, com_user, user_nickname, com_hs, com_content, com_id FROM `comment` LEFT JOIN user ON user.user_id=com_user WHERE com_hs = ?", id).QueryRows(&comments)
	return &comments, err
}
