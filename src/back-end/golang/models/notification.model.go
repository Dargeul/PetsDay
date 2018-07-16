package models

import "github.com/astaxie/beego/orm"

type Notification struct {
	NoticeStatus  int    `json:"notice_status"`
	NoticeUser    int    `json:"notice_user"`
	NoticeComment int    `json:"notice_comment"`
	NoticeId      int    `json:"notice_id"`
	ComTime       string `json:"com_time,omitempty"`
	ComUser       int    `json:"com_user,omitempty"`
	UserNickname  string `json:"user_nickname,omitempty"`
	ComHs         int    `json:"com_hs,omitempty"`
	ComContent    string `json:"com_content,omitempty"`
}

func GetNotifications() (*[]Notification, error) {
	o := orm.NewOrm()
	var notifications []Notification
	_, err := o.Raw("SELECT * FROM notification").QueryRows(&notifications)
	return &notifications, err
}

func InsertNotification(notification *Notification) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO notification (notice_comment, notice_status, notice_user) VALUES (?, ?, ?);", notification.NoticeComment, notification.NoticeStatus, notification.NoticeUser).Exec()
	insertId, _ := res.LastInsertId()
	return insertId, err
}

func UpdateNotification(notification *Notification, id int) error {
	o := orm.NewOrm()
	_, err := o.Raw("UPDATE notification SET notice_status = ? WHERE notice_id = ?;", notification.NoticeStatus, id).Exec()
	return err
}

func GetNotificationsByUserId(id int) (*[]Notification, error) {
	o := orm.NewOrm()
	var notifications []Notification
	_, err := o.Raw("SELECT DISTINCT notice_status, notice_user,  notice_comment, notice_id, com_time, com_user, user_nickname, com_hs, com_content FROM notification LEFT JOIN comment ON notice_comment=comment.com_id LEFT JOIN user ON user.user_id = com_user WHERE notice_user = ?", id).QueryRows(&notifications)
	return &notifications, err
}
