package driver

import (
	"log"
	"os"

	"github.com/jinzhu/gorm"
	_ "github.com/lib/pq"
)

var db *gorm.DB

func ConnectDB() *gorm.DB {
	connect := os.Getenv("POSTGRES_URL")
	db, err := gorm.Open("postgres", connect)
	if err != nil {
		log.Fatal(err)
	}

	return db
}
