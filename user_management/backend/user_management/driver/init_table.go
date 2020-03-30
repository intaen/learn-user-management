package driver

import (
	"user_management/model"

	"github.com/jinzhu/gorm"
)

//InitTable representaion
func InitTable(db *gorm.DB) {
	db.AutoMigrate(&model.User{})
}