package models

import "github.com/astaxie/beego/orm"

type User struct {
	UserId       int    `json:"user_id"`
	Password     string `json:"password"`
	Username     string `json:"username"`
	UserNickname string `json:"user_nickname"`
}

func GetUsers() (*[]User, error) {
	o := orm.NewOrm()
	var users []User
	_, err := o.Raw("SELECT * FROM user").QueryRows(&users)
	return &users, err
}

func InsertUser(user *User) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO user (username, password, user_nickname) VALUES (?, ?, ?);", user.Username, user.Password, user.UserNickname).Exec()
	insertId, _ := res.LastInsertId()
	return insertId, err
}

func UpdateUser(user *User, id int) error {
	o := orm.NewOrm()
	_, err := o.Raw("UPDATE user SET password = ?, user_nickname = ? WHERE user_id = ?;", user.Password, user.UserNickname, id).Exec()
	return err
}

func CheckUser(user *User) (*User, error) {
	o := orm.NewOrm()
	var users []User
	_, err := o.Raw("SELECT * FROM user WHERE username = ? && password = ?;", user.Username, user.Password).QueryRows(&users)
	if 0 == len(users) {
		return nil, err
	} else {
		return &users[0], err
	}
}

func GetOneUser(username string) (User, error) {
	o := orm.NewOrm()
	var user User
	err := o.Raw("SELECT * FROM user WHERE username = ?;", username).QueryRow(&user)
	return user, err
}