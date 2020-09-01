package com.example.assignment2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
class FoodEntity(
    @PrimaryKey var food_Id: Int,
    @ColumnInfo(name = "food_name") var foodName: String,
    @ColumnInfo(name = "food_cost") var cost_for_one: String,
    @ColumnInfo(name = "rest_Id") var restId: String
)