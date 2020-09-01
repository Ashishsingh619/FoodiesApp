package com.example.assignment2.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import com.example.assignment2.Restaurants
import com.example.assignment2.adapter.CartAdapter
import com.example.assignment2.database.FoodDatabse
import com.example.assignment2.database.FoodEntity
import com.example.assignment2.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_navigation.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var cartAdapter: CartAdapter
    lateinit var recyleCart: RecyclerView
    lateinit var btn_PlaceOrder: Button
    lateinit var txt_Cart_Res_Name: TextView
    lateinit var sharedpreference: SharedPreferences
    lateinit var Toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        sharedpreference =
            getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE)
        Toolbar = findViewById(R.id.toolbarCart)
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "My Cart"
        recyleCart = findViewById(R.id.recyleCart)
        btn_PlaceOrder = findViewById(R.id.btn_PlaceOrder)
        txt_Cart_Res_Name = findViewById(R.id.txt_Cart_Res_Name)
        var id_array = arrayListOf<String>()
        var Cartitems = CartItems(applicationContext).execute().get()
        var sum = 0
        for (i in 0..Cartitems.size - 1) {
            sum = sum + Cartitems[i].cost_for_one.toInt()
        }

        if (Cartitems != null) {
            btn_PlaceOrder.text = "Place Order(Total Rs.${sum})"
            btn_PlaceOrder.visibility = View.VISIBLE
            txt_Cart_Res_Name.text = sharedpreference.getString("RestName", "Name")
            layoutmanager = LinearLayoutManager(this@CartActivity)
            recyleCart.layoutManager = layoutmanager
            cartAdapter = CartAdapter(this@CartActivity, Cartitems)
            recyleCart.adapter = cartAdapter
        }
        if (Cartitems.isEmpty()) {
            btn_PlaceOrder.visibility = View.INVISIBLE
        }
        btn_PlaceOrder.setOnClickListener {
            if (ConnectionManager().checkConnection(this@CartActivity)) {
                var queue = Volley.newRequestQueue(this@CartActivity)
                val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                println(sharedpreference.getString("User_id", "99"))
                println(Cartitems[1].restId)
                println(sum)
                println(id_array)
                var getParam = JSONObject()
                getParam.put("user_id", sharedpreference.getString("User_id", "99"))
                getParam.put("restaurant_id", Cartitems[0].restId)
                getParam.put("total_cost", sum)
                val foodArray = JSONArray()
                for (i in 0 until Cartitems.size) {
                    val foodId = JSONObject()
                    foodId.put("food_item_id", Cartitems[i].food_Id)
                    foodArray.put(i, foodId)
                }
                getParam.put("food", foodArray)
                var jsonObjReq =
                    object : JsonObjectRequest(Method.POST, url, getParam, Response.Listener {
                        try {
                            println(it)
                            var data = it.getJSONObject("data")
                            var success = data.getBoolean("success")
                            if (success) {
                                val intent =
                                    Intent(this@CartActivity, OrderPlaceActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@CartActivity,
                                    "There was an error placing the error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@CartActivity,
                                "Json Error Occured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@CartActivity,
                                "Volley Error Occured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            var headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "992284710ee108"//992284710ee108
                            return headers
                        }
                    }
                queue.add(jsonObjReq)
            } else {
                val dialog = AlertDialog.Builder(this@CartActivity)
                dialog.setTitle("OOPS!!")
                dialog.setMessage("No Internet Connection Found")
                dialog.setPositiveButton("Go to Settings") { text, Listener ->
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                    finish()
                }
                dialog.setNegativeButton("Exit") { text, Listener ->
                    ActivityCompat.finishAffinity(this@CartActivity)
                }
                dialog.create()
                dialog.show()
            }
        }


    }

    override fun onBackPressed() {
        if (!CartItems(this@CartActivity).execute().get().isEmpty()) {
            val dialog = AlertDialog.Builder(this@CartActivity)
            dialog.setTitle("Alert!!")
            dialog.setMessage("Proceeding back will Clear the cart")
            dialog.setPositiveButton("Stay") { text, Listener ->
            }
            dialog.setNegativeButton("Clear the Cart") { text, Listener ->
                CartDelete(this@CartActivity).execute().get()
                super.onBackPressed()
            }
            dialog.create()
            dialog.show()
        } else
            super.onBackPressed()
    }
}

class CartItems(val context: Context) : AsyncTask<Void, Void, List<FoodEntity>>() {

    override fun doInBackground(vararg p0: Void?): List<FoodEntity> {
        var db = Room.databaseBuilder(context, FoodDatabse::class.java, "food-db").build()
        return db.foodDao().getAllFood()
    }
}

class CartDelete(val context: Context) : AsyncTask<Void, Void, Boolean>() {
    var db = Room.databaseBuilder(context, FoodDatabse::class.java, "food-db").build()
    override fun doInBackground(vararg p0: Void?): Boolean {
        var delete = db.foodDao().deleteOrders()
        return true
    }
}
