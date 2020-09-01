package com.example.assignment2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.R
import com.example.assignment2.RestItemHistory
import com.example.assignment2.RestNameHistory
import com.example.assignment2.database.FoodEntity

class CartHisItemAdapter(var context: Context, var cartItems: List<RestItemHistory>) :
    RecyclerView.Adapter<CartHisItemAdapter.cartHisViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartHisViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_layout_item, parent, false)
        return cartHisViewHolder(view)

    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: cartHisViewHolder, position: Int) {
        var cartitems = cartItems[position]
        holder.txt_CartfoodName.text = cartitems.ItemName
        holder.txt_cartFoodPrice.text = cartitems.ItemCost
    }

    class cartHisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txt_CartfoodName = view.findViewById<TextView>(R.id.txt_CartfoodName)
        var txt_cartFoodPrice = view.findViewById<TextView>(R.id.txt_cartFoodPrice)
    }

}