package util

import (
	"encoding/json"
	"log"

	"user_management/model"
)

func DataWrapper(status bool, message string, data interface{}) []byte {
	dataWrap := model.DataWrapper{
		Status:  status,
		Message: message,
		Data:    data,
	}
	jsonData, err := json.Marshal(dataWrap)
	if err != nil {
		log.Fatal("Error when encode to json : ", err)
	}
	return jsonData
}
