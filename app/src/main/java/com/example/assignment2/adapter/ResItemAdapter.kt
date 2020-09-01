package com.example.assignment2.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment2.R
import com.example.assignment2.RestaurantItems
import com.example.assignment2.database.FoodDatabse
import com.example.assignment2.database.FoodEntity

class ResItemAdapter(var context: Context, var restItem: ArrayList<RestaurantItems>) :
    RecyclerView.Adapter<ResItemAdapter.ResItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResItemViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.rest_list_view, parent, false)
        return ResItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restItem.size
    }

    override fun onBindViewHolder(holder: ResItemViewHolder, position: Int) {
        var res_Items = restItem[position]
        holder.txt_foodItem.text = res_Items.foodName
        holder.txt_foodPrice.text = res_Items.cost_for_one
        val foodEntity = FoodEntity(
            res_Items.foodId?.toInt() as Int,
            res_Items.foodName,
            res_Items.cost_for_one,
            res_Items.restId
        )
        holder.btn_add.setOnClickListener {

            if (!foodSyncTask(context, foodEntity, 1).execute().get()) {
                var add = foodSyncTask(context, foodEntity, 2).execute()
                if (add.get()) {
                    holder.btn_add.text = "REMOVE"
                    val favcolor = ContextCompat.getColor(
                        context,
                        R.color.new_Btn
                    )
                    holder.btn_add.setBackgroundColor(favcolor)
                }
            } else {
                var remove = foodSyncTask(context, foodEntity, 3).execute()
                if (remove.get()) {
                    holder.btn_add.text = "ADD"
                    val favcolor = ContextCompat.getColor(
                        context,
                        R.color.app_background_color
                    )
                    holder.btn_add.setBackgroundColor(favcolor)
                }
            }
        }
    }

    class ResItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txt_foodItem = view.findViewById<TextView>(R.id.txt_foodItem)
        var txt_foodPrice = view.findViewById<TextView>(R.id.txt_foodPrice)
        var btn_add = view.findViewById<Button>(R.id.btn_add)
    }

    class foodSyncTask(val context: Context, var foodEntity: FoodEntity, var mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        var db = Room.databaseBuilder(context, FoodDatabse::class.java, "food-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    var food = db.foodDao().findFoodById(foodEntity.food_Id.toString())
                    db.close()
                    return food != null
                }
                2 -> {
                    db.foodDao().InsertFood(foodEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.foodDao().DeleteFood(foodEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}
