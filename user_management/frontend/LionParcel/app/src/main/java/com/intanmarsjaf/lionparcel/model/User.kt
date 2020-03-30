package com.intanmarsjaf.bengongapps.model

import java.io.Serializable

data class User(
    var id: Int,
    var name: String?,
    var username: String?,
    var email: String?,
    var password: String?
): Serializable {}

class ResponseUser(
    var success: Boolean,
    var message: String,
    var data: User
) {
}

class UserRegister(
    var name: String,
    var username: String,
    var email: String,
    var password: String
) {

}

class UserLogin(
    var email: String,
    var password: String
) {

}