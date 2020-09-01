package com.example.assignment2.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEnitity::class], version = 1)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}