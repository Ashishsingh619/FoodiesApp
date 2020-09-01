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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import com.example.assignment2.RestaurantItems
import com.example.assignment2.adapter.ResItemAdapter
import com.example.assignment2.database.FoodDatabse
import com.example.assignment2.database.FoodEntity
import com.example.assignment2.database.RestaurantDatabase
import com.example.assignment2.database.RestaurantEnitity
import com.example.assignment2.util.ConnectionManager
import org.json.JSONException

class ResdetailsActivity : AppCompatActivity() {
    var restItemId: String? = "01"
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var resItemsAdapter: ResItemAdapter
    lateinit var detailsRecyle: RecyclerView
    lateinit var btn_CartAdd: Button
    lateinit var RelativeFoodItems: RelativeLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    val rstItems = arrayListOf<RestaurantItems>()
    lateinit var sharedpreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resdetails)
        sharedpreference =
            getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE)
        detailsRecyle = findViewById(R.id.detailsRecyle)
        btn_CartAdd = findViewById(R.id.btn_CartAdd)
        toolbar = findViewById(R.id.toolbarResDetails)
        RelativeFoodItems = findViewById(R.id.RelativeFoodItems)
        RelativeFoodItems.visibility = View.VISIBLE
        if (intent != null) {
            restItemId = intent.getStringExtra("res_id")
            var restName = intent.getStringExtra("Rest_cart_name")
            sharedpreference.edit().putString("RestName", restName).apply()
            setSupportActionBar(toolbar)
            supportActionBar?.title = sharedpreference.getString("RestName", "Restaurants")
        } else {

            Toast.makeText(
                this@ResdetailsActivity,
                "Data not recieved",
                Toast.LENGTH_SHORT
            )
                .show()
            finish()
        }
        if (restItemId == "01") {
            Toast.makeText(this@ResdetailsActivity, "Rest Id not received", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
        if (ConnectionManager().checkConnection(this@ResdetailsActivity)) {
            var queue = Volley.newRequestQueue(this@ResdetailsActivity)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/${restItemId}"

            var jsonObjReq = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                try {
                    RelativeFoodItems.visibility = View.GONE

                    var data = it.getJSONObject("data")
                    var success = data.getBoolean("success")
                    if (success) {
                        var innerData = data.getJSONArray("data")
                        for (i in 0..innerData.length() - 1) {
                            var objectItems = innerData.getJSONObject(i)
                            var restObject = RestaurantItems(
                                objectItems.getString("id"),
                                objectItems.getString("name"),
                                objectItems.getString("cost_for_one"),
                                objectItems.getString("restaurant_id")
                            )
                            rstItems.add(restObject)
                        }
                        layoutmanager = LinearLayoutManager(this@ResdetailsActivity)
                        detailsRecyle.layoutManager = layoutmanager
                        resItemsAdapter = ResItemAdapter(this@ResdetailsActivity, rstItems)
                        detailsRecyle.adapter = resItemsAdapter

                        btn_CartAdd.setOnClickListener {
                            val intent = Intent(this@ResdetailsActivity, CartActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        //detailsRecyle.addItemDecoration(DividerItemDecoration(detailsRecyle.context,(layoutmanager as LinearLayoutManager).orientation))

                    }
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@ResdetailsActivity,
                        "Json Error Occured",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            },
                Response.ErrorListener {
                    if (this@ResdetailsActivity != null)
                        Toast.makeText(
                            this@ResdetailsActivity,
                            "Volley Error Occured",
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
            queue.add(jsonObjReq)
        } else {
            val dialog = AlertDialog.Builder(this@ResdetailsActivity)
            dialog.setTitle("OOPS!!")
            dialog.setMessage("No Internet Connection Found")
            dialog.setPositiveButton("Go to Settings") { text, Listener ->
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, Listener ->
                ActivityCompat.finishAffinity(this@ResdetailsActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    override fun onBackPressed() {
        if (!ButtonVisible(this@ResdetailsActivity).execute().get().isEmpty()) {
            val dialog = AlertDialog.Builder(this@ResdetailsActivity)
            dialog.setTitle("Alert!!")
            dialog.setMessage("Proceeding back will delete the item Added")
            dialog.setPositiveButton("Stay") { text, Listener ->

            }
            dialog.setNegativeButton("Clear the Cart") { text, Listener ->
                CartAdd(this@ResdetailsActivity, 2).execute().get()
                super.onBackPressed()
            }
            dialog.create()
            dialog.show()
        } else
            super.onBackPressed()
    }

}

class CartAdd(val context: Context, var mode: Int) : AsyncTask<Void, Void, Boolean>() {
    var db = Room.databaseBuilder(context, FoodDatabse::class.java, "food-db").build()
    override fun doInBackground(vararg p0: Void?): Boolean {
        when (mode) {
            1 -> {
                var btn_visible = db.foodDao().getAllFood()
                return btn_visible.size == 0
            }
            2 -> {
                var delete = db.foodDao().deleteOrders()
                return true
            }
        }
        return false
    }

}

class ButtonVisible(val context: Context) : AsyncTask<Void, Void, List<FoodEntity>>() {

    override fun doInBackground(vararg p0: Void?): List<FoodEntity> {
        var db = Room.databaseBuilder(context, FoodDatabse::class.java, "food-db").build()
        return db.foodDao().getAllFood()
    }

}




