package com.example.assignment2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    fun InsertFood(foodEntity: FoodEntity)

    @Delete
    fun DeleteFood(foodEntity: FoodEntity)

    @Query("SELECT * from Food Where food_Id=:FoodId")
    fun findFoodById(FoodId: String): FoodEntity

    @Query("SELECT * from Food")
    fun getAllFood(): List<FoodEntity>

    @Query("DELETE from Food")
    fun deleteOrders()

}