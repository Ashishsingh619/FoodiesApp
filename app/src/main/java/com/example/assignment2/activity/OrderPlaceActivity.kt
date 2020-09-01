package com.example.assignment2.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import com.example.assignment2.R
import com.example.assignment2.database.FoodDatabse

class OrderPlaceActivity : AppCompatActivity() {
    lateinit var btn_ok: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_place)
        btn_ok = findViewById(R.id.btn_ok)
        btn_ok.setOnClickListener {
            CartClear(this@OrderPlaceActivity).execute().get()
            finish()
        }

    }
}

class CartClear(val context: Context) : AsyncTask<Void, Void, Boolean>() {
    var db = Room.databaseBuilder(context, FoodDatabse::class.java, "food-db").build()
    override fun doInBackground(vararg p0: Void?): Boolean {
        var delete = db.foodDao().deleteOrders()
        return true
    }
}

