package models

import "github.com/astaxie/beego/orm"

type PetAndUser struct {
	PetId  int `json:"pet_id"`
	UserId int `json:"user_id"`
}

func GetPetAndUser() (*[]PetAndUser, error) {
	o := orm.NewOrm()
	var petAndUsers []PetAndUser
	_, err := o.Raw("SELECT * FROM pet_and_user").QueryRows(&petAndUsers)
	return &petAndUsers, err
}

func InsertPetAndUsers(petAndUser *PetAndUser) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO pet_and_user (pet_id, user_id) VALUES (?, ?);",
	petAndUser.PetId, petAndUser.UserId).Exec()
	insertId, err := res.LastInsertId()
	return insertId, err
}

func DeletePetAndUsers(petId int, userId int) (error) {
	o := orm.NewOrm()
	_, err := o.Raw("DELETE FROM pet_and_user WHERE pet_id = ?&&user_id = ?;", petId, userId).Exec()
	return err
}