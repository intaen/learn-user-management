package com.intanmarsjaf.lionparcel.model

import com.google.gson.annotations.SerializedName

class DataUser (status: Boolean, message: String, data: List<ListUser>){
    @SerializedName("status")
    var status:Boolean? = status
    @SerializedName("message")
    var message:String? = message
    @SerializedName("data")
    var data:List<ListUser>? = data
}

data class ListUser(
    var id: Int,
    var name: String,
    var username: String,
    var email: String,
    var password: String
)