package controllers

import (
	"encoding/json"
	Models "pets-day/models"
	"strconv"

	"github.com/astaxie/beego"
)

type PetController struct {
	beego.Controller
}

func (c *PetController) Get() {
	var pets *[]Models.Pet
	var err error
	hsId, hsErr := c.GetInt("hs_id")
	if nil == hsErr {
		pets, err = Models.GetPetsByHsId(hsId)
	} else {
		pets, err = Models.GetPets()
	}
	if nil == err {
		c.Data["json"] = pets
	}
	c.ServeJSON()
}

func (c *PetController) GetOne() {
	id, _ := strconv.Atoi(c.Ctx.Input.Params()[":id"])
	pets, err := Models.GetOnePet(id)
	if nil == err {
		c.Data["json"] = pets
	}
	c.ServeJSON()
}

func (c *PetController) GetPossess() {
	userId, _ := c.GetInt("user_id")
	pets, err := Models.GetPossessPets(userId)
	if nil == err {
		c.Data["json"] = pets
	}
	c.ServeJSON()
}

func (c *PetController) GetFollow() {
	userId, _ := c.GetInt("user_id")
	pets, err := Models.GetFollowPets(userId)
	if nil == err {
		c.Data["json"] = pets
	}
	c.ServeJSON()
}

func (c *PetController) Post() {
	var pet Models.Pet
	json.Unmarshal(c.Ctx.Input.RequestBody, &pet)
	insertId, err := Models.InsertPet(&pet)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}

func (c *PetController) Patch() {
	var pet Models.Pet
	json.Unmarshal(c.Ctx.Input.RequestBody, &pet)
	id, _ := strconv.Atoi(c.Ctx.Input.Params()[":id"])
	err := Models.UpdatePet(&pet, id)
	if nil == err {
		c.Data["json"] = true
	} else {
		c.Data["json"] = false
	}
	c.ServeJSON()
}
