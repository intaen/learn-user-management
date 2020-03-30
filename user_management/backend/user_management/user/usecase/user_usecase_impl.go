package usecase

import (
	"user_management/model"
	"user_management/user"
)

type UserUsecaseImpl struct {
	userRepo user.UserRepo
}

func CreateUserUsecaseImpl(userRepo user.UserRepo) user.UserUsecase {
	return &UserUsecaseImpl{userRepo}
}

func (u *UserUsecaseImpl) SignUp(user *model.User) (*model.User, error) {
	return u.userRepo.SignUp(user)
}

func (u *UserUsecaseImpl) Login(user *model.User) (*model.User, error) {
	return u.userRepo.Login(user)
}

func (u *UserUsecaseImpl) GetAll() (*[]model.User, error) {
	return u.userRepo.GetAll()
}

func (u *UserUsecaseImpl) GetByID(id int) (*model.User, error) {
	return u.userRepo.GetByID(id)
}

func (u *UserUsecaseImpl) Update(id int, data *model.User) (*model.User, error) {
	return u.userRepo.Update(id, data)
}

func (u *UserUsecaseImpl) Delete(id int) error {
	return u.userRepo.Delete(id)
}
