package handler

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	"github.com/jinzhu/gorm"
	"golang.org/x/crypto/bcrypt"

	"user_management/middleware"
	"user_management/model"
	"user_management/user"
	util "user_management/utils"
)

var db *gorm.DB

type UserHandler struct {
	userUsecase user.UserUsecase
}

func CreateUserHandler(r *mux.Router, userUsecase user.UserUsecase) {
	userHandler := UserHandler{userUsecase}

	s := r.PathPrefix("/user").Subrouter()
	s.HandleFunc("/signup", userHandler.SignUp).Methods(http.MethodPost)
	s.HandleFunc("/login", userHandler.Login).Methods(http.MethodPost)
	r.HandleFunc("/users", middleware.TokenVerify(userHandler.GetAll)).Methods(http.MethodGet)
	s.HandleFunc("/{id}", middleware.TokenVerify(userHandler.GetByID)).Methods(http.MethodGet)
	s.HandleFunc("/{id}", middleware.TokenVerify(userHandler.Update)).Methods(http.MethodPut)
	s.HandleFunc("/{id}", middleware.TokenVerify(userHandler.Delete)).Methods(http.MethodDelete)
}

func (u *UserHandler) SignUp(resp http.ResponseWriter, req *http.Request) {

	var user = model.User{}

	resp.Header().Set("Content-type", "application/json")
	json.NewDecoder(req.Body).Decode(&user)

	if user.Email == "" {
		util.HandleError(resp, http.StatusBadRequest, "Email is needed!")
	}
	if user.Password == "" {
		util.HandleError(resp, http.StatusBadRequest, "Password is needed!")
	}

	hash, err := bcrypt.GenerateFromPassword([]byte(user.Password), 10)
	if err != nil {
		fmt.Printf("[Hash Password] Error: %v", err)
		log.Fatal(err)
	}

	user.Password = string(hash)

	u.userUsecase.SignUp(&user)
	util.HandleSuccess(resp, http.StatusCreated, user)
}

func (u *UserHandler) Login(resp http.ResponseWriter, req *http.Request) {
	var user = model.User{}
	var jwt = model.JWT{}

	resp.Header().Set("Content-type", "application/json")
	json.NewDecoder(req.Body).Decode(&user)

	if user.Email == "" {
		util.HandleError(resp, http.StatusBadRequest, "Email is needed!")
	}
	if user.Password == "" {
		util.HandleError(resp, http.StatusBadRequest, "Password is needed!")
	}

	password := user.Password
	hashedPassword, err := u.userUsecase.Login(&user)
	if err != nil {
		util.HandleError(resp, http.StatusBadRequest, "User doesn't exist")
		return
	}

	isValidPassword := util.ComparePassword(hashedPassword.Password, []byte(password))
	if isValidPassword {
		token, err := util.GenerateToken(user)
		if err != nil {
			fmt.Printf("[UserHandler.Login] Error when generate token: %v", err)
		}

		jwt.Token = token
		jwt.User = user
		util.HandleSuccess(resp, http.StatusOK, jwt)
	} else {
		util.HandleError(resp, http.StatusUnauthorized, "Access Denied!")
	}
}

func (u *UserHandler) GetAll(resp http.ResponseWriter, req *http.Request) {
	user, err := u.userUsecase.GetAll()
	if err != nil {
		util.HandleError(resp, http.StatusInternalServerError, "Oops, Something went wrong")
		fmt.Printf("[UserHandler.GetDataByUser] Error when request data to service: %v\n", err)
		return
	}
	util.HandleSuccess(resp, http.StatusOK, user)
}

func (u *UserHandler) GetByID(resp http.ResponseWriter, req *http.Request) {
	pathVar := mux.Vars(req)
	id, err := strconv.Atoi(pathVar["id"])
	if err != nil {
		util.HandleError(resp, http.StatusBadRequest, "ID must number!")
		fmt.Printf("[UserHandler.GetByID] Error when convert path: %v\n", err)
	}

	user, err := u.userUsecase.GetByID(id)
	if err != nil {
		util.HandleError(resp, http.StatusNoContent, "Oops, Something went wrong")
		fmt.Printf("[UserHandler.GetByID] Error when request data to service: %v\n", err)
		return
	}
	util.HandleSuccess(resp, http.StatusOK, user)
}

func (u *UserHandler) Update(resp http.ResponseWriter, req *http.Request) {
	pathVar := mux.Vars(req)
	id, err := strconv.Atoi(pathVar["id"])
	if err != nil {
		util.HandleError(resp, http.StatusInternalServerError, "Oops, Something went wrong")
		fmt.Printf("[UserHandler.Update] Error when convert path: %v\n", err)
		return
	}

	user := model.User{}
	err = json.NewDecoder(req.Body).Decode(&user)
	if err != nil {
		util.HandleError(resp, http.StatusInternalServerError, "Oops, Something went wrong")
		fmt.Printf("[UserHandler.Update] Error when decode data: %v\n", err)
		return
	}

	data, err := u.userUsecase.Update(id, &user)
	if err != nil {
		util.HandleError(resp, http.StatusInternalServerError, "Oops, Something went wrong")
		fmt.Printf("[UserHandler.Update] Error when request data to service: %v\n", err)
		return
	}

	util.HandleSuccess(resp, http.StatusOK, data)
}

func (u *UserHandler) Delete(resp http.ResponseWriter, req *http.Request) {
	pathVar := mux.Vars(req)
	id, err := strconv.Atoi(pathVar["id"])
	if err != nil {
		util.HandleError(resp, http.StatusInternalServerError, "Oops, Something went wrong")
		fmt.Printf("[UserHandler.Delete] Error when convert path: %v\n", err)
		return
	}

	err = u.userUsecase.Delete(id)
	if err != nil {
		util.HandleError(resp, http.StatusInternalServerError, "Oops, Something went wrong")
		fmt.Printf("[UserHandler.Delete] Error when request data to service: %v\n", err)
		return
	}
	util.HandleSuccess(resp, http.StatusOK, nil)
}

func (u *UserHandler) HashPassword(user *model.User) string {
	hash, err := bcrypt.GenerateFromPassword([]byte(user.Password), 10)
	if err != nil {
		fmt.Printf("[Hash Password] Error: %v", err)
		log.Fatal(err)
	}
	user.Password = string(hash)
	hashPassword := user.Password

	return hashPassword
}
