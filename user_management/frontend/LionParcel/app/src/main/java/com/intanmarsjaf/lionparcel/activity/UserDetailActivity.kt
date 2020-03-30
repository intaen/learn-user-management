package com.intanmarsjaf.lionparcel.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.intanmarsjaf.bengongapps.model.ResponseUser
import com.intanmarsjaf.lionparcel.R
import com.intanmarsjaf.lionparcel.api.UserAPI
import com.intanmarsjaf.lionparcel.config.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sharedPreferences

class UserDetailActivity : AppCompatActivity() {

    private lateinit var getSharedPreferences: sharedPreferences
    private lateinit var context: Context

    lateinit var btnUpdate: Button
    lateinit var btnDelete: Button
    lateinit var name: TextView
    lateinit var username: TextView
    lateinit var email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        getSharedPreferences = sharedPreferences(this)

        btnUpdate = findViewById(R.id.detailUpdate)
        btnDelete = findViewById(R.id.detailDeleteAccount)
        name = findViewById(R.id.detailFullName)
        username = findViewById(R.id.detailUserName)
        email = findViewById(R.id.detailEmail)

        val userID = intent.getStringExtra("id")
        val userName = intent.getStringExtra("name")
        val userUsername = intent.getStringExtra("username")
        val userEmail = intent.getStringExtra("email")
        findViewById<TextView>(R.id.detailFullName).setText(userName).toString()
        findViewById<TextView>(R.id.detailUserName).setText(userUsername).toString()
        findViewById<TextView>(R.id.detailEmail).setText(userEmail).toString()

        context = this

        addData(userID!!.toInt())

        btnDelete.setOnClickListener{
            delete(userID.toInt())
        }

        btnUpdate.setOnClickListener{
            update(userID.toInt())
        }
    }

    private fun addData(userID: Int){
        this?.let {
            val userAPI = RetrofitClient.createRetrofit().create(UserAPI::class.java)
            val getToken=getSharedPreferences.getString(getSharedPreferences._token).toString()

            userAPI.getByID("Bearer " + getToken,userID).enqueue(object:
                Callback<ResponseUser> {
                override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                    Log.e("FETCH FAIL", t.message.toString())
                    Toast.makeText(it, "Error Network timeout", Toast.LENGTH_LONG).show()}

                override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                    Log.i("FETCH SUCCESS", response.body().toString())
                    if (response.isSuccessful) {
                        name.text = response.body()?.data?.name
                        username.text=response.body()?.data?.username
                        email.text=response.body()?.data?.email
                    } else {
                        getSharedPreferences.remove(getSharedPreferences._token)
                        Toast.makeText(it, "Token invalid!", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(it, LoginActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }  }
            })
        }
    }

    private fun delete(userID: Int) {
        this?.let {
            val userAPI = RetrofitClient.createRetrofit().create(UserAPI::class.java)
            val getToken = getSharedPreferences.getString(getSharedPreferences._token).toString()

            userAPI.delete("Bearer " + getToken, userID).enqueue(object :
                Callback<ResponseUser> {
                override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                    Log.e("FETCH FAIL", t.message.toString())
                    Toast.makeText(it, "Error Network timeout", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(it, "Delete success", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(it, HomeActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
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

    private fun update(userID: Int) {
        this?.let {
            var intent= Intent(context,UserUpdateActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("id",userID)
            intent.putExtra("token",getSharedPreferences.getString(getSharedPreferences._token))
            intent.putExtra("name",name.text)
            intent.putExtra("username",username.text)
            intent.putExtra("email",email.text)
            context.startActivity(intent)
        }
    }
}
