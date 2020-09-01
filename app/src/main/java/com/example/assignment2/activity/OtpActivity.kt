package com.example.assignment2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import org.json.JSONObject

class OtpActivity : AppCompatActivity() {
    lateinit var et_reset_otp: String
    lateinit var et_reset_password: String
    lateinit var et_reset_conpassword: String
    lateinit var btn_submit: Button
    var phoneNo: String? = "9998886669"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        if (intent != null) {
            phoneNo = intent.getStringExtra("Phone_no")
        }
        if (phoneNo == "9998886669") {
            phoneNo = intent.getStringExtra("Phone_no")
            Toast.makeText(
                this@OtpActivity,
                "Phone Number not recieved",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        btn_submit = findViewById(R.id.btn_submit)
        var queue = Volley.newRequestQueue(this@OtpActivity)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"
        var putparam = JSONObject()
        btn_submit.setOnClickListener {
            et_reset_otp = findViewById<EditText>(R.id.et_reset_otp).text.toString()
            et_reset_password = findViewById<EditText>(R.id.et_reset_password).text.toString()
            et_reset_conpassword = findViewById<EditText>(R.id.et_reset_conPassword).text.toString()
            if (et_reset_password.length < 6) {
                Toast.makeText(
                    this@OtpActivity,
                    "Password Doesn't match",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (et_reset_password == et_reset_conpassword) {
                putparam.put("mobile_number", phoneNo)
                putparam.put("password", et_reset_password)
                putparam.put("otp", et_reset_otp)
                val JsonObjectReq =
                    object : JsonObjectRequest(Method.POST, url, putparam, Response.Listener {

                        var data = it.getJSONObject("data")
                        println(it)
                        if (data.getBoolean("success")) {

                            Toast.makeText(
                                this@OtpActivity,
                                "${data.getString("successMessage")}",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(
                                this@OtpActivity,
                                LoginActivity::class.java
                            )
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@OtpActivity,
                                "Data not recieved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@OtpActivity,
                            "Volley error occured",
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
                queue.add(JsonObjectReq)

            } else

                Toast.makeText(
                    this@OtpActivity,
                    "Password Doesn't match",
                    Toast.LENGTH_SHORT
                ).show()
        }

    }
}
