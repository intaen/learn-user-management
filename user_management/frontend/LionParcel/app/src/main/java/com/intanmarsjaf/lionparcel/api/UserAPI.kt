package com.intanmarsjaf.lionparcel.api

import Login
import com.intanmarsjaf.bengongapps.model.ResponseUser
import com.intanmarsjaf.bengongapps.model.UserLogin
import com.intanmarsjaf.bengongapps.model.UserRegister
import com.intanmarsjaf.lionparcel.model.DataUser
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {
    @POST("/user/signup")
    fun register(@Body register: UserRegister): Call<ResponseUser>

    @POST("/user/login")
    fun login(@Body userLogin: UserLogin): Call<Login>

    @GET("/users")
    fun getAllUser(@Header("Authorization") token:String): Call<DataUser>

    @GET("user/{id}")
    fun getByID(@Header("Authorization")token: String, @Path("id")id:Int): Call<ResponseUser>

    @PUT("user/{id}")
    fun update(@Header("Authorization")token: String, @Path("id")id:Int, @Body request: RequestBody): Call<ResponseUser>

    @DELETE("user/{id}")
    fun delete(@Header("Authorization")token: String, @Path("id")id:Int): Call<ResponseUser>
}