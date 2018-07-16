package controllers

import (
	"encoding/json"
	Models "pets-day/models"

	"github.com/astaxie/beego"
)

type GoodController struct {
	beego.Controller
}

func (c *GoodController) Get() {
	goods, err := Models.GetGoods()
	if nil == err {
		c.Data["json"] = &goods
	}
	c.ServeJSON()
}

func (c *GoodController) Post() {
	var good Models.Good
	json.Unmarshal(c.Ctx.Input.RequestBody, &good)
	insertId, err := Models.InsertGood(&good)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}
