package com.example.assignment2.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.assignment2.R
import com.example.assignment2.activity.ResdetailsActivity
import com.example.assignment2.Restaurants
import com.example.assignment2.database.RestaurantDatabase
import com.example.assignment2.database.RestaurantEnitity
import com.squareup.picasso.Picasso

class RestaurantAdapter(var context: Context, var ItemList: ArrayList<Restaurants>) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.restuarant_items, parent, false)
        return RestaurantViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return ItemList.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        var list = ItemList[position]
        holder.txt_resName.text = list.RestuarantName
        holder.txtResPrice.text = list.cost_for_one
        holder.txtResRating.text = list.RestuarantRating
        Picasso.get().load(list.image).error(R.drawable.logo_app_2).into(holder.Img_resImage)
        var restaurantEntity = RestaurantEnitity(
            list.RestId.toInt() as Int,
            list.RestuarantName,
            list.RestuarantRating,
            list.cost_for_one,
            list.image
        )
        if (dbasyncTask(context, restaurantEntity, 1).execute().get()) {
            holder.img_fav.setImageResource(R.drawable.heart_filled)
        } else
            holder.img_fav.setImageResource(R.drawable.heart_outline)
        holder.img_fav.setOnClickListener {
            if (dbasyncTask(context, restaurantEntity, 1).execute().get()) {
                var remove = dbasyncTask(context, restaurantEntity, 3).execute().get()
                if (remove) {
                    Toast.makeText(context, "Removed to Favourites", Toast.LENGTH_SHORT).show()
                    holder.img_fav.setImageResource(R.drawable.heart_outline)
                } else {
                    Toast.makeText(context, "There was a problem removing", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                var add = dbasyncTask(context, restaurantEntity, 2).execute().get()
                if (add) {
                    Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show()
                    holder.img_fav.setImageResource(R.drawable.heart_filled)
                } else
                    Toast.makeText(context, "There was a problem Adding", Toast.LENGTH_SHORT).show()
            }


        }
        holder.linearOnclick.setOnClickListener {
            val intent = Intent(
                context,
                ResdetailsActivity::class.java
            )
            intent.putExtra("res_id", list.RestId)
            intent.putExtra("Rest_cart_name", list.RestuarantName)
            context.startActivity(intent)

        }

    }

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var Img_resImage = view.findViewById<ImageView>(R.id.Img_resImage)
        var txtResPrice = view.findViewById<TextView>(R.id.txtResPrice)
        var txt_resName = view.findViewById<TextView>(R.id.txt_resName)
        var txtResRating = view.findViewById<TextView>(R.id.txtResRating)
        var linearOnclick = view.findViewById<LinearLayout>(R.id.linearOnclick)
        var img_fav = view.findViewById<ImageView>(R.id.img_fav)
    }
}

class dbasyncTask(context: Context, val restEntity: RestaurantEnitity, var mode: Int) :
    AsyncTask<Void, Void, Boolean>() {
    var db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
    override fun doInBackground(vararg p0: Void?): Boolean {
        when (mode) {
            1 -> {
                val book = db.restaurantDao().getRestaurantById(restEntity.RestId.toString())
                db.close()
                return book != null
            }
            2 -> {
                db.restaurantDao().addRestaurant(restEntity)
                db.close()
                return true
            }
            3 -> {
                db.restaurantDao().deleteRestaurants(restEntity)
                db.close()
                return true
            }

        }
        return false
    }

}
