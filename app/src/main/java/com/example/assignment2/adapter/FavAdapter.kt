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
import com.example.assignment2.database.RestaurantDatabase
import com.example.assignment2.database.RestaurantEnitity
import com.squareup.picasso.Picasso

class FavAdapter(var context: Context, var favList: List<RestaurantEnitity>) :
    RecyclerView.Adapter<FavAdapter.FavViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.favourites_items, parent, false)
        return FavViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        var FavList = favList[position]
        holder.txt_resName.text = FavList.RestuarantName
        holder.txtResPrice.text = FavList.cost_for_one
        holder.txtResRating.text = FavList.RestuarantRating
        Picasso.get().load(FavList.image).error(R.drawable.logo_app_2).into(holder.Img_resImage)
        holder.img_fav.setOnClickListener {
            deleteFavAsyncTask(context, FavList).execute().get()
            holder.img_fav.setImageResource(R.drawable.heart_outline)
            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show()
        }
        holder.linearOnclick.setOnClickListener {
            val intent = Intent(
                context,
                ResdetailsActivity::class.java
            )
            intent.putExtra("res_id", FavList.RestId.toString())
            intent.putExtra("Rest_cart_name", FavList.RestuarantName)
            context.startActivity(intent)
        }

    }

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var Img_resImage = view.findViewById<ImageView>(R.id.Img_resImage)
        var txtResPrice = view.findViewById<TextView>(R.id.txtResPrice)
        var txt_resName = view.findViewById<TextView>(R.id.txt_resName)
        var txtResRating = view.findViewById<TextView>(R.id.txtResRating)
        var linearOnclick = view.findViewById<LinearLayout>(R.id.linearOnclick)
        var img_fav = view.findViewById<ImageView>(R.id.img_fav)
    }

}

class deleteFavAsyncTask(context: Context, val restEntity: RestaurantEnitity) :
    AsyncTask<Void, Void, Boolean>() {
    var db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
    override fun doInBackground(vararg p0: Void?): Boolean {
        db.restaurantDao().deleteRestaurants(restEntity)
        db.close()
        return true
    }
}