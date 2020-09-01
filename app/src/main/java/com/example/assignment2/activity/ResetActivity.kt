package com.example.assignment2.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import com.example.assignment2.util.ConnectionManager
import org.json.JSONObject

class ResetActivity : AppCompatActivity() {
    lateinit var et_reset_phone: String
    lateinit var et_reset_email: String
    lateinit var btn_reset: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)


        var queue = Volley.newRequestQueue(this@ResetActivity)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
        var putparam = JSONObject()
        btn_reset = findViewById(R.id.btn_reset)
        btn_reset.setOnClickListener {

            et_reset_phone = findViewById<EditText>(R.id.et_reset_phone).text.toString()
            et_reset_email = findViewById<EditText>(R.id.et_reset_email).text.toString()
            if (ConnectionManager().checkConnection(this@ResetActivity)) {
                putparam.put("mobile_number", et_reset_phone)
                putparam.put("email", et_reset_email)
                val JsonObjectReq =
                    object : JsonObjectRequest(Method.POST, url, putparam, Response.Listener {
                        var data = it.getJSONObject("data")
                        println(it)
                        if (data.getBoolean("success")) {

                            finish()
                            var first_try = data.getBoolean("first_try")
                            if (first_try) {
                                val intent = Intent(this@ResetActivity, OtpActivity::class.java)
                                intent.putExtra("Phone_no", et_reset_phone)
                                startActivity(intent)
                                finish()
                                Toast.makeText(
                                    this@ResetActivity,
                                    "OTP sent to your mail",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else{
                            val intent = Intent(this@ResetActivity, OtpActivity::class.java)
                            intent.putExtra("Phone_no", et_reset_phone)
                            startActivity(intent)
                            finish()
                                Toast.makeText(
                                    this@ResetActivity,
                                    "You can reset password once in 24hours",
                                    Toast.LENGTH_SHORT
                                ).show()
                        } }else {
                            Toast.makeText(
                                this@ResetActivity,
                                "Data not recieved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@ResetActivity,
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

            } else {
                val dialog = AlertDialog.Builder(this@ResetActivity)
                dialog.setTitle("OOPS!!")
                dialog.setMessage("No Internet Connection Found")
                dialog.setPositiveButton("Go to Settings") { text, Listener ->
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, Listener ->
                    ActivityCompat.finishAffinity(this@ResetActivity)
                }
                dialog.create()
                dialog.show()
            }
        }
    }

}
