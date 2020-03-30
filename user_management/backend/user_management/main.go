package main

import (
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/gorilla/mux"
	"github.com/jinzhu/gorm"
	"github.com/subosito/gotenv"

	"user_management/driver"
	"user_management/middleware"
	uh "user_management/user/handler"
	ur "user_management/user/repo"
	us "user_management/user/usecase"
)

var db *gorm.DB

func init() {
	gotenv.Load()
}

func main() {
	db = driver.ConnectDB()
	defer db.Close()
	driver.InitTable(db)

	r := mux.NewRouter().StrictSlash(true)

	userRepo := ur.CreateUserRepoImpl(db)
	userUsecase := us.CreateUserUsecaseImpl(userRepo)
	uh.CreateUserHandler(r, userUsecase)

	r.Use(middleware.Logger)

	fmt.Println("Starting Web Server at Port: " + os.Getenv("PORT"))
	err := http.ListenAndServe(":"+os.Getenv("PORT"), r)
	if err != nil {
		log.Fatal(err)
	}
}