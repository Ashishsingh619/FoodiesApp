package com.example.assignment2.fragments


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment2.R
import com.example.assignment2.adapter.RestaurantAdapter
import com.example.assignment2.Restaurants
import com.example.assignment2.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var homeadapter: RestaurantAdapter
    lateinit var recylcleview: RecyclerView
    lateinit var restCover: RelativeLayout
    val rstList = arrayListOf<Restaurants>()
    var ratingComparator = Comparator<Restaurants> { Restaurants1, Restaurants2 ->
        if (Restaurants1.RestuarantRating.compareTo(Restaurants2.RestuarantRating, true) == 0) {
            Restaurants1.RestuarantName.compareTo(Restaurants2.RestuarantName, true)
        } else
            Restaurants1.RestuarantRating.compareTo(Restaurants2.RestuarantRating, true)
    }
    var priceComparator = Comparator<Restaurants> { Restaurants1, Restaurants2 ->
        Restaurants1.cost_for_one.compareTo(Restaurants2.cost_for_one, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        recylcleview = view.findViewById(R.id.recyleview)
        restCover = view.findViewById(R.id.RestCover)
        restCover.visibility = View.VISIBLE

        var queue = Volley.newRequestQueue(activity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().checkConnection(activity as Context)) {
            var jsonObjReq =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        var data = it.getJSONObject("data")
                        if (data.getBoolean("success")) {
                            restCover.visibility = View.GONE
                            var dataArray = data.getJSONArray("data")
                            for (i in 0..dataArray.length() - 1) {
                                var jsonObj = dataArray.getJSONObject(i)
                                var resObj = Restaurants(
                                    jsonObj.getString("id"),
                                    jsonObj.getString("name"),
                                    jsonObj.getString("rating"),
                                    jsonObj.getString("cost_for_one"),
                                    jsonObj.getString("image_url")
                                )
                                rstList.add(resObj)
                            }
                            homeadapter =
                                RestaurantAdapter(
                                    activity as Context,
                                    rstList
                                )
                            layoutmanager = LinearLayoutManager(activity)
                            recylcleview.layoutManager = layoutmanager
                            recylcleview.adapter = homeadapter

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Data not recieved",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Json Error Occured",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                    , Response.ErrorListener {
                        if (activity != null)
                            Toast.makeText(
                                activity as Context,
                                "Volley error occured",
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.itm_sortRating) {
            Collections.sort(rstList, ratingComparator)
            rstList.reverse()
        }
        if (id == R.id.itm_sortPrice) {
            Collections.sort(rstList, priceComparator)
        }
        if (id == R.id.itm_sortPriceHigh) {
            Collections.sort(rstList, priceComparator)
            rstList.reverse()
        }
        homeadapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}


