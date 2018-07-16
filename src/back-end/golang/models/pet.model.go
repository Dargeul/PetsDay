package models

import "github.com/astaxie/beego/orm"

type Pet struct {
	PetId        int    `json:"pet_id"`
	PetNickname  string `json:"pet_nickname"`
	PetOwner     int    `json:"pet_owner"`
	PetType      string `json:"pet_type"`
	PetWeight    int    `json:"pet_weight"`
	PetSex       string `json:"pet_sex"`
	PetBirth     string `json:"pet_birth"`
	PetPhoto     string `json:"pet_photo"`
	UserNickname string `json:"user_nickname,omitempty"`
	Count        int    `json:"count,omitempty"`
	HsId         int    `json:"hs_id,omitempty"`
}

func GetPets() (*[]Pet, error) {
	o := orm.NewOrm()
	var pets []Pet
	_, err := o.Raw("SELECT * FROM pet").QueryRows(&pets)
	return &pets, err
}

func InsertPet(pet *Pet) (int64, error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO pet (pet_birth, pet_owner, pet_nickname, pet_photo, pet_sex, pet_type, pet_weight) VALUES (?, ?, ?, ?, ?, ?, ?);", pet.PetBirth, pet.PetOwner, pet.PetNickname, pet.PetPhoto, pet.PetSex, pet.PetType, pet.PetWeight).Exec()
	insertId, _ := res.LastInsertId()
	return insertId, err
}

func UpdatePet(pet *Pet, id int) error {
	o := orm.NewOrm()
	_, err := o.Raw("UPDATE pet SET pet_birth = ?, pet_owner = ?, pet_nickname= ?, pet_photo = ?, pet_sex = ?, pet_type = ?, pet_weight = ? WHERE pet_id = ?;", pet.PetBirth, pet.PetOwner, pet.PetNickname, pet.PetPhoto, pet.PetSex, pet.PetType, pet.PetWeight, id).Exec()
	return err
}

func GetPossessPets(id int) (*[]Pet, error) {
	o := orm.NewOrm()
	var pets []Pet
	_, err := o.Raw("SELECT DISTINCT pet.pet_id, pet_nickname, pet_owner, user_nickname, pet_type, pet_weight, pet_sex, pet_birth, pet_photo, count FROM `pet` LEFT JOIN (SELECT pet_id, COUNT(pet_id) AS count FROM pet_and_user GROUP BY pet_id) AS count_table ON count_table.pet_id=pet.pet_id LEFT JOIN user ON user.user_id=pet_owner WHERE pet_owner = ?", id).QueryRows(&pets)
	return &pets, err
}

func GetFollowPets(id int) (*[]Pet, error) {
	o := orm.NewOrm()
	var pets []Pet
	_, err := o.Raw("SELECT DISTINCT pet.pet_id, pet_nickname, pet_owner, user_nickname pet_type, pet_weight, pet_sex, pet_birth, pet_photo, count FROM `pet` LEFT JOIN (SELECT pet_id, COUNT(pet_id) AS count FROM pet_and_user GROUP BY pet_id) AS count_table ON count_table.pet_id=pet.pet_id LEFT JOIN pet_and_user ON  pet_and_user.pet_id=pet.pet_id LEFT JOIN user ON user.user_id=pet_owner WHERE pet_and_user.user_id=?", id).QueryRows(&pets)
	return &pets, err
}

func GetPetsByHsId(id int) (*[]Pet, error) {
	o := orm.NewOrm()
	var pets []Pet
	_, err := o.Raw("SELECT DISTINCT pet.pet_id, pet_nickname, pet_owner, user_nickname, pet_type, pet_weight, pet_sex, pet_birth, pet_photo, hs_id, count FROM `pet` LEFT JOIN (SELECT pet_id, hs_id FROM `pet_and_hotspot`)AS joinTable ON pet.pet_id=joinTable.pet_id LEFT JOIN (SELECT pet_id, COUNT(pet_id) AS count FROM pet_and_user GROUP BY pet_id) AS count_table ON count_table.pet_id=pet.pet_id LEFT JOIN user ON user.user_id=pet_owner WHERE hs_id = ?", id).QueryRows(&pets)
	return &pets, err
}

func GetOnePet(id int) (*[]Pet, error) {
	o := orm.NewOrm()
	var pets []Pet
	_, err := o.Raw("SELECT DISTINCT pet.pet_id, pet_nickname, pet_type, pet_weight, pet_sex, pet_birth, pet_photo, user_nickname FROM `pet` LEFT JOIN pet_and_user ON pet.pet_id = pet_and_user.pet_id LEFT JOIN user ON user.user_id = pet_and_user.user_id WHERE pet.pet_id = ?", id).QueryRows(&pets)
	return &pets, err
}
