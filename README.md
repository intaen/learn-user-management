# User Management
##### @intanmarsjaf
-----------------------
### Mobile Apps User Management using Kotlin and Golang
So, ignore the project name wkwkwk, in this project I made mobile apps about manage the user like register, delete, update, and get the user. But, I'm still beginner, so i failed when I want to get the id :((
I use two programming language to build this, golang for backend and kotlin for frontend. Stack in this apps are:
#### Golang:
+ Gorm
+ Postgre
+ Env
+ Bycrypt
+ Jwt
+ Gorilla Mux
#### Kotlin:
+ Retrofit
+ Custom Array Adapter
+ Shared Preferences
#### Assets:
+ flaticon.com
+ fonts.google.com

#### To run this apps, all you have to do:
+ Create database (I set up my database with name user_management), because im using gorm, so you dont have to create table
+ Run main.go file and the server will start in localhost:5051
```sh
go run main.go
```
+ Then, open android studio and start emulator, klik l>, wait for awhile...
+ Because the database is empty, we have to add user
+ So, this is API for UserManagement (because, I use JWT, so you have to bring token everywhere):
#### SignUp User
```sh
http://localhost:5051/user/signup
```
```sh
{
    "name": "example",
    "username": "example",
    "email": "example@gmail.com",
    "password": "123"
}
```
#### Login User
```sh
http://localhost:5051/user/login
```
```sh
{
    "email": "example@gmail.com",
    "password": "123"
}
```
#### GetAll User (+Bearer Token)
```sh
http://localhost:5051/users
```
#### GetByID User (+Bearer Token)
```sh
http://localhost:5051/user/{id}
```
#### Update User (Put) (+Bearer Token)
```sh
http://localhost:5051/user/{id}
```
#### Delete User (Delete) (+Bearer Token)
```sh
http://localhost:5051/user/{id}
```
