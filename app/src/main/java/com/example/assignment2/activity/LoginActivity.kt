package com.example.assignment2.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import com.example.assignment2.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var et_phone: String
    lateinit var et_password: String
    lateinit var sharedpreference: SharedPreferences
    lateinit var btn_Login: Button
    lateinit var txt_forget_password: TextView
    lateinit var txt_register: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedpreference =
            getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE)
        btn_Login = findViewById(R.id.btn_login)


        var loggedIn = sharedpreference.getBoolean("LoggedIn", false)

        if (loggedIn) {
            val intent = Intent(this@LoginActivity, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
        txt_register = findViewById(R.id.txt_Register)
        txt_forget_password = findViewById(R.id.txt_forgetPassword)
        txt_register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
        txt_forget_password.setOnClickListener {
            val intent = Intent(this@LoginActivity, ResetActivity::class.java)
            startActivity(intent)
        }

        btn_Login.setOnClickListener {
            et_phone = findViewById<EditText>(R.id.et_phone).text.toString()
            et_password = findViewById<EditText>(R.id.et_password).text.toString()


            if (ConnectionManager().checkConnection(this@LoginActivity)) {
                if (et_phone.trim().isBlank() || et_password.trim().isBlank()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "The Input field cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (et_phone.length < 10 || et_password.length < 6) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Invalid Credentials length",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    var queue = Volley.newRequestQueue(this@LoginActivity)
                    val url = "http://13.235.250.119/v2/login/fetch_result"
                    var jsonParam = JSONObject()
                    jsonParam.put("mobile_number", et_phone)
                    jsonParam.put("password", et_password)

                    val jsonObjectReq =
                        object :
                            JsonObjectRequest(Method.POST, url, jsonParam, Response.Listener {
                                try {

                                    var data = it.getJSONObject("data")
                                    println(it)
                                    var success = data.getBoolean("success")
                                    println(success)
                                    if (success) {
                                        println(" i am in json")
                                        var dataInside = data.getJSONObject("data")
                                        var userId = dataInside.getString("user_id")
                                        sharedpreference.edit().putString("User_id", userId).apply()
                                        var phoneRec = dataInside.getString("mobile_number")
                                        sharedpreference.edit().putString("Pphone", phoneRec)
                                            .apply()
                                        var emailRec = dataInside.getString("email")
                                        sharedpreference.edit().putString("Pemail", emailRec)
                                            .apply()
                                        var nameRec = dataInside.getString("name")
                                        sharedpreference.edit().putString("Pname", nameRec).apply()
                                        var addressRec = dataInside.getString("address")
                                        sharedpreference.edit().putString("Paddress", addressRec)
                                            .apply()
                                        sharedpreference.edit().putBoolean("LoggedIn", true).apply()
                                        val intent = Intent(
                                            this@LoginActivity,
                                            NavigationActivity::class.java
                                        )
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Invalid Credentials",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: JSONException) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Try_catch error",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }, Response.ErrorListener {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Json Error Object ${it}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                var headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "992284710ee108"//992284710ee108
                                return headers
                            }
                        }
                    queue.add(jsonObjectReq)
                }
            } else {
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("OOPS!!")
                dialog.setMessage("No Internet Connection Found")
                dialog.setPositiveButton("Go to Settings") { text, Listener ->
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, Listener ->
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }


}
