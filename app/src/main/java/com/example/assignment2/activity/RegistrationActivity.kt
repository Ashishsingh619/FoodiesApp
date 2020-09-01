package com.example.assignment2.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import com.example.assignment2.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {
    lateinit var et_Reg_name: String
    lateinit var et_Reg_email: String
    lateinit var et_Reg_phone: String
    lateinit var et_Reg_adress: String
    lateinit var et_Reg_password: String
    lateinit var et_Reg_conPassword: String
    lateinit var btn_register: Button
    lateinit var sharedpreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        sharedpreferences =
            getSharedPreferences("getString(R.string.shared_file_name)", Context.MODE_PRIVATE)

        btn_register = findViewById(R.id.btn_register)
        btn_register.setOnClickListener {
            et_Reg_name = findViewById<EditText>(R.id.et_Reg_name).text.toString()
            et_Reg_email = findViewById<EditText>(R.id.et_Reg_email).text.toString()
            et_Reg_phone = findViewById<EditText>(R.id.et_Reg_phone).text.toString()
            et_Reg_adress = findViewById<EditText>(R.id.et_Reg_address).text.toString()
            et_Reg_password = findViewById<EditText>(R.id.et_Reg_password).text.toString()
            et_Reg_conPassword = findViewById<EditText>(R.id.et_Reg_conPassword).text.toString()
            if (ConnectionManager().checkConnection(this@RegistrationActivity)) {
                if (et_Reg_name.isBlank() || et_Reg_email.isBlank() || et_Reg_phone.isBlank() || et_Reg_adress.isBlank()) {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "The input field cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (et_Reg_name.length <= 3 || et_Reg_password.length < 6) {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Inavlid Information",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (et_Reg_password == et_Reg_conPassword) {
                    var queue = Volley.newRequestQueue(this@RegistrationActivity)
                    val url =
                        "http://13.235.250.119/v2/register/fetch_result"// http://13.235.250.119/v2/register/fetch_result
                    var jsonParam = JSONObject()
                    jsonParam.put("name", et_Reg_name)
                    jsonParam.put("mobile_number", et_Reg_phone)
                    jsonParam.put("password", et_Reg_password)
                    jsonParam.put("address", et_Reg_adress)
                    jsonParam.put("email", et_Reg_email)
                    var jsonObjectReq =
                        object : JsonObjectRequest(Method.POST, url, jsonParam, Response.Listener {
                            var data = it.getJSONObject("data")
                            var success = data.getBoolean("success")
                            println(it)
                            Log.e("resposne", "" + data)
                            try {
                                Toast.makeText(
                                    this@RegistrationActivity,
                                    "Congrats!! You are Registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (success) {

                                    Log.e("resposne", "" + success)
                                    var dataInside = data.getJSONObject("data")
                                    sharedpreferences.edit()
                                        .putString("name", dataInside.getString("name"))
                                        .apply()
                                    sharedpreferences.edit()
                                        .putString("Email", dataInside.getString("email"))
                                        .apply()
                                    sharedpreferences.edit()
                                        .putString("name", dataInside.getString("name"))
                                        .apply()
                                    sharedpreferences.edit()
                                        .putString("userId", dataInside.getString("user_id"))
                                        .apply()
                                    sharedpreferences.edit()
                                        .putString("address", dataInside.getString("address"))
                                        .apply()

                                    startActivity(
                                        Intent(
                                            this,
                                            LoginActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Data didnt Recieved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@RegistrationActivity,
                                    "Json Exception occured",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, Response.ErrorListener {
                            Log.e("resposne", "" + it)
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Volley Error occured",
                                Toast.LENGTH_SHORT
                            ).show()

                        }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "992284710ee108"
                                return headers
                            }
                        }
                    queue.add(jsonObjectReq)

                } else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Password Didn't match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val dialog = AlertDialog.Builder(this@RegistrationActivity)
                dialog.setTitle("OOPS!!")
                dialog.setMessage("No Internet Connection Found")
                dialog.setPositiveButton("Go to Settings") { text, Listener ->
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, Listener ->
                    ActivityCompat.finishAffinity(this@RegistrationActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
}
