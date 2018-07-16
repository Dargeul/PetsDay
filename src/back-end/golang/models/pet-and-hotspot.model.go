package models

import "github.com/astaxie/beego/orm"

type PetAndHotspot struct {
	PetId int `json:"pet_id"`
	HsId  int `json:"hs_id"`
}

type PetAndHotspotArray struct {
	PetAndHotspots []PetAndHotspot `json:"pet_and_hotspots"`
}

func GetPetAndHotspot() (*[]PetAndHotspot, error) {
	o := orm.NewOrm()
	var petAndHotspots []PetAndHotspot
	_, err := o.Raw("SELECT * FROM pet_and_hotspot").QueryRows(&petAndHotspots)
	return &petAndHotspots, err
}

func InsertPetAndHotspot(petAndHotspot *PetAndHotspot) (error) {
	o := orm.NewOrm()
	res, err := o.Raw("INSERT INTO pet_and_hotspot (hs_id, pet_id) VALUES (?, ?);",
		petAndHotspot.HsId, petAndHotspot.PetId).Exec()
	_, err = res.LastInsertId()
	return err
}
