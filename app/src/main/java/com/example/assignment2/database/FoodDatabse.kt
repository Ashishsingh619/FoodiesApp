package com.example.assignment2.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodEntity::class], version = 1)

abstract class FoodDatabse : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}