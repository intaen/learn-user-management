package middleware

import (
	util "user_management/utils"
	"fmt"
	"github.com/dgrijalva/jwt-go"
	"net/http"
	"os"
	"strings"
)

// TokenVerify to verify token
func TokenVerify(next http.HandlerFunc) http.HandlerFunc {
	return http.HandlerFunc(func(resp http.ResponseWriter, req *http.Request) {
		authHeader := req.Header.Get("Authorization")
		bearerToken := strings.Split(authHeader, " ")

		if len(bearerToken) == 2 {
			authToken := bearerToken[1]

			token, err := jwt.Parse(authToken, func(token *jwt.Token) (i interface{}, err error) {
				if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
					return nil, fmt.Errorf("[TokenVerify.tokenSigning] Error: %w", err)
				}
				return []byte(os.Getenv("SECRET")), nil
			})

			if err != nil {
				fmt.Errorf("[TokenVerify.Parse] Error: %w", err)
				util.HandleError(resp, http.StatusUnauthorized, "Oops, Something went wrong")
				return
			}

			if token.Valid {
				next.ServeHTTP(resp, req)
			} else {
				util.HandleError(resp, http.StatusUnauthorized, "Invalid Token")
				return
			}

		} else {
			util.HandleError(resp, http.StatusUnauthorized, "Invalid Token")
			return
		}
	})
}
