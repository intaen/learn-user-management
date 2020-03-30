package com.intanmarsjaf.lionparcel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.user_management.utils.Email
import com.intanmarsjaf.bengongapps.model.ResponseUser
import com.intanmarsjaf.lionparcel.R
import com.intanmarsjaf.lionparcel.api.UserAPI
import com.intanmarsjaf.lionparcel.config.RetrofitClient
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sharedPreferences

class UserUpdateActivity : AppCompatActivity() {

    private lateinit var getSharedPreferences: sharedPreferences
    lateinit var name: EditText
    lateinit var username: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var btnUpdate: Button
    lateinit var isEmailValid: Email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_update)

        name = findViewById(R.id.update_name)
        username = findViewById(R.id.update_username)
        email = findViewById(R.id.update_email)
        password = findViewById(R.id.update_password)
        btnUpdate = findViewById(R.id.update)

        val userID = intent.getStringExtra("id")
        val userEmail = intent.getStringExtra("email")
        findViewById<TextView>(R.id.upd_email).setText(userEmail).toString()

        isEmailValid = Email()
        getSharedPreferences = sharedPreferences(this)

        btnUpdate.setOnClickListener {
            if (name.text.isEmpty()) {
                name.error = "Name is required"
                name.requestFocus()
                return@setOnClickListener
            } else if (username.text.isEmpty()) {
                username.error = "Username is required"
            } else if (email.text.isEmpty()) {
                email.error = "Email is required"
                email.requestFocus()
                return@setOnClickListener
            } else if (!isEmailValid.emailValidation(email.text.toString())) {
                email.error = "Email invalid"
                email.requestFocus()
                return@setOnClickListener
            } else if (password.text.isEmpty()) {
                password.error = "Password is required"
                password.requestFocus()
                return@setOnClickListener
            } else {
                Update(userID!!.toInt())
            }
        }
    }

    private fun Update(userID: Int) {
        this?.let {
            val data = JSONObject()
            data.put("name", name.text)
            data.put("user_name", username.text)
            data.put("email", email.text)
            data.put("password", password.text)

            val requestBody: RequestBody =
                RequestBody.create(MediaType.parse("application/json"), data.toString())
            val userAPI = RetrofitClient.createRetrofit().create(UserAPI::class.java)
            val getToken = this.intent.getStringExtra("token")
            Log.d("TOKEN =>", getToken!!.toString())

            userAPI.update("Bearer " + getToken, userID, requestBody)
                .enqueue(object :
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
                            Toast.makeText(it, "Update success", Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        name.setText(intent.getStringExtra("name")?.toString())
        username.setText(intent.getStringExtra("username")?.toString())
        email.setText(intent.getStringExtra("email")?.toString())
    }
}

