package repo

import (
	"user_management/model"
	"user_management/user"
	"fmt"
	"github.com/jinzhu/gorm"
)

type UserRepoImpl struct {
	db *gorm.DB
}

func CreateUserRepoImpl(db *gorm.DB) user.UserRepo {
	return &UserRepoImpl{db}
}

func (u *UserRepoImpl) SignUp(user *model.User) (*model.User, error) {
	err := u.db.Save(&user).Error
	if err != nil {
		fmt.Errorf("[UserRepoImpl.SignUp] Error when query insert: %v\n", err)
	}
	return user, nil
}

func (u *UserRepoImpl) Login(user *model.User) (*model.User, error) {
	err:= u.db.Where("email=?",user.Email).Find(&user).Error
	if err != nil {
		fmt.Errorf("[UserRepoImpl.Login] Error when query to get password: %w\n", err)
	}


	return user, nil
}

func (u *UserRepoImpl) GetAll() (*[]model.User, error) {
	user := []model.User{}

	err := u.db.Find(&user).Error
	if err != nil {
		return nil, fmt.Errorf("[UserRepoImpl.GetDataByUser] Error when query to get all data: %w\n", err)
	}
	return &user, nil
}

func (u *UserRepoImpl) GetByID(id int) (*model.User, error) {
	user := model.User{}

	err := u.db.First(&user, id).Error
	if err != nil {
		return nil, fmt.Errorf("[UserRepoImpl.GetByID] Error when query get data by ID")
	}
	return &user, nil
}

func (u *UserRepoImpl) Update(id int, data *model.User) (*model.User, error) {
	err := u.db.Model(&data).Where("id = ?", id).Update(map[string]interface{}{
		"name":    data.Name,
		"username": data.Username,
		"email":        data.Email,
		"password":     data.Password}).Error
	if err != nil {
		return nil, fmt.Errorf("[UserRepoImpl.Update] Error when query update: %v\n", err)
	}
	return data, nil
}

func (u *UserRepoImpl) Delete(id int) error {
	user := model.User{}
	err := u.db.Where("id = ?", id).Delete(&user).Error
	if err != nil {
		return fmt.Errorf("[UserRepoImpl.Delete] Error when query delete: %v\n", err)
	}
	return nil
}