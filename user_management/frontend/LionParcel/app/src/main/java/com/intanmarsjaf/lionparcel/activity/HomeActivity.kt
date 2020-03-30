package com.intanmarsjaf.lionparcel.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.intanmarsjaf.bengongapps.adapter.ListUserAdapter
import com.intanmarsjaf.bengongapps.model.User
import com.intanmarsjaf.lionparcel.R
import com.intanmarsjaf.lionparcel.api.UserAPI
import com.intanmarsjaf.lionparcel.config.RetrofitClient
import com.intanmarsjaf.lionparcel.model.DataUser
import com.intanmarsjaf.lionparcel.model.ListUser
import com.intanmarsjaf.lionparcel.utils.JWTUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sharedPreferences

class HomeActivity : AppCompatActivity() {

    private lateinit var getSharedPreferences: sharedPreferences
    private lateinit var context: Context
    lateinit var getToken: String

    lateinit var btnLogout: Button
    lateinit var email:TextView

    lateinit var listView: ListView
    lateinit var arrayAdapter: ListUserAdapter
    var listUser = mutableListOf<ListUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user_list)
        getSharedPreferences = sharedPreferences(this)
        getToken = getSharedPreferences.getString(getSharedPreferences._token).toString()

        btnLogout = findViewById(R.id.logout)
        email=findViewById(R.id.home_email)

        context = this

        if (getToken == "empty") {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        var getData=JWTUtil.decoded(getSharedPreferences.getString(getSharedPreferences._token).toString())
        email.text=getData.getString("email")

        listView = findViewById<ListView>(R.id.user_list)
        arrayAdapter = ListUserAdapter(
            context = this@HomeActivity,
            userList = listUser
        )
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener{ _, _, position, _ ->
            startActivity(Intent(this, UserDetailActivity::class.java).apply {
                putExtra("id",listUser[position].id.toString())
                putExtra("name", listUser[position].name)
                putExtra("username", listUser[position].username)
                putExtra("email", listUser[position].email)
            })
        }

        listUser()

        btnLogout.setOnClickListener {
            this?.let {
                getSharedPreferences.remove(getSharedPreferences._token)
                startActivity(
                    Intent(it, LoginActivity::class.java)
                )
                finish()
            }
        }
    }

    private fun listUser() {
        this?.let {
            val userAPI = RetrofitClient.createRetrofit().create(UserAPI::class.java)

            getToken = getSharedPreferences.getString(getSharedPreferences._token).toString()
            userAPI.getAllUser("Bearer " + getToken).enqueue(object : Callback<DataUser> {
                override fun onFailure(call: Call<DataUser>, t: Throwable) {
                    Log.e("[UserViewModel.getByID] Error occured: ", t.message)
                }

                override fun onResponse(
                    call: Call<DataUser>,
                    response: Response<DataUser>
                ) {
                    if (response.isSuccessful) {
                        if (getToken == "empty") {
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            finish()

                        } else {
                            val getData = response.body()?.data
                            getAllUser(getData)
                        }

                    } else {
                        getSharedPreferences.remove(getSharedPreferences._token)
                        Toast.makeText(it, "Token invalid!", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(it, LoginActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }
                }
            })
        }
    }

    fun getAllUser(user: List<ListUser>?) {
        arrayAdapter.clear()
        for (i in 0 until user!!.size) {
            println("fetchGetAllUser : " + user[i].name)
            arrayAdapter.add(user[i])
        }
    }
}