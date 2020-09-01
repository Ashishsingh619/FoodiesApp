package com.example.assignment2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.R
import com.example.assignment2.RestItemHistory
import com.example.assignment2.RestNameHistory

class RestNameCartAdapter(var context: Context, var restList: List<RestNameHistory>) :
    RecyclerView.Adapter<RestNameCartAdapter.RestNameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestNameViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.rest_name_history, parent, false)
        return RestNameViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restList.size
    }

    override fun onBindViewHolder(holder: RestNameViewHolder, position: Int) {
        var restlist = restList[position]
        holder.txt_resHisName.text = restlist.RestName
        holder.txt_date.text = restlist.orderDateTime
        setUpRecycler(holder.recyclerResHistory, restlist)
    }

    class RestNameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txt_resHisName = view.findViewById<TextView>(R.id.txt_resHisName)
        var txt_date = view.findViewById<TextView>(R.id.txt_date)
        var recyclerResHistory = view.findViewById<RecyclerView>(R.id.recyclerResHistory)
    }

    private fun setUpRecycler(recyclerResHistory: RecyclerView, orderHistoryList: RestNameHistory) {
        val foodItemsList = ArrayList<RestItemHistory>()
        for (i in 0 until orderHistoryList.ResItems.length()) {
            val foodJson = orderHistoryList.ResItems.getJSONObject(i)
            foodItemsList.add(
                RestItemHistory(
                    foodJson.getString("food_item_id"),
                    foodJson.getString("name"),
                    foodJson.getString("cost")
                )
            )
        }
        val cartItemAdapter = CartHisItemAdapter(context, foodItemsList)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerResHistory.layoutManager = mLayoutManager
        recyclerResHistory.itemAnimator = DefaultItemAnimator()
        recyclerResHistory.adapter = cartItemAdapter
    }
}