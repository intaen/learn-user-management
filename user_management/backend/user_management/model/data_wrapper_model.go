package model

//DataWrapper struct
type DataWrapper struct {
	Status  bool        `from:"Status" json:"status"`
	Message string      `from:"Message" json:"message"`
	Data    interface{} `from:"Data" json:"data"`
}
