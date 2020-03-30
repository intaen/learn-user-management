package util

import (
	"encoding/json"
	"github.com/dgrijalva/jwt-go"
	"os"

	"log"
	"net/http"

	"golang.org/x/crypto/bcrypt"

	"user_management/model"
)

func HandleError(resp http.ResponseWriter, status int, message string) {
	var error model.Error
	error.Message = message

	resp.Header().Set("Content-Type", "application/json")
	resp.WriteHeader(status)
	json.NewEncoder(resp).Encode(error)
}

// HandleSuccess func
func HandleSuccess(resp http.ResponseWriter, status int, data interface{}) {
	jsonData := DataWrapper(true, "success", data)

	resp.Header().Set("Content-Type", "application/json")
	resp.WriteHeader(status)
	resp.Write(jsonData)
}

// LogError func
func LogError(function string, str string, err error) {
	log.Fatal("[%v] %v : %v", function, str, err)
}

func ComparePassword(hashedPassword string, password []byte) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hashedPassword), password)
	if err != nil {
		return false
	}
	return true
}

func GenerateToken(user model.User) (string, error) {
	secret := os.Getenv("SECRET")

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{ // decode data struct user menjadi token
		"name":user.Name,
		"user_name": user.Username,
		"email":user.Email,
	})

	tokenString, err := token.SignedString([]byte(secret))
	if err != nil {
		log.Fatal()
	}

	return tokenString, nil
}