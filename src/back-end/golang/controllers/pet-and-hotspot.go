package controllers

import (
	"encoding/json"
	Models "pets-day/models"

	"github.com/astaxie/beego"
)

type PetAndHotspotController struct {
	beego.Controller
}

func (c *PetAndHotspotController) Get() {
	petAndHotspots, err := Models.GetPetAndHotspot()
	if nil == err {
		c.Data["json"] = petAndHotspots
	}
	c.ServeJSON()
}

func (c *PetAndHotspotController) Post() {
	var petAndHotspots Models.PetAndHotspotArray
	json.Unmarshal(c.Ctx.Input.RequestBody, &petAndHotspots)
	for _, petAndHotspot := range petAndHotspots.PetAndHotspots {
		err := Models.InsertPetAndHotspot(&petAndHotspot)
		if nil != err {
			c.Data["json"] = false
			c.ServeJSON()
			return
		}
	}
	c.Data["json"] = true
	c.ServeJSON()
}
