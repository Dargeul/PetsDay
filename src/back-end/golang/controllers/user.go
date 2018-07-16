package controllers

import (
	"encoding/json"
	Models "pets-day/models"
	"strconv"

	"github.com/astaxie/beego"
)

type UserController struct {
	beego.Controller
}

func (c *UserController) Get() {
	users, err := Models.GetUsers()
	if nil == err {
		c.Data["json"] = users
	}
	c.ServeJSON()
}

func (c *UserController) Post() {
	var user Models.User
	json.Unmarshal(c.Ctx.Input.RequestBody, &user)
	insertId, err := Models.InsertUser(&user)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}

func (c *UserController) Patch() {
	var user Models.User
	json.Unmarshal(c.Ctx.Input.RequestBody, &user)
	id, _ := strconv.Atoi(c.Ctx.Input.Params()[":id"])
	err := Models.UpdateUser(&user, id)
	if nil == err {
		c.Data["json"] = true
	} else {
		c.Data["json"] = false
	}
	c.ServeJSON()
}

func (c *UserController) CheckOne() {
	var user Models.User
	json.Unmarshal(c.Ctx.Input.RequestBody, &user)
	returnUser, err := Models.CheckUser(&user)
	if nil == err {
		c.Data["json"] = returnUser
	}
	c.ServeJSON()
}

func (c *UserController) GetOne() {
	username := c.Ctx.Input.Params()[":username"]
	user, err := Models.GetOneUser(username)
	if (nil == err) {
		c.Data["json"] = user
	}
	c.ServeJSON()
}