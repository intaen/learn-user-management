package com.intanmarsjaf.lionparcel.activity

import Login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.user_management.utils.Email
import com.intanmarsjaf.bengongapps.model.User
import com.intanmarsjaf.bengongapps.model.UserLogin
import com.intanmarsjaf.lionparcel.R
import com.intanmarsjaf.lionparcel.api.UserAPI
import com.intanmarsjaf.lionparcel.config.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sharedPreferences
import java.io.Serializable

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: sharedPreferences

    lateinit var btnLogin: TextView
    lateinit var btnRegister: TextView
    lateinit var email: EditText
    lateinit var isEmailValid: Email
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin = findViewById(R.id.login)
        btnRegister = findViewById(R.id.gotoSignup)
        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)

        isEmailValid = Email()
        sharedPreferences = sharedPreferences(this)

        btnLogin.setOnClickListener {
            if (email.text.isEmpty()) {
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
                Login()
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    fun Login() {
        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        val userAPI = RetrofitClient.createRetrofit().create(UserAPI::class.java)

        userAPI.login(UserLogin(inputEmail, inputPassword)).enqueue(object : Callback<Login> {
            override fun onFailure(call: Call<Login>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Error when connect to server",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                val data=JSONObject()
                data.put("email",email.text)
                data.put("password",password.text)

                var user = response.body()?.data

                if (response.isSuccessful) {
                    if (user == null) {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        sharedPreferences.saveString(sharedPreferences._token, response.body()?.data?.token.toString())
                        Toast.makeText(applicationContext, "Login success", Toast.LENGTH_SHORT)
                            .show()
                        goToHome()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    fun goToHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}