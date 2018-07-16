package main

import (
	_ "pets-day/routers"

	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
)

func main() {
	beego.Run()
}

func init() {
	user := beego.AppConfig.String("mysqluser")
	password := beego.AppConfig.String("mysqlpass")
	database := beego.AppConfig.String("mysqldb")
	dbConfig := user + ":" + password + "@/" + database
	orm.RegisterDataBase("default", "mysql", dbConfig, 30)
	// Models.CreateDatabase()
	// Models.CreateInitialData()
}
