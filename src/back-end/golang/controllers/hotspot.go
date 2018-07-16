package controllers

import (
	"encoding/json"
	Models "pets-day/models"
	"strconv"

	"github.com/astaxie/beego"
)

type HotspotController struct {
	beego.Controller
}

func (c *HotspotController) Get() {
	var hotspots *[]Models.Hotspot
	var petDbErr, pageDbErr, hotspotDbErr, err error
	petId, petIdErr := c.GetInt("pet_id")
	page, pageErr := c.GetInt("page")
	if nil == petIdErr {
		hotspots, petDbErr = Models.GetHotspotsByPetId(petId)
	} else if nil == pageErr {
		hotspots, pageDbErr = Models.GetHotspotsByPage(page)
	} else {
		hotspots, err = Models.GetHotspots()
	}
	if nil == err && nil == petDbErr && nil == pageDbErr && nil == hotspotDbErr {
		c.Data["json"] = hotspots
	}
	c.ServeJSON()
}

func (c *HotspotController) Post() {
	var hotspot Models.Hotspot
	json.Unmarshal(c.Ctx.Input.RequestBody, &hotspot)
	insertId, err := Models.InsertHotspot(&hotspot)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}

func (c *HotspotController) GetOne() {
	id, _ := strconv.Atoi(c.Ctx.Input.Params()[":id"])
	hotspot, err := Models.GetHotspotsByHsId(id)
	if nil == err {
		c.Data["json"] = hotspot
	}
	c.ServeJSON()
}
