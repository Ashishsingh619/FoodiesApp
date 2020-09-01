package com.example.assignment2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
data class RestaurantEnitity(
    @PrimaryKey var RestId: Int,
    @ColumnInfo(name = "Rest_name") var RestuarantName: String,
    @ColumnInfo(name = "Rest_Rating") var RestuarantRating: String,
    @ColumnInfo(name = "Rest_cost") var cost_for_one: String,
    @ColumnInfo(name = "Res_Url") var image: String
)
