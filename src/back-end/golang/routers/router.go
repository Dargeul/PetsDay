package routers

import (
	"pets-day/controllers"
	"github.com/astaxie/beego"
)

func init() {
	beego.Router("/user/?:id", &controllers.UserController{})
	beego.Router("/user/username/:username", &controllers.UserController{}, "get:GetOne")
	beego.Router("/user/check", &controllers.UserController{}, "post:CheckOne")	
	beego.Router("/pet", &controllers.PetController{})
	beego.Router("/pet/?:id", &controllers.PetController{}, "get:GetOne")
	beego.Router("/pet/?:id", &controllers.PetController{})	
	beego.Router("/pet/possess", &controllers.PetController{}, "get:GetPossess")
	beego.Router("/pet/follow", &controllers.PetController{}, "get:GetFollow")
	beego.Router("/comment/?:id", &controllers.CommentController{})
	beego.Router("/good", &controllers.GoodController{})
	beego.Router("/hotspot", &controllers.HotspotController{})
	beego.Router("/hotspot/?:id", &controllers.HotspotController{}, "get:GetOne")
	beego.Router("/like/?:id", &controllers.LikeController{})
	beego.Router("/notification/?:id", &controllers.NotificationController{})
	beego.Router("/pet_and_hotspot", &controllers.PetAndHotspotController{})
	beego.Router("/pet_and_user/?:id", &controllers.PetAndUserController{})
	beego.Router("/upload-handler", &controllers.UploadHandlerController{})
}
