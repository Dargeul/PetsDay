package controllers

import (
	"encoding/json"
	Models "pets-day/models"
	"strconv"

	"github.com/astaxie/beego"
)

type CommentController struct {
	beego.Controller
}

func (c *CommentController) Get() {
	var comments *[]Models.Comment
	var err error
	hsId, hsErr := c.GetInt("hs_id")
	if nil == hsErr {
		comments, err = Models.GetCommentsByHsId(hsId)
	} else {
		comments, err = Models.GetComments()
	}
	if nil == err {
		c.Data["json"] = comments
	}
	c.ServeJSON()
}

func (c *CommentController) Post() {
	var comment Models.Comment
	json.Unmarshal(c.Ctx.Input.RequestBody, &comment)
	insertId, err := Models.InsertComment(&comment)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}

func (c *CommentController) Delete() {
	id, _ := strconv.Atoi(c.Ctx.Input.Params()[":id"])
	err := Models.DeleteComment(id)
	if nil == err {
		c.Data["json"] = true
	} else {
		c.Data["json"] = false
	}
	c.ServeJSON()
}
