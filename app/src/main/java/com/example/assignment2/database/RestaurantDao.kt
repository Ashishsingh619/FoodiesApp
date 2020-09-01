package com.example.assignment2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun addRestaurant(restaurantEntity: RestaurantEnitity)

    @Delete
    fun deleteRestaurants(restaurantEntity: RestaurantEnitity)

    @Query("SELECT * from Restaurants where RestId=:RestName_Id")
    fun getRestaurantById(RestName_Id: String): RestaurantEnitity

    @Query("SElECT * from Restaurants")
    fun getRestaurantsFav(): List<RestaurantEnitity>
}