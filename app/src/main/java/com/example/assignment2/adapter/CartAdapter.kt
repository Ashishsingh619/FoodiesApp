package com.example.assignment2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.R
import com.example.assignment2.database.FoodEntity

class CartAdapter(var context: Context, var cartItems: List<FoodEntity>) :
    RecyclerView.Adapter<CartAdapter.cartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_layout_item, parent, false)
        return cartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        var cartitems = cartItems[position]
        holder.txt_CartfoodName.text = cartitems.foodName
        holder.txt_cartFoodPrice.text = cartitems.cost_for_one
    }

    class cartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txt_CartfoodName = view.findViewById<TextView>(R.id.txt_CartfoodName)
        var txt_cartFoodPrice = view.findViewById<TextView>(R.id.txt_cartFoodPrice)
    }

}