package controllers

import (
	"encoding/json"
	Models "pets-day/models"

	"github.com/astaxie/beego"
)

type PetAndUserController struct {
	beego.Controller
}

func (c *PetAndUserController) Get() {
	petAndUsers, err := Models.GetPetAndUser()
	if nil == err {
		c.Data["json"] = petAndUsers
	}
	c.ServeJSON()
}

func (c *PetAndUserController) Post() {
	petAndUser := Models.PetAndUser{}
	json.Unmarshal(c.Ctx.Input.RequestBody, &petAndUser)
	insertId, err := Models.InsertPetAndUsers(&petAndUser)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}

func (c *PetAndUserController) Delete() {
	pet_id, _ := c.GetInt("pet_id")
	user_id, _ := c.GetInt("user_id")
	err := Models.DeletePetAndUsers(pet_id, user_id)
	if nil == err {
		c.Data["json"] = true
	} else {
		c.Data["json"] = false
	}
	c.ServeJSON()
}
