package model

type User struct {
	ID 			int		`gorm:"primary_key;auto_increment"`
	Name        string `gorm:"column:name;not null" json:"name"`
	Username   	string `gorm:"column:username;not null" json:"username"`
	Email       string `gorm:"column:email;not null" json:"email"`
	Password    string `gorm:"column:password;not null" json:"password"`
}
