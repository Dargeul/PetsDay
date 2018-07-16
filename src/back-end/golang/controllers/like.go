package controllers

import (
	"encoding/json"
	Models "pets-day/models"
	"strconv"

	"github.com/astaxie/beego"
)

type LikeController struct {
	beego.Controller
}

func (c *LikeController) Get() {
	var likes *[]Models.Like
	var err error
	userId, userErr := c.GetInt("user_id")
	if nil == userErr {
		likes, err = Models.GetLikesByUserId(userId)
	} else {
		likes, err = Models.GetLikes()
	}
	if nil == err {
		c.Data["json"] = likes
	}
	c.ServeJSON()
}

func (c *LikeController) Post() {
	var like Models.Like
	json.Unmarshal(c.Ctx.Input.RequestBody, &like)
	insertId, err := Models.InsertLike(&like)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}

func (c *LikeController) Delete() {
	id, _ := strconv.Atoi(c.Ctx.Input.Params()[":id"])
	err := Models.DeleteLike(id)
	if nil == err {
		c.Data["json"] = true
	} else {
		c.Data["json"] = false
	}
	c.ServeJSON()
}