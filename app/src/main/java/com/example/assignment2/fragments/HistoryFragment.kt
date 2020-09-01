package com.example.assignment2.fragments


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import com.example.assignment2.RestNameHistory
import com.example.assignment2.adapter.RestNameCartAdapter
import com.example.assignment2.util.ConnectionManager
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var restNameAdapter: RestNameCartAdapter
    lateinit var recyclerOrderHistory: RecyclerView
    lateinit var sharedpreference: SharedPreferences
    var restHisName = arrayListOf<RestNameHistory>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_history2, container, false)
        sharedpreference = activity!!.getSharedPreferences(
            getString(R.string.shared_file_name),
            Context.MODE_PRIVATE
        )
        recyclerOrderHistory = view.findViewById(R.id.recyclerOrderHistory)
        var queue = Volley.newRequestQueue(activity as Context)
        var user_id = sharedpreference.getString("User_id", "99")
        val url = "http://13.235.250.119/v2/orders/fetch_result/${user_id}"
        if (ConnectionManager().checkConnection(activity as Context)) {
            var jsonObjReq = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                try {
                    var data = it.getJSONObject("data")
                    println(it)
                    if (data.getBoolean("success")) {
                        var innerData = data.getJSONArray("data")
                        println(innerData)
                        for (i in 0..innerData.length() - 1) {
                            var getJson = innerData.getJSONObject(i)
                            var innerArray = getJson.getJSONArray("food_items")
                            var restNameHistory = RestNameHistory(
                                getJson.getString("restaurant_name"),
                                getJson.getString("total_cost"),
                                getJson.getString("order_placed_at"),
                                innerArray
                            )
                            restHisName.add(restNameHistory)
                        }
                        if (activity != null) {
                            restNameAdapter = RestNameCartAdapter(activity as Context, restHisName)
                            layoutmanager = LinearLayoutManager(activity as Context)
                            recyclerOrderHistory.layoutManager = layoutmanager
                            recyclerOrderHistory.itemAnimator = DefaultItemAnimator()
                            recyclerOrderHistory.adapter = restNameAdapter
                        }


                    }

                } catch (e: JSONException) {
                    Toast.makeText(
                        activity as Context,
                        "Json error Occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
                Response.ErrorListener {
                    if (activity != null)
                        Toast.makeText(
                            activity as Context,
                            "Volley error Occured",
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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("OOPS!!")
            dialog.setMessage("No Internet Connection Found")
            dialog.setPositiveButton("Go to Settings") { text, Listener ->
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, Listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }


}
