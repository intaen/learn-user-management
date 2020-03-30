package middleware

import (
	"fmt"
	"net/http"
	"time"
)

//Logger message
func Logger(next http.Handler) http.Handler {
	return http.HandlerFunc(func(resp http.ResponseWriter, req *http.Request) {
		resp.Header().Add("header-x", "XXXXXX")
		fmt.Printf("URL %v dipanggil pada jam %v\n", req.URL.Path, time.Now())
		next.ServeHTTP(resp, req)
		fmt.Printf("URL %v selesai dipanggil pada jam %v\n", req.URL.Path, time.Now())
	})
}
