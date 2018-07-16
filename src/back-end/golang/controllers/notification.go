package controllers

import (
	"encoding/json"
	Models "pets-day/models"
	"strconv"

	"github.com/astaxie/beego"
)

type NotificationController struct {
	beego.Controller
}

func (c *NotificationController) Get() {
	var notifications *[]Models.Notification
	var err error
	userId, userErr := c.GetInt("user_id")
	if nil == userErr {
		notifications, err = Models.GetNotificationsByUserId(userId)
	} else {
		notifications, err = Models.GetNotifications()
	}
	if nil == err {
		c.Data["json"] = notifications
	}
	c.ServeJSON()
}

func (c *NotificationController) Post() {
	var notification Models.Notification
	json.Unmarshal(c.Ctx.Input.RequestBody, &notification)
	insertId, err := Models.InsertNotification(&notification)
	if nil == err {
		c.Data["json"] = insertId
	}
	c.ServeJSON()
}

func (c *NotificationController) Patch() {
	var notification Models.Notification
	json.Unmarshal(c.Ctx.Input.RequestBody, &notification)
	id, _ := strconv.Atoi(c.Ctx.Input.Params()[":id"])
	err := Models.UpdateNotification(&notification, id)
	if nil == err {
		c.Data["json"] = true
	} else {
		c.Data["json"] = false
	}
	c.ServeJSON()
}
